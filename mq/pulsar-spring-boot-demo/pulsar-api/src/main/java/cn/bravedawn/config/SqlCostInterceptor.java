package cn.bravedawn.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Statement;
import java.util.*;

/**
 * @author : depers
 * @program : jasper
 * @description:
 * @date : Created in 2023/4/12 17:14
 */

@Intercepts({
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})
})
public class SqlCostInterceptor implements Interceptor {


    private static final Logger log = LoggerFactory.getLogger(SqlCostInterceptor.class);
    private long normalCost;
    private boolean isOpenSqlFormat;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        long startTime = System.currentTimeMillis();

        Object result = null;
        try {
             result = invocation.proceed();
             return result;
        } finally {
            // 统计sql执行时间
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;

            // 格式化sql
            BoundSql boundSql = statementHandler.getBoundSql();
            String sql = boundSql.getSql();
            Object parameterObject = boundSql.getParameterObject();
            List<ParameterMapping> parameterMappingList = boundSql.getParameterMappings();

            if (isOpenSqlFormat) {
                sql = formatSql(sql, parameterObject, parameterMappingList);
            }

            if (costTime <= normalCost) {
                if (result instanceof List<?> && result != null) {
                    log.info("SQL=[{}], 执行耗时=[{}ms], 执行结果={}.", sql, costTime, ((List<?>) result).size());
                } else {
                    log.info("SQL=[{}], 执行耗时=[{}ms], 执行结果={}.", sql, costTime, result);
                }
            } else {
                if (result instanceof List<?> && result != null) {
                    log.warn("SLOW_SQL=[{}], 执行耗时=[{}ms], 执行结果={}.", sql, costTime, ((List<?>) result).size());
                } else {
                    log.warn("SLOW_SQL=[{}], 执行耗时=[{}ms], 执行结果={}.", sql, costTime, result);
                }
            }

        }
    }


    /**
     * 这个方法的作用是就是让mybatis判断，是否要进行拦截，然后做出决定是否生成一个代理。
     * 下面的做法是生成一个代理对象
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        String cost = properties.getProperty("normalCost", "200");
        String isUse = properties.getProperty("isOpenSqlFormat", "true");

        normalCost = Long.valueOf(cost);
        isOpenSqlFormat = Boolean.valueOf(isUse);
    }


    private String formatSql(String sql, Object parameterObject, List<ParameterMapping> parameterMappingList) {
        // 如果sql为空则返回空
        if (StringUtils.isBlank(sql)) {
            return "";
        }

        // 规整sql
        sql = sql.replaceAll("[\\s]+", " ");
        if (parameterMappingList.size() > 0 && parameterObject != null) {
            // 针对foreach标签中传一个数组过来的处理
            if (parameterObject instanceof MapperMethod.ParamMap) {
                sql = handleParamMapParameter(sql, parameterMappingList, parameterObject);
            } else {
                sql = handleCommonParameter(sql, parameterMappingList, parameterObject);
            }
        }
        return sql;
    }

    private String handleParamMapParameter(String sql, List<ParameterMapping> parameterMappingList, Object parameterObject) {
        Map parameterMap = (Map) parameterObject;
        List<?> list = (List<?>) parameterMap.get("list");

        // 获取对象属性名
        List<String> propertyNameList = new ArrayList<>();
        for (ParameterMapping parameterMapping : parameterMappingList) {
            String propertyName = parameterMapping.getProperty();
            if (propertyName.contains(".")) {
                String objectName = propertyName.substring(propertyName.indexOf(".") + 1);
                if (!propertyNameList.contains(objectName)) {
                    propertyNameList.add(objectName);
                }
            }
        }

        // 从对象中获取属性值
        for (Object obj : list) {
            for (String propertyName : propertyNameList) {
                Object propertyValue = getFieldValue(obj, propertyName);

                if (propertyValue != null) {
                    if (propertyValue.getClass().isAssignableFrom(String.class)) {
                        propertyValue = "\'" + propertyValue + "\'";
                    }
                } else {
                    propertyValue = "\'" + propertyValue + "\'";
                }

                sql = sql.replaceFirst("\\?", propertyValue.toString());
            }
        }
        return sql;

    }

    private String handleCommonParameter(String sql, List<ParameterMapping> parameterMappingList, Object parameterObject) {
        for (ParameterMapping parameterMapping : parameterMappingList) {
            String propertyValue = null;
            if (isPrimitiveOrPrimitiveWrapper(parameterObject.getClass())) {
                propertyValue = parameterObject.toString();
            } else {
                String propertyName = parameterMapping.getProperty();
                propertyValue = String.valueOf(getFieldValue(parameterObject, propertyName));
            }

            if (parameterMapping.getJavaType().isAssignableFrom(String.class) && propertyValue != null) {
                propertyValue = "\'" + propertyValue + "\'";
            }

            sql = sql.replaceFirst("\\?", propertyValue);
        }

        return sql;

    }


    private boolean isPrimitiveOrPrimitiveWrapper(Class<?> cls) {
        return cls.isPrimitive() ||
                cls.isAssignableFrom(Byte.class) || cls.isAssignableFrom(Short.class) ||
                cls.isAssignableFrom(Integer.class) || cls.isAssignableFrom(Long.class) ||
                cls.isAssignableFrom(Double.class) || cls.isAssignableFrom(Float.class) ||
                cls.isAssignableFrom(Character.class) || cls.isAssignableFrom(Boolean.class) ||
                cls.isAssignableFrom(String.class) || cls.isAssignableFrom(BigDecimal.class);
    }

    private Object getFieldValue(Object bean, String fieldNameOrIndex) {
        if (bean != null && fieldNameOrIndex != null) {
            if (bean instanceof Map) {
                return ((Map<?, ?>)bean).get(fieldNameOrIndex);
            } else if (bean instanceof Collection<?>) {
                return ((Collection<?>)bean).toArray()[Integer.parseInt(fieldNameOrIndex)];
            } else if (bean.getClass().isArray()) {
                return Array.get(bean, Integer.parseInt(fieldNameOrIndex));
            } else {

                try {
                    // 如果是对象的话
                    Field field = bean.getClass().getDeclaredField(fieldNameOrIndex);
                    field.setAccessible(true);
                    Object result = field.get(bean);
                    return result;
                } catch (Throwable e) {
                    log.error("解析sql异常", e);
                }
            }
        }
        return null;
    }
}
