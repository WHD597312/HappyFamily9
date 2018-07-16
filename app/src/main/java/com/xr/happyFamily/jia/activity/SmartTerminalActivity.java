package com.xr.happyFamily.jia.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.SmartTerminalInfo;
import com.xr.happyFamily.jia.view_custom.SmartTerminalBar;
import com.xr.happyFamily.jia.view_custom.SmartTerminalCircle;
import com.xr.happyFamily.jia.view_custom.SmartTerminalHumBar;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.http.NetWorkUtil;
import com.xr.happyFamily.together.util.TenTwoUtil;
import com.xr.happyFamily.together.util.mqtt.MQService;
import com.xr.happyFamily.together.util.receiver.MQTTMessageReveiver;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.Unbinder;


/**智能终端*/
public class SmartTerminalActivity extends AppCompatActivity implements View.OnTouchListener{

    Unbinder unbinder;
    MyApplication myApplication;
    /**
     *  -12 到6 为寒冷 / 潮湿
     *  8 到 17为舒适 / 舒适
     *  19 到 34为酷热 / 干燥
     * */
    @BindView(R.id.smartTerminalBar) SmartTerminalBar smartTerminalBar;
    @BindView(R.id.tv_title) TextView tv_title;/**设备名称*/
    @BindView(R.id.tv_temp_value) TextView tv_temp_value;/**温度采样值*/
    @BindView(R.id.tv_hum_value) TextView tv_hum_value;/**湿度采样值*/
    @BindView(R.id.tv_air_value) TextView tv_air_value;/**pm2.5*/
    @BindView(R.id.tv_air_state) TextView tv_air_state;/**空气质量*/
    @BindView(R.id.smartTerminalHumBar) SmartTerminalHumBar  smartTerminalHumBar;
    @BindView(R.id.smart_temp_decrease) ImageView smart_temp_decrease;/**减温度*/
    @BindView(R.id.smart_temp_add) ImageView smart_temp_add;/**加温度*/
    @BindView(R.id.smart_hum_decrease) ImageView smart_hum_decrease;/**减湿度*/
    @BindView(R.id.smart_hum_add) ImageView smart_hum_add;/**加湿度*/
    @BindView(R.id.tv_smart_temp) TextView tv_smart_temp;/**设定温度*/
    @BindView(R.id.image_switch) ImageView image_switch;/**一键开关*/
    private List<SmartTerminalInfo> list=new ArrayList<>();
    private String[] mStrs = new String[]{"", "","", "","","","",""};
    @BindView(R.id.smartTerminalCircle)
    SmartTerminalCircle smartTerminalCircle;
    private DeviceChild deviceChild;
    private DeviceChildDaoImpl deviceChildDao;
    private String linkedUrl= HttpUtils.ipAddress+"/family/device/sensors/getDevicesInRoom";
    MessageReceiver receiver;
    public static boolean running=false;
    private List<DeviceChild> linkList=new ArrayList<>();
    int sensorId;
    long houseId;
    long roomId;
    List<DeviceChild> warmers=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_smart_terminal);
        unbinder=ButterKnife.bind(this);
        if (myApplication==null){
            myApplication= (MyApplication) getApplication();
            myApplication.addActivity(this);
        }

        deviceChildDao=new DeviceChildDaoImpl(getApplicationContext());
        Intent intent=getIntent();
        long  id=intent.getLongExtra("deviceId",0);
        houseId=intent.getLongExtra("houseId",0);

        deviceChild=deviceChildDao.findById(id);
        sensorId=deviceChild.getDeviceId();
        roomId=deviceChild.getRoomId();
