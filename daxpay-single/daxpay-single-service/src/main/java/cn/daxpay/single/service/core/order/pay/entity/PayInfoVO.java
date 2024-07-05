package cn.daxpay.single.service.core.order.pay.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PayInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商户订单号
     */
    private String payId;

    /**
     * 出参：云端订单号
     */
    private String orderId;
    
    /**
     * 入参：微信支付为1 支付宝支付为2
     */
    private Integer type;

    /**
     * 出参：微信支付为1 支付宝支付为2
     */
    private Integer payType;

    /**
     * 订单金额
     */
    private Double price;

    /**
     * 出参：实付金额
     */
    private Double reallyPrice;

    /**
     * 入参：签名
     */
    private String sign;

    /**
     * 入参：传输参数
     */
    private String param;

    /**
     * 入参：1跳转到支付页面，否则返回创建结果的json数据
     */
    private Integer isHtml = 0;

    /**
     * 出参：支付二维码内容
     */
    private String payUrl;

    /**
     * 出参：1需要手动输入金额 0扫码后自动输入金额
     */
    private Integer isAuto = 0;

    /**
     * 出参：订单状态：-1|订单过期 0|等待支付 1|完成 2|支付完成但通知失败
     */
    private Integer state;

    /**
     * 出参：订单有效时间（分钟）
     */
    private Integer timeOut;

    /**
     * 出参：订单创建时间时间戳（13位）
     */
    private Long date;

}
