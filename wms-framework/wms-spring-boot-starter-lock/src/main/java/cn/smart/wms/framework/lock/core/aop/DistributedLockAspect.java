package cn.smart.wms.framework.lock.core.aop;

import cn.smart.wms.framework.common.exception.ServiceException;
import cn.smart.wms.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.smart.wms.framework.lock.core.annotation.DistributedLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * 分布式锁切面
 * 
 * 处理 @DistributedLock 注解，进行加锁和解锁操作
 */
@Aspect
@Component
public class DistributedLockAspect {

    private static final Logger log = LoggerFactory.getLogger(DistributedLockAspect.class);

    private final RedissonClient redissonClient;
    private final ExpressionParser parser = new SpelExpressionParser();
    private final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    public DistributedLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint point, DistributedLock distributedLock) throws Throwable {
        String lockKey = getLockKey(point, distributedLock);
        RLock lock = redissonClient.getLock(lockKey);

        log.debug("尝试获取分布式锁: {}", lockKey);
        boolean locked = false;

        try {
            locked = lock.tryLock(
                    distributedLock.waitTime(),
                    distributedLock.leaseTime(),
                    distributedLock.timeUnit()
            );

            if (!locked) {
                log.info("获取分布式锁失败: {}", lockKey);
                throw new ServiceException(GlobalErrorCodeConstants.LOCKED.getCode(), distributedLock.errorMessage());
            }

            log.debug("成功获取分布式锁: {}", lockKey);
            return point.proceed();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ServiceException(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(), "获取锁过程被中断");
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("分布式锁操作异常, 锁key: {}", lockKey, e);
            throw e;
        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.debug("释放分布式锁: {}", lockKey);
            }
        }
    }

    private String getLockKey(ProceedingJoinPoint point, DistributedLock distributedLock) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Object[] args = point.getArgs();
        String[] parameterNames = discoverer.getParameterNames(method);

        // 创建表达式上下文
        EvaluationContext context = new StandardEvaluationContext();

        // 设置方法参数
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }
        }

        // 解析SpEL表达式
        Expression expression = parser.parseExpression(distributedLock.key());
        String key = expression.getValue(context, String.class);

        if (key == null) {
            throw new ServiceException(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(), "无法解析分布式锁的key");
        }

        // 组装最终的锁key
        StringBuilder lockKey = new StringBuilder();
        if (StringUtils.hasText(distributedLock.prefix())) {
            lockKey.append(distributedLock.prefix()).append(":");
        }
        lockKey.append(key);

        return lockKey.toString();
    }
} 