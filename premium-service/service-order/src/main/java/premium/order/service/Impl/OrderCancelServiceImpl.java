package premium.order.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import premium.common.exception.MyException;
import premium.model.entity.order.OrderInfo;
import premium.model.entity.order.OrderStatus;
import premium.model.vo.common.ResultCodeEnum;
import premium.order.mapper.OrderInfoMapper;
import premium.order.service.OrderCancelService;
import premium.stock.StockManager;

import java.util.Date;

@Service
public class OrderCancelServiceImpl implements OrderCancelService {

    @Autowired
    private StockManager stockManager;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Transactional
    @Override
    public void cancelOrder(String orderNo) {
        // 1. 检查订单状态
        OrderInfo orderInfo = orderInfoMapper.getByOrderNo(orderNo);
        if (orderInfo == null || !OrderStatus.WAIT_PAYMENT.getCode().equals(orderInfo.getOrderStatus())) {
            throw new MyException(ResultCodeEnum.ORDER_CANT_CANCELED);
        }

        // 2. 更新订单状态为已取消
        orderInfo.setOrderStatus(OrderStatus.CANCELLED.getCode());
        orderInfo.setCancelTime(new Date());
        orderInfoMapper.updateById(orderInfo);

        // 3. 释放预占库存
        stockManager.releaseStock(orderNo);
    }
}