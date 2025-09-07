package cn.bravedawn.broker;

import cn.bravedawn.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.common.policies.data.BacklogQuota;
import org.apache.pulsar.common.policies.data.RetentionPolicies;
import org.apache.pulsar.common.policies.data.SchemaCompatibilityStrategy;

import java.util.List;
import java.util.Map;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-demo
 * @Date : Created in 2025-07-15 13:57
 */
@Slf4j
public class NamespaceConfigurationClient{


    public static void main(String[] args) throws PulsarClientException, PulsarAdminException {

        PulsarAdmin admin = PulsarAdmin.builder()
                .serviceHttpUrl("http://192.168.24.128:8080")
                .build();

        try {
            List<String> namespaces = admin.namespaces().getNamespaces("public");
            if (namespaces.contains("public/siis")) {
                log.warn("该命名空间已存在，namespace={}", "public/siis");
            } else {
                log.info("创建命名空间, namespace={}", "public/siis");
                admin.namespaces().createNamespace("public/siis");
            }

//            Boolean deduplicationStatus = admin.namespaces().getDeduplicationStatus("public/siis");
//            if (deduplicationStatus == null) {
//                log.info("开启命名空间的重复数据删除配置");
//                admin.namespaces().setDeduplicationStatus("public/siis", true);
//            } else {
//                log.info("获取命名空间的重复删除数据配置：{}", deduplicationStatus);
//            }ｿ



            Map<BacklogQuota.BacklogQuotaType, BacklogQuota> backlogQuotaMap = admin.namespaces().getBacklogQuotaMap("public/siis");
            log.info("该命名空间下的积压配额：{}", backlogQuotaMap);

            Integer namespaceMessageTTL = admin.namespaces().getNamespaceMessageTTL("public/siis");
            log.info("消息的有效时间：{}", namespaceMessageTTL);


            SchemaCompatibilityStrategy schemaCompatibilityStrategy = admin.namespaces().getSchemaCompatibilityStrategy(Constants.NAMESPACE_NAME);
            log.info("该命名空间的Schema兼容性策略：{}", schemaCompatibilityStrategy);
            admin.namespaces().setSchemaCompatibilityStrategy(Constants.NAMESPACE_NAME, SchemaCompatibilityStrategy.FORWARD);

            boolean isAllowAutoUpdateSchema = admin.namespaces().getIsAllowAutoUpdateSchema(Constants.NAMESPACE_NAME);
            log.info("控制是否允许客户端自动更新 Topic 的 Schema: {}", isAllowAutoUpdateSchema);
            admin.namespaces().setIsAllowAutoUpdateSchema(Constants.NAMESPACE_NAME, true);

            boolean schemaValidationEnforced = admin.namespaces().getSchemaValidationEnforced(Constants.NAMESPACE_NAME);
            log.info("是否强制 Broker 对所有消息进行 Schema 验证: {}", schemaValidationEnforced);
            admin.namespaces().setSchemaValidationEnforced(Constants.NAMESPACE_NAME, true);


        } catch (Throwable e) {
            log.info("命名空间操作出现异常", e);
        } finally {
            admin.close();
        }




    }


    private void getRetention(PulsarAdmin admin) throws PulsarAdminException {
        RetentionPolicies retention = admin.namespaces().getRetention("public/siis");
        log.info("该命名空间下每个主题的保留策略：{}", retention);

        if (retention == null) {
            // 示例：设置保留策略（保留6小时或1GB数据）
            RetentionPolicies ret = new RetentionPolicies(60, 1024);
            admin.namespaces().setRetention("public/siis", ret);
        }
    }
}
