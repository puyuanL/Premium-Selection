package premium.common.log.service;

import premium.model.entity.system.SysOperLog;

public interface AsyncOperLogService {
    void saveSysOperLog(SysOperLog sysOperLog) ;
}
