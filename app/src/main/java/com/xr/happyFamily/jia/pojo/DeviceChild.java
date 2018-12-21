package com.xr.happyFamily.jia.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

@Entity
public class DeviceChild implements Serializable{
    @Id(autoincrement = true)
    private Long id;
    private boolean online;/**设备离线状态*/
    long houseId;/**设备的房子Id*/
    long roomId;/**设备的房间Id*/
    int deviceUsedCount;/**设备被用户使用的次数*/
    String roomName;/**房间名称*/
    String common;/**常用设备*/
    String share;/**分享设备*/
    int type;/**设备类型*/
    int busModel;/**商业模式*/
    String macAddress;/**mac地址*/
    String name;/**设备名称*/
    int timerMoudle=0;/**定时器模式*/
    String mcuVersion;/**MCU版本*/
    String wifiVersion;/**wifi版本*/
    int waramerSetTemp=0;/**取暖器设定温度*/
    int warmerCurTemp=0;/**取暖器当前温度*/
    int warmerSampleData=0;/**取暖器采样数据*/
    int warmerRatePower=0;/**取暖器额定总功率*/
    int warmerCurRunRoatePower=0;/**取暖器当前运行功率*/
    int warmerRunState=0;/**机器当前运行状态*/
    int deviceState=0;/**开关机状态 0表示关机，1表示开机*/
    int warmerFall;/**机器倾斜*/
    String rateState=null;/**功率状态  11: 3档 10: 2档  01: 1档*/
    int lock;/**机器锁定状态*/
    int week;
    int timerOpenOneHour;
    int timerOpenOneMin;
    int timerCloseOneHour;
    int timerCloseOneMin;
    int timerOpenTwoHour;
    int timerOpenTwoMin;
    int timerCloseTwoHour;
    int timerCloseTwoMin;
    int timerOpenThrHour;
    int timerOpenThrMin;
    int timerCloseThrHour;
    int timerCloseThrMin;
    int timerOpenForHour;
    int timerOpenForMin;
    int timerCloseForHour;
    int timerCloseForMin;
    int timerOne;
    int timerTwo;
    int timerThr;
    int timerFor;
    int lockState=0;/** 屏幕是否锁定 1：锁定 0：未锁定*/
    int screenState=0;/**屏保是否开启 1：开启 0：未开启 */
    int curRunState2=0;/**机器当前运行状态2  (保留)*/
    int curRunState3=0;/**机器当前运行状态2  (保留)*/
    int timerHour=0;/**定时时间 小时*/
    int timerMin=0;/**定时时间 分*/
    int checkCode=0;/**校验码*/
    int endCode=0;/**结束码*/
    int userId=0;/**用户Id*/
    int img;/**设备标志图片*/
    int deviceId;/**设备Id*/
    int linked;/**是否联动*/
    int linkedSensorId;/**与智能终端联动的设备*/
    long shareId;/**分享设备*/
    int sensorState;/**传感器状态*/
    int sensorSimpleTemp;/**传感器采样温度*/
    int sensorSimpleHum;/**传感器采样湿度*/
    int sorsorPm;/**PM2.5粉尘传感器数据*/
    int sensorOx;/**氧浓度传感器数据*/
    int sensorHcho;/**甲醛数据*/

    int socketPowerHigh;/**插座高功率参数*/
    int socketPowerLow;/**插座低功率参数*/
    int socketPower;/**插座功率*/
    int socketTemp;/**插座温度*/
    int socketState;/**插座当前状态*/
    int socketTimer;/**插座定时模式*/
    int socketTimerHour;/**插座定时模式的时*/
    int socketTimerMin;/**插座定时模式的分*/
    int socketCurrent;/**插座当前电流值*/
    int socketVal;/**插座当前电压值*/
    int socketPowerConsume;/**插座当前耗电量总度数*/



    //jjjjjjj
    int timerSwitch=0;/**定时器开关*/
    int waterLevel=0; //水位量
    String windLevel; //风速等级
    int equipRatedPowerHigh;/**设备额定高功率参数*/
    int equipRatedPowerLow;/**设备额定低功率参数*/
    int equipCurdPowerHigh;/**设备当前高功率参数*/
    int equipCurdPowerLow;/**设备当前低功率参数*/
    int faultCode;/**空调故障代码*/

    String purifierState;//空气净化器当前状态* 00自动 01睡眠


