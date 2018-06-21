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
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.mqtt.MQService;


import org.angmarch.views.NiceSpinner;

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
    @BindView(R.id.wifi_layout) RelativeLayout wifi_layout;/**wifi添加设备的布局*/
    @BindView(R.id.nice_spinner) NiceSpinner nice_spinner;
    @BindView(R.id.et_wifi) EditText et_wifi;
    @BindView(R.id.bt_add_finish) Button bt_add_finish;/**确定配置*/
    @BindView(R.id.image_gif) GifImageView image_gif;
    private MyApplication application;

    private String inNewRoom= HttpUtils.ipAddress+"/family/device/registerDeviceInNewRoom";
    private String inOldRoom=HttpUtils.ipAddress+"/family/device/registerDeviceInOldRoom";
    GifDrawable gifDrawable;
    DeviceChildDaoImpl deviceChildDao;
    private boolean isBound=false;
    private String mac=null;
    MessageReceiver receiver;
    public static boolean running=false;


    DeviceChild deviceChild=null;
    SharedPreferences my;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        unbinder=ButterKnife.bind(this);
        application= (MyApplication) getApplicationContext();
        if (application!=null){
            application.addActivity(this);
        }
        my=MyApplication.getContext().getSharedPreferences("my",Context.MODE_PRIVATE);
        Intent service=new Intent(this, MQService.class);
        startService(service);


        IntentFilter intentFilter = new IntentFilter("AddDeviceActivity");
        receiver = new MessageReceiver();
        registerReceiver(receiver, intentFilter);

        deviceChildDao=new DeviceChildDaoImpl(getApplicationContext());
        mWifiAdmin = new EspWifiAdminSimple(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String apSsid = mWifiAdmin.getWifiConnectedSsid();
        if (apSsid != null) {
            nice_spinner.setText(apSsid);
        } else {
            nice_spinner.setText("");
        }
        String userId=my.getString("userId","");
        Log.i("userId","-->"+userId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        running=true;
    }

    @OnClick({R.id.back,R.id.bt_add_finish})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                if (gifDrawable!=null && gifDrawable.isPlaying()){
                    gifDrawable.stop();
                    image_gif.setVisibility(View.GONE);
                    et_wifi.setEnabled(true);
                    bt_add_finish.setEnabled(true);
                    nice_spinner.setEnabled(true);
                    break;
                }
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
                if (TextUtils.isEmpty(apPassword)){
                    com.xr.happyFamily.login.util.Utils.showToast(AddDeviceActivity.this,"请输入wifi密码");
                    break;
                }
                if (!TextUtils.isEmpty(ssid)) {
                    new EsptouchAsyncTask3().execute(ssid, apBssid, apPassword, taskResultCountStr);
                }
                break;
        }
    }

    MQService mqService;
    private boolean bound=false;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MQService.LocalBinder binder = (MQService.LocalBinder) service;
            mqService = binder.getService();
            bound = true;
            if (bound==true && !TextUtils.isEmpty(mac)){
                String wifiName=nice_spinner.getText().toString();
                String macAddress=wifiName+mac;
                String topicName2="p99/"+macAddress+"/transfer";
                boolean success=mqService.subscribe(topicName2,1);
                if (success){
                    String topicName="p99/"+macAddress+"/set";
                    String payLoad="getType";
                    boolean step2=mqService.publish(topicName,1,payLoad);
                    if (step2){
                        new AddDeviceAsync().execute(macAddress);
                    }
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
    private class AddDeviceAsync extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... macs) {
            String macAddress=macs[0];
            DeviceChild deviceChild3=null;
            List<DeviceChild> deviceChildren=deviceChildDao.findAllDevice();
            if (deviceChildren==null ||deviceChildren.isEmpty()){
                deviceChild=new DeviceChild();/**不存在，就添加该设备*/
                deviceChild.setMacAddress(macAddress);
                deviceChild.setName(mac);
                boolean insert=deviceChildDao.insert(deviceChild);
                Log.i("insert","-->"+insert);
            }else {
                for(DeviceChild deviceChild2:deviceChildren){/**从数据库中找是否该设备已经存在*/
                    if (macAddress.equals(deviceChild2.getMacAddress())){
                        deviceChild3=deviceChild2;/**存在，就结束循环，遍历结束*/
                        break;
                    }
                }
                if (deviceChild3!=null){
                    deviceChild=deviceChild3;
                    deviceChildDao.update(deviceChild);/**存在就更新一下该设备*/
                }else {
                    deviceChild=new DeviceChild();/**不存在，就添加该设备*/
                    deviceChild.setMacAddress(macAddress);
                    deviceChild.setName(mac);
                    boolean insert=deviceChildDao.insert(deviceChild);
                    Log.i("insert","-->"+insert);
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
            mProgressDialog
                    .setMessage("正在配置, 请耐心等待...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    synchronized (mLock) {
                        if (__IEsptouchTask.DEBUG) {
                            Log.i(TAG, "progress dialog is canceled");
                        }
                        if (mEsptouchTask != null) {
                            mEsptouchTask.interrupt();
                        }
                    }
                }
            });
            mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                    "Waiting...", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            mProgressDialog.show();
            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setEnabled(false);
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
            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setEnabled(true);
            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(
                    "确认");
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

                        sb.append("配置成功"+ssid);
                        if (!TextUtils.isEmpty(ssid)){
                            Intent service=new Intent(AddDeviceActivity.this, MQService.class);
                            isBound = bindService(service, connection, Context.BIND_AUTO_CREATE);
                            mac=ssid;
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
                    mProgressDialog.setMessage(sb.toString());
                } else {

                    com.xr.happyFamily.login.util.Utils.showToast(AddDeviceActivity.this,"配置失败");
                    mProgressDialog.setMessage("配置失败");
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
    class AddDeviceInNewRoomAsync extends AsyncTask<Map<String,Object>,Void,Integer>{

        @Override
        protected Integer doInBackground(Map<String, Object>... maps) {
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }
    }

    class MessageReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String macAddress=intent.getStringExtra("macAddress");
            if (!TextUtils.isEmpty(macAddress) && deviceChild!=null && macAddress.equals(deviceChild.getMacAddress())){
                Map<String,Object> params=new HashMap<>();
                params.put("deviceName",deviceChild.getName());
                params.put("deviceType",deviceChild.getType());
                params.put("deviceMacAddress",deviceChild.getMacAddress());
                params.put("houseId",deviceChild.getHouseId());
                params.put("roomId",deviceChild.getRoomId());
                String userId=my.getString("userId","");
                params.put("userId",userId);
                new AddDeviceInNewRoomAsync().execute(params);
            }
        }
    }
}
