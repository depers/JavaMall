package cn.bravedawn.core;

import cn.bravedawn.config.PulsarProperties;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.shade.io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-21 21:51
 */
@Slf4j
public class PulsarClientWrapper implements InitializingBean, DisposableBean {

    @Autowired
    private PulsarProperties pulsarProperties;

    private PulsarClient pulsarClient;

    @Override
    public void destroy() throws Exception {
        if (pulsarClient != null) {
            pulsarClient.close();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        PulsarProperties.PulsarConfig pulsarConfig = pulsarProperties.getPulsarConfig();
        pulsarClient = PulsarClient.builder()
                .ioThreads(pulsarConfig.getIoThreads())
                .listenerThreads(pulsarConfig.getListenerThreads())
                .serviceUrl(pulsarConfig.getServiceUrl())
                .build();
    }

    public PulsarClient getPulsarClient() {
        return pulsarClient;
    }


    public void createTopic() {
        PulsarProperties.PulsarConfig pulsarConfig = pulsarProperties.getPulsarConfig();
        Preconditions.checkArgument(pulsarConfig == null, "pulsar配置信息不能为空");
        Preconditions.checkArgument(pulsarProperties.getTopics().isEmpty(), "pulsar Topic配置信息不能为空");
        Preconditions.checkArgument(StringUtils.isBlank(pulsarConfig.getNamespace()), "pulsar Namespace配置信息不能为空");

        try(PulsarAdmin admin = PulsarAdmin.builder()
                .serviceHttpUrl(pulsarConfig.getServiceUrl())
                .build()) {

            List<String> topicList = admin.topics().getList(pulsarConfig.getNamespace());
            pulsarProperties.getTopics().forEach(item -> {
                if (topicList.contains(item.getTopicName())) {
                    log.info("该Topic已经创建，无需重新创建，Topic={}", item.getTopicName());
                } else {
                    log.info("该Topic没有创建，需要进行创建， Topic={}", item.getTopicName());
                    try {
                        admin.topics().createPartitionedTopic(item.getTopicName(), item.getPartitionNum());
                    } catch (PulsarAdminException e) {
                        log.error("创建Topic出现异常", e);
                    }
                }
            });

        } catch (PulsarClientException | PulsarAdminException e) {
            log.error("pulsarAdmin创建出现异常", e);
        }
    }
}
