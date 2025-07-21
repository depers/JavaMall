package cn.bravedawn.core;

import cn.bravedawn.config.PulsarProperties;
import cn.hutool.core.net.NetUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.MessageRoutingMode;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.ProducerAccessMode;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.impl.schema.JSONSchema;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-21 18:23
 */
@Slf4j
@Component
@AllArgsConstructor
public class PulsarTemplate implements InitializingBean, DisposableBean {

    private final PulsarClientWrapper pulsarClientWrapper;
    private final PulsarProperties pulsarProperties;
    private final Map<String, Producer<PulsarMessage>> producerMap = new HashMap<>();


    @Override
    public void destroy() throws Exception {
        for (Producer<PulsarMessage> producer : producerMap.values()) {
            producer.close();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 定义模式
        JSONSchema<PulsarMessage> jsonSchema = JSONSchema.of(PulsarMessage.class);
        for (PulsarProperties.TopicProperties topic : pulsarProperties.getTopics()) {
            Producer<PulsarMessage> producer = pulsarClientWrapper.getPulsarClient()
                    .newProducer(jsonSchema)
                    .producerName(topic.getTopicPrefix() + "_" + NetUtil.getLocalhostStr())
                    // 访问模式
                    .accessMode(ProducerAccessMode.Shared)
                    // 路由模式
                    .messageRoutingMode(MessageRoutingMode.RoundRobinPartition)
                    .topic(topic.getTopicName())
                    .enableBatching(true)
                    .batchingMaxBytes(512 * 1024)
                    .batchingMaxPublishDelay(50, TimeUnit.MILLISECONDS)
                    .batchingMaxMessages(500)
                    .create();
            producerMap.put(topic.getTopicPrefix(), producer);
        }
    }

    public void sendMessage(PulsarMessage pulsarMessage) {
        // 判断当前消息是否为新消息，若是需要入库

        // 如果不是新消息更新发送时间和次数

        producerMap.get(pulsarMessage.getTopic())
                .newMessage()
                .value(pulsarMessage)
                .sequenceId(pulsarMessage.getMessageId())
                .sendAsync()
                .thenApply(messageId -> {
                    // 这里需要同步更新数据库
                    log.info("消息发送成功，pulsarMessage={}", pulsarMessage);
                    return messageId;
                }).exceptionally(e -> {
                    log.error("消息发送失败", e);
                    return null;
                });
    }
}
