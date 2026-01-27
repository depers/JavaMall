/**
 * @Author : depers
 * @Date : Created in 2026-01-27 10:49
 */
package cn.bravedawn.obj.proxy.cglib.v2;

/**
 * 一个基于 CGLIB 的简易 AOP（面向切面编程）框架，通过链式代理的方式实现了多个拦截器的嵌套执行。
 *
 * 这段代码虽然使用了 CGLIB 来生成代理类，但在核心执行逻辑上确实避开了 CGLIB 推荐生成的高性能 FastClass，无法使用MethodProxy.invokeSuper()和MethodProxy.invoke()
 * 转而使用了传统的 JDK 反射 (Method.invoke())。
 */