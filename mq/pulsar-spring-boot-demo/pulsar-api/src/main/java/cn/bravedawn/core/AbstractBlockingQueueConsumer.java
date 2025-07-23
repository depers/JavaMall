package cn.bravedawn.core;


import cn.hutool.core.net.NetUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.SubscriptionInitialPosition;
import org.apache.pulsar.client.impl.schema.JSONSchema;

import javax.xml.crypto.Data;
import java.util.List;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-22 16:03
 */
public abstract class AbstractBlockingQueueConsumer {

    abstract String getTopicPrefix();

    abstract void handleConsumer(PulsarMessage pulsarMessage);

    abstract void exceptionHandle(Exception e);


}
