package cn.daxpay.single.service.core.channel.vmq.service;

import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.core.code.PayStatusEnum;
import cn.daxpay.single.service.code.PayCallbackStatusEnum;
import cn.daxpay.single.service.code.PayRepairSourceEnum;
import cn.daxpay.single.service.code.PaymentTypeEnum;
import cn.daxpay.single.service.common.context.CallbackLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.channel.vmq.entity.VmqPayConfig;
import cn.daxpay.single.service.core.payment.callback.service.PayCallbackService;
import cn.daxpay.single.service.core.record.callback.service.PayCallbackRecordService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.qcloud.cos.utils.Md5Utils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * V免签回调处理
 *
 * @author xxm
 * @since 2021/2/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VmqPayCallbackService {

    private final VmqPayConfigService vmqPayConfigService;

    private final PayCallbackRecordService callbackService;

    private final PayCallbackService payCallbackService;

    /**
     * 回调处理入口
     */
    public String callback(Map<String, String> params){
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        try {
            // 将参数写入到上下文中
            callbackInfo.getCallbackParam().putAll(params);

            // 判断并保存回调类型
            PaymentTypeEnum callbackType = this.getCallbackType();
            callbackInfo.setCallbackType(callbackType).setChannel(PayChannelEnum.VMQ.getCode());

            // 验证消息
            if (!this.verifyNotify()) {
                callbackInfo.setCallbackStatus(PayCallbackStatusEnum.FAIL).setErrorMsg("验证信息格式不通过");
                // 消息有问题, 保存记录并返回
                this.resolvePayData();
                callbackService.saveCallbackRecord();
                return null;
            }
            // 提前设置订单修复的来源
            PaymentContextLocal.get().getRepairInfo().setSource(PayRepairSourceEnum.CALLBACK);

            if (callbackType == PaymentTypeEnum.PAY){
                // 解析支付数据并放处理
                this.resolvePayData();
                payCallbackService.payCallback();
            }
            callbackService.saveCallbackRecord();
            return this.getReturnMsg();
        } catch (Exception e) {
            log.error("回调处理失败", e);
            callbackInfo.setCallbackStatus(PayCallbackStatusEnum.FAIL).setErrorMsg("回调处理失败: "+e.getMessage());
            callbackService.saveCallbackRecord();
            throw e;
        }
    }

    /**
     * 验证信息格式是否合法
     */
    @SneakyThrows
    public boolean verifyNotify() {
        Map<String, String> params = PaymentContextLocal.get().getCallbackInfo().getCallbackParam();
        String callReq = JSONUtil.toJsonStr(params);
        log.info("V免签发起回调 报文: {}", callReq);
        String payId = params.get("payId");
        if (StrUtil.isBlank(payId)) {
            log.error("V免签回调报文payId为空");
            return false;
        }
        VmqPayConfig alipayConfig = vmqPayConfigService.getConfig();
        if (Objects.isNull(alipayConfig)) {
            log.error("V免签支付配置不存在");
            return false;
        }
        String appKey = alipayConfig.getAppKey();
        String sign = Md5Utils.md5Hex(payId + params.get("param") + params.get("type") + params.get("price") + params.get("reallyPrice") + appKey);
        if (!sign.equals(params.get("sign"))) {
            log.error("签名校验不通过");
            return false;
        }
        return true;
    }

    /**
     * 解析支付数据并放到上下文中
     */
    public void resolvePayData() {
        CallbackLocal callback = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> callbackParam = callback.getCallbackParam();
        // 网关订单号
        callback.setOutTradeNo(callbackParam.get("orderId"));
        // 支付订单ID
        callback.setTradeNo(callbackParam.get("orderId"));
        callback.setRepairNo(callbackParam.get("orderId"));
        // 支付状态
        callback.setOutStatus(PayStatusEnum.SUCCESS.getCode());
        // 支付金额
        callback.setAmount(callbackParam.get("reallyPrice"));
        // 支付时间
       callback.setFinishTime(LocalDateTime.now());

    }

    /**
     * 判断类型 支付回调/退款回调
     *
     * @see PaymentTypeEnum
     */
    public PaymentTypeEnum getCallbackType() {
        return PaymentTypeEnum.PAY;
    }

    /**
     * 返回值
     */
    public String getReturnMsg() {
        return "success";
    }
}
