package premium.manager.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
}
