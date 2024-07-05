package cn.daxpay.single.service.core.channel.vmq.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.daxpay.single.core.code.PayMethodEnum;
import cn.daxpay.single.core.exception.AmountExceedLimitException;
import cn.daxpay.single.core.exception.MethodNotExistException;
import cn.daxpay.single.core.exception.PayFailureException;
import cn.daxpay.single.core.exception.TradeFailException;
import cn.daxpay.single.core.param.channel.AliPayParam;
import cn.daxpay.single.core.param.payment.pay.PayParam;
import cn.daxpay.single.core.util.PayUtil;
import cn.daxpay.single.service.code.AliPayCode;
import cn.daxpay.single.service.code.VmqPayCode;
import cn.daxpay.single.service.code.VmqPayWay;
import cn.daxpay.single.service.common.context.PayLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.channel.vmq.entity.VmqPayConfig;
import cn.daxpay.single.service.core.order.pay.entity.PayInfoVO;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.util.RestUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qcloud.cos.utils.Md5Utils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static cn.daxpay.single.service.code.AliPayCode.QUICK_MSECURITY_PAY;


/**
 * V免签支付服务
 *
 * @author xxm
 * @since 2021/2/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VmqPayService {

    /**
     * 支付前检查支付方式是否可用
     */
    public void validation(PayParam payParam, VmqPayConfig alipayConfig) {
        // 发起的支付类型是否在支持的范围内
        PayMethodEnum payMethodEnum = Optional.ofNullable(VmqPayWay.findByCode(payParam.getMethod()))
                .orElseThrow(() -> new MethodNotExistException("非法的V免签支付类型"));
        // 验证订单金额是否超限
        if(payParam.getAmount() > alipayConfig.getLimitAmount()){
            throw new AmountExceedLimitException("V免签支付金额超过限额");
        }
    }

    /**
     * 调起支付
     */
    public void pay(PayOrder payOrder, PayParam payParam, VmqPayConfig payConfig) {
        double amount = PayUtil.conversionAmount(payOrder.getAmount()).doubleValue();
        // 异步线程存储
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();
        PayInfoVO infoVO = new PayInfoVO();
        infoVO.setPayId(payOrder.getBizOrderNo());
        infoVO.setPrice(amount);
        infoVO.setType(1);
        if (Objects.equals(payOrder.getMethod(), PayMethodEnum.ALI.getCode())) {
            infoVO.setType(2);
        }
        String sign = Md5Utils.md5Hex(infoVO.getPayId() + infoVO.getType() + infoVO.getPrice() + payConfig.getAppKey());
        infoVO.setSign(sign);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> param = objectMapper.convertValue(infoVO,Map.class);
        HttpHeaders headers = new HttpHeaders();
        String mediaType = MediaType.APPLICATION_JSON_UTF8_VALUE;
        headers.setContentType(MediaType.parseMediaType(mediaType));
        String url = payConfig.getServerUrl() + VmqPayCode.createOrder;
        JSONObject result = RestUtil.request(url, HttpMethod.POST, headers, JSON.parseObject(JSON.toJSONString(param)), JSON.toJSONString(param), JSONObject.class).getBody();
        if (result.getIntValue(VmqPayCode.CODE) == VmqPayCode.fail) {
            throw new PayFailureException(result.getString(VmqPayCode.MSG));
        }
        payOrder.setOrderNo(result.getJSONObject(VmqPayCode.DATA).getString("orderId"));
        String payUrl = result.getJSONObject(VmqPayCode.DATA).getString("payUrl");
        // 通常是发起支付的参数
        payInfo.setPayBody(payUrl);
    }

    /**
     * 验证错误信息
     */
    private void verifyErrorMsg(AlipayResponse alipayResponse) {
        if (!Objects.equals(alipayResponse.getCode(), AliPayCode.SUCCESS)) {
            String errorMsg = alipayResponse.getSubMsg();
            if (StrUtil.isBlank(errorMsg)) {
                errorMsg = alipayResponse.getMsg();
            }
            log.error("支付失败 {}", errorMsg);
            throw new TradeFailException(errorMsg);
        }
    }

}
