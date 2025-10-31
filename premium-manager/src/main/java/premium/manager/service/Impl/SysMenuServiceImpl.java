package premium.manager.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import premium.common.exception.MyException;
import premium.manager.mapper.SysMenuMapper;
import premium.manager.mapper.SysRoleMenuMapper;
import premium.manager.service.SysMenuService;
import premium.manager.utils.MenuHelper;
import premium.model.entity.system.SysMenu;
import premium.model.entity.system.SysUser;
import premium.model.vo.common.ResultCodeEnum;
import premium.model.vo.system.SysMenuVo;
import premium.utils.AuthContextUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenu> findNodes() {
        // 查询所有菜单，返回所有list集合
        List<SysMenu> menuList = sysMenuMapper.findAll();
        if(CollectionUtils.isEmpty(menuList)) {
            return null;
        }

        // 调用工具类方法，返回list集合封装要求的数据格式
        return MenuHelper.buildTree(menuList);
    }

    @Override
    public void save(SysMenu sysMenu) {
        sysMenuMapper.save(sysMenu);
        // 新添加子菜单，把父菜单 set isHalf = 1
        updateSysRoleMenu(sysMenu);
    }

    private void updateSysRoleMenu(SysMenu sysMenu) {
        SysMenu parentMenu = sysMenuMapper.selectParentMenu(sysMenu.getParentId());
        if(parentMenu != null) {
            // set is_half=1
            sysRoleMenuMapper.updateSysRoleMenuIsHalf(parentMenu.getParentId());
            updateSysRoleMenu(parentMenu);
        }

    }

    @Override
    public void update(SysMenu sysMenu) {
        sysMenuMapper.update(sysMenu);
    }

    @Override
    public void removeById(Long id) {
        // 有子菜单，不能删除；没有子菜单可以删除
        if(sysMenuMapper.selectCountById(id) > 0) {
            throw new MyException(ResultCodeEnum.NODE_ERROR);
        }
        sysMenuMapper.delete(id);
    }

    @Override
    public List<SysMenuVo> findMenusByUserId() {
        // 获取当前用户id
        SysUser sysuser = AuthContextUtil.get();
        Long userId = sysuser.getId();
        // 根据id查询菜单
        List<SysMenu> sysMenuList = sysMenuMapper.findMenusByUserId(userId);
        // 封装成要求的格式返回
        sysMenuList = MenuHelper.buildTree(sysMenuList);
        return this.buildMenus(sysMenuList);
    }

    /**
     * 将List<SysMenu>对象转换成List<SysMenuVo>对象
     */
    private List<SysMenuVo> buildMenus(List<SysMenu> menus) {
        List<SysMenuVo> sysMenuVoList = new LinkedList<>();
        for (SysMenu sysMenu : menus) {
            SysMenuVo sysMenuVo = new SysMenuVo();
            sysMenuVo.setTitle(sysMenu.getTitle());
            sysMenuVo.setName(sysMenu.getComponent());
            List<SysMenu> children = sysMenu.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                sysMenuVo.setChildren(buildMenus(children));
            }
            sysMenuVoList.add(sysMenuVo);
        }
        return sysMenuVoList;
    }
}
