package premium.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.entity.order.OrderStatistics;

@Mapper
public interface OrderInfoMapper {
    OrderStatistics selectStatisticsByDate(String createDate);
}
