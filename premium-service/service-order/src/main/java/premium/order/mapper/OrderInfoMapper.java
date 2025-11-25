package premium.order.mapper;

import org.apache.ibatis.annotations.Mapper;
import premium.model.entity.order.OrderInfo;

import java.util.Date;
import java.util.List;

@Mapper
public interface OrderInfoMapper {
    void save(OrderInfo orderInfo);

    OrderInfo getById(Long orderId);

    List<OrderInfo> findUserPage(Long userId, Integer orderStatus);

    OrderInfo getByOrderNo(String orderNo);

    void updateById(OrderInfo orderInfo);

    List<OrderInfo> selectTimeoutOrders(Integer status, Date timeoutTime);
}
