package premium.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.dto.system.SysUserDto;
import premium.model.entity.system.SysUser;

import java.util.List;


@Mapper
public interface SysUserMapper {
    /**
     * Search user data by userName
     * @param userName String
     * @return SysUser Object
     */
    SysUser selectByUserInfoName(String userName);

    List<SysUser> findByPage(SysUserDto sysUserDto);

    void save(SysUser sysUser);

    void update(SysUser sysUser);

    void deleteById(Long userId);
}
