package premium.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import premium.common.annotation.EnableUserLoginAuthInterceptor;
import premium.common.annotation.EnableUserTokenFeignInterceptor;

@SpringBootApplication
@EnableFeignClients(basePackages = {"premium.feign"})
@ComponentScan(basePackages = {"premium.utils", "premium.stock"})
@EnableUserTokenFeignInterceptor
@EnableUserLoginAuthInterceptor
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class , args) ;
    }

}
