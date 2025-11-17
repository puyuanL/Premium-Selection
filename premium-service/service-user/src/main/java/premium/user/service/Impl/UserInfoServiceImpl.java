package premium.user.service.Impl;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import premium.common.exception.MyException;
import premium.model.dto.h5.UserLoginDto;
import premium.model.dto.h5.UserRegisterDto;
import premium.model.entity.user.UserInfo;
import premium.model.vo.common.ResultCodeEnum;
import premium.model.vo.h5.UserInfoVo;
import premium.user.mapper.UserInfoMapper;
import premium.user.service.UserInfoService;
import premium.utils.AuthContextUtil;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
        if (StringUtils.hasText(username) ||
                StringUtils.hasText(password) ||
                StringUtils.hasText(nickName) ||
                StringUtils.hasText(code)
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

    @Override
    public String login(UserLoginDto userLoginDto) {
        // dto获取用户名和密码
        String inputUsername = userLoginDto.getUsername();
        String inputPassword = userLoginDto.getPassword();

        // 根据用户数据库得到用户信息
        UserInfo userInfo = userInfoMapper.getByUsername(inputUsername);
        if (userInfo == null) {
            throw new MyException(ResultCodeEnum.LOGIN_ERROR);
        }
        // 比较密码是否一致
        inputPassword = DigestUtils.md5DigestAsHex(inputPassword.getBytes());
        if (!inputPassword.equals(userInfo.getPassword())) {
            throw new MyException(ResultCodeEnum.LOGIN_ERROR);
        }
        // 生成token
        String token = UUID.randomUUID().toString().replaceAll("-", "");

        // 把用户信息存放到redis里面
        redisTemplate.opsForValue().set(
                "user:" + token, JSON.toJSONString(userInfo),
                30, TimeUnit.MINUTES
        );

        // 返回token
        return token;
    }

    @Override
    public UserInfoVo getCurrentUserInfo(String token) {
//        // 从redis中根据token获取用户信息
//        String userJson = redisTemplate.opsForValue().get("user:" + token);
//        if (!StringUtils.hasText(userJson)) {
//            throw new MyException(ResultCodeEnum.LOGIN_AUTH);
//        }
//        UserInfo userInfo = JSON.parseObject(userJson, UserInfo.class);
        // 从 ThreadLocal 中获取用户信息
        UserInfo userInfo = AuthContextUtil.getUserInfo();
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo, userInfoVo);

        return userInfoVo;
    }
}
