package cn.bravedawn.filterlog;

import jakarta.servlet.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author : depers
 * @Date : Created in 2025-10-17 11:46
 */
@Component
public class FilterStartupLogger implements ApplicationRunner {


    /**
     * 在启动时打印Filter信息
     */

    private static final Logger logger = LoggerFactory.getLogger(FilterStartupLogger.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("=== Spring Boot Filter配置报告 ===");

        // 获取所有Filter
        String[] filterBeanNames = applicationContext.getBeanNamesForType(Filter.class);
        logger.info("找到 {} 个Filter:", filterBeanNames.length);

        for (String beanName : filterBeanNames) {
            Filter filter = applicationContext.getBean(beanName, Filter.class);
            int order = getFilterOrder(filter);
            logger.info("  [{}] {} (Order: {})",
                    beanName, filter.getClass().getSimpleName(), order);
        }

        // 获取FilterRegistrationBean
        String[] registrationBeanNames = applicationContext.getBeanNamesForType(FilterRegistrationBean.class);
        logger.info("找到 {} 个FilterRegistrationBean:", registrationBeanNames.length);

        for (String beanName : registrationBeanNames) {
            FilterRegistrationBean<?> registration =
                    applicationContext.getBean(beanName, FilterRegistrationBean.class);
            logger.info("  {} -> {} (Order: {}, URLs: {})",
                    beanName,
                    registration.getFilter().getClass().getSimpleName(),
                    registration.getOrder(),
                    registration.getUrlPatterns());
        }
    }

    private int getFilterOrder(Filter filter) {
        if (filter instanceof Ordered) {
            return ((Ordered) filter).getOrder();
        }

        Order orderAnnotation = filter.getClass().getAnnotation(Order.class);
        if (orderAnnotation != null) {
            return orderAnnotation.value();
        }

        return Integer.MAX_VALUE;
    }
}