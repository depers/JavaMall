package cn.bravedawn.aspect;

import cn.bravedawn.util.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : depers
 * @program : replay-protect-demo
 * @date : Created in 2024/10/29 20:59
 */
@Aspect
@Component
@Slf4j
public class ReplayProtectAspect {
    private static final Map<Class<?>, Map<String, Object>> CACHE_MAP = new ConcurrentHashMap<>();

    @Pointcut("execution(* cn.bravedawn.controller.*.*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("开始进行放重放验证");
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();

        String timeStamp = request.getHeader("timestamp");
        String nonce = request.getHeader("nonce");
        String sign = request.getHeader("sign");
        Object[] args = proceedingJoinPoint.getArgs();
        Map<String, Object> requestParam = new HashMap<>();
        for (Object arg : args) {
            Class<?> cls = arg.getClass();
            if (CACHE_MAP.containsKey(cls)) {
                requestParam.putAll(CACHE_MAP.get(cls));
            } else {
                Map<String, Object> argObjectMap = ReflectionUtil.objectToMap(arg);
                requestParam.putAll(argObjectMap);
                CACHE_MAP.put(cls, argObjectMap);
                ReplayProtection replayProtection = ReplayProtection.getInstance();
                replayProtection.validateSign(timeStamp, nonce, sign, requestParam);
            }
        }

        return proceedingJoinPoint.proceed(args);
    }
}
