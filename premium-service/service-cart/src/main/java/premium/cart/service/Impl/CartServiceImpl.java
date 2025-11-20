package premium.cart.service.Impl;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import premium.cart.service.CartService;
import premium.feign.product.ProductFeignClient;
import premium.model.entity.h5.CartInfo;
import premium.model.entity.product.ProductSku;
import premium.utils.AuthContextUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ProductFeignClient productFeignClient;

    private String getCartKey(Long userId) {
        // key:  user:cart:userId
        return "user:cart:" + userId;
    }

    @Override
    public void addToCart(Long skuId, Integer skuNum) {
        // 获取用户信息
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = this.getCartKey(userId);

        // 从 Redis 里面获取购物车数据 (已存在商品数量相加；不存在商品添加购物车)
        // hash:    key:userId ; field:skuId ; value:sku information CartInfo
        Object cartInfoObj = redisTemplate.opsForHash().get(cartKey, String.valueOf(skuId));
        CartInfo cartInfo;
        if (cartInfoObj != null) {
            cartInfo = JSON.parseObject(cartInfoObj.toString(), CartInfo.class);
            cartInfo.setSkuNum(cartInfo.getSkuNum() + skuNum);
            cartInfo.setIsChecked(1);
            cartInfo.setUpdateTime(new Date());
        } else {
            cartInfo = new CartInfo();
            // Nacos + OpenFeign 远程调用实现，根据skuId获取商品sku信息
            ProductSku productSku = productFeignClient.getBySkuId(skuId);

            cartInfo.setCartPrice(productSku.getSalePrice());
            cartInfo.setSkuNum(skuNum);
            cartInfo.setSkuId(skuId);
            cartInfo.setUserId(userId);
            cartInfo.setImgUrl(productSku.getThumbImg());
            cartInfo.setSkuName(productSku.getSkuName());
            cartInfo.setIsChecked(1);
            cartInfo.setCreateTime(new Date());
            cartInfo.setUpdateTime(new Date());
        }
        redisTemplate.opsForHash().put(cartKey, String.valueOf(skuId) ,JSON.toJSONString(cartInfo));
    }

    @Override
    public List<CartInfo> getCartList() {
        String cartKey = this.getCartKey(AuthContextUtil.getUserInfo().getId());
        List<Object> valueList = redisTemplate.opsForHash().values(cartKey);
        if (!CollectionUtils.isEmpty(valueList)) {
            return valueList.stream().map(
                    cartInfoObj -> JSON.parseObject(cartInfoObj.toString(), CartInfo.class)
            ).toList();
        }

        return new ArrayList<>();
    }

    @Override
    public void deleteCart(Long skuId) {
        redisTemplate.opsForHash().delete(
                this.getCartKey(AuthContextUtil.getUserInfo().getId()),
                String.valueOf(skuId)
        );
    }

    @Override
    public void checkCart(Long skuId, Integer isChecked) {
        String cartKey = this.getCartKey(AuthContextUtil.getUserInfo().getId());
        String skuIdStr = String.valueOf(skuId);

        Object cartInfoObj = redisTemplate.opsForHash().get(cartKey, skuIdStr);
        if (cartInfoObj != null) {
            String cartInfoJSON = cartInfoObj.toString();
            CartInfo cartInfo = JSON.parseObject(cartInfoJSON, CartInfo.class);
            cartInfo.setIsChecked(isChecked);
            redisTemplate.opsForHash().put(cartKey, skuIdStr, JSON.toJSONString(cartInfo));
        }
    }

    @Override
    public void allCheckCart(Integer isChecked) {
        String cartKey = this.getCartKey(AuthContextUtil.getUserInfo().getId());
        List<Object> objList = redisTemplate.opsForHash().values(cartKey);
        if (!CollectionUtils.isEmpty(objList)) {
            List<CartInfo> cartInfoList = objList.stream()
                    .map(obj -> JSON.parseObject(obj.toString(), CartInfo.class))
                    .toList();
            cartInfoList.forEach(cartInfo -> {
                cartInfo.setIsChecked(isChecked);
                redisTemplate.opsForHash().put(
                        cartKey, String.valueOf(cartInfo.getSkuId()), JSON.toJSONString(cartInfo)
                );
            });
        }

    }

    @Override
    public void clearCart() {
        String cartKey = this.getCartKey(AuthContextUtil.getUserInfo().getId());
        redisTemplate.delete(cartKey);
    }

    @Override
    public List<CartInfo> getAllChecked() {
        String cartKey = this.getCartKey(AuthContextUtil.getUserInfo().getId());
        List<Object> objList = redisTemplate.opsForHash().values(cartKey);
        if (!CollectionUtils.isEmpty(objList)) {
            return objList.stream()
                    .map(obj -> JSON.parseObject(obj.toString(), CartInfo.class))
                    .filter(obj -> obj.getIsChecked() == 1)
                    .toList();
        }
        return null;
    }

    @Override
    public void deleteChecked() {
        String cartKey = this.getCartKey(AuthContextUtil.getUserInfo().getId());
        List<Object> objList = redisTemplate.opsForHash().values(cartKey);

        objList.stream()
                .map(obj -> JSON.parseObject(obj.toString(), CartInfo.class))
                .filter(cartInfo -> cartInfo.getIsChecked() == 1)
                .forEach(cartInfo ->
                    redisTemplate.opsForHash().delete(
                            cartKey, String.valueOf(cartInfo.getSkuId())
                    )
                );
    }
}