//        warmers=deviceChildDao.findDeviceByType(houseId,roomId,2,true);
//        linkList=deviceChildDao.findLinkDevice(houseId,roomId,3);
        boolean isConn=NetWorkUtil.isConn(this);
        if (isConn){
            new GetLinkedAsync().execute();
        }else {
            Toast.makeText(this,"请检查网络",Toast.LENGTH_SHORT).show();
        }
        getBitWheelInfos();
        smartTerminalCircle.setBitInfos(list);
        image_switch.setTag("close");
    }

    public void getBitWheelInfos() {
        for (int i = 0; i < mStrs.length; i++) {
            list.add(new SmartTerminalInfo(mStrs[i], BitmapFactory.decodeResource(getResources(), R.mipmap.humidifier)));
        }
    }
    double tempCurProgress=0;/**温度进度*/
    double humCurProgress=0;/**湿度进度*/
    private boolean isBound=false;
    @Override
    protected void onStart() {
        super.onStart();
        running=true;
        Intent service=new Intent(SmartTerminalActivity.this,MQService.class);
        isBound=bindService(service,connection, Context.BIND_AUTO_CREATE);

        IntentFilter intentFilter = new IntentFilter("SmartTerminalActivity");
        receiver = new MessageReceiver();
        registerReceiver(receiver, intentFilter);

        tempCurProgress=smartTerminalBar.getCurProcess();
        humCurProgress=smartTerminalHumBar.getCurProcess();
        smart_temp_decrease.setOnTouchListener(this);
        smart_temp_add.setOnTouchListener(this);
        smart_hum_decrease.setOnTouchListener(this);
        smart_hum_add.setOnTouchListener(this);
        if (deviceChild!=null){
            String name=deviceChild.getName();
            tv_title.setText(name);
            setMode(deviceChild);
        }
    }
    private void setMode(DeviceChild deviceChild){
        boolean online=deviceChild.getOnline();
        int sensorSimpleTemp=deviceChild.getSensorSimpleTemp();
        int sensorSimpleHum=deviceChild.getSensorSimpleHum();
        int sorsorPm=deviceChild.getSorsorPm();
        tv_temp_value.setText(sensorSimpleTemp+"");
        tv_hum_value.setText(sensorSimpleHum+"");
        tv_air_value.setText(sorsorPm+"");
        if (sorsorPm>0 && sensorSimpleHum<=35){
            tv_air_state.setText("优");
        }else if (sorsorPm>35 && sorsorPm<=75){
            tv_air_state.setText("良");
        }else if (sorsorPm>75){
            tv_air_value.setText("差");
        }
    }

    /**
     * 向取暖器发送数据
     * @param deviceChild
     */
    public void send(DeviceChild deviceChild){
        try {
            int sum=0;
            JSONObject jsonObject=new JSONObject();
            JSONArray jsonArray=new JSONArray();
            int headCode=144;
            jsonArray.put(0,headCode);/**头码*/
            sum=sum+headCode;
            int type=deviceChild.getType();
            int typeHigh=type/256;
            sum=sum+typeHigh;
            int typeLow=type%256;
            sum=sum+typeLow;
            int dataLength=6;
            sum=sum+dataLength;

            int busMode=deviceChild.getBusModel();
            sum=sum+busMode;
            jsonArray.put(1,typeHigh);/**类型 高位*/
            jsonArray.put(2,typeLow);/**类型 低位*/
            jsonArray.put(3,busMode);/**商业模式*/
            jsonArray.put(4,dataLength);/**数据长度*/
            int deviceState=deviceChild.getDeviceState();
            String rateStateStr=deviceChild.getRateState();
            int rateStateHigh=Integer.parseInt(rateStateStr.substring(0,1));
            int rateStateLow=Integer.parseInt(rateStateStr.substring(1));
            int lockState=deviceChild.getLockState();
            int screenState=deviceChild.getScreenState();
            int []x=new int[8];
            x[0]=deviceState;
            x[1]=rateStateHigh;
            x[2]=rateStateLow;
            x[3]=lockState;
            x[4]=screenState;
            x[5]=0;
            x[6]=0;
            x[7]=0;


            int dataContent= TenTwoUtil.changeToTen(x);
            sum=sum+dataContent;
            jsonArray.put(5,dataContent);/**数据内容 开关，功率状态，屏幕状态，屏保状态*/
            int runState2=0;
            sum=sum+runState2;
            jsonArray.put(6,runState2);/**机器当前运行状态2  (保留*/
            int runState3=0;
            sum=sum+runState3;
            jsonArray.put(7,runState3);/**机器当前运行状态3  (保留*/

            int timeHour=deviceChild.getTimerHour();
            sum=sum+timeHour;
            jsonArray.put(8,timeHour);/**定时时间 小时*/
            int timeMin=deviceChild.getTimerMin();
//            sum=sum+timeMin;
            jsonArray.put(9,0);/**定时时间 分*/
            int waramerSetTemp=deviceChild.getWaramerSetTemp();
            sum=sum+waramerSetTemp;

            int checkCode=sum%256;
            jsonArray.put(10,(waramerSetTemp));/**设定温度*/
            jsonArray.put(11,checkCode);/**校验码*/

            jsonArray.put(12,9);/**结束码*/
            jsonObject.put("Warmer",jsonArray);
            if (isBound){
                String topicName="p99/warmer/"+deviceChild.getMacAddress()+"/set";
                String s=jsonObject.toString();
                boolean success=mqService.publish(topicName,1,s);
                if (success){
                    Log.i("success","-->"+success);
                    int deviceState2=deviceChild.getDeviceState();
                    Log.i("deviceState2","-->"+deviceState2);
                    deviceChildDao.update(deviceChild);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    int temp;
    @OnClick({R.id.image_back,R.id.smart_temp_decrease,R.id.smart_temp_add,R.id.smart_hum_decrease,R.id.smart_hum_add,R.id.image_linkage,R.id.image_switch})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.image_back:
                Intent intent=new Intent();
                intent.putExtra("houseId",houseId);
                setResult(6000,intent);
                finish();
                break;
            case R.id.smart_temp_decrease:
                tempCurProgress=smartTerminalBar.getCurProcess();
                tempCurProgress=tempCurProgress-1;
                if (tempCurProgress<=-12){
                    tempCurProgress=-12;
                }
                temp=(int)tempCurProgress+16;
                if (temp<=5){
                    temp=5;
                }else if (temp>=42){
                    temp=42;
                }
                Message tempDecrease=handler.obtainMessage();
                tempDecrease.arg1=0;/**减温度标记*/
                handler.sendMessage(tempDecrease);

                break;
            case R.id.smart_temp_add:
                tempCurProgress=smartTerminalBar.getCurProcess();
                tempCurProgress=tempCurProgress+1;
                if (tempCurProgress>=34){
                    tempCurProgress=34;
                }
                temp=(int)tempCurProgress+16;
                if (temp>=42){
                    temp=42;
                }
                Message tempAdd=handler.obtainMessage();
                tempAdd.arg1=1;/**加温度标记*/
                handler.sendMessage(tempAdd);
                break;
            case R.id.smart_hum_decrease:
                humCurProgress=smartTerminalHumBar.getCurProcess();
                humCurProgress=humCurProgress-1;
                if (humCurProgress<=-12){
                    humCurProgress=-12;
                }
                Message humDecrease=handler.obtainMessage();
                humDecrease.arg1=2;/**减湿度标记*/
                handler.sendMessage(humDecrease);
                break;
            case R.id.smart_hum_add:
                humCurProgress=smartTerminalHumBar.getCurProcess();
                humCurProgress=humCurProgress+1;
                if (humCurProgress>=34){
                    humCurProgress=34;
                }
                Message humAdd=handler.obtainMessage();
                humAdd.arg1=3;/**加湿度标记*/
                handler.sendMessage(humAdd);
                break;
            case R.id.image_linkage:
                if (linkList.isEmpty()){
                    Toast.makeText(this,"没有可联动的设备",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent2=new Intent(this,SmartLinkedActivity.class);
                    intent2.putExtra("sensorId",sensorId);
                    intent2.putExtra("Id",deviceChild.getId());
                    intent2.putExtra("deviceList",(Serializable) linkList);
                    startActivityForResult(intent2,100);
                }
                break;
            case R.id.image_switch:
                String tag= (String) image_switch.getTag();

                List<DeviceChild> deviceChildren=deviceChildDao.findLinkedDevices(houseId,roomId,2,sensorId,1,true);
                if ("close".equals(tag)){
                    for(DeviceChild deviceChild:deviceChildren){
                        deviceChild.setDeviceState(1);
                        send(deviceChild);
                    }
                    image_switch.setTag("open");
                    image_switch.setImageResource(R.mipmap.image_switch);
                }else if ("open".equals(tag)){
                    for(DeviceChild deviceChild:deviceChildren){
                        deviceChild.setDeviceState(0);
                        send(deviceChild);
                    }
                    image_switch.setTag("close");
                    image_switch.setImageResource(R.mipmap.image_unswitch);
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==100){
            List<DeviceChild> list= (List<DeviceChild>) data.getSerializableExtra("list");
            if (!list.isEmpty()){
                linkList=list;
            }
        }
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i("handler","-->"+tempCurProgress);
            switch (msg.arg1){
                case 0:
                    smartTerminalBar.setmCurProcess(tempCurProgress);
                    smartTerminalBar.invalidate();
                    break;
                case 1:
                    smartTerminalBar.setmCurProcess(tempCurProgress);
                    smartTerminalBar.invalidate();
                    break;
                case 2:
                    smartTerminalHumBar.setmCurProcess(humCurProgress);
                    smartTerminalHumBar.invalidate();
                    break;
                case 3:
                    smartTerminalHumBar.setmCurProcess(humCurProgress);
                    smartTerminalHumBar.invalidate();
                    break;
            }

        }
    };
    class GetLinkedAsync extends AsyncTask<Void,Void,Integer>{

        @Override
        protected Integer doInBackground(Void... voids) {
            int Id=deviceChild.getDeviceId();
            int type=deviceChild.getType();
            long roomId=deviceChild.getRoomId();
            String url=linkedUrl+"?deviceId="+Id+"&deviceType="+type+"&roomId="+roomId;
            String result=HttpUtils.getOkHpptRequest(url);
            try {
                if (!TextUtils.isEmpty(result)){
                    JSONObject jsonObject=new JSONObject(result);
                    String returnCode=jsonObject.getString("returnCode");
                    if ("100".equals(returnCode)){
                        JSONArray returnData=jsonObject.getJSONArray("returnData");
                        for (int i = 0; i < returnData.length(); i++) {
                            JSONObject device=returnData.getJSONObject(i);
                            int deviceId=device.getInt("deviceId");
                            int isLinked=device.getInt("isLinked");
                            DeviceChild deviceChild=deviceChildDao.findDeviceByDeviceId(houseId,roomId,deviceId);
                            deviceChild.setLinked(isLinked);
                            deviceChild.setLinkedSensorId(sensorId);
                            deviceChildDao.update(deviceChild);
                            if (linkList!=null){
                                linkList.add(deviceChild);
                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("houseId",houseId);
        setResult(6000,intent);
        finish();
    }
    MQService mqService;
    private boolean bound;
    ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MQService.LocalBinder binder = (MQService.LocalBinder) service;
            mqService = binder.getService();
            bound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    class MessageReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String macAddress=intent.getStringExtra("macAddress");
            DeviceChild deviceChild2= (DeviceChild) intent.getSerializableExtra("deviceChild");
            if (deviceChild!=null && macAddress.equals(deviceChild.getMacAddress())){
                if (deviceChild2!=null){
                    deviceChild=deviceChild2;
                    setMode(deviceChild);
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
        if (isBound){
            unbindService(connection);
        }
        if (receiver!=null){
            unregisterReceiver(receiver);
        }
        running=false;
    }

    private boolean onClick=false;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.smart_temp_decrease:
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    onClick=true;
                    new Thread(){
                        @Override
                        public void run() {
                            while (onClick){
                                tempCurProgress=smartTerminalBar.getCurProcess();
                                tempCurProgress=tempCurProgress-1;
                                if (tempCurProgress<=-12){
                                    tempCurProgress=-12;
                                }
                                try {
                                    Thread.sleep(100);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                Message tempDecrease=handler.obtainMessage();
                                tempDecrease.arg1=0;/**减温度标记*/
                                handler.sendMessage(tempDecrease);
                            }
                        }
                    }.start();
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    onClick=false;
                    temp=(int)tempCurProgress+15;
                    if (temp<=5){
                        temp=5;
                    }else if (temp>=42){
                        temp=42;
                    }
                    warmers=deviceChildDao.findLinkedDevices(houseId,roomId,2,sensorId,1,true);
                    for (DeviceChild deviceChild:warmers){
                        deviceChild.setWaramerSetTemp(temp);
                        send(deviceChild);
                    }
                    tv_smart_temp.setText(temp+"℃");
                }
                break;
            case R.id.smart_temp_add:
                if (event.getAction()==MotionEvent.ACTION_DOWN) {
                    onClick = true;
                    new Thread() {
                        @Override
                        public void run() {
                            while (onClick) {
                                tempCurProgress = smartTerminalBar.getCurProcess();
                                tempCurProgress++;
                                if (tempCurProgress >= 34) {
                                    tempCurProgress = 34;
                                }

                                try {
                                    Thread.sleep(100);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Message tempAdd = handler.obtainMessage();
                                tempAdd.arg1 = 1;/**加温度标记*/
                                handler.sendMessage(tempAdd);
                            }
                        }
                    }.start();
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    temp=(int)tempCurProgress+15;
                    if (temp<=5){
                        temp=5;
                    } else if (temp>=42){
                        temp=42;
                    }
                    warmers=deviceChildDao.findLinkedDevices(houseId,roomId,2,sensorId,1,true);
                    for (DeviceChild deviceChild:warmers){
                        deviceChild.setWaramerSetTemp(temp);
                        send(deviceChild);
                    }
                    tv_smart_temp.setText(temp+"℃");
                    onClick=false;
                }
                break;
            case R.id.smart_hum_decrease:
                if (event.getAction()==MotionEvent.ACTION_DOWN) {
                    onClick = true;
                    new Thread() {
                        @Override
                        public void run() {
                            while (onClick) {
                                humCurProgress = smartTerminalHumBar.getCurProcess();
                                humCurProgress = humCurProgress - 1;
                                if (humCurProgress <= -12) {
                                    humCurProgress = -12;
                                }
                                try {
                                    Thread.sleep(100);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Message humDecrease = handler.obtainMessage();
                                humDecrease.arg1 = 2;/**减湿度标记*/
                                handler.sendMessage(humDecrease);
                            }
                        }
                    }.start();
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    onClick=false;
                }
                break;
            case R.id.smart_hum_add:
                if (event.getAction()==MotionEvent.ACTION_DOWN) {
                    onClick = true;
                    new Thread() {
                        @Override
                        public void run() {
                            while (onClick) {
                                humCurProgress = smartTerminalHumBar.getCurProcess();
                                humCurProgress = humCurProgress + 1;
                                if (humCurProgress >= 34) {
                                    humCurProgress = 34;
                                }

                                try {
                                    Thread.sleep(100);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Message humAdd = handler.obtainMessage();
                                humAdd.arg1 = 3;/**加湿度标记*/
                                handler.sendMessage(humAdd);
                            }
                        }
                    }.start();
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    onClick=false;
                }
                break;
        }
        return false;
    }
}
