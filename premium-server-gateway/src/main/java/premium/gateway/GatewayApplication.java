package premium.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
//        SpringApplication.run(GatewayApplication.class, args);
        try {
            SpringApplication.run(GatewayApplication.class, args);
        } catch (Exception e) {
            // 强制打印完整异常堆栈
            System.err.println("❌ SpringBoot启动失败，完整异常信息：");
            e.printStackTrace(); // 核心：打印所有异常细节
            // 保持退出码一致
            System.exit(1);
        }
    }

}
