package cn.bravedawn.core;

import cn.bravedawn.config.PulsarProperties;
import cn.bravedawn.contant.SchemaDefinitionConfig;
import cn.bravedawn.interceptor.ConsumeIdempotentInterceptor;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.schema.JSONSchema;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.support.ExecutorServiceAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-22 15:45
 */

@Slf4j
public class MessageListenerContainer implements InitializingBean, DisposableBean {

    private volatile boolean isInitialized = false;
    protected final Lock lifecycleLock = new ReentrantLock();
    private final PulsarProperties pulsarProperties;
    private final PulsarClientWrapper pulsarClientWrapper;
    private final List<AbstractBlockingQueueConsumer> blockingQueueConsumers;
    private final Map<String, AbstractBlockingQueueConsumer> blockingQueueConsumerMap;
    private final Map<String, List<Consumer<PulsarMessage>>> consumersMap;
    private final AbstractDeadLetterBlockingQueueConsumer deadLetterBlockingQueueConsumer;
    private Consumer<PulsarMessage> deadLetterConsumer;
    private final ConsumeIdempotentInterceptor consumeIdempotentInterceptor = new ConsumeIdempotentInterceptor();

    public MessageListenerContainer(PulsarClientWrapper pulsarClientWrapper, PulsarProperties pulsarProperties,
                                    List<AbstractBlockingQueueConsumer> blockingQueueConsumers, AbstractDeadLetterBlockingQueueConsumer deadLetterBlockingQueueConsumer) {
        this.pulsarClientWrapper = pulsarClientWrapper;
        this.pulsarProperties = pulsarProperties;
        this.blockingQueueConsumers = blockingQueueConsumers;
        this.blockingQueueConsumerMap = blockingQueueConsumers.stream().collect(Collectors.toMap(AbstractBlockingQueueConsumer::getTopicPrefix, Function.identity()));
        this.consumersMap = new HashMap<>();
        this.deadLetterBlockingQueueConsumer = deadLetterBlockingQueueConsumer;
    }

    public void start() {
        lifecycleLock.lock();
        try {
            if (!isInitialized) {
                pulsarProperties.getTopics().forEach(topic -> {
                    PulsarProperties.ListenProperties listenConfig = topic.getListenConfig();
                    for (int i = 1; i <= listenConfig.getConsumerNum(); i++) {
                        createConsumer(topic.getTopicPrefix(), topic.getTopicName(), listenConfig, false);
                    }
                });

                pulsarProperties.getPriorityQueue().forEach(topic -> {
                    PulsarProperties.ListenProperties listenConfig = topic.getListenConfig();
                    for (int i = 1; i <= listenConfig.getConsumerNum(); i++) {
                        createConsumer(topic.getTopicPrefix(), topic.getTopicName(), listenConfig, true);
                    }
                });

                createDeadLetterConsumer();

                isInitialized = true;
            }
        } finally {
            lifecycleLock.unlock();
        }
    }


