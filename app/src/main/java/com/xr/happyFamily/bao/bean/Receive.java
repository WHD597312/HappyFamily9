package com.xr.happyFamily.bao.bean;

import java.util.Date;


/**
 * 用户收货地址
 *
 */
public class Receive {
    private int receiveId;
    private int userId;
    private String contact;
    private String tel;
    private String receiveProvince;
    private String receiveCity;
    private String receiveCounty;
    private String receiveAddress;
    private int isDefault;
    private int delState;
    private long createTime;
    private long updateTime;
    public void setReceiveId(int receiveId) {
        this.receiveId = receiveId;
    }
    public int getReceiveId() {
        return receiveId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getUserId() {
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

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }
    public int getIsDefault() {
        return isDefault;
    }

    public void setDelState(int delState) {
        this.delState = delState;
    }
    public int getDelState() {
        return delState;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public long getCreateTime() {
        return createTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
    public long getUpdateTime() {
        return updateTime;
    }
}