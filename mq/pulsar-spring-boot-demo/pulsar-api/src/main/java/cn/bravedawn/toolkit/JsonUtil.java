package cn.bravedawn.toolkit;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-30 10:21
 */
@Slf4j
public class JsonUtil {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.example")       // 允许 com.example 包下所有子类
                .allowIfSubType("java.util.ArrayList")
                .build();

        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        // 涉及日期的传递设置格式
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_MS_PATTERN));
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
    }


    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象json序列化失败");
        }
        return "";
    }


    public static <T> T toObj(String json, Class<T> tClass) {
        try {
            return objectMapper.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            log.error("对象json反序列化失败");
        }
        return null;
    }

    public static <T> T toObkect(String json, TypeReference<T> tTypeReference) {
        try {
            return objectMapper.readValue(json, tTypeReference);
        } catch (JsonProcessingException e) {
            log.error("对象json反序列化失败");
        }
        return null;
    }

    public static byte[] serializerRaw(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            log.error("对象json序列化失败");
        }
        return new byte[0];
    }


    public static <T> T deserializerRaw(byte[] data, Class<T> tClass) {
        try {
            return objectMapper.readValue(data, tClass);
        } catch (Throwable e) {
            log.error("对象json反序列化失败");
        }
        return null;
    }

    public static <T> T deserializerRaw(byte[] bytes, int offset, int length, Class<T> tClass) {
        try {
            return objectMapper.readValue(bytes, offset, length, tClass);
        } catch (Throwable e) {
            log.error("对象json反序列化失败");
        }
        return null;
    }

}
