package premium.feign.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import premium.model.entity.user.UserAddress;

@FeignClient(value = "service-user")
public interface UserFeignClient {

    @GetMapping("/api/user/userAddress/findUserAddressById/{addressId}")
    UserAddress getUserAddressById(@PathVariable("addressId") Long addressId);

}
