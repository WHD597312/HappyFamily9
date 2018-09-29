package com.xr.happyFamily.jia.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.SmartTerminalInfo;
import com.xr.happyFamily.jia.view_custom.HomeDialog;
import com.xr.happyFamily.jia.view_custom.SmartTerminalBar;
import com.xr.happyFamily.jia.view_custom.SmartTerminalCircle;
import com.xr.happyFamily.jia.view_custom.SmartTerminalHumBar;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.http.NetWorkUtil;
import com.xr.happyFamily.together.util.TenTwoUtil;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.mqtt.MQService;
import com.xr.happyFamily.together.util.receiver.MQTTMessageReveiver;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.Unbinder;


/**
 * 智能终端
 */
public class SmartTerminalActivity extends AppCompatActivity implements View.OnTouchListener {

    Unbinder unbinder;
    MyApplication myApplication;
    /**
     * -12 到6 为寒冷 / 潮湿
     * 8 到 17为舒适 / 舒适
     * 19 到 34为酷热 / 干燥
     */
    @BindView(R.id.smartTerminalBar)
    SmartTerminalBar smartTerminalBar;
    @BindView(R.id.tv_title)
    TextView tv_title;
    /**
     * 设备名称
     */
    @BindView(R.id.tv_temp_value)
    TextView tv_temp_value;
    /**
     * 温度采样值
     */
    @BindView(R.id.tv_hum_value)
    TextView tv_hum_value;
    /**
     * 湿度采样值
     */
    @BindView(R.id.tv_air_value)
    TextView tv_air_value;
    /**
     * pm2.5
     */
    @BindView(R.id.tv_air_state)
    TextView tv_air_state;
    /**
     * 空气质量
     */
    @BindView(R.id.smartTerminalHumBar)
    SmartTerminalHumBar smartTerminalHumBar;
    @BindView(R.id.smart_temp_decrease)
    ImageView smart_temp_decrease;
    /**
     * 减温度
     */
    @BindView(R.id.smart_temp_add)
    ImageView smart_temp_add;
    /**
     * 加温度
     */
    @BindView(R.id.smart_hum_decrease)
    ImageView smart_hum_decrease;
    /**
     * 减湿度
     */
    @BindView(R.id.smart_hum_add)
    ImageView smart_hum_add;
    /**
     * 加湿度
     */
    @BindView(R.id.tv_smart_temp)
    TextView tv_smart_temp;
    /**
     * 设定温度
     */
    @BindView(R.id.image_switch)
    ImageView image_switch;
    /**
     * 一键开关
     */
    @BindView(R.id.image_more)
    ImageView image_more;
    @BindView(R.id.tv_temp_state) TextView tv_temp_state;/**温度状态*/
    @BindView(R.id.tv_hum_state) TextView tv_hum_state;/**湿度状态*/
    /**
     * 修改设备名称
     */
    private List<SmartTerminalInfo> list = new ArrayList<>();
    private String[] mStrs = new String[]{"", "", "", "", "", "", "", ""};
    @BindView(R.id.smartTerminalCircle)
    SmartTerminalCircle smartTerminalCircle;
    private DeviceChild deviceChild;
    private DeviceChildDaoImpl deviceChildDao;
    private String linkedUrl = HttpUtils.ipAddress + "/family/device/sensors/getDevicesInRoom";
    private String updateDeviceNameUrl = HttpUtils.ipAddress + "/family/device/changeDeviceName";
    MessageReceiver receiver;
    public static boolean running = false;
    private List<DeviceChild> linkList = new LinkedList<>();
    int sensorId;
    long houseId;
    long roomId;
    private ProgressDialog progressDialog;
    List<DeviceChild> warmers = new ArrayList<>();
    private Map<String, DeviceChild> linkDeviceChildMap = new LinkedHashMap<>();
    long id;
    private String macAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_smart_terminal);
        unbinder = ButterKnife.bind(this);
        if (myApplication == null) {
            myApplication = (MyApplication) getApplication();
            myApplication.addActivity(this);
        }

        progressDialog = new ProgressDialog(this);

        deviceChildDao = new DeviceChildDaoImpl(getApplicationContext());
        Intent intent = getIntent();
        id = intent.getLongExtra("deviceId", 0);
        houseId = intent.getLongExtra("houseId", 0);
        deviceChild = deviceChildDao.findById(id);
        macAddress=deviceChild.getMacAddress();
        sensorId = deviceChild.getDeviceId();
        roomId = deviceChild.getRoomId();
