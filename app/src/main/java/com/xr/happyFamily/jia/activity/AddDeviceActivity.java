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
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.esptouch.EspWifiAdminSimple;
import com.xr.happyFamily.esptouch.EsptouchTask;
import com.xr.happyFamily.esptouch.IEsptouchListener;
import com.xr.happyFamily.esptouch.IEsptouchResult;
import com.xr.happyFamily.esptouch.IEsptouchTask;
import com.xr.happyFamily.esptouch.task.__IEsptouchTask;

import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.MyPaperActivity;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.mqtt.MQService;


import org.angmarch.views.NiceSpinner;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class AddDeviceActivity extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.wifi_layout)
    RelativeLayout wifi_layout;
    /**
     * wifi添加设备的布局
     */
    @BindView(R.id.nice_spinner)
    NiceSpinner nice_spinner;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        unbinder = ButterKnife.bind(this);
        application = (MyApplication) getApplicationContext();
        if (application != null) {
            application.addActivity(this);
        }
        my = MyApplication.getContext().getSharedPreferences("my", Context.MODE_PRIVATE);
        Intent service = new Intent(this, MQService.class);
        startService(service);

        IntentFilter intentFilter = new IntentFilter("AddDeviceActivity");
        receiver = new MessageReceiver();
        registerReceiver(receiver, intentFilter);

        deviceChildDao = new DeviceChildDaoImpl(getApplicationContext());
        roomDao = new RoomDaoImpl(getApplicationContext());
        mWifiAdmin = new EspWifiAdminSimple(this);
    }

    long roomId;
    long houseId;
    int mPosition;

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        roomId = intent.getLongExtra("roomId", 0);
        Room room = roomDao.findById(roomId);
        houseId = room.getHouseId();
        List<DeviceChild> deviceChildren = deviceChildDao.findHouseInRoomDevices(houseId, roomId);
        Log.i("roomId", "-->" + roomId);
        int size = deviceChildren.size();
        Log.i("size", "-->" + size);
        String apSsid = mWifiAdmin.getWifiConnectedSsid();
        if (apSsid != null) {
            nice_spinner.setText(apSsid);
        } else {
            nice_spinner.setText("");
        }
        String userId = my.getString("userId", "");
        Log.i("userId", "-->" + userId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;
    }

    @OnClick({R.id.back, R.id.bt_add_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (gifDrawable != null && gifDrawable.isPlaying()) {
                    gifDrawable.stop();
                    image_gif.setVisibility(View.GONE);
                    et_wifi.setEnabled(true);
                    bt_add_finish.setEnabled(true);
                    nice_spinner.setEnabled(true);
                    break;
                }
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("houseId", houseId);
                setResult(6000,intent);
                finish();
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
                if (TextUtils.isEmpty(apPassword)) {
                    com.xr.happyFamily.login.util.Utils.showToast(AddDeviceActivity.this, "请输入wifi密码");
                    break;
                }
                if (!TextUtils.isEmpty(ssid)) {
                    new EsptouchAsyncTask3().execute(ssid, apBssid, apPassword, taskResultCountStr);
                }
//                String macAddress="kkkdfddddddd";
//                List<DeviceChild> deviceChildren = deviceChildDao.findAllDevice();
//                for (DeviceChild deviceChild2 : deviceChildren) {
//                    if (macAddress.equals(deviceChild2.getMacAddress())) {
//                        deviceChildDao.delete(deviceChild2);
//                        break;
//                    }
//                }
//                deviceChild = new DeviceChild();
//                deviceChild.setMacAddress(macAddress);
//                deviceChild.setName(macAddress);
//                deviceChild.setHouseId(houseId);
//                deviceChild.setRoomId(roomId);
//                boolean insert = deviceChildDao.insert(deviceChild);
//                if (insert){
//                    Intent intent2 = new Intent(this, MainActivity.class);
//                    intent2.putExtra("houseId", houseId);
//                    setResult(6000,intent2);
//                    finish();
//                }
                break;
        }
    }

    MQService mqService;
    private boolean bound = false;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MQService.LocalBinder binder = (MQService.LocalBinder) service;
            mqService = binder.getService();
            bound = true;
            if (bound == true && !TextUtils.isEmpty(mac)) {
                String wifiName = nice_spinner.getText().toString();
                String macAddress = wifiName + mac;
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
            List<DeviceChild> deviceChildren = deviceChildDao.findAllDevice();
            for (DeviceChild deviceChild2 : deviceChildren) {
                if (macAddress.equals(deviceChild2.getMacAddress())) {
                    deviceChildDao.delete(deviceChild2);
                    break;
                }
            }
            deviceChild = new DeviceChild();
            deviceChild.setMacAddress(macAddress);
            deviceChild.setName(macAddress);
            deviceChild.setHouseId(houseId);
            deviceChild.setRoomId(roomId);
            boolean insert = deviceChildDao.insert(deviceChild);
            Log.i("insert", "-->" + insert);
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

    private class EsptouchAsyncTask3 extends AsyncTask<String, Void, List<IEsptouchResult>> {


        private IEsptouchTask mEsptouchTask;
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
            mProgressDialog = new ProgressDialog(AddDeviceActivity.this);
            mProgressDialog.setMessage("正在配置, 请耐心等待...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
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
                    for (IEsptouchResult resultInList : result) {
                        //                String ssid=et_ssid.getText().toString();
                        String ssid = resultInList.getBssid();

                        sb.append("配置成功" + ssid);
                        mProgressDialog.dismiss();
                        if (!TextUtils.isEmpty(ssid)) {
                            Intent service = new Intent(AddDeviceActivity.this, MQService.class);
                            isBound = bindService(service, connection, Context.BIND_AUTO_CREATE);
                            mac = ssid;
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
                    com.xr.happyFamily.login.util.Utils.showToast(AddDeviceActivity.this, "配置失败");
                    mProgressDialog.dismiss();
                }
            }
        }
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
    }

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
                        JSONObject returnData = jsonObject.getJSONObject("returnData");
                        int deviceType = returnData.getInt("deviceType");
                        String deviceMacAddress = returnData.getString("deviceMacAddress");
                        code = Integer.parseInt(returnCode);
                        String macAddress = deviceMacAddress;
                        String topicName = "";
                        if (2 == deviceType) {
                            topicName = "p99/warmer/" + macAddress + "/transfer";
                        }
                        boolean success = mqService.subscribe(topicName, 1);
                        if (!success) {
                            mqService.subscribe(topicName, 1);
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
                    Toast.makeText(AddDeviceActivity.this, "添加成功", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddDeviceActivity.this, MainActivity.class);
                    intent.putExtra("houseId", houseId);
                    startActivity(intent);
                    break;
                case 2001:
                    Toast.makeText(AddDeviceActivity.this, "添加失败", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String macAddress = intent.getStringExtra("macAddress");
            DeviceChild deviceChild2 = (DeviceChild) intent.getSerializableExtra("deviceChild");
            if (!TextUtils.isEmpty(macAddress) && deviceChild != null && macAddress.equals(deviceChild.getMacAddress())) {
                deviceChild = deviceChild2;
                Map<String, Object> params = new HashMap<>();
                params.put("deviceName", deviceChild.getName());
                params.put("deviceType", deviceChild.getType());
                params.put("deviceMacAddress", deviceChild.getMacAddress());
                params.put("houseId", houseId);
                params.put("roomId", roomId);
                String userId = my.getString("userId", "");
                params.put("userId", userId);
                new AddDeviceInOldRoomAsync().execute(params);
                AddDeviceActivity.running = false;
            }
        }
    }
}
