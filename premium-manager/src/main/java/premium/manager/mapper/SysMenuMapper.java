package premium.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.entity.system.SysMenu;

import java.util.List;

@Mapper
public interface SysMenuMapper {
    List<SysMenu> findAll();

    void save(SysMenu sysMenu);

    void update(SysMenu sysMenu);

    int selectCountById(Long id);

    void delete(Long id);

    List<SysMenu> findMenusByUserId(Long userId);

    SysMenu selectParentMenu(Long parentId);
}
