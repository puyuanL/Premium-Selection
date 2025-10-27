package premium.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.dto.system.SysRoleDto;
import premium.model.entity.system.SysRole;

import java.util.List;

@Mapper
public interface SysRoleMapper {

    List<SysRole> findByPage(SysRoleDto sysRoleDto);

    void save(SysRole sysRole);

    void update(SysRole sysRole);

    void delete(Long id);

    List<SysRole> findAll();
}
