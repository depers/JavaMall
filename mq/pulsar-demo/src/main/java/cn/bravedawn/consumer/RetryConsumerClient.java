package cn.bravedawn.consumer;

import cn.bravedawn.schema.DemoData;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.schema.JSONSchema;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-demo
 * @Date : Created in 2025-07-16 17:16
 */

@Slf4j
public class RetryConsumerClient {


    public static void main(String[] args) throws PulsarClientException {
        PulsarClient client = PulsarClient.builder()
                .serviceUrl("pulsar://192.168.133.128:6650")
                // 添加以下配置
                .build();

        DeadLetterProducerBuilderCustomizer producerBuilderCustomizer = (context, producerBuilder) -> {
            producerBuilder.enableBatching(true);
            producerBuilder.enableChunking(false);
        };

        Consumer<DemoData> consumer = client.newConsumer(JSONSchema.of(DemoData.class))
                .topic("persistent://public/siis/partitionedTopic")
                .subscriptionName("my-subscription")
                .subscriptionType(SubscriptionType.Shared)
                .subscriptionMode(SubscriptionMode.Durable)
                .enableBatchIndexAcknowledgment(true)
                .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
                // 开启消息重试
                .enableRetry(true)
                // 构建死信队列策略
                .deadLetterPolicy(DeadLetterPolicy.builder()
                        // 最大重试投递次数，也就是如果第一次消费失败，还会重新再投递一次
                        .maxRedeliverCount(1)
                        // 死信主题
                        .deadLetterTopic("persistent://public/siis/partitionedTopic-deadLetter")
                        // 重试主题
                        .retryLetterTopic("persistent://public/siis/partitionedTopic-retryLetter")
                        // 配置重试信件主题的生产者
                        .retryLetterProducerBuilderCustomizer(producerBuilderCustomizer)
                        .deadLetterProducerBuilderCustomizer(producerBuilderCustomizer)
                        .build())
                .messageListener((consumer1, msg) -> {
                    try {
                        log.info("收到消息: {}, sequenceId={}, property-sequenceId={}",
                                new String(msg.getData()), msg.getSequenceId(), msg.getProperties().get("ORIGIN_MESSAGE_ID"));
                        int i = 1 / 0;
                        consumer1.acknowledge(msg);
                    } catch (Exception e) {
                        log.error("消息消费出现异常", e);
                        // 否定确认
                        consumer1.negativeAcknowledge(msg);

//                        // 重新消费
//                        try {
//                            Map<String, String> customProperties = new HashMap<String, String>();
//                            customProperties.put("ORIGIN_MESSAGE_ID", String.valueOf(msg.getSequenceId()));
//                            consumer1.reconsumeLater(msg, customProperties, 5, TimeUnit.SECONDS);
//                        } catch (PulsarClientException ex) {
//                            throw new RuntimeException(ex);
//                        }
                    }
                })
                .subscribe();

        // 保持消费者运行
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                consumer.close();
                client.close();
            } catch (PulsarClientException e) {
                e.printStackTrace();
            }
        }));
    }

}
