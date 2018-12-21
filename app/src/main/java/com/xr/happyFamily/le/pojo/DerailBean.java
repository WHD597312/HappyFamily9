package com.xr.happyFamily.le.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DerailBean {

    @Id(autoincrement = true)
    Long id;
    String derailName;
    long createTime;
    int derailId;
    public int getDerailId() {
        return this.derailId;
    }
    public void setDerailId(int derailId) {
        this.derailId = derailId;
    }
    public long getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public String getDerailName() {
        return this.derailName;
    }
    public void setDerailName(String derailName) {
        this.derailName = derailName;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 2074773086)
    public DerailBean(Long id, String derailName, long createTime, int derailId) {
        this.id = id;
        this.derailName = derailName;
        this.createTime = createTime;
        this.derailId = derailId;
    }
    @Generated(hash = 222470807)
    public DerailBean() {
    }

}
