package premium.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.entity.user.UserAddress;

import java.util.List;

@Mapper
public interface UserAddressMapper {
    List<UserAddress> findUserAddressList(Long userId);

    UserAddress getUserAddressById(Long addressId);
}
