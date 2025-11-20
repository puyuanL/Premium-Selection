package premium.user.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import premium.model.entity.user.UserAddress;
import premium.user.mapper.UserAddressMapper;
import premium.user.service.UserAddressService;
import premium.utils.AuthContextUtil;

import java.util.List;

@Service
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public List<UserAddress> findUserAddressList() {
        Long userId = AuthContextUtil.getUserInfo().getId();
        return userAddressMapper.findUserAddressList(userId);
    }

    @Override
    public UserAddress getUserAddressById(Long addressId) {
        return userAddressMapper.getUserAddressById(addressId);
    }

}
