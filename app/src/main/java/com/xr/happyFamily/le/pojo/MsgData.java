package com.xr.happyFamily.le.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MsgData {

    @Id(autoincrement = true)
    private Long id;
    private String userName;
    private long createTime;
    private int state;
    public int getState() {
        return this.state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public long getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 246180949)
    public MsgData(Long id, String userName, long createTime, int state) {
        this.id = id;
        this.userName = userName;
        this.createTime = createTime;
        this.state = state;
    }
    @Generated(hash = 729412307)
    public MsgData() {
    }
   
}
