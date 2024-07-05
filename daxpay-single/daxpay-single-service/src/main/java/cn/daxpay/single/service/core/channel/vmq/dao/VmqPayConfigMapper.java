package cn.daxpay.single.service.core.channel.vmq.dao;

import cn.daxpay.single.service.core.channel.vmq.entity.VmqPayConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * V免签配置
 * @author xxm
 * @since 2023/12/18
 */
@Mapper
public interface VmqPayConfigMapper extends BaseMapper<VmqPayConfig> {

}
