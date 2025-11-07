package premium.manager.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import premium.common.log.annotation.Log;
import premium.manager.service.SysRoleService;
import premium.model.dto.system.SysRoleDto;
import premium.model.entity.system.SysRole;
import premium.model.vo.common.Result;
import premium.model.vo.common.ResultCodeEnum;

@RestController
@RequestMapping(value = "/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * Role list method
     * @pageNum current page
     * @pageSize show number of each page
     */
    @PostMapping("/findByPage/{pageNum}/{pageSize}")
    public Result findByPage(@PathVariable("pageNum") Integer pageNum,
                             @PathVariable("pageSize") Integer pageSize,
                             @RequestBody SysRoleDto sysRoleDto) {
        PageInfo<SysRole> pageInfo = sysRoleService.findByPage(sysRoleDto, pageNum, pageSize);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    /**
     * Role Add method
     */
    @Log(title = "角色管理-添加", businessType = 1)
    @PostMapping("/saveSysRole")
    public Result saveSysRole(@RequestBody SysRole sysRole) {
        sysRoleService.saveSysRole(sysRole);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    /**
     * Role Edit method
     */
    @Log(title = "角色管理-修改", businessType = 2)
    @PutMapping("/updateSysRole")
    public Result updateSysRole(@RequestBody SysRole sysRole) {
        sysRoleService.updateSysRole(sysRole);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    /**
     * Role Delete method
     */
    @Log(title = "角色管理-删除", businessType = 3)
    @DeleteMapping("/deleteById/{roleId}")
    public Result deleteById(@PathVariable Long roleId) {
        sysRoleService.deleteById(roleId);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

}
