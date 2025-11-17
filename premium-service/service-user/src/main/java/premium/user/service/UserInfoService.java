package premium.user.service;

import premium.model.dto.h5.UserLoginDto;
import premium.model.dto.h5.UserRegisterDto;
import premium.model.vo.h5.UserInfoVo;

public interface UserInfoService {
    void register(UserRegisterDto userRegisterDto);

    String login(UserLoginDto userLoginDto);

    UserInfoVo getCurrentUserInfo(String token);
}
