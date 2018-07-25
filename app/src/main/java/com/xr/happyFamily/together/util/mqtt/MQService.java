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
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.xr.database.dao.daoimpl.ClockDaoImpl;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.database.dao.daoimpl.FriendDataDaoImpl;
import com.xr.database.dao.daoimpl.MsgDaoImpl;
import com.xr.database.dao.daoimpl.UserInfosDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.activity.AddDeviceActivity;
import com.xr.happyFamily.jia.activity.DeviceDetailActivity;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.le.BtClock.RingReceiver;
import com.xr.happyFamily.le.ClockActivity;
import com.xr.happyFamily.le.bean.MsgFriendBean;
import com.xr.happyFamily.le.clock.MsgActivity;
import com.xr.happyFamily.le.fragment.QingLvFragment;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.FriendData;
import com.xr.happyFamily.le.pojo.MsgData;
import com.xr.happyFamily.le.pojo.UserInfo;
import com.xr.happyFamily.le.view.btClockjsDialog4;
import com.xr.happyFamily.le.view.btClockjsDialog5;
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
    /**
     * 测试的macAddrsss
     */
    private DeviceChildDaoImpl deviceChildDao;
    private FriendDataDaoImpl friendDataDao;
    private MsgDaoImpl msgDao;

    private Context mContext = this;
    boolean isNew = false;
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
        deviceChildDao = new DeviceChildDaoImpl(this);
        friendDataDao = new FriendDataDaoImpl(this);
        msgDao = new MsgDaoImpl(this);
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

    /**
     * 加载MQTT返回的消息
     */
    class LoadAsyncTask extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... strings) {

            String topicName = strings[0];/**收到的主题*/
            Log.i("topicName", "-->:" + topicName);
            String macAddress = null;
            if (topicName.startsWith("p99/warmer")) {
                macAddress = topicName.substring(11, topicName.lastIndexOf("/"));
            } else if (topicName.startsWith("p99/sensor1")) {
                macAddress = topicName.substring(12, topicName.lastIndexOf("/"));
            } else if (topicName.startsWith("p99")) {
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
            int checkCode = -1;/**校验码*/
            int endCode = -1;/**结束码*/
            String message = strings[1];/**收到的消息*/
            long sharedId = -1;/**分享的设备*/

            Log.i("mmm", "-->" + message);

            Log.i("mmm222", "-->" + macAddress);

            DeviceChild deviceChild = null;
            JSONObject messageJsonObject = null;
            String productType = null;
            JSONArray messageJsonArray = null;
            if (AddDeviceActivity.running) {

            } else {
                List<DeviceChild> deviceChildren = deviceChildDao.findAllDevice();
                for (DeviceChild deviceChild2 : deviceChildren) {
                    if (macAddress.equals(deviceChild2.getMacAddress())) {
                        deviceChild = deviceChild2;
                        break;
                    }
                }
            }
            try {

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
                } else if (topicName.contains("acceptorId_") && topicName.contains("friend")) {

                } else if (macAddress.equals("clockuniversal")) {

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
                        if (!TextUtils.isEmpty(productType)) {

                        } else {
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
                            int ss = deviceChild.getDeviceId();
                            Log.i("ssssss", "-->" + ss);
                            Log.i("warmerCurTemp2222", "-->" + deviceChild.getWarmerCurTemp());
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
                        if (!TextUtils.isEmpty(productType)) {

                        } else {
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
                                sensorSimpleTemp = messageJsonArray.getInt(8) - 128;
                                sensorSimpleHum = messageJsonArray.getInt(9) - 128;
                                sorsorPm = messageJsonArray.getInt(10) - 128;
                                sensorOx = messageJsonArray.getInt(11) - 128;
                                sensorHcho = messageJsonArray.getInt(12) - 128;

                                deviceChild.setSensorState(sensorState);
                                deviceChild.setBusModel(busModel);
                                deviceChild.setMcuVersion(mcuVersion);
                                deviceChild.setWifiVersion(wifiVersion);
                                deviceChild.setSensorSimpleTemp(sensorSimpleTemp);
                                deviceChild.setSensorSimpleHum(sensorSimpleHum);
                                deviceChild.setSorsorPm(sorsorPm);
                                deviceChild.setSensorOx(sensorOx);
                                deviceChild.setSensorHcho(sensorHcho);
                                if (screenState == 4) {
                                    deviceChild.setOnline(false);
                                } else {
                                    deviceChild.setOnline(true);
                                }
                                deviceChildDao.update(deviceChild);
                            }
                        }
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        break;
                }
                Log.i("FamilyFragmentManager", "-->" + FamilyFragmentManager.running);
                if (AddDeviceActivity.running) {
                    if (type != -1) {
                        Intent mqttIntent = new Intent("AddDeviceActivity");
                        mqttIntent.putExtra("type", type);
                        mqttIntent.putExtra("macAddress", macAddress);
                        sendBroadcast(mqttIntent);
                    }
                } else if (DeviceDetailActivity.running) {
                    Intent mqttIntent = new Intent("DeviceDetailActivity");
                    mqttIntent.putExtra("deviceChild", deviceChild);
                    mqttIntent.putExtra("macAddress", macAddress);
                    sendBroadcast(mqttIntent);
                } else if (FamilyFragmentManager.running) {
                    Intent mqttIntent = new Intent("RoomFragment");
                    mqttIntent.putExtra("deviceChild", deviceChild);
                    mqttIntent.putExtra("macAddress", macAddress);
                    mqttIntent.putExtra("sharedId", sharedId);
                    sendBroadcast(mqttIntent);
                } else if (MsgActivity.running) {
                    Intent mqttIntent = new Intent("Friend");
                    mqttIntent.putExtra("msg", message);
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
                if (message.contains("您的好友请求")) {
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
                    msgDao.insert(msgData);
                }

                Log.e("qqqqqqqqqqWWW", macAddress + "," + topicName);

                if (macAddress.equals("clockuniversal")) {
                    SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
                    String clockData = preferences.getString("clockData", "");
                    String[] clocks = clockData.split(",");
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("clockData", message);
                    Log.e("qqqqqHHHHH",message+"------"+clockData);
                    if(!message.equals(clockData)){
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
                    isNew=false;
                    for (int j = 0; j < newClocks.length; j++) {
                        if (newMap.get(newClocks[j]) == null) {
                            String str = "p99/" + newClocks[j] + "/clockuniversal";
                            boolean success = subscribe(str, 1);
                            isNew=true;
                            Log.e("qqqqqqqqqqWWW1111", str + "," + success+"????"+isNew);



                        }

                    }

                    //判断旧的数据中有没有新数据中不存在的，如果有，取消订阅并删除数据
                    Map<String, Integer> map = new HashMap();
                    for (int i = 0; i < newClocks.length; i++) {
                        map.put(newClocks[i], 1);
                    }
                    for (int j = 0; j < clocks.length; j++) {
                        if (map.get(clocks[j]) == null) {
                            String str = "p99/" + clocks[j] + "/clockuniversal";

                            unsubscribe(str);
                            List<ClockBean> findClock = clockBeanDao.findClockByClockId(Integer.parseInt(clocks[j].substring(2, clocks[j].length())));
                            if (findClock.size()>0)
                            clockBeanDao.delete(findClock.get(0));
                            List<UserInfo> findUser = userInfosDao.findUserInfoByClockId(Integer.parseInt(clocks[j].substring(2, clocks[j].length())));
                            for (int i = 0; i < findUser.size(); i++) {
                                userInfosDao.delete(findUser.get(i));
                            }
                        }
                    }

                    if (QingLvFragment.running) {
                        Intent mqttIntent = new Intent("QingLvFragment");
                        mqttIntent.putExtra("msg", message);
                        sendBroadcast(mqttIntent);
                    }

                } else if (topicName.contains("clockuniversal")) {
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
                            msgData.setState(1);
                            clockBeanDao.insert(userList);
                            JsonArray userInfos = content.getAsJsonArray("userInfos");
                            Gson gson = new Gson();
                            for (JsonElement userInfo : userInfos) {
                                UserInfo userInfo1 = gson.fromJson(userInfo, UserInfo.class);
                                userInfo1.setClockId(userList.getClockId());
                                userInfosDao.insert(userInfo1);
                            }
                        } else {
                            msgData.setState(2);
                            ClockBean userList2 = findClock.get(0);
                            userList2.setSwitchs(jsonObject.getInt("switchs"));
                            userList2.setFlag(userList.getFlag());
                            userList2.setMusic(userList.getMusic());
                            userList2.setClockHour(userList.getClockHour());
                            userList2.setClockMinute(userList.getClockMinute());
                            clockBeanDao.update(userList2);
                            Message msg =Message.obtain();
                            msg.what=1;   //标志消息的标志
                            handler.sendMessage(msg);
                        }

                        if (isNew) {
                            SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
//                            String str = preferences.getString("clockNew", "");
//                            if("new".equals(str)) {
                                if (!(userList.getClockCreater() + "").equals(userId)) {
                                    if (!QingLvFragment.running)
                                        gotoClockActivity();
//                                }
                            }
                        }
                    } else if (state == 1) {
                        msgData.setState(1);
                        Log.e("qqqqqqqqqqWWW2222", isNew + "???");
                        clockBeanDao.insert(userList);
                        JsonArray userInfos = content.getAsJsonArray("userInfos");
                        Gson gson = new Gson();
                        for (JsonElement userInfo : userInfos) {
                            UserInfo userInfo1 = gson.fromJson(userInfo, UserInfo.class);
                            userInfo1.setClockId(userList.getClockId());
                            userInfosDao.insert(userInfo1);
                        }
                        if (isNew) {
                            SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
//                            String str = preferences.getString("clockNew", "");
//                            if("new".equals(str)) {
                                if (!(userList.getClockCreater() + "").equals(userId)) {
                                    if (!QingLvFragment.running)
                                        gotoClockActivity();
                                }
//                            }
                        }


                    } else if (state == 3) {
                        msgData.setState(3);
                        if (findClock.size() != 0) {
                            clockBeanDao.delete(findClock.get(0));
                        }
                    } else if (state == 4) {
                        List<UserInfo> findUser = userInfosDao.findUserInfoByClockId(userList.getClockId());
                        if (findClock.size() == 0) {
                            boolean isAdd = false;
                            JsonArray userInfos = content.getAsJsonArray("userInfos");
                            Gson gson = new Gson();
                            for (JsonElement userInfo : userInfos) {
                                UserInfo userInfo1 = gson.fromJson(userInfo, UserInfo.class);
                                Log.e("qqqqAAAA", userId + "????" + userInfo1.getUserId() + "");
                                if (userId.equals(userInfo1.getUserId() + "")) {
                                    isAdd = true;
                                }
                            }

                            Log.e("qqqqAAAA", isAdd + "????");
                            if (isAdd) {
                                msgData.setState(1);
                                List<ClockBean> findLish=clockBeanDao.findClockByClockId(userList.getClockId());
                                if(findLish.size()==0) {
                                    clockBeanDao.insert(userList);
                                    for (JsonElement userInfo : userInfos) {
                                        UserInfo userInfo1 = gson.fromJson(userInfo, UserInfo.class);
                                        userInfo1.setClockId(userList.getClockId());
                                        userInfosDao.insert(userInfo1);
                                    }
                                }
                            }
                        } else {
                            for (int i = 0; i < findUser.size(); i++) {
                                userInfosDao.delete(findUser.get(i));
                            }
                            JsonArray userInfos = content.getAsJsonArray("userInfos");
                            Gson gson = new Gson();
                            for (JsonElement userInfo : userInfos) {
                                UserInfo userInfo1 = gson.fromJson(userInfo, UserInfo.class);
                                userInfo1.setClockId(userList.getClockId());
                                userInfosDao.insert(userInfo1);
                            }
                        }
                    }


                    Log.e("qqqqqZZZZ", clockBeanDao.findAll().size() + "??");
                    if (QingLvFragment.running) {
                        Intent mqttIntent = new Intent("QingLvFragment");
                        mqttIntent.putExtra("msg", message);
                        sendBroadcast(mqttIntent);
                    }

//                    if (ClockActivity.running) {
//                        Log.e("qqqqqqqqqqWWW1111", "3333333");
//                        gotoClockActivity();
//                    }
                    List<MsgData> msgDataList = msgDao.findMsgByTime();


                    Log.e("qqqqqqqqqMMMM", msgDataList.size() + "??");
                    if (!userId.equals(jsonObject.getString("clockCreater"))) {
                        msgDao.insert(msgData);
                        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
                        String str = preferences.getString("clockNew", "");
                        if("new".equals(str)) {
                        if (msgData.getState() == 1 || msgData.getState() == 4) {
                            //设置跳转的页面
                            PendingIntent intent = PendingIntent.getActivity(getApplicationContext(),
                                    100, new Intent(getApplicationContext(), ClockActivity.class),
                                    PendingIntent.FLAG_CANCEL_CURRENT);
                            //设置跳转
                            builder.setContentIntent(intent);
                            //设置通知栏标题
                            builder.setContentTitle("新的闹钟消息");
                            //设置通知栏内容
                            builder.setContentText(msgData.getUserName() + "创建了新的情侣闹钟");
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
                                builder.setContentText(msgData.getUserName() + "删除了情侣闹钟");
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

    public List<String> getTopicNames() {
        List<String> list = new ArrayList<>();
        SharedPreferences preferences;
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        userId = preferences.getString("userId", "");
        String userName = preferences.getString("username", "");
        String friendTopic = "p99/+/acceptorId_" + userId + "/friend";
        String friendReplayTopic = "p99/+/acceptorId_" + userId + "/friendReplay";
        String clockTopic = "p99/clockuniversal/userId_" + userId;
        Log.e("qqqqqCCC", friendTopic);
        List<DeviceChild> deviceChildren = deviceChildDao.findAllDevice();
        for (DeviceChild deviceChild : deviceChildren) {
            String macAddress = deviceChild.getMacAddress();
            int type = deviceChild.getType();
            String onlineTopicName = "";
            String offlineTopicName = "";
            switch (type) {
                case 2:
                    onlineTopicName = "p99/warmer/" + macAddress + "/transfer";
                    offlineTopicName = "p99/warmer/" + macAddress + "/lwt";
                    list.add(onlineTopicName);
                    list.add(offlineTopicName);
                    break;
                case 3:
                    onlineTopicName = "p99/sensor1/" + macAddress + "/transfer";
                    offlineTopicName = "p99/sensor1/" + macAddress + "/lwt";
                    list.add(onlineTopicName);
                    list.add(offlineTopicName);
                    break;
            }
        }
        list.add(friendTopic);
        list.add(clockTopic);
        list.add(friendReplayTopic);
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



    public void gotoClockActivity(){
        Intent intent = new Intent(mContext, ClockActivity.class);
        intent.putExtra("type","MQServer");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

  Handler handler= new Handler(){
      @Override
      public void handleMessage(Message msg) {
          super.handleMessage(msg);
          switch (msg.what){
              case  1:
                  showDialog();
                  break;
          }
      }
  };

    btClockjsDialog5 dialog4;

    private void showDialog() {
        dialog4 = new btClockjsDialog5(this);
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
