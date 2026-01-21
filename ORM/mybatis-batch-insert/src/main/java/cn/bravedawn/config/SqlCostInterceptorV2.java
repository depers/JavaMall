package cn.bravedawn.config;

/**
 * @Author : depers
 * @Date : Created in 2026-01-21 16:44
 */

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

@Intercepts({
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        ),
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
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
            String sqlId = ms.getId();

            System.out.println(
                    "[MyBatis SQL COST] " + sqlId + " 耗时：" + cost + " ms"
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

