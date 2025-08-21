package cn.bravedawn.consumer;

import cn.bravedawn.constant.TopicEnum;
import cn.bravedawn.core.AbstractBlockingQueueConsumer;
import cn.bravedawn.core.PulsarMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-24 09:45
 */
@Slf4j
@Component
public class SmsConsumer implements AbstractBlockingQueueConsumer {

    @Override
    public String getTopicPrefix() {
        return TopicEnum.SMS_TOPIC.getTopicPrefix();
    }

    @Override
    public void handleConsumer(PulsarMessage pulsarMessage) {
        log.info("消费者收到了信息：{}", pulsarMessage);
        int i = 1 / 0;
    }

    @Override
    public void exceptionHandle(Throwable e) {
        log.error("消费者消费出现异常", e);
    }
}
