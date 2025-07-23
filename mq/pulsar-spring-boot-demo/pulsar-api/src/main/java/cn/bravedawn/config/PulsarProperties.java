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


    @JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
    public static class PulsarConfig {
        private String serviceUrl;
        private String serviceHttpUrl;
        private String namespace;
        private ProducerConfig producer;
        private ConsumerConfig consumer;

        public String getServiceUrl() {
            return serviceUrl;
        }

        public String getNamespace() {
            return namespace;
        }

        public ProducerConfig getProducer() {
            return producer;
        }

        public ConsumerConfig getConsumer() {
            return consumer;
        }

        public String getServiceHttpUrl() {
            return serviceHttpUrl;
        }
    }

    @JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
    public static class ProducerConfig {
        private int ioThreads;
        private int listenerThreads;
        private int sendTimeout;
        private int sendMaxRetryCount;


        public int getSendTimeout() {
            return sendTimeout;
        }

        public int getSendMaxRetryCount() {
            return sendMaxRetryCount;
        }

        public int getIoThreads() {
            return ioThreads;
        }

        public int getListenerThreads() {
            return listenerThreads;
        }
    }

    @JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
    public static class ConsumerConfig {
        private int ioThreads;
        private int listenerThreads;

        public int getIoThreads() {
            return ioThreads;
        }

        public int getListenerThreads() {
            return listenerThreads;
        }
    }


    @JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
    public static class TopicProperties {

        private String topicName;
        private String topicPrefix;
        private int partitionNum;
        private ListenProperties listenConfig;

        public String getTopicName() {
            return topicName;
        }

        public String getTopicPrefix() {
            return topicPrefix;
        }

        public ListenProperties getListenConfig() {
            return listenConfig;
        }

        public int getPartitionNum() {
            return partitionNum;
        }
    }

    @JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
    public static class ListenProperties {
        private boolean enableRetry;
        private int maxRetryCount;
        private int consumerNum;
        private String retryLetterTopicName;
        private String deadLetterTopicName;

        public String getRetryLetterTopicName() {
            return retryLetterTopicName;
        }

        public String getDeadLetterTopicName() {
            return deadLetterTopicName;
        }

        public int getMaxRetryCount() {
            return maxRetryCount;
        }

        public int getConsumerNum() {
            return consumerNum;
        }

        public boolean isEnableRetry() {
            return enableRetry;
        }
    }
}
