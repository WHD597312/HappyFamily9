package com.xr.happyFamily.together.util.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.jia.activity.AddDeviceActivity;
import com.xr.happyFamily.jia.activity.DeviceDetailActivity;
import com.xr.happyFamily.jia.pojo.DeviceChild;
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
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MQService extends Service {

    private String TAG = "MQService";
    private String host = "tcp://47.98.131.11:1883";
    private String userName = "admin";
    private String passWord = "Xr7891122";
    String macAddress="hrrj7895ccf7f6c9fa4";/**测试的macAddrsss*/
    private DeviceChildDaoImpl deviceChildDao;

    /***
     * 模块类型
     */
    private int[] moduleType={0X01,0X02,0X03,0X04,0X05,0X06,0X07,0X08};
    /***
     * 商业模块
     */
    private int [] bussinessmodule={0XF0,0};



    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private MqttClient client;
    private MqttConnectOptions options;
    String clientId;
    private LocalBinder binder = new LocalBinder();

    /**
     * 服务启动之后就初始化MQTT,连接MQTT
     * */
    @Override
    public void onCreate() {
        super.onCreate();
        clientId=UUID.getUUID(this);
        deviceChildDao = new DeviceChildDaoImpl(this);
        init();
        connect();
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
        return super.onStartCommand(intent, flags, startId);
    }

    /**销毁服务，则断开MQTT,释放资源*/
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
                        new LoadAsyncTask().execute(topicName,message.toString());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        new ConAsync().execute();
    }
    /**
     * 连接MQTT
     * */
    class ConAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                client.connect(options);
                List<String> topicNames = getTopicNames();
                if (!topicNames.isEmpty()) {
                    for (String topicName : topicNames) {
                        client.subscribe(topicName, 1);
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
     * */
    class LoadAsyncTask extends AsyncTask<String,Void,Object>{

        @Override
        protected Object doInBackground(String... strings) {

            String topicName=strings[0];/**收到的主题*/
            String macAddress=null;

            if (topicName.startsWith("p99/warmer")){
                macAddress = topicName.substring(11, topicName.lastIndexOf("/"));
            }else if (topicName.startsWith("p99")){
                macAddress = topicName.substring(4, topicName.lastIndexOf("/"));
            }
            int type=-1;/**产品类型*/
            int busModel=-1;/**商业模式*/
            int timerMoudle=-1;/**定时器模式*/
            String mcuVersion=null;/**MCU版本*/
            String wifiVersion=null;/**wifi版本*/
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
            String message=strings[1];/**收到的消息*/
            Log.i("message","-->"+message);
            try {
                JSONObject messageJsonObject=null;
                if (isGoodJson(message)){
                    messageJsonObject=new JSONObject(message);
                }
                String productType=null;
                JSONArray messageJsonArray=null;
                if (messageJsonObject!=null && messageJsonObject.has("productType")){
                    productType= messageJsonObject.getString("productType");
                }
                if (messageJsonObject !=null && messageJsonObject.has("Warmer")){
                    messageJsonArray=messageJsonObject.getJSONArray("Warmer");
                }

                if (!TextUtils.isEmpty(productType)){
                    type=Integer.parseInt(productType);
                }else {
                    int index=messageJsonArray.getInt(1);
                    int index2=messageJsonArray.getInt(2);
                    String x=""+index+index2;
                    type=Integer.parseInt(x);
                }

                DeviceChild deviceChild=null;
                switch (type){
                    case 1:
                        break;
                    case 2:/**取暖器*/
                        if (!TextUtils.isEmpty(productType)){

                        }else {
                            busModel=messageJsonArray.getInt(3);
                            int mMcuVersion=messageJsonArray.getInt(4);
                            mcuVersion="v"+mMcuVersion/16+"."+mMcuVersion%16;
                            int mWifiVersion=messageJsonArray.getInt(5);
                            wifiVersion="v"+mWifiVersion/16+"."+mWifiVersion%16;

                            warmerRunState=messageJsonArray.getInt(7);
                            curRunState2=messageJsonArray.getInt(8);
                            curRunState3=messageJsonArray.getInt(9);

                            int[] x=TenTwoUtil.changeToTwo(warmerRunState);
                            deviceState=x[7];
                            rateState=x[6]+""+x[5];
                            lockState=x[4];
                            screenState=x[3];

                            waramerSetTemp=messageJsonArray.getInt(10);
                            warmerCurTemp=messageJsonArray.getInt(11);

                            Log.i("warmerCurTemp","-->"+warmerCurTemp);

                            warmerSampleData=messageJsonArray.getInt(12);
                            warmerRatePower=messageJsonArray.getInt(13);
                            warmerCurRunRoatePower=messageJsonArray.getInt(14);

                            timerHour=messageJsonArray.getInt(15);
                            timerMin=messageJsonArray.getInt(16);
                            checkCode=messageJsonArray.getInt(17);
                            endCode=messageJsonArray.getInt(18);
                        }

                        List<DeviceChild> deviceChildren=deviceChildDao.findAllDevice();
                        for (DeviceChild deviceChild2:deviceChildren) {
                            if (macAddress.equals(deviceChild2.getMacAddress())){
                                deviceChild=deviceChild2;
                                break;
                            }
                        }
                        if (deviceChild!=null){
                            if (type!=-1){
                                deviceChild.setType(type);
                            }
                            if (busModel!=-1){
                                deviceChild.setBusModel(busModel);
                            }
                            if (timerMoudle!=-1){
                                deviceChild.setTimerMoudle(timerMoudle);
                            }
                            if (!TextUtils.isEmpty(wifiVersion)){
                                deviceChild.setWifiVersion(wifiVersion);
                            }
                            if (!TextUtils.isEmpty(mcuVersion)){
                                deviceChild.setMcuVersion(mcuVersion);
                            }
                            if (waramerSetTemp!=-1){
                                deviceChild.setWaramerSetTemp(waramerSetTemp);
                            }
                            if (warmerCurTemp!=-1){
                                deviceChild.setWarmerCurTemp(warmerCurTemp-128);
                            }
                            if (warmerSampleData!=-1){
                                deviceChild.setWarmerSampleData(warmerSampleData-128);
                            }
                            if (warmerRatePower!=-1){
                                deviceChild.setWarmerRatePower(warmerRatePower);
                            }
                            if (warmerCurRunRoatePower!=-1){
                                deviceChild.setWarmerCurRunRoatePower(warmerCurRunRoatePower);
                            }
                            if (deviceState!=-1){
                                deviceChild.setDeviceState(deviceState);
                            }
                            if (!TextUtils.isEmpty(rateState)){
                                deviceChild.setRateState(rateState);
                            }
                            if (lockState!=-1){
                                deviceChild.setLockState(lockState);
                            }
                            if (screenState!=-1){
                                deviceChild.setScreenState(screenState);
                            }
                            if (curRunState2!=-1){
                                deviceChild.setCurRunState2(curRunState2);
                            }
                            if (curRunState3!=-1){
                                deviceChild.setCurRunState3(curRunState3);
                            }
                            if (timerHour!=-1){
                                deviceChild.setTimerHour(timerHour);
                            }
                            if (timerMin!=-1){
                                deviceChild.setTimerMin(timerMin);
                            }
                            if (checkCode!=-1){
                                deviceChild.setCheckCode(checkCode);
                            }
                            if (endCode!=-1){
                                deviceChild.setEndCode(endCode);
                            }
                            Log.i("warmerCurTemp2222","-->"+deviceChild.getWarmerCurTemp());
                            deviceChildDao.update(deviceChild);
                        }
                        break;
                    case 3:
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

                if (AddDeviceActivity.running){
                    Intent mqttIntent = new Intent("AddDeviceActivity");
                    mqttIntent.putExtra("deviceChild", deviceChild);
                    mqttIntent.putExtra("macAddress",macAddress);
                    sendBroadcast(mqttIntent);
                } else if (DeviceDetailActivity.running){
                    Intent mqttIntent = new Intent("DeviceDetailActivity");
                    mqttIntent.putExtra("deviceChild", deviceChild);
                    mqttIntent.putExtra("macAddress",macAddress);
                    sendBroadcast(mqttIntent);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
    /**
     * 获得订阅MQTT的所有主题
     * */
    public List<String> getTopicNames(){
        List<String> list=new ArrayList<>();
        List<DeviceChild> deviceChildren=deviceChildDao.findAllDevice();
        for (DeviceChild deviceChild:deviceChildren){
            String macAddress=deviceChild.getMacAddress();
            String topicName="p99/warmer/"+macAddress+"/transfer";
            list.add(topicName);
        }
//        list.add("warmer/p99"+macAddress+"/set");
        return  list;
    }
    /**
     * 重新连接MQTT
     * */
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
     * */
    public boolean publish(String topicName, int qos, String payload) {
        boolean flag = false;
        if (client != null && client.isConnected()) {

            try {
                MqttMessage message = new MqttMessage(payload.getBytes("utf-8"));
                qos=1;
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
     * */
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
}