    int dehumSetTemp;/**除湿机设定温度*/
    int dehumSetHum;/**除湿机设定湿度*/
    int dehumInnerTemp;//除湿机内盘管温度
    int dehumOuterTemp;//除湿机外盘管温度
    int dehumSleep;//除湿机睡眠模式 0关闭 1开启
    int dehumAnion;//除湿机负离子模式 0关闭 1开启
    int dehumDrying;//除湿机干衣模式 0关闭 1开启
    int dehumDefrost;//除湿机除霜模式 0关闭 1开启


    String aCondState;//空调当前状态 000:  自动模式；001： 制冷模式；010： 制热模式；011： 通风模式；100： 除湿模式；
    int aCondSetTemp1;/**空调设定温度1*/
    int aCondSetTemp2;/**空调设定温度2*/
    int aCondSetData;/**空调设定参数*/
    int aCondSimpleTemp1;/**空调采样温度1*/
    int aCondSimpleTemp2;/**空调采样温度2*/
    int aCondInnerTemp;//空调内盘管温度
    int aCondOuterTemp;//空调外盘管温度

    int aCondSleep;//空调睡眠模式 0关闭 1开启
    int aCondSUpDown;// 0:上下摆叶关闭   1：摆叶开启
    int aCondSLeftRight;//0:左右摆叶关闭   1：摆叶开启



    int socketTimerMode;/**插座计时模式 定时模式，倒计时模式 1为倒计时 2为定时*/
    int isSocketTimerMode;/**定时模式是否开启*/


    int wPurifierEndFlow;/**净水器截止使用流量*/
    int wPurifierEndYear;/**净水器截止使用时间年*/
    int wPurifierEndMonth;/**净水器截止使用月*/
    int wPurifierEndDay;/**净水器截止使用日*/
    String wPurifierState;/**净水器状态*/
    int wPurifierFlowData;/**净水器流量数据*/
    int wPurifierPrimaryQuqlity;/**净水器原生水质*/
    int wPurifierCurTemp;/**净水器当前温度*/
    int wPurifierOutQuqlity;/**净水器出水水质*/
    /**净水器滤芯寿命 1-10*/
    int wPurifierfilter1,wPurifierfilter2,wPurifierfilter3,wPurifierfilter4,wPurifierfilter5,wPurifierfilter6,wPurifierfilter7,wPurifierfilter8,wPurifierfilter9,wPurifierfilter10;

