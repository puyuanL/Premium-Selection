package premium.pay.service.Impl;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import premium.common.exception.MyException;
import premium.feign.order.OrderFeignClient;
import premium.feign.product.ProductFeignClient;
import premium.model.dto.product.SkuSaleDto;
import premium.model.entity.order.OrderInfo;
import premium.model.entity.order.OrderItem;
import premium.model.entity.order.OrderStatus;
import premium.model.entity.pay.PaymentInfo;
import premium.model.vo.common.ResultCodeEnum;
import premium.pay.mapper.PaymentInfoMapper;
import premium.pay.service.PaymentInfoService;
import premium.stock.StockManager;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PaymentInfoServiceImpl implements PaymentInfoService {

    @Autowired
    private PaymentInfoMapper paymentInfoMapper;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StockManager stockManager;

    /**
     * 保存支付记录
     */
    @Override
    public PaymentInfo savePaymentInfo(String orderNo) {

        // 查询支付信息数据，如果已经已经存在了就不用进行保存(一个订单支付失败以后可以继续支付)
        PaymentInfo paymentInfo = paymentInfoMapper.getByOrderNo(orderNo);
        if (null == paymentInfo) {
            OrderInfo orderInfo = orderFeignClient.getOrderInfoByOrderNo(orderNo);
            paymentInfo = new PaymentInfo();
            paymentInfo.setUserId(orderInfo.getUserId());
            paymentInfo.setPayType(orderInfo.getPayType());
            StringBuilder content = new StringBuilder();
            for(OrderItem item : orderInfo.getOrderItemList()) {
                content.append(item.getSkuName()).append(" ");
            }
            paymentInfo.setContent(content.toString());
            paymentInfo.setAmount(orderInfo.getTotalAmount());
            paymentInfo.setOrderNo(orderNo);
            paymentInfo.setPaymentStatus(0);
            paymentInfoMapper.save(paymentInfo);
        }
        return paymentInfo;
    }

    /**
     * 更新支付状态
     */
    @Transactional
    @Override
    public void updatePaymentStatus(Map<String, String> paramMap) {
        // 根据订单编号查询支付记录
        PaymentInfo paymentInfo = paymentInfoMapper.getByOrderNo(paramMap.get("out_trade_no"));
        // 支付完成，不需要更新
        if (paymentInfo.getPaymentStatus() == 1) {
            return ;
        }
        // 支付未完成，更新支付状态
        paymentInfo.setPaymentStatus(1);
        paymentInfo.setOutTradeNo(paramMap.get("trade_no"));
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setCallbackContent(JSON.toJSONString(paramMap));
        paymentInfoMapper.updatePaymentInfo(paymentInfo);

        // 更新订单状态 为 完成支付（待发货）
        orderFeignClient.updateOrderStatus(paymentInfo.getOrderNo(), 1);

        // 更新sku库存、销量状态
        OrderInfo orderInfo = orderFeignClient.getOrderInfoByOrderNo(paymentInfo.getOrderNo());
        List<SkuSaleDto> skuSaleDtoList = orderInfo.getOrderItemList().stream().map(item -> {
            SkuSaleDto skuSaleDto = new SkuSaleDto();
            skuSaleDto.setSkuId(item.getSkuId());
            skuSaleDto.setNum(item.getSkuNum());
            return skuSaleDto;
        }).toList();
        productFeignClient.updateSkuSaleAndStockNum(skuSaleDtoList);
        // ToDo 解决库存超卖问题
    }

    @Transactional
    @Override
    public void updatePaymentStatusToSuccess(String orderNo) {
        // 根据订单编号查询支付记录
        PaymentInfo paymentInfo = paymentInfoMapper.getByOrderNo(orderNo);
        // 支付完成，不需要更新
        if (paymentInfo.getPaymentStatus() == 1) {
            return ;
        }
        // 直接设置为支付成功状态
        paymentInfo.setPaymentStatus(1);
        paymentInfo.setOutTradeNo("SIMULATE_" + System.currentTimeMillis()); // 模拟外部交易号
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setCallbackContent("模拟支付成功");
        paymentInfoMapper.updatePaymentInfo(paymentInfo);

        // ToDo 解决库存超卖问题 (更新sku库存、销量状态: update -> product, orderInfo, redis)
        // update Redis
        boolean deducted = stockManager.deductStock(orderNo);
        if (!deducted) {
            throw new MyException(ResultCodeEnum.STOCK_OPT_ERROR);
        }
        // update MySQL(product)
        rabbitTemplate.convertAndSend("stock-exchange", "stock.deduct", orderNo);
        // update orderInfo
        orderFeignClient.updateOrderStatus(paymentInfo.getOrderNo(), OrderStatus.PAID.getCode());
    }
}
