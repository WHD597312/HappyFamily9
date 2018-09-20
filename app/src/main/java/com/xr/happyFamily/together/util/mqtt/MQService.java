package com.xr.happyFamily.together.util.mqtt;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.xr.database.dao.daoimpl.ClockDaoImpl;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.database.dao.daoimpl.FriendDataDaoImpl;
import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.database.dao.daoimpl.MsgDaoImpl;
import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.database.dao.daoimpl.TimeDaoImpl;
import com.xr.database.dao.daoimpl.UserInfosDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.LiveActivity;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.activity.AConfActivity;
import com.xr.happyFamily.jia.activity.APurifierActivity;
import com.xr.happyFamily.jia.activity.AddDeviceActivity;
import com.xr.happyFamily.jia.activity.DehumidifierActivity;
import com.xr.happyFamily.jia.activity.DeviceDetailActivity;
import com.xr.happyFamily.jia.activity.PurifierActivity;
import com.xr.happyFamily.jia.activity.ShareDeviceActivity;
import com.xr.happyFamily.jia.activity.SmartLinkedActivity;
import com.xr.happyFamily.jia.activity.SmartTerminalActivity;
import com.xr.happyFamily.jia.activity.SocketActivity;
import com.xr.happyFamily.jia.activity.TempChatActivity;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.le.BtClock.RingReceiver;
import com.xr.happyFamily.le.ClockActivity;
import com.xr.happyFamily.le.bean.MsgFriendBean;
import com.xr.happyFamily.le.clock.MsgActivity;
import com.xr.happyFamily.le.clock.QunzuAddActivity;
import com.xr.happyFamily.le.fragment.QingLvFragment;
import com.xr.happyFamily.le.fragment.QunZuFragment;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.FriendData;
import com.xr.happyFamily.le.pojo.MsgData;
import com.xr.happyFamily.le.pojo.UserInfo;
import com.xr.happyFamily.le.view.btClockjsDialog4;
import com.xr.happyFamily.le.view.btClockjsDialog5;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.main.FamilyFragmentManager;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.util.TenTwoUtil;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MQService extends Service {

    private String TAG = "MQService";
    private String host = "tcp://47.98.131.11:1883";
    private String userName = "admin";
    private String passWord = "Xr7891122";
    String macAddress = "hrrj7895ccf7f6c9fa4";
    private HourseDaoImpl hourseDao;
    private RoomDaoImpl roomDao;
    private TimeDaoImpl timeDao;
    private UserInfosDaoImpl userInfosDao;
    private ClockDaoImpl clockDao;

    /**
     * 测试的macAddrsss
     */
    private DeviceChildDaoImpl deviceChildDao;
    private FriendDataDaoImpl friendDataDao;
    private MsgDaoImpl msgDao;

    private Context mContext = this;
//    CountTimer countTimer;
//    boolean isNew = false;
    /***
     * 模块类型
     */
    private int[] moduleType = {0X01, 0X02, 0X03, 0X04, 0X05, 0X06, 0X07, 0X08};
    /***
     * 商业模块
     */
    private int[] bussinessmodule = {0XF0, 0};


    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private MqttClient client;
    private MqttConnectOptions options;
    String clientId;
    private LocalBinder binder = new LocalBinder();

    /**
     * 服务启动之后就初始化MQTT,连接MQTT
     */
    @Override
    public void onCreate() {
        super.onCreate();
        clientId = UUID.getUUID(this);
        hourseDao = new HourseDaoImpl(this);
        deviceChildDao = new DeviceChildDaoImpl(this);
        friendDataDao = new FriendDataDaoImpl(this);
        msgDao = new MsgDaoImpl(this);
        roomDao = new RoomDaoImpl(this);
        timeDao = new TimeDaoImpl(this);
        userInfosDao = new UserInfosDaoImpl(this);
        clockDao = new ClockDaoImpl(this);
        init();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {

        public MQService getService() {
            Log.i(TAG, "Binder");
            return MQService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        connect();
        Log.i("clientId", "-->" + clientId);
        isFinish = false;
//        countTimer = new CountTimer(5000, 1000);
//        countTimer.start();
        SharedPreferences preferences = getSharedPreferences("position", MODE_PRIVATE);
        String clockData = preferences.getString("clockData", "");
        String[] clocks = clockData.split(",");
        for (int i = 0; i < clocks.length; i++) {
            new getClockAsync().execute(clocks[i]);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 销毁服务，则断开MQTT,释放资源
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            Log.i(TAG, "onDestroy");
            scheduler.shutdown();
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            //host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存

            client = new MqttClient(host, clientId,
                    new MemoryPersistence());
            //MQTT的连接设置
            options = new MqttConnectOptions();
            //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            //设置连接的用户名
            options.setUserName(userName);
            //设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(15);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
//            options.setKeepAliveInterval(20);


            //设置回调
            client.setCallback(new MqttCallback() {

                @Override
                public void connectionLost(Throwable cause) {
                    //连接丢失后，一般在这里面进行重连
                    System.out.println("connectionLost----------");
                    startReconnect();
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //publish后会执行到这里
                    System.out.println("deliveryComplete---------"
                            + token.isComplete());
                }

                @Override
                public void messageArrived(String topicName, MqttMessage message) {
                    try {
                        new LoadAsyncTask().execute(topicName, message.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            if (client.isConnected()) {
                client.disconnect();
                client.connect(options);
            }
            new ConAsync().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接MQTT
     */
    class ConAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (!client.isConnected()) {
                    client.connect(options);
                }
                List<String> topicNames = getTopicNames();
                if (client.isConnected() && !topicNames.isEmpty()) {
                    for (String topicName : topicNames) {
                        if (!TextUtils.isEmpty(topicName)) {
                            client.subscribe(topicName, 1);
                            Log.i("client", "-->" + topicName);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private int falling=-1;
    /**
     * 加载MQTT返回的消息
     */
    class LoadAsyncTask extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... strings) {

            String topicName = strings[0];/**收到的主题*/
            Log.i("topicName", "-->:" + topicName);
            String macAddress = null;
            if (topicName.startsWith("p99/" + phone)) {

            } else if (topicName.startsWith("p99/warmer1")) {
                macAddress = topicName.substring(12, topicName.lastIndexOf("/"));
            } else if (topicName.startsWith("p99/sensor1")) {
                macAddress = topicName.substring(12, topicName.lastIndexOf("/"));
            } else if (topicName.startsWith("p99/socket1")) {
                macAddress = topicName.substring(12, topicName.lastIndexOf("/"));
            }else if(topicName.startsWith("p99/dehumidifier1")){
                macAddress = topicName.substring(18, topicName.lastIndexOf("/"));
            }else if(topicName.startsWith("p99/aConditioning1")){
                macAddress = topicName.substring(19, topicName.lastIndexOf("/"));
            }else if(topicName.startsWith("p99/aPurifier1")){
                macAddress = topicName.substring(15, topicName.lastIndexOf("/"));
            }else if (topicName.startsWith("p99/wPurifier1")){
                macAddress = topicName.substring(15, topicName.lastIndexOf("/"));
            }else if (topicName.startsWith("p99")) {
                macAddress = topicName.substring(4, topicName.lastIndexOf("/"));
            }

            int type = -1;/**产品类型*/
            int busModel = -1;/**商业模式*/
            int timerMoudle = -1;/**定时器模式*/
            String mcuVersion = null;/**MCU版本*/
            String wifiVersion = null;/**wifi版本*/
            int waramerSetTemp = -1;/**取暖器设定温度*/
            int warmerCurTemp = -1;/**取暖器当前温度*/
            int warmerSampleData = -1;/**取暖器采样数据*/
            int warmerRatePower = -1;/**取暖器额定总功率*/
            int warmerCurRunRoatePower = -1;/**取暖器当前运行功率*/
            int warmerRunState = -1;/**机器当前运行状态*/
            int deviceState = -1;/**开关机状态 0表示关机，1表示开机*/
            String rateState = null;/**功率状态  11: 3档 10: 2档  01: 1档*/
            int lockState = -1;/** 屏幕是否锁定 1：锁定 0：未锁定*/
            int screenState = -1;/**屏保是否开启 1：开启 0：未开启 */
            int curRunState2 = -1;/**机器当前运行状态2  (保留)*/
            int curRunState3 = -1;/**机器当前运行状态2  (保留)*/
            int timerHour = -1;/**定时时间 小时*/
            int timerMin = -1;/**定时时间 分*/

            int timerSwitch=-1;//定时器开关
            String windLevel=null;//风速等级
            int waterLevel=-1;//水位量
            int equipRatedPowerHigh;/**设备额定高功率参数*/
            int equipRatedPowerLow;/**设备额定低功率参数*/
            int equipCurdPowerHigh;/**设备当前高功率参数*/
            int equipCurdPowerLow;/**设备当前低功率参数*/
            int faultCode;/**设备故障代码*/
            int checkCode = -1;/**校验码*/
            int endCode = -1;/**结束码*/
            String message = strings[1];/**收到的消息*/
            long sharedId = -1;/**分享的设备*/
            int warmerFall=-1;/**电暖器倾斜*/

            Log.i("mmm", "-->" + message);

            Log.i("mmm222", "-->" + macAddress);

            DeviceChild deviceChild = null;
            JSONObject messageJsonObject = null;
            String productType = null;
            JSONArray messageJsonArray = null;
            if (!TextUtils.isEmpty(macAddress)) {
                deviceChild = deviceChildDao.findDeviceByMacAddress2(macAddress);
            }

            if (AddDeviceActivity.running && !"reSet".equals(message)) {

            }
            try {
                if (topicName.equals("p99/" + phone + "/login")) {
                    JSONObject login = new JSONObject(message);
                    String uuid = login.getString("userId");
                    String uuid2 = UUID.getUUID(MQService.this);
                    if (!TextUtils.isEmpty(uuid) && uuid.equals(uuid2)) {
                        return null;
                    } else {
                        SharedPreferences.Editor editor = preferences.edit();
                        if (preferences.contains("password")) {
                            editor.remove("password").commit();
                        }
                        if (preferences.contains("image")) {
                            String image = preferences.getString("image", "");
                            preferences.edit().remove("image").commit();
                            File file = new File(image);
                            if (file.exists()) {
                                file.delete();
                            }
                        }
                        Message msg = handler.obtainMessage();
                        msg.what = 2;
                        handler.sendMessage(msg);
                        hourseDao.deleteAll();
                        roomDao.deleteAll();
                        deviceChildDao.deleteAll();
                        clockDao.deleteAll();
                        timeDao.deleteAll();
                        userInfosDao.deleteAll();
                        friendDataDao.deleteAll();
                        msgDao.deleteAll();
                        cancelAllsubscibe();

                    }
                }
                if ("reSet".equals(message)) {
                    if (deviceChild != null) {
                        long sharedId2 = deviceChild.getShareId();
                        if (sharedId2 == Long.MAX_VALUE) {
                            sharedId = sharedId2;
                        }
                        deviceChildDao.delete(deviceChild);
                        unsubscribe(topicName);
                        deviceChild = null;
                    }
                } else if ("offline".equals(message)) {
                    if (deviceChild != null) {
                        long sharedId2 = deviceChild.getShareId();
                        if (sharedId2 == Long.MAX_VALUE) {
                            sharedId = sharedId2;
                        }
                        deviceChild.setOnline(false);
                        deviceChildDao.update(deviceChild);
                    }
                }
                else if (topicName.contains("acceptorId_") && topicName.contains("friend")) {

                } else if (!TextUtils.isEmpty(macAddress) && macAddress.equals("clockuniversal")) {

                } else if (topicName.contains("clockuniversal")) {

                } else {
                    if (!TextUtils.isEmpty(message) && message.startsWith("{") && message.endsWith("}")) {
                        messageJsonObject = new JSONObject(message);
                    }
                    if (messageJsonObject != null && messageJsonObject.has("productType")) {
                        productType = messageJsonObject.getString("productType");
                    }
                    if (messageJsonObject != null && messageJsonObject.has("Warmer")) {
                        messageJsonArray = messageJsonObject.getJSONArray("Warmer");
                    } else if (messageJsonObject != null && messageJsonObject.has("TempHumPM2_5")) {
                        messageJsonArray = messageJsonObject.getJSONArray("TempHumPM2_5");
                    } else if (messageJsonObject != null && messageJsonObject.has("Socket")) {
                        messageJsonArray = messageJsonObject.getJSONArray("Socket");
                    }else if (messageJsonObject!=null && messageJsonObject.has("WPurifier")){
                        messageJsonArray = messageJsonObject.getJSONArray("WPurifier");
                    }

                    //jjjjjjjjjjjjjjjjjj
                    else if (messageJsonObject!=null && messageJsonObject.has("Dehumidifier")){
                        messageJsonArray = messageJsonObject.getJSONArray("Dehumidifier");
                    }
                    else if (messageJsonObject!=null && messageJsonObject.has("AConditioning")){
                        messageJsonArray = messageJsonObject.getJSONArray("AConditioning");
                    }
                    else if (messageJsonObject!=null && messageJsonObject.has("APurifier")){
                        messageJsonArray = messageJsonObject.getJSONArray("APurifier");
                    }
                    if (!TextUtils.isEmpty(productType)) {
                        type = Integer.parseInt(productType);
                    } else {
                        if (messageJsonArray != null) {
                            int index = messageJsonArray.getInt(1);
                            int index2 = messageJsonArray.getInt(2);
                            String x = "" + index + index2;
                            type = Integer.parseInt(x);
                        }
                    }
                }
                switch (type) {
                    case 1:
                        break;
                    case 2:/**取暖器*/

                            if (messageJsonArray != null) {
                                busModel = messageJsonArray.getInt(3);
                                int mMcuVersion = messageJsonArray.getInt(4);
                                mcuVersion = "v" + mMcuVersion / 16 + "." + mMcuVersion % 16;
                                int mWifiVersion = messageJsonArray.getInt(5);
                                wifiVersion = "v" + mWifiVersion / 16 + "." + mWifiVersion % 16;

                                warmerRunState = messageJsonArray.getInt(7);
                                curRunState2 = messageJsonArray.getInt(8);
                                curRunState3 = messageJsonArray.getInt(9);

                                int[] x = TenTwoUtil.changeToTwo(warmerRunState);
                                deviceState = x[7];
                                rateState = x[6] + "" + x[5];
                                lockState = x[4];
                                screenState = x[3];
                                warmerFall=x[2];

                                waramerSetTemp = messageJsonArray.getInt(10);
                                warmerCurTemp = messageJsonArray.getInt(11);

                                Log.i("warmerCurTemp", "-->" + warmerCurTemp);

                                warmerSampleData = messageJsonArray.getInt(12);
                                warmerRatePower = messageJsonArray.getInt(13);
                                warmerCurRunRoatePower = messageJsonArray.getInt(14);

                                timerHour = messageJsonArray.getInt(15);
                                timerMin = messageJsonArray.getInt(16);
                                checkCode = messageJsonArray.getInt(17);
                                endCode = messageJsonArray.getInt(18);
                            }

                        if (deviceChild != null) {
                            if (type != -1) {
                                deviceChild.setType(type);
                            }
                            if (busModel != -1) {
                                deviceChild.setBusModel(busModel);
                            }
                            if (timerMoudle != -1) {
                                deviceChild.setTimerMoudle(timerMoudle);
                            }
                            if (!TextUtils.isEmpty(wifiVersion)) {
                                deviceChild.setWifiVersion(wifiVersion);
                            }
                            if (!TextUtils.isEmpty(mcuVersion)) {
                                deviceChild.setMcuVersion(mcuVersion);
                            }
                            if (waramerSetTemp != -1) {
                                deviceChild.setWaramerSetTemp(waramerSetTemp);
                            }
                            if (warmerCurTemp != -1) {
                                deviceChild.setWarmerCurTemp(warmerCurTemp - 128);
                            }
                            if (warmerSampleData != -1) {
                                deviceChild.setWarmerSampleData(warmerSampleData - 128);
                            }
                            if (warmerRatePower != -1) {
                                deviceChild.setWarmerRatePower(warmerRatePower);
                            }
                            if (warmerCurRunRoatePower != -1) {
                                deviceChild.setWarmerCurRunRoatePower(warmerCurRunRoatePower);
                            }
                            if (deviceState != -1) {
                                deviceChild.setDeviceState(deviceState);
                            }
                            if (!TextUtils.isEmpty(rateState)) {
                                deviceChild.setRateState(rateState);
                            }
                            if (lockState != -1) {
                                deviceChild.setLockState(lockState);
                            }
                            if (screenState != -1) {
                                deviceChild.setScreenState(screenState);
                            }
                            if (curRunState2 != -1) {
                                deviceChild.setCurRunState2(curRunState2);
                            }
                            if (curRunState3 != -1) {
                                deviceChild.setCurRunState3(curRunState3);
                            }
                            if (timerHour != -1) {
                                deviceChild.setTimerHour(timerHour);
                            }
                            if (timerMin != -1) {
                                deviceChild.setTimerMin(timerMin);
                            }
                            if (checkCode != -1) {
                                deviceChild.setCheckCode(checkCode);
                            }
                            if (endCode != -1) {
                                deviceChild.setEndCode(endCode);
                            }
                            if (warmerFall!=-1){
                                deviceChild.setWarmerFall(warmerFall);
                            }
                            if (warmerFall==1){
                                falling=1;
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                                Intent notifyIntent = new Intent(getApplicationContext(), MainActivity.class);
                                long houseId=deviceChild.getHouseId();
                                notifyIntent.putExtra("houseId", houseId);

                                notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                PendingIntent notifyPendingIntent =
                                        PendingIntent.getActivity(getApplicationContext(), 0, notifyIntent,
                                                PendingIntent.FLAG_UPDATE_CURRENT);

                                builder.setContentText(deviceChild.getName() + "已倾倒")
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setDefaults(Notification.DEFAULT_ALL)
                                        .setAutoCancel(true);
                                builder.setContentIntent(notifyPendingIntent);

                                NotificationManager mNotificationManager =
                                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                mNotificationManager.notify(0, builder.build());
                            }else {
                                falling=0;
                            }
//                            deviceChild.setWarmerFall(wa);
                            int ss = deviceChild.getDeviceId();
                            int deviceUsedCount = deviceChild.getDeviceUsedCount();
                            deviceChild.setDeviceUsedCount(deviceUsedCount + 1);
                            deviceChild.setOnline(true);
                            long shareId2 = deviceChild.getShareId();
                            if (shareId2 == Long.MAX_VALUE) {
                                sharedId = Long.MAX_VALUE;
                            }
                            deviceChildDao.update(deviceChild);
                            Log.i("deviceChildDao", "-->" + deviceChild.getDeviceId());
                        }
                        break;
                    case 3:
                            if (messageJsonArray != null) {
                                int sensorSimpleTemp;/**传感器采样温度*/
                                int sensorSimpleHum;/**传感器采样湿度*/
                                int sorsorPm;/**PM2.5粉尘传感器数据*/
                                int sensorOx;/**氧浓度传感器数据*/
                                int sensorHcho;/**甲醛数据*/

                                busModel = messageJsonArray.getInt(3);
                                int mMcuVersion = messageJsonArray.getInt(4);
                                mcuVersion = "v" + mMcuVersion / 16 + "." + mMcuVersion % 16;
                                int mWifiVersion = messageJsonArray.getInt(5);
                                wifiVersion = "v" + mWifiVersion / 16 + "." + mWifiVersion % 16;
                                int sensorState = messageJsonArray.getInt(7);
                                sensorSimpleTemp = messageJsonArray.getInt(8)-128;
                                sensorSimpleHum = messageJsonArray.getInt(9) - 128;
                                sorsorPm = messageJsonArray.getInt(10) - 128;
                                sensorOx = messageJsonArray.getInt(11) - 128;
                                sensorHcho = messageJsonArray.getInt(12) - 128;

                                if (deviceChild != null) {
                                    deviceChild.setSensorState(sensorState);
                                    deviceChild.setBusModel(busModel);
                                    deviceChild.setMcuVersion(mcuVersion);
                                    deviceChild.setWifiVersion(wifiVersion);
                                    deviceChild.setSensorSimpleTemp(sensorSimpleTemp);
                                    deviceChild.setSensorSimpleHum(sensorSimpleHum);
                                    deviceChild.setSorsorPm(sorsorPm);
                                    deviceChild.setSensorOx(sensorOx);
                                    deviceChild.setSensorHcho(sensorHcho);
                                    deviceChild.setOnline(true);
                                    deviceChildDao.update(deviceChild);
                                }
                            }

                        break;
                    case 4:
                            if (messageJsonArray != null) {
                                int socketPower;/**插座功率*/
                                int socketTemp;/**插座温度*/
                                int socketState;/**插座当前状态*/
                                int socketTimer=0;/**插座定时模式*/
                                int socketTimerHour;/**定时模式的时*/
                                int socketTimerMin;/**定时模式的分*/
                                int socketCurrent;/**插座当前电流值*/
                                int socketVal;/**插座当前电压值*/
                                int socketPowerConsume;/**插座当前耗电量总度数*/
                                int isSocketTimerMode;/**定时模式是否开启*/
                                busModel = messageJsonArray.getInt(3);
                                int mMcuVersion = messageJsonArray.getInt(4);
                                mcuVersion = "v" + mMcuVersion / 16 + "." + mMcuVersion % 16;
                                int mWifiVersion = messageJsonArray.getInt(5);
                                wifiVersion = "v" + mWifiVersion / 16 + "." + mWifiVersion % 16;
                                int socketPowerHigh = messageJsonArray.getInt(7);
                                int socketPowerLow = messageJsonArray.getInt(8);

                                String power2=socketPowerHigh/256+socketPowerLow%256+"";
                                socketPower=Integer.parseInt(power2);
                                socketTemp = messageJsonArray.getInt(9)-128;
                                int state = messageJsonArray.getInt(10);
                                int x[] = TenTwoUtil.changeToTwo(state);
                                socketState = x[7];
                                isSocketTimerMode=x[6];


                                socketTimer = messageJsonArray.getInt(11);
                                socketTimerHour=messageJsonArray.getInt(12);
                                socketTimerMin=messageJsonArray.getInt(13);

                                int highCurrent = messageJsonArray.getInt(14);
                                int lowCurrent=messageJsonArray.getInt(15);
                                String socketCurrent2=""+highCurrent/256+lowCurrent%256;
                                socketCurrent=Integer.parseInt(socketCurrent2);
                                int highVal = messageJsonArray.getInt(16);
                                int lowVal=messageJsonArray.getInt(17);
                                String socketVal2=""+highVal/256 +lowVal%256;
                                socketVal=Integer.parseInt(socketVal2);

                                int highPowerConsume = messageJsonArray.getInt(18);
                                int lowPowerConsume=messageJsonArray.getInt(19);
                                String powerConsume2=""+highPowerConsume/256 +lowPowerConsume%256;
                                socketPowerConsume=Integer.parseInt(powerConsume2);
                                if (deviceChild != null) {
                                    deviceChild.setSocketPower(socketPower);
                                    deviceChild.setBusModel(busModel);
                                    deviceChild.setMcuVersion(mcuVersion);
                                    deviceChild.setWifiVersion(wifiVersion);
                                    deviceChild.setSocketTemp(socketTemp);
                                    deviceChild.setSocketState(socketState);
                                    deviceChild.setIsSocketTimerMode(isSocketTimerMode);
                                    deviceChild.setSocketTimer(socketTimer);
                                    deviceChild.setSocketTimerHour(socketTimerHour);
                                    deviceChild.setSocketTimerMin(socketTimerMin);
                                    deviceChild.setSocketCurrent(socketCurrent);
                                    deviceChild.setSocketVal(socketVal);
                                    deviceChild.setSocketPowerConsume(socketPowerConsume);
                                    deviceChild.setOnline(true);
                                    deviceChildDao.update(deviceChild);
                                }
                            }

                        break;
                    case 5:
                        //除湿机
                        if (!TextUtils.isEmpty(productType)) {

                        } else {
                            int dehumSetTemp;/**除湿机设定温度*/
                            int dehumSetHum;/**除湿机设定湿度*/
                            int dehumInnerTemp;//除湿机内盘管温度
                            int dehumOuterTemp;//除湿机外盘管温度

                            int dehumSleep;//除湿机睡眠模式 0关闭 1开启
                            int dehumAnion;//除湿机负离子模式 0关闭 1开启
                            int dehumDrying;//除湿机干衣模式 0关闭 1开启
                            int dehumDefrost;//除湿机除霜模式 0关闭 1开启
                            int sensorSimpleTemp;//温度采样数据
                            int sensorSimpleHum;//湿度采样数据
                            if (messageJsonArray != null) {
                                busModel = messageJsonArray.getInt(3);
                                int mMcuVersion = messageJsonArray.getInt(4);
                                mcuVersion = "v" + mMcuVersion / 16 + "." + mMcuVersion % 16;
                                int mWifiVersion = messageJsonArray.getInt(5);
                                wifiVersion = "v" + mWifiVersion / 16 + "." + mWifiVersion % 16;

                                warmerRunState = messageJsonArray.getInt(7);
                                curRunState2 = messageJsonArray.getInt(8);
                                curRunState3 = messageJsonArray.getInt(9);
                                int[] x = TenTwoUtil.changeToTwo(warmerRunState);
                                deviceState = x[7];
                                timerSwitch = x[6];
                                windLevel = x[5] + "" + x[4] + "" + x[3];
                                dehumSleep = x[2];
                                int[] x2 = TenTwoUtil.changeToTwo(curRunState2);
                                dehumAnion = x2[7];
                                dehumDrying = x2[6];
                                dehumDefrost = x2[5];
                                dehumSetHum = messageJsonArray.getInt(10);
                                dehumSetTemp = messageJsonArray.getInt(11);
                                timerMoudle = messageJsonArray.getInt(12);
                                timerHour= messageJsonArray.getInt(13);
                                timerMin= messageJsonArray.getInt(14);
                                sensorSimpleHum= messageJsonArray.getInt(15)-128;
                                sensorSimpleTemp= messageJsonArray.getInt(16)-128;
                                waterLevel=messageJsonArray.getInt(17)-128;
                                dehumInnerTemp = messageJsonArray.getInt(18)-128;
                                dehumOuterTemp = messageJsonArray.getInt(19)-128;
                                equipRatedPowerHigh = messageJsonArray.getInt(20);
                                equipRatedPowerLow = messageJsonArray.getInt(21);
                                equipCurdPowerHigh = messageJsonArray.getInt(22);
                                equipCurdPowerLow = messageJsonArray.getInt(23);

                                Log.e("qqqqPow",equipCurdPowerHigh+","+equipCurdPowerLow);
                                faultCode = messageJsonArray.getInt(24);
                                checkCode = messageJsonArray.getInt(25);
                                endCode = messageJsonArray.getInt(26);
                                if (deviceChild != null) {
                                    deviceChild.setBusModel(busModel);
                                    deviceChild.setMcuVersion(mcuVersion);
                                    deviceChild.setWifiVersion(wifiVersion);
                                    deviceChild.setCurRunState2(curRunState2);
                                    deviceChild.setCurRunState3(curRunState3);
                                    deviceChild.setDeviceState(deviceState);
                                    deviceChild.setTimerSwitch(timerSwitch);
                                    deviceChild.setWindLevel(windLevel);
                                    deviceChild.setDehumSleep(dehumSleep);
                                    deviceChild.setDehumAnion(dehumAnion);
                                    deviceChild.setDehumDrying(dehumDrying);
                                    deviceChild.setDehumDefrost(dehumDefrost);
                                    deviceChild.setDehumSetHum(dehumSetHum);
                                    deviceChild.setDehumSetTemp(dehumSetTemp);
                                    deviceChild.setTimerMoudle(timerMoudle);
                                    deviceChild.setTimerHour(timerHour);
                                    deviceChild.setTimerMin(timerMin);
                                    deviceChild.setSensorSimpleHum(sensorSimpleHum);
                                    deviceChild.setSensorSimpleTemp(sensorSimpleTemp);
                                    deviceChild.setWaterLevel(waterLevel);
                                    deviceChild.setDehumInnerTemp(dehumInnerTemp);
                                    deviceChild.setDehumOuterTemp(dehumOuterTemp);
                                    deviceChild.setEquipCurdPowerHigh(equipCurdPowerHigh);
                                    deviceChild.setEquipCurdPowerLow(equipCurdPowerLow);
                                    deviceChild.setEquipRatedPowerHigh(equipRatedPowerHigh);
                                    deviceChild.setEquipRatedPowerLow(equipRatedPowerLow);
                                    deviceChild.setFaultCode(faultCode);
                                    deviceChild.setCheckCode(checkCode);
                                    deviceChild.setEndCode(endCode);
                                    deviceChild.setOnline(true);
                                    deviceChildDao.update(deviceChild);
                                }else {
                                }
                            }
                        }
                        break;
                    case 6:
                        //空调
                        if (!TextUtils.isEmpty(productType)) {

                        } else {
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
                            int sensorSimpleHum;//传感器采样湿度

                            if (messageJsonArray != null) {
                                busModel = messageJsonArray.getInt(3);
                                int mMcuVersion = messageJsonArray.getInt(4);
                                mcuVersion = "v" + mMcuVersion / 16 + "." + mMcuVersion % 16;
                                int mWifiVersion = messageJsonArray.getInt(5);
                                wifiVersion = "v" + mWifiVersion / 16 + "." + mWifiVersion % 16;

                                warmerRunState = messageJsonArray.getInt(7);
                                curRunState2 = messageJsonArray.getInt(8);
                                curRunState3 = messageJsonArray.getInt(9);
                                int[] x = TenTwoUtil.changeToTwo(warmerRunState);
                                deviceState = x[7];
                                aCondState = x[6] + "" + x[5] + "" + x[4] ;
                                aCondSleep = x[3] ;
                                timerSwitch=x[2];
                                int[] x2 = TenTwoUtil.changeToTwo(curRunState2);
                                windLevel = x2[7] + "" + x2[6] + "" + x2[5];
                                aCondSUpDown = x2[4] ;
                                aCondSLeftRight=x2[3];
                                aCondSetTemp1 = messageJsonArray.getInt(10);
                                aCondSetTemp2 = messageJsonArray.getInt(11);
                                aCondSetData = messageJsonArray.getInt(12);
                                timerMoudle= messageJsonArray.getInt(13);
                                timerHour= messageJsonArray.getInt(14);
                                timerMin= messageJsonArray.getInt(15);
                                aCondSimpleTemp1= messageJsonArray.getInt(16)-128;
                                aCondSimpleTemp2= messageJsonArray.getInt(17)-128;
                                sensorSimpleHum= messageJsonArray.getInt(18)-128;
                                aCondInnerTemp= messageJsonArray.getInt(19)-128;
                                aCondOuterTemp= messageJsonArray.getInt(20)-128;
                                equipRatedPowerHigh= messageJsonArray.getInt(21);
                                equipRatedPowerLow= messageJsonArray.getInt(22);
                                equipCurdPowerHigh= messageJsonArray.getInt(23);
                                equipCurdPowerLow= messageJsonArray.getInt(24);
                                faultCode= messageJsonArray.getInt(25);
                                checkCode = messageJsonArray.getInt(26);
                                endCode = messageJsonArray.getInt(27);
                                if (deviceChild != null) {
                                    deviceChild.setBusModel(busModel);
                                    deviceChild.setMcuVersion(mcuVersion);
                                    deviceChild.setWifiVersion(wifiVersion);
                                    deviceChild.setACondState(aCondState);
                                    deviceChild.setACondSleep(aCondSleep);
                                    deviceChild.setCurRunState2(curRunState2);
                                    deviceChild.setCurRunState3(curRunState3);
                                    deviceChild.setWindLevel(windLevel);
                                    deviceChild.setACondSUpDown(aCondSUpDown);
                                    deviceChild.setACondSLeftRight(aCondSLeftRight);
                                    deviceChild.setACondSetTemp1(aCondSetTemp1);
                                    deviceChild.setACondSetTemp2(aCondSetTemp2);
                                    deviceChild.setACondSetData(aCondSetData);
                                    deviceChild.setACondSimpleTemp1(aCondSimpleTemp1);
                                    deviceChild.setACondSimpleTemp2(aCondSimpleTemp2);
                                    deviceChild.setSensorSimpleHum(sensorSimpleHum);
                                    deviceChild.setACondInnerTemp(aCondInnerTemp);
                                    deviceChild.setACondOuterTemp(aCondOuterTemp);
                                    deviceChild.setEquipRatedPowerHigh(equipRatedPowerHigh);
                                    deviceChild.setEquipRatedPowerLow(equipRatedPowerLow);
                                    deviceChild.setEquipCurdPowerHigh(equipCurdPowerHigh);
                                    deviceChild.setEquipCurdPowerLow(equipCurdPowerLow);
                                    deviceChild.setFaultCode(faultCode);
                                    deviceChild.setDeviceState(deviceState);
                                    deviceChild.setTimerSwitch(timerSwitch);
                                    deviceChild.setTimerMoudle(timerMoudle);
                                    deviceChild.setTimerHour(timerHour);
                                    deviceChild.setTimerMin(timerMin);
                                    deviceChild.setCheckCode(checkCode);
                                    deviceChild.setEndCode(endCode);
                                    deviceChild.setOnline(true);
                                    deviceChildDao.update(deviceChild);
                                }else {

                                }
                            }
                        }
                        break;
                    case 7:
                        //空气净化器
                        if (!TextUtils.isEmpty(productType)) {

                        } else {
                            String purifierState; //空气净化器状态
                            int sorsorPm,sensorSimpleTemp,sensorSimpleHum,sensorHcho;
                            if (messageJsonArray != null) {
                                busModel = messageJsonArray.getInt(3);
                                int mMcuVersion = messageJsonArray.getInt(4);
                                mcuVersion = "v" + mMcuVersion / 16 + "." + mMcuVersion % 16;
                                int mWifiVersion = messageJsonArray.getInt(5);
                                wifiVersion = "v" + mWifiVersion / 16 + "." + mWifiVersion % 16;

                                warmerRunState = messageJsonArray.getInt(7);
                                curRunState2 = messageJsonArray.getInt(8);
                                curRunState3 = messageJsonArray.getInt(9);
                                int[] x = TenTwoUtil.changeToTwo(warmerRunState);
                                deviceState = x[7];
                                rateState = x[6] + "" + x[5] + "" + x[4] ;
                                purifierState = x[3] + "" + x[2];
                                timerSwitch = x[1];
                                timerMoudle = messageJsonArray.getInt(10);
                                timerHour = messageJsonArray.getInt(11);
                                timerMin = messageJsonArray.getInt(12);
                                sorsorPm= messageJsonArray.getInt(13)-128;
                                sensorSimpleTemp= messageJsonArray.getInt(14)-128;
                                sensorSimpleHum= messageJsonArray.getInt(15)-128;
                                sensorHcho= messageJsonArray.getInt(16)-128;
                                checkCode = messageJsonArray.getInt(17);
                                endCode = messageJsonArray.getInt(18);
                                if (deviceChild != null) {
                                    deviceChild.setBusModel(busModel);
                                    deviceChild.setMcuVersion(mcuVersion);
                                    deviceChild.setWifiVersion(wifiVersion);
                                    deviceChild.setCurRunState2(curRunState2);
                                    deviceChild.setCurRunState3(curRunState3);
                                    deviceChild.setDeviceState(deviceState);
                                    deviceChild.setRateState(rateState);
                                    deviceChild.setPurifierState(purifierState);
                                    deviceChild.setTimerSwitch(timerSwitch);
                                    deviceChild.setTimerMoudle(timerMoudle);
                                    deviceChild.setTimerHour(timerHour);
                                    deviceChild.setTimerMin(timerMin);
                                    deviceChild.setSorsorPm(sorsorPm);
                                    deviceChild.setSensorSimpleTemp(sensorSimpleTemp);
                                    deviceChild.setSensorSimpleHum(sensorSimpleHum);
                                    deviceChild.setSensorHcho(sensorHcho);
                                    deviceChild.setCheckCode(checkCode);
                                    deviceChild.setEndCode(endCode);
                                    deviceChild.setOnline(true);
                                    deviceChildDao.update(deviceChild);
                                }else {

                                }
                            }
                        }
                        break;
                    case 8:
                        if (messageJsonArray!=null){
                            int wPurifierEndYear;/**净水器截止使用年*/
                            int wPurifierEndMonth;/**净水器截止使用月*/
                            int wPurifierEndDay;/**净水器截止使用日*/
                            int wPurifierEndFlow;/**净水器截止使用流量*/
                            String wPurifierState;/**净水器状态*/
                            int wPurifierFlowData;/**净水器流量数据*/
                            int wPurifierCurTemp;/**净水器当前温度*/
                            int wPurifierPrimaryQuqlity;/**净水器原生水质*/
                            int wPurifierOutQuqlity;/**净水器出水水质*/
                            /**净水器滤芯寿命 1-10*/
                            int wPurifierfilter1,wPurifierfilter2,wPurifierfilter3,wPurifierfilter4,wPurifierfilter5,wPurifierfilter6,wPurifierfilter7,wPurifierfilter8,wPurifierfilter9,wPurifierfilter10;
                            busModel = messageJsonArray.getInt(3);
                            int mMcuVersion = messageJsonArray.getInt(4);
                            mcuVersion = "v" + mMcuVersion / 16 + "." + mMcuVersion % 16;
                            int mWifiVersion = messageJsonArray.getInt(5);
                            wifiVersion = "v" + mWifiVersion / 16 + "." + mWifiVersion % 16;
                            int wPurifierEndYearHigh=messageJsonArray.getInt(7);
                            int wPurifierEndYeadLow=messageJsonArray.getInt(8);
                            String wPurifierEndYear2=""+wPurifierEndYearHigh/256 + wPurifierEndYeadLow%256;
                            wPurifierEndYear=Integer.parseInt(wPurifierEndYear2);
                            wPurifierEndMonth=messageJsonArray.getInt(9);
                            wPurifierEndDay=messageJsonArray.getInt(10);
                            int wPurifierEndFlowHigh=messageJsonArray.getInt(11);
                            int wPurifierEndFlowLow=messageJsonArray.getInt(12);
                            String wPurifierEndFlow2=""+wPurifierEndFlowHigh/256+wPurifierEndFlowLow%256;
                            wPurifierEndFlow=Integer.parseInt(wPurifierEndFlow2);
                            int state=messageJsonArray.getInt(13);
                            int []x=TenTwoUtil.changeToTwo(state);
                            wPurifierState=""+x[7]+x[6]+x[5];
                            int wPurifierFlowDataHigh=messageJsonArray.getInt(14);
                            int wPurifierFlowDataLow=messageJsonArray.getInt(15);
                            String wPurifierFlowData2=""+wPurifierFlowDataHigh/256+wPurifierFlowDataLow%256;
                            wPurifierFlowData=Integer.parseInt(wPurifierFlowData2);
                            wPurifierCurTemp=messageJsonArray.getInt(16)-128;
                            wPurifierPrimaryQuqlity=messageJsonArray.getInt(17);
                            wPurifierOutQuqlity=messageJsonArray.getInt(18);
                            wPurifierfilter1=messageJsonArray.getInt(19);
                            wPurifierfilter2=messageJsonArray.getInt(20);
                            wPurifierfilter3=messageJsonArray.getInt(21);
                            wPurifierfilter4=messageJsonArray.getInt(22);
                            wPurifierfilter5=messageJsonArray.getInt(23);
                            wPurifierfilter6=messageJsonArray.getInt(24);
                            wPurifierfilter7=messageJsonArray.getInt(25);
                            wPurifierfilter8=messageJsonArray.getInt(26);
                            wPurifierfilter9=messageJsonArray.getInt(27);
                            wPurifierfilter10=messageJsonArray.getInt(28);

                            if (deviceChild!=null){
                                deviceChild.setBusModel(busModel);
                                deviceChild.setWifiVersion(wifiVersion);
                                deviceChild.setMcuVersion(mcuVersion);
                                deviceChild.setWPurifierEndYear(wPurifierEndYear);
                                deviceChild.setWPurifierEndMonth(wPurifierEndMonth);
                                deviceChild.setWPurifierEndDay(wPurifierEndDay);
                                deviceChild.setWPurifierEndFlow(wPurifierEndFlow);
                                deviceChild.setWPurifierState(wPurifierState);
                                deviceChild.setWPurifierFlowData(wPurifierFlowData);
                                deviceChild.setWPurifierCurTemp(wPurifierCurTemp);
                                deviceChild.setWPurifierPrimaryQuqlity(wPurifierPrimaryQuqlity);
                                deviceChild.setWPurifierOutQuqlity(wPurifierOutQuqlity);
                                deviceChild.setWPurifierfilter1(wPurifierfilter1);
                                deviceChild.setWPurifierfilter2(wPurifierfilter2);
                                deviceChild.setWPurifierfilter3(wPurifierfilter3);
                                deviceChild.setWPurifierfilter4(wPurifierfilter4);
                                deviceChild.setWPurifierfilter5(wPurifierfilter5);
                                deviceChild.setWPurifierfilter6(wPurifierfilter6);
                                deviceChild.setWPurifierfilter7(wPurifierfilter7);
                                deviceChild.setWPurifierfilter8(wPurifierfilter8);
                                deviceChild.setWPurifierfilter9(wPurifierfilter9);
                                deviceChild.setWPurifierfilter10(wPurifierfilter10);
                                deviceChild.setOnline(true);
                                deviceChildDao.update(deviceChild);
                            }
                        }
                        break;
                }

                if (AddDeviceActivity.running || DeviceDetailActivity.running || MsgActivity.running
                        ||SmartTerminalActivity.running||SocketActivity.running|| PurifierActivity.running
                        ||ShareDeviceActivity.running||LiveActivity.running
                        ||TempChatActivity.running||APurifierActivity.running
                        || AConfActivity.running ||DehumidifierActivity.running
                        ||SmartLinkedActivity.running||QingLvFragment.running
                        ||QunzuAddActivity.running ||QunZuFragment.running){
                    falling=-1;
                }
                Log.i("FamilyFragmentManager", "-->" + FamilyFragmentManager.running);
                if (AddDeviceActivity.running) {
                    if (type != -1) {
                        Intent mqttIntent = new Intent("AddDeviceActivity");
                        mqttIntent.putExtra("type", type);
                        mqttIntent.putExtra("macAddress", macAddress);
                        sendBroadcast(mqttIntent);
                    }
                }else if (FamilyFragmentManager.running|| falling==1 || falling==0) {
                    Intent mqttIntent = new Intent("RoomFragment");
                    mqttIntent.putExtra("deviceChild", deviceChild);
                    mqttIntent.putExtra("macAddress", macAddress);
                    mqttIntent.putExtra("sharedId", sharedId);
                    sendBroadcast(mqttIntent);
                }  else if (DeviceDetailActivity.running ) {
                    Intent mqttIntent = new Intent("DeviceDetailActivity");
                    mqttIntent.putExtra("deviceChild", deviceChild);
                    mqttIntent.putExtra("macAddress", macAddress);
                    sendBroadcast(mqttIntent);
                } else if (MsgActivity.running) {
                    Intent mqttIntent = new Intent("Friend");
                    mqttIntent.putExtra("msg", message);
                    sendBroadcast(mqttIntent);
                } else if (SmartTerminalActivity.running) {
                    Intent mqttIntent = new Intent("SmartTerminalActivity");
                    mqttIntent.putExtra("deviceChild", deviceChild);
                    mqttIntent.putExtra("macAddress", macAddress);
                    sendBroadcast(mqttIntent);
                } else if (SocketActivity.running) {
                    Intent mqttIntent = new Intent("SocketActivity");
                    mqttIntent.putExtra("deviceChild", deviceChild);
                    mqttIntent.putExtra("macAddress", macAddress);
                    sendBroadcast(mqttIntent);
                }else if (PurifierActivity.running){
                    Intent mqttIntent = new Intent("PurifierActivity");
                    mqttIntent.putExtra("deviceChild", deviceChild);
                    mqttIntent.putExtra("macAddress", macAddress);
                    sendBroadcast(mqttIntent);
                }else if (ShareDeviceActivity.running) {
                    Intent mqttIntent = new Intent("ShareDeviceActivity");
                    mqttIntent.putExtra("deviceChild", deviceChild);
                    mqttIntent.putExtra("macAddress", macAddress);
                    sendBroadcast(mqttIntent);
                }else if (LiveActivity.running){
                    Intent mqttIntent = new Intent("LiveActivity");
                    mqttIntent.putExtra("deviceChild", deviceChild);
                    mqttIntent.putExtra("macAddress", macAddress);
                    sendBroadcast(mqttIntent);
                }else if (TempChatActivity.running){
                    Intent mqttIntent = new Intent("TempChatActivity");
                    mqttIntent.putExtra("deviceChild", deviceChild);
                    mqttIntent.putExtra("macAddress", macAddress);
                    sendBroadcast(mqttIntent);
                }
                else if (APurifierActivity.running){
                    Intent mqttIntent = new Intent("APurifierActivity");
                    mqttIntent.putExtra("deviceChild", deviceChild);
                    mqttIntent.putExtra("macAddress", macAddress);
                    sendBroadcast(mqttIntent);
                }
                else if (AConfActivity.running){
                    Intent mqttIntent = new Intent("AConfActivity");
                    mqttIntent.putExtra("deviceChild", deviceChild);
                    mqttIntent.putExtra("macAddress", macAddress);
                    sendBroadcast(mqttIntent);
                }
                else if (DehumidifierActivity.running){
                    Intent mqttIntent = new Intent("DehumidifierActivity");
                    mqttIntent.putExtra("deviceChild", deviceChild);
                    mqttIntent.putExtra("macAddress", macAddress);
                    sendBroadcast(mqttIntent);
                }else if (SmartLinkedActivity.running){
                    Intent mqttIntent = new Intent("SmartLinkedActivity");
                    mqttIntent.putExtra("deviceChild", deviceChild);
                    mqttIntent.putExtra("macAddress", macAddress);
                    sendBroadcast(mqttIntent);
                }
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                //创建通知建设类
                Notification.Builder builder = new Notification.Builder(getApplicationContext());
                //设置图标
                builder.setSmallIcon(R.mipmap.app);
                if (message.contains("senderRemark") && message.contains("senderAge") && message.contains("senderSex")) {
                    Gson gson = new Gson();
                    FriendData user = gson.fromJson(message, FriendData.class);
                    if (friendDataDao.findFriendBySendId(user.getSenderId()).size() == 0) {
                        friendDataDao.insert(user);
                        //设置跳转的页面
                        PendingIntent intent = PendingIntent.getActivity(getApplicationContext(),
                                100, new Intent(getApplicationContext(), MsgActivity.class),
                                PendingIntent.FLAG_CANCEL_CURRENT);
                        //设置跳转
                        builder.setContentIntent(intent);
                        //设置通知栏标题
                        builder.setContentTitle("新的好友请求");
                        //设置通知栏内容
                        builder.setContentText(user.getSenderName() + "请求加你为好友");
                        //设置
                        builder.setDefaults(Notification.DEFAULT_ALL);
                        //创建通知类
                        Notification notification = builder.build();
                        notification.flags = Notification.FLAG_AUTO_CANCEL;
                        //显示在通知栏
                        manager.notify(0, notification);
                    }
                }
                if (message.contains("您的好友请求")) {
                    if (message.contains(",")) {
                        String str[] = message.split(",");
                        MsgData msgData = new MsgData();
                        int state;
                        if (str[1].contains("同意"))
                            state = 5;
                        else
                            state = 6;
                        msgData.setUserName(str[0]);
                        msgData.setState(state);
                        msgData.setCreateTime(Long.parseLong(str[2]));
                        List<MsgData> msgDataList = msgDao.findMsgByTime(Long.parseLong(str[2]));
                        if (msgDataList.size() == 0)
                            msgDao.insert(msgData);
                    }
                }



                if (!TextUtils.isEmpty(macAddress) && macAddress.equals("clockuniversal")) {
                    SharedPreferences preferences = getSharedPreferences("password", MODE_PRIVATE);
                    String clockData = preferences.getString("clockData", "");
                    String[] clocks = clockData.split(",");
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("clockData", message);
                    Log.e("qqqqqHHHHH", message + "------" + clockData);
                    if (!message.equals(clockData)) {
                        SharedPreferences.Editor editor2 = preferences.edit();
                        editor2.putString("clockNew", "new");
                        editor2.commit();
                    }
                    editor.commit();
                    String[] newClocks = message.split(",");
                    ClockDaoImpl clockBeanDao = new ClockDaoImpl(getApplicationContext());
                    UserInfosDaoImpl userInfosDao = new UserInfosDaoImpl(getApplicationContext());
                    //判断新数据中有没有旧数据中不存在的，如果有则添加新订阅
                    Map<String, Integer> newMap = new HashMap<>();
                    for (int i = 0; i < clocks.length; i++) {
                        newMap.put(clocks[i], 1);
                    }
//                    isNew = false;
                    for (int j = 0; j < newClocks.length; j++) {
                        if (newMap.get(newClocks[j]) == null) {
                            String str = "p99/" + newClocks[j] + "/clockuniversal";
                            boolean success = subscribe(str, 1);
//                            isNew = true;

                        }

                    }

                    //判断旧的数据中有没有新数据中不存在的，如果有，取消订阅并删除数据
                    Map<String, Integer> map = new HashMap();
                    for (int i = 0; i < newClocks.length; i++) {
                        map.put(newClocks[i], 1);
                    }
                    for (int j = 0; j < clocks.length; j++) {
                        if (map.get(clocks[j]) == null) {
                            if(clocks[j].length()>0) {
                                String str = "p99/" + clocks[j] + "/clockuniversal";
                                unsubscribe(str);
                                List<ClockBean> findClock = clockBeanDao.findClockByClockId(Integer.parseInt(clocks[j].substring(2, clocks[j].length())));
                                if (findClock.size() > 0)
                                    clockBeanDao.delete(findClock.get(0));
                                List<UserInfo> findUser = userInfosDao.findUserInfoByClockId(Integer.parseInt(clocks[j].substring(2, clocks[j].length())));
                                for (int i = 0; i < findUser.size(); i++) {
                                    userInfosDao.delete(findUser.get(i));
                                }
                            }
                        }
                    }

                    if (QingLvFragment.running) {
                        Intent mqttIntent = new Intent("QingLvFragment");
                        mqttIntent.putExtra("msg", message);
                        sendBroadcast(mqttIntent);
                    }


                    Log.e("qqqqqRRRRR", QunZuFragment.running + "?");
                    if (QunZuFragment.running) {
                        Intent mqttIntent = new Intent("QunzuFragment");
                        mqttIntent.putExtra("msg", message);
                        sendBroadcast(mqttIntent);
                    }

                } else if (topicName.contains("clockuniversal")) {
                    boolean isShow = preferences.getBoolean("isClockPopShow", false);
                    Log.e("qqqqqIsShow", isShow + "???");
                    JSONObject jsonObject = new JSONObject(message);
                    JsonObject content = new JsonParser().parse(message).getAsJsonObject();
                    //添加闹钟
                    int state = jsonObject.getInt("state");
                    ClockDaoImpl clockBeanDao = new ClockDaoImpl(getApplicationContext());
                    UserInfosDaoImpl userInfosDao = new UserInfosDaoImpl(getApplicationContext());
                    ClockBean userList = new ClockBean();
                    userList.setFlag(jsonObject.getString("flag"));
                    int hour = jsonObject.getInt("clockHour");
                    int min = jsonObject.getInt("clockMinute");
                    userList.setClockHour(hour);
                    userList.setClockMinute(min);
                    userList.setSumMinute(hour * 60 + min);
                    userList.setClockCreater(Integer.parseInt(jsonObject.getString("clockCreater")));
                    userList.setClockDay(jsonObject.getString("clockDay"));
                    userList.setClockId(jsonObject.getInt("clockId"));
                    userList.setClockType(jsonObject.getInt("clockType"));
                    userList.setMusic(jsonObject.getString("music"));
                    userList.setSwitchs(jsonObject.getInt("switchs"));
                    userList.setCreaterName(jsonObject.getString("createrName"));
                    List<ClockBean> findClock = clockBeanDao.findClockByClockId(jsonObject.getInt("clockId"));
                    MsgData msgData = new MsgData();
                    msgData.setCreateTime(jsonObject.getLong("createTime"));
                    msgData.setUserName(jsonObject.getString("createrName"));
                    if (state == 2) {
                        if (findClock.size() == 0) {
                            if (userList.getClockType() == 3)
                                msgData.setState(1);
                            else
                                msgData.setState(11);
                            clockBeanDao.insert(userList);
                            JsonArray userInfos = content.getAsJsonArray("userInfos");
                            Gson gson = new Gson();
                            for (JsonElement userInfo : userInfos) {
                                UserInfo userInfo1 = gson.fromJson(userInfo, UserInfo.class);
                                userInfo1.setClockId(userList.getClockId());
                                userInfosDao.insert(userInfo1);
                            }
                        } else {
                            if (userList.getClockType() == 3)
                                msgData.setState(2);
                            else
                                msgData.setState(12);
                            ClockBean userList2 = findClock.get(0);
                            userList2.setSwitchs(jsonObject.getInt("switchs"));
                            userList2.setFlag(userList.getFlag());
                            userList2.setMusic(userList.getMusic());
                            userList2.setClockHour(userList.getClockHour());
                            userList2.setClockMinute(userList.getClockMinute());
                            clockBeanDao.update(userList2);
                        }
                        if (!(userList.getClockCreater() + "").equals(userId) && !isShow) {
                            dialogSign = userList.getClockType();
                            Message msg = Message.obtain();
                            msg.what = 1;   //标志消息的标志
                            handler.sendMessage(msg);
                        }
                    } else if (state == 1) {
                        if (userList.getClockType() == 3)
                            msgData.setState(1);
                        else
                            msgData.setState(11);
                        if (findClock.size() == 0) {
                            clockBeanDao.insert(userList);
                            JsonArray userInfos = content.getAsJsonArray("userInfos");
                            Gson gson = new Gson();
                            for (JsonElement userInfo : userInfos) {
                                UserInfo userInfo1 = gson.fromJson(userInfo, UserInfo.class);
                                userInfo1.setClockId(userList.getClockId());
                                userInfosDao.insert(userInfo1);
                            }
                            if (!(userList.getClockCreater() + "").equals(userId) && !isShow) {
                                dialogSign = userList.getClockType();
                                Message msg = Message.obtain();
                                msg.what = 1;   //标志消息的标志
                                handler.sendMessage(msg);
                            }
                        }
                    } else if (state == 3) {
                        if (userList.getClockType() == 3)
                            msgData.setState(3);
                        else
                            msgData.setState(13);
                        if (findClock.size() != 0) {
                            clockBeanDao.delete(findClock.get(0));
                        }
                    } else if (state == 4) {
                        List<UserInfo> findUser = userInfosDao.findUserInfoByClockId(userList.getClockId());
                        if (findUser.size() > 0) {
                            for (int i = 0; i < findUser.size(); i++) {
                                userInfosDao.delete(findUser.get(i));
                            }
                        }
                        boolean isAdd = false;
                        JsonArray userInfos = content.getAsJsonArray("userInfos");
                        Gson gson = new Gson();
                        for (JsonElement userInfo : userInfos) {
                            UserInfo userInfo1 = gson.fromJson(userInfo, UserInfo.class);
                            if (userId.equals(userInfo1.getUserId() + "")) {
                                isAdd = true;
                            }
                        }

                        if (findClock.size() == 0) {
                            if (isAdd) {
                                if (userList.getClockType() == 3)
                                    msgData.setState(1);
                                else
                                    msgData.setState(11);
                                clockBeanDao.insert(userList);
                                for (JsonElement userInfo : userInfos) {
                                    UserInfo userInfo1 = gson.fromJson(userInfo, UserInfo.class);
                                    userInfo1.setClockId(userList.getClockId());
                                    userInfosDao.insert(userInfo1);
                                }
                                if (!(userList.getClockCreater() + "").equals(userId) && !isShow) {
                                    dialogSign = userList.getClockType();
                                    Message msg = Message.obtain();
                                    msg.what = 1;   //标志消息的标志
                                    handler.sendMessage(msg);
                                }
                            }
                        } else {
                            ClockBean userList2 = findClock.get(0);
                            if (isAdd) {
                                if (userList.getClockType() == 3)
                                    msgData.setState(2);
                                else
                                    msgData.setState(12);
                                userList2.setSwitchs(jsonObject.getInt("switchs"));
                                userList2.setFlag(userList.getFlag());
                                userList2.setMusic(userList.getMusic());
                                userList2.setClockHour(userList.getClockHour());
                                userList2.setClockMinute(userList.getClockMinute());
                                clockBeanDao.update(userList2);
                                for (JsonElement userInfo : userInfos) {
                                    UserInfo userInfo1 = gson.fromJson(userInfo, UserInfo.class);
                                    userInfo1.setClockId(userList.getClockId());
                                    userInfosDao.insert(userInfo1);
                                }
                                if (!(userList.getClockCreater() + "").equals(userId) && !isShow) {
                                    dialogSign = userList.getClockType();
                                    Message msg = Message.obtain();
                                    msg.what = 1;   //标志消息的标志
                                    handler.sendMessage(msg);
                                }
                            } else {
                                clockBeanDao.delete(userList2);
                            }
                        }
                    }


                    if (userList.getClockType() == 3 && QingLvFragment.running) {
                        Intent mqttIntent = new Intent("QingLvFragment");
                        mqttIntent.putExtra("msg", message);
                        sendBroadcast(mqttIntent);
                    }
                    if (QunzuAddActivity.running) {
                        Intent mqttIntent = new Intent("QunzuAddActivity");
                        mqttIntent.putExtra("msg", message);
                        sendBroadcast(mqttIntent);
                    }
                    if (userList.getClockType() == 2 && QunZuFragment.running) {
                        Intent mqttIntent = new Intent("QunzuFragment");
                        mqttIntent.putExtra("msg", message);
                        sendBroadcast(mqttIntent);
                    }

                    if (!userId.equals(jsonObject.getString("clockCreater"))) {
                        List<MsgData> msgDataList2 = msgDao.findMsgByTime(msgData.getCreateTime());
                        if (msgDataList2.size() == 0) {
                            msgDao.insert(msgData);
                        }
                        SharedPreferences preferences = getSharedPreferences("password", MODE_PRIVATE);
                        String str = preferences.getString("clockNew", "");
                        if ("new".equals(str)) {
                            int msgState = msgData.getState();
                            if (msgState == 1 || msgState == 2) {
                                //设置跳转的页面
                                Intent dataIntent = new Intent(getApplicationContext(), ClockActivity.class);
                                dataIntent.putExtra("type", "MQService");
                                PendingIntent intent = PendingIntent.getActivity(getApplicationContext(),
                                        100, dataIntent,
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                                //设置跳转
                                builder.setContentIntent(intent);
                                //设置通知栏标题
                                builder.setContentTitle("新的闹钟消息");

                                //设置通知栏内容
                                String title = "";
                                if (userList.getClockType() == 3) {
                                    if (msgState == 1)
                                        title = "创建了新的情侣闹钟";
                                    else
                                        title = "修改了情侣闹钟";
                                } else if (userList.getClockType() == 2) {
                                    if (msgState == 1)
                                        title = "创建了新的群组闹钟";
                                    else
                                        title = "修改了群组闹钟";
                                }
                                builder.setContentText(msgData.getUserName() + title);
                                //设置
                                builder.setDefaults(Notification.DEFAULT_ALL);
                                //创建通知类
                                Notification notification = builder.build();
                                notification.flags = Notification.FLAG_AUTO_CANCEL;
                                //显示在通知栏
                                manager.notify(0, notification);
                            }
                            if (msgData.getState() == 3) {
                                //设置跳转的页面
                                PendingIntent intent = PendingIntent.getActivity(getApplicationContext(),
                                        100, new Intent(getApplicationContext(), ClockActivity.class),
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                                //设置跳转
                                builder.setContentIntent(intent);
                                //设置通知栏标题
                                builder.setContentTitle("新的闹钟消息");
                                //设置通知栏内容
                                String title = "";
                                if (userList.getClockType() == 3)
                                    title = "删除了情侣闹钟";
                                else if (userList.getClockType() == 2)
                                    title = "删除了群组闹钟";
                                builder.setContentText(msgData.getUserName() + title);
                                //设置
                                builder.setDefaults(Notification.DEFAULT_ALL);
                                //创建通知类
                                Notification notification = builder.build();
                                notification.flags = Notification.FLAG_AUTO_CANCEL;
                                //显示在通知栏
                                manager.notify(0, notification);
                            }
                        }
                    }
                }

                //标记  运行控件
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 获得订阅MQTT的所有主题
     */
    String userId;
    String phone;
    SharedPreferences preferences;

    public List<String> getTopicNames() {
        List<String> list = new ArrayList<>();
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        userId = preferences.getString("userId", "");
        String userName = preferences.getString("username", "");
        String friendTopic = "p99/+/acceptorId_" + userId + "/friend";
        phone = preferences.getString("phone", "");
        list.add("p99/" + phone + "/login");
        List<DeviceChild> deviceChildren = deviceChildDao.findAllDevice();
        for (DeviceChild deviceChild : deviceChildren) {
            String macAddress = deviceChild.getMacAddress();
            int type = deviceChild.getType();
            String onlineTopicName = "";
            String offlineTopicName = "";
            switch (type) {
                case 2:
                    onlineTopicName = "p99/warmer1/" + macAddress + "/transfer";
                    offlineTopicName = "p99/warmer1/" + macAddress + "/lwt";
                    list.add(onlineTopicName);
                    list.add(offlineTopicName);
                    break;
                case 3:
                    onlineTopicName = "p99/sensor1/" + macAddress + "/transfer";
                    offlineTopicName = "p99/sensor1/" + macAddress + "/lwt";
                    list.add(onlineTopicName);
                    list.add(offlineTopicName);
                case 4:
                    onlineTopicName = "p99/socket1/" + macAddress + "/transfer";
                    offlineTopicName = "p99/socket1/" + macAddress + "/lwt";
                    list.add(onlineTopicName);
                    list.add(offlineTopicName);
                    break;
                //jjjjjjjjjjjjjjjjj
                case 5:
                    onlineTopicName = "p99/dehumidifier1/" + macAddress + "/transfer";
                    offlineTopicName = "p99/dehumidifier1/" + macAddress + "/lwt";
                    list.add(onlineTopicName);
                    list.add(offlineTopicName);
                case 6:
                    onlineTopicName = "p99/aConditioning1/" + macAddress + "/transfer";
                    offlineTopicName = "p99/aConditioning1/" + macAddress + "/lwt";
                    list.add(onlineTopicName);
                    list.add(offlineTopicName);
                case 7:
                    onlineTopicName = "p99/aPurifier1/" + macAddress + "/transfer";
                    offlineTopicName = "p99/aPurifier1/" + macAddress + "/lwt";
                    list.add(onlineTopicName);
                    list.add(offlineTopicName);
                    break;
                case 8:
                    onlineTopicName = "p99/wPurifier1/" + macAddress + "/transfer";
                    offlineTopicName = "p99/wPurifier1/" + macAddress + "/lwt";
                    list.add(onlineTopicName);
                    list.add(offlineTopicName);
                    break;
            }
        }
        if (!LoginActivity.running){
            String friendReplayTopic = "p99/+/acceptorId_" + userId + "/friendReplay";
            String clockTopic = "p99/clockuniversal/userId_" + userId;
            list.add(friendTopic);
            list.add(clockTopic);
            list.add(friendReplayTopic);
            Log.e("qqqqqCCC", friendTopic);
        }
        return list;
    }

    /**
     * 重新连接MQTT
     */
    private void startReconnect() {

        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                if (!client.isConnected()) {
                    connect();
                }
            }
        }, 0 * 1000, 1 * 1000, TimeUnit.MILLISECONDS);
    }

    public static boolean isGoodJson(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            System.out.println("bad json: " + json);
            return false;
        }
    }

    /**
     * 发送MQTT主题
     */

    public boolean publish(String topicName, int qos, String payload) {
        boolean flag = false;
        if (client != null && client.isConnected()) {
            try {
                MqttMessage message = new MqttMessage(payload.getBytes("utf-8"));
                qos = 1;
                //设置保留消息
                if (topicName.contains("friend")) {
                    message.setRetained(true);
                }
                if (topicName.contains("clockuniversal")) {
                    message.setRetained(true);
                }
                message.setQos(qos);
                client.publish(topicName, message);
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 订阅MQTT主题
     */
    public boolean subscribe(String topicName, int qos) {
        boolean flag = false;
        if (client != null && client.isConnected()) {
            try {
                client.subscribe(topicName, qos);
                flag = true;
            } catch (MqttException e) {
            }
        }
        return flag;
    }

    public void updateDevice(DeviceChild deviceChild) {
        deviceChildDao.update(deviceChild);
        Log.i("deviceChild2", "-->" + deviceChild.getDeviceId());
    }


    //取消订阅
    public void unsubscribe(String topicName) {
        if (client != null && client.isConnected()) {
            try {
                client.unsubscribe(topicName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 取消所有的订阅
     */
    public void cancelAllsubscibe() {
        List<String> list = getTopicNames();
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            unsubscribe(s);
        }
    }

    int dialogSign = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    showDialog();
                    break;
                case 2:
                    // 获取电源管理器对象
                    PowerManager pm = (PowerManager) MyApplication.getContext()
                            .getSystemService(Context.POWER_SERVICE);
                    boolean screenOn = pm.isScreenOn();
                    if (!screenOn) {
                        // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
                        PowerManager.WakeLock wl = pm.newWakeLock(
                                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
                        wl.acquire(10000); // 点亮屏幕
                        wl.release(); // 释放
                    }
                    if (!LoginActivity.running){
                        Intent notifyIntent = new Intent(MQService.this, LoginActivity.class);
                        Toast.makeText(MQService.this,"该账号已在其他设备上登录",Toast.LENGTH_SHORT).show();
                        startActivity(notifyIntent);
                    }
                    break;
            }
        }
    };

    btClockjsDialog5 dialog4;

    boolean isFinish = false;

    private void showDialog() {
        if (isFinish) {
            dialog4 = new btClockjsDialog5(MQService.this, dialogSign);
            dialog4.setOnNegativeClickListener(new btClockjsDialog5.OnNegativeClickListener() {
                @Override
                public void onNegativeClick() {
//                dialog.dismiss();
                }
            });
            dialog4.setOnPositiveClickListener(new btClockjsDialog5.OnPositiveClickListener() {
                @Override
                public void onPositiveClick() {

                }
            });

            dialog4.setCanceledOnTouchOutside(false);
            dialog4.setCancelable(false);
            dialog4.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog4.show();
        }


    }


    private class getClockAsync extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... macs) {
            String macAddress = macs[0];
            String topicName = "p99/" + macAddress + "/clockuniversal";
            boolean success = subscribe(topicName, 1);
            return null;
        }
    }


//    class CountTimer extends CountDownTimer {
//        public CountTimer(long millisInFuture, long countDownInterval) {
//            super(millisInFuture, countDownInterval);
//        }
//
//        /**
//         * 倒计时过程中调用
//         *
//         * @param millisUntilFinished
//         */
//        @Override
//        public void onTick(long millisUntilFinished) {
//
//            Log.e("Tag", "倒计时=" + (millisUntilFinished / 1000));
//        }
//
//        /**
//         * 倒计时完成后调用
//         */
//
//        @Override
//        public void onFinish() {
//            Log.e("Tag", "倒计时完成");
//            isFinish = true;
//
//
//        }
//    }

}
