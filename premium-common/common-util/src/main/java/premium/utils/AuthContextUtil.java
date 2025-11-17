package premium.utils;

import premium.model.entity.system.SysUser;
import premium.model.entity.user.UserInfo;

public class AuthContextUtil {

    // new a ThreadLocal object
    private static final ThreadLocal<SysUser> threadLocal = new ThreadLocal<>();

    // add data
    public static void set(SysUser sysUser) {
        threadLocal.set(sysUser);
    }

    // get data
    public static SysUser get() {
        return threadLocal.get();
    }

    // del data
    public static void remove() {
        threadLocal.remove();
    }

    private static final ThreadLocal<UserInfo> userInfoThreadLocal = new ThreadLocal<>() ;

    // 定义存储数据的静态方法
    public static void setUserInfo(UserInfo userInfo) {
        userInfoThreadLocal.set(userInfo);
    }

    // 定义获取数据的方法
    public static UserInfo getUserInfo() {
        return userInfoThreadLocal.get() ;
    }

    // 删除数据的方法
    public static void removeUserInfo() {
        userInfoThreadLocal.remove();
    }
}
