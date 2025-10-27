package premium.manager.service;

import com.github.pagehelper.PageInfo;
import premium.model.dto.system.SysRoleDto;
import premium.model.entity.system.SysRole;

import java.util.Map;

public interface SysRoleService {
    PageInfo<SysRole> findByPage(SysRoleDto sysRoleDto, Integer pageNum, Integer pageSize);

    void saveSysRole(SysRole sysRole);

    void updateSysRole(SysRole sysRole);

    void deleteById(Long id);

    Map<String, Object> findAllRoles(Long userId);
}
