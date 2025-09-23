package premium.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.entity.system.SysUser;


@Mapper
public interface SysUserMapper {
    /**
     * Search user data by userName
     * @param userName String
     * @return SysUser Object
     */
    SysUser selectByUserInfoName(String userName);

}
