package cn.bravedawn.broker;

import cn.bravedawn.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.PulsarClientException;

import java.util.Map;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-demo
 * @Date : Created in 2025-07-15 13:57
 */
@Slf4j
public class TopicConfigurationClient {

    public static void main(String[] args) throws PulsarClientException, PulsarAdminException {
        PulsarAdmin admin = PulsarAdmin.builder()
                .serviceHttpUrl("http://192.168.133.128:8080")
                .build();

        Map<String, String> properties = admin.topics().getProperties(Constants.TOPIC_NAME);
        log.info("topic的配置： {}", properties);

        // 因为pulsar默认关闭了topic的策略配置，这里会报错
        Boolean deduplicationStatus = admin.topics().getDeduplicationStatus(Constants.TOPIC_NAME);
        log.info("topic的删除重复消息配置：{}", deduplicationStatus);


        admin.close();

    }
}
