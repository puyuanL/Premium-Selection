package premium.model.entity.order;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CANCELLED(-1, "已取消"),
    WAIT_PAYMENT(0, "待付款"),
    WAIT_SHIPMENT(1, "待发货"),
    PAID(1, "已付款"),
    DISPATCHED(2, "已发货"),
    WAIT_RECEIVE(3, "待收货")
    ;

    private final Integer code;
    private final String message;

    OrderStatus(Integer code, String message) {
        this.code = code ;
        this.message = message ;
    }
}
