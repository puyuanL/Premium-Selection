package premium.order.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.entity.order.OrderLog;

@Mapper
public interface OrderLogMapper {
    void save(OrderLog orderLog);
}
