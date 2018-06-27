/**
 * Copyright 2018 bejson.com
 */
package com.xr.happyFamily.bean;
import java.util.List;

/**
 * Auto-generated: 2018-06-26 14:23:5
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class PostFreeBean {

    private String OrderCode;
    private List<ExpressList> ExpressList;
    public void setOrderCode(String OrderCode) {
        this.OrderCode = OrderCode;
    }
    public String getOrderCode() {
        return OrderCode;
    }

    public void setExpressList(List<ExpressList> ExpressList) {
        this.ExpressList = ExpressList;
    }
    public List<ExpressList> getExpressList() {
        return ExpressList;
    }



    public class ExpressList {

        private String IsSupport;
        private double EstimatedDeliveryTime;
        private String ExpCode;
        private double Fee;
        private String ExpName;
        private int RecommendReason;
        private String LineName;
        private String Remark;
        public void setIsSupport(String IsSupport) {
            this.IsSupport = IsSupport;
        }
        public String getIsSupport() {
            return IsSupport;
        }

        public void setEstimatedDeliveryTime(double EstimatedDeliveryTime) {
            this.EstimatedDeliveryTime = EstimatedDeliveryTime;
        }
        public double getEstimatedDeliveryTime() {
            return EstimatedDeliveryTime;
        }

        public void setExpCode(String ExpCode) {
            this.ExpCode = ExpCode;
        }
        public String getExpCode() {
            return ExpCode;
        }

        public void setFee(double Fee) {
            this.Fee = Fee;
        }
        public double getFee() {
            return Fee;
        }

        public void setExpName(String ExpName) {
            this.ExpName = ExpName;
        }
        public String getExpName() {
            return ExpName;
        }

        public void setRecommendReason(int RecommendReason) {
            this.RecommendReason = RecommendReason;
        }
        public int getRecommendReason() {
            return RecommendReason;
        }

        public void setLineName(String LineName) {
            this.LineName = LineName;
        }
        public String getLineName() {
            return LineName;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }
        public String getRemark() {
            return Remark;
        }

    }

}