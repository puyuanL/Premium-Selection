package premium.manager.service;

import com.github.pagehelper.PageInfo;
import premium.model.dto.system.SysRoleDto;
import premium.model.entity.system.SysRole;

public interface SysRoleService {
    PageInfo<SysRole> findByPage(SysRoleDto sysRoleDto, Integer pageNum, Integer pageSize);
}
