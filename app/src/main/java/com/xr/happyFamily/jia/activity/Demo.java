package com.xr.happyFamily.jia.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.esptouch.EspWifiAdminSimple;
import com.xr.happyFamily.esptouch.EsptouchTask;
import com.xr.happyFamily.esptouch.IEsptouchListener;
import com.xr.happyFamily.esptouch.IEsptouchResult;
import com.xr.happyFamily.esptouch.IEsptouchTask;
import com.xr.happyFamily.esptouch.task.__IEsptouchTask;
import com.xr.happyFamily.jia.MainActivity;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.SmartTerminalInfo;
import com.xr.happyFamily.jia.view_custom.SmartTerminalCircle;
import com.xr.happyFamily.together.http.HttpUtils;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class Demo extends AppCompatActivity {

    private boolean running=false;
    MyApplication application;
    @BindView(R.id.btn_wifi)
    Button btn_wifi;
    @BindView(R.id.btn_scan)
    Button btn_scan;
    @BindView(R.id.tv_result)
    TextView tv_result;
    @BindView(R.id.et_ssid)
    EditText et_ssid;
    @BindView(R.id.et_pswd)
    EditText et_pswd;
    @BindView(R.id.btn_match)
    Button btn_match;
    String group;
    String groupPosition;
    private long houseId;
    @BindView(R.id.add_image) pl.droidsonroids.gif.GifImageView add_image;
//    private DeviceGroupDaoImpl deviceGroupDao;
    private DeviceChildDaoImpl deviceChildDao;

    int[] wifi_drawables = {R.drawable.shape_btnwifi_connect, R.drawable.shape_btnwifi_noconnect};
    int[] wifi_colors = new int[2];

    int[] scan_drawables = {R.drawable.shape_btnzxscan_connect, R.drawable.shape_btnzxscan_noconnect};
    @BindView(R.id.linearout_add_wifi_device)
    LinearLayout linearout_add_wifi_device;
    @BindView(R.id.linearout_add_scan_device)
    LinearLayout linearout_add_scan_device;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.linear) LinearLayout linear;
    int[] visibilities = {View.GONE, View.VISIBLE};
    int visibility;
    int wifi_drawable;
    int wifi_color;

    int scan_drawable;


    private String userId;
    private String wifiConnectionUrl = "http://47.98.131.11:8082/warmer/v1.0/device/registerDevice";
    private String qrCodeConnectionUrl = "http://47.98.131.11:8082/warmer/v1.0/device/createShareDevice";

//    private AddDeviceDialog addDeviceDialog;

    GifDrawable gifDrawable;
    int getAlpha;
    int getAlpha2;

    float alpha=0;
    WindowManager.LayoutParams lp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);
        add_image= (GifImageView) findViewById(R.id.add_image);


        getAlpha=linear.getBackground().mutate().getAlpha();
        getAlpha2=add_image.getBackground().mutate().getAlpha();

        mWifiAdmin = new EspWifiAdminSimple(this);
        SharedPreferences my = getSharedPreferences("my", MODE_PRIVATE);
        userId = my.getString("userId", "");
        for (int i = 0; i < wifi_colors.length; i++) {
            if (0 == i) {
                wifi_colors[0] = getResources().getColor(R.color.white);
            } else if (1 == i) {
                wifi_colors[1] = getResources().getColor(R.color.color_blue);
            }
        }
        if (application == null) {
            application = (MyApplication) getApplication();
        }
        application.addActivity(this);



        String wifi = "wifi";

        if (!TextUtils.isEmpty(wifi)) {
            if ("wifi".equals(wifi)) {
                linearout_add_wifi_device.setVisibility(View.VISIBLE);
                linearout_add_scan_device.setVisibility(View.GONE);
                btn_wifi.setVisibility(View.GONE);
                btn_scan.setVisibility(View.GONE);


            } else if ("share".equals(wifi)) {
                linearout_add_scan_device.setVisibility(View.VISIBLE);
                linearout_add_wifi_device.setVisibility(View.GONE);
                btn_wifi.setVisibility(View.GONE);
                btn_scan.setVisibility(View.GONE);
            }
            btn_wifi.setVisibility(View.GONE);
            btn_scan.setVisibility(View.GONE);
        }
        et_pswd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }

    @OnClick({R.id.img_back, R.id.btn_wifi, R.id.btn_scan, R.id.btn_scan2, R.id.btn_match})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                Log.i("dialog","sssssss");
                if (gifDrawable!=null && gifDrawable.isPlaying()){
                    gifDrawable.stop();
                    add_image.setVisibility(View.GONE);
                    et_ssid.setEnabled(true);
                    et_pswd.setEnabled(true);
                    btn_match.setEnabled(true);
                    break;
                }
                finish();
                break;
            case R.id.btn_wifi:
                wifi_drawable = wifi_drawables[1];
                wifi_drawables[1] = wifi_drawables[0];
                wifi_drawables[0] = wifi_drawable;

                scan_drawable = scan_drawables[0];
                scan_drawables[0] = scan_drawables[1];
                scan_drawables[1] = scan_drawable;

                wifi_color = wifi_colors[1];
                wifi_colors[1] = wifi_colors[0];
                wifi_colors[0] = wifi_color;

                btn_wifi.setBackgroundResource(wifi_drawable);
                btn_scan.setBackgroundResource(scan_drawable);
                btn_wifi.setTextColor(wifi_colors[0]);
                btn_scan.setTextColor(wifi_colors[1]);
                linearout_add_wifi_device.setVisibility(View.GONE);

                visibility = visibilities[1];
                visibilities[1] = visibilities[0];
                visibilities[0] = visibility;
                linearout_add_wifi_device.setVisibility(visibilities[1]);
                linearout_add_scan_device.setVisibility(visibilities[0]);
                break;
            case R.id.btn_scan:
                wifi_drawable = wifi_drawables[1];
                wifi_drawables[1] = wifi_drawables[0];
                wifi_drawables[0] = wifi_drawable;

                scan_drawable = scan_drawables[0];
                scan_drawables[0] = scan_drawables[1];
                scan_drawables[1] = scan_drawable;

                wifi_color = wifi_colors[1];
                wifi_colors[1] = wifi_colors[0];
                wifi_colors[0] = wifi_color;

                btn_wifi.setBackgroundResource(wifi_drawable);
                btn_scan.setBackgroundResource(scan_drawable);
                btn_wifi.setTextColor(wifi_colors[0]);
                btn_scan.setTextColor(wifi_colors[1]);

                visibility = visibilities[1];
                visibilities[1] = visibilities[0];
                visibilities[0] = visibility;
                linearout_add_wifi_device.setVisibility(visibilities[1]);
                linearout_add_scan_device.setVisibility(visibilities[0]);
                break;
            case R.id.btn_scan2:
                startActivity(new Intent(this,QRScannerActivity.class));
