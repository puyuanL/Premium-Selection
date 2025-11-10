package premium.common.log.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import premium.common.log.annotation.Log;
import premium.common.log.service.AsyncOperLogService;
import premium.common.log.utils.LogUtil;
import premium.model.entity.system.SysOperLog;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private AsyncOperLogService operLogService;

    // 环绕通知
    @Around(value = "@annotation(sysLog)")
    public Object doAroundAdvice(ProceedingJoinPoint joinPoint, Log sysLog) throws Throwable{

        // 封装数据
        SysOperLog sysOperLog = new SysOperLog();
        LogUtil.beforeHandleLog(sysLog, joinPoint, sysOperLog);

        // 业务方法
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();
            LogUtil.afterHandleLog(sysLog, proceed, sysOperLog, 0, null);
        } catch (Throwable e) {
            e.printStackTrace();
            LogUtil.afterHandleLog(sysLog, proceed, sysOperLog, 1, e.getMessage());
            throw e;
        }

        // 调用service方法把日志信息添加数据库
        operLogService.saveSysOperLog(sysOperLog);
        return proceed;
    }
}
