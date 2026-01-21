package cn.bravedawn.config;

/**
 * @Author : depers
 * @Date : Created in 2026-01-21 17:25
 */
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

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
public class SqlCostInterceptorV3 implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        long start = System.currentTimeMillis();

        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs().length > 1
                ? invocation.getArgs()[1]
                : null;

        BoundSql boundSql = ms.getBoundSql(parameter);

        Object result = invocation.proceed();

        long cost = System.currentTimeMillis() - start;

        String fullSql = SqlUtils.buildSql(ms.getConfiguration(), boundSql);

        System.out.println("======================================");
        System.out.println("SQL 类型 : " + ms.getSqlCommandType());
        System.out.println("SQL ID   : " + ms.getId());
        System.out.println("SQL 语句 : " + fullSql);
        System.out.println("耗时     : " + cost + " ms");
        System.out.println("======================================");

        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}

