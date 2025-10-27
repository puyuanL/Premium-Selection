package premium.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.entity.system.SysRole;

import java.util.List;

@Mapper
public interface SysRoleUserMapper {

    void doAssign(Long userId, Long roleId);

    void deleteRoleByUserId(Long userId);

    List<Long> findRolesByUserId(Long userId);
}
