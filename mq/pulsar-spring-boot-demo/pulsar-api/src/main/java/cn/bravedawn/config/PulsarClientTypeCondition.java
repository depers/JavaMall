package cn.bravedawn.config;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;

/**
 * @Description : 因为存在在消费端消费消息的时候需要再发消息到broker里面，所以需要同时支持消费者和生产者，这里做条件判断
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-24 15:50
 */
public class PulsarClientTypeCondition implements Condition {


    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String property = context.getEnvironment().getProperty("pulsar.client-type", "");
        Preconditions.checkArgument(StringUtils.isNotBlank(property), "pulsar客户端类型必须指定");
        return property.equals("mixture") || property.equals("producer");
    }
}
