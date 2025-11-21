package premium.pay.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import premium.common.exception.MyException;
import premium.model.entity.pay.PaymentInfo;
import premium.model.vo.common.ResultCodeEnum;
import premium.pay.service.AlipayService;
import premium.pay.service.PaymentInfoService;
import premium.pay.utils.AlipayProperties;

import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    private PaymentInfoService paymentInfoService;

    @Autowired
    private AlipayProperties alipayProperties;

    @Autowired
    private AlipayClient alipayClient;

    @Override
    public String submitAlipay(String orderNo) {
        // 保存支付记录
        PaymentInfo paymentInfo = paymentInfoService.savePaymentInfo(orderNo);

        // 调用支付宝服务接口
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        // 同步回调
        alipayRequest.setReturnUrl(alipayProperties.getReturnPaymentUrl());
        // 异步回调
        alipayRequest.setNotifyUrl(alipayProperties.getNotifyPaymentUrl());
        // 准备请求参数 ，声明一个map 集合
        HashMap<String, Object> map = new HashMap<>();
        map.put("out_trade_no",paymentInfo.getOrderNo());
        map.put("product_code","QUICK_WAP_WAY");
        //map.put("total_amount",paymentInfo.getAmount());
        map.put("total_amount",new BigDecimal("0.01"));
        map.put("subject",paymentInfo.getContent());
        alipayRequest.setBizContent(JSON.toJSONString(map));

        // 调用支付宝服务的接口
        try {
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(alipayRequest);
            if (response.isSuccess()) {
                return response.getBody();
            } else {
                throw new MyException(ResultCodeEnum.DATA_ERROR);
            }
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }

}
