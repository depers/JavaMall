package cn.bravedawn.interceptor;

import cn.bravedawn.contant.RedisKey;
import cn.bravedawn.core.PulsarMessage;
import cn.bravedawn.toolkit.ApplicationContextHolder;
import cn.bravedawn.toolkit.RedissonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;

import java.util.Set;

/**
 * @Author : depers
 * @Date : Created in 2025-08-21 14:18
 */
@Slf4j
public class ConsumeIdempotentInterceptor implements ConsumerInterceptor<PulsarMessage> {
    @Override
    public void close() {

    }

    @Override
    public Message<PulsarMessage> beforeConsume(Consumer<PulsarMessage> consumer, Message<PulsarMessage> message) {
        PulsarMessage value = message.getValue();
        Long messageId = value.getMessageId();
        if (message.getProperties().get("RECONSUMETIMES") != null) {
            log.info("该消息为重试消息, 不做幂等性校验, messageId={}", messageId);
            return message;
        } else {
            String idempotentKey = String.format(RedisKey.IDEMPOTENT_REDIS_KEY.getKey(), messageId);
            RedissonUtils redissonUtils = ApplicationContextHolder.getBean(RedissonUtils.class);
            boolean result = redissonUtils.setNx(idempotentKey, "1", RedisKey.IDEMPOTENT_REDIS_KEY.getTtl());
            if (result) {
                log.info("幂等性校验通过, messageId={}", messageId);
                return message;
            } else {
                log.warn("幂等性校验不通过, messageId={}", messageId);
                try {
                    consumer.acknowledge(message);
                } catch (PulsarClientException e) {
                    log.error("消费端拦截器消息确认异常", e);
                }
                return null;
            }
        }

    }

    @Override
    public void onAcknowledge(Consumer<PulsarMessage> consumer, MessageId messageId, Throwable exception) {

    }

    @Override
    public void onAcknowledgeCumulative(Consumer<PulsarMessage> consumer, MessageId messageId, Throwable exception) {

    }

    @Override
    public void onNegativeAcksSend(Consumer<PulsarMessage> consumer, Set<MessageId> messageIds) {
    }

    @Override
    public void onAckTimeoutSend(Consumer<PulsarMessage> consumer, Set<MessageId> messageIds) {

    }
}
