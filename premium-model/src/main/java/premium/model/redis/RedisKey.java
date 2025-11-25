package premium.model.redis;

import lombok.Getter;

@Getter
public enum RedisKey {
    STOCK_REAL_KEY("product:stock:real:"),      // 实际库存
    STOCK_LOCK_KEY("product:stock:lock:"),      // 预占库存
    ORDER_STOCK_KEY("order:stock:"),            // 订单预占记录
    LOCK_REDIS_KEY("lock:"),                    // redis上锁前缀
    ;

    private final String key;
    RedisKey(String key) {
        this.key = key;
    }
}
