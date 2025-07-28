package cn.bravedawn.job;

import cn.bravedawn.service.MessageStoreService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-28 14:08
 */
@DisallowConcurrentExecution
@Slf4j
public class RetryMessageDataflowJob implements Job {


    @Autowired
    private MessageStoreService messageStoreService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("开始对消息进行补发");
        messageStoreService.resend();
        log.info("消息补发结束");
    }
}
