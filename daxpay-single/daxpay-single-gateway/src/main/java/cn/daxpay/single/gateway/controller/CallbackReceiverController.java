package cn.daxpay.single.gateway.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayCallbackService;
import cn.daxpay.single.service.core.channel.union.service.UnionPayCallbackService;
import cn.daxpay.single.service.core.channel.vmq.service.VmqPayCallbackService;
import cn.daxpay.single.service.core.channel.wechat.service.WeChatPayCallbackService;
import cn.daxpay.single.service.core.extra.AliPayAuthService;
import cn.daxpay.single.service.core.extra.WeChatAuthService;
import cn.daxpay.single.service.sdk.union.api.UnionPayKit;
import cn.daxpay.single.core.util.PayUtil;
import com.egzosn.pay.union.api.UnionPayConfigStorage;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.WxPayKit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 包括支付成功/退款/转账等一系列类型的回调处理
 * 也包括获取微信OpenId/支付宝UserId等的授权回调
 * @author xxm
 * @since 2021/2/27
 */
@IgnoreAuth
@Slf4j
@Tag(name = "支付通道信息回调")
@RestController
@RequestMapping("/unipay/callback")
@AllArgsConstructor
public class CallbackReceiverController {

    private final VmqPayCallbackService vmqPayCallbackService;

    private final AliPayCallbackService aliPayCallbackService;

    private final WeChatAuthService wechatAuthService;

    private final WeChatPayCallbackService weChatPayCallbackService;

    private final UnionPayCallbackService unionPayCallbackService;

    private final AliPayAuthService aliPayAuthService;

    @SneakyThrows
    @Operation(summary = "V免签信息回调")
    @PostMapping("/vmq")
    public String vmqPayNotify(HttpServletRequest request) {
        Map<String, String> stringStringMap = PayUtil.toMap(request);
        return vmqPayCallbackService.callback(stringStringMap);
    }

    @SneakyThrows
    @Operation(summary = "支付宝信息回调")
    @PostMapping("/alipay")
    public String aliPayNotify(HttpServletRequest request) {
        Map<String, String> stringStringMap = PayUtil.toMap(request);
        return aliPayCallbackService.callback(stringStringMap);
    }


    @Operation(summary = "支付宝认证授权回调")
    @GetMapping("/alipay/auth/{code}")
    public ModelAndView wechatCallback(@RequestParam("code") String authCode, @PathVariable("code") String code){
        aliPayAuthService.authCallback(authCode, code);
        // 调用页面自动关闭
        return new ModelAndView("forward:/h5/openIdCallbackClose.html");
    }

    @SneakyThrows
    @Operation(summary = "微信支付信息回调")
    @PostMapping("/wechat")
    public String wechatPayNotify(HttpServletRequest request) {
        String xmlMsg = HttpKit.readData(request);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);
        return weChatPayCallbackService.callback(params);
    }

    @Operation(summary = "微信认证授权回调")
    @GetMapping("/wechat/auth/{code}")
    public ModelAndView wxAuthCallback(@RequestParam("code") String authCode, @PathVariable("code") String code){
        wechatAuthService.authCallback(authCode, code);
        // 调用页面自动关闭
        return new ModelAndView("forward:/h5/openIdCallbackClose.html");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @SneakyThrows
    @Operation(summary = "云闪付支付信息回调")
    @PostMapping("/union")
    public String unionPayNotify(HttpServletRequest request) {
        UnionPayKit unionPayKit = new UnionPayKit(new UnionPayConfigStorage());
        // 实际返回的是 Map<String, String> 格式数据
        Map parameter2Map = unionPayKit.getParameter2Map(request.getParameterMap(), request.getInputStream());
        return unionPayCallbackService.callback(parameter2Map);
    }
}
