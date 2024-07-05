package cn.daxpay.single.service.core.channel.vmq.convert;

import cn.daxpay.single.service.core.channel.vmq.entity.VmqPayConfig;
import cn.daxpay.single.service.dto.channel.vmq.VmqPayConfigDto;
import cn.daxpay.single.service.param.channel.vmq.VmqPayConfigParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * V免签转换
 *
 * @author xxm
 * @since 2021/7/5
 */
@Mapper
public interface VmqPayConvert {

    VmqPayConvert CONVERT = Mappers.getMapper(VmqPayConvert.class);

    VmqPayConfig convert(VmqPayConfigParam in);

    VmqPayConfigDto convert(VmqPayConfig in);
}
