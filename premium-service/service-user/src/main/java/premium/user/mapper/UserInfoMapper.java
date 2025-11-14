package premium.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.entity.user.UserInfo;

@Mapper
public interface UserInfoMapper {
    UserInfo getByUsername(String username);

    void save(UserInfo userInfo);
}
