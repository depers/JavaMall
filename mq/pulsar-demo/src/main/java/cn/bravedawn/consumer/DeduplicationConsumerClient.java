package cn.bravedawn.consumer;

import cn.bravedawn.schema.DemoData;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.SubscriptionInitialPosition;
import org.apache.pulsar.client.impl.schema.JSONSchema;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-demo
 * @Date : Created in 2025-07-15 10:13
 */

@Slf4j
public class DeduplicationConsumerClient {



    public static void main(String[] args) throws PulsarClientException {
        PulsarClient client = PulsarClient.builder()
                .serviceUrl("pulsar://192.168.133.128:6650")
                // 添加以下配置
                .build();

        Consumer<DemoData> consumer = client.newConsumer(JSONSchema.of(DemoData.class))
                .topic("persistent://public/siis/partitionedTopic")
                .subscriptionName("my-subscription")
                .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
                .messageListener((consumer1, msg) -> {
                    try {
                        log.info("收到消息: {}, sequenceId={}", new String(msg.getData()), msg.getSequenceId());
                        consumer1.acknowledge(msg);
                    } catch (Exception e) {
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
