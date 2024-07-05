package cn.daxpay.single.admin.controller.channel;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.daxpay.single.service.core.channel.vmq.service.VmqPayConfigService;
import cn.daxpay.single.service.dto.channel.vmq.VmqPayConfigDto;
import cn.daxpay.single.service.param.channel.vmq.VmqPayConfigParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author xxm
 * @since 2021/2/26
 */
@Tag(name = "V免签配置")
@RestController
@RequestMapping("/vmq/config")
@AllArgsConstructor
public class VmqPayConfigController {

    private final VmqPayConfigService alipayConfigService;

    @Operation(summary = "获取配置")
    @GetMapping("/getConfig")
    public ResResult<VmqPayConfigDto> getConfig() {
        return Res.ok(alipayConfigService.getConfig().toDto());
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public ResResult<Void> update(@RequestBody VmqPayConfigParam param) {
        alipayConfigService.update(param);
        return Res.ok();
    }

    @Operation(summary = "生成异步通知地址")
    @GetMapping("/generateNotifyUrl")
    public ResResult<String> generateNotifyUrl() {
        return Res.ok(alipayConfigService.generateNotifyUrl());
    }

    @Operation(summary = "生成同步通知地址")
    @GetMapping("/generateReturnUrl")
    public ResResult<String> generateReturnUrl() {
        return Res.ok(alipayConfigService.generateReturnUrl());
    }
}
