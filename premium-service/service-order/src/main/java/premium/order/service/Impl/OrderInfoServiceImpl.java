package premium.order.service.Impl;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import premium.common.exception.MyException;
import premium.feign.cart.CartFeignClient;
import premium.feign.product.ProductFeignClient;
import premium.feign.user.UserFeignClient;
import premium.model.dto.h5.OrderInfoDto;
import premium.model.entity.h5.CartInfo;
import premium.model.entity.order.OrderInfo;
import premium.model.entity.order.OrderItem;
import premium.model.entity.order.OrderLog;
import premium.model.entity.product.ProductSku;
import premium.model.entity.user.UserAddress;
import premium.model.entity.user.UserInfo;
import premium.model.vo.common.ResultCodeEnum;
import premium.model.vo.h5.TradeVo;
import premium.order.mapper.OrderInfoMapper;
import premium.order.mapper.OrderItemMapper;
import premium.order.mapper.OrderLogMapper;
import premium.order.service.OrderInfoService;
import premium.utils.AuthContextUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private CartFeignClient cartFeignClient;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderLogMapper orderLogMapper;

    @Override
    public TradeVo getTrade() {
        List<CartInfo> cartInfoList = cartFeignClient.getAllChecked();

        // make order item list
        List<OrderItem> orderItemList = new ArrayList<>();
        for (CartInfo cartInfo : cartInfoList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setSkuId(cartInfo.getSkuId());
            orderItem.setSkuName(cartInfo.getSkuName());
            orderItem.setSkuNum(cartInfo.getSkuNum());
            orderItem.setSkuPrice(cartInfo.getCartPrice());
            orderItem.setThumbImg(cartInfo.getImgUrl());

            orderItemList.add(orderItem);
        }

        // make total amount
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItemList) {
            totalPrice = totalPrice.add(
                    orderItem.getSkuPrice().multiply(
                            new BigDecimal(orderItem.getSkuNum())
                    )
            );
        }

        TradeVo tradeVo = new TradeVo();
        tradeVo.setOrderItemList(orderItemList);
        tradeVo.setTotalAmount(totalPrice);
        return tradeVo;
    }

    @Override
    public Long submitOrder(OrderInfoDto orderInfoDto) {
        List<OrderItem> orderItemList = orderInfoDto.getOrderItemList();
        if (CollectionUtils.isEmpty(orderItemList)) {
            throw new MyException(ResultCodeEnum.DATA_ERROR);
        }

        // add data into 'order_info'
        OrderInfo orderInfo = setOrderInfo(orderInfoDto, orderItemList);
        orderInfoMapper.save(orderInfo);

        // add data into 'order_item'
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderId(orderInfo.getId());
            orderItemMapper.save(orderItem);
        }

        // add data into 'order_log'
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderInfo.getId());
        orderLog.setProcessStatus(0);
        orderLog.setNote("提交订单");
        orderLogMapper.save(orderLog);

        // 远程调用service-cart微服务清空购物车数据
        cartFeignClient.deleteChecked();

        return orderInfo.getId();
    }

    private OrderInfo setOrderInfo(OrderInfoDto orderInfoDto, List<OrderItem> orderItemList) {
        UserInfo userInfo = AuthContextUtil.getUserInfo();
        OrderInfo orderInfo = new OrderInfo();
        //订单编号
        orderInfo.setOrderNo(String.valueOf(System.currentTimeMillis()));
        //用户id
        orderInfo.setUserId(userInfo.getId());
        //用户昵称
        orderInfo.setNickName(userInfo.getNickName());
        //用户收货地址信息
        UserAddress userAddress = userFeignClient.getUserAddressById(orderInfoDto.getUserAddressId());
        orderInfo.setReceiverName(userAddress.getName());
        orderInfo.setReceiverPhone(userAddress.getPhone());
        orderInfo.setReceiverTagName(userAddress.getTagName());
        orderInfo.setReceiverProvince(userAddress.getProvinceCode());
        orderInfo.setReceiverCity(userAddress.getCityCode());
        orderInfo.setReceiverDistrict(userAddress.getDistrictCode());
        orderInfo.setReceiverAddress(userAddress.getFullAddress());
        //订单金额
        BigDecimal totalAmount = new BigDecimal(0);
        for (OrderItem orderItem : orderItemList) {
            totalAmount = totalAmount.add(orderItem.getSkuPrice().multiply(new BigDecimal(orderItem.getSkuNum())));
        }
        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setCouponAmount(new BigDecimal(0));
        orderInfo.setOriginalTotalAmount(totalAmount);
        orderInfo.setFeightFee(orderInfoDto.getFeightFee());
        orderInfo.setPayType(2);
        orderInfo.setOrderStatus(0);

        return orderInfo;
    }

    @Override
    public OrderInfo getOrderInfo(Long orderId) {
        return orderInfoMapper.getById(orderId);
    }

    @Override
    public TradeVo buy(Long skuId) {
        ProductSku productSku = productFeignClient.getBySkuId(skuId);
        // make order item list
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setSkuId(skuId);
        orderItem.setSkuName(productSku.getSkuName());
        orderItem.setSkuNum(1);
        orderItem.setSkuPrice(productSku.getSalePrice());
        orderItem.setThumbImg(productSku.getThumbImg());
        orderItemList.add(orderItem);

        TradeVo tradeVo = new TradeVo();
        tradeVo.setOrderItemList(orderItemList);
        tradeVo.setTotalAmount(productSku.getMarketPrice());
        return tradeVo;
    }

    @Override
    public PageInfo<OrderInfo> findUserPage(Integer page, Integer limit, Integer orderStatus) {
        PageHelper.startPage(page, limit);
        Long userId = AuthContextUtil.getUserInfo().getId();
        // 查询订单信息
        List<OrderInfo> orderInfoList = orderInfoMapper.findUserPage(userId, orderStatus);
        // 查询所有订单项数据
        orderInfoList.forEach(orderInfo -> {
            List<OrderItem> orderItemList = orderItemMapper.findByOrderId(orderInfo.getId());
            orderInfo.setOrderItemList(orderItemList);
        });

        return new PageInfo<>(orderInfoList);
    }

    @Override
    public OrderInfo getByOrderNo(String orderNo) {
        OrderInfo orderInfo = orderInfoMapper.getByOrderNo(orderNo);
        List<OrderItem> orderItemList = orderItemMapper.findByOrderId(orderInfo.getId());
        orderInfo.setOrderItemList(orderItemList);
        return orderInfo;
    }

    @Override
    public void updateOrderStatus(String orderNo, Integer orderStatus) {
        OrderInfo orderInfo = orderInfoMapper.getByOrderNo(orderNo);
        orderInfo.setOrderStatus(orderStatus);
        orderInfo.setPaymentTime(new Date());
        orderInfo.setPayType(2); // alipay

        // 更新订单状态
        orderInfoMapper.updateById(orderInfo);
        // ToDo 更新 order_log 表
    }
}
