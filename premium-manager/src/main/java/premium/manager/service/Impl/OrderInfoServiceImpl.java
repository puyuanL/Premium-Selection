package premium.manager.service.Impl;

import cn.hutool.core.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import premium.manager.mapper.OrderStatisticsMapper;
import premium.manager.service.OrderInfoService;
import premium.model.dto.order.OrderStatisticsDto;
import premium.model.entity.order.OrderStatistics;
import premium.model.vo.order.OrderStatisticsVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;

    @Override
    public OrderStatisticsVo getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto) {
        List<OrderStatistics> orderStatisticsList = orderStatisticsMapper.selectList(orderStatisticsDto);
        // Date List
        List<String> dateList = orderStatisticsList.stream().map(orderStatistics ->
                DateUtil.format(orderStatistics.getOrderDate(), "yyyy-MM-dd")).toList();
        // Amount List
        List<BigDecimal> amountList = orderStatisticsList.stream().map(OrderStatistics::getTotalAmount).toList();

        // make into OrderStatisticsVo
        OrderStatisticsVo orderStatisticsVo = new OrderStatisticsVo();
        orderStatisticsVo.setDateList(dateList);
        orderStatisticsVo.setAmountList(amountList);

        return orderStatisticsVo;
    }
}
