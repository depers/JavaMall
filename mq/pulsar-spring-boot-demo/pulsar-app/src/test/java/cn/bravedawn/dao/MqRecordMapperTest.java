package cn.bravedawn.dao;

import cn.bravedawn.PulsarApplication;
import cn.bravedawn.core.MqRecord;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-28 08:58
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PulsarApplication.class)
@DisplayName("测试dao")
@Slf4j
public class MqRecordMapperTest {

    @Autowired
    private MqRecordMapper mqRecordMapper;

    @Test
    public void testUpdateById() {
        MqRecord record = new MqRecord();
        record.setId(1948215349376315392L);
        record.setStatus(MsgRecordStatusEnum.SEND_SUCCESS.getStatus());
        mqRecordMapper.updateById(record);
    }
}
