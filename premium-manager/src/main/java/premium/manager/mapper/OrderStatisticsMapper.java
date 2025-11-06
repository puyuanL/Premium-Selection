package premium.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.dto.order.OrderStatisticsDto;
import premium.model.entity.order.OrderStatistics;

import java.util.List;

@Mapper
public interface OrderStatisticsMapper {
    void insert(OrderStatistics orderStatistics);

    List<OrderStatistics> selectList(OrderStatisticsDto orderStatisticsDto);
}
