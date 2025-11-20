package premium.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import premium.model.entity.user.UserAddress;
import premium.model.vo.common.Result;
import premium.model.vo.common.ResultCodeEnum;
import premium.user.service.UserAddressService;

import java.util.List;

@RestController
@RequestMapping(value="/api/user/userAddress")
@SuppressWarnings({"unchecked", "rawtypes"})
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    @Operation(summary = "获取用户地址列表")
    @GetMapping("/auth/findUserAddressList")
    public Result<List<UserAddress>> findUserAddressList() {
        List<UserAddress> list = userAddressService.findUserAddressList();
        return Result.build(list , ResultCodeEnum.SUCCESS) ;
    }

    /**
     * 远程调用，获取用户收货地址
     */
    @GetMapping("/findUserAddressById/{addressId}")
    public UserAddress getUserAddressById(@PathVariable("addressId") Long addressId) {
        return userAddressService.getUserAddressById(addressId);
    }
}