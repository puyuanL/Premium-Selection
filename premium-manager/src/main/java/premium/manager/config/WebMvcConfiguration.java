package premium.manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import premium.manager.interceptor.LoginAuthInterceptor;
import premium.manager.properties.UserProperties;

@Component
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private LoginAuthInterceptor loginAuthInterceptor;

    @Autowired
    private UserProperties userProperties;

    // register interceptor
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginAuthInterceptor)
                // .excludePathPatterns("/admin/system/index/login", "/admin/system/index/generateValidateCode")
                .excludePathPatterns(userProperties.getNoAuthUrls())
                .addPathPatterns("/**");
    }

    // Cross-domain
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 添加路径规则
                .allowCredentials(true)        // 允许在跨域的情况下传递 Cookie
                .allowedOriginPatterns("*")    // 允许请求来源的域规则
                .allowedMethods("*")
                .allowedHeaders("*");
    }

}

