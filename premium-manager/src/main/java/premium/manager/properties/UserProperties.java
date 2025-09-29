package premium.manager.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "premium.auth")
public class UserProperties {
    private List<String> noAuthUrls;
}
