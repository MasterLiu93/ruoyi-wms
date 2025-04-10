package cn.smart.wms.framework.lock.core.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁注解
 * 
 * 基于Redisson的分布式锁注解，使用方式:
 * <pre>
 * {@code @DistributedLock(key = "'order:' + #orderId")}
 * public void processOrder(String orderId) {
 *     // 业务逻辑
 * }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {
    
    /**
     * 锁的key前缀，会自动拼接到key之前，格式为 prefix:key
     */
    String prefix() default "lock";
    
    /**
     * 锁的key，支持SpEL表达式
     * 例如：'shipment:' + #orderId
     */
    String key();
    
    /**
     * 等待锁的最长时间，默认5秒
     */
    long waitTime() default 5;
    
    /**
     * 持有锁的最长时间，默认30秒
     */
    long leaseTime() default 30;
    
    /**
     * 时间单位，默认秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
    
    /**
     * 获取锁失败时的错误消息
     */
    String errorMessage() default "操作正在处理中，请稍后再试";
} 