/**
 * Copyright 2018 bejson.com
 */
package com.xr.happyFamily.bean;

import java.util.Date;
import java.util.List;

/**
 * Auto-generated: 2018-06-15 16:33:11
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class OrderBean {

    private int orderId;
    private String orderNumber;
    private User user;
    private int totalAmount;
    private int paidAmount;
    private int discountAmount;
    private String postFee;
    private String receive;
    private String sendTime;
    private String paymentTime;
    private int logisticsState;
    private int invoiceTag;
    private String comment;
    private int delState;
    private String payment;
    private String paymentSeq;
    private int payState;
    private int state;
    private Date createTime;
    private Date updateTime;
    private String userId;
    private String paymentId;
    private String receiveId;
    private List<OrderDetailsList> orderDetailsList;
    private String orderCertify;
    private String startTime;
    private String endTime;
    private String startAmount;
    private String endAmount;
    private String phone;
    private String logisticCode;
    private String shipperCode;

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setPaidAmount(int paidAmount) {
        this.paidAmount = paidAmount;
    }

    public int getPaidAmount() {
        return paidAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setPostFee(String postFee) {
        this.postFee = postFee;
    }

    public String getPostFee() {
        return postFee;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public String getReceive() {
        return receive;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setLogisticsState(int logisticsState) {
        this.logisticsState = logisticsState;
    }

    public int getLogisticsState() {
        return logisticsState;
    }

    public void setInvoiceTag(int invoiceTag) {
        this.invoiceTag = invoiceTag;
    }

    public int getInvoiceTag() {
        return invoiceTag;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setDelState(int delState) {
        this.delState = delState;
    }

    public int getDelState() {
        return delState;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPayment() {
        return payment;
    }

    public void setPaymentSeq(String paymentSeq) {
        this.paymentSeq = paymentSeq;
    }

    public String getPaymentSeq() {
        return paymentSeq;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    public int getPayState() {
        return payState;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setOrderDetailsList(List<OrderDetailsList> orderDetailsList) {
        this.orderDetailsList = orderDetailsList;
    }

    public List<OrderDetailsList> getOrderDetailsList() {
        return orderDetailsList;
    }

    public void setOrderCertify(String orderCertify) {
        this.orderCertify = orderCertify;
    }

    public String getOrderCertify() {
        return orderCertify;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setStartAmount(String startAmount) {
        this.startAmount = startAmount;
    }

    public String getStartAmount() {
        return startAmount;
    }

    public void setEndAmount(String endAmount) {
        this.endAmount = endAmount;
    }

    public String getEndAmount() {
        return endAmount;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setLogisticCode(String logisticCode) {
        this.logisticCode = logisticCode;
    }

    public String getLogisticCode() {
        return logisticCode;
    }

    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode;
    }

    public String getShipperCode() {
        return shipperCode;
    }


    /**
     * Auto-generated: 2018-06-15 16:33:11
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class User {

        private int userId;
        private String username;
        private String phone;
        private String password;
        private String birthday;
        private boolean sex;
        private String oAuthType;
        private String oAuthId;
        private String headImgUrl;

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPhone() {
            return phone;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPassword() {
            return password;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setSex(boolean sex) {
            this.sex = sex;
        }

        public boolean getSex() {
            return sex;
        }

        public void setOAuthType(String oAuthType) {
            this.oAuthType = oAuthType;
        }

        public String getOAuthType() {
            return oAuthType;
        }

        public void setOAuthId(String oAuthId) {
            this.oAuthId = oAuthId;
        }

        public String getOAuthId() {
            return oAuthId;
        }

        public void setHeadImgUrl(String headImgUrl) {
            this.headImgUrl = headImgUrl;
        }

        public String getHeadImgUrl() {
            return headImgUrl;
        }

    }


    public class OrderRefund {

        private int orderId;
        private String logisticCode;
        private String shipperCode;
        private int refundState;
        private String reason;
        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }
        public int getOrderId() {
            return orderId;
        }

        public void setLogisticCode(String logisticCode) {
            this.logisticCode = logisticCode;
        }
        public String getLogisticCode() {
            return logisticCode;
        }

        public void setShipperCode(String shipperCode) {
            this.shipperCode = shipperCode;
        }
        public String getShipperCode() {
            return shipperCode;
        }

        public void setRefundState(int refundState) {
            this.refundState = refundState;
        }
        public int getRefundState() {
            return refundState;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
        public String getReason() {
            return reason;
        }

    }


    /**
     * Auto-generated: 2018-06-15 16:33:11
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class OrderDetailsList {

        private int orderDetailsId;
        private int orderId;
        private String orderNumber;
        private String goodsId;
        private String goodsName;
        private int num;
        private int priceId;
        private int detailsAmount;
        private String createTime;
        private String image;

        private int price;
        private String simpleDescribe;

        private boolean isFirst,isFinish;
        private String state,time,shipperCode,refundTime;
        int isRate,refundState;

        public void setIsRate(int isRate) {
            this.isRate = isRate;
        }

        public int getIsRate() {
            return isRate;
        }

        public void setRefundState(int refundState) {
            this.refundState = refundState;
        }

        public int getRefundState() {
            return refundState;
        }


        public void setOrderDetailsId(int orderDetailsId) {
            this.orderDetailsId = orderDetailsId;
        }

        public int getOrderDetailsId() {
            return orderDetailsId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setRefundTime(String refundTime) {
            this.refundTime = refundTime;
        }

        public String getRefundTime() {
            return refundTime;
        }


        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        public void setPriceId(int priceId) {
            this.priceId = priceId;
        }

        public int getPriceId() {
            return priceId;
        }

        public void setDetailsAmount(int detailsAmount) {
            this.detailsAmount = detailsAmount;
        }

        public int getDetailsAmount() {
            return detailsAmount;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getPrice() {
            return price;
        }

        public void setSimpleDescribe(String simpleDescribe) {
            this.simpleDescribe = simpleDescribe;
        }

        public String getSimpleDescribe() {
            return simpleDescribe;
        }


        public void setFirst(Boolean isFirst) {
            this.isFirst = isFirst;
        }

        public Boolean getFinish() {
            return isFinish;
        }

        public void setFinish(Boolean isFinish) {
            this.isFinish = isFinish;
        }

        public Boolean getFirst() {
            return isFirst;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getImage() {
            return image;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getState() {
            return state;
        }


        public void setTime(String time) {
            this.time = time;
        }

        public String getTime() {
            return time;
        }

        public void setShipperCode(String shipperCode) {
            this.shipperCode = shipperCode;
        }

        public String getShipperCode() {
            return shipperCode;
        }

        public void setLogisticCode(String logisticCode) {
            this.time = logisticCode;
        }

        public String getLogisticCode() {
            return logisticCode;
        }

    }


    /**
     * Copyright 2018 bejson.com
     */


    /**
     * Auto-generated: 2018-06-15 16:33:11
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */

}
