package cn.bravedawn.contant;

import cn.bravedawn.core.PulsarMessage;
import cn.bravedawn.toolkit.JsonUtil;
import cn.hutool.core.io.IoUtil;
import org.apache.pulsar.client.api.schema.SchemaDefinition;
import org.apache.pulsar.client.api.schema.SchemaReader;

import java.io.InputStream;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-30 11:04
 */
public interface SchemaDefinitionConfig {


    SchemaDefinition<PulsarMessage> DEFAULT_SCHEMA = SchemaDefinition.<PulsarMessage>builder()
            .withPojo(PulsarMessage.class)
            .withSchemaWriter(JsonUtil::serializerRaw)
            .withSchemaReader(new SchemaReader<PulsarMessage>() {
                @Override
                public PulsarMessage read(byte[] bytes, int offset, int length) {
                    return JsonUtil.deserializerRaw(bytes, offset, length, PulsarMessage.class);
                }

                @Override
                public PulsarMessage read(InputStream inputStream) {
                    byte[] bytes = IoUtil.readBytes(inputStream);
                    return JsonUtil.deserializerRaw(bytes, PulsarMessage.class);
                }
            }).build();
}
