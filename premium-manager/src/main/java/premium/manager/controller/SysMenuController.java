package premium.manager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import premium.common.log.annotation.Log;
import premium.manager.service.SysMenuService;
import premium.model.entity.system.SysMenu;
import premium.model.vo.common.Result;
import premium.model.vo.common.ResultCodeEnum;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/system/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 菜单列表
     */
    @GetMapping("/findNodes")
    public Result<List<SysMenu>> findNodes() {
        List<SysMenu> list = sysMenuService.findNodes();
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }

    /**
     * 菜单添加
     */
    @Log(title = "菜单管理-添加", businessType = 1)
    @PostMapping("/save")
    public Result save(@RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    /**
     * 菜单修改
     */
    @Log(title = "菜单管理-修改", businessType = 2)
    @PutMapping("/update")
    public Result update(@RequestBody SysMenu sysMenu) {
        sysMenuService.update(sysMenu);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    /**
     * 菜单删除
     */
    @Log(title = "菜单管理-删除", businessType = 3)
    @DeleteMapping("/removeById/{id}")
    public Result removeById(@PathVariable("id") Long id) {
        sysMenuService.removeById(id);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

}
