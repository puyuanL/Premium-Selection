package premium.manager.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import premium.manager.mapper.SysRoleMapper;
import premium.manager.service.SysRoleService;
import premium.model.dto.system.SysRoleDto;
import premium.model.entity.system.SysRole;

import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public PageInfo<SysRole> findByPage(SysRoleDto sysRoleDto, Integer pageNum, Integer pageSize) {
        // set page parameter
        PageHelper.startPage(pageNum, pageSize);
        // select all data according to condition
        List<SysRole> list =  sysRoleMapper.findByPage(sysRoleDto);
        // return PageInfo Object
        return new PageInfo<>(list);
    }

}
