/**
 * Copyright 2018 bejson.com
 */
package com.xr.happyFamily.le.bean;
import java.util.List;

/**
 * Auto-generated: 2018-07-09 10:40:2
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class MyClockBean {

    private int clockId;
    private int clockHour;
    private int clockMinute;
    private String clockDay;
    private String flag;
    private String music;
    private int switchs;
    private List<UserInfos> userInfos;
    private int clockCreater;
    private int clockType;
    public void setClockId(int clockId) {
        this.clockId = clockId;
    }
    public int getClockId() {
        return clockId;
    }

    public void setClockHour(int clockHour) {
        this.clockHour = clockHour;
    }
    public int getClockHour() {
        return clockHour;
    }

    public void setClockMinute(int clockMinute) {
        this.clockMinute = clockMinute;
    }
    public int getClockMinute() {
        return clockMinute;
    }

    public void setClockDay(String clockDay) {
        this.clockDay = clockDay;
    }
    public String getClockDay() {
        return clockDay;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
    public String getFlag() {
        return flag;
    }

    public void setMusic(String music) {
        this.music = music;
    }
    public String getMusic() {
        return music;
    }

    public void setSwitchs(int switchs) {
        this.switchs = switchs;
    }
    public int getSwitchs() {
        return switchs;
    }

    public void setUserInfos(List<UserInfos> userInfos) {
        this.userInfos = userInfos;
    }
    public List<UserInfos> getUserInfos() {
        return userInfos;
    }

    public void setClockCreater(int clockCreater) {
        this.clockCreater = clockCreater;
    }
    public int getClockCreater() {
        return clockCreater;
    }

    public void setClockType(int clockType) {
        this.clockType = clockType;
    }
    public int getClockType() {
        return clockType;
    }



    /**
     * Auto-generated: 2018-07-09 10:40:2
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class UserInfos {

        private int userId;
        private String username;
        private String headImgUrl;
        private String phone;
        private boolean sex;
        private int age;
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
}