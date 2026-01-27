/**
 * @Author : depers
 * @Date : Created in 2026-01-27 10:52
 */
package cn.bravedawn.obj.proxy.cglib.v3;

/**
 * 只生成一个代理对象，但在该对象内部维护一个拦截器列表。 当方法被调用时，通过一个递增的索引（Index）在拦截器链中递归执行。
 *
 * 这种模式不仅减少了内存中代理对象的数量，还能更好地利用 CGLIB 的 MethodProxy 来提升性能。
 */