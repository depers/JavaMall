package cn.bravedawn;

import cn.bravedawn.entity.Sms;
import cn.bravedawn.mapper.SmsMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author : depers
 * @Date : Created in 2025-10-24 15:31
 */
@Slf4j
@SpringBootTest
public class ShardingSelectTest {


    @Autowired
    private SmsMapper smsMapper;



    @Test
    public void testSelectSendTime() {
        LambdaQueryWrapper<Sms> smsLambdaQueryWrapper = Wrappers.lambdaQuery(Sms.class).eq(Sms::getSendTime, "2020-12-12 13:21:56");
        List<Sms> smsList = smsMapper.selectList(smsLambdaQueryWrapper);
        log.info("查询结果是：{}", smsList);
    }


    @Test
    public void testSelectPhone() {
        LambdaQueryWrapper<Sms> smsLambdaQueryWrapper = Wrappers.lambdaQuery(Sms.class).eq(Sms::getPhone, "13201410766");
        List<Sms> smsList = smsMapper.selectList(smsLambdaQueryWrapper);
        log.info("查询结果是：{}", smsList);
    }


    @Test
    public void testSelectBetweenTime() {
        LambdaQueryWrapper<Sms> smsLambdaQueryWrapper = Wrappers.lambdaQuery(Sms.class).between(Sms::getSendTime, "2020-12-12 00:00:00", "2021:12:31 00:00:00");
        List<Sms> smsList = smsMapper.selectList(smsLambdaQueryWrapper);
        log.info("查询结果是：{}", smsList);
    }
}
