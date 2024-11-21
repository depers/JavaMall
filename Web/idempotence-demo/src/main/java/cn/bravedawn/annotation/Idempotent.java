package cn.bravedawn.annotation;

/**
 * @author : depers
 * @program : idempotence-demo
 * @date : Created in 2024/11/21 21:52
 */
public @interface Idempotent {

    /**
     * 前缀
     */
    String prefix();

    /**
     * 唯一性标识，支持SPEL表达式
     */
    String key();

    /**
     * 幂等控制时长，默认45秒
     */
    int duration() default 45;

    /**
     * 在具体逻辑执行结束之后是否删除Key
     */
    boolean removeKeyWhenFinished() default false;

    /**
     * 在具体逻辑执行出现异常之后是否移除Key
     */
    boolean removeKeyWhenError() default false;
}
