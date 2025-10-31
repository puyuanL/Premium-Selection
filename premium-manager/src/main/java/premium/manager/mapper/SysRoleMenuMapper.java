package premium.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.dto.system.AssignMenuDto;

import java.util.List;

@Mapper
public interface SysRoleMenuMapper {
    List<Long> findSysRoleMenuByRoleId(Long roleId);

    void deleteByRoleId(Long roleId);

    void doAssign(AssignMenuDto assignMenuDto);

    void updateSysRoleMenuIsHalf(Long menuId);
}
