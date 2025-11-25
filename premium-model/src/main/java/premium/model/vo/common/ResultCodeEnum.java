package premium.model.vo.common;

import lombok.Getter;

@Getter // 提供获取属性值的getter方法
public enum ResultCodeEnum {

    SUCCESS(200 , "操作成功") ,
    LOGIN_ERROR(201 , "用户名或者密码错误"),
    VALIDATECODE_ERROR(202 , "验证码错误") ,
    LOGIN_AUTH(208 , "用户未登录"),
    USER_NAME_IS_EXISTS(209 , "用户名已经存在"),
    SYSTEM_ERROR(9999 , "您的网络有问题请稍后重试"),
    NODE_ERROR( 217, "该节点下有子节点，不可以删除"),
    DATA_ERROR(204, "数据异常"),
    ACCOUNT_STOP( 216, "账号已停用"),
    ORDER_CANT_CANCELED(218, "订单无法取消"),
    ORDER_ERROR(219, "订单状态异常"),
    STOCK_LESS( 220, "库存不足"),
    STOCK_OPT_ERROR(221, "库存扣减失败"),
    SEND_CODE_SYS_ERROR(8888, "验证码发送失败"),
    ;

    private final Integer code ;      // 业务状态码
    private final String message ;    // 响应消息

    ResultCodeEnum(Integer code , String message) {
        this.code = code ;
        this.message = message ;
    }

}
