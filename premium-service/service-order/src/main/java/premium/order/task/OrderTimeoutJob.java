package premium.order.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import premium.common.exception.MyException;
import premium.model.entity.order.OrderInfo;
import premium.model.entity.order.OrderStatus;
import premium.model.vo.common.ResultCodeEnum;
import premium.order.mapper.OrderInfoMapper;
import premium.order.service.Impl.OrderCancelServiceImpl;

import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
public class OrderTimeoutJob {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderCancelServiceImpl orderCancelService;

    // 每1分钟执行一次
    @Scheduled(cron = "0 */5 * * * ?")
    public void handleTimeoutOrders() {
        // 查询超时未支付的订单（超过10分钟）
        Date timeoutTime = new Date(System.currentTimeMillis() - 15 * 60 * 1000);
        List<OrderInfo> timeoutOrders = orderInfoMapper.selectTimeoutOrders(OrderStatus.WAIT_PAYMENT.getCode(), timeoutTime);

        // 取消订单并释放库存
        for (OrderInfo order : timeoutOrders) {
            orderCancelService.cancelOrder(order.getOrderNo());
        }
    }
}