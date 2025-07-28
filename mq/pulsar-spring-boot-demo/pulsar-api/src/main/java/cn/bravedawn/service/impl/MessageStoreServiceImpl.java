package cn.bravedawn.service.impl;

import cn.bravedawn.config.PulsarProperties;
import cn.bravedawn.contant.MessageStatus;
import cn.bravedawn.core.MqRecord;
import cn.bravedawn.core.PulsarMessage;
import cn.bravedawn.core.PulsarTemplate;
import cn.bravedawn.dao.MqRecordMapper;
import cn.bravedawn.service.MessageStoreService;
import cn.bravedawn.toolkit.DateTimeUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.shade.org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-28 14:10
 */
@Slf4j
@Service
public class MessageStoreServiceImpl implements MessageStoreService {

    @Autowired
    private MqRecordMapper mqRecordMapper;

    @Autowired
    private PulsarProperties pulsarProperties;

    @Autowired
    private PulsarTemplate pulsarTemplate;

    private static final int BATCH_COUNT = 100;

    @Override
    public void resend() {
        Date startTime = DateTimeUtil.localDateTime2Date(LocalDateTime.now().minusDays(3));
        PulsarProperties.ProducerConfig producerConfig = pulsarProperties.getPulsarConfig().getProducer();
        int timeoutMessageCount = mqRecordMapper.fetchTimeoutMessageCount(MessageStatus.SENDING.getStatus(), producerConfig.getSendMaxRetryCount(), startTime);
        Long lastTimeId = null;
        Optional<Long> maxId = Optional.of(0L);
        int handleCount = timeoutMessageCount % BATCH_COUNT == 0 ? timeoutMessageCount / BATCH_COUNT : timeoutMessageCount / BATCH_COUNT + 1;
        log.info("需要发送的消息数量有：count={}, page={}, maxRetryCount={}次, timeout={}ms", timeoutMessageCount, handleCount, producerConfig.getSendMaxRetryCount(), producerConfig.getSendTimeout());

        while (handleCount > 0 && maxId.isPresent()) {
            lastTimeId = maxId.get();
            List<MqRecord> mqRecordList = mqRecordMapper.fetchTimeoutMessage(MessageStatus.SENDING.getStatus(), lastTimeId, producerConfig.getSendMaxRetryCount(), BATCH_COUNT, startTime);
            log.info("本次需要重新发送的消息id: {}", mqRecordList.stream().map(MqRecord::getId).collect(Collectors.toList()));
            mqRecordList.forEach(record -> {
                log.info("开始重发消息, id={}, retryCount={}, createTime={}", record.getId(), record.getTryCount() + 1, DateUtil.format(record.getCreateTime(), DatePattern.NORM_DATETIME_MS_PATTERN));
                mqRecordMapper.updateRetryCount(record.getId(), record.getCreateTime());
                PulsarMessage msg = record.getMsgContent();
                msg.setMessageId(record.getId());
                msg.setFirstSendTime(record.getCreateTime());
                // 重发消息
                pulsarTemplate.sendMessage(msg);
            });
            log.info("分批次执行消息重发, handleCount={}, lastTime={}, size={}", handleCount, lastTimeId, mqRecordList.size());
            maxId = mqRecordList.stream().map(MqRecord::getId).max(Long::compareTo);
            handleCount--;
        }

    }
}
