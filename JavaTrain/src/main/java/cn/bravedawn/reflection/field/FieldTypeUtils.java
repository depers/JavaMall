package cn.bravedawn.reflection.field;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @Author : depers
 * @Date : Created in 2026-01-09 18:22
 */
public class FieldTypeUtils {

    public static boolean isCustomType(Field field) {
        return isCustomType(field.getType());
    }

    public static boolean isCustomType(Class<?> type) {

        // 1. primitive
        if (type.isPrimitive()) {
            return false;
        }

        // 2. enum
        if (type.isEnum()) {
            return false;
        }

        // 3. 常见简单类型
        if (isSimpleType(type)) {
            return false;
        }

        // 4. 集合 / Map
        if (Collection.class.isAssignableFrom(type)
                || Map.class.isAssignableFrom(type)) {
            return false;
        }

        // 5. JDK 内置类型
        Package pkg = type.getPackage();
        if (pkg != null) {
            String pkgName = pkg.getName();
            if (pkgName.startsWith("java.")
                    || pkgName.startsWith("javax.")
                    || pkgName.startsWith("jakarta.")) {
                return false;
            }
        }

        // 剩下的，一般认为是自定义类型
        return true;
    }

    private static boolean isSimpleType(Class<?> type) {
        return type == String.class
                || Number.class.isAssignableFrom(type)
                || type == Boolean.class
                || type == Character.class
                || type == BigDecimal.class
                || type == BigInteger.class
                || Date.class.isAssignableFrom(type)
                || Temporal.class.isAssignableFrom(type)
                || UUID.class == type;
    }
}

