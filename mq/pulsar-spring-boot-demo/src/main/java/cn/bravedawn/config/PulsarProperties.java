package cn.bravedawn.config;

import lombok.Data;

import java.util.List;

/**
 * @Description : 读取yml
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-21 16:44
 */
@Data
public class PulsarProperties {

    private PulsarConfig pulsarConfig;

    private List<TopicProperties> topics;


    public static class PulsarConfig {
        private String serviceUrl;
        private int ioThreads;
        private int listenerThreads;
        private int sendTimeout;
        private int sendMaxRetryCount;

        public String getServiceUrl() {
            return serviceUrl;
        }

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


    public static class TopicProperties {

        private String topicName;
        private String topicPrefix;
        private String retryLetterTopicName;
        private String deadLetterTopicName;
        private ListenProperties listenConfig;

        public String getTopicName() {
            return topicName;
        }

        public String getTopicPrefix() {
            return topicPrefix;
        }

        public String getRetryLetterTopicName() {
            return retryLetterTopicName;
        }

        public String getDeadLetterTopicName() {
            return deadLetterTopicName;
        }

        public ListenProperties getListenConfig() {
            return listenConfig;
        }
    }

    public static class ListenProperties {
        private int maxRetryCount;
        private int workThreadNum;

        public int getMaxRetryCount() {
            return maxRetryCount;
        }

        public int getWorkThreadNum() {
            return workThreadNum;
        }
    }
}
