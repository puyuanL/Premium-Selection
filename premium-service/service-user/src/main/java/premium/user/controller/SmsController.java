package premium.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import premium.model.vo.common.Result;
import premium.model.vo.common.ResultCodeEnum;
import premium.user.service.SmsService;

@RestController
@RequestMapping("/api/user/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @GetMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable("phone") String phone) {
        smsService.sendCode(phone);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
