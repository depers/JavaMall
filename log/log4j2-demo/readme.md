# log4j2-demo

# Intro
该项目主要演示Log4j2的相关功能。


# Main Content
* mdc：演示在父子线程中传递MDC的值
* mdc2：演示通过transmittable-thread-local在父子线程中传递MDC的值
* MDCAdapter：这种就是自己实现一个MDCAdapter替换logback/log4j的MDCAdapter，内部将其ThreadLocal替换为TransmittableThreadLocal的实现，关于这种方式的实现，可以参考这篇文章：[初识 DispatcherServlet](https://juejin.cn/post/7146952550766346270)。
* 演示@Slf4j注解topic属性的使用。