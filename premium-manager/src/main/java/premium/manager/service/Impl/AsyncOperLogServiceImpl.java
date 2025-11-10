package premium.manager.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import premium.common.log.service.AsyncOperLogService;
import premium.manager.mapper.SysOperLogMapper;
import premium.model.entity.system.SysOperLog;

@Service
public class AsyncOperLogServiceImpl implements AsyncOperLogService {

    @Autowired
    private SysOperLogMapper sysOperLogMapper;

    /**
     * 保存日志数据
     */
    @Async
    @Override
    public void saveSysOperLog(SysOperLog sysOperLog) {
        sysOperLogMapper.insert(sysOperLog);
    }
}
