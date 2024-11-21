package cn.bravedawn.aspect;

import cn.bravedawn.constant.MQRedisKey;
import cn.bravedawn.exception.BusinessException;
import cn.bravedawn.model.bo.MQMessage;
import cn.bravedawn.util.RedissonUtils;
import cn.bravedawn.util.SnowflakeUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RedissonClient;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : depers
 * @program : replay-protect-demo
 * @date : Created in 2024/11/2 22:13
 */
@Aspect
@Component
@Slf4j
public class RabbitMQListenerAspect {

    @Autowired
    private RedissonUtils redissonUtils;


    @Autowired
    private RedissonClient redissonClient;

    // 定义切点
    @Pointcut("@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)")
    public void pointcut() {

    }


    @Around("pointcut()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        MDC.put("log4i2Id", String.valueOf(SnowflakeUtil.getId()));
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        MQMessage message = (MQMessage) args[0];
        String targetMethod = signature.getMethod().getClass().getName() + "." + signature.getMethod().getName();
        log.info("对消息进行幂等性控制,message={}, method={}", JSONUtil.toJsonStr(message), targetMethod);
        String idempotenceKey = String.format(MQRedisKey.MQ_CONSUME_FLAG.getKey(), message.getMessageId());
        boolean res = redissonUtils.setNx(idempotenceKey, "1", MQRedisKey.MQ_CONSUME_FLAG.getTtl());
        if (res) {
            log.info("通过幂等性校验，messageId-{}", message.getMessageId());
        } else {
            log.warn("幂等性校验不通过，messageId={}", message.getMessageId());
            return;
        }
        Throwable cause = null;
        try {
            String timeKey = String.format(MQRedisKey.REDIS_MQ_CONSUME_TIME.getKey(), message.getMessageId());
            long time = redissonUtils.incr(timeKey, MQRedisKey.REDIS_MQ_CONSUME_TIME.getTtl());
            log.info("第{}次消费消息，messageId={}", time, message.getMessageId());
            long startTime = System.currentTimeMillis();
            joinPoint.proceed();
            log.info("消息消费完成, messageId={}, 耗时={}ms", message.getMessageId(), System.currentTimeMillis() - startTime);
        } catch (Throwable e) {
            log.error("消息消费出现异常", e);
            redissonUtils.del(idempotenceKey);

            if (e instanceof BusinessException) {
                cause = e;
            } else {
                cause = new BusinessException("消息消费异常，请联系管理员");
            }
        } finally {
            if (cause != null) {
                throw cause;
            }
            MDC.clear();
        }
    }
}

