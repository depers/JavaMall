package cn.bravedawn.interceptor;

import cn.bravedawn.contant.MsgRecordStatusEnum;
import cn.bravedawn.core.MqRecord;
import cn.bravedawn.core.PulsarMessage;
import cn.bravedawn.dao.MqRecordMapper;
import cn.bravedawn.toolkit.ApplicationContextHolder;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.interceptor.ProducerInterceptor;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-25 17:22
 */
@Slf4j
public class SendSuccessInterceptor implements ProducerInterceptor {
    @Override
    public void close() {}

    /**
     * 判断该拦截器是否对当前消息生效（返回 true 才继续处理）
     * @param message
     * @return
     */
    @Override
    public boolean eligible(Message message) {
        return true;
    }


    /**
     * 消息发送前调用，可修改消息
     * @param producer
     * @param message
     * @return
     */
    @Override
    public Message beforeSend(Producer producer, Message message) {
        return message;
    }

    /**
     * 消息发送完成后（成功或失败）调用
     * @param producer
     * @param message
     * @param msgId
     * @param exception
     */
    @Override
    public void onSendAcknowledgement(Producer producer, Message message, MessageId msgId, Throwable exception) {
        if (exception == null) {
            log.info("消息发送成功，更新消息的状态为发送成功, messageId={}", message.getSequenceId());
            MqRecord record = new MqRecord();
            record.setStatus(MsgRecordStatusEnum.SEND_SUCCESS.getStatus());
            MqRecordMapper mqRecordMapper = ApplicationContextHolder.getBean(MqRecordMapper.class);
            LambdaUpdateWrapper<MqRecord> lambdaUpdateWrapper = Wrappers.lambdaUpdate(MqRecord.class).eq(MqRecord::getId, message.getSequenceId());
            mqRecordMapper.update(record, lambdaUpdateWrapper);
        }
    }
}
