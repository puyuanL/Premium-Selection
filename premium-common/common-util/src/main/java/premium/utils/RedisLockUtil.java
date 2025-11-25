package premium.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import premium.model.redis.RedisKey;

import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class RedisLockUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 锁的过期时间，防止死锁
    private static final long LOCK_EXPIRE = 30000; // 30秒
    // 基础重试间隔（毫秒）
    private static final long BASE_INTERVAL = 50;
    private static final Random RANDOM = new Random();

    /**
     * 获取锁
     * @param key 商品skuId
     * @return 是否获取成功
     */
    public String tryLock(String key) {
        String lockValue = UUID.randomUUID().toString();
        String lockKey = RedisKey.LOCK_REDIS_KEY + key;

        // SETNX 命令：如果key不存在则设置，返回1；存在则返回0
        Boolean success = redisTemplate.opsForValue().setIfAbsent(
                lockKey,
                lockValue,
                LOCK_EXPIRE,
                TimeUnit.MILLISECONDS
        );
        return Boolean.TRUE.equals(success) ? lockValue : null;
    }

    /**
     * 带重试机制的分布式锁获取
     * 延时方案：指数退避 + 随机抖动
     * @param key 锁键
     * @param maxRetryTimes 最大重试次数
     * @return 是否获取成功
     */
    public String tryLockWithRetry(String key, int maxRetryTimes) {
        String lockValue = tryLock(key);
        if (lockValue != null) {
            return lockValue;
        }

        int retryCount = 0;
        while (retryCount < maxRetryTimes) {
            try {
                long baseDelay = BASE_INTERVAL * (1L << retryCount); // 指数退避
                long jitter = RANDOM.nextLong(baseDelay + 1); // 随机抖动
                Thread.sleep(Math.min(baseDelay + jitter, 1000));

                lockValue = tryLock(key);
                if (lockValue != null) {
                    return lockValue;
                }
                retryCount++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        return null;
    }

    /**
     * 释放锁
     * @param key 商品skuId
     */
    public boolean releaseLock(String key, String lockValue) {
        if (lockValue == null) {
            return false;
        }
        String lockKey = RedisKey.LOCK_REDIS_KEY + key;

        // 使用Lua脚本保证"校验标识+删除锁"的原子性
        String luaScript = """
            if redis.call('get', KEYS[1]) == ARGV[1] then
                return redis.call('del', KEYS[1])
            else
                return 0
            end
            """;

        // 执行Lua脚本
        Long result = redisTemplate.execute(
                new DefaultRedisScript<>(luaScript, Long.class),
                Collections.singletonList(lockKey),
                lockValue
        );
        // redisTemplate.delete(RedisKey.LOCK_REDIS_KEY + key);
        return result != null && result > 0;
    }
}