package cn.daxpay.single.service.core.channel.vmq.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.daxpay.single.service.common.typehandler.DecryptTypeHandler;
import cn.daxpay.single.service.core.channel.vmq.convert.VmqPayConvert;
import cn.daxpay.single.service.dto.channel.vmq.VmqPayConfigDto;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * V免签支付配置
 *
 * @author xxm
 * @since 2020/12/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "V免签支付配置")
@TableName(value = "pay_vmqpay_config",autoResultMap = true)
public class VmqPayConfig extends MpBaseEntity implements EntityBaseFunction<VmqPayConfigDto> {

    /** V免签商户appId */
    @DbColumn(comment = "V免签商户appId", length = 50)
    private String appId;

    @DbColumn(comment = "签名Key", length = 50)
    private String appKey;

    /** 是否启用, 只影响支付和退款操作 */
    @DbColumn(comment = "是否启用")
    private Boolean enable;

    @DbColumn(comment = "邮箱")
    private String email;

    /** 支付限额 */
    @DbColumn(comment = "支付限额", length = 15)
    private Integer limitAmount;

    @DbColumn(comment = "异步通知接收路径", length = 200)
    private String notifyUrl;

    @DbColumn(comment = "同步通知页面路径", length = 200)
    private String returnUrl;

    /** 支付网关地址 */
    @DbColumn(comment = "支付网关地址", length = 200)
    private String serverUrl;

    /** 授权回调地址 */
    @DbColumn(comment = "授权回调地址", length = 200)
    private String redirectUrl;

    /** 签名类型 RSA2 */
    @DbColumn(comment = "签名类型MD5", length = 20)
    public String signType;

    /**
     * 是商家与V免签签约后，商家获得的V免签商家唯一识别码，以 2088 开头的 16 位数字组成，在开放平台中账户中心获取
     */
    @TableField(typeHandler = DecryptTypeHandler.class)
    @DbColumn(comment = "合作者身份ID")
    private String alipayUserId;

    /** 备注 */
    @DbColumn(comment = "备注")
    private String remark;

    @Override
    public VmqPayConfigDto toDto() {
        return VmqPayConvert.CONVERT.convert(this);
    }

    public Boolean getEnable() {
        return Objects.equals(enable,true);
    }

    public String getRedirectUrl() {
        return StrUtil.removeSuffix(redirectUrl, "/");
    }

}
