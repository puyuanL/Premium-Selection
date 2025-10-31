package premium.manager.service;

import premium.model.entity.system.SysMenu;
import premium.model.vo.system.SysMenuVo;

import java.util.List;

public interface SysMenuService {
    List<SysMenu> findNodes();

    void save(SysMenu sysMenu);

    void update(SysMenu sysMenu);

    void removeById(Long id);

    List<SysMenuVo> findMenusByUserId();
}
