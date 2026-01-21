package cn.bravedawn.config;

/**
 * @Author : depers
 * @Date : Created in 2026-01-21 16:44
 */

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

@Intercepts({
        // 普通查询（MyBatis）
        @Signature(
                type = Executor.class,
                method = "query",
                args = {
                        MappedStatement.class,
                        Object.class,
                        RowBounds.class,
                        ResultHandler.class
                }
        ),
        // MyBatis-Plus / QueryWrapper / 分页 常用
        @Signature(
                type = Executor.class,
                method = "query",
                args = {
                        MappedStatement.class,
                        Object.class,
                        RowBounds.class,
                        ResultHandler.class,
                        CacheKey.class,
                        BoundSql.class
                }
        ),
        // INSERT / UPDATE / DELETE
        @Signature(
                type = Executor.class,
                method = "update",
                args = {
                        MappedStatement.class,
                        Object.class
                }
        )
})
public class SqlCostInterceptorV2 implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        long start = System.currentTimeMillis();
        try {
            return invocation.proceed();
        } finally {

            long cost = System.currentTimeMillis() - start;
            MappedStatement ms = (MappedStatement) invocation.getArgs()[0];

            System.out.println(
                    "[MyBatis SQL COST] " +
                            ms.getSqlCommandType() + " | " +
                            ms.getId() + " | " +
                            cost + " ms"
            );
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}


