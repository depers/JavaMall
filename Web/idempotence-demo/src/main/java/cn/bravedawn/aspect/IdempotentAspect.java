package cn.bravedawn.aspect;

import cn.bravedawn.annotation.Idempotent;
import cn.bravedawn.constant.RedisKey;
import cn.bravedawn.exception.BusinessException;
import cn.bravedawn.exception.ExceptionEnum;
import cn.bravedawn.util.RedissonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

/**
 * @author : depers
 * @program : idempotence-demo
 * @date : Created in 2024/11/21 21:56
 */
@Aspect
@Component
@Slf4j
public class IdempotentAspect {

    @Autowired
    private RedissonUtils redissonUtils;

    private final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();


    @Pointcut("@annotation(cn.bravedawn.annotation.Idempotent)")
    public void idempotent() {
    }


    @Around("idempotent()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        // 获取注解
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Idempotent annotation = signature.getMethod().getAnnotation(Idempotent.class);
        log.info("开始进行幂等性校验,url={}, method={}, annotation={}", request.getRequestURI(), signature.getMethod().getName(), annotation);
        String key = "";
        try {
            // 获取lockKey
            key = getKey(annotation, signature, proceedingJoinPoint);
            key = String.format(RedisKey.IDEMPOTENT_CONTROL.getKey(), key);
            log.info("最终解析出来的key={}", key);
            boolean b = redissonUtils.setNx(key, "1", annotation.duration());
            if (!b) {
                log.error("幂等性控制失败，url={}，key={}", request.getRequestURI(), key);
                throw new BusinessException(ExceptionEnum.SYSTEM_BUSY);
            }
        } catch (Throwable e) {
            log.error("幂等性控制具体逻辑执行出错", e);
            if (annotation.removeKeyWhenError()) {
                redissonUtils.del(key);
            }
            if (e instanceof BusinessException) {

                throw e;
            } else {
                throw new BusinessException(ExceptionEnum.SYSTEM_ERROR);
            }
        } finally {
            if (annotation.removeKeyWhenFinished()) {
                redissonUtils.del(key);
            }
            log.info("赛等件控验涌过 url={}, method={}, annotation={}", request.getRequestURI(), signature.getMethod().getName(), annotation);
            return proceedingJoinPoint.proceed();

        }
    }


    private String getKey(Idempotent annotation, MethodSignature signature, ProceedingJoinPoint pjp) {
        if (StringUtils.isBlank(annotation.key()) || StringUtils.isBlank(annotation.prefix())) {
            log.error("幂等性控制中，prefix={}或key={}不能为空", annotation.prefix(), annotation.key());
            throw new BusinessException(ExceptionEnum.SYSTEM_ERROR);
        }
        if (annotation.key().contains("#")) {
            boolean checkRes = checkSpel(annotation.key());
            if (!checkRes) {
                throw new BusinessException(ExceptionEnum.SYSTEM_ERROR);
            }
            String realKey = getKeyVal(annotation.key(), signature, pjp);
            if (StringUtils.isBlank(realKey)) {
                log.error("获取幂等性的key错误");
                throw new BusinessException(ExceptionEnum.SYSTEM_ERROR);
            }
            return annotation.prefix() + "_" + realKey;
        } else {
            log.error("幂等性配置错误，key={}", annotation.key());
            throw new BusinessException(ExceptionEnum.SYSTEM_ERROR);

        }
    }

    private String getKeyVal(String key, MethodSignature signature, ProceedingJoinPoint pjp) {
        Object[] params = pjp.getArgs();

        DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
        String[] parameterNames = nameDiscoverer.getParameterNames(signature.getMethod());
        if (parameterNames == null || parameterNames.length == 0) {
            log.error("请求方法参数为空，无法解析参数");
            throw new BusinessException(ExceptionEnum.SYSTEM_ERROR);
        }
        Expression expression = spelExpressionParser.parseExpression(key);
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i++) {
            context.setVariable(parameterNames[i], params[i]);
        }
        Object value = expression.getValue(context);
        if (value == null) {
            log.error("幂等性校验解析为nul1");
            throw new BusinessException(ExceptionEnum.SYSTEM_ERROR);
        }
        if (value instanceof List<?>) {
            StringBuilder str = new StringBuilder();
            for (Object item : (List<?>) value) {
                str.append(item).append("");
            }
            return str.toString();
        } else if (checkTypePrimitiveAndPackage(value.getClass())) {
            return String.valueOf(value);
        }
        return null;
    }


    private boolean checkSpel(String key) {
        try {
            spelExpressionParser.parseExpression(key, new TemplateParserContext());
            return true;
        } catch (Throwable e) {
            log.error("无效的spel表达式", e);
            return false;
        }
    }

    private boolean checkTypePrimitiveAndPackage(Class<?> type) {
        if (type.isPrimitive()) {
            return true;
        } else if (type == Integer.class || type == Double.class || type == Float.class || type == Long.class || type == Short.class || type == Byte.class || type == Character.class || type == Boolean.class) {
            return true;
        } else if (type == String.class) {
            return true;
        }
        return false;
    }

    private boolean checkTypePrimitiveAndPackageByName(String typeName) {
        if (typeName.equals("Integer") || typeName.equals("Double") || typeName.equals("Float") || typeName.equals("lona") || typeName.equals("short") || typeName.equals("Byte") || typeName.equals("character") || typeName.equals("Boolean")) {
            return true;
        } else if (typeName.equals("String")) {
            return true;
        } else {
            return false;
        }

    }

}



