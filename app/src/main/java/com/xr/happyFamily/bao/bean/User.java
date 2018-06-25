package com.xr.happyFamily.bao.bean;



import java.security.Timestamp;
//@ApiModel(value="User", description="用户对象")
public class User {
//    @ApiModelProperty(value="用户Id",name="userId",example="10001")
    private int userId;
//    @ApiModelProperty(value="用户名",name="username",example="张三")
    private String username;
//    @ApiModelProperty(value="用户手机号",name="phone",example="13780645923")
    private String phone;
//    @ApiModelProperty(value="用户密码",name="password",example="123456")
    private String password;
//    @ApiModelProperty(value="用户生日",name="birthday",example="1526881476000")
    private Timestamp birthday;
//    @ApiModelProperty(value="用户性别",name="sex",example="1")
    private boolean sex;
//    @ApiModelProperty(value="第三方登录类型（微信、qq）",name="oAuthType",example="weixin")
    private String oAuthType;
//    @ApiModelProperty(value="第三方Id",name="oAuthType",example="id21323")
    private String oAuthId;
//    @ApiModelProperty(value="用户头像",name="headImgUrl",example="https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-395252.jpg")
    private String headImgUrl;

    public User() {
    }

    public User(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getoAuthType() {
        return oAuthType;
    }

    public void setoAuthType(String oAuthType) {
        this.oAuthType = oAuthType;
    }

    public String getoAuthId() {
        return oAuthId;
    }

    public void setoAuthId(String oAuthId) {
        this.oAuthId = oAuthId;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

}
