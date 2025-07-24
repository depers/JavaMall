package cn.bravedawn.toolkit;

import cn.bravedawn.config.PulsarProperties;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @Description : 读取YAML
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-21 16:52
 */
@Slf4j
public class YamlUtil {


    public static PulsarProperties readYaml(String resource) {
        try {
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            PulsarProperties pulsarProperties = objectMapper.readValue(YamlUtil.class.getResource(resource), PulsarProperties.class);
            log.info("读取Pulsar配置：{}", JSONUtil.toJsonStr(pulsarProperties));
            return pulsarProperties;
        } catch (IOException e) {
            log.error("读取Pulsar配置异常", e);
        }

        return null;

    }
}
