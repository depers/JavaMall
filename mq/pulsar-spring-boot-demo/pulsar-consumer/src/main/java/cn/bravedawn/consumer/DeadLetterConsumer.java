package cn.bravedawn.consumer;

import cn.bravedawn.config.PulsarProperties;
import cn.bravedawn.core.AbstractDeadLetterBlockingQueueConsumer;
import cn.bravedawn.core.PulsarMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@AllArgsConstructor
public class DeadLetterConsumer implements AbstractDeadLetterBlockingQueueConsumer {


    private final PulsarProperties pulsarProperties;

    @Override
    public String getTopicPattern() {
        return pulsarProperties.getDeadLetter().getTopicsPattern();
    }

    @Override
    public void handleConsumer(PulsarMessage pulsarMessage) {
        log.info("进入死信队列处理逻辑, pulsarMessage={}", pulsarMessage);
    }

    @Override
    public void exceptionHandle(Throwable e) {
        log.error("死信队列逻辑处理异常", e);
    }
}
