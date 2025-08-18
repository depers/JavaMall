package cn.bravedawn.broker;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.common.policies.data.BacklogQuota;
import org.apache.pulsar.common.policies.data.RetentionPolicies;

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
//            }



            Map<BacklogQuota.BacklogQuotaType, BacklogQuota> backlogQuotaMap = admin.namespaces().getBacklogQuotaMap("public/siis");
            log.info("该命名空间下的积压配额：{}", backlogQuotaMap);

            Integer namespaceMessageTTL = admin.namespaces().getNamespaceMessageTTL("public/siis");
            log.info("消息的有效时间：{}", namespaceMessageTTL);


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
