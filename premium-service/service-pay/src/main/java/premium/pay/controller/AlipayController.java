package premium.pay.controller;

//import com.alipay.api.AlipayApiException;
//import com.alipay.api.internal.util.AlipaySignature;
//import jakarta.servlet.http.HttpServletRequest;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import premium.model.vo.common.Result;
import premium.model.vo.common.ResultCodeEnum;
import premium.pay.service.AlipayService;
import premium.pay.service.PaymentInfoService;
//import premium.pay.utils.AlipayProperties;

import java.io.PrintWriter;

@Controller
@RequestMapping(value = "/api/order/alipay")
public class AlipayController {

    @Autowired
    private AlipayService alipayService;

//    @Autowired
//    private AlipayProperties alipayProperties;

    @Autowired
    private PaymentInfoService paymentInfoService;

    /**
     * 支付宝支付
     */
    @SentinelResource(value = "alipay_submit", blockHandler = "submitAlipayBlockHandler")
    @GetMapping("/submitAlipay/{orderNo}")
    @ResponseBody
    public Result submitAlipay(@PathVariable("orderNo") String orderNo) {
        String form = alipayService.submitAlipay(orderNo);
        return Result.build(form, ResultCodeEnum.SUCCESS);
    }

    public static Result submitAlipayBlockHandler(String orderNo, BlockException e) {
        return Result.build(null, ResultCodeEnum.SYSTEM_BUSY);
    }

//    /**
//     * 支付宝异步回调
//     */
//    @RequestMapping("callback/notify")
//    @ResponseBody
//    public String alipayNotify(@RequestParam Map<String, String> paramMap, HttpServletRequest request) {
//        boolean signVerified = false; //调用SDK验证签名
//        try {
//            signVerified = AlipaySignature.rsaCheckV1(paramMap, alipayProperties.getAlipayPublicKey(), AlipayProperties.charset, AlipayProperties.sign_type);
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
//
//        // 交易状态
//        String trade_status = paramMap.get("trade_status");
//        if (signVerified) {
//            // 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
//            if ("TRADE_SUCCESS".equals(trade_status) || "TRADE_FINISHED".equals(trade_status)) {
//                // 正常的支付成功，我们应该更新交易记录状态
//                paymentInfoService.updatePaymentStatus(paramMap);
//                return "success";
//            }
//        } else {
//            // 验签失败则记录异常日志，并在response中返回failure.
//            return "failure";
//        }
//        return "failure";
//    }
}
