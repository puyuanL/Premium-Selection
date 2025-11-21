package premium.pay.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.entity.pay.PaymentInfo;

@Mapper
public interface PaymentInfoMapper {

    PaymentInfo getByOrderNo(String orderNo);

    void save(PaymentInfo paymentInfo);

    void updatePaymentInfo(PaymentInfo paymentInfo);
}
