package cn.bravedawn.config;

import cn.bravedawn.core.AbstractBlockingQueueConsumer;
import cn.bravedawn.core.MessageListenerContainer;
import cn.bravedawn.core.PulsarClientWrapper;
import cn.bravedawn.core.PulsarTemplate;
import cn.bravedawn.dao.MqRecordMapper;
import cn.bravedawn.toolkit.ApplicationContextHolder;
import cn.bravedawn.toolkit.YamlUtil;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-21 17:29
 */
@Slf4j
@Configuration
public class PulsarConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }

    @Bean
    public PulsarProperties pulsarProperties() {
        PulsarProperties pulsarProperties = YamlUtil.readYaml("/pulsar.yml");
        Preconditions.checkArgument(pulsarProperties != null, "初始化Pulsar配置异常");
        return pulsarProperties;
    }

    @Bean
    public PulsarClientWrapper pulsarClientWrapper(ApplicationContextHolder applicationContextHolder, PulsarProperties pulsarProperties)  {
        return new PulsarClientWrapper(applicationContextHolder, pulsarProperties);
    }

    @Bean
    public PulsarTemplate pulsarTemplate(PulsarClientWrapper pulsarClientWrapper, PulsarProperties pulsarProperties, ApplicationContextHolder applicationContextHolder) {
        return new PulsarTemplate(pulsarClientWrapper, pulsarProperties, applicationContextHolder);
    }


    @Bean
    @ConditionalOnBean(AbstractBlockingQueueConsumer.class)
    public MessageListenerContainer messageListenerContainer(PulsarClientWrapper pulsarClientWrapper, PulsarProperties pulsarProperties, List<AbstractBlockingQueueConsumer> blockingQueueConsumerList) {
        return new MessageListenerContainer(pulsarClientWrapper, pulsarProperties, blockingQueueConsumerList);
    }

}
