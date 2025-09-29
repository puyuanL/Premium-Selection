package premium.manager.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import premium.model.entity.system.SysUser;
import premium.model.vo.common.Result;
import premium.model.vo.common.ResultCodeEnum;
import premium.utils.AuthContextUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Component
public class LoginAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // way of request (get or post)
        // options -> Pre-inspection request -> pass
        String method = request.getMethod();
        if("OPTIONS".equals(method)) {
            return true;
        }
        // get token
        // null -> return wrong code
        String token = request.getHeader("token");
        if(StrUtil.isEmpty(token)) {
            responseNoLoginInfo(response);
            return false;
        }
        // not null -> find data in redis
        // can't find data in redis, return wrong prompt
        String userInfoString = redisTemplate.opsForValue().get("user:login" + token);
        if (StrUtil.isEmpty(userInfoString)) {
            responseNoLoginInfo(response);
            return false;
        }

        // put user info into threadLocal
        SysUser user = JSON.parseObject(userInfoString, SysUser.class);
        AuthContextUtil.set(user);

        // update user data timeout info & pass
        redisTemplate.expire("user:login" + token, 30, TimeUnit.MINUTES);
        return true;
    }

    // response 208 state code to frontend
    private void responseNoLoginInfo(HttpServletResponse response) {
        Result<Object> result = Result.build(null, ResultCodeEnum.LOGIN_AUTH);
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        // del threadLocal data
        AuthContextUtil.remove();
    }
}
