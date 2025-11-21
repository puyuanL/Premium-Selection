package premium.pay.service;

import premium.model.entity.pay.PaymentInfo;

import java.util.Map;

public interface PaymentInfoService {
    PaymentInfo savePaymentInfo(String orderNo);

    void updatePaymentStatus(Map<String, String> paramMap);
}
