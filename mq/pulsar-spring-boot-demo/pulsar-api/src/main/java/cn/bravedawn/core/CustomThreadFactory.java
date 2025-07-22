package cn.bravedawn.core;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-22 17:11
 */
@Slf4j
public class CustomThreadFactory implements ThreadFactory {

    private String prefix;
    private LongAdder increment = new LongAdder();


    public CustomThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(prefix + "-" + increment);
        thread.setUncaughtExceptionHandler((t, e) -> {
            log.error("线程执行出现异常", e);
        });
        this.increment.increment();
        return thread;
    }
}
