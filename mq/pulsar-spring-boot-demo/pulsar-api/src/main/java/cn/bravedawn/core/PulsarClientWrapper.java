package cn.bravedawn.core;

import cn.bravedawn.config.PulsarProperties;
import cn.bravedawn.toolkit.ApplicationContextHolder;
import cn.bravedawn.toolkit.EnvironmentUtil;
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
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-21 21:51
 */
@Slf4j
public class PulsarClientWrapper implements InitializingBean, DisposableBean {

    private PulsarProperties pulsarProperties;
    private ApplicationContextHolder applicationContextHolder;
    private PulsarClient pulsarClient;

    public PulsarClientWrapper(ApplicationContextHolder applicationContextHolder, PulsarProperties pulsarProperties) {
        this.applicationContextHolder = applicationContextHolder;
        this.pulsarProperties = pulsarProperties;
    }

    @Override
    public void destroy() throws Exception {
        if (pulsarClient != null) {
            pulsarClient.close();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        PulsarProperties.PulsarConfig pulsarConfig = pulsarProperties.getPulsarConfig();
        if (EnvironmentUtil.isProducer()) {
            pulsarClient = PulsarClient.builder()
                    .ioThreads(pulsarConfig.getProducer().getIoThreads())
                    .listenerThreads(pulsarConfig.getProducer().getListenerThreads())
                    .serviceUrl(pulsarConfig.getServiceUrl())
                    .build();
        } else {
            pulsarClient = PulsarClient.builder()
                    .ioThreads(pulsarConfig.getConsumer().getIoThreads())
                    .listenerThreads(pulsarConfig.getConsumer().getListenerThreads())
                    .serviceUrl(pulsarConfig.getServiceUrl())
                    .build();
        }

        createTopic();
    }

    public PulsarClient getPulsarClient() {
        return pulsarClient;
    }


    public void createTopic() {
        PulsarProperties.PulsarConfig pulsarConfig = pulsarProperties.getPulsarConfig();
        Preconditions.checkArgument(pulsarConfig != null, "pulsar配置信息不能为空");
        Preconditions.checkArgument(!pulsarProperties.getTopics().isEmpty(), "pulsar Topic配置信息不能为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(pulsarConfig.getNamespace()), "pulsar Namespace配置信息不能为空");

        try (PulsarAdmin admin = PulsarAdmin.builder()
                .serviceHttpUrl(pulsarConfig.getServiceHttpUrl())
                .build()) {

            List<String> topicList = admin.topics().getList(pulsarConfig.getNamespace());
            List<String> simplifyList  = topicList.stream().map(item -> item.substring(0, item.indexOf("partition")-1)).distinct().collect(Collectors.toList());
            List<PulsarProperties.TopicProperties> existTopics = pulsarProperties.getTopics().stream().filter(item -> simplifyList.contains(item.getTopicName())).collect(Collectors.toList());
            List<PulsarProperties.TopicProperties> noExistTopics = pulsarProperties.getTopics().stream().filter(item -> !simplifyList.contains(item.getTopicName())).collect(Collectors.toList());
            if (!existTopics.isEmpty()) {
                log.info("已经创建，无需重新创建的Topic有：{}", existTopics.stream().map(PulsarProperties.TopicProperties::getTopicName).collect(Collectors.toList()));
            }
            if (!noExistTopics.isEmpty()) {
                log.info("没有创建，需要重新创建的Topic有：{}", noExistTopics.stream().map(PulsarProperties.TopicProperties::getTopicName).collect(Collectors.toList()));
                noExistTopics.forEach(item -> {
                    try {
                        admin.topics().createPartitionedTopic(item.getTopicName(), item.getPartitionNum());
                        log.info("Topic创建成功，TopicName={}, TopicPrefix={}, partition={}", item.getTopicName(), item.getTopicPrefix(), item.getPartitionNum());
                    } catch (PulsarAdminException e) {
                        log.error("创建Topic出现异常", e);
                    }
                });
            }
        } catch (PulsarClientException |
                 PulsarAdminException e) {
            log.error("pulsarAdmin创建出现异常", e);
        }
    }
}
