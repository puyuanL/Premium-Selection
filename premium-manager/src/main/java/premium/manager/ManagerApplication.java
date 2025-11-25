package premium.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import premium.common.log.annotation.EnableLogAspect;
import premium.manager.properties.MyMinioProperties;
import premium.manager.properties.UserProperties;

@EnableAsync
@EnableLogAspect
@SpringBootApplication
@EnableConfigurationProperties(value = {UserProperties.class, MyMinioProperties.class})
@EnableScheduling
public class ManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class, args);
    }
}