//        warmers=deviceChildDao.findDeviceByType(houseId,roomId,2,true);
//        linkList=deviceChildDao.findLinkDevice(houseId,roomId,3);
        boolean isConn = NetWorkUtil.isConn(this);

        String name = deviceChild.getName();
        tv_title.setText(name);
        setMode(deviceChild);
        if (isConn) {
            result=1;
            new GetLinkedAsync().execute();
        } else {
            Toast.makeText(this, "请检查网络", Toast.LENGTH_SHORT).show();
        }
        getBitWheelInfos();
//        smartTerminalCircle.setBitInfos(list);
        image_switch.setTag("close");

        Intent service = new Intent(SmartTerminalActivity.this, MQService.class);
        isBound = bindService(service, connection, Context.BIND_AUTO_CREATE);

        IntentFilter intentFilter = new IntentFilter("SmartTerminalActivity");
        receiver = new MessageReceiver();
        registerReceiver(receiver, intentFilter);

        tempCurProgress = smartTerminalBar.getCurProcess();
        humCurProgress = smartTerminalHumBar.getCurProcess();
        smart_temp_decrease.setOnTouchListener(this);
        smart_temp_add.setOnTouchListener(this);
        smart_hum_decrease.setOnTouchListener(this);
        smart_hum_add.setOnTouchListener(this);
    }

    public void getBitWheelInfos() {
        for (int i = 0; i < mStrs.length; i++) {
            list.add(new SmartTerminalInfo(mStrs[i], BitmapFactory.decodeResource(getResources(), R.mipmap.humidifier)));
        }
    }

    double tempCurProgress = 0;
    /**
     * 温度进度
     */
    double humCurProgress = 0;
    /**
     * 湿度进度
     */
    private boolean isBound = false;

    int result=0;
    @Override
    protected void onStart() {
        super.onStart();
        running = true;
        deviceChild=deviceChildDao.findDeviceByMacAddress2(macAddress);
        if (deviceChild != null && result==0) {
            String name = deviceChild.getName();
            tv_title.setText(name);
            setMode(deviceChild);

            int first=linkList.size();
            for(int i=0;i<linkList.size();i++){
                DeviceChild deviceChild2=linkList.get(i);
                DeviceChild deviceChild3=deviceChildDao.findDeviceByMacAddress2(deviceChild2.getMacAddress());
                if (deviceChild3==null){
                    String macAddress=deviceChild2.getMacAddress();
                    linkList.remove(i);
                    if (linkDeviceChildMap.containsKey(macAddress)){
                        linkDeviceChildMap.remove(deviceChild2);
                    }
                }
            }
            int second=linkList.size();
            if (first!=second){
                list.clear();
                getBitWheelInfos();
                List<SmartTerminalInfo> infoList = new ArrayList<>();
                for (int i = 0; i < linkList.size(); i++) {
                    DeviceChild deviceChild=linkList.get(i);
                    int linked=deviceChild.getLinked();
                    if (linked==1){
                        linkDeviceChildMap.put(deviceChild.getMacAddress(),deviceChild);
                        SmartTerminalInfo terminalInfo = list.get(i);
                        infoList.add(terminalInfo);
                    }
                }
                if (smartTerminalCircle != null) {
                    smartTerminalCircle.setBitInfos(infoList);
                }
            }

        }else {
            if (deviceChild==null && result==0){
                Intent intent=new Intent();
                intent.putExtra("houseId",houseId);
                setResult(6000,intent);
                finish();
            }
        }
    }


    private void setMode(DeviceChild deviceChild) {
        boolean online = deviceChild.getOnline();
        int sensorSimpleTemp = deviceChild.getSensorSimpleTemp();
        int sensorSimpleHum = deviceChild.getSensorSimpleHum();
        int sorsorPm = deviceChild.getSorsorPm();
        if (sensorSimpleTemp>=0){
            tv_temp_value.setText(sensorSimpleTemp + "");
        }else {
            tv_temp_value.setText("--");
        }
        if (sensorSimpleHum>=0){
            tv_hum_value.setText(sensorSimpleHum + "");
        }else {
            tv_hum_value.setText("--");
        }
        if (sorsorPm>=0){
            tv_air_value.setText(sorsorPm + "");
        }else {
            tv_air_value.setText("__");
        }

        if (sensorSimpleTemp<18){
            tv_temp_state.setText("较低");
        }else if (sensorSimpleTemp>=18 && sensorSimpleTemp<26){
            tv_temp_state.setText("舒适");
        }else if (sensorSimpleTemp>=26){
            tv_temp_state.setText("较高");
        }

        if (sensorSimpleHum<30){
            tv_hum_state.setText("干燥");
        }else  if (sensorSimpleHum>=30 && sensorSimpleHum<60){
            tv_hum_state.setText("舒适");
        }else if (sensorSimpleHum>=60){
            tv_hum_state.setText("潮湿");
        }


        if (sorsorPm > 0 && sensorSimpleHum <= 35) {
            tv_air_state.setText("优");
        } else if (sorsorPm > 35 && sorsorPm <= 75) {
            tv_air_state.setText("良");
        } else if (sorsorPm > 75) {
            tv_air_value.setText("差");
        }
    }

    /**
     * 向取暖器发送数据
     *
     * @param deviceChild
     */
    public void sendWarmer(DeviceChild deviceChild){
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

            int dataContent=TenTwoUtil.changeToTen(x);
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
                String topicName="p99/warmer1/"+deviceChild.getMacAddress()+"/set";
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

    /**
     * 向智能插座发送数据
     * @param deviceChild
     */
    private void sendSocket(DeviceChild deviceChild) {
        try {
            int sum = 0;
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            int headCode = 144;
            sum = sum + headCode;
            int type = deviceChild.getType();
            int typeHigh = type / 256;
            sum = sum + typeHigh;
            int typeLow = type % 256;
            sum = sum + typeLow;
            int busMode = deviceChild.getBusModel();
            sum = sum + busMode;
            int dataLength = 7;
            sum = sum + dataLength;
            int socketPower = deviceChild.getSocketPower();
            int socketPowerHigh = socketPower / 256;
            sum = sum + socketPowerHigh;
            int socketPowerLow = socketPower % 256;
            sum = sum + socketPowerLow;
            int socketTemp = deviceChild.getSocketTemp();/**温度*/
            sum = sum + 0;
            int socketState = deviceChild.getSocketState();
            int isSocketTimerMode = deviceChild.getIsSocketTimerMode();
            int[] x = new int[8];
            x[0] = socketState;
            x[1] = isSocketTimerMode;
            int dataContent = TenTwoUtil.changeToTen(x);
            sum = sum + dataContent;
            int socketTimer = deviceChild.getSocketTimer();
            sum = sum + socketTimer;
            int socketTimerHour = deviceChild.getSocketTimerHour();
            sum = sum + socketTimerHour;
            int socketTimerMin = deviceChild.getSocketTimerMin();
            sum = sum + socketTimerMin;

            int checkCode = sum % 256;
            jsonArray.put(0, headCode);/**头码*/
            jsonArray.put(1, typeHigh);/**类型 高位*/
            jsonArray.put(2, typeLow);/**类型 低位*/
            jsonArray.put(3, busMode);/**商业模式*/
            jsonArray.put(4, dataLength);/**数据长度*/
            jsonArray.put(5, socketPowerHigh);
            jsonArray.put(6, socketPowerLow);
            jsonArray.put(7, 0);
            jsonArray.put(8, dataContent);
            jsonArray.put(9,socketTimer);
            jsonArray.put(10, socketTimerHour);
            jsonArray.put(11, socketTimerMin);
            jsonArray.put(12, checkCode);/**校验码*/
            jsonArray.put(13, 9);/**结束码*/
            jsonObject.put("Socket", jsonArray);

            if (isBound) {
                String macAddress=deviceChild.getMacAddress();
                String topicName = "p99/socket1/" + macAddress + "/set";
                String s = jsonObject.toString();
                boolean success = mqService.publish(topicName, 1, s);
                if (success) {
                    deviceChildDao.update(deviceChild);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int temp;

    @OnClick({R.id.image_more, R.id.image_back, R.id.smart_temp_decrease, R.id.smart_temp_add, R.id.smart_hum_decrease, R.id.smart_hum_add, R.id.image_linkage, R.id.image_switch})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_more:
                popupmenuWindow();
                break;
            case R.id.image_back:
                Intent intent = new Intent();
                intent.putExtra("houseId", houseId);
                setResult(6000, intent);
                finish();
                break;
            case R.id.smart_temp_decrease:
                tempCurProgress = smartTerminalBar.getCurProcess();
                tempCurProgress = tempCurProgress - 1;
                if (tempCurProgress <= -12) {
                    tempCurProgress = -12;
                }
                temp = (int) tempCurProgress + 16;
                if (temp <= 5) {
                    temp = 5;
                } else if (temp >= 42) {
                    temp = 42;
                }
                Message tempDecrease = handler.obtainMessage();
                tempDecrease.arg1 = 0;/**减温度标记*/
                handler.sendMessage(tempDecrease);

                break;
            case R.id.smart_temp_add:
                tempCurProgress = smartTerminalBar.getCurProcess();
                tempCurProgress = tempCurProgress + 1;
                if (tempCurProgress >= 34) {
                    tempCurProgress = 34;
                }
                temp = (int) tempCurProgress + 16;
                if (temp >= 42) {
                    temp = 42;
                }
                Message tempAdd = handler.obtainMessage();
                tempAdd.arg1 = 1;/**加温度标记*/
                handler.sendMessage(tempAdd);
                break;
            case R.id.smart_hum_decrease:
                humCurProgress = smartTerminalHumBar.getCurProcess();
                humCurProgress = humCurProgress - 1;
                if (humCurProgress <= -12) {
                    humCurProgress = -12;
                }
                Message humDecrease = handler.obtainMessage();
                humDecrease.arg1 = 2;/**减湿度标记*/
                handler.sendMessage(humDecrease);
                break;
            case R.id.smart_hum_add:
                humCurProgress = smartTerminalHumBar.getCurProcess();
                humCurProgress = humCurProgress + 1;
                if (humCurProgress >= 34) {
                    humCurProgress = 34;
                }
                Message humAdd = handler.obtainMessage();
                humAdd.arg1 = 3;/**加湿度标记*/
                handler.sendMessage(humAdd);
                break;
            case R.id.image_linkage:
                if (linkList.isEmpty()) {
                    Toast.makeText(this, "没有可联动的设备", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent2 = new Intent(this, SmartLinkedActivity.class);
                    intent2.putExtra("sensorId", sensorId);
                    intent2.putExtra("Id", deviceChild.getId());
                    intent2.putExtra("deviceList", (Serializable) linkList);
                    startActivityForResult(intent2, 100);
                }
                break;
            case R.id.image_switch:
                String tag = (String) image_switch.getTag();
                List<DeviceChild> deviceChildren = deviceChildDao.findLinkedDevices(houseId, roomId, 2, sensorId, 1);
                if ("close".equals(tag)) {
                    for (Map.Entry<String, DeviceChild> entry : linkDeviceChildMap.entrySet()) {
                        DeviceChild deviceChild3 = entry.getValue();
                        boolean online = deviceChild3.getOnline();
                        if (online && deviceChild3.getType()==2) {
                            deviceChild3.setDeviceState(1);
                            sendLindedDevice(deviceChild3);
                        }else if (online && deviceChild3.getType()==4){
                            deviceChild3.setSocketState(1);
                            sendLindedDevice(deviceChild3);
                        }
                    }
                    image_switch.setTag("open");
                    image_switch.setImageResource(R.mipmap.image_switch);
                } else if ("open".equals(tag)) {
                    for (Map.Entry<String, DeviceChild> entry : linkDeviceChildMap.entrySet()) {
                        DeviceChild deviceChild3 = entry.getValue();
                        boolean online = deviceChild3.getOnline();
                        if (online && deviceChild3.getType()==2){
                          deviceChild3.setDeviceState(0);
                          sendLindedDevice(deviceChild3);
                        } else if (online && deviceChild3.getType()==4) {
                            deviceChild3.setDeviceState(0);
                            sendLindedDevice(deviceChild3);
                        }
                    }
                    image_switch.setTag("close");
                    image_switch.setImageResource(R.mipmap.image_unswitch);
                }
                break;
        }
    }

    String deviceName;

    private void buildUpdateDeviceDialog() {
        final HomeDialog dialog = new HomeDialog(this);
        dialog.setOnNegativeClickListener(new HomeDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
                dialog.dismiss();
            }
        });
        dialog.setOnPositiveClickListener(new HomeDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {
                deviceName = dialog.getName();
                if (TextUtils.isEmpty(deviceName)) {
                    Utils.showToast(SmartTerminalActivity.this, "设备名称不能为空");
                } else {
                    new UpdateDeviceAsync().execute();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    class UpdateDeviceAsync extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            int code = 0;
            try {
                int deviceId = deviceChild.getDeviceId();
                String url = updateDeviceNameUrl + "?deviceName=" + URLEncoder.encode(deviceName, "utf-8") + "&deviceId=" + deviceId;
                String result = HttpUtils.getOkHpptRequest(url);
                JSONObject jsonObject = new JSONObject(result);
                String returnCode = jsonObject.getString("returnCode");
                if ("100".equals(returnCode)) {
                    code = 100;
                    deviceChild.setName(deviceName);
                    deviceChildDao.update(deviceChild);
                }
                Log.i("result", "-->" + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            try {
                switch (code) {
                    case 100:
                        Utils.showToast(SmartTerminalActivity.this, "修改成功");
                        tv_title.setText(deviceName);
                        break;
                    default:
                        Utils.showToast(SmartTerminalActivity.this, "修改失败");
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private PopupWindow popupWindow1;

    public void popupmenuWindow() {
        if (popupWindow1 != null && popupWindow1.isShowing()) {
            return;
        }
        View view = View.inflate(this, R.layout.popview_update_device, null);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        RelativeLayout rl_room_rename = (RelativeLayout) view.findViewById(R.id.rl_room_rename);
        TextView tv_rname_r1 = (TextView) view.findViewById(R.id.tv_rname_r1);
        tv_rname_r1.setText("修改名称");

        popupWindow1 = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //点击空白处时，隐藏掉pop窗口
        popupWindow1.setFocusable(true);
        popupWindow1.setOutsideTouchable(true);
        //添加弹出、弹入的动画
        popupWindow1.setAnimationStyle(R.style.Popupwindow);

//        ColorDrawable dw = new ColorDrawable(0x30000000);
//        popupWindow.setBackgroundDrawable(dw);
        popupWindow1.showAsDropDown(image_more, 0, -20);
//        popupWindow.showAtLocation(tv_home_manager, Gravity.RIGHT, 0, 0);
        //添加按键事件监听

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl_room_rename:
                        buildUpdateDeviceDialog();
                        popupWindow1.dismiss();
                        break;
                }
            }
        };

        rl_room_rename.setOnClickListener(listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            result=1;
            linkList.clear();
            list.clear();
            getBitWheelInfos();
            linkList = (List<DeviceChild>) data.getSerializableExtra("list");
            linkDeviceChildMap.clear();
            List<SmartTerminalInfo> infoList = new ArrayList<>();
            for (int i = 0; i < linkList.size(); i++) {
                DeviceChild deviceChild = linkList.get(i);
                String macAddress = deviceChild.getMacAddress();
                int linked = deviceChild.getLinked();
                if (linked == 1) {
                    SmartTerminalInfo terminalInfo = list.get(i);
                    infoList.add(terminalInfo);
                    linkDeviceChildMap.put(macAddress, deviceChild);
                }
                int type = deviceChild.getType();
                String onlineTopicName = "";
                String offlineTopicName = "";
                switch (type) {
                    case 2:
                        onlineTopicName = "p99/warmer1/" + macAddress + "/transfer";
                        offlineTopicName = "p99/warmer1/" + macAddress + "/lwt";
                        mqService.subscribe(onlineTopicName, 1);
                        mqService.subscribe(offlineTopicName, 2);
                        break;
                    case 3:
                        onlineTopicName = "p99/sensor1/" + macAddress + "/transfer";
                        offlineTopicName = "p99/sensor1/" + macAddress + "/lwt";
                        mqService.subscribe(onlineTopicName, 1);
                        mqService.subscribe(offlineTopicName, 2);
                        break;
                }
            }
            smartTerminalCircle.setBitInfos(infoList);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i("handler", "-->" + tempCurProgress);
            switch (msg.arg1) {
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

    class GetLinkedAsync extends AsyncTask<Void, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("正在加载数据");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            int code = 0;
            int Id = deviceChild.getDeviceId();
            int type = deviceChild.getType();
            long roomId = deviceChild.getRoomId();
            String url = linkedUrl + "?deviceId=" + Id + "&deviceType=" + type + "&roomId=" + roomId;
            String result = HttpUtils.getOkHpptRequest(url);
            SmartTerminalActivity.this.result=1;
            Log.i("GetLinkedAsync", "-->" + result);
            try {
                if (!TextUtils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    String returnCode = jsonObject.getString("returnCode");
                    code = Integer.parseInt(returnCode);
                    if ("100".equals(returnCode)) {
                        JSONArray returnData = jsonObject.getJSONArray("returnData");
                        for (int i = 0; i < returnData.length(); i++) {
                            if (linkList.size()>8){
                                break;
                            }
                            JSONObject device = returnData.getJSONObject(i);
                            int deviceId = device.getInt("deviceId");
                            int isLinked = device.getInt("isLinked");
                            DeviceChild deviceChild = deviceChildDao.findDeviceByDeviceId(houseId, roomId, deviceId);
                            if (deviceChild!=null){
                                deviceChild.setLinked(isLinked);
                                deviceChild.setLinkedSensorId(sensorId);
                                deviceChildDao.update(deviceChild);
                                if (!linkList.contains(deviceChild)) {
                                    linkList.add(deviceChild);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            result=1;
            switch (code) {
                case 100:
//                    List<DeviceChild> list2 = deviceChildDao.findLinkedDevices(houseId, roomId, 2, sensorId, 1);
                    List<SmartTerminalInfo> infoList = new ArrayList<>();
                    for (int i = 0; i < linkList.size(); i++) {
                        if (infoList.size()>8){
                            break;
                        }
                        DeviceChild deviceChild = linkList.get(i);
                        String macAddress = deviceChild.getMacAddress();
                        if (deviceChild.getLinked()==1){
                            linkDeviceChildMap.put(macAddress, deviceChild);
                            SmartTerminalInfo terminalInfo = list.get(i);
                            infoList.add(terminalInfo);
                        }
                        int type = deviceChild.getType();
                        String onlineTopicName = "";
                        String offlineTopicName = "";
                        switch (type) {
                            case 2:
                                onlineTopicName = "p99/warmer/" + macAddress + "/transfer";
                                offlineTopicName = "p99/warmer/" + macAddress + "/lwt";
                                mqService.subscribe(onlineTopicName, 1);
                                mqService.subscribe(offlineTopicName, 2);
                                break;
                            case 3:
                                onlineTopicName = "p99/sensor1/" + macAddress + "/transfer";
                                offlineTopicName = "p99/sensor1/" + macAddress + "/lwt";
                                mqService.subscribe(onlineTopicName, 1);
                                mqService.subscribe(offlineTopicName, 2);
                                break;
                        }

                    }
                    if (smartTerminalCircle != null) {
                        smartTerminalCircle.setBitInfos(infoList);
                    }
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("houseId", houseId);
        setResult(6000, intent);
        finish();
    }

    MQService mqService;
    private boolean bound;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MQService.LocalBinder binder = (MQService.LocalBinder) service;
            mqService = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private void sendLindedDevice(DeviceChild deviceChild){
        int type=deviceChild.getType();
        switch (type){
            case 2:
                sendWarmer(deviceChild);
                break;
            case 4:
                sendSocket(deviceChild);
                break;
        }
    }


    class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String macAddress = intent.getStringExtra("macAddress");
                Log.i("macAddress", "-->" + macAddress);
                DeviceChild deviceChild2 = (DeviceChild) intent.getSerializableExtra("deviceChild");
                if (deviceChild2 != null && deviceChild != null && macAddress.equals(deviceChild.getMacAddress())) {
                    Log.i("macAddress", "-->2222");
                    deviceChild = deviceChild2;
                    setMode(deviceChild);
                } else if (deviceChild2 == null && deviceChild != null) {
                    if (macAddress.equals(deviceChild.getMacAddress())) {
                        Toast.makeText(SmartTerminalActivity.this, "该设备已重置", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent();
                        intent2.putExtra("houseId", houseId);
                        SmartTerminalActivity.this.setResult(6000, intent2);
                        SmartTerminalActivity.this.finish();
                    } else {
                        DeviceChild deviceChild4 = null;
                        for (int i = 0; i < linkList.size(); i++) {
                            DeviceChild deviceChild5 = linkList.get(i);
                            String macAddress2 = deviceChild5.getMacAddress();
                            if (macAddress2.equals(macAddress)) {
                                deviceChild4 = deviceChild5;
                                break;
                            }
                        }
                        if (deviceChild4 != null) {
                            String name = deviceChild4.getName();
                            linkList.remove(deviceChild4);
                            linkDeviceChildMap.remove(deviceChild4.getMacAddress());
                            list.clear();
                            getBitWheelInfos();
                            Toast.makeText(SmartTerminalActivity.this, name + "设备已重置", Toast.LENGTH_SHORT).show();
                        }
                        List<SmartTerminalInfo> infoList = new ArrayList<>();
                        for (int i = 0; i < linkList.size(); i++) {
                            DeviceChild deviceChild=linkList.get(i);
                            int linked=deviceChild.getLinked();
                            if (linked==1){
                                SmartTerminalInfo terminalInfo = list.get(i);
                                infoList.add(terminalInfo);
                            }
                        }
                        if (smartTerminalCircle != null) {
                            smartTerminalCircle.setBitInfos(infoList);
                        }
                    }
//                    if (macAddress.equals(deviceChild.getMacAddress())) {
//                        Toast.makeText(SmartTerminalActivity.this, "该设备已重置", Toast.LENGTH_SHORT).show();
//                        Intent intent2 = new Intent();
//                        intent2.putExtra("houseId", houseId);
//                        SmartTerminalActivity.this.setResult(6000, intent2);
//                        SmartTerminalActivity.this.finish();
//                    } else {
//                        DeviceChild deviceChild4 = null;
//                        for (int i = 0; i < linkList.size(); i++) {
//                            DeviceChild deviceChild5 = linkList.get(i);
//                            String mac = deviceChild5.getMacAddress();
//                            String macAddress2 = deviceChild5.getMacAddress();
//                            if (macAddress2.equals(mac)) {
//                                deviceChild4 = deviceChild5;
//                                break;
//                            }
//                        }
//                        if (deviceChild4 != null) {
//                            list.clear();
//                            getBitWheelInfos();
//                            String name = deviceChild4.getName();
//                            linkList.remove(deviceChild4);
//                            Toast.makeText(SmartTerminalActivity.this, name + "设备已重置", Toast.LENGTH_SHORT).show();
//                        }
//                        List<SmartTerminalInfo> infoList = new ArrayList<>();
//                        for (int i = 0; i < linkList.size(); i++) {
//                            DeviceChild deviceChild = linkList.get(i);
//                            int linked = deviceChild.getLinked();
//                            if (linked == 1) {
//                                SmartTerminalInfo terminalInfo = list.get(i);
//                                infoList.add(terminalInfo);
//                            }
//                        }
//                        if (smartTerminalCircle != null) {
//                            smartTerminalCircle.setBitInfos(infoList);
//                        }
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        running=false;
        result=0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (isBound) {
            unbindService(connection);
        }
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        running = false;
        handler.removeCallbacksAndMessages(null);
    }

    private boolean onClick = false;
    Thread thread;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.smart_temp_decrease:

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    onClick = true;
                    new Thread() {
                        @Override
                        public void run() {
                            while (onClick) {
                                tempCurProgress = smartTerminalBar.getCurProcess();
                                tempCurProgress = tempCurProgress - 1;
                                if (tempCurProgress <= -12) {
                                    tempCurProgress = -12;
                                }
                                try {
                                    Thread.sleep(100);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Message tempDecrease = handler.obtainMessage();
                                tempDecrease.arg1 = 0;/**减温度标记*/
                                handler.sendMessage(tempDecrease);
                            }
                        }
                    }.start();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    onClick = false;
                    temp = (int) tempCurProgress + 15;
                    if (temp <= 5) {
                        temp = 5;
                    } else if (temp >= 42) {
                        temp = 42;
                    }
                    for (Map.Entry<String, DeviceChild> entry : linkDeviceChildMap.entrySet()) {
                        DeviceChild deviceChild3 = entry.getValue();
                        boolean online = deviceChild3.getOnline();
                        if (online && deviceChild3.getType()==2){
                            deviceChild3.setWaramerSetTemp(temp);
                            sendLindedDevice(deviceChild3);
                        }
                    }
                    tv_smart_temp.setText(temp + "℃");
                }
                break;
            case R.id.smart_temp_add:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
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
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    onClick = false;
                    temp = (int) tempCurProgress + 15;
                    if (temp <= 5) {
                        temp = 5;
                    } else if (temp >= 42) {
                        temp = 42;
                    }
                    for (Map.Entry<String, DeviceChild> entry : linkDeviceChildMap.entrySet()) {
                        DeviceChild deviceChild3 = entry.getValue();
                        boolean online = deviceChild3.getOnline();
                        if (online && deviceChild3.getType()==2){
                            deviceChild3.setWaramerSetTemp(temp);
                            sendLindedDevice(deviceChild3);
                        }
                    }
                    tv_smart_temp.setText(temp + "℃");
                }
                break;
            case R.id.smart_hum_decrease:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
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
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    onClick = false;
                }
                break;
            case R.id.smart_hum_add:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
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
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    onClick = false;
                }
                break;
        }
        return false;
    }

}
