package premium.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import premium.common.annotation.EnableUserLoginAuthInterceptor;
import premium.common.annotation.EnableUserTokenFeignInterceptor;

@SpringBootApplication
@EnableFeignClients(basePackages = {
        "premium.feign.cart",
        "premium.feign.product",
        "premium.feign.user"
})
@EnableUserTokenFeignInterceptor
@EnableUserLoginAuthInterceptor
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class , args) ;
    }

}
