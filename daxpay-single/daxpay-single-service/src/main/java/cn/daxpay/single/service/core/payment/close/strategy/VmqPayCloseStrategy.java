package cn.daxpay.single.service.core.payment.close.strategy;

import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.service.core.channel.vmq.entity.VmqPayConfig;
import cn.daxpay.single.service.core.channel.vmq.service.VmqPayCloseService;
import cn.daxpay.single.service.core.channel.vmq.service.VmqPayConfigService;
import cn.daxpay.single.service.func.AbsPayCloseStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 *
 * @author xxm
 * @since 2023/12/30
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class VmqPayCloseStrategy extends AbsPayCloseStrategy {

    private final VmqPayConfigService vmqPayConfigService;

    private final VmqPayCloseService vmqPayCloseService;

    private VmqPayConfig alipayConfig;

    @Override
    public String getChannel() {
        return PayChannelEnum.VMQ.getCode();
    }

    /**
     * 关闭前的处理方式
     */
    @Override
    public void doBeforeCloseHandler() {
        this.alipayConfig = vmqPayConfigService.getConfig();
    }

    /**
     * 关闭操作
     */
    @Override
    public void doCloseHandler() {
        vmqPayCloseService.close(this.getOrder(), this.alipayConfig);
    }
}
