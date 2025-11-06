package premium.manager.task;

import cn.hutool.core.date.DateUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import premium.manager.mapper.OrderInfoMapper;
import premium.manager.mapper.OrderStatisticsMapper;
import premium.model.entity.order.OrderStatistics;

import java.util.Date;

@Component
public class OrderStatisticsTask {
    private final OrderInfoMapper orderInfoMapper;
    private final OrderStatisticsMapper orderStatisticsMapper;

    public OrderStatisticsTask(OrderInfoMapper orderInfoMapper, OrderStatisticsMapper orderStatisticsMapper) {
        this.orderInfoMapper = orderInfoMapper;
        this.orderStatisticsMapper = orderStatisticsMapper;
    }

//    /**
//     * 测试每5s运行一次
//     * SpringTask: @Scheduled注解 + cron表达式
//     */
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void testHello() {
//        System.out.println(new Date().toInstant());
//    }

    /**
     * 每天凌晨2点，查询前一天统计数据
     * 将数据添加进统计结果表
     * (基于定时任务实现，防止频繁更改数据库)
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void orderTotalAmountStatics() {
        // 获取前一天日期
        String createDate = DateUtil.offsetDay(new Date(), -1).toString("yyyy-MM-dd");
        // 查询日期的订单交易总金额
        OrderStatistics orderStatistics = orderInfoMapper.selectStatisticsByDate(createDate);
        // 添加数据进入统计结果表
        if (orderStatistics != null) {
            orderStatisticsMapper.insert(orderStatistics);
        }
    }
}
