package cn.daxpay.single.service.code;

/**
 * V免签支付参数
 *
 * @author xxm
 * @since 2021/2/27
 */
public interface VmqPayCode {

    String CODE = "code";

    String MSG = "msg";

    String DATA = "data";

    String PAYID = "payId";

    String ORDERID = "orderId";

    int success = 1;

    int fail = -1;

    String createOrder = "/createOrder";

    String checkOrder = "/checkOrder";

    String getOrder = "/getOrder";

    String closeOrder = "/closeOrder";

}
