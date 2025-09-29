package premium.utils;

import premium.model.entity.system.SysUser;

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
}
