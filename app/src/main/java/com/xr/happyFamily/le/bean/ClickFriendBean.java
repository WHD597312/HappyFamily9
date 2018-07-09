/**
 * Copyright 2018 bejson.com
 */
package com.xr.happyFamily.le.bean;

import java.io.Serializable;

/**
 * Auto-generated: 2018-07-06 14:51:32
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ClickFriendBean implements Serializable{

    private int userId;
    private String username;
    private String headImgUrl;
    private String phone;
    private boolean sex;
    private int age;
    private int memSign;
    public void setMemSign(int memSign) {
        this.memSign = memSign;
    }
    public int getMemSign() {
        return memSign;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getUserId() {
        return userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }
    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPhone() {
        return phone;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }
    public boolean getSex() {
        return sex;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public int getAge() {
        return age;
    }

}