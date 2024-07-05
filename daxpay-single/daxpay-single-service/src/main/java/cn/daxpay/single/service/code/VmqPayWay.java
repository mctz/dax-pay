package cn.daxpay.single.service.code;

import cn.daxpay.single.core.code.PayMethodEnum;
import cn.daxpay.single.core.exception.MethodNotExistException;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 支付宝支付方式
 *
 * @author xxm
 * @since 2021/7/2
 */
@UtilityClass
public class VmqPayWay {

    // 支付方式
    private static final List<PayMethodEnum> PAY_WAYS = Arrays.asList(PayMethodEnum.WX, PayMethodEnum.ALI);

    /**
     * 根据编码获取
     */
    public PayMethodEnum findByCode(String code) {
        return PAY_WAYS.stream()
            .filter(e -> Objects.equals(code, e.getCode()))
            .findFirst()
            .orElseThrow(() -> new MethodNotExistException(code + "不存在的支付方式"));
    }

    /**
     * 获取支持的支付方式
     */
    public List<PayMethodEnum> getPayWays() {
        return PAY_WAYS;
    }
}
