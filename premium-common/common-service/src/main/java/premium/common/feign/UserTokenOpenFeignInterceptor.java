package premium.common.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class UserTokenOpenFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
//        ServletRequestAttributes requestAttributes =
//                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = requestAttributes.getRequest();
//        String token = request.getHeader("token");
//        requestTemplate.header("token" , token);

        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            if (request != null) {
                // 从请求头中获取令牌并传递（原逻辑保留）
                String token = request.getHeader("token");
                if (token != null) {
                    requestTemplate.header("token", token);
                }
            }
        }
    }
}
