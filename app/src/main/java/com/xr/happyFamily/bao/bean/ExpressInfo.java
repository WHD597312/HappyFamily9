package com.xr.happyFamily.bao.bean;


/**
 * 项目名称: boot-module-demo
 * 创建时间: 2017/11/17 9:26
 * 作者: xiangwb
 * 说明: 快递信息
 */
//@ApiModel
public class ExpressInfo {
//    @ApiModelProperty(value = "快递公司编号", example = "HTKY")
    private String shipperCode;
//    @ApiModelProperty(value = "物流单号", example = "211386517825")
    private String logisticCode;
//    @ApiModelProperty(value = "快递名称", example = "百世汇通")
//    private String expressName;
//    @ApiModelProperty(value = "订单编号", example = "201703140000000018")
//    private String orderNo;


    public String getShipperCode() {
        return shipperCode;
    }

    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode;
    }

    public String getLogisticCode() {
        return logisticCode;
    }

    public void setLogisticCode(String logisticCode) {
        this.logisticCode = logisticCode;
    }
}
