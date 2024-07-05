package cn.daxpay.single.service.core.payment.sync.strategy.pay;


import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.service.core.channel.vmq.entity.VmqPayConfig;
import cn.daxpay.single.service.core.channel.vmq.service.VmqPayConfigService;
import cn.daxpay.single.service.core.channel.vmq.service.VmqPaySyncService;
import cn.daxpay.single.service.core.payment.sync.result.PayRemoteSyncResult;
import cn.daxpay.single.service.func.AbsPaySyncStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * V免签支付同步
 * @author xxm
 * @since 2023/7/14
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class VmqPaySyncStrategy extends AbsPaySyncStrategy {

    private final VmqPayConfigService vmqPayConfigService;

    private final VmqPaySyncService vmqPaySyncService;

    /**
     * 策略标识
     */
    @Override
    public String getChannel() {
        return PayChannelEnum.VMQ.getCode();
    }

    /**
     * 异步支付单与支付网关进行状态比对
     */
    @Override
    public PayRemoteSyncResult doSyncStatus() {
        VmqPayConfig config = vmqPayConfigService.getConfig();
        return vmqPaySyncService.syncPayStatus(this.getOrder(),config);
    }
}
