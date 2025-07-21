package cn.bravedawn.config;

import cn.bravedawn.core.PulsarClientWrapper;
import cn.bravedawn.toolkit.YamlUtil;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

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

}
