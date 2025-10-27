package premium.manager.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "premium.minio")
public class MyMinioProperties {

    private String endpointUrl;
    private String accessKey;
    private String secreteKey;
    private String bucketName;

}
