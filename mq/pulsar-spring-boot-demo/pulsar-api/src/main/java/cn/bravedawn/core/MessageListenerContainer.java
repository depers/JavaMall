package cn.bravedawn.core;

import cn.bravedawn.config.PulsarProperties;
import cn.hutool.core.net.NetUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.SubscriptionInitialPosition;
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
                pulsarProperties.getTopics().forEach(topic -> {
                    PulsarProperties.ListenProperties listenConfig = topic.getListenConfig();
                    for (int i = 0; i < listenConfig.getConsumerNum(); i++) {
                        try {
                            Consumer<PulsarMessage> pulsarMessageConsumer = pulsarClientWrapper.getPulsarClient().newConsumer(JSONSchema.of(PulsarMessage.class))
                                    .topic(topic.getTopicName())
                                    .subscriptionName(topic.getTopicPrefix() + "-listen-" + NetUtil.getLocalhostStr() + "-" + i+1)
                                    .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
                                    .messageListener((consume, msg) -> {
                                        try {
                                            System.out.println("收到消息: " + new String(msg.getData()));
                                            blockingQueueConsumerMap.get(topic.getTopicPrefix()).handleConsumer(msg.getValue());
                                            consume.acknowledge(msg);
                                        } catch (Exception e) {
                                            blockingQueueConsumerMap.get(topic.getTopicPrefix()).exceptionHandle(e);
                                            consume.negativeAcknowledge(msg);
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
