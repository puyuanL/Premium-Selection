package premium.manager.service;

import com.github.pagehelper.PageInfo;
import premium.model.dto.system.AssginRoleDto;
import premium.model.dto.system.LoginDto;
import premium.model.dto.system.SysUserDto;
import premium.model.entity.system.SysUser;
import premium.model.vo.system.LoginVo;

public interface SysUserService {

    LoginVo login(LoginDto loginDto);

    SysUser getUserInfo(String token);

    void logout(String token);

    PageInfo<SysUser> findByPage(Integer pageNum, Integer pageSize, SysUserDto sysUserDto);

    void saveSysUser(SysUser sysUser);

    void updateSysUser(SysUser sysUser);

    void deleteById(Long userId);

    void doAssign(AssginRoleDto assginRoleDto);
}
