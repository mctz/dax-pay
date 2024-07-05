package cn.daxpay.single.service.core.channel.vmq.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.single.service.core.channel.vmq.entity.VmqPayConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * V免签配置
 *
 * @author xxm
 * @since 2021/2/26
 */
@Repository
@RequiredArgsConstructor
public class VmqPayConfigManager extends BaseManager<VmqPayConfigMapper, VmqPayConfig> {

}
