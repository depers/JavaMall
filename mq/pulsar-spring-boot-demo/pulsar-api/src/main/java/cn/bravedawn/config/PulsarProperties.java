package cn.bravedawn.config;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

/**
 * @Description : 读取yml
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-21 16:44
 */
@Data
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class PulsarProperties {

    private PulsarConfig pulsarConfig;

    private List<TopicProperties> topics;

    private DeadLetterProperties deadLetter;

    private List<PriorityQueue> priorityQueue;


    @Data
    @JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
    public static class PulsarConfig {
        private String serviceUrl;
        private String serviceHttpUrl;
        private String namespace;
        private ProducerConfig producer;
        private ConsumerConfig consumer;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
    public static class ProducerConfig {
        private int ioThreads;
        private int listenerThreads;
        private int sendTimeout;
        private int sendMaxRetryCount;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
    public static class ConsumerConfig {
        private int ioThreads;
        private int listenerThreads;
    }


    @Data
    @JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
    public static class TopicProperties {
        private String topicName;
        private String topicPrefix;
        private int partitionNum;
        private ListenProperties listenConfig;
        private boolean priorityEnable;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
    public static class ListenProperties {
        private boolean enableRetry;
        private int maxRetryCount;
        private int retryDelayTime;
        private int consumerNum;
        private String retryLetterTopicName;
        private String deadLetterTopicName;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
    public static class DeadLetterProperties {
        private String topicsPattern;

        public String getTopicsPattern() {
            return topicsPattern;
        }
    }

    @JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
    @Data
    public static class PriorityQueue {
        private String topicName;
        private String topicPrefix;
        private int partitionNum;
        private ListenProperties listenConfig;

    }

}
