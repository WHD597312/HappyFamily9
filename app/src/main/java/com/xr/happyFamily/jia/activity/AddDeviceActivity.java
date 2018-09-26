package com.xr.happyFamily.jia.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.amap.api.maps.model.Text;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.esptouch.EspWifiAdminSimple;
import com.xr.happyFamily.esptouch.EsptouchTask;
import com.xr.happyFamily.esptouch.IEsptouchListener;
import com.xr.happyFamily.esptouch.IEsptouchResult;
import com.xr.happyFamily.esptouch.IEsptouchTask;
import com.xr.happyFamily.esptouch.task.__IEsptouchTask;

import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.IsChinese;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.location.CheckPermissionsActivity;
import com.xr.happyFamily.together.util.mqtt.MQService;


import org.angmarch.views.NiceSpinner;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class AddDeviceActivity extends CheckPermissionsActivity {

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    Unbinder unbinder;
    @BindView(R.id.wifi_layout)
    RelativeLayout wifi_layout;
    /**
     * wifi添加设备的布局
     */
    @BindView(R.id.nice_spinner)
    EditText nice_spinner;
    @BindView(R.id.et_wifi)
    EditText et_wifi;
    @BindView(R.id.bt_add_finish)
    Button bt_add_finish;
    /**
     * 确定配置
     */
    @BindView(R.id.image_gif)
    GifImageView image_gif;
    private MyApplication application;

    private String inOldRoom = HttpUtils.ipAddress + "/family/device/registerDeviceInOldRoom";
    GifDrawable gifDrawable;
    DeviceChildDaoImpl deviceChildDao;
    private boolean isBound = false;
    private String mac = null;
    MessageReceiver receiver;
    public static boolean running = false;

    DeviceChild deviceChild = null;
    SharedPreferences my;
    private RoomDaoImpl roomDao;
    private SharedPreferences macAddressPreferences;
    private String province;/**省*/
    private String city;/**市*/
    private String distrct;/**区*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        macAddressPreferences=getSharedPreferences("macAddress",Context.MODE_PRIVATE);
        unbinder = ButterKnife.bind(this);
        application = (MyApplication) getApplicationContext();
        if (application != null) {
            application.addActivity(this);
        }
        my = getSharedPreferences("my", Context.MODE_PRIVATE);
        IntentFilter intentFilter = new IntentFilter("AddDeviceActivity");
        receiver = new MessageReceiver();
        registerReceiver(receiver, intentFilter);

        deviceChildDao = new DeviceChildDaoImpl(getApplicationContext());
        roomDao = new RoomDaoImpl(getApplicationContext());
        mWifiAdmin = new EspWifiAdminSimple(this);



        et_wifi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                char chars[]=s.toString().toCharArray();
                for (char c:chars){
                    if (IsChinese.isChinese(c)){
                        et_wifi.setText("");
                        Utils.showToast(AddDeviceActivity.this,"不能输入中文");
                        break;
                    }
                }
            }
        });
//        et_wifi.setKeyListener(new DigitsKeyListener() {
//            @Override
//            public int getInputType() {
//                return InputType.TYPE_TEXT_VARIATION_PASSWORD;
//            }
//            @Override
//            protected char[] getAcceptedChars() {
//                char[] data = getStringData(R.string.login_only_can_input).toCharArray();
//
//                return data;
//            }
//
//        });
        initLocation();
        startLocation();//开始定位
    }

    long roomId;
    long houseId;
    int mPosition;
    String roomName;
    String houseAddress;
    public String getStringData(int id) {
        return getResources().getString(id);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        roomId = intent.getLongExtra("roomId", 0);
        Room room = roomDao.findById(roomId);
        roomName=room.getRoomName();
        houseId = room.getHouseId();
        HourseDaoImpl hourseDao=new HourseDaoImpl(getApplicationContext());
        Hourse hourse = hourseDao.findById(houseId);
        houseAddress=hourse.getHouseAddress();
        List<DeviceChild> deviceChildren = deviceChildDao.findHouseInRoomDevices(houseId, roomId);
        Log.i("roomId", "-->" + roomId);
        int size = deviceChildren.size();
        Log.i("size", "-->" + size);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mEsptouchTask != null) {
                mEsptouchTask.interrupt();
            }

            if (popupWindow2!=null && popupWindow2.isShowing()){
                if (gifDrawable != null && gifDrawable.isRunning()) {
                    gifDrawable.stop();
                }
                nice_spinner.setEnabled(true);
                et_wifi.setEnabled(true);
                bt_add_finish.setEnabled(true);
                popupWindow2.dismiss();
                backgroundAlpha(1f);
                return false;
            }
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("houseId", houseId);
            setResult(6000,intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        String apSsid = mWifiAdmin.getWifiConnectedSsid();
//        if (apSsid != null) {
//            nice_spinner.setText(apSsid);
//        } else {
//            nice_spinner.setText("");
//        }
//        SharedPreferences wifi = getSharedPreferences("wifi", MODE_PRIVATE);
//        if (wifi.contains(apSsid)) {
//            String pswd = wifi.getString(apSsid, "");
//            et_wifi.setText(pswd);
//        }else {
//            et_wifi.setText("");

        SharedPreferences wifi = getSharedPreferences("wifi", MODE_PRIVATE);
        if (wifi.contains(apSsid)) {
            nice_spinner.setText(apSsid);
            nice_spinner.setFocusable(false);
            String pswd = wifi.getString(apSsid, "");
            et_wifi.setText(pswd);
        }else {
            nice_spinner.setText(apSsid);
            nice_spinner.setFocusable(false);
            et_wifi.setText("");
        }
        if (!TextUtils.isEmpty(apSsid)) {
            char[] chars=apSsid.toCharArray();
            nice_spinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.showToast(AddDeviceActivity.this,"WiFi名称不可编辑");
                }
            });

            for (char c:chars){
                if (IsChinese.isChinese(c)){
                    Utils.showToast(AddDeviceActivity.this,"WiFi名称不能是中文");
                    nice_spinner.setText("");
                    et_wifi.setText("");
                    break;
                }
            }
        } else {
            nice_spinner.setText("");
            nice_spinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.showToast(AddDeviceActivity.this,"请连接英文名称的wifi");
                }
            });
            et_wifi.setText("");
        }

        String userId = my.getString("userId", "");
        Log.i("userId", "-->" + userId);
    }

    @OnClick({R.id.back, R.id.image_scan,R.id.bt_add_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (mEsptouchTask != null) {
                    mEsptouchTask.interrupt();
                }

                if (popupWindow2!=null && popupWindow2.isShowing()){
                    if (gifDrawable != null && gifDrawable.isRunning()) {
                        gifDrawable.stop();
                    }
                    nice_spinner.setEnabled(true);
                    et_wifi.setEnabled(true);
                    bt_add_finish.setEnabled(true);
                    popupWindow2.dismiss();
                    backgroundAlpha(1f);
                    break;
                }
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("houseId", houseId);
                setResult(6000,intent);
                finish();
                break;
            case R.id.image_scan:
                Intent intent2=new Intent(this,QRScannerActivity.class);
                intent2.putExtra("houseId",houseId);
                startActivity(intent2);
                break;
            case R.id.bt_add_finish:
                String ssid = nice_spinner.getText().toString();
                String apPassword = et_wifi.getText().toString();
                String apBssid = mWifiAdmin.getWifiConnectedBssid();
                String taskResultCountStr = "1";
                if (__IEsptouchTask.DEBUG) {
//                    Log.d(TAG, "mBtnConfirm is clicked, mEdtApSsid = " + apSsid
//                            + ", " + " mEdtApPassword = " + apPassword);
                }
                if (TextUtils.isEmpty(ssid)){
                    Utils.showToast(AddDeviceActivity.this, "请连接英文名称的WiFi");
                    break;
                }
                if (TextUtils.isEmpty(apPassword)) {
                    Utils.showToast(AddDeviceActivity.this, "请输入wifi密码");
                    break;
                }
                nice_spinner.setEnabled(false);
                et_wifi.setEnabled(false);
                bt_add_finish.setEnabled(false);
                if (!TextUtils.isEmpty(ssid)) {
                    popupmenuWindow3();
                    if (isBound) {
                        unbindService(connection);
                    }
                    new EsptouchAsyncTask3().execute(ssid, apBssid, apPassword, taskResultCountStr);
                }
//                Intent service = new Intent(AddDeviceActivity.this, MQService.class);
//                isBound = bindService(service, connection, Context.BIND_AUTO_CREATE);
//                mac="5asdfghi69w";
                break;
        }
    }

    MQService mqService;
    private boolean bound = false;
    private String macAddress;
    private String deviceName;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MQService.LocalBinder binder = (MQService.LocalBinder) service;
            mqService = binder.getService();
            bound = true;
            if (bound == true && !TextUtils.isEmpty(mac)) {
                String wifiName = mWifiAdmin.getWifiConnectedSsid();
                macAddress = wifiName + mac;
                deviceName=mac;
                if (!TextUtils.isEmpty(macAddress)) {
                    new AddDeviceAsync().execute(macAddress);
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    private void onEsptoucResultAddedPerform(final IEsptouchResult result) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                String text = result.getBssid() + " is connected to the wifi";
//                Toast.makeText(AddDeviceActivity.this, text,
//                        Toast.LENGTH_LONG).show();
            }

        });
    }

    private IEsptouchListener myListener = new IEsptouchListener() {

        @Override
        public void onEsptouchResultAdded(final IEsptouchResult result) {
            onEsptoucResultAddedPerform(result);
        }
    };
    private ProgressDialog mProgressDialog;
    private static final String TAG = "Esptouch";
    private EspWifiAdminSimple mWifiAdmin;

    private class AddDeviceAsync extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... macs) {
            String macAddress = macs[0];

            deviceChild = new DeviceChild();
            deviceChild.setMacAddress(macAddress);
            deviceChild.setName(mac);
            deviceChild.setHouseId(houseId);
            deviceChild.setRoomId(roomId);

            Log.i("deviceChild3", "-->" + "yes");
            String topicName2 = "p99/" + macAddress + "/transfer";
            if (mqService!=null){
                boolean success = mqService.subscribe(topicName2, 1);
                if (success) {
                    String topicName = "p99/" + macAddress + "/set";
                    String payLoad = "getType";
                    boolean step2 = mqService.publish(topicName, 1, payLoad);
                }
            }
            return null;
        }
    }

    private PopupWindow popupWindow2;
    GifImageView image_heater_help;
    public void popupmenuWindow3() {
        if (popupWindow2 != null && popupWindow2.isShowing()) {
            return;
        }
        View view = View.inflate(this, R.layout.popup_help2, null);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        image_heater_help = (GifImageView) view.findViewById(R.id.image_heater_help);
        try {
            gifDrawable = new GifDrawable(getResources(), R.mipmap.touxiang3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        image_heater_help.setVisibility(View.VISIBLE);
        if (gifDrawable != null) {
            gifDrawable.start();
            image_heater_help.setImageDrawable(gifDrawable);
        }

        popupWindow2 = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //添加弹出、弹入的动画
        popupWindow2.setAnimationStyle(R.style.Popupwindow);
        backgroundAlpha(0.6f);
        popupWindow2.setFocusable(false);
        popupWindow2.setOutsideTouchable(false);
//        ColorDrawable dw = new ColorDrawable(0x30000000);
//        popupWindow.setBackgroundDrawable(dw);
//        popupWindow2.showAsDropDown(et_wifi, 0, -20);
        popupWindow2.showAtLocation(et_wifi, Gravity.CENTER, 0, 0);
        //添加按键事件监听
    }
    //设置蒙版
    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = f;
        getWindow().setAttributes(lp);
    }
    private IEsptouchTask mEsptouchTask;
    private class EsptouchAsyncTask3 extends AsyncTask<String, Void, List<IEsptouchResult>> {



        // without the lock, if the user tap confirm and cancel quickly enough,
        // the bug will arise. the reason is follows:
        // 0. task is starting created, but not finished
        // 1. the task is cancel for the task hasn't been created, it do nothing
        // 2. task is created
        // 3. Oops, the task should be cancelled, but it is running
        private final Object mLock = new Object();

        @Override
        protected void onPreExecute() {
//            popupWindow();
//            addDeviceDialog=new AddDeviceDialog(AddDeviceActivity.this);
//            addDeviceDialog.setCanceledOnTouchOutside(false);
//            addDeviceDialog.show();
//            mProgressDialog = new ProgressDialog(AddDeviceActivity.this);
//            mProgressDialog.setMessage("正在配置, 请耐心等待...");
//            mProgressDialog.setCanceledOnTouchOutside(false);
//            mProgressDialog.show();
//            CountTimer countTimer = new CountTimer(30000, 1000);
//            countTimer.start();
        }

        @Override
        protected List<IEsptouchResult> doInBackground(String... params) {
            int taskResultCount = -1;
            synchronized (mLock) {
                // !!!NOTICE
                String apSsid = mWifiAdmin.getWifiConnectedSsidAscii(params[0]);
                String apBssid = params[1];
                String apPassword = params[2];
                String taskResultCountStr = params[3];
                taskResultCount = Integer.parseInt(taskResultCountStr);
                mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, AddDeviceActivity.this);
                mEsptouchTask.setEsptouchListener(myListener);
            }
            List<IEsptouchResult> resultList = mEsptouchTask.executeForResults(taskResultCount);
            return resultList;
        }

        @Override
        protected void onPostExecute(List<IEsptouchResult> result) {
            IEsptouchResult firstResult = result.get(0);
            // check whether the task is cancelled and no results received
            if (!firstResult.isCancelled()) {
                int count = 0;
                // max results to be displayed, if it is more than maxDisplayCount,
                // just show the count of redundant ones
                final int maxDisplayCount = 5;
                // the task received some results including cancelled while
                // executing before receiving enough results
                if (firstResult.isSuc()) {
                    StringBuilder sb = new StringBuilder();
                    Log.i("IEsptouchResult","-->"+result.size());
                    for (IEsptouchResult resultInList : result) {
                        //                String ssid=et_ssid.getText().toString();
                        String ssid = resultInList.getBssid();

                        sb.append("配置成功" + ssid);
                        if (!TextUtils.isEmpty(ssid)) {
                            Intent service = new Intent(AddDeviceActivity.this, MQService.class);
                            isBound = bindService(service, connection, Context.BIND_AUTO_CREATE);
                            mac = ssid;
                            break;
                        }

                        count++;
                        if (count >= maxDisplayCount) {
                            break;
                        }
                    }
                    if (count < result.size()) {
                        sb.append("\nthere's " + (result.size() - count)
                                + " more result(s) without showing\n");
                    }
                } else {
                    if (popupWindow2!=null && popupWindow2.isShowing()){
                        if (gifDrawable != null && gifDrawable.isPlaying()) {
                            gifDrawable.stop();

                            if (et_wifi != null) {
                                et_wifi.setEnabled(true);
                            }
                            if (nice_spinner != null) {
                                nice_spinner.setEnabled(true);
                            }
                            if (bt_add_finish != null) {
                                bt_add_finish.setEnabled(true);
                                Utils.showToast(AddDeviceActivity.this, "配置失败");
                            }

                            if (mEsptouchTask != null) {
                                mEsptouchTask.interrupt();
                            }
                        }
                        popupWindow2.dismiss();
                        backgroundAlpha(1f);

                    }
                }
            }
        }
    }
    class CountTimer extends CountDownTimer {
        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        /**
         * 倒计时过程中调用
         *
         * @param millisUntilFinished
         */
        @Override
        public void onTick(long millisUntilFinished) {
            Log.e("Tag", "倒计时=" + (millisUntilFinished / 1000));
//            btn_get_code.setBackgroundColor(Color.parseColor("#c7c7c7"));
//            btn_get_code.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
//            btn_get_code.setTextSize(16);
        }

        /**
         * 倒计时完成后调用
         */
        @Override
        public void onFinish() {
            Log.e("Tag", "倒计时完成");

                if (popupWindow2!=null && popupWindow2.isShowing()){
                    if (gifDrawable != null && gifDrawable.isPlaying()) {
                        gifDrawable.stop();

                        if (et_wifi != null) {
                            et_wifi.setEnabled(true);
                        }
                        if (nice_spinner != null) {
                            nice_spinner.setEnabled(true);
                        }
                        if (bt_add_finish != null) {
                            bt_add_finish.setEnabled(true);
                            Utils.showToast(AddDeviceActivity.this, "配置失败");
                        }

                        if (mEsptouchTask != null) {
                            mEsptouchTask.interrupt();
                        }
                    }
                    popupWindow2.dismiss();
                    backgroundAlpha(1f);

            }
            //设置倒计时结束之后的按钮样式
//            btn_get_code.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_light));
//            btn_get_code.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
//            btn_get_code.setTextSize(18);
//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        running=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (TextUtils.isEmpty(addSuccess)){
            try {
                if (isBound) {
                    unbindService(connection);
                }
                if (receiver != null) {
                    unregisterReceiver(receiver);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        running = false;
    }

    private String addSuccess;
    class AddDeviceInOldRoomAsync extends AsyncTask<Map<String, Object>, Void, Integer> {

        @Override
        protected Integer doInBackground(Map<String, Object>... maps) {
            int code = 0;
            Map<String, Object> params = maps[0];
            String result = HttpUtils.postOkHpptRequest(inOldRoom, params);
            Log.i("result", "-->" + result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String returnCode = jsonObject.getString("returnCode");
                    if ("100".equals(returnCode)) {
                        SharedPreferences wifi = getSharedPreferences("wifi", MODE_PRIVATE);
                        wifi.edit().putString(mWifiAdmin.getWifiConnectedSsid(), et_wifi.getText().toString()).commit();
                        JSONObject returnData = jsonObject.getJSONObject("returnData");
                        int deviceType = returnData.getInt("deviceType");
                        String deviceMacAddress = returnData.getString("deviceMacAddress");
                        int deviceId=returnData.getInt("deviceId");
                        String deviceName=returnData.getString("deviceName");
                        List<DeviceChild> deleteDevices=deviceChildDao.findDeviceByMacAddress(macAddress);
                        if (deleteDevices!=null && !deleteDevices.isEmpty()){
                            deviceChildDao.deleteDevices(deleteDevices);
                        }
//                        List<DeviceChild> deviceChildren = deviceChildDao.findAllDevice();
//                        for (DeviceChild deviceChild2 : deviceChildren) {
//                            if (macAddress.equals(deviceChild2.getMacAddress())) {
//                                deviceChildDao.delete(deviceChild2);
//                                break;
//                            }
//                        }
                        if (deviceChild!=null){
                            deviceChild.setRoomName(roomName);
                            deviceChild.setName(deviceName);
                            deviceChild.setType(deviceType);
                            deviceChild.setDeviceId(deviceId);
                            deviceChild.setHouseId(houseId);
                            deviceChild.setRoomId(roomId);
                            deviceChildDao.insert(deviceChild);
                        }
                        String macAddress = deviceMacAddress;
                        code = Integer.parseInt(returnCode);
                        String onlineTopicName = "";
                        String offlineTopicName="";
                        if (2 == deviceType) {
                            onlineTopicName = "p99/warmer1/" + macAddress + "/transfer";
                            offlineTopicName="p99/warmer1/"+macAddress+"/lwt";
                        }else if (3==deviceType){
                            onlineTopicName="p99/sensor1/"+macAddress+"/transfer";
                            offlineTopicName="p99/sensor1/"+macAddress+"/lwt";
                        }else if (4==deviceType){
                            onlineTopicName = "p99/socket1/" + macAddress + "/transfer";
                            offlineTopicName = "p99/socket1/" + macAddress + "/lwt";
                        }else if (5==deviceType){
                            onlineTopicName = "p99/dehumidifier1/" + macAddress + "/transfer";
                            offlineTopicName = "p99/dehumidifier1/" + macAddress + "/lwt";
                        }
                        else if (6==deviceType){
                            onlineTopicName = "p99/aConditioning1/" + macAddress + "/transfer";
                            offlineTopicName = "p99/aConditioning1/" + macAddress + "/lwt";
                        }
                        else if (7==deviceType){
                            onlineTopicName = "p99/aPurifier1/" + macAddress + "/transfer";
                            offlineTopicName = "p99/aPurifier1/" + macAddress + "/lwt";
                        }else if (8==deviceType){
                            onlineTopicName = "p99/wPurifier1/" + macAddress + "/transfer";
                            offlineTopicName = "p99/wPurifier1/" + macAddress + "/lwt";
                        }
                        if (!TextUtils.isEmpty(onlineTopicName) && !TextUtils.isEmpty(offlineTopicName)){
                            boolean success = mqService.subscribe(onlineTopicName, 1);
                            boolean success2=mqService.subscribe(offlineTopicName,1);
                            if (!success) {
                                mqService.subscribe(onlineTopicName, 1);
                            }
                            if (!success2){
                                mqService.subscribe(offlineTopicName, 1);
                            }
                            if (deviceType==3){
                                String topicName="p99/sensor1/"+macAddress+"/set";
                                if (TextUtils.isEmpty(city)){
                                    city=houseAddress;
                                    if (city.contains("市")){
                                        city=city.substring(0,city.length()-1);
                                    }
                                    String info="url:http://apicloud.mob.com/v1/weather/query?key=257a640199764&city="+ URLEncoder.encode(city,"utf-8");
                                    mqService.publish(topicName,1,info);
                                }else {
                                    if (city.contains("市")){
                                        city=city.substring(0,city.length()-1);
                                    }
                                    String info="url:http://apicloud.mob.com/v1/weather/query?key=257a640199764&city="+ URLEncoder.encode(city,"utf-8")+"&province="+URLEncoder.encode(province,"utf-8");
                                    mqService.publish(topicName,1,info);
                                }

                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            switch (code) {
                case 100:
                    if (popupWindow2!=null && popupWindow2.isShowing()){
                        if (gifDrawable != null && gifDrawable.isPlaying()) {
                            gifDrawable.stop();
                        }
                        popupWindow2.dismiss();
                        backgroundAlpha(1f);
                    }
                    addSuccess="success";
                    Toast.makeText(AddDeviceActivity.this, "添加成功", Toast.LENGTH_LONG).show();
                    if (isBound) {
                        unbindService(connection);
                    }
                    if (receiver != null) {
                        unregisterReceiver(receiver);
                    }
                    Intent intent = new Intent();
                    intent.putExtra("houseId", houseId);
                    setResult(6000,intent);
                    finish();
                    break;
                default:
                    if (popupWindow2!=null && popupWindow2.isShowing()){
                        if (gifDrawable != null && gifDrawable.isPlaying()) {
                            gifDrawable.stop();

                            if (et_wifi != null) {
                                et_wifi.setEnabled(true);
                            }
                            if (nice_spinner != null) {
                                nice_spinner.setEnabled(true);
                            }
                            if (bt_add_finish != null) {
                                bt_add_finish.setEnabled(true);
                                Toast.makeText(AddDeviceActivity.this, "添加失败", Toast.LENGTH_LONG).show();
                            }
                            if (mEsptouchTask != null) {
                                mEsptouchTask.interrupt();
                            }
                        }
                        popupWindow2.dismiss();
                        backgroundAlpha(1f);
                    }
                    break;
            }
        }
    }

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String macAddress = intent.getStringExtra("macAddress");
            int type=intent.getIntExtra("type",0);
            if (!TextUtils.isEmpty(macAddress) && deviceChild != null && macAddress.equals(deviceChild.getMacAddress())) {
                Map<String, Object> params = new HashMap<>();
                params.put("deviceName", deviceName);
                params.put("deviceType", type);
                params.put("deviceMacAddress", deviceChild.getMacAddress());
                params.put("houseId", houseId);
                params.put("roomId", roomId);
                String userId = my.getString("userId", "");
                params.put("userId", userId);
                new AddDeviceInOldRoomAsync().execute(params);
                AddDeviceActivity.running=false;
            }
        }
    }

    /**
     * 初始化定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void initLocation(){
        //初始化client
        locationClient = new AMapLocationClient(getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }
    /**
     * 默认的定位参数
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {

                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if(location.getErrorCode() == 0){
                    sb.append("定位成功" + "\n");
                    sb.append("定位类型: " + location.getLocationType() + "\n");
                    sb.append("经    度    : " + location.getLongitude() + "\n");
                    sb.append("纬    度    : " + location.getLatitude() + "\n");
                    sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                    sb.append("提供者    : " + location.getProvider() + "\n");

                    sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                    sb.append("角    度    : " + location.getBearing() + "\n");
                    // 获取当前提供定位服务的卫星个数
                    sb.append("星    数    : " + location.getSatellites() + "\n");
                    sb.append("国    家    : " + location.getCountry() + "\n");
                    sb.append("省            : " + location.getProvince() + "\n");
                    sb.append("市            : " + location.getCity() + "\n");
                    sb.append("城市编码 : " + location.getCityCode() + "\n");

                    sb.append("区            : " + location.getDistrict() + "\n");
                    sb.append("区域 码   : " + location.getAdCode() + "\n");
                    sb.append("地    址    : " + location.getAddress() + "\n");
                    sb.append("兴趣点    : " + location.getPoiName() + "\n");
                    //定位完成的时间
                    sb.append("定位时间: " + com.xr.happyFamily.together.util.location.Utils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
                } else {
                    //定位失败
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                }
                sb.append("***定位质量报告***").append("\n");
                sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启":"关闭").append("\n");
                sb.append("* GPS状态：").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
                sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
                sb.append("****************").append("\n");
                //定位之后的回调时间
                sb.append("回调时间: " + com.xr.happyFamily.together.util.location.Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");

                //解析定位结果，
                String result = sb.toString();
                Log.i("reSult","-->"+result);

                if ("定位失败".equals(result)){

                }

                province=location.getProvince();
                city=location.getCity();
                distrct=location.getDistrict();
                if (!TextUtils.isEmpty(province) && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(distrct)){
                    stopLocation();
                    destroyLocation();
                }
            }
        }
    };

    /**
     * 获取GPS状态的字符串
     * @param statusCode GPS状态码
     * @return
     */
    private String getGPSStatusString(int statusCode){
        String str = "";
        switch (statusCode){
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
                break;
        }
        return str;
    }
    /**
     * 开始定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void startLocation(){
        //根据控件的选择，重新设置定位参数
//        resetOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void stopLocation(){
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void destroyLocation(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }
}
