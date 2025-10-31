package premium.manager.service;

import premium.model.dto.system.AssignMenuDto;

import java.util.Map;

public interface SysRoleMenuService {
    Map<String, Object> findSysRoleMenuByRoleId(Long roleId);

    void doAssign(AssignMenuDto assignMenuDto);
}
