package cn.smart.wms.framework.idgenerator.core;

import cn.smart.wms.framework.common.exception.ServiceException;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redisson的分布式ID生成器
 */
public class RedissonIdGenerator {

    /**
     * 日期格式
     */
    private static final String DATE_FORMAT = "yyyyMMdd";

    /**
     * 最大重试次数
     */
    private static final int MAX_RETRY_ATTEMPTS = 3;

    private final RedissonClient redissonClient;

    @Autowired
    public RedissonIdGenerator(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 生成通用ID
     *
     * @param prefix 前缀
     * @param category 类别
     * @return 生成的ID
     * @throws ServiceException 当生成ID失败时抛出异常
     */
    public String generateId(String prefix, int category) {
        if (prefix == null || prefix.isEmpty()) {
            throw new ServiceException(400, "前缀不能为空");
        }
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        String categoryStr = String.valueOf(category);

        for (int attempt = 0; attempt < MAX_RETRY_ATTEMPTS; attempt++) {
            try {
                String newId = generateIdWithRetry(prefix, categoryStr, dateStr);
                if (newId != null) {
                    return newId;
                }
            } catch (Exception e) {
                if (attempt == MAX_RETRY_ATTEMPTS - 1) {
                    throw new ServiceException(500, "尝试生成ID失败，已重试" + MAX_RETRY_ATTEMPTS + "次: " + e.getMessage());
                }
            }
        }

        throw new ServiceException(500, "生成ID失败");
    }

    private String generateIdWithRetry(String prefix, String categoryStr, String dateStr) {
        String currentYear = dateStr.substring(0, 4);
        String redisKey = prefix.toLowerCase() + ":" + categoryStr + ":seq:" + currentYear;
        String digitsKey = prefix.toLowerCase() + ":" + categoryStr + ":digits:" + currentYear;
        String lastYearKey = prefix.toLowerCase() + ":" + categoryStr + ":last_year";

        // 检查年份是否变更
        Object storedYearObj = redissonClient.getBucket(lastYearKey).get();
        String storedYear = storedYearObj != null ? storedYearObj.toString() : null;
        if (!currentYear.equals(storedYear)) {
            // 年份变更，重置序列号和位数
            redissonClient.getBucket(lastYearKey).set(currentYear);
            redissonClient.getAtomicLong(redisKey).set(0);
            redissonClient.getAtomicLong(digitsKey).set(6); // 重置为6位
        }

        // 获取当前序列号位数
        RAtomicLong digitsAtomic = redissonClient.getAtomicLong(digitsKey);
        long currentDigits = digitsAtomic.get();
        if (currentDigits == 0) {
            currentDigits = 6; // 默认6位
            digitsAtomic.set(currentDigits);
        }

        // 使用 Redisson 的原子操作获取序列号
        RAtomicLong atomicLong = redissonClient.getAtomicLong(redisKey);
        long sequence = atomicLong.incrementAndGet();

        // 计算当前位数对应的最大值
        long maxSequence = (long) Math.pow(10, currentDigits) - 1;

        // 如果超过当前位数的最大值，增加位数
        if (sequence > maxSequence) {
            currentDigits++;
            digitsAtomic.set(currentDigits);
            atomicLong.set(1);
            sequence = 1;
        }

        // 生成ID，使用动态位数的序列号格式
        String sequenceStr = String.format("%0" + currentDigits + "d", sequence);
        String newId = prefix + categoryStr + dateStr + sequenceStr;

        // 使用 Redisson 的分布式锁确保ID唯一性
        String lockKey = prefix.toLowerCase() + ":lock:" + newId;
        boolean success = redissonClient.getBucket(lockKey).trySet(System.currentTimeMillis(), 24, TimeUnit.HOURS);

        return success ? newId : null;
    }

    /**
     * 检查编号是否已存在
     *
     * @param prefix 模块前缀
     * @param id     要检查的ID
     * @return true if exists, false otherwise
     */
    public boolean isIdExists(String prefix, String id) {
        if (id == null || id.isEmpty()) {
            return false;
        }
        String lockKey = prefix.toLowerCase() + ":lock:" + id;
        return redissonClient.getBucket(lockKey).isExists();
    }

    /**
     * 清理过期的编号缓存（可以通过定时任务调用）
     *
     * @param prefix 模块前缀
     */
    public void cleanupExpiredIds(String prefix) {
        String pattern = prefix.toLowerCase() + ":lock:*";
        redissonClient.getKeys().deleteByPattern(pattern);
    }
} 