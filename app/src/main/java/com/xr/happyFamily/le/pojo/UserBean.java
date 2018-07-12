package com.xr.happyFamily.le.pojo;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class UserBean {
    @Id(autoincrement = true)
    Long Id;
    String userId;
    String birthday;
    public String getBirthday() {
        return this.birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Long getId() {
        return this.Id;
    }
    public void setId(Long Id) {
        this.Id = Id;
    }
    @Generated(hash = 1141933584)
    public UserBean(Long Id, String userId, String birthday) {
        this.Id = Id;
        this.userId = userId;
        this.birthday = birthday;
    }
    @Generated(hash = 1203313951)
    public UserBean() {
    }
}
