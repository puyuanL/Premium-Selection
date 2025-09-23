package premium.manager.service;

import premium.model.dto.system.LoginDto;
import premium.model.entity.system.SysUser;
import premium.model.vo.system.LoginVo;

public interface SysUserService {

    LoginVo login(LoginDto loginDto);

    SysUser getUserInfo(String token);

    void logout(String token);

}
