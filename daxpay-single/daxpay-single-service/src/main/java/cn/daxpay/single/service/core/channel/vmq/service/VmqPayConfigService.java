package cn.daxpay.single.service.core.channel.vmq.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.daxpay.single.service.core.channel.vmq.dao.VmqPayConfigManager;
import cn.daxpay.single.service.core.channel.vmq.entity.VmqPayConfig;
import cn.daxpay.single.service.core.system.config.service.PlatformConfigService;
import cn.daxpay.single.service.param.channel.vmq.VmqPayConfigParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * V免签支付
 *
 * @author xxm
 * @since 2020/12/15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VmqPayConfigService {
    /** 默认V免签配置的主键ID */
    private final static Long ID = 0L;
    private final VmqPayConfigManager alipayConfigManager;
    private final PlatformConfigService platformConfigService;

    /**
     * 修改
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(VmqPayConfigParam param) {
        VmqPayConfig alipayConfig = alipayConfigManager.findById(ID).orElseThrow(() -> new DataNotExistException("V免签配置不存在"));
        BeanUtil.copyProperties(param, alipayConfig, CopyOptions.create().ignoreNullValue());
        alipayConfigManager.updateById(alipayConfig);
    }

    /**
     * 获取支付配置
     */
    public VmqPayConfig getConfig(){
        return alipayConfigManager.findById(ID).orElseThrow(() -> new DataNotExistException("V免签配置不存在"));
    }

    /**
     * 获取并检查支付配置
     */
    public VmqPayConfig getAndCheckConfig() {
        VmqPayConfig alipayConfig = this.getConfig();
        return alipayConfig;
    }

    /**
     * 生成通知地址
     */
    public String generateNotifyUrl(){
        return platformConfigService.getConfig().getWebsiteUrl() + "/unipay/callback/alipay";
    }

    /**
     * 生成同步跳转地址
     */
    public String generateReturnUrl(){
        return platformConfigService.getConfig().getWebsiteUrl() + "/unipay/return/alipay";
    }

    /**
     * 获取V免签SDK的配置
     */
    @SneakyThrows
    public AlipayClient getAlipayClient(VmqPayConfig aliPayConfig){
        AlipayConfig config = new AlipayConfig();
        config.setServerUrl(aliPayConfig.getServerUrl());
        config.setAppId(aliPayConfig.getAppId());
        config.setFormat("json");
        config.setCharset("UTF-8");
        config.setSignType(aliPayConfig.getSignType());
        return new DefaultAlipayClient(config);
    }
}
