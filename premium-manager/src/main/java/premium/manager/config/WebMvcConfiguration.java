package premium.manager.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 添加路径规则
                .allowCredentials(true)        // 允许在跨域的情况下传递 Cookie
                .allowedOriginPatterns("*")    // 允许请求来源的域规则
                .allowedMethods("*")
                .allowedHeaders("*");
    }

}