    private void createConsumer(String topicPrefix, String topicName, PulsarProperties.ListenProperties listenConfig, boolean isPriority) {
        try {
            String originTopicPrefix = topicPrefix;
            if (isPriority) {
                topicPrefix = topicPrefix + "-priority";
            }
            Consumer<PulsarMessage> pulsarMessageConsumer = pulsarClientWrapper.getPulsarClient()
                    .newConsumer(JSONSchema.of(SchemaDefinitionConfig.DEFAULT_SCHEMA))
                    .topic(topicName)
                    .subscriptionType(SubscriptionType.Shared)
                    .subscriptionMode(SubscriptionMode.Durable)
                    .subscriptionName(topicPrefix + "-subscription")
                    .consumerName(topicPrefix + "-listener-" + NetUtil.getLocalhostStr() + "-" + RandomUtil.randomString(4))
                    .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
                    // 开启消息重试
                    .enableRetry(listenConfig.isEnableRetry())
                    // 构建死信队列策略
                    .deadLetterPolicy(DeadLetterPolicy.builder()
                            // 最大重试投递次数，也就是如果第一次消费失败，还会重新再投递一次
                            .maxRedeliverCount(listenConfig.getMaxRetryCount())
                            // 死信主题
                            .deadLetterTopic(listenConfig.getDeadLetterTopicName())
                            // 重试主题
                            .retryLetterTopic(listenConfig.getRetryLetterTopicName())
                            .build())
                    .intercept(consumeIdempotentInterceptor)
                    .messageListener((consume, msg) -> {
                        String msgStr = new String(msg.getData());
                        try {
                            int retryCount = 1;
                            if (msg.getProperties().get("RECONSUMETIMES") != null) {
                                retryCount = Integer.parseInt(msg.getProperties().get("RECONSUMETIMES")) + 1;
                            }

                            log.info("第{}次，收到消息: {}", retryCount, msgStr);
                            blockingQueueConsumerMap.get(originTopicPrefix).handleConsumer(msg.getValue());
                            consume.acknowledge(msg);
                        } catch (Throwable e) {
                            blockingQueueConsumerMap.get(originTopicPrefix).exceptionHandle(e);
                            if (listenConfig.isEnableRetry()) {

                                // 获取属性，这个属性map是不可修改的，如果要添加自己的属性，需要新建一个map放到参数里面，pulsar会将该map拼接到properties
                                Map<String, String> properties = msg.getProperties();
                                log.info("消息的属性：{}", properties);

                                log.info("消息重试已开启，开始重试消费该消息。msg={}", msgStr);
                                try {
                                    consume.reconsumeLater(msg, listenConfig.getRetryDelayTime(), TimeUnit.MILLISECONDS);
                                } catch (PulsarClientException ex) {
                                    log.error("将消息投递到重试队列异常", ex);
                                }
                            } else {
                                log.info("消息重试已关闭，不再重试消费该消息，确认消费。msg={}", msgStr);
                                try {
                                    consume.acknowledge(msg);
                                } catch (PulsarClientException ex) {
                                    log.info("消息重试，确认消息异常", e);
                                }
                            }
                        }
                    })
                    .subscribe();
            if (consumersMap.get(topicPrefix) == null) {
                List<Consumer<PulsarMessage>> consumerList = new ArrayList<>();
                consumerList.add(pulsarMessageConsumer);
                consumersMap.put(topicPrefix, consumerList);
            } else {
                consumersMap.get(topicPrefix).add(pulsarMessageConsumer);
            }
        } catch (PulsarClientException e) {
            log.error("创建消费者出现异常，topic={}", topicName);
            log.error("创建消费者出现异常", e);
        }
    }
    private void createDeadLetterConsumer() {
        try {
            // 创建死信队列逻辑
            deadLetterConsumer = pulsarClientWrapper.getPulsarClient()
                    .newConsumer(JSONSchema.of(SchemaDefinitionConfig.DEFAULT_SCHEMA))
                    .topicsPattern(deadLetterBlockingQueueConsumer.getTopicPattern())
                    .subscriptionType(SubscriptionType.Shared)
                    .subscriptionMode(SubscriptionMode.Durable)
                    .subscriptionName("dead-letter-subscription")
                    .consumerName("dead-letter" + "-listener-" + NetUtil.getLocalhostStr() + "-" + RandomUtil.randomString(4))
                    .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
                    .messageListener((consumer, msg) -> {
                        String msgStr = new String(msg.getData());
                        try {
                            log.info("收到死信消息: {}", msgStr );
                            deadLetterBlockingQueueConsumer.handleConsumer(msg.getValue());
                        } catch (Throwable e) {
                            log.error("处理死信消息异常", e);
                        } finally {
                            try {
                                consumer.acknowledge(msg);
                            } catch (PulsarClientException e) {
                                log.error("确认死信消息异常", e);
                            }
                        }
                    })
                    .subscribe();
        } catch (Throwable e) {
            log.error("创建死信消费者异常", e);
        }
    }

    public void unload() {
        // 关闭线程池
        consumersMap.forEach((topic, consumers) -> {
            consumers.forEach(c -> {
                try {
                    c.close();
                } catch (PulsarClientException e) {
                    log.error("关闭pulsar消费者异常", e);
                }
            });
            log.info("{}主题前缀的消费者已关闭", topic);
        });

        try {
            deadLetterConsumer.close();
        } catch (PulsarClientException e){
            log.error("关闭死信队列消费者异常", e);
        }
    }

    @Override
    public void destroy() throws Exception {
        unload();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }
}
