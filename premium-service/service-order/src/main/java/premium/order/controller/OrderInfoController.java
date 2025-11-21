package premium.order.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import premium.model.dto.h5.OrderInfoDto;
import premium.model.entity.order.OrderInfo;
import premium.model.vo.common.Result;
import premium.model.vo.common.ResultCodeEnum;
import premium.model.vo.h5.TradeVo;
import premium.order.service.OrderInfoService;

@RestController
@RequestMapping(value="/api/order/orderInfo")
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * 订单结算
     * @return TradeVo (Total Amount & ProductList)
     */
    @GetMapping("/auth/trade")
    public Result<TradeVo> trade() {
        TradeVo tradeVo = orderInfoService.getTrade();
        return Result.build(tradeVo, ResultCodeEnum.SUCCESS);
    }

    /**
     * 生成订单
     */
    @PostMapping("/auth/submitOrder")
    public Result<Long> submitOrder(@RequestBody OrderInfoDto orderInfoDto) {
        Long orderId = orderInfoService.submitOrder(orderInfoDto);
        return Result.build(orderId, ResultCodeEnum.SUCCESS);
    }

    /**
     * 获取订单信息
     */
    @GetMapping("/auth/{orderId}")
    public Result<OrderInfo> getOrderInfo(@PathVariable Long orderId) {
        OrderInfo orderInfo = orderInfoService.getOrderInfo(orderId);
        return Result.build(orderInfo, ResultCodeEnum.SUCCESS);
    }

    /**
     * 立即购买
     */
    @GetMapping("/auth/buy/{skuId}")
    public Result<TradeVo> buy(@PathVariable Long skuId) {
        TradeVo tradeVo = orderInfoService.buy(skuId);
        return Result.build(tradeVo, ResultCodeEnum.SUCCESS);
    }

    /**
     * 获取分页订单列表
     */
    @GetMapping("/auth/{page}/{limit}")
    public Result<PageInfo<OrderInfo>> list(@PathVariable Integer page,
                                            @PathVariable Integer limit,
                                            @RequestParam(required = false, defaultValue = "") Integer orderStatus) {
        PageInfo<OrderInfo> pageInfo = orderInfoService.findUserPage(page, limit, orderStatus);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    /**
     * 远程调用——根据订单编号获取订单信息
     */
    @GetMapping("/auth/getOrderInfoByOrderNo/{orderNo}")
    public OrderInfo getOrderInfoByOrderNo(@PathVariable String orderNo) {
        return orderInfoService.getByOrderNo(orderNo) ;
    }

    /**
     * 更新订单支付状态
     */
    @GetMapping("/auth/updateOrderStatusPayed/{orderNo}/{orderStatus}")
    public Result updateOrderStatus(@PathVariable(value = "orderNo") String orderNo ,
                                    @PathVariable(value = "orderStatus") Integer orderStatus) {
        orderInfoService.updateOrderStatus(orderNo, orderStatus);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }
}