package cn.bravedawn.consumer;

import cn.bravedawn.schema.DemoData;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.schema.JSONSchema;

import java.util.regex.Pattern;

@Slf4j
public class PatternRetryDeadLetterConsumerClient {


    public static void main(String[] args) throws PulsarClientException {
        PulsarClient client = PulsarClient.builder()
                .serviceUrl("pulsar://192.168.24.128:6650")
                // 添加以下配置
                .build();

        Consumer<DemoData> consumer = client.newConsumer(JSONSchema.of(DemoData.class))
                .topicsPattern(Pattern.compile("persistent://public/siis/partitionedTopic-.*"))
                .subscriptionName("my-subscription")
                .subscriptionType(SubscriptionType.Shared)
                .subscriptionMode(SubscriptionMode.Durable)
                .enableBatchIndexAcknowledgment(true)
                .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
                .messageListener((consumer1, msg) -> {
                    try {
                        log.info("收到死信消息: {}, sequenceId={}, property-sequenceId={}",
                                new String(msg.getData()), msg.getSequenceId(), msg.getProperties().get("ORIGIN_MESSAGE_ID"));
                        consumer1.acknowledge(msg);
                    } catch (Exception e) {
                        log.error("死信消息消费异常");
                        consumer1.negativeAcknowledge(msg);
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
