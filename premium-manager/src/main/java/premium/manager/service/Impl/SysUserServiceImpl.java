package premium.manager.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;

import premium.common.exception.MyException;
import premium.model.dto.system.LoginDto;
import premium.model.entity.system.SysUser;

import premium.model.vo.system.LoginVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import premium.manager.mapper.SysUserMapper;
import premium.manager.service.SysUserService;
import premium.model.vo.common.ResultCodeEnum;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public LoginVo login(LoginDto loginDto) {
        // ****************** Submit Validate Code Info ****************** //
        // 1 get input and redis key validate code
        // 2 check redis value by key
        String codeKey = "user:validate" + loginDto.getCodeKey();
        String inputCaptcha = loginDto.getCaptcha();
        String redisCaptcha = redisTemplate.opsForValue().get(codeKey);

        // 3 compare input and redis value validation code
        if(StrUtil.isEmpty(redisCaptcha) || !StrUtil.equalsIgnoreCase(redisCaptcha, inputCaptcha)){
            throw new MyException(ResultCodeEnum.VALIDATECODE_ERROR);
        }

        // 4 validate code true, delete key in redis
        redisTemplate.delete(codeKey);

        // ******************** Submit User Login Info ******************** //
        // 1 get user id
        String userName = loginDto.getUserName();
        // 2 select database 'sys_user'
        SysUser sysuser = sysUserMapper.selectByUserInfoName(userName);
        // 3 if user not exist, return error info
        if (sysuser == null) {
            // UserName does not exist
            throw new MyException(ResultCodeEnum.LOGIN_ERROR);
        }
        // 4 User exist. Get input password and encrypt inPassword, compare with database password
        String inPassword = DigestUtils.md5DigestAsHex(loginDto.getPassword().getBytes());
        String dbPassword = sysuser.getPassword();
        // 5 password equal: login success; else: login fail
        if (!dbPassword.equals(inPassword)) {
            // Wrong password
            throw new MyException(ResultCodeEnum.LOGIN_ERROR);
        }
        // 7 login success, generate user token
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        // 8 put user info (login success) into redis
        redisTemplate.opsForValue().set(
                "user:login" + token,
                JSON.toJSONString(sysuser),
                10,
                TimeUnit.DAYS
        );
        // 9 return LoginVo
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        return loginVo;
    }

    @Override
    public SysUser getUserInfo(String token) {
        String userJson =  redisTemplate.opsForValue().get("user:login" + token);
        return JSON.parseObject(userJson, SysUser.class);
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete("user:login" + token);
    }

}
