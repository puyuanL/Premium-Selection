package premium.stock;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import premium.feign.order.OrderFeignClient;
import premium.feign.product.ProductFeignClient;
import premium.model.dto.product.SkuSaleDto;
import premium.model.entity.order.OrderInfo;

import java.util.List;

@Component
public class StockDeductConsumer {

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private ProductFeignClient productFeignClient;

    @RabbitListener(queues = "stock.deduct.queue")
    public void handleStockDeduct(String orderNo) {
        // 同步扣减MySQL库存
        OrderInfo orderInfo = orderFeignClient.getOrderInfoByOrderNo(orderNo);
        List<SkuSaleDto> skuSaleDtoList = orderInfo.getOrderItemList().stream().map(item -> {
            SkuSaleDto skuSaleDto = new SkuSaleDto();
            skuSaleDto.setSkuId(item.getSkuId());
            skuSaleDto.setNum(item.getSkuNum());
            return skuSaleDto;
        }).toList();
        productFeignClient.updateSkuSaleAndStockNum(skuSaleDtoList);
    }
}