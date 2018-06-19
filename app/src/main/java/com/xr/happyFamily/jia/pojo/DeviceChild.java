package com.xr.happyFamily.jia.pojo;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

@Entity
public class DeviceChild implements Serializable{
    @Id(autoincrement = false)
    private Long id;
    int type;/**设备类型*/
    String macAddress;/**mac地址*/
    int timerMoudle=-1;/**定时器模式*/
    int mcuVersion=-1;/**MCU版本*/
    int waramerSetTemp=-1;/**取暖器设定温度*/
    int warmerCurTemp=-1;/**取暖器当前温度*/
    int warmerSampleData=-1;/**取暖器采样数据*/
    int warmerRatePower=-1;/**取暖器额定总功率*/
    int warmerCurRunRoatePower=-1;/**取暖器当前运行功率*/
    int warmerRunState=-1;/**机器当前运行状态*/
    int deviceState=-1;/**开关机状态 0表示关机，1表示开机*/
    String rateState=null;/**功率状态  11: 3档 10: 2档  01: 1档*/
    int lockState=-1;/** 屏幕是否锁定 1：锁定 0：未锁定*/
    int screenState=-1;/**屏保是否开启 1：开启 0：未开启 */
    int curRunState2=-1;/**机器当前运行状态2  (保留)*/
    int curRunState3=-1;/**机器当前运行状态2  (保留)*/
    int timerHour=-1;/**定时时间 小时*/
    int timerMin=-1;/**定时时间 分*/
    int checkCode=-1;/**校验码*/
    int endCode=-1;/**结束码*/
    public int getEndCode() {
        return this.endCode;
    }
    public void setEndCode(int endCode) {
        this.endCode = endCode;
    }
    public int getCheckCode() {
        return this.checkCode;
    }
    public void setCheckCode(int checkCode) {
        this.checkCode = checkCode;
    }
    public int getTimerMin() {
        return this.timerMin;
    }
    public void setTimerMin(int timerMin) {
        this.timerMin = timerMin;
    }
    public int getTimerHour() {
        return this.timerHour;
    }
    public void setTimerHour(int timerHour) {
        this.timerHour = timerHour;
    }
    public int getCurRunState3() {
        return this.curRunState3;
    }
    public void setCurRunState3(int curRunState3) {
        this.curRunState3 = curRunState3;
    }
    public int getCurRunState2() {
        return this.curRunState2;
    }
    public void setCurRunState2(int curRunState2) {
        this.curRunState2 = curRunState2;
    }
    public int getScreenState() {
        return this.screenState;
    }
    public void setScreenState(int screenState) {
        this.screenState = screenState;
    }
    public int getLockState() {
        return this.lockState;
    }
    public void setLockState(int lockState) {
        this.lockState = lockState;
    }
    public String getRateState() {
        return this.rateState;
    }
    public void setRateState(String rateState) {
        this.rateState = rateState;
    }
    public int getDeviceState() {
        return this.deviceState;
    }
    public void setDeviceState(int deviceState) {
        this.deviceState = deviceState;
    }
    public int getWarmerRunState() {
        return this.warmerRunState;
    }
    public void setWarmerRunState(int warmerRunState) {
        this.warmerRunState = warmerRunState;
    }
    public int getWarmerCurRunRoatePower() {
        return this.warmerCurRunRoatePower;
    }
    public void setWarmerCurRunRoatePower(int warmerCurRunRoatePower) {
        this.warmerCurRunRoatePower = warmerCurRunRoatePower;
    }
    public int getWarmerRatePower() {
        return this.warmerRatePower;
    }
    public void setWarmerRatePower(int warmerRatePower) {
        this.warmerRatePower = warmerRatePower;
    }
    public int getWarmerSampleData() {
        return this.warmerSampleData;
    }
    public void setWarmerSampleData(int warmerSampleData) {
        this.warmerSampleData = warmerSampleData;
    }
    public int getWarmerCurTemp() {
        return this.warmerCurTemp;
    }
    public void setWarmerCurTemp(int warmerCurTemp) {
        this.warmerCurTemp = warmerCurTemp;
    }
    public int getWaramerSetTemp() {
        return this.waramerSetTemp;
    }
    public void setWaramerSetTemp(int waramerSetTemp) {
        this.waramerSetTemp = waramerSetTemp;
    }
    public int getMcuVersion() {
        return this.mcuVersion;
    }
    public void setMcuVersion(int mcuVersion) {
        this.mcuVersion = mcuVersion;
    }
    public int getTimerMoudle() {
        return this.timerMoudle;
    }
    public void setTimerMoudle(int timerMoudle) {
        this.timerMoudle = timerMoudle;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getMacAddress() {
        return this.macAddress;
    }
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
    @Generated(hash = 1185543534)
    public DeviceChild(Long id, int type, String macAddress, int timerMoudle,
            int mcuVersion, int waramerSetTemp, int warmerCurTemp,
            int warmerSampleData, int warmerRatePower, int warmerCurRunRoatePower,
            int warmerRunState, int deviceState, String rateState, int lockState,
            int screenState, int curRunState2, int curRunState3, int timerHour,
            int timerMin, int checkCode, int endCode) {
        this.id = id;
        this.type = type;
        this.macAddress = macAddress;
        this.timerMoudle = timerMoudle;
        this.mcuVersion = mcuVersion;
        this.waramerSetTemp = waramerSetTemp;
        this.warmerCurTemp = warmerCurTemp;
        this.warmerSampleData = warmerSampleData;
        this.warmerRatePower = warmerRatePower;
        this.warmerCurRunRoatePower = warmerCurRunRoatePower;
        this.warmerRunState = warmerRunState;
        this.deviceState = deviceState;
        this.rateState = rateState;
        this.lockState = lockState;
        this.screenState = screenState;
        this.curRunState2 = curRunState2;
        this.curRunState3 = curRunState3;
        this.timerHour = timerHour;
        this.timerMin = timerMin;
        this.checkCode = checkCode;
        this.endCode = endCode;
    }
    @Generated(hash = 1973447423)
    public DeviceChild() {
    }

}
