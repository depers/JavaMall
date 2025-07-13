package cn.bravedawn.broker;

import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.PulsarClientException;

import java.util.List;
import java.util.Map;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-demo
 * @Date : Created in 2025-07-12 10:13
 */
public class ConfigurationClient {


    public static void main(String[] args) throws PulsarClientException, PulsarAdminException {
        PulsarAdmin admin = PulsarAdmin.builder()
                .serviceHttpUrl("http://192.168.133.128:8080")
                .build();

        /**
         * 返回 所有动态配置参数（可动态修改的配置）
         * 这些配置可以通过 Admin API 在运行时修改并立即生效，无需重启 Broker
         */
        Map<String, String> allDynamicConfigurations = admin.brokers().getAllDynamicConfigurations();
        System.out.println("allDynamicConfigurations: " + allDynamicConfigurations);


        /**
         * 返回 Broker 当前运行时实际生效的所有配置（包括静态和动态配置）
         */
        Map<String, String> runtimeConfigurations = admin.brokers().getRuntimeConfigurations();
        System.out.println("runtimeConfigurations: " + runtimeConfigurations);
    }
}
