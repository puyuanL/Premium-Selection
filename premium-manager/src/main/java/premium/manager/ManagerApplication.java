package premium.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import premium.manager.properties.UserProperties;

@SpringBootApplication
@ComponentScan(basePackages = {"premium"})
@EnableConfigurationProperties(value = {UserProperties.class})
public class ManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class, args);
    }
}
