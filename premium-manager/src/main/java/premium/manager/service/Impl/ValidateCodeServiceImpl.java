package premium.manager.service.Impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import premium.manager.service.ValidateCodeService;
import premium.model.vo.system.ValidateCodeVo;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public ValidateCodeVo generateValidateCode() {
        // 1 generate image ValidateCode through 'hutool'
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(150, 48, 4, 2);
        String codeValue = circleCaptcha.getCode();  // value of validate code
        String imageBase64 = circleCaptcha.getImageBase64();  // image validate code, coding by base64

        // 2 store ValidateCode into redis, set redis key(uuid) and value. Set timeout
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.opsForValue().set(
                "user:validate" + key,
                codeValue,
                5,
                TimeUnit.MINUTES
        );

        // 3 return ValidateCodeVo
        ValidateCodeVo validateCodeVo = new ValidateCodeVo();
        validateCodeVo.setCodeKey(key);
        validateCodeVo.setCodeValue("data:image/png;base64," + imageBase64);
        return validateCodeVo;
    }
}
