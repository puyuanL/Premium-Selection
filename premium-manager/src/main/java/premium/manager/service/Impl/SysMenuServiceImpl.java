package premium.manager.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import premium.common.exception.MyException;
import premium.manager.mapper.SysMenuMapper;
import premium.manager.service.SysMenuService;
import premium.manager.utils.MenuHelper;
import premium.model.entity.system.SysMenu;
import premium.model.vo.common.ResultCodeEnum;

import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

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
}
