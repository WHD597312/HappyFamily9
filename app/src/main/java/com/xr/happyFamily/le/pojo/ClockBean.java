/**
 * Copyright 2018 bejson.com
 */
package com.xr.happyFamily.le.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2018-07-09 9:18:38
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
public class ClockBean implements Serializable{

    @Id(autoincrement = true)
    private Long id;
    private int clockId;
    private int clockHour;
    private int clockMinute;
    private int sumMinute;
    private String clockDay;
    private String flag;
    private String music;
    private int switchs;
    private int clockCreater;
    private int clockType;

    @Generated(hash = 1188591895)
    public ClockBean(Long id, int clockId, int clockHour, int clockMinute,
            int sumMinute, String clockDay, String flag, String music, int switchs,
            int clockCreater, int clockType) {
        this.id = id;
        this.clockId = clockId;
        this.clockHour = clockHour;
        this.clockMinute = clockMinute;
        this.sumMinute = sumMinute;
        this.clockDay = clockDay;
        this.flag = flag;
        this.music = music;
        this.switchs = switchs;
        this.clockCreater = clockCreater;
        this.clockType = clockType;
    }

    @Generated(hash = 513947775)
    public ClockBean() {
    }

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

    public void setSumMinute(int sumMinute) {
        this.sumMinute = sumMinute;
    }

    public int getSumMinute() {
        return sumMinute;
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Copyright 2018 bejson.com
     */

    /**
     * Auto-generated: 2018-07-09 9:18:38
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