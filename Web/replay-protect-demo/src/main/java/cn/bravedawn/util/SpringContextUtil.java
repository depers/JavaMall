package cn.bravedawn.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author : depers
 * @program : replay-protect-demo
 * @date : Created in 2024/10/29 21:38
 */
@Component
@Order(Integer.MAX_VALUE)
@SuppressWarnings("all")
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    public static Object getBean(Class clazz) {
        return applicationContext.getBean(clazz);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
