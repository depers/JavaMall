package cn.bravedawn.topic;

import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.PulsarClientException;

/**
 * @Description : 创建topic
 * @Author : depers
 * @Project : pulsar-demo
 * @Date : Created in 2025-07-12 10:48
 */
public class CreateTopic {

    public static void main(String[] args) throws PulsarClientException, PulsarAdminException {

        String topicName = "persistent://public/siis/partitionedTopic";
        PulsarAdmin admin = PulsarAdmin.builder()
                .serviceHttpUrl("http://192.168.133.128:8080")
                .build();
//        admin.topics().createPartitionedTopic(topicName, 3);

        admin.topics().delete("persistent://public/default/partitionedTopic-deadLetter");
        admin.topics().delete("persistent://public/default/partitionedTopic-retryLetter");

        admin.close();
    }
}
