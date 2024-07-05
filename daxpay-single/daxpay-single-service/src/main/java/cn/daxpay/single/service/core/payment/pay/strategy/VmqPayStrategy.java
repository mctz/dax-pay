package cn.daxpay.single.service.core.payment.pay.strategy;

import cn.bootx.platform.common.core.exception.ValidationFailedException;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.core.param.payment.pay.PayParam;
import cn.daxpay.single.service.core.channel.vmq.entity.VmqPayConfig;
import cn.daxpay.single.service.core.channel.vmq.service.VmqPayConfigService;
import cn.daxpay.single.service.core.channel.vmq.service.VmqPayService;
import cn.daxpay.single.service.func.AbsPayStrategy;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * V免签支付
 * @author xxm
 * @since 2021/2/27
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class VmqPayStrategy extends AbsPayStrategy {

    private final VmqPayConfigService vmqPayConfigService;
    
    private final VmqPayService vmqPayService;

    private VmqPayConfig vmqPayConfig;

    private PayParam payParam;

    @Override
    public String getChannel() {
        return PayChannelEnum.VMQ.getCode();
    }

    /**
     * 支付前操作
     */
    @Override
    public void doBeforePayHandler() {
        try {
            // V免签参数验证
            Map<String, Object> channelParam = this.getPayParam().getExtraParam();
            if (CollUtil.isNotEmpty(channelParam)) {
                this.payParam = BeanUtil.toBean(channelParam, PayParam.class);
            }
            else {
                this.payParam = new PayParam();
            }
        } catch (JSONException e) {
            throw new ValidationFailedException("支付参数错误");
        }
        this.vmqPayConfig = vmqPayConfigService.getAndCheckConfig();
        // V免签相关校验
        vmqPayService.validation(this.getPayParam(), vmqPayConfig);
    }

    /**
     * 发起支付操作
     */
    @Override
    public void doPayHandler() {
        vmqPayService.pay(this.getOrder(), this.payParam, this.vmqPayConfig);
    }

}
