package com.xr.happyFamily.bao.bean;

public class OAuthUser {
    private long userId;

    private String oAuthType;

    private String oAuthId;

    private String headImgUrl;    //用户头像

    private String username;


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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "OAuthUser{" +
                "userId=" + userId +
                ", oAuthType='" + oAuthType + '\'' +
                ", oAuthId='" + oAuthId + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
