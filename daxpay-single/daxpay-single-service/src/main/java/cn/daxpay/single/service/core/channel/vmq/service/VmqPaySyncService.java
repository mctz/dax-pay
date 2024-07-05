package cn.daxpay.single.service.core.channel.vmq.service;

import cn.daxpay.single.core.code.PaySyncStatusEnum;
import cn.daxpay.single.core.code.RefundSyncStatusEnum;
import cn.daxpay.single.core.code.TransferStatusEnum;
import cn.daxpay.single.core.result.sync.TransferSyncResult;
import cn.daxpay.single.service.code.VmqPayCode;
import cn.daxpay.single.service.core.channel.vmq.entity.VmqPayConfig;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.daxpay.single.service.core.order.transfer.entity.TransferOrder;
import cn.daxpay.single.service.core.payment.sync.result.PayRemoteSyncResult;
import cn.daxpay.single.service.core.payment.sync.result.RefundRemoteSyncResult;
import cn.daxpay.single.service.util.RestUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * V免签同步
 *
 * @author xxm
 * @since 2021/5/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VmqPaySyncService {

    private final VmqPayConfigService vmqPayConfigService;

    /**
     * 与V免签网关同步状态, 退款状态有
     * 1 远程支付成功
     * -1 支付失败
     */
    public PayRemoteSyncResult syncPayStatus(PayOrder payOrder, VmqPayConfig payConfig){
        PayRemoteSyncResult syncResult = new PayRemoteSyncResult().setSyncStatus(PaySyncStatusEnum.FAIL);
        // 查询
        Map<String,Object> param = new HashMap<>();
        param.put("orderId",payOrder.getOrderNo());
        HttpHeaders headers = new HttpHeaders();
        String mediaType = MediaType.APPLICATION_JSON_UTF8_VALUE;
        headers.setContentType(MediaType.parseMediaType(mediaType));
        String url = payConfig.getServerUrl() + VmqPayCode.getOrder;
        JSONObject result = RestUtil.request(url, HttpMethod.POST, headers, JSON.parseObject(JSON.toJSONString(param)), JSON.toJSONString(param), JSONObject.class).getBody();
        // 设置网关订单号
        syncResult.setOutOrderNo(payOrder.getOrderNo());
        syncResult.setSyncInfo(JSONUtil.toJsonStr(result.getJSONObject(VmqPayCode.DATA)));
        if (result.getIntValue(VmqPayCode.CODE) == VmqPayCode.success) {
            // 订单状态：-1|订单过期 0|等待支付 1|完成 2|支付完成但通知失败
            if (result.getJSONObject(VmqPayCode.DATA).getIntValue("state") > 0) {
                syncResult.setSyncStatus(PaySyncStatusEnum.SUCCESS);
            } else if (result.getJSONObject(VmqPayCode.DATA).getIntValue("state") == 0) {
                syncResult.setSyncStatus(PaySyncStatusEnum.PROGRESS);
            } else if (result.getJSONObject(VmqPayCode.DATA).getIntValue("state") == -1) {
                syncResult.setSyncStatus(PaySyncStatusEnum.TIMEOUT);
            }
            return syncResult;
        }
        syncResult.setErrorMsg(result.getString(VmqPayCode.MSG));
        return syncResult.setSyncStatus(PaySyncStatusEnum.FAIL);
    }

    /**
     * 退款同步查询
     * 注意: V免签退款没有网关订单号, 网关订单号是支付单的
     */
    public RefundRemoteSyncResult syncRefundStatus(RefundOrder refundOrder){
        RefundRemoteSyncResult syncResult = new RefundRemoteSyncResult().setSyncStatus(RefundSyncStatusEnum.FAIL);
        syncResult.setErrorMsg("不支持该能力");
        return syncResult;
    }

    /**
     * 转账同步
     */
    @SneakyThrows
    public TransferSyncResult syncTransferStatus(TransferOrder transferOrder){
        TransferSyncResult result = new TransferSyncResult().setStatus(TransferStatusEnum.FAIL.getCode());
        result.setMsg("不支持该能力");
        return result;
    }

}
