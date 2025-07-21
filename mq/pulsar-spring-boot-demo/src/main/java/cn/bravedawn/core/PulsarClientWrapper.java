package cn.bravedawn.core;

import cn.bravedawn.config.PulsarProperties;
import org.apache.pulsar.client.api.PulsarClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-21 21:51
 */
public class PulsarClientWrapper implements InitializingBean, DisposableBean {

    @Autowired
    private PulsarProperties pulsarProperties;

    private PulsarClient pulsarClient;

    @Override
    public void destroy() throws Exception {
        if (pulsarClient != null) {
            pulsarClient.close();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        PulsarProperties.PulsarConfig pulsarConfig = pulsarProperties.getPulsarConfig();
        pulsarClient = PulsarClient.builder()
                .ioThreads(pulsarConfig.getIoThreads())
                .listenerThreads(pulsarConfig.getListenerThreads())
                .serviceUrl(pulsarConfig.getServiceUrl())
                .build();
    }

    public PulsarClient getPulsarClient() {
        return pulsarClient;
    }
}
