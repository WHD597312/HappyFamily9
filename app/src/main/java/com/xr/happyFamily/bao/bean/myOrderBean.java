/**
 * Copyright 2018 bejson.com
 */
package com.xr.happyFamily.bao.bean;
import java.util.Date;
import java.util.List;

/**
 * Auto-generated: 2018-06-20 11:8:28
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class myOrderBean {

    private int orderId;
    private String orderNumber;
    private User user;
    private int totalAmount;
    private int paidAmount;
    private String discountAmount;
    private String postFee;
    private Receive receive;
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

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }
    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setPostFee(String postFee) {
        this.postFee = postFee;
    }
    public String getPostFee() {
        return postFee;
    }

    public void setReceive(Receive receive) {
        this.receive = receive;
    }
    public Receive getReceive() {
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
 * Copyright 2018 bejson.com
 */


    /**
     * Auto-generated: 2018-06-20 11:8:28
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
    /**
     * Copyright 2018 bejson.com
     */

    /**
     * Auto-generated: 2018-06-20 11:8:28
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class Receive {

        private String receiveId;
        private String userId;
        private String contact;
        private String tel;
        private String receiveProvince;
        private String receiveCity;
        private String receiveCounty;
        private String receiveAddress;
        private String isDefault;
        private String delState;
        private String createTime;
        private String updateTime;
        public void setReceiveId(String receiveId) {
            this.receiveId = receiveId;
        }
        public String getReceiveId() {
            return receiveId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
        public String getUserId() {
            return userId;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }
        public String getContact() {
            return contact;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
        public String getTel() {
            return tel;
        }

        public void setReceiveProvince(String receiveProvince) {
            this.receiveProvince = receiveProvince;
        }
        public String getReceiveProvince() {
            return receiveProvince;
        }

        public void setReceiveCity(String receiveCity) {
            this.receiveCity = receiveCity;
        }
        public String getReceiveCity() {
            return receiveCity;
        }

        public void setReceiveCounty(String receiveCounty) {
            this.receiveCounty = receiveCounty;
        }
        public String getReceiveCounty() {
            return receiveCounty;
        }

        public void setReceiveAddress(String receiveAddress) {
            this.receiveAddress = receiveAddress;
        }
        public String getReceiveAddress() {
            return receiveAddress;
        }

        public void setIsDefault(String isDefault) {
            this.isDefault = isDefault;
        }
        public String getIsDefault() {
            return isDefault;
        }

        public void setDelState(String delState) {
            this.delState = delState;
        }
        public String getDelState() {
            return delState;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
        public String getCreateTime() {
            return createTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
        public String getUpdateTime() {
            return updateTime;
        }

    }

    /**
     * Copyright 2018 bejson.com
     */

    /**
     * Auto-generated: 2018-06-20 11:8:28
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class OrderDetailsList {

        private int orderDetailsId;
        private String orderId;
        private String orderNumber;
        private int goodsId;
        private String goodsName;
        private int num;
        private int priceId;
        private int detailsAmount;
        private String createTime;
        private String image;
        private String simpleDescribe;
        private int price;
        public void setOrderDetailsId(int orderDetailsId) {
            this.orderDetailsId = orderDetailsId;
        }
        public int getOrderDetailsId() {
            return orderDetailsId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
        public String getOrderId() {
            return orderId;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }
        public String getOrderNumber() {
            return orderNumber;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }
        public int getGoodsId() {
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

        public void setImage(String image) {
            this.image = image;
        }
        public String getImage() {
            return image;
        }

        public void setSimpleDescribe(String simpleDescribe) {
            this.simpleDescribe = simpleDescribe;
        }
        public String getSimpleDescribe() {
            return simpleDescribe;
        }

        public void setPrice(int price) {
            this.price = price;
        }
        public int getPrice() {
            return price;
        }

    }
}