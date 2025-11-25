package premium.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisLockUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 锁的过期时间，防止死锁
    private static final long LOCK_EXPIRE = 30000; // 30秒

    /**
     * 获取锁
     * @param key 商品skuId
     * @return 是否获取成功
     */
    public boolean tryLock(String key) {
        // SETNX 命令：如果key不存在则设置，返回1；存在则返回0
        Boolean result = redisTemplate
                .opsForValue()
                .setIfAbsent(
                        "lock:" + key,
                        "1", LOCK_EXPIRE, TimeUnit.MILLISECONDS
                );
        return Boolean.TRUE.equals(result);
    }

    /**
     * 释放锁
     * @param key 商品skuId
     */
    public void releaseLock(String key) {
        redisTemplate.delete("lock:" + key);
    }
}