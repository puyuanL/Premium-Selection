package premium.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import premium.model.dto.h5.UserRegisterDto;
import premium.model.vo.common.Result;
import premium.model.vo.common.ResultCodeEnum;
import premium.user.service.UserInfoService;

@RestController
@RequestMapping("/api/user/userInfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("register")
    public Result register(@RequestBody UserRegisterDto userRegisterDto) {
        userInfoService.register(userRegisterDto);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }
}
