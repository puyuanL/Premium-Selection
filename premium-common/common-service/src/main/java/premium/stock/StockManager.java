package premium.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import premium.common.exception.MyException;
import premium.model.redis.RedisKey;
import premium.model.vo.common.ResultCodeEnum;
import premium.utils.RedisLockUtil;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class StockManager {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisLockUtil redisLockUtil;

//    // 库存Redis键前缀
//    private static final String STOCK_REAL_KEY = "product:stock:real:";      // 实际库存
//    private static final String STOCK_LOCK_KEY = "product:stock:lock:";      // 预占库存
//    private static final String ORDER_STOCK_KEY = "order:stock:";            // 订单预占记录

    /**
     * 下单时预占库存
     * @param skuId 商品ID
     * @param num 数量
     * @param orderNo 订单号
     * @return 是否成功
     */
    public boolean lockStock(Long skuId, Integer num, String orderNo) {
        String skuKey = skuId.toString();
        String lockKey = "lock:stock:" + skuKey;

        // 获取分布式锁
        String lockValue = redisLockUtil.tryLockWithRetry(lockKey, 5);
        if (lockValue == null) {
            throw new MyException(ResultCodeEnum.SYSTEM_BUSY);
            // return false;   // 系统繁忙
        }

        try {
            String realStockKey = RedisKey.STOCK_REAL_KEY + skuKey;
            String lockStockKey = RedisKey.STOCK_LOCK_KEY + skuKey;

            // 获取当前实际库存和已预占库存
            String realStockStr = redisTemplate.opsForValue().get(realStockKey);
            String lockStockStr = redisTemplate.opsForValue().get(lockStockKey);
            Integer realStock = Integer.parseInt(realStockStr == null ? "0" : realStockStr);
            Integer lockedStock = Integer.parseInt(lockStockStr == null ? "0" : lockStockStr);

            // 检查库存是否充足：实际库存 - 已预占库存 >= 本次预占数量
            if (realStock - lockedStock < num) {
                return false;   // 库存不足
            }

            // 增加预占库存
            redisTemplate.opsForValue().increment(lockStockKey, num);
            // 记录订单预占信息（用于后续释放）
            redisTemplate.opsForHash().put(RedisKey.ORDER_STOCK_KEY + orderNo, skuKey, num.toString());
            // 设置订单库存预占过期时间（30分钟，应对支付超时）
            redisTemplate.expire(RedisKey.ORDER_STOCK_KEY + orderNo, 30, TimeUnit.MINUTES);

            return true;
        } finally {
            // 释放锁
            redisLockUtil.releaseLock(lockKey, lockValue);
        }
    }

    /**
     * 支付成功后扣减实际库存
     * @param orderNo 订单号
     * @return 是否成功
     * don't need use redis lock
     */
    public boolean deductStock(String orderNo) {
        String orderStockKey = RedisKey.ORDER_STOCK_KEY + orderNo;

        // 获取订单预占的库存信息
        Map<Object, Object> stockMap = redisTemplate.opsForHash().entries(orderStockKey);
        if (stockMap.isEmpty()) {
            return false;
        }

        // 遍历扣减实际库存
        for (Map.Entry<Object, Object> entry : stockMap.entrySet()) {
            String skuKey = entry.getKey().toString();
            Integer num = Integer.parseInt(entry.getValue().toString());

            String realStockKey = RedisKey.STOCK_REAL_KEY + skuKey;
            String lockStockKey = RedisKey.STOCK_LOCK_KEY + skuKey;

            // 扣减实际库存
            redisTemplate.opsForValue().decrement(realStockKey, num);
            // 释放预占库存
            redisTemplate.opsForValue().decrement(lockStockKey, num);
        }

        // 删除订单预占记录
        redisTemplate.delete(orderStockKey);

        return true;
    }

    /**
     * 取消订单时释放预占库存
     * @param orderNo 订单号
     * @return 是否成功
     * don't need use redis lock
     */
    public boolean releaseStock(String orderNo) {
        String orderStockKey = RedisKey.ORDER_STOCK_KEY + orderNo;

        // 获取订单预占的库存信息
        Map<Object, Object> stockMap = redisTemplate.opsForHash().entries(orderStockKey);
        if (stockMap.isEmpty()) {
            return false;
        }

        // 遍历释放预占库存
        for (Map.Entry<Object, Object> entry : stockMap.entrySet()) {
            String skuKey = entry.getKey().toString();
            Integer num = Integer.parseInt(entry.getValue().toString());

            String lockStockKey = RedisKey.STOCK_LOCK_KEY + skuKey;

            // 释放预占库存
            redisTemplate.opsForValue().decrement(lockStockKey, num);
        }

        // 删除订单预占记录
        redisTemplate.delete(orderStockKey);

        return true;
    }

    /**
     * 初始化商品库存到Redis
     * @param skuId 商品ID
     * @param stockNum 库存数量
     */
    public void initStock(Long skuId, Integer stockNum) {
        String skuKey = skuId.toString();
        redisTemplate.opsForValue().setIfAbsent(RedisKey.STOCK_REAL_KEY + skuKey, stockNum.toString());
        redisTemplate.opsForValue().setIfAbsent(RedisKey.STOCK_LOCK_KEY + skuKey, "0");
    }
}
