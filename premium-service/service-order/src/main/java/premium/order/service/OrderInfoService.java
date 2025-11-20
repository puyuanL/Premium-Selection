package premium.order.service;

import com.github.pagehelper.PageInfo;
import premium.model.dto.h5.OrderInfoDto;
import premium.model.entity.order.OrderInfo;
import premium.model.vo.h5.TradeVo;

public interface OrderInfoService {
    TradeVo getTrade();

    Long submitOrder(OrderInfoDto orderInfoDto);

    OrderInfo getOrderInfo(Long orderId);

    TradeVo buy(Long skuId);

    PageInfo<OrderInfo> findUserPage(Integer page, Integer limit, Integer orderStatus);
}
