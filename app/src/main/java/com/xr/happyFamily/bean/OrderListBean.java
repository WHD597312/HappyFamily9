
package com.xr.happyFamily.bean;


import java.util.List;

/**
 * Auto-generated: 2018-06-15 16:33:11
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class OrderListBean {
/**
 * Copyright 2018 bejson.com
 */

    /**
     * Auto-generated: 2018-06-19 9:54:20
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */


        private int totalPage;
        private List<myList> list;
        private int totalCount;

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setList(List<myList> list) {
            this.list = list;
        }

        public List<myList> getList() {
            return list;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getTotalCount() {
            return totalCount;
        }
    public class myList {
        private int totalAmount;
        private String orderNumber;
        private int orderId;
        private String createTime;
        private int invoiceTag;
        private int payState;
        private int paidAmount;
        private User user;
        private int logisticsState;
        private int state;

        public void setState(int state) {
            this.totalAmount = state;
        }

        public int getState() {
            return state;
        }

        public void setTotalAmount(int totalAmount) {
            this.totalAmount = totalAmount;
        }

        public int getTotalAmount() {
            return totalAmount;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setInvoiceTag(int invoiceTag) {
            this.invoiceTag = invoiceTag;
        }

        public int getInvoiceTag() {
            return invoiceTag;
        }

        public void setPayState(int payState) {
            this.payState = payState;
        }

        public int getPayState() {
            return payState;
        }

        public void setPaidAmount(int paidAmount) {
            this.paidAmount = paidAmount;
        }

        public int getPaidAmount() {
            return paidAmount;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }

        public void setLogisticsState(int logisticsState) {
            this.logisticsState = logisticsState;
        }

        public int getLogisticsState() {
            return logisticsState;
        }

        public class User {

            private String phone;
            private int userId;

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getPhone() {
                return phone;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getUserId() {
                return userId;
            }
        }
    }
}
