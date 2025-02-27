package cn.bravedawn.beanfactorydemo;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author : depers
 * @program : spring-demo
 * @description:
 * @date : Created in 2023/5/21 12:05
 */

public class BeanLifeCycle {

    /**
     * 运行这段代码的打印输出如下:
     * InstantiationAwareBeanPostProcessor.postProcessBeforeInstantiation
     * 调用Car()构造函数，进行实例化
     * InstantiationAwareBeanPostProcessor.postProcessAfterInstantiation
     * InstantiationAwareBeanPostProcessor.postProcessPropertyValues
     * 调用setBrand()设置属性值
     * 调用BeanNameAware.setBeanName()方法
     * 调用BeanFactoryAware.setBeanFactory()方法
     * 调用BeanPostProcessor.postProcessBeforeInitialization(), color为空，设置为默认黑色。
     * 调用InitializingBean.afterPropertiesSet()方法
     * 调用init-method所指定的myInit()，将maxSpeed设置为240
     * 调用BeanPostProcessor.postProcessAfterInitialization(), 将maxSpeed调整为200。
     * brand: 红旗CA72, color: 黑色, maxSpeed: 200
     * car==car2：true
     * 调用DisposableBean.destroy()方法
     * 调用destroy-method所指定的myDestroy方法
     */

    private static void lifeCycleInBeanFactory() {
        Resource resource = new ClassPathResource("cn/bravedawn/beanfactory/bean2.xml");
        BeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader((DefaultListableBeanFactory)beanFactory);
        reader.loadBeanDefinitions(resource);

        ConfigurableBeanFactory bf = (ConfigurableBeanFactory) beanFactory;
        bf.addBeanPostProcessor(new MyBeanPostProcessor());
        bf.addBeanPostProcessor(new MyInstantiationAwareBeanPostProcessor());

        Car car = (Car) bf.getBean("car");
        car.introduce();
        car.setColor("红色");

        Car car2 = (Car) bf.getBean("car");
        System.out.println("car==car2：" + (car2 == car));

        bf.destroySingletons();
    }


    public static void main(String[] args) {
        lifeCycleInBeanFactory();
    }
}
