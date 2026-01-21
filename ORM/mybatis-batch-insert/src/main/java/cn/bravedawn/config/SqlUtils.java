package cn.bravedawn.config;

/**
 * @Author : depers
 * @Date : Created in 2026-01-21 17:36
 */
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SqlUtils {

    public static String buildSql(Configuration configuration, BoundSql boundSql) {
        String sql = boundSql.getSql().replaceAll("\\s+", " ");

        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        Object parameterObject = boundSql.getParameterObject();

        if (parameterMappings == null || parameterMappings.isEmpty()) {
            return sql;
        }

        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        MetaObject metaObject = parameterObject == null
                ? null
                : configuration.newMetaObject(parameterObject);

        for (ParameterMapping mapping : parameterMappings) {
            String propertyName = mapping.getProperty();
            Object value;

            if (boundSql.hasAdditionalParameter(propertyName)) {
                value = boundSql.getAdditionalParameter(propertyName);
            } else if (metaObject != null && metaObject.hasGetter(propertyName)) {
                value = metaObject.getValue(propertyName);
            } else {
                value = null;
            }

            sql = sql.replaceFirst("\\?", formatValue(value));
        }

        return sql;
    }

    private static String formatValue(Object value) {
        if (value == null) {
            return "NULL";
        }
        if (value instanceof String) {
            return "'" + value + "'";
        }
        if (value instanceof Date) {
            return "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value) + "'";
        }
        return value.toString();
    }
}
