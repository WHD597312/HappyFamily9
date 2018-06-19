package com.xr.happyFamily.jia.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.xr.happyFamily.jia.MyPaperActivity;
import com.xr.happyFamily.jia.pojo.DeviceChild;


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

    GifDrawable gifDrawable;

    DeviceChildDaoImpl deviceChildDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        unbinder=ButterKnife.bind(this);
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
//                    popupWindow();
//                    image_gif.setVisibility(View.VISIBLE);
//                    nice_spinner.setEnabled(false);
//                    et_wifi.setEnabled(false);
//                    bt_add_finish.setEnabled(false);
//                    try {
//                        gifDrawable=new GifDrawable(getResources(),R.mipmap.touxiang3);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    if (gifDrawable!=null){
//                        gifDrawable.start();
//                        image_gif.setImageDrawable(gifDrawable);
//                    }


//                    lp.alpha = 0.4f;
//                    getWindow().setAttributes(lp);
//                    linear.getBackground().mutate().setAlpha(100);

//                    image_gif.getBackground().mutate().setAlpha(0);
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
    }
}
