package cn.bravedawn.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-demo
 * @Date : Created in 2025-07-07 15:36
 */
@Slf4j
@Component
public class ProducerClient {



    public boolean sendMsg() throws PulsarClientException {
        PulsarClient pulsarClient = PulsarClient.builder()
                .serviceUrl("pulsar://localhost:6650")
                .ioThreads(1)
                .listenerThreads(1)
                .build();

        Producer<byte[]> producer = pulsarClient.newProducer().topic("my-topic").create();
        CompletableFuture<MessageId> messageIdCompletableFuture = producer.sendAsync("你好，我是生产者".getBytes(StandardCharsets.UTF_8));
//        messageIdCompletableFuture.thenApply(msgId -> {
//
//        })
        return true;
    }
}