//                scanQrCode();
//                sharedDeviceId="userId";
//                Map<String,Object> params2=new HashMap<>();
//                params2.put("deviceId","1067");
//                params2.put("userId",userId);
//                new QrCodeAsync().execute(params2);
                break;
            case R.id.btn_match:

                String ssid = et_ssid.getText().toString();
                String apPassword = et_pswd.getText().toString();
                String apBssid = mWifiAdmin.getWifiConnectedBssid();
                String taskResultCountStr = "1";
                if (__IEsptouchTask.DEBUG) {
//                    Log.d(TAG, "mBtnConfirm is clicked, mEdtApSsid = " + apSsid
//                            + ", " + " mEdtApPassword = " + apPassword);
                }
                if (com.xr.happyFamily.login.util.Utils.isEmpty(apPassword)){
                    break;
                }
                if (!com.xr.happyFamily.login.util.Utils.isEmpty(ssid)) {
//                    popupWindow();
                    add_image.setVisibility(View.VISIBLE);
                    et_ssid.setEnabled(false);
                    et_pswd.setEnabled(false);
                    btn_match.setEnabled(false);
                    try {
                        gifDrawable=new GifDrawable(getResources(),R.mipmap.touxiang3);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if (gifDrawable!=null){
                        gifDrawable.start();
                        add_image.setImageDrawable(gifDrawable);
                    }


//                    lp.alpha = 0.4f;
//                    getWindow().setAttributes(lp);
//                    linear.getBackground().mutate().setAlpha(100);

                    add_image.getBackground().mutate().setAlpha(0);
//                    add_image.getBackground().mutate().setAlpha((int) alpha);

//                    WindowManager.LayoutParams lp=getWindow().getAttributes();
//                    lp.alpha = 0.4f;
//                    getWindow().setAttributes(lp);

                    new EsptouchAsyncTask3().execute(ssid, apBssid, apPassword, taskResultCountStr);
//                    String macAddress="vlinks_test18d634d6d3c6";
//                    Map<String, Object> params = new HashMap<>();
//                    params.put("deviceName", "设备3");
//                    params.put("houseId", houseId);
//                    params.put("masterControllerUserId", Integer.parseInt(userId));
//                    params.put("type", 1);
//                    params.put("macAddress", macAddress);
//                    new WifiConectionAsync().execute(params);

                }
                break;
        }
    }



    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // display the connected ap's ssid
        String apSsid = mWifiAdmin.getWifiConnectedSsid();
        if (apSsid != null) {
            et_ssid.setText(apSsid);
        } else {
            et_ssid.setText("");
        }
        // check whether the wifi is connected
        boolean isApSsidEmpty = TextUtils.isEmpty(apSsid);
        btn_match.setEnabled(!isApSsidEmpty);
        SharedPreferences wifi = getSharedPreferences("wifi", MODE_PRIVATE);
        if (wifi.contains(et_ssid.getText().toString())) {
            String pswd = wifi.getString(et_ssid.getText().toString(), "");
            et_pswd.setText(pswd);
            et_pswd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        Log.i("AddDevice","-->"+"onResume");

        running=true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("AddDevice","-->"+"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("AddDevice","-->"+"onStop");
        running=false;
    }

    String shareDeviceId;
    String shareContent;
    String shareMacAddress;

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (intentResult != null) {
//            if (intentResult.getContents() == null) {
//                Toast.makeText(this, "内容为空", Toast.LENGTH_LONG).show();
//            } else {
//                String content = intentResult.getContents();
//                if (!Utils.isEmpty(content)) {
//                    content = new String(Base64.decode(content, Base64.DEFAULT));
//                    if (!Utils.isEmpty(content)) {
//                        String[] ss = content.split("&");
//                        String s0 = ss[0];
//                        String deviceId = s0.substring(s0.indexOf("'") + 1);
//                        String s2 = ss[2];
//                        String macAddress = s2.substring(s2.indexOf("'") + 1);
//                        shareMacAddress = macAddress;
//                        Map<String, Object> params = new HashMap<>();
//                        params.put("deviceId", deviceId);
//                        params.put("userId", userId);
//                        new QrCodeAsync().execute(params);
//                    }
//
//
//                }
////                tv_result.setText(content);
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

//    class LoadUserInfoAsync extends AsyncTask<String,Void,Void>{
//
//        @Override
//        protected Void doInBackground(String.. voids) {
//            return null;
//        }
//    }

//    /**
//     * 扫描二维码
//     */
//    public void scanQrCode() {
//
//        IntentIntegrator integrator = new IntentIntegrator(AddDeviceActivity.this);
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
//        integrator.setCaptureActivity(ScanActivity.class);
//        integrator.setPrompt("请扫描二维码"); //底部的提示文字，设为""可以置空
//        integrator.setCameraId(0); //前置或者后置摄像头
//        integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
//        integrator.setBarcodeImageEnabled(true);//是否保留扫码成功时候的截图
//        integrator.initiateScan();
//    }

    private static final String TAG = "Esptouch";
    private EspWifiAdminSimple mWifiAdmin;





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

    private int type;
    int count = 0;


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

//            mProgressDialog = new ProgressDialog(AddDeviceActivity.this);
//            mProgressDialog
//                    .setMessage("正在配置, 请耐心等待...");
//            mProgressDialog.setCanceledOnTouchOutside(false);
//            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    synchronized (mLock) {
//                        if (__IEsptouchTask.DEBUG) {
//                            Log.i(TAG, "progress dialog is canceled");
//                        }
//                        if (mEsptouchTask != null) {
//                            mEsptouchTask.interrupt();
//                        }
//                    }
//                }
//            });
//            mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE,
//                    "Waiting...", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    });
//            mProgressDialog.show();
//            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
//                    .setEnabled(false);
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
                mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, Demo.this);
                mEsptouchTask.setEsptouchListener(myListener);
            }
            List<IEsptouchResult> resultList = mEsptouchTask.executeForResults(taskResultCount);
            return resultList;
        }

        @Override
        protected void onPostExecute(List<IEsptouchResult> result) {
//            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
//                    .setEnabled(true);
//            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(
//                    "确认");
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
                        DeviceChild deviceChild = new DeviceChild();
                        String ssid = resultInList.getBssid();
                        if (!TextUtils.isEmpty(ssid)) {
                            String s = ssid.substring(0, 1);
                            type = Integer.parseInt(s);
                            if (type == 0)
                                type = 2;
                        }

                        sb.append("配置成功");
                        count++;
                        if (count >= maxDisplayCount) {
                            break;
                        }
                    }
                    if (count < result.size()) {
                        sb.append("\nthere's " + (result.size() - count)
                                + " more result(s) without showing\n");
                    }
                    Toast.makeText(Demo.this,"配置成功",Toast.LENGTH_SHORT).show();
//                    mProgressDialog.setMessage(sb.toString());
                } else {


                    if (running){
                        if (gifDrawable!=null && gifDrawable.isPlaying()){
                            gifDrawable.stop();
                            if (add_image!=null){
                                add_image.setVisibility(View.GONE);
                                et_ssid.setEnabled(true);
                                et_pswd.setEnabled(true);
                                btn_match.setEnabled(true);
                            }
                        }
                        Toast.makeText(Demo.this,"配置失败",Toast.LENGTH_SHORT).show();
//                        Utils.showToast(AddDeviceActivity.this,"配置失败");
                    }
//                    mProgressDialog.setMessage("配置失败");
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (gifDrawable!=null && gifDrawable.isPlaying()){
            gifDrawable.stop();
            add_image.setVisibility(View.GONE);
            et_ssid.setEnabled(true);
            et_pswd.setEnabled(true);
            btn_match.setEnabled(true);
            return;
        }

        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
