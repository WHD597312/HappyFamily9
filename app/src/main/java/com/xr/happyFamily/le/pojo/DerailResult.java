package com.xr.happyFamily.le.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DerailResult {

    @Id(autoincrement = true)
    Long id;
    String derailName;
    int derailId;
    long createTime;
    int derailResult;
    int derailPo;//绑定这身份
    public int getDerailPo() {
        return this.derailPo;
    }
    public void setDerailPo(int derailPo) {
        this.derailPo = derailPo;
    }
    public int getDerailResult() {
        return this.derailResult;
    }
    public void setDerailResult(int derailResult) {
        this.derailResult = derailResult;
    }
    public long getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public int getDerailId() {
        return this.derailId;
    }
    public void setDerailId(int derailId) {
        this.derailId = derailId;
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
    @Generated(hash = 413251686)
    public DerailResult(Long id, String derailName, int derailId, long createTime,
            int derailResult, int derailPo) {
        this.id = id;
        this.derailName = derailName;
        this.derailId = derailId;
        this.createTime = createTime;
        this.derailResult = derailResult;
        this.derailPo = derailPo;
    }
    @Generated(hash = 1893008881)
    public DerailResult() {
    }
   
    

}
