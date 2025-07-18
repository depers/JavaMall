package cn.bravedawn.broker;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.PulsarClientException;

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
                .serviceHttpUrl("http://192.168.133.128:8080")
                .build();

        admin.namespaces().createNamespace("public/siis");

        Boolean deduplicationStatus = admin.namespaces().getDeduplicationStatus("public/siis");
        log.info("获取命名空间的重复删除数据配置：{}", deduplicationStatus);

        admin.close();
    }
}
