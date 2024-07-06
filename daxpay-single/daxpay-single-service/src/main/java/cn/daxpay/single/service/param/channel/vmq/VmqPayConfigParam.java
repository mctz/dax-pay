package cn.daxpay.single.service.param.channel.vmq;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author xxm
 * @since 2021/2/26
 */
@Data
@Accessors(chain = true)
@Schema(title = "V免签配置参数")
public class VmqPayConfigParam {

    @Schema(description = "V免签商户appId")
    private String appId;
    
    @Schema(description = "V免签密钥")
    private String appKey;

    @Schema(description = "是否启用")
    private Boolean enable;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "支付限额")
    private Integer limitAmount;

    @Schema(description = "服务器异步通知页面路径")
    private String notifyUrl;

    @Schema(description = "页面跳转同步通知页面路径")
    private String returnUrl;

    @Schema(description = "授权回调地址")
    private String redirectUrl;

    @Schema(description = "请求网关地址")
    private String serverUrl;

    @Schema(description = "签名类型")
    public String signType;

    @Schema(description = "合作者身份ID")
    private String alipayUserId;

    @Schema(description = "超时配置")
    private Integer expireTime;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "备注")
    private String remark;

}
