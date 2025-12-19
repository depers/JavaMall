package cn.bravedawn.util;

/**
 * @Author : depers
 * @Date : Created in 2025-12-19 11:02
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.util.TimeZone;

@Slf4j
public class JsonUtil {

    private static final ObjectMapper INSTANCE;

    static {
        INSTANCE = new ObjectMapper();

        // --- 核心配置 ---
        // 1. 自动注册所有模块（支持 Java8 日期等）
        INSTANCE.registerModule(new JavaTimeModule());

        // 2. 忽略在 JSON 中存在但在 Java 对象中不存在的属性，防止序列化失败
        INSTANCE.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 3. 忽略空 Bean 转 JSON 的错误
        INSTANCE.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 4. 序列化时忽略 null 值字段（可选）
        // INSTANCE.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 5. 统一日期格式
        INSTANCE.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }

    /**
     * 对象转 JSON 字符串
     */
    public static String toJsonStr(Object obj) {
        if (obj == null) return null;
        try {
            return INSTANCE.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("JSON 序列化失败: {}", e.getMessage(), e);
            throw new RuntimeException("Json serialization error", e);
        }
    }

    /**
     * JSON 字符串转简单对象
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) return null;
        try {
            return INSTANCE.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("JSON 反序列化失败: {}", e.getMessage(), e);
            throw new RuntimeException("Json deserialization error", e);
        }
    }

    /**
     * JSON 字符串转复杂类型（如 List<User>, Map<String, Object>）
     */
    public static <T> T parseReference(String json, TypeReference<T> typeReference) {
        if (json == null || json.isEmpty()) return null;
        try {
            return INSTANCE.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("JSON 复杂反序列化失败: {}", e.getMessage(), e);
            throw new RuntimeException("Json deserialization error", e);
        }
    }

    /**
     * 获取原生 ObjectMapper (用于扩展配置)
     */
    public static ObjectMapper getInstance() {
        return INSTANCE;
    }
}
