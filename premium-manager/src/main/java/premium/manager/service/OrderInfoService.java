package premium.manager.service;

import premium.model.dto.order.OrderStatisticsDto;
import premium.model.vo.order.OrderStatisticsVo;

public interface OrderInfoService {
    OrderStatisticsVo getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto);
}
