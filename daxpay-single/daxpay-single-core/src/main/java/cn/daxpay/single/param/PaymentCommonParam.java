package cn.daxpay.single.param;

import cn.daxpay.single.serializer.TimestampToLocalDateTimeDeserializer;
import cn.bootx.platform.common.core.validation.IpAddress;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 支付公共参数
 * @author xxm
 * @since 2023/12/17
 */
@Data
@Schema(title = "支付公共参数")
public abstract class PaymentCommonParam {

    /** 客户端ip */
    @Schema(description = "客户端ip")
    @IpAddress
    private String clientIp;

    /** 签名 */
    @Schema(description = "签名")
    private String sign;


    /** 请求时间，时间戳转时间 */
    @Schema(description = "请求时间，传输时间戳")
    @NotNull(message = "请求时间必填")
    @JsonDeserialize(using = TimestampToLocalDateTimeDeserializer.class)
    private LocalDateTime reqTime;

}
