package premium.user.service.Impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import premium.user.service.SmsService;
import premium.utils.HttpUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void sendCode(String phone) {

        // test step without sending code
        if (phone.equals("13323390892")) {
            redisTemplate.opsForValue().set(phone, "6666");
        }
        String code = redisTemplate.opsForValue().get(phone);
        if (StringUtils.hasText(code)) {
            System.out.println("Test Mod without Sending Code.");
            return ;
        }

        // 生成验证码，放到Redis中，并设置过期时间
        code = RandomStringUtils.randomNumeric(4);
        redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
        // send validate code
        sendMessage(phone, code);
    }

    /**
     * 发送短信验证码
     */
    private void sendMessage(String phone, String code) {
        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "POST";
        String appcode = "177a6bea1bde42ab97fa24699b3f389c";
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> queries = new HashMap<>();
        Map<String, String> bodies = new HashMap<>();
        bodies.put("content", "code:" + code);
        bodies.put("template_id", "CST_ptdie100");  //注意，CST_ptdie100该模板ID仅为调试使用，调试结果为"status": "OK" ，即表示接口调用成功，然后联系客服报备自己的专属签名模板ID，以保证短信稳定下发
        bodies.put("phone_number", phone);

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, queries, bodies);
            HttpEntity entity = response.getEntity();
            System.out.println(EntityUtils.toString(entity,"UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
