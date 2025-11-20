package premium.feign.cart;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import premium.model.entity.h5.CartInfo;
import premium.model.vo.common.Result;

import java.util.List;

@FeignClient(value = "service-cart")
public interface CartFeignClient {

    @GetMapping(value = "/api/order/cart/auth/getAllChecked")
    List<CartInfo> getAllChecked();

    @GetMapping(value = "/api/order/cart/auth/deleteChecked")
    Result deleteChecked();

}
