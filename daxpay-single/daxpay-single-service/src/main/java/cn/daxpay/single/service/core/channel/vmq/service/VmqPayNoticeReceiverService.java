package cn.daxpay.single.service.core.channel.vmq.service;

import cn.daxpay.single.service.core.channel.vmq.entity.VmqPayConfig;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * V免签通知消息接收
 * @author xxm
 * @since 2024/5/31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VmqPayNoticeReceiverService {

    private final VmqPayConfigService aliPayConfigService;

    /**
     * 校验消息通知
     */
    private boolean verifyNotify(Map<String, String> params) {
        String callReq = JSONUtil.toJsonStr(params);
        log.info("V免签消息通知报文: {}", callReq);
        VmqPayConfig alipayConfig = aliPayConfigService.getConfig();
        if (Objects.isNull(alipayConfig)) {
            log.error("V免签支付配置不存在");
            return false;
        }
        return true;
    }
}
