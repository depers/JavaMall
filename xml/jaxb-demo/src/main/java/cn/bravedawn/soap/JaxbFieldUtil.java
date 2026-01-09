package cn.bravedawn.soap;

/**
 * @Author : depers
 * @Date : Created in 2026-01-09 14:20
 */
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public final class JaxbFieldUtil {

    private JaxbFieldUtil() {}

    /**
     * 获取字段名 → XML 名称 映射
     */
    public static Map<String, String> getFieldXmlNameMap(Object obj) throws IllegalAccessException {
        Map<String, String> result = new LinkedHashMap<>();

        Class<?> clazz = obj.getClass();
        for (Field field : getAllFields(clazz)) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            String xmlName = resolveXmlName(field);

            field.setAccessible(true);
            result.put(xmlName, field.get(obj).toString());
        }
        return result;
    }

    /**
     * 解析字段对应的 XML 名称
     */
    private static String resolveXmlName(Field field) {
        // @XmlElement
        XmlElement xmlElement = field.getAnnotation(XmlElement.class);
        if (xmlElement != null) {
            return isBlank(xmlElement.name())
                    ? field.getName()
                    : xmlElement.name();
        }

        // 默认
        return field.getName();
    }

    /**
     * 获取当前类 + 父类的所有字段
     */
    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isEmpty() || "##default".equals(value);
    }
}

