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
    private Map<String, ExecutorService> executorMap = new ConcurrentHashMap<>();
    private PulsarProperties pulsarProperties;
    private PulsarClientWrapper pulsarClientWrapper;
    private List<BlockingQueueConsumer> blockingQueueConsumers;
    private Map<String, BlockingQueueConsumer> blockingQueueConsumerMap;

    public MessageListenerContainer(PulsarClientWrapper pulsarClientWrapper, PulsarProperties pulsarProperties, List<BlockingQueueConsumer> blockingQueueConsumers) {
        this.pulsarClientWrapper = pulsarClientWrapper;
        this.pulsarProperties = pulsarProperties;
        this.blockingQueueConsumers = blockingQueueConsumers;
        this.blockingQueueConsumerMap = blockingQueueConsumers.stream().collect(Collectors.toMap(BlockingQueueConsumer::getTopicPrefix, Function.identity()));
    }

    public void start() {
        lifecycleLock.lock();
        try {
            if (!isInitialized) {
                pulsarProperties.getTopics().forEach(topic -> {
                    PulsarProperties.ListenProperties listenConfig = topic.getListenConfig();
                    ExecutorService executorService = new ThreadPoolExecutor(listenConfig.getWorkThreadNum(),
                            listenConfig.getWorkThreadNum(), 60L, TimeUnit.SECONDS,
                            new ArrayBlockingQueue<>(1), new CustomThreadFactory(topic.getTopicPrefix() + "-listen"),
                            new ThreadPoolExecutor.DiscardPolicy());

                    for (int i = 0; i < listenConfig.getWorkThreadNum(); i++) {
                        executorService.submit(new AsyncMessageProcessingConsumer(blockingQueueConsumerMap.get(topic.getTopicPrefix()), topic.getTopicName(), topic.getTopicPrefix(), listenConfig));
                    }
                    executorMap.put(topic.getTopicPrefix(), executorService);
                });

                isInitialized = true;
            }
        } finally {
            lifecycleLock.unlock();
        }
    }

    public void unload() {
        // 关闭线程池
        blockingQueueConsumerMap.forEach((topic, consumer) -> {
            consumer.getConsumerList().forEach(c -> {
                try {
                    c.close();
                } catch (PulsarClientException e) {
                    log.error("关闭pulsar消费者异常", e);
                }
            });
            log.info("{}主题的消费者已关闭", topic);
        });

        executorMap.forEach((topic, executor) -> {
            executor.shutdown();
            log.info("{}主题的线程池已关闭", topic);
        });
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }


    private final class AsyncMessageProcessingConsumer implements Runnable {

        private BlockingQueueConsumer blockingQueueConsumer;
        private String topicName;
        private String subscriptionNamePrefix;
        private int maxRetryCount;
        private String retryLetterTopicName;
        private String deadLetterTopicName;
        private AtomicInteger serialNo = new AtomicInteger(0);


        public AsyncMessageProcessingConsumer(BlockingQueueConsumer consumer, String topicName, String topicPrefix, PulsarProperties.ListenProperties listenConfig) {
            this.blockingQueueConsumer = consumer;
            this.topicName = topicName;
            this.subscriptionNamePrefix = topicPrefix;
            this.maxRetryCount = listenConfig.getMaxRetryCount();
            this.deadLetterTopicName = listenConfig.getDeadLetterTopicName();
            this.retryLetterTopicName = listenConfig.getRetryLetterTopicName();
        }

        @Override
        public void run() {
            try {
                Consumer<PulsarMessage> pulsarMessageConsumer = pulsarClientWrapper.getPulsarClient().newConsumer(JSONSchema.of(PulsarMessage.class))
                        .topic(topicName)
                        .subscriptionName(subscriptionNamePrefix + "-listen-" + NetUtil.getLocalhostStr() + "-" + serialNo.incrementAndGet())
                        .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
                        .messageListener((consume, msg) -> {
                            try {
                                System.out.println("收到消息: " + new String(msg.getData()));
                                blockingQueueConsumer.handleConsumer(msg.getValue());
                                consume.acknowledge(msg);
                            } catch (Exception e) {
                                blockingQueueConsumer.exceptionHandle(e);
                                consume.negativeAcknowledge(msg);
                            }
                        })
                        .subscribe();
                blockingQueueConsumer.getConsumerList().add(pulsarMessageConsumer);
            } catch (PulsarClientException e) {
                log.error("pulsar消费端出现异常", e);
            }
        }
    }
}
