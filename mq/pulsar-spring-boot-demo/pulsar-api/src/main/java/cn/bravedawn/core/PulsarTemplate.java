package cn.bravedawn.core;

import cn.bravedawn.config.PulsarProperties;
import cn.bravedawn.contant.Constant;
import cn.bravedawn.contant.MessageStatus;
import cn.bravedawn.dao.MqRecordMapper;
import cn.bravedawn.interceptor.SendSuccessInterceptor;
import cn.bravedawn.toolkit.ApplicationContextHolder;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.pulsar.client.api.MessageRoutingMode;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.ProducerAccessMode;
import org.apache.pulsar.client.impl.schema.JSONSchema;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Date;
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
public class PulsarTemplate implements InitializingBean, DisposableBean {

    private final PulsarClientWrapper pulsarClientWrapper;
    private final PulsarProperties pulsarProperties;
    private final Map<String, Producer<PulsarMessage>> producerMap = new HashMap<>();

    public PulsarTemplate(PulsarClientWrapper pulsarClientWrapper, PulsarProperties pulsarProperties) {
        this.pulsarClientWrapper = pulsarClientWrapper;
        this.pulsarProperties = pulsarProperties;
    }

    @Override
    public void destroy() throws Exception {
        for (Map.Entry<String, Producer<PulsarMessage>> entry : producerMap.entrySet()) {
            entry.getValue().close();
            log.info("{}主题的生产者已关闭", entry.getKey());
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
                    .intercept(new SendSuccessInterceptor())
                    .create();
            producerMap.put(topic.getTopicPrefix(), producer);
        }
    }

    public void sendMessage(PulsarMessage pulsarMessage) {
        // 判断当前消息是否为新消息，若是需要入库
        if (pulsarMessage.getMessageId() == null) {
            MqRecordMapper mqRecordMapper = ApplicationContextHolder.getBean(MqRecordMapper.class);
            PulsarProperties.ProducerConfig topicProperties = pulsarProperties.getPulsarConfig().getProducer();
            log.info("首次发送该消息，message={}", pulsarMessage);
            MqRecord mqRecord = new MqRecord();
            Date sendTime = new Date();
            Snowflake snowflake = new Snowflake();
            mqRecord.setId(snowflake.nextId());
            mqRecord.setMsgContent(JSONUtil.toJsonStr(pulsarMessage));
            mqRecord.setTryCount(1);
            mqRecord.setStatus(MessageStatus.SENDING.getStatus());
            Date nextRetryTime = DateUtils.addMilliseconds(sendTime, topicProperties.getSendTimeout());
            mqRecord.setNextRetryTime(nextRetryTime);
            mqRecord.setCreateTime(sendTime);
            mqRecord.setUpdateTime(sendTime);
            mqRecord.setCreateUser(Constant.SYSTEM);
            mqRecord.setUpdateUser(Constant.SYSTEM);
            mqRecordMapper.insert(mqRecord);
            pulsarMessage.setMessageId(mqRecord.getId());
            pulsarMessage.setFirstSendTime(sendTime);
        } else {
            log.info("重发该消息，message={}", pulsarMessage);
        }

        producerMap.get(pulsarMessage.getTopicPrefix())
                .newMessage()
                .value(pulsarMessage)
                // 事件时间
                .eventTime(pulsarMessage.getFirstSendTime().getTime())
                .sequenceId(pulsarMessage.getMessageId())
                .sendAsync()
                .thenApply(messageId -> {
                    // 这里需要同步更新数据库
                    log.info("消息发送成功，更新消息的状态为发送成功, messageId={}", pulsarMessage.getMessageId());
                    MqRecord record = new MqRecord();
                    record.setId(pulsarMessage.getMessageId());
                    record.setStatus(MessageStatus.SEND_SUCCESS.getStatus());
                    MqRecordMapper mqRecordMapper = ApplicationContextHolder.getBean(MqRecordMapper.class);
                    mqRecordMapper.updateById(record);
                    return messageId;
                }).exceptionally(e -> {
                    log.error("消息发送失败", e);
                    return null;
                });
    }
}
