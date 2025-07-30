package cn.bravedawn.core;

import cn.bravedawn.config.PulsarProperties;
import cn.bravedawn.contant.SchemaDefinitionConfig;
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
    private PulsarProperties pulsarProperties;
    private PulsarClientWrapper pulsarClientWrapper;
    private List<AbstractBlockingQueueConsumer> blockingQueueConsumers;
    private Map<String, AbstractBlockingQueueConsumer> blockingQueueConsumerMap;
    private Map<String, List<Consumer<PulsarMessage>>> consumersMap;

    public MessageListenerContainer(PulsarClientWrapper pulsarClientWrapper, PulsarProperties pulsarProperties, List<AbstractBlockingQueueConsumer> blockingQueueConsumers) {
        this.pulsarClientWrapper = pulsarClientWrapper;
        this.pulsarProperties = pulsarProperties;
        this.blockingQueueConsumers = blockingQueueConsumers;
        this.blockingQueueConsumerMap = blockingQueueConsumers.stream().collect(Collectors.toMap(AbstractBlockingQueueConsumer::getTopicPrefix, Function.identity()));
        this.consumersMap = new HashMap<>();
    }

    public void start() {
        lifecycleLock.lock();
        try {
            if (!isInitialized) {
                DeadLetterProducerBuilderCustomizer producerBuilderCustomizer = (context, producerBuilder) -> {
                    producerBuilder.enableBatching(true);
                    producerBuilder.enableChunking(false);
                };

                pulsarProperties.getTopics().forEach(topic -> {
                    PulsarProperties.ListenProperties listenConfig = topic.getListenConfig();
                    for (int i = 1; i <= listenConfig.getConsumerNum(); i++) {
                        try {
                            Consumer<PulsarMessage> pulsarMessageConsumer = pulsarClientWrapper.getPulsarClient()
                                    .newConsumer(JSONSchema.of(SchemaDefinitionConfig.DEFAULT_SCHEMA))
                                    .topic(topic.getTopicName())
                                    .subscriptionType(SubscriptionType.Shared)
                                    .subscriptionMode(SubscriptionMode.Durable)
                                    .subscriptionName(topic.getTopicPrefix() + "-subscription")
                                    .consumerName(topic.getTopicPrefix() + "-listener-" + NetUtil.getLocalhostStr() + "-" + RandomUtil.randomString(4))
                                    .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
                                    // 开启消息重试
                                    .enableRetry(listenConfig.isEnableRetry())
                                    // 构建死信队列策略
                                    .deadLetterPolicy(DeadLetterPolicy.builder()
                                            // 最大重试投递次数，也就是如果第一次消费失败，还会重新再投递一次
                                            .maxRedeliverCount(listenConfig.getMaxRetryCount())
                                            // 死信主题
                                            .deadLetterTopic(listenConfig.getRetryLetterTopicName())
                                            // 重试主题
                                            .retryLetterTopic(listenConfig.getDeadLetterTopicName())
                                            // 配置重试信件主题的生产者
                                            .retryLetterProducerBuilderCustomizer(producerBuilderCustomizer)
                                            .deadLetterProducerBuilderCustomizer(producerBuilderCustomizer)
                                            .build())
                                    .messageListener((consume, msg) -> {
                                        String msgStr = new String(msg.getData());
                                        try {
                                            log.info("收到消息: {}", msgStr );
                                            blockingQueueConsumerMap.get(topic.getTopicPrefix()).handleConsumer(msg.getValue());
                                            consume.acknowledge(msg);
                                        } catch (Exception e) {
                                            blockingQueueConsumerMap.get(topic.getTopicPrefix()).exceptionHandle(e);
                                            if (listenConfig.isEnableRetry()) {
                                                log.info("消息重试已开启，重试消费该消息。msg={}", msgStr);
                                                try {
                                                    consume.reconsumeLater(msg, listenConfig.getRetryDelayTime(), TimeUnit.MILLISECONDS);
                                                } catch (PulsarClientException ex) {
                                                    log.error("将消息投递到重试队列异常", e);
                                                }
                                            } else {
                                                log.info("消息重试已关闭，不再重试消费该消息，确认消费。msg={}", msgStr);
                                            }

                                        }
                                    })
                                    .subscribe();
                            if (consumersMap.get(topic) == null) {
                                List<Consumer<PulsarMessage>> consumerList = new ArrayList<>();
                                consumerList.add(pulsarMessageConsumer);
                                consumersMap.put(topic.getTopicPrefix(), consumerList);
                            } else {
                                consumersMap.get(topic.getTopicPrefix()).add(pulsarMessageConsumer);
                            }
                        } catch (PulsarClientException e) {
                            log.error("创建消费者出现异常，topic={}", topic.getTopicName());
                            log.error("创建消费者出现异常", e);
                        }
                    }
                });

                isInitialized = true;
            }
        } finally {
            lifecycleLock.unlock();
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
            log.info("{}主题的消费者已关闭", topic);
        });

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
