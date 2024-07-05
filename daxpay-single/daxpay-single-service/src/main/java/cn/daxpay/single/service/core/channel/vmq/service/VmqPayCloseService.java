package cn.daxpay.single.service.core.channel.vmq.service;

import cn.daxpay.single.core.code.PaySyncStatusEnum;
import cn.daxpay.single.core.exception.OperationFailException;
import cn.daxpay.single.core.exception.TradeStatusErrorException;
import cn.daxpay.single.service.code.AliPayCode;
import cn.daxpay.single.service.code.VmqPayCode;
import cn.daxpay.single.service.core.channel.vmq.entity.VmqPayConfig;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.payment.sync.result.PayRemoteSyncResult;
import cn.daxpay.single.service.util.RestUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeCancelModel;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.qcloud.cos.utils.Md5Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * V免签支付撤销和支付关闭
 *
 * @author xxm
 * @since 2021/4/20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VmqPayCloseService {

    /**
     * 关闭支付 此处使用交易关闭接口
     * 交易关闭: 只有订单在未支付的状态下才可以进行关闭, 商户不需要额外申请就有此接口的权限
     */
    public void close(PayOrder payOrder, VmqPayConfig payConfig) {
        // 查询
        Map<String,Object> param = new HashMap<>();
        String sign = Md5Utils.md5Hex(payOrder.getOrderNo() + payConfig.getAppKey());
        param.put("orderId",payOrder.getOrderNo());
        param.put("sign",sign);
        HttpHeaders headers = new HttpHeaders();
        String mediaType = MediaType.APPLICATION_JSON_UTF8_VALUE;
        headers.setContentType(MediaType.parseMediaType(mediaType));
        String url = payConfig.getServerUrl() + VmqPayCode.closeOrder;
        JSONObject result = RestUtil.request(url, HttpMethod.POST, headers, JSON.parseObject(JSON.toJSONString(param)), JSON.toJSONString(param), JSONObject.class).getBody();
        log.info("{}",result);
        if (result.getIntValue(VmqPayCode.CODE) == VmqPayCode.fail) {
            log.error("关闭订单失败:", result.getString(VmqPayCode.MSG));
            throw new OperationFailException("关闭订单失败");
        }
    }

}
