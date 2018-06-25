/**
 * Copyright 2018 bejson.com
 */
package com.xr.happyFamily.bean;;
import java.util.List;

/**
 * Auto-generated: 2018-06-12 11:10:46
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class AddressBean {

    private String returnCode;
    private String returnMsg;
    private List<ReturnData> returnData;
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }
    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnData(List<ReturnData> returnData) {
        this.returnData = returnData;
    }
    public List<ReturnData> getReturnData() {
        return returnData;
    }

    public class ReturnData {

        private int isDefault;
        private String receiveAddress;
        private long createTime;
        private String receiveCity;
        private String contact;
        private int delState;
        private String tel;
        private long updateTime;
        private int userId;
        private String receiveProvince;
        private String receiveCounty;
        private int receiveId;
        public void setIsDefault(int isDefault) {
            this.isDefault = isDefault;
        }
        public int getIsDefault() {
            return isDefault;
        }

        public void setReceiveAddress(String receiveAddress) {
            this.receiveAddress = receiveAddress;
        }
        public String getReceiveAddress() {
            return receiveAddress;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
        public long getCreateTime() {
            return createTime;
        }

        public void setReceiveCity(String receiveCity) {
            this.receiveCity = receiveCity;
        }
        public String getReceiveCity() {
            return receiveCity;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }
        public String getContact() {
            return contact;
        }

        public void setDelState(int delState) {
            this.delState = delState;
        }
        public int getDelState() {
            return delState;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
        public String getTel() {
            return tel;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }
        public long getUpdateTime() {
            return updateTime;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
        public int getUserId() {
            return userId;
        }

        public void setReceiveProvince(String receiveProvince) {
            this.receiveProvince = receiveProvince;
        }
        public String getReceiveProvince() {
            return receiveProvince;
        }

        public void setReceiveCounty(String receiveCounty) {
            this.receiveCounty = receiveCounty;
        }
        public String getReceiveCounty() {
            return receiveCounty;
        }

        public void setReceiveId(int receiveId) {
            this.receiveId = receiveId;
        }
        public int getReceiveId() {
            return receiveId;
        }

    }
}