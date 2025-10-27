package premium.manager.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import premium.manager.mapper.SysRoleMapper;
import premium.manager.mapper.SysRoleUserMapper;
import premium.manager.service.SysRoleService;
import premium.model.dto.system.SysRoleDto;
import premium.model.entity.system.SysRole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Override
    public PageInfo<SysRole> findByPage(SysRoleDto sysRoleDto, Integer pageNum, Integer pageSize) {
        // set page parameter
        PageHelper.startPage(pageNum, pageSize);
        // select all data according to condition
        List<SysRole> list =  sysRoleMapper.findByPage(sysRoleDto);
        // return PageInfo Object
        return new PageInfo<>(list);
    }

    @Override
    public void saveSysRole(SysRole sysRole) {
        sysRoleMapper.save(sysRole);
    }

    @Override
    public void updateSysRole(SysRole sysRole) {
        sysRoleMapper.update(sysRole);
    }

    @Override
    public void deleteById(Long id) {
        sysRoleMapper.delete(id);
    }

    @Override
    public Map<String, Object> findAllRoles(Long userId) {
        Map<String, Object> sysRoleMap = new HashMap<>();

        // find all roles
        List<SysRole> allRoleList = sysRoleMapper.findAll();
        // find roles by user id
        List<Long> userRoleList = sysRoleUserMapper.findRolesByUserId(userId);

        sysRoleMap.put("allRolesList", allRoleList);
        sysRoleMap.put("sysUserRoles", userRoleList);

        return sysRoleMap;
    }

}
