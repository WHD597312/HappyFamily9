package com.xr.happyFamily.bao.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class ExpressInfoVo {
    /**
     * 快递代码
     */
    private String orderCode;

    private String shipperCode;

    private boolean success;

    private String reason;

    private String remark;

    private String describe;

    /**
     * 运单号
     */
    private String logisticCode;
    /**
     * 状态
     */
    private String state;



    /**
     * 物流信息
     */
    private List<ExpressTrace> traces;
    @JSONField(name = "Traces")
    public List<ExpressTrace> getTraces() {
        return traces;
    }

    public void setTraces(List<ExpressTrace> traces) {
        this.traces = traces;
    }
    @JSONField(name = "OrderCode")
    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
    @JSONField(name = "ShipperCode")
    public String getShipperCode() {
        return shipperCode;
    }

    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode;
    }
    @JSONField(name = "Success")
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    @JSONField(name = "Reason")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    @JSONField(name = "Remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getLogisticCode() {
        return logisticCode;
    }

    public void setLogisticCode(String logisticCode) {
        this.logisticCode = logisticCode;
    }
    @JSONField(name = "State")

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
