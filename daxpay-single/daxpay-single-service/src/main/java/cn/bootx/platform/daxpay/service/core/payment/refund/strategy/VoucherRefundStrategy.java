package cn.bootx.platform.daxpay.service.core.payment.refund.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.param.channel.VoucherPayParam;
import cn.bootx.platform.daxpay.service.core.channel.voucher.entity.Voucher;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherPayService;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherQueryService;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherRecordService;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.func.AbsRefundStrategy;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 储值卡支付退款
 * @author xxm
 * @since 2023/7/5
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class VoucherRefundStrategy extends AbsRefundStrategy {
    private final VoucherPayService voucherPayService;

    private final VoucherQueryService voucherQueryService;

    private final VoucherRecordService voucherRecordService;

    private Voucher voucher;

    private VoucherPayParam voucherPayParam;

    /**
     * 策略标识
     *
     * @see PayChannelEnum
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.VOUCHER;
    }

    /**
     * 退款前对处理
     */
    @Override
    public void initRefundParam(PayOrder payOrder, PayChannelOrder payChannelOrder) {
        // 先设置参数
        super.initRefundParam(payOrder, payChannelOrder);
        // 从通道扩展参数中取出钱包参数
        String channelExtra = this.getPayChannelOrder().getChannelExtra();
        this.voucherPayParam = JSONUtil.toBean(channelExtra, VoucherPayParam.class);
    }

    /**
     * 退款前对处理
     */
    @Override
    public void doBeforeRefundHandler() {
        // 如果任务执行完成, 则跳过
        if (Objects.equals(this.getRefundChannelOrder().getStatus(), RefundStatusEnum.SUCCESS.getCode())){
            return;
        }
        // 不包含异步支付, 则只在支付订单中进行扣减, 等待异步退款完成, 再进行退款
        if (!this.getPayOrder().isAsyncPay()) {
            this.voucher = voucherQueryService.getVoucherByCardNo(this.voucherPayParam.getCardNo());
        }
    }


    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        // 如果任务执行完成, 则跳过
        if (Objects.equals(this.getRefundChannelOrder().getStatus(), RefundStatusEnum.SUCCESS.getCode())){
            return;
        }
        // 不包含异步支付, 直接完成退款
        if (!this.getPayOrder().isAsyncPay()){
            voucherPayService.refund(this.getRefundChannelParam().getAmount(), this.voucher);
            voucherRecordService.refund(this.getRefundChannelOrder(), this.getPayOrder().getTitle(), this.voucher);
        }
    }

    /**
     * 退款发起成功操作, 异步支付通道需要进行重写
     */
    @Override
    public void doSuccessHandler() {
        // 如果任务执行完成, 则跳过
        if (Objects.equals(this.getRefundChannelOrder().getStatus(), RefundStatusEnum.SUCCESS.getCode())){
            return;
        }
        // 包含异步支付, 变更状态到退款中
        if (this.getPayOrder().isAsyncPay()) {
            this.getPayChannelOrder().setStatus(PayStatusEnum.REFUNDING.getCode());
            this.getRefundChannelOrder().setStatus(RefundStatusEnum.PROGRESS.getCode());
        } else{
            // 同步支付, 直接标识状态为退款完成
            super.doSuccessHandler();
        }
    }

    /**
     * 生成通道退款订单对象
     */
    @Override
    public void generateChannelOrder() {
        // 先生成通用的通道退款订单对象
        super.generateChannelOrder();
        // 设置扩展参数
        this.getRefundChannelOrder().setChannelExtra(JSONUtil.toJsonStr(this.voucherPayParam));
    }
}
