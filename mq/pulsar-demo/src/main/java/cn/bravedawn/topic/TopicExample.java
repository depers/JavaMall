package cn.bravedawn.topic;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.PulsarClientException;

import java.util.List;

/**
 * @Description : 创建topic
 * @Author : depers
 * @Project : pulsar-demo
 * @Date : Created in 2025-07-12 10:48
 */

@Slf4j
public class TopicExample {

    public static void main(String[] args) throws PulsarClientException, PulsarAdminException {

        String topicName = "persistent://public/siis/partitionedTopic";
        PulsarAdmin admin = PulsarAdmin.builder()
                .serviceHttpUrl("http://192.168.24.128:8080")
                .build();
        deleteSubscription(admin, topicName, "my-subscription");

        admin.close();
    }


    /**
     * 获取topic列表
     * @param admin
     * @return
     * @throws PulsarAdminException
     */
    private static List<String> getTopicList(PulsarAdmin admin) throws PulsarAdminException {
        List<String> list = admin.topics().getList("public/siis");
        return list;
    }

    /**
     * 创建topic
     * @param admin
     * @param topicName
     * @throws PulsarAdminException
     */
    private static void createTopic(PulsarAdmin admin, String topicName) throws PulsarAdminException {
        if (!getTopicList(admin).contains(topicName)) {
            log.info("topic:{} 创建成功", topicName);
            admin.topics().createPartitionedTopic(topicName, 3);
        } else {
            log.info("topic:{} 已经创建", topicName);
        }
    }

    /**
     * 删除订阅关系
     * @param admin
     * @param topicName
     * @param subName
     * @throws PulsarAdminException
     */
    private static void deleteSubscription(PulsarAdmin admin, String topicName, String subName) throws PulsarAdminException {
        admin.topics().deleteSubscription(topicName, subName);
    }


    private static void deleteTopic(PulsarAdmin admin, String topicName) throws PulsarAdminException {
        admin.topics().delete(topicName);
    }
}
