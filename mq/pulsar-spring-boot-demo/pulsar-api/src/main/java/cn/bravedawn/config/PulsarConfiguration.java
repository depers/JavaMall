package cn.bravedawn.config;

import cn.bravedawn.core.BlockingQueueConsumer;
import cn.bravedawn.core.MessageListenerContainer;
import cn.bravedawn.core.PulsarClientWrapper;
import cn.bravedawn.core.PulsarTemplate;
import cn.bravedawn.dao.MqRecordMapper;
import cn.bravedawn.toolkit.YamlUtil;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-21 17:29
 */
@Slf4j
public class PulsarConfiguration {


    @Bean
    public PulsarProperties pulsarProperties() {
        PulsarProperties pulsarProperties = YamlUtil.readYaml("pulsar.yml");
        Preconditions.checkArgument(pulsarProperties == null, "初始化Pulsar配置异常");
        return pulsarProperties;
    }

    @Bean
    public PulsarClientWrapper pulsarClientWrapper()  {
        return new PulsarClientWrapper();
    }

    @Bean
    public PulsarTemplate pulsarTemplate(PulsarClientWrapper pulsarClientWrapper, PulsarProperties pulsarProperties, MqRecordMapper mqRecordMapper) {
        return new PulsarTemplate(pulsarClientWrapper, pulsarProperties, mqRecordMapper);
    }


    @Bean
    @ConditionalOnBean(BlockingQueueConsumer.class)
    public MessageListenerContainer messageListenerContainer(PulsarClientWrapper pulsarClientWrapper, PulsarProperties pulsarProperties, List<BlockingQueueConsumer> blockingQueueConsumerList) {
        return new MessageListenerContainer(pulsarClientWrapper, pulsarProperties, blockingQueueConsumerList);
    }

}
