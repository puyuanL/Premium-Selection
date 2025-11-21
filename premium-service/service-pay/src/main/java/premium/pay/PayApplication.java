package premium.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import premium.common.annotation.EnableUserLoginAuthInterceptor;
import premium.pay.utils.AlipayProperties;

@SpringBootApplication
@EnableUserLoginAuthInterceptor
@EnableFeignClients(basePackages = {"premium.feign"})
@EnableConfigurationProperties(value = {AlipayProperties.class})
public class PayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class, args);
    }

}
