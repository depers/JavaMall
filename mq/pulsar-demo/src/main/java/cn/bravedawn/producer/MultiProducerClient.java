package cn.bravedawn.producer;

import cn.bravedawn.schema.DemoData;
import cn.hutool.core.lang.Snowflake;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.schema.JSONSchema;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-demo
 * @Date : Created in 2025-07-07 15:36
 */
@Slf4j
@Component
public class MultiProducerClient {


    public static void main(String[] args) throws PulsarClientException, ExecutionException, InterruptedException {
        // 定义客户端
        PulsarClient pulsarClient = PulsarClient.builder()
                .serviceUrl("pulsar://192.168.133.128:6650")
                .ioThreads(1)
                .listenerThreads(1)
                .build();

        // 定义模式
        JSONSchema<DemoData> jsonSchema = JSONSchema.of(DemoData.class);

        // 定义生产者
        Producer<DemoData> producer = pulsarClient
                .newProducer(jsonSchema)
                // 访问模式
                .accessMode(ProducerAccessMode.Shared)
                // 路由模式
                .messageRoutingMode(MessageRoutingMode.RoundRobinPartition)
                .topic("persistent://public/siis/partitionedTopic").create();



        // 发送消息
        for (int i = 0; i < 6; i++) {
            Snowflake snowflake = new Snowflake();
            DemoData data = new DemoData();
            data.setId(Long.valueOf(i));
            data.setName("test");

            producer.newMessage()
                    .value(data)
//                    .key(String.valueOf(i))
                    .sequenceId(snowflake.nextId())
                    .sendAsync()
                    .thenApply(messageId -> {
                        log.info("消息发送成功，{}, data={}", messageId, data);
                        return messageId;
                    }).exceptionally(e -> {
                        log.error("消息发送失败", e);
                        return null;
                    }).get();
        }

        // 关闭客户端和生产者
        producer.close();
        pulsarClient.close();
    }
}
