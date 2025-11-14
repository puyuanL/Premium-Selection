package premium.user.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import premium.common.exception.MyException;
import premium.model.dto.h5.UserRegisterDto;
import premium.model.entity.user.UserInfo;
import premium.model.vo.common.ResultCodeEnum;
import premium.user.mapper.UserInfoMapper;
import premium.user.service.UserInfoService;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Transactional(rollbackFor = MyException.class)
    @Override
    public void register(UserRegisterDto userRegisterDto) {
        String username = userRegisterDto.getUsername();
        String password = userRegisterDto.getPassword();
        String nickName = userRegisterDto.getNickName();
        String code = userRegisterDto.getCode();
        if (StringUtils.isEmpty(username) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(nickName) ||
                StringUtils.isEmpty(code)
        ) {
            throw new MyException(ResultCodeEnum.DATA_ERROR);
        }

        String redisCode = redisTemplate.opsForValue().get(username);
        if (redisCode == null || !redisCode.equals(code)) {
            throw new MyException(ResultCodeEnum.VALIDATECODE_ERROR);
        }
        UserInfo userInfo = userInfoMapper.getByUsername(username);
        if(null != userInfo) {
            throw new MyException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }
        // make new user object
        userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        userInfo.setNickName(nickName);
        userInfo.setPhone(username);
        userInfo.setStatus(1);
        userInfo.setSex(0);
        userInfo.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");

        userInfoMapper.save(userInfo);
        redisTemplate.delete(username);
    }
}
