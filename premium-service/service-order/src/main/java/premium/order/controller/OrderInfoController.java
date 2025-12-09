package premium.order.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
//    private static final Logger log = LoggerFactory.getLogger(OrderInfoController.class);

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
    @SentinelResource(value = "order_submit", blockHandler = "submitOrderBlockHandler")
    @PostMapping("/auth/submitOrder")
    public Result<Long> submitOrder(@RequestBody OrderInfoDto orderInfoDto) {
        Long orderId = orderInfoService.submitOrder(orderInfoDto);
        return Result.build(orderId, ResultCodeEnum.SUCCESS);
    }

    public static Result<Long> submitOrderBlockHandler(OrderInfoDto orderInfoDto, BlockException e) {
//        log.warn("[Sentinel limit] Sentinel_Resource: {}, type: {}, Exception Info: {}",
//                "order_submit",
//                e.getClass().getSimpleName(),
//                e.getMessage(),
//                e // Logback 自动打印异常栈
//        );
        return Result.build(null, ResultCodeEnum.SYSTEM_BUSY);
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
     * 远程调用 —— 根据订单编号获取订单信息
     */
    @GetMapping("/auth/getOrderInfoByOrderNo/{orderNo}")
    public OrderInfo getOrderInfoByOrderNo(@PathVariable String orderNo) {
        return orderInfoService.getByOrderNo(orderNo) ;
    }

    /**
     * 远程调用 —— 更新订单支付状态
     */
    @GetMapping("/auth/updateOrderStatusPayed/{orderNo}/{orderStatus}")
    public Result updateOrderStatus(@PathVariable(value = "orderNo") String orderNo ,
                                    @PathVariable(value = "orderStatus") Integer orderStatus) {
        orderInfoService.updateOrderStatus(orderNo, orderStatus);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }
}