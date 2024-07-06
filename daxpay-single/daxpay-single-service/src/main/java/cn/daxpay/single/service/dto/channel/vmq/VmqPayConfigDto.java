package cn.daxpay.single.service.dto.channel.vmq;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.starter.data.perm.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xxm
 * @since 2021/2/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "V免签配置")
public class VmqPayConfigDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 6641158663606363171L;

    @Schema(description = "V免签商户appId")
    @SensitiveInfo
    private String appId;

    @Schema(description = "密钥")
    @SensitiveInfo
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
    private String signType;

    @Schema(description = "合作者身份ID")
    @SensitiveInfo(value = SensitiveInfo.SensitiveType.OTHER, front = 15)
    private String alipayUserId;

    @Schema(description = "备注")
    private String remark;

}
