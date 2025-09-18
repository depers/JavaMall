package cn.bravedawn.broker;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.common.policies.data.BrokerInfo;

import java.util.List;
import java.util.Map;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-demo
 * @Date : Created in 2025-07-12 10:13
 */
@Slf4j
public class BrokerConfigurationClient {


    public static void main(String[] args) throws PulsarClientException, PulsarAdminException {
        PulsarAdmin admin = PulsarAdmin.builder()
                .serviceHttpUrl("http://192.168.24.128:8080")
                .build();

        List<String> dynamicConfigurationNames = admin.brokers().getDynamicConfigurationNames();
        log.info("dynamicConfigurationNames: {}", dynamicConfigurationNames);

        /**
         * 返回 获取所有已动态更新的参数的列表。
         * 这些配置可以通过 Admin API 在运行时修改并立即生效，无需重启 Broker
         */
        Map<String, String> allDynamicConfigurations = admin.brokers().getAllDynamicConfigurations();
        log.info("allDynamicConfigurations: " + allDynamicConfigurations);


        /**
         * 返回 Broker 当前运行时实际生效的所有配置（包括静态和动态配置）
         */
        Map<String, String> runtimeConfigurations = admin.brokers().getRuntimeConfigurations();
        log.info("runtimeConfigurations: " + runtimeConfigurations);


        /**
         * 是否开启批消息的确认
         */

        String acknowledgmentAtBatchIndexLevelEnabled = admin.brokers().getRuntimeConfigurations().get("acknowledgmentAtBatchIndexLevelEnabled");
        log.info("acknowledgmentAtBatchIndexLevelEnabled: {}", acknowledgmentAtBatchIndexLevelEnabled);

        /**
         * 是否开启延迟投递
         */

        String delayedDeliveryEnabled = admin.brokers().getRuntimeConfigurations().get("delayedDeliveryEnabled");
        log.info("delayedDeliveryEnabled: {}", delayedDeliveryEnabled);

        String brokerSubscriptionPatternEvaluationEnabled = admin.brokers().getRuntimeConfigurations().get("brokerSubscriptionPatternEvaluationEnabled");
        log.info("brokerSubscriptionPatternEvaluationEnabled: {}", brokerSubscriptionPatternEvaluationEnabled);


        System.out.println("----------------------------------");
        getBrokers(admin);

        admin.close();
    }


    public static void getBrokers(PulsarAdmin admin) throws PulsarAdminException {
        // 2. 获取集群名称列表（如果你的部署有多个集群）
        List<String> clusters = admin.clusters().getClusters();
        System.out.println("Clusters: " + clusters);

        // 假设我们获取第一个集群的 Broker（通常单集群部署只有一个）
        String targetCluster = clusters.get(0); // 例如 "standalone" 或 "prod-cluster"

        // 3. 获取指定集群下所有活跃 Broker 的列表
        List<String> brokers = admin.brokers().getActiveBrokers(targetCluster);

        // 4. 遍历并打印 Broker 信息
        System.out.println("Active Brokers in cluster '" + targetCluster + "':");
        System.out.println("Broker: " + brokers);
    }
}
