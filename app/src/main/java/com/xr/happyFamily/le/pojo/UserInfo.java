/**
 * Copyright 2018 bejson.com
 */
package com.xr.happyFamily.le.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Auto-generated: 2018-07-09 9:18:38
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
public class UserInfo {


    @Id(autoincrement = true)
    private Long id;
    private int clockId;
    private int userId;
    private String username;
    private String headImgUrl;
    private String phone;
    private boolean sex;
    private int age;
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public boolean getSex() {
        return this.sex;
    }
    public void setSex(boolean sex) {
        this.sex = sex;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getHeadImgUrl() {
        return this.headImgUrl;
    }
    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getUserId() {
        return this.userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getClockId() {
        return this.clockId;
    }
    public void setClockId(int clockId) {
        this.clockId = clockId;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 2064066128)
    public UserInfo(Long id, int clockId, int userId, String username,
            String headImgUrl, String phone, boolean sex, int age) {
        this.id = id;
        this.clockId = clockId;
        this.userId = userId;
        this.username = username;
        this.headImgUrl = headImgUrl;
        this.phone = phone;
        this.sex = sex;
        this.age = age;
    }
    @Generated(hash = 1279772520)
    public UserInfo() {
    }



}