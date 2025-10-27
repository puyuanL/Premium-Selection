package premium.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import premium.manager.service.SysRoleService;
import premium.manager.service.SysUserService;
import premium.model.dto.system.AssginRoleDto;
import premium.model.vo.common.Result;
import premium.model.vo.common.ResultCodeEnum;

import java.util.Map;

@RestController
@RequestMapping(value = "/admin/system/sysRoleUser")
public class SysRoleUserController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 查询所有角色 & 查询用户id对应角色
     * @return Result with result map
     */
    @GetMapping(value = "/findAllRoles/{userId}")
    public Result<Map<String, Object>> findAllRoles(@PathVariable("userId") Long userId) {
        // find all roles and id->roles
        Map<String, Object> resultMap = sysRoleService.findAllRoles(userId);
        return Result.build(resultMap, ResultCodeEnum.SUCCESS);
    }

    /**
     * 为用户分配角色, 保存分配的数据
     */
    @PostMapping(value = "/doAssign")
    public Result doAssign(@RequestBody AssginRoleDto assginRoleDto) {
        sysUserService.doAssign(assginRoleDto);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

}