    private String houseAddress="";/**设备家庭真实地址*/
    private String province;/**设备所在的省份*/
    public int getWPurifierfilter10() {
        return this.wPurifierfilter10;
    }
    public void setWPurifierfilter10(int wPurifierfilter10) {
        this.wPurifierfilter10 = wPurifierfilter10;
    }
    public int getWPurifierfilter9() {
        return this.wPurifierfilter9;
    }
    public void setWPurifierfilter9(int wPurifierfilter9) {
        this.wPurifierfilter9 = wPurifierfilter9;
    }
    public int getWPurifierfilter8() {
        return this.wPurifierfilter8;
    }
    public void setWPurifierfilter8(int wPurifierfilter8) {
        this.wPurifierfilter8 = wPurifierfilter8;
    }
    public int getWPurifierfilter7() {
        return this.wPurifierfilter7;
    }
    public void setWPurifierfilter7(int wPurifierfilter7) {
        this.wPurifierfilter7 = wPurifierfilter7;
    }
    public int getWPurifierfilter6() {
        return this.wPurifierfilter6;
    }
    public void setWPurifierfilter6(int wPurifierfilter6) {
        this.wPurifierfilter6 = wPurifierfilter6;
    }
    public int getWPurifierfilter5() {
        return this.wPurifierfilter5;
    }
    public void setWPurifierfilter5(int wPurifierfilter5) {
        this.wPurifierfilter5 = wPurifierfilter5;
    }
    public int getWPurifierfilter4() {
        return this.wPurifierfilter4;
    }
    public void setWPurifierfilter4(int wPurifierfilter4) {
        this.wPurifierfilter4 = wPurifierfilter4;
    }
    public int getWPurifierfilter3() {
        return this.wPurifierfilter3;
    }
    public void setWPurifierfilter3(int wPurifierfilter3) {
        this.wPurifierfilter3 = wPurifierfilter3;
    }
    public int getWPurifierfilter2() {
        return this.wPurifierfilter2;
    }
    public void setWPurifierfilter2(int wPurifierfilter2) {
        this.wPurifierfilter2 = wPurifierfilter2;
    }
    public int getWPurifierfilter1() {
        return this.wPurifierfilter1;
    }
    public void setWPurifierfilter1(int wPurifierfilter1) {
        this.wPurifierfilter1 = wPurifierfilter1;
    }
    public int getWPurifierOutQuqlity() {
        return this.wPurifierOutQuqlity;
    }
    public void setWPurifierOutQuqlity(int wPurifierOutQuqlity) {
        this.wPurifierOutQuqlity = wPurifierOutQuqlity;
    }
    public int getWPurifierCurTemp() {
        return this.wPurifierCurTemp;
    }
    public void setWPurifierCurTemp(int wPurifierCurTemp) {
        this.wPurifierCurTemp = wPurifierCurTemp;
    }
    public int getWPurifierPrimaryQuqlity() {
        return this.wPurifierPrimaryQuqlity;
    }
    public void setWPurifierPrimaryQuqlity(int wPurifierPrimaryQuqlity) {
        this.wPurifierPrimaryQuqlity = wPurifierPrimaryQuqlity;
    }
    public int getWPurifierFlowData() {
        return this.wPurifierFlowData;
    }
    public void setWPurifierFlowData(int wPurifierFlowData) {
        this.wPurifierFlowData = wPurifierFlowData;
    }
    public String getWPurifierState() {
        return this.wPurifierState;
    }
    public void setWPurifierState(String wPurifierState) {
        this.wPurifierState = wPurifierState;
    }
    public int getWPurifierEndDay() {
        return this.wPurifierEndDay;
    }
    public void setWPurifierEndDay(int wPurifierEndDay) {
        this.wPurifierEndDay = wPurifierEndDay;
    }
    public int getWPurifierEndMonth() {
        return this.wPurifierEndMonth;
    }
    public void setWPurifierEndMonth(int wPurifierEndMonth) {
        this.wPurifierEndMonth = wPurifierEndMonth;
    }
    public int getWPurifierEndYear() {
        return this.wPurifierEndYear;
    }
    public void setWPurifierEndYear(int wPurifierEndYear) {
        this.wPurifierEndYear = wPurifierEndYear;
    }
    public int getWPurifierEndFlow() {
        return this.wPurifierEndFlow;
    }
    public void setWPurifierEndFlow(int wPurifierEndFlow) {
        this.wPurifierEndFlow = wPurifierEndFlow;
    }
    public int getIsSocketTimerMode() {
        return this.isSocketTimerMode;
    }
    public void setIsSocketTimerMode(int isSocketTimerMode) {
        this.isSocketTimerMode = isSocketTimerMode;
    }
    public int getSocketTimerMode() {
        return this.socketTimerMode;
    }
    public void setSocketTimerMode(int socketTimerMode) {
        this.socketTimerMode = socketTimerMode;
    }
    public int getACondSLeftRight() {
        return this.aCondSLeftRight;
    }
    public void setACondSLeftRight(int aCondSLeftRight) {
        this.aCondSLeftRight = aCondSLeftRight;
    }
    public int getACondSUpDown() {
        return this.aCondSUpDown;
    }
    public void setACondSUpDown(int aCondSUpDown) {
        this.aCondSUpDown = aCondSUpDown;
    }
    public int getACondSleep() {
        return this.aCondSleep;
    }
    public void setACondSleep(int aCondSleep) {
        this.aCondSleep = aCondSleep;
    }
    public int getACondOuterTemp() {
        return this.aCondOuterTemp;
    }
    public void setACondOuterTemp(int aCondOuterTemp) {
        this.aCondOuterTemp = aCondOuterTemp;
    }
    public int getACondInnerTemp() {
        return this.aCondInnerTemp;
    }
    public void setACondInnerTemp(int aCondInnerTemp) {
        this.aCondInnerTemp = aCondInnerTemp;
    }
    public int getACondSimpleTemp2() {
        return this.aCondSimpleTemp2;
    }
    public void setACondSimpleTemp2(int aCondSimpleTemp2) {
        this.aCondSimpleTemp2 = aCondSimpleTemp2;
    }
    public int getACondSimpleTemp1() {
        return this.aCondSimpleTemp1;
    }
    public void setACondSimpleTemp1(int aCondSimpleTemp1) {
        this.aCondSimpleTemp1 = aCondSimpleTemp1;
    }
    public int getACondSetData() {
        return this.aCondSetData;
    }
    public void setACondSetData(int aCondSetData) {
        this.aCondSetData = aCondSetData;
    }
    public int getACondSetTemp2() {
        return this.aCondSetTemp2;
    }
    public void setACondSetTemp2(int aCondSetTemp2) {
        this.aCondSetTemp2 = aCondSetTemp2;
    }
    public int getACondSetTemp1() {
        return this.aCondSetTemp1;
    }
    public void setACondSetTemp1(int aCondSetTemp1) {
        this.aCondSetTemp1 = aCondSetTemp1;
    }
    public String getACondState() {
        return this.aCondState;
    }
    public void setACondState(String aCondState) {
        this.aCondState = aCondState;
    }
    public int getDehumDefrost() {
        return this.dehumDefrost;
    }
    public void setDehumDefrost(int dehumDefrost) {
        this.dehumDefrost = dehumDefrost;
    }
    public int getDehumDrying() {
        return this.dehumDrying;
    }
    public void setDehumDrying(int dehumDrying) {
        this.dehumDrying = dehumDrying;
    }
    public int getDehumAnion() {
        return this.dehumAnion;
    }
    public void setDehumAnion(int dehumAnion) {
        this.dehumAnion = dehumAnion;
    }
    public int getDehumSleep() {
        return this.dehumSleep;
    }
    public void setDehumSleep(int dehumSleep) {
        this.dehumSleep = dehumSleep;
    }
    public int getDehumOuterTemp() {
        return this.dehumOuterTemp;
    }
    public void setDehumOuterTemp(int dehumOuterTemp) {
        this.dehumOuterTemp = dehumOuterTemp;
    }
    public int getDehumInnerTemp() {
        return this.dehumInnerTemp;
    }
    public void setDehumInnerTemp(int dehumInnerTemp) {
        this.dehumInnerTemp = dehumInnerTemp;
    }
    public int getDehumSetHum() {
        return this.dehumSetHum;
    }
    public void setDehumSetHum(int dehumSetHum) {
        this.dehumSetHum = dehumSetHum;
    }
    public int getDehumSetTemp() {
        return this.dehumSetTemp;
    }
    public void setDehumSetTemp(int dehumSetTemp) {
        this.dehumSetTemp = dehumSetTemp;
    }
    public String getPurifierState() {
        return this.purifierState;
    }
    public void setPurifierState(String purifierState) {
        this.purifierState = purifierState;
    }
    public int getFaultCode() {
        return this.faultCode;
    }
    public void setFaultCode(int faultCode) {
        this.faultCode = faultCode;
    }
    public int getEquipCurdPowerLow() {
        return this.equipCurdPowerLow;
    }
    public void setEquipCurdPowerLow(int equipCurdPowerLow) {
        this.equipCurdPowerLow = equipCurdPowerLow;
    }
    public int getEquipCurdPowerHigh() {
        return this.equipCurdPowerHigh;
    }
    public void setEquipCurdPowerHigh(int equipCurdPowerHigh) {
        this.equipCurdPowerHigh = equipCurdPowerHigh;
    }
    public int getEquipRatedPowerLow() {
        return this.equipRatedPowerLow;
    }
    public void setEquipRatedPowerLow(int equipRatedPowerLow) {
        this.equipRatedPowerLow = equipRatedPowerLow;
    }
    public int getEquipRatedPowerHigh() {
        return this.equipRatedPowerHigh;
    }
    public void setEquipRatedPowerHigh(int equipRatedPowerHigh) {
        this.equipRatedPowerHigh = equipRatedPowerHigh;
    }
    public String getWindLevel() {
        return this.windLevel;
    }
    public void setWindLevel(String windLevel) {
        this.windLevel = windLevel;
    }
    public int getWaterLevel() {
        return this.waterLevel;
    }
    public void setWaterLevel(int waterLevel) {
        this.waterLevel = waterLevel;
    }
    public int getTimerSwitch() {
        return this.timerSwitch;
    }
    public void setTimerSwitch(int timerSwitch) {
        this.timerSwitch = timerSwitch;
    }
    public int getSocketPowerConsume() {
        return this.socketPowerConsume;
    }
    public void setSocketPowerConsume(int socketPowerConsume) {
        this.socketPowerConsume = socketPowerConsume;
    }
    public int getSocketVal() {
        return this.socketVal;
    }
    public void setSocketVal(int socketVal) {
        this.socketVal = socketVal;
    }
    public int getSocketCurrent() {
        return this.socketCurrent;
    }
    public void setSocketCurrent(int socketCurrent) {
        this.socketCurrent = socketCurrent;
    }
    public int getSocketTimer() {
        return this.socketTimer;
    }
    public void setSocketTimer(int socketTimer) {
        this.socketTimer = socketTimer;
    }
    public int getSocketState() {
        return this.socketState;
    }
    public void setSocketState(int socketState) {
        this.socketState = socketState;
    }
    public int getSocketTemp() {
        return this.socketTemp;
    }
    public void setSocketTemp(int socketTemp) {
        this.socketTemp = socketTemp;
    }
    public int getSocketPower() {
        return this.socketPower;
    }
    public void setSocketPower(int socketPower) {
        this.socketPower = socketPower;
    }
    public int getSocketPowerLow() {
        return this.socketPowerLow;
    }
    public void setSocketPowerLow(int socketPowerLow) {
        this.socketPowerLow = socketPowerLow;
    }
    public int getSocketPowerHigh() {
        return this.socketPowerHigh;
    }
    public void setSocketPowerHigh(int socketPowerHigh) {
        this.socketPowerHigh = socketPowerHigh;
    }
    public int getSensorHcho() {
        return this.sensorHcho;
    }
    public void setSensorHcho(int sensorHcho) {
        this.sensorHcho = sensorHcho;
    }
    public int getSensorOx() {
        return this.sensorOx;
    }
    public void setSensorOx(int sensorOx) {
        this.sensorOx = sensorOx;
    }
    public int getSorsorPm() {
        return this.sorsorPm;
    }
    public void setSorsorPm(int sorsorPm) {
        this.sorsorPm = sorsorPm;
    }
    public int getSensorSimpleHum() {
        return this.sensorSimpleHum;
    }
    public void setSensorSimpleHum(int sensorSimpleHum) {
        this.sensorSimpleHum = sensorSimpleHum;
    }
    public int getSensorSimpleTemp() {
        return this.sensorSimpleTemp;
    }
    public void setSensorSimpleTemp(int sensorSimpleTemp) {
        this.sensorSimpleTemp = sensorSimpleTemp;
    }
    public int getSensorState() {
        return this.sensorState;
    }
    public void setSensorState(int sensorState) {
        this.sensorState = sensorState;
    }
    public long getShareId() {
        return this.shareId;
    }
    public void setShareId(long shareId) {
        this.shareId = shareId;
    }
    public int getLinkedSensorId() {
        return this.linkedSensorId;
    }
    public void setLinkedSensorId(int linkedSensorId) {
        this.linkedSensorId = linkedSensorId;
    }
    public int getLinked() {
        return this.linked;
    }
    public void setLinked(int linked) {
        this.linked = linked;
    }
    public int getDeviceId() {
        return this.deviceId;
    }
    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }
    public int getImg() {
        return this.img;
    }
    public void setImg(int img) {
        this.img = img;
    }
    public int getUserId() {
        return this.userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
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
    public String getWifiVersion() {
        return this.wifiVersion;
    }
    public void setWifiVersion(String wifiVersion) {
        this.wifiVersion = wifiVersion;
    }
    public String getMcuVersion() {
        return this.mcuVersion;
    }
    public void setMcuVersion(String mcuVersion) {
        this.mcuVersion = mcuVersion;
    }
    public int getTimerMoudle() {
        return this.timerMoudle;
    }
    public void setTimerMoudle(int timerMoudle) {
        this.timerMoudle = timerMoudle;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMacAddress() {
        return this.macAddress;
    }
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
    public int getBusModel() {
        return this.busModel;
    }
    public void setBusModel(int busModel) {
        this.busModel = busModel;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getShare() {
        return this.share;
    }
    public void setShare(String share) {
        this.share = share;
    }
    public String getCommon() {
        return this.common;
    }
    public void setCommon(String common) {
        this.common = common;
    }
    public String getRoomName() {
        return this.roomName;
    }
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public int getDeviceUsedCount() {
        return this.deviceUsedCount;
    }
    public void setDeviceUsedCount(int deviceUsedCount) {
        this.deviceUsedCount = deviceUsedCount;
    }
    public long getRoomId() {
        return this.roomId;
    }
    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }
    public long getHouseId() {
        return this.houseId;
    }
    public void setHouseId(long houseId) {
        this.houseId = houseId;
    }
    public boolean getOnline() {
        return this.online;
    }
    public void setOnline(boolean online) {
        this.online = online;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getWarmerFall() {
        return this.warmerFall;
    }
    public void setWarmerFall(int warmerFall) {
        this.warmerFall = warmerFall;
    }
    public int getSocketTimerMin() {
        return this.socketTimerMin;
    }
    public void setSocketTimerMin(int socketTimerMin) {
        this.socketTimerMin = socketTimerMin;
    }
    public int getSocketTimerHour() {
        return this.socketTimerHour;
    }
    public void setSocketTimerHour(int socketTimerHour) {
        this.socketTimerHour = socketTimerHour;
    }
    public String getProvince() {
        return this.province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getHouseAddress() {
        return this.houseAddress;
    }
    public void setHouseAddress(String houseAddress) {
        this.houseAddress = houseAddress;
    }
    public int getLock() {
        return this.lock;
    }
    public void setLock(int lock) {
        this.lock = lock;
    }

    public DeviceChild(long houseId, long roomId, int deviceUsedCount, int type, String macAddress, String name, int userId) {
        this.houseId = houseId;
        this.roomId = roomId;
        this.deviceUsedCount = deviceUsedCount;
        this.type = type;
        this.macAddress = macAddress;
        this.name = name;
        this.userId = userId;
    }


    @Generated(hash = 1973447423)
    public DeviceChild() {
    }
    @Generated(hash = 869457734)
    public DeviceChild(Long id, boolean online, long houseId, long roomId, int deviceUsedCount, String roomName, String common, String share, int type, int busModel,
            String macAddress, String name, int timerMoudle, String mcuVersion, String wifiVersion, int waramerSetTemp, int warmerCurTemp, int warmerSampleData, int warmerRatePower,
            int warmerCurRunRoatePower, int warmerRunState, int deviceState, int warmerFall, String rateState, int lock, int week, int timerOpenOneHour, int timerOpenOneMin,
            int timerCloseOneHour, int timerCloseOneMin, int timerOpenTwoHour, int timerOpenTwoMin, int timerCloseTwoHour, int timerCloseTwoMin, int timerOpenThrHour,
            int timerOpenThrMin, int timerCloseThrHour, int timerCloseThrMin, int timerOpenForHour, int timerOpenForMin, int timerCloseForHour, int timerCloseForMin, int timerOne,
            int timerTwo, int timerThr, int timerFor, int lockState, int screenState, int curRunState2, int curRunState3, int timerHour, int timerMin, int checkCode, int endCode,
            int userId, int img, int deviceId, int linked, int linkedSensorId, long shareId, int sensorState, int sensorSimpleTemp, int sensorSimpleHum, int sorsorPm, int sensorOx,
            int sensorHcho, int socketPowerHigh, int socketPowerLow, int socketPower, int socketTemp, int socketState, int socketTimer, int socketTimerHour, int socketTimerMin,
            int socketCurrent, int socketVal, int socketPowerConsume, int timerSwitch, int waterLevel, String windLevel, int equipRatedPowerHigh, int equipRatedPowerLow,
            int equipCurdPowerHigh, int equipCurdPowerLow, int faultCode, String purifierState, int dehumSetTemp, int dehumSetHum, int dehumInnerTemp, int dehumOuterTemp,
            int dehumSleep, int dehumAnion, int dehumDrying, int dehumDefrost, String aCondState, int aCondSetTemp1, int aCondSetTemp2, int aCondSetData, int aCondSimpleTemp1,
            int aCondSimpleTemp2, int aCondInnerTemp, int aCondOuterTemp, int aCondSleep, int aCondSUpDown, int aCondSLeftRight, int socketTimerMode, int isSocketTimerMode,
            int wPurifierEndFlow, int wPurifierEndYear, int wPurifierEndMonth, int wPurifierEndDay, String wPurifierState, int wPurifierFlowData, int wPurifierPrimaryQuqlity,
            int wPurifierCurTemp, int wPurifierOutQuqlity, int wPurifierfilter1, int wPurifierfilter2, int wPurifierfilter3, int wPurifierfilter4, int wPurifierfilter5,
            int wPurifierfilter6, int wPurifierfilter7, int wPurifierfilter8, int wPurifierfilter9, int wPurifierfilter10, String houseAddress, String province) {
        this.id = id;
        this.online = online;
        this.houseId = houseId;
        this.roomId = roomId;
        this.deviceUsedCount = deviceUsedCount;
        this.roomName = roomName;
        this.common = common;
        this.share = share;
        this.type = type;
        this.busModel = busModel;
        this.macAddress = macAddress;
        this.name = name;
        this.timerMoudle = timerMoudle;
        this.mcuVersion = mcuVersion;
        this.wifiVersion = wifiVersion;
        this.waramerSetTemp = waramerSetTemp;
        this.warmerCurTemp = warmerCurTemp;
        this.warmerSampleData = warmerSampleData;
        this.warmerRatePower = warmerRatePower;
        this.warmerCurRunRoatePower = warmerCurRunRoatePower;
        this.warmerRunState = warmerRunState;
        this.deviceState = deviceState;
        this.warmerFall = warmerFall;
        this.rateState = rateState;
        this.lock = lock;
        this.week = week;
        this.timerOpenOneHour = timerOpenOneHour;
        this.timerOpenOneMin = timerOpenOneMin;
        this.timerCloseOneHour = timerCloseOneHour;
        this.timerCloseOneMin = timerCloseOneMin;
        this.timerOpenTwoHour = timerOpenTwoHour;
        this.timerOpenTwoMin = timerOpenTwoMin;
        this.timerCloseTwoHour = timerCloseTwoHour;
        this.timerCloseTwoMin = timerCloseTwoMin;
        this.timerOpenThrHour = timerOpenThrHour;
        this.timerOpenThrMin = timerOpenThrMin;
        this.timerCloseThrHour = timerCloseThrHour;
        this.timerCloseThrMin = timerCloseThrMin;
        this.timerOpenForHour = timerOpenForHour;
        this.timerOpenForMin = timerOpenForMin;
        this.timerCloseForHour = timerCloseForHour;
        this.timerCloseForMin = timerCloseForMin;
        this.timerOne = timerOne;
        this.timerTwo = timerTwo;
        this.timerThr = timerThr;
        this.timerFor = timerFor;
        this.lockState = lockState;
        this.screenState = screenState;
        this.curRunState2 = curRunState2;
        this.curRunState3 = curRunState3;
        this.timerHour = timerHour;
        this.timerMin = timerMin;
        this.checkCode = checkCode;
        this.endCode = endCode;
        this.userId = userId;
        this.img = img;
        this.deviceId = deviceId;
        this.linked = linked;
        this.linkedSensorId = linkedSensorId;
        this.shareId = shareId;
        this.sensorState = sensorState;
        this.sensorSimpleTemp = sensorSimpleTemp;
        this.sensorSimpleHum = sensorSimpleHum;
        this.sorsorPm = sorsorPm;
        this.sensorOx = sensorOx;
        this.sensorHcho = sensorHcho;
        this.socketPowerHigh = socketPowerHigh;
        this.socketPowerLow = socketPowerLow;
        this.socketPower = socketPower;
        this.socketTemp = socketTemp;
        this.socketState = socketState;
        this.socketTimer = socketTimer;
        this.socketTimerHour = socketTimerHour;
        this.socketTimerMin = socketTimerMin;
        this.socketCurrent = socketCurrent;
        this.socketVal = socketVal;
        this.socketPowerConsume = socketPowerConsume;
        this.timerSwitch = timerSwitch;
        this.waterLevel = waterLevel;
        this.windLevel = windLevel;
        this.equipRatedPowerHigh = equipRatedPowerHigh;
        this.equipRatedPowerLow = equipRatedPowerLow;
        this.equipCurdPowerHigh = equipCurdPowerHigh;
        this.equipCurdPowerLow = equipCurdPowerLow;
        this.faultCode = faultCode;
        this.purifierState = purifierState;
        this.dehumSetTemp = dehumSetTemp;
        this.dehumSetHum = dehumSetHum;
        this.dehumInnerTemp = dehumInnerTemp;
        this.dehumOuterTemp = dehumOuterTemp;
        this.dehumSleep = dehumSleep;
        this.dehumAnion = dehumAnion;
        this.dehumDrying = dehumDrying;
        this.dehumDefrost = dehumDefrost;
        this.aCondState = aCondState;
        this.aCondSetTemp1 = aCondSetTemp1;
        this.aCondSetTemp2 = aCondSetTemp2;
        this.aCondSetData = aCondSetData;
        this.aCondSimpleTemp1 = aCondSimpleTemp1;
        this.aCondSimpleTemp2 = aCondSimpleTemp2;
        this.aCondInnerTemp = aCondInnerTemp;
        this.aCondOuterTemp = aCondOuterTemp;
        this.aCondSleep = aCondSleep;
        this.aCondSUpDown = aCondSUpDown;
        this.aCondSLeftRight = aCondSLeftRight;
        this.socketTimerMode = socketTimerMode;
        this.isSocketTimerMode = isSocketTimerMode;
        this.wPurifierEndFlow = wPurifierEndFlow;
        this.wPurifierEndYear = wPurifierEndYear;
        this.wPurifierEndMonth = wPurifierEndMonth;
        this.wPurifierEndDay = wPurifierEndDay;
        this.wPurifierState = wPurifierState;
        this.wPurifierFlowData = wPurifierFlowData;
        this.wPurifierPrimaryQuqlity = wPurifierPrimaryQuqlity;
        this.wPurifierCurTemp = wPurifierCurTemp;
        this.wPurifierOutQuqlity = wPurifierOutQuqlity;
        this.wPurifierfilter1 = wPurifierfilter1;
        this.wPurifierfilter2 = wPurifierfilter2;
        this.wPurifierfilter3 = wPurifierfilter3;
        this.wPurifierfilter4 = wPurifierfilter4;
        this.wPurifierfilter5 = wPurifierfilter5;
        this.wPurifierfilter6 = wPurifierfilter6;
        this.wPurifierfilter7 = wPurifierfilter7;
        this.wPurifierfilter8 = wPurifierfilter8;
        this.wPurifierfilter9 = wPurifierfilter9;
        this.wPurifierfilter10 = wPurifierfilter10;
        this.houseAddress = houseAddress;
        this.province = province;
    }
    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getTimerOpenOneHour() {
        return timerOpenOneHour;
    }

    public void setTimerOpenOneHour(int timerOpenOneHour) {
        this.timerOpenOneHour = timerOpenOneHour;
    }

    public int getTimerOpenOneMin() {
        return timerOpenOneMin;
    }

    public void setTimerOpenOneMin(int timerOpenOneMin) {
        this.timerOpenOneMin = timerOpenOneMin;
    }

    public int getTimerCloseOneHour() {
        return timerCloseOneHour;
    }

    public void setTimerCloseOneHour(int timerCloseOneHour) {
        this.timerCloseOneHour = timerCloseOneHour;
    }

    public int getTimerCloseOneMin() {
        return timerCloseOneMin;
    }

    public void setTimerCloseOneMin(int timerCloseOneMin) {
        this.timerCloseOneMin = timerCloseOneMin;
    }

    public int getTimerOpenTwoHour() {
        return timerOpenTwoHour;
    }

    public void setTimerOpenTwoHour(int timerOpenTwoHour) {
        this.timerOpenTwoHour = timerOpenTwoHour;
    }

    public int getTimerOpenTwoMin() {
        return timerOpenTwoMin;
    }

    public void setTimerOpenTwoMin(int timerOpenTwoMin) {
        this.timerOpenTwoMin = timerOpenTwoMin;
    }

    public int getTimerCloseTwoHour() {
        return timerCloseTwoHour;
    }

    public void setTimerCloseTwoHour(int timerCloseTwoHour) {
        this.timerCloseTwoHour = timerCloseTwoHour;
    }

    public int getTimerCloseTwoMin() {
        return timerCloseTwoMin;
    }

    public void setTimerCloseTwoMin(int timerCloseTwoMin) {
        this.timerCloseTwoMin = timerCloseTwoMin;
    }

    public int getTimerOpenThrHour() {
        return timerOpenThrHour;
    }

    public void setTimerOpenThrHour(int timerOpenThrHour) {
        this.timerOpenThrHour = timerOpenThrHour;
    }

    public int getTimerOpenThrMin() {
        return timerOpenThrMin;
    }

    public void setTimerOpenThrMin(int timerOpenThrMin) {
        this.timerOpenThrMin = timerOpenThrMin;
    }

    public int getTimerCloseThrHour() {
        return timerCloseThrHour;
    }

    public void setTimerCloseThrHour(int timerCloseThrHour) {
        this.timerCloseThrHour = timerCloseThrHour;
    }

    public int getTimerCloseThrMin() {
        return timerCloseThrMin;
    }

    public void setTimerCloseThrMin(int timerCloseThrMin) {
        this.timerCloseThrMin = timerCloseThrMin;
    }

    public int getTimerOpenForHour() {
        return timerOpenForHour;
    }

    public void setTimerOpenForHour(int timerOpenForHour) {
        this.timerOpenForHour = timerOpenForHour;
    }

    public int getTimerOpenForMin() {
        return timerOpenForMin;
    }

    public void setTimerOpenForMin(int timerOpenForMin) {
        this.timerOpenForMin = timerOpenForMin;
    }

    public int getTimerCloseForHour() {
        return timerCloseForHour;
    }

    public void setTimerCloseForHour(int timerCloseForHour) {
        this.timerCloseForHour = timerCloseForHour;
    }

    public int getTimerCloseForMin() {
        return timerCloseForMin;
    }

    public void setTimerCloseForMin(int timerCloseForMin) {
        this.timerCloseForMin = timerCloseForMin;
    }

    public int getTimerOne() {
        return timerOne;
    }

    public void setTimerOne(int timerOne) {
        this.timerOne = timerOne;
    }

    public int getTimerTwo() {
        return timerTwo;
    }

    public void setTimerTwo(int timerTwo) {
        this.timerTwo = timerTwo;
    }

    public int getTimerThr() {
        return timerThr;
    }

    public void setTimerThr(int timerThr) {
        this.timerThr = timerThr;
    }

    public int getTimerFor() {
        return timerFor;
    }

    public void setTimerFor(int timerFor) {
        this.timerFor = timerFor;
    }
}
