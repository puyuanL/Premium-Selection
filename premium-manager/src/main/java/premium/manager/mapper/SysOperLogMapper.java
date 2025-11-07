package premium.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.entity.system.SysOperLog;

@Mapper
public interface SysOperLogMapper {
    void insert(SysOperLog sysOperLog);
}
