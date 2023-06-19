package cn.bootx.platform.daxpay.core.payment.entity;

import cn.bootx.mybatis.table.modify.annotation.DbColumn;
import cn.bootx.mybatis.table.modify.annotation.DbComment;
import cn.bootx.mybatis.table.modify.annotation.DbTable;
import cn.bootx.mybatis.table.modify.mybatis.mysq.annotation.DbMySqlFieldType;
import cn.bootx.mybatis.table.modify.mybatis.mysq.constants.MySqlFieldTypeEnum;
import cn.bootx.platform.common.core.annotation.BigField;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.handler.JacksonRawTypeHandler;
import cn.bootx.platform.daxpay.code.pay.PayStatusCode;
import cn.bootx.platform.daxpay.core.payment.convert.PaymentConvert;
import cn.bootx.platform.daxpay.dto.payment.PayChannelInfo;
import cn.bootx.platform.daxpay.dto.payment.PaymentDto;
import cn.bootx.platform.daxpay.dto.payment.RefundableInfo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付记录
 *
 * @author xxm
 * @date 2020/12/8
 */
@DbTable
@EqualsAndHashCode(callSuper = true)
@Data
@DbComment("支付记录")
@FieldNameConstants
@Accessors(chain = true)
@TableName(value = "pay_payment", autoResultMap = true)
public class Payment extends MpBaseEntity implements EntityBaseFunction<PaymentDto> {

    /** 关联的业务id */
    @DbColumn(comment = "商户编码")
    private String businessId;

    /** 商户编码 */
    @DbColumn(comment = "商户编码")
    private String mchCode;

    /** 商户应用编码 */
    @DbColumn(comment = "商户应用编码")
    private String mchAppCode;

    /** 标题 */
    private String title;

    /** 描述 */
    private String description;

    /** 是否是异步支付 */
    private boolean asyncPayMode;

    /**
     * 异步支付通道
     * @see cn.bootx.platform.daxpay.code.pay.PayChannelEnum#ALI
     */
    private String asyncPayChannel;

    /** 金额 */
    private BigDecimal amount;

    /** 可退款余额 */
    private BigDecimal refundableBalance;

    /** 错误码 */
    private String errorCode;

    /** 错误信息 */
    private String errorMsg;

    /**
     * 支付通道信息列表
     * @see PayChannelInfo
     */
    @TableField(typeHandler = JacksonRawTypeHandler.class)
    @BigField
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    private List<PayChannelInfo> payChannelInfo;

    /**
     * 退款信息列表
     * @see RefundableInfo
     */
    @TableField(typeHandler = JacksonRawTypeHandler.class)
    @BigField
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    private List<RefundableInfo> refundableInfo;

    /**
     * 支付状态
     * @see PayStatusCode#TRADE_PROGRESS
     */
    private String payStatus;

    /** 支付时间 */
    private LocalDateTime payTime;

    /** 支付终端ip */
    private String clientIp;

    /** 过期时间 */
    private LocalDateTime expiredTime;

    @Override
    public PaymentDto toDto() {
        return PaymentConvert.CONVERT.convert(this);
    }

}