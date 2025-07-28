package cn.bravedawn.toolkit;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-23 17:54
 */
public class EnvironmentUtil {

    public static boolean isProducer() {
        ConfigurableEnvironment configurableEnvironment = ApplicationContextHolder.getBean(ConfigurableEnvironment.class);
        String pulsarType = configurableEnvironment.getProperty("pulsar.client-type");
        Preconditions.checkArgument(StringUtils.isNotBlank(pulsarType), "pulsar客户端类型需要配置");
        return StringUtils.equals("producer", pulsarType) || StringUtils.equals("mixture", pulsarType);
    }


    public static boolean isConsumer() {
        ConfigurableEnvironment configurableEnvironment = ApplicationContextHolder.getBean(ConfigurableEnvironment.class);
        String pulsarType = configurableEnvironment.getProperty("pulsar.client-type");
        Preconditions.checkArgument(StringUtils.isNotBlank(pulsarType), "pulsar客户端类型需要配置");
        return StringUtils.equals("consumer", pulsarType) || StringUtils.equals("mixture", pulsarType);
    }
}


