package premium.manager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import premium.manager.service.ValidateCodeService;
import premium.model.dto.system.LoginDto;
import premium.model.entity.system.SysUser;
import premium.model.entity.user.UserInfo;
import premium.model.vo.common.Result;
import premium.model.vo.common.ResultCodeEnum;
import premium.model.vo.system.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import premium.manager.service.SysUserService;
import premium.model.vo.system.ValidateCodeVo;

@Tag(name = "用户接口")
@RestController
@RequestMapping(value = "/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ValidateCodeService validateCodeService;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * user login
     * @param loginDto LoginDto
     * @return Result with code number
     */
    // @Operation(summary = "登陆方法")
    @PostMapping("login")
    public Result login(@RequestBody LoginDto loginDto) {
        LoginVo loginVo = sysUserService.login(loginDto);
        return Result.build(loginVo, ResultCodeEnum.SUCCESS);
    }

    /**
     * Generate Image Validate Code
     * @return Result with code number
     */
    @GetMapping("/generateValidateCode")
    public Result<ValidateCodeVo> generateValidateCode() {
        ValidateCodeVo validateCodeVo = validateCodeService.generateValidateCode();
        return Result.build(validateCodeVo, ResultCodeEnum.SUCCESS);
    }

    /**
     * Get current user login info
     */
    @GetMapping("/getUserInfo")
    public Result getUserInfo(@RequestHeader(name = "token") String token) {
        // 1 get token from request
        // String token = request.getHeader("token")
        // 2 get redis of user info by token
        SysUser sysuser = sysUserService.getUserInfo(token);
        // 3 return user info
        return Result.build(sysuser, ResultCodeEnum.SUCCESS);
    }

    /**
     *
     */
    @GetMapping("/logout")
    public Result logout(@RequestHeader(name = "token") String token) {
        sysUserService.logout(token);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

}
