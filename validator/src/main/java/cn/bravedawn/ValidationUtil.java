package cn.bravedawn;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author : depers
 * @Date : Created in 2025-12-29 18:57
 */

/**
 * Hibernate Validator 校验工具类
 */
public final class ValidationUtil {

    private static final Validator VALIDATOR;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        VALIDATOR = factory.getValidator();
    }

    private ValidationUtil() {
        // 工具类禁止实例化
    }

    /**
     * 校验对象，返回校验错误信息
     */
    public static <T> Set<String> validate(T obj) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(obj);
        return violations.stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.toSet());
    }

    /**
     * 校验对象（支持分组），返回校验错误信息
     */
    public static <T> Set<String> validate(T obj, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(obj, groups);
        return violations.stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.toSet());
    }

    /**
     * 校验对象，校验失败直接抛异常
     */
    public static <T> void validateAndThrow(T obj) {
        Set<String> errors = validate(obj);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errors));
        }
    }

    /**
     * 校验对象（支持分组），校验失败直接抛异常
     */
    public static <T> void validateAndThrow(T obj, Class<?>... groups) {
        Set<String> errors = validate(obj, groups);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errors));
        }
    }
}
