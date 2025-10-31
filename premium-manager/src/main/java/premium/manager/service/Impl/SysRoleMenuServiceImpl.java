package premium.manager.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import premium.manager.mapper.SysRoleMenuMapper;
import premium.manager.service.SysMenuService;
import premium.manager.service.SysRoleMenuService;
import premium.model.dto.system.AssignMenuDto;
import premium.model.entity.system.SysMenu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleMenuServiceImpl implements SysRoleMenuService {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public Map<String, Object> findSysRoleMenuByRoleId(Long roleId) {
        Map<String, Object> result = new HashMap<>();

        // select all menu list
        List<SysMenu> sysMenuList = sysMenuService.findNodes();
        result.put("sysMenuList", sysMenuList);
        // select id list which has been assigned menu
        List<Long> roleMenuIds = sysRoleMenuMapper.findSysRoleMenuByRoleId(roleId);
        result.put("roleMenuIds", roleMenuIds);

        return result;
    }

    @Override
    public void doAssign(AssignMenuDto assignMenuDto) {
        // 删除角色之前分配过的菜单数据
        sysRoleMenuMapper.deleteByRoleId(assignMenuDto.getRoleId());
        // 保存分配的数据
        List<Map<String, Number>> menuInfo = assignMenuDto.getMenuIdList();
        if (menuInfo != null && !menuInfo.isEmpty()) {  // 角色分配了菜单
            sysRoleMenuMapper.doAssign(assignMenuDto);
        }
    }
}
