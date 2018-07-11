package com.xr.happyFamily.le.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class FriendData {
    @Id(autoincrement = true)
    private Long id;
    private int frdId;
    private int senderId;
    private String senderName;
    private String senderSex;
    private int senderAge;
    private String senderRemark;
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setFrdId(int frdId) {
        this.frdId = frdId;
    }
    public int getFrdId() {
        return frdId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }
    public int getSenderId() {
        return senderId;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    public String getSenderName() {
        return senderName;
    }

    public void setSenderSex(String senderSex) {
        this.senderSex = senderSex;
    }
    public String getSenderSex() {
        return senderSex;
    }

    public void setSenderAge(int senderAge) {
        this.senderAge = senderAge;
    }
    public int getSenderAge() {
        return senderAge;
    }

    public void setSenderRemark(String senderRemark) {
        this.senderRemark = senderRemark;
    }
    public String getSenderRemark() {
        return senderRemark;
    }
    @Generated(hash = 291223402)
    public FriendData(Long id, int frdId, int senderId, String senderName,
            String senderSex, int senderAge, String senderRemark) {
        this.id = id;
        this.frdId = frdId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderSex = senderSex;
        this.senderAge = senderAge;
        this.senderRemark = senderRemark;
    }
    @Generated(hash = 951877157)
    public FriendData() {
    }

}
