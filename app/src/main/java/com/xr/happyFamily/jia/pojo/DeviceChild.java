package com.xr.happyFamily.jia.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

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
    String rateState=null;/**功率状态  11: 3档 10: 2档  01: 1档*/
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

    int socketPower;/**插座功率*/
    int socketTemp;/**插座温度*/
    int socketState;/**插座当前状态*/
    int socketTimer;/**插座定时模式*/
    int socketTimerOpenHour;/**插座定时模式开的 时*/
    int socketTimerOpenMin;/**插座定时模式开的 分*/
    int socketTimerCloseHour;/**插座定时模式关的 时*/
    int socketTimerCloseMin;/**插座定时模式关的 分*/
    int socketCurrent;/**插座当前电流值*/
    int socketVal;/**插座当前电压值*/
    int socketPowerConsume;/**插座当前耗电量总度数*/
    int socketTimerMode;/**插座计时模式 定时模式，倒计时模式 1为倒计时 2为定时*/
    int isSocketTimerMode;/**定时模式是否开启*/

    int wPurifierEndTime;/**净水器截止使用时间*/
    int wPurifierEndFlow;/**净水器截止使用流量*/
    String wPurifierState;/**净水器状态*/
    int wPurifierFlowData;/**净水器流量数据*/
    int wPurifierPrimaryQuqlity;/**净水器原生水质*/
    int wPurifierCurTemp;/**净水器当前温度*/
    int wPurifierOutQuqlity;/**净水器出水水质*/
    /**净水器滤芯寿命 1-10*/
    int wPurifierfilter1,wPurifierfilter2,wPurifierfilter3,wPurifierfilter4,wPurifierfilter5,wPurifierfilter6,wPurifierfilter7,wPurifierfilter8,wPurifierfilter9,wPurifierfilter10;
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
    public int getSocketTimerCloseMin() {
        return this.socketTimerCloseMin;
    }
    public void setSocketTimerCloseMin(int socketTimerCloseMin) {
        this.socketTimerCloseMin = socketTimerCloseMin;
    }
    public int getSocketTimerCloseHour() {
        return this.socketTimerCloseHour;
    }
    public void setSocketTimerCloseHour(int socketTimerCloseHour) {
        this.socketTimerCloseHour = socketTimerCloseHour;
    }
    public int getSocketTimerOpenMin() {
        return this.socketTimerOpenMin;
    }
    public void setSocketTimerOpenMin(int socketTimerOpenMin) {
        this.socketTimerOpenMin = socketTimerOpenMin;
    }
    public int getSocketTimerOpenHour() {
        return this.socketTimerOpenHour;
    }
    public void setSocketTimerOpenHour(int socketTimerOpenHour) {
        this.socketTimerOpenHour = socketTimerOpenHour;
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

    public String getShare() {
        return this.share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public int getSocketTimerMode() {
        return this.socketTimerMode;
    }

    public void setSocketTimerMode(int socketTimerMode) {
        this.socketTimerMode = socketTimerMode;
    }

    public int getIsSocketTimerMode() {
        return this.isSocketTimerMode;
    }

    public void setIsSocketTimerMode(int isSocketTimerMode) {
        this.isSocketTimerMode = isSocketTimerMode;
    }

    public int getSocketPower() {
        return this.socketPower;
    }

    public void setSocketPower(int socketPower) {
        this.socketPower = socketPower;
    }

    public int getWPurifierCurTemp() {
        return this.wPurifierCurTemp;
    }

    public void setWPurifierCurTemp(int wPurifierCurTemp) {
        this.wPurifierCurTemp = wPurifierCurTemp;
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

    public int getWPurifierEndFlow() {
        return this.wPurifierEndFlow;
    }

    public void setWPurifierEndFlow(int wPurifierEndFlow) {
        this.wPurifierEndFlow = wPurifierEndFlow;
    }

    public int getWPurifierEndTime() {
        return this.wPurifierEndTime;
    }

    public void setWPurifierEndTime(int wPurifierEndTime) {
        this.wPurifierEndTime = wPurifierEndTime;
    }

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

    public int getWPurifierPrimaryQuqlity() {
        return this.wPurifierPrimaryQuqlity;
    }

    public void setWPurifierPrimaryQuqlity(int wPurifierPrimaryQuqlity) {
        this.wPurifierPrimaryQuqlity = wPurifierPrimaryQuqlity;
    }

    public int getWPurifierOutQuqlity() {
        return this.wPurifierOutQuqlity;
    }

    public void setWPurifierOutQuqlity(int wPurifierOutQuqlity) {
        this.wPurifierOutQuqlity = wPurifierOutQuqlity;
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

    @Generated(hash = 1601519420)
    public DeviceChild(Long id, boolean online, long houseId, long roomId, int deviceUsedCount, String roomName, String common, String share, int type, int busModel,
            String macAddress, String name, int timerMoudle, String mcuVersion, String wifiVersion, int waramerSetTemp, int warmerCurTemp, int warmerSampleData, int warmerRatePower,
            int warmerCurRunRoatePower, int warmerRunState, int deviceState, String rateState, int lockState, int screenState, int curRunState2, int curRunState3, int timerHour,
            int timerMin, int checkCode, int endCode, int userId, int img, int deviceId, int linked, int linkedSensorId, long shareId, int sensorState, int sensorSimpleTemp,
            int sensorSimpleHum, int sorsorPm, int sensorOx, int sensorHcho, int socketPower, int socketTemp, int socketState, int socketTimer, int socketTimerOpenHour,
            int socketTimerOpenMin, int socketTimerCloseHour, int socketTimerCloseMin, int socketCurrent, int socketVal, int socketPowerConsume, int socketTimerMode,
            int isSocketTimerMode, int wPurifierEndTime, int wPurifierEndFlow, String wPurifierState, int wPurifierFlowData, int wPurifierPrimaryQuqlity, int wPurifierCurTemp,
            int wPurifierOutQuqlity, int wPurifierfilter1, int wPurifierfilter2, int wPurifierfilter3, int wPurifierfilter4, int wPurifierfilter5, int wPurifierfilter6,
            int wPurifierfilter7, int wPurifierfilter8, int wPurifierfilter9, int wPurifierfilter10) {
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
        this.rateState = rateState;
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
        this.socketPower = socketPower;
        this.socketTemp = socketTemp;
        this.socketState = socketState;
        this.socketTimer = socketTimer;
        this.socketTimerOpenHour = socketTimerOpenHour;
        this.socketTimerOpenMin = socketTimerOpenMin;
        this.socketTimerCloseHour = socketTimerCloseHour;
        this.socketTimerCloseMin = socketTimerCloseMin;
        this.socketCurrent = socketCurrent;
        this.socketVal = socketVal;
        this.socketPowerConsume = socketPowerConsume;
        this.socketTimerMode = socketTimerMode;
        this.isSocketTimerMode = isSocketTimerMode;
        this.wPurifierEndTime = wPurifierEndTime;
        this.wPurifierEndFlow = wPurifierEndFlow;
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
    }


    
}
