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
import org.apache.pulsar.common.policies.data.BacklogQuota;
import org.apache.pulsar.common.policies.data.RetentionPolicies;
import org.apache.pulsar.shade.io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private PulsarClient pulsarClient;

    public PulsarClientWrapper(PulsarProperties pulsarProperties) {
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
            log.info("客户端按照生产者模式进行配置, ioThreads={}, listenerThreads={}", pulsarConfig.getProducer().getIoThreads(), pulsarConfig.getProducer().getListenerThreads());
            pulsarClient = PulsarClient.builder()
                    .ioThreads(pulsarConfig.getProducer().getIoThreads())
                    .listenerThreads(pulsarConfig.getProducer().getListenerThreads())
                    .serviceUrl(pulsarConfig.getServiceUrl())
                    .build();
        }
        if (EnvironmentUtil.isConsumer()){
            log.info("客户端按照消费者模式进行配置, ioThreads={}, listenerThreads={}", pulsarConfig.getConsumer().getIoThreads(), pulsarConfig.getConsumer().getListenerThreads());
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

            // 解析topics配置
            List<String> topicList = admin.topics().getList(pulsarConfig.getNamespace());
            List<String> simplifyList  = topicList.stream()
                    .filter(item -> item.contains("partition-"))
                    .map(item -> item.substring(0, item.indexOf("partition-")-1)).distinct().collect(Collectors.toList());
            Map<String, Integer> existTopics = pulsarProperties.getTopics()
                    .stream().filter(item -> simplifyList.contains(item.getTopicName()))
                    .collect(Collectors.toMap(PulsarProperties.TopicProperties::getTopicName, PulsarProperties.TopicProperties::getPartitionNum,
                            (oldValue, newValue) -> oldValue));

            Map<String, Integer> noExistTopics = pulsarProperties.getTopics()
                    .stream().filter(item -> !simplifyList.contains(item.getTopicName()))
                    .collect(Collectors.toMap(PulsarProperties.TopicProperties::getTopicName, PulsarProperties.TopicProperties::getPartitionNum,
                            (oldValue, newValue) -> oldValue));

            // 解析priority-queue配置
            List<String> prefixList = pulsarProperties.getTopics().stream()
                    .filter(PulsarProperties.TopicProperties::isPriorityEnable)
                    .map(PulsarProperties.TopicProperties::getTopicPrefix).collect(Collectors.toList());

            Map<String, Integer> priorityQueue = pulsarProperties.getPriorityQueue().stream()
                    .filter(item -> prefixList.contains(item.getTopicPrefix()))
                    .collect(Collectors.toMap(PulsarProperties.PriorityQueue::getTopicName, PulsarProperties.PriorityQueue::getPartitionNum,
                            (oldValue, newValue) -> oldValue));
            priorityQueue.entrySet().stream().forEach(item -> {
                if (simplifyList.contains(item.getKey())) {
                    existTopics.put(item.getKey(), item.getValue());
                } else {
                    noExistTopics.put(item.getKey(), item.getValue());
                }
            });


            if (!existTopics.isEmpty()) {
                log.info("已经创建，无需重新创建的Topic有：{}", existTopics.keySet());
            }
            if (!noExistTopics.isEmpty()) {
                log.info("没有创建，需要重新创建的Topic有：{}", noExistTopics.keySet());
                noExistTopics.forEach((key, value) -> {
                    try {
                        admin.topics().createPartitionedTopic(key, value);
                        log.info("Topic创建成功，TopicName={}, partition={}", key, value);
                    } catch (PulsarAdminException e) {
                        log.error("创建Topic出现异常", e);
                    }
                });
            }

            // 检测是否开启延时队列
            detectSupportDelayQueue(admin);
            getRetention(admin, pulsarConfig.getNamespace());
            getBlockLogQuotas(admin, pulsarConfig.getNamespace());
            getTTL(admin, pulsarConfig.getNamespace());
        } catch (PulsarClientException |
                 PulsarAdminException e) {
            log.error("pulsarAdmin创建出现异常", e);
        }
    }


    public void detectSupportDelayQueue(PulsarAdmin admin) throws PulsarAdminException {
        String delayedDeliveryEnabled = admin.brokers().getRuntimeConfigurations().get("delayedDeliveryEnabled");
        String delayedDeliveryTickTimeMillis = admin.brokers().getRuntimeConfigurations().get("delayedDeliveryTickTimeMillis");
        String isDelayedDeliveryDeliverAtTimeStrict = admin.brokers().getRuntimeConfigurations().get("isDelayedDeliveryDeliverAtTimeStrict");

        log.info("检测broker是否开启延时队列：delayedDeliveryEnabled={}, delayedDeliveryTickTimeMillis={}, isDelayedDeliveryDeliverAtTimeStrict={}",
                delayedDeliveryEnabled, delayedDeliveryTickTimeMillis, isDelayedDeliveryDeliverAtTimeStrict);
    }


    private void getRetention(PulsarAdmin admin, String namespace) throws PulsarAdminException {
        RetentionPolicies retention = admin.namespaces().getRetention(namespace);
        log.info("{}命名空间下的保留策略：{}", namespace, retention);
    }


    private void getBlockLogQuotas(PulsarAdmin admin, String namespace) throws PulsarAdminException {
        Map<BacklogQuota.BacklogQuotaType, BacklogQuota> backlogQuotaMap = admin.namespaces().getBacklogQuotaMap(namespace);
        log.info("{}命名空间下的积压配额：{}", namespace, backlogQuotaMap);
    }


    private void getTTL(PulsarAdmin admin, String namespace) throws PulsarAdminException {
        Integer namespaceMessageTTL = admin.namespaces().getNamespaceMessageTTL(namespace);
        log.info("{}命名空间下消息的生存时间：{}", namespace, namespaceMessageTTL);
    }
}
