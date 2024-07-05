package cn.daxpay.single.service.core.payment.repair.strategy.pay;

import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.service.core.channel.vmq.entity.VmqPayConfig;
import cn.daxpay.single.service.core.channel.vmq.service.VmqPayCloseService;
import cn.daxpay.single.service.core.channel.vmq.service.VmqPayConfigService;
import cn.daxpay.single.service.func.AbsPayRepairStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * V免签订单修复策略
 * @author xxm
 * @since 2023/12/27
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class VmqPayRepairStrategy extends AbsPayRepairStrategy {

    private final VmqPayCloseService closeService;

    private final VmqPayConfigService vmqPayConfigService;

    private VmqPayConfig config;

    /**
     * 策略标识
     */
     @Override
    public String getChannel() {
        return PayChannelEnum.VMQ.getCode();
    }

    /**
     * 修复前处理
     */
    @Override
    public void doBeforeHandler() {
        this.config = vmqPayConfigService.getConfig();
    }


    /**
     * 关闭本地支付和网关支付
     */
    @Override
    public void doCloseRemoteHandler() {
        closeService.close(this.getOrder(), this.config);
    }
}
