package cn.daxpay.single.service.param.channel.vmq;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * V免签同步回调参数
 * @author xxm
 * @since 2024/2/11
 */
@Data
@Accessors(chain = true)
@Schema(title = "V免签同步回调参数")
public class VmqPayReturnParam {

    /**
     * 编码格式，如 utf-8、gbk、gb2312 等。
     */
    @Schema(description = "编码格式")
    @JsonAlias("charset")
    private String charset;

    @Schema(description = "商户订单号")
    @JsonAlias("payId")
    private String payId;

    @Schema(description = "云端订单编号")
    @JsonAlias("orderId")
    private String orderId;

    @Schema(description = "入参")
    @JsonAlias("param")
    private String param;

    @Schema(description = "支付方式")
    @JsonAlias("type")
    private Integer type;

    @Schema(description = "订单金额")
    @JsonAlias("price")
    private Double price;

    @Schema(description = "实付金额")
    @JsonAlias("reallyPrice")
    private Double reallyPrice;

    @Schema(description = "签名")
    @JsonAlias("sign")
    private String sign;

    /**
     * V免签分配给开发者的应用ID。
     * 示例值：2016040501024706
     */
    @Schema(description = "应用ID")
    @JsonAlias("app_id")
    private String appId;

    /**
     * 签名算法类型，目前支持 RSA2 和 RSA，推荐使用RSA2
     */
    @Schema(description = "签名算法类型")
    @JsonAlias("sign_type")
    private String signType;

    /**
     * 前台回跳的时间，格式：yyyy-MM-dd HH:mm:ss。
     */
    @Schema(description = "前台回跳的时间")
    @JsonAlias("timestamp")
    private String timestamp;
}
