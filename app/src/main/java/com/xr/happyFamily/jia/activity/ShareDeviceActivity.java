package com.xr.happyFamily.jia.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.base.ToastUtil;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.view_custom.DialogLoad;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.share.PlatformUtil;
import com.xr.happyFamily.together.util.BitmapCompressUtils;
import com.xr.happyFamily.together.util.GlideCacheUtil;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.ZXingUtils;
import com.xr.happyFamily.together.util.mqtt.MQService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ShareDeviceActivity extends AppCompatActivity {

    Unbinder unbinder;
    private DeviceChild deviceChild;
    private SharedPreferences preferences;
    private String createQRCode = HttpUtils.ipAddress + "/family/device/createQRCode";
    private String getDeviceVersion=HttpUtils.ipAddress+"/family/device/getVersions";
    @BindView(R.id.img_qrCode)
    ImageView img_qrCode;
    @BindView(R.id.tv_version) TextView tv_version;
    long houseId;
    long id;
    /**
     * 二维码
     */
    @BindView(R.id.tv_device)
    TextView tv_device;
    @BindView(R.id.btn_update_version2)
    Button btn_update_version2;
    /**
     * 设备名称
     */
    @BindView(R.id.image_more)
    ImageView image_more;
    /**
     * 分享图片
     */
    private MyApplication application;
    private int deviceId;
    private DeviceChildDaoImpl deviceChildDao;
    File file;
    private Bitmap mBitmap;
    int type;
    public static boolean running=false;

    MessageReceiver receiver;
    String share;
    int wifiVersion;//取暖器服务端设备wifi版本
    int screen;//取暖器服务端设备有屏mcu版本
    int noScreen;//取暖器服务端设备没屏mcu版本


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("share","-->onRestart");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_device);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        unbinder = ButterKnife.bind(this);
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        if (application == null) {
            application = (MyApplication) getApplication();
            application.addActivity(this);
        }
        deviceChildDao = new DeviceChildDaoImpl(getApplicationContext());

        Log.i("share","-->onCreate");
        Intent intent = getIntent();
        id = intent.getLongExtra("deviceId", 0);
        deviceChild = deviceChildDao.findById(id);
        houseId=deviceChild.getHouseId();
        type=deviceChild.getType();
        String userId=preferences.getString("userId","");
        if (deviceChild != null) {
            if (deviceChild.getType()==9){
                tv_version.setText(deviceChild.getWVerion()+","+deviceChild.getMVersion());
                new GetDeviceVersionAsync().execute();
            }else {
                tv_version.setText(deviceChild.getWifiVersion()+","+deviceChild.getMcuVersion());
            }
            String name = deviceChild.getName();
            tv_device.setText(name);
            int deviceId=deviceChild.getDeviceId();
            String macAddress=deviceChild.getMacAddress();
            int type=deviceChild.getType();
            long roomId=deviceChild.getRoomId();
            long houseId=deviceChild.getHouseId();
            String deviceName=deviceChild.getName();
            share="deviceId'"+deviceId+"&userId'"+userId+"&macAddress'"+macAddress+"&deviceType'"+type+"&roomId'"+roomId+"&houseId'"+houseId+"&deviceName'"+deviceName;
            try {
                share=new String(Base64.encode(share.getBytes("utf-8"), Base64.NO_WRAP),"UTF-8");
                createQrCode();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        Intent serice=new Intent(this,MQService.class);
        bind=bindService(serice,connection,Context.BIND_AUTO_CREATE);
        IntentFilter intentFilter = new IntentFilter("ShareDeviceActivity");
        receiver = new MessageReceiver();
        registerReceiver(receiver, intentFilter);
//        new ShareQrCodeAsync().execute();
    }

    @OnClick({R.id.back, R.id.image_more,R.id.btn_update_version,R.id.btn_update_version2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                Intent intent=new Intent();
                intent.putExtra("deviceChild",deviceChild);
                setResult(8000,intent);
                finish();
                break;
            case R.id.image_more:
                popupShare();
                break;
            case R.id.btn_update_version:
                if (type!=9){
                    new GetDeviceVersionAsync().execute();
                }else {
                    if (isDialogShowing()){
                        break;
                    }
                    if (mqService!=null){
                        if (deviceChild.getWarmerScreen()==1){
                            if (wifiVersion!=deviceChild.getwVerion() && noScreen!=deviceChild.getMVersion()){
                                mqService.requestUpdateGrade(deviceChild,3,1,wifiVersion,noScreen);
                                countTimer.start();
                            } else if (wifiVersion!=deviceChild.getwVerion()){
                                mqService.requestUpdateGrade(deviceChild,1,1,wifiVersion,noScreen);
                                countTimer.start();
                            }else if (noScreen!=deviceChild.getMVersion()){
                                mqService.requestUpdateGrade(deviceChild,2,1,wifiVersion,noScreen);
                                countTimer.start();
                            }else {
                                ToastUtil.showShortToast("设备不需要升级");
                            }
                        }else if (deviceChild.getWarmerScreen()==2){
                            if (wifiVersion!=deviceChild.getwVerion() && screen!=deviceChild.getMVersion()){
                                mqService.requestUpdateGrade(deviceChild,3,2,wifiVersion,screen);
                                countTimer.start();
                            }else if (wifiVersion!=deviceChild.getwVerion()){
                                mqService.requestUpdateGrade(deviceChild,1,2,wifiVersion,screen);
                                countTimer.start();
                            }else if (screen!=deviceChild.getMVersion()){
                                mqService.requestUpdateGrade(deviceChild,2,2,wifiVersion,screen);
                                countTimer.start();
                            }else {
                                ToastUtil.showShortToast("设备不需要升级");
                            }
                        }
                    }
                }
                break;
        }
    }
    private boolean isDialogShowing() {
        if (dialogLoad != null && dialogLoad.isShowing()) {
            ToastUtil.showShortToast("请稍后...");
            return true;
        }
        return false;
    }
    CountTimer countTimer = new CountTimer(2000, 1000);

    class CountTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            setLoadDialog();
            Log.e("CountDownTimer", "-->" + millisUntilFinished);
        }

        @Override
        public void onFinish() {
            if (dialogLoad != null && dialogLoad.isShowing()) {
                dialogLoad.dismiss();
            }
        }
    }
    DialogLoad dialogLoad;

    private void setLoadDialog() {
        if (dialogLoad != null && dialogLoad.isShowing()) {
            return;
        }

        dialogLoad = new DialogLoad(this);
        dialogLoad.setCanceledOnTouchOutside(false);
        dialogLoad.setLoad("正在加载,请稍后");
        dialogLoad.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("share","-->onResume");
    }

    @Override
    protected void onPause() {
        Log.i("share","-->onPause");
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        running=true;
        deviceChild = deviceChildDao.findById(id);
        if (deviceChild==null){
            Toast.makeText(ShareDeviceActivity.this, "该设备已重置", Toast.LENGTH_SHORT).show();
            Intent data = new Intent(ShareDeviceActivity.this, MainActivity.class);
            data.putExtra("houseId", houseId);
            startActivity(data);
        }
    }

    boolean bind;
    MQService mqService;
    ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MQService.LocalBinder binder= (MQService.LocalBinder) service;
            mqService=binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private PopupWindow popupWindow;

    private void popupShare() {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        View view = View.inflate(this, R.layout.popview_share_device, null);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        TextView tv_save_phone = (TextView) view.findViewById(R.id.tv_save_phone);
        TextView tv_send_wechat = (TextView) view.findViewById(R.id.tv_send_wechat);
        TextView tv_send_qq = (TextView) view.findViewById(R.id.tv_send_qq);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);


        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //添加弹出、弹入的动画
        popupWindow.setAnimationStyle(R.style.Popupwindow);

        ColorDrawable dw = new ColorDrawable(0x30000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAtLocation(img_qrCode, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //添加按键事件监听

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_save_phone:
                        Utils.showToast(ShareDeviceActivity.this,"保存成功");
                        break;
                    case R.id.tv_send_wechat:
                        if (mBitmap!=null){
                            PlatformUtil.shareWechatFriend(ShareDeviceActivity.this, mBitmap);
                        }
                        break;
                    case R.id.tv_send_qq:
                        if (mBitmap!=null){
                            PlatformUtil.shareImageToQQ(ShareDeviceActivity.this,mBitmap);
                        }
                        break;
                    case R.id.tv_cancel:
                        popupWindow.dismiss();
                        break;

                }
            }
        };

        tv_save_phone.setOnClickListener(listener);
        tv_send_wechat.setOnClickListener(listener);
        tv_send_qq.setOnClickListener(listener);
        tv_cancel.setOnClickListener(listener);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent=new Intent();
            intent.putExtra("deviceChild",deviceChild);
            setResult(8000,intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (popupWindow!=null){
            popupWindow.dismiss();
        }

        Log.i("share","-->onStop");
        running=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("share","-->onDestroy");
        if (popupWindow!=null){
            popupWindow.dismiss();
        }
//        if (mBitmap!=null){
//            BitmapCompressUtils.recycleBitmap(mBitmap);
//        }
        if (receiver!=null){
            unregisterReceiver(receiver);
        }
        if (bind){
            unbindService(connection);
        }
    }
    /**生成二维码*/
    private void createQrCode(){
        Bitmap bitmap = ZXingUtils.createQRImage(share,400, 400);
        img_qrCode.setImageBitmap(bitmap);
        mBitmap=bitmap;
    }

    class ShareQrCodeAsync extends AsyncTask<Void, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Void... maps) {
            Bitmap bitmap = null;
            if (deviceChild != null) {
                long deviceId = deviceChild.getDeviceId();
                String token = preferences.getString("token", "token");
                String userId = preferences.getString("userId", "");
                String url = createQRCode + "?deviceId=" + deviceId + "&userId=" + userId;
                try {
                    GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder().addHeader("authorization", token).build());
                    bitmap = Glide.with(ShareDeviceActivity.this)
                            .load(glideUrl)
                            .asBitmap()
                            .centerCrop()
                            .into(180, 180)
                            .get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            try {
                if (bitmap != null) {
                    img_qrCode.setImageBitmap(bitmap);
                    mBitmap=bitmap;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void onSaveBitmap(Bitmap mBitmap) {
        // 第一步：首先保存图片
        //将Bitmap保存图片到指定的路径/sdcard/Boohee/下，文件名以当前系统时间命名,但是这种方法保存的图片没有加入到系统图库中
        File appDir = new File(getExternalCacheDir(), "p99");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        // 第二步：其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
////   /storage/emulated/0/Boohee/1493711988333.jpg
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        // 第三步：最后通知图库更新
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
        //context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }
    class GetDeviceVersionAsync extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            int code = 0;
            String s=null;
            String url = getDeviceVersion + "?type="+type;
            String result = HttpUtils.getOkHpptRequest(url);
            Log.i("result2", "-->" + result);
            try {
                if (!TextUtils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    String returnCode = jsonObject.getString("returnCode");
                    if ("100".equals(returnCode)) {
                        JSONArray returnData=jsonObject.getJSONArray("returnData");
                        String wifiVersion2=returnData.getString(0);
                        String mcuVersion2=returnData.getString(1);
                        if (type==9){
                            String mcuVersion3=returnData.getString(2);
                            mcuVersion3=mcuVersion3.substring(1);
                            wifiVersion=Integer.parseInt(wifiVersion2.substring(1));
                            screen=Integer.parseInt(mcuVersion2.substring(1));
                            noScreen=Integer.parseInt(mcuVersion3);
                        }
                        if ("null".equals(wifiVersion2)){
                            wifiVersion2=deviceChild.getWifiVersion();
                        }
                        if ("null".equals(mcuVersion2)){
                            mcuVersion2=deviceChild.getMcuVersion();
                        }
                        if (type!=9){
                            s=wifiVersion2+","+mcuVersion2;
                        }else {
                            if (deviceChild.getWarmerScreen()==1){
                                s=wifiVersion+","+noScreen;
                            }else if (deviceChild.getWarmerScreen()==2){
                                s=wifiVersion+","+screen;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!TextUtils.isEmpty(s)){
                if (type!=9){
                    Utils.showToast(ShareDeviceActivity.this,"更新成功");
                    tv_version.setText(s);
                }else {
                    tv_version.setText(s);
                }
            }
        }
    }
    class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String macAddress=intent.getStringExtra("macAddress");
                String noNet=intent.getStringExtra("noNet");
                DeviceChild deviceChild2= (DeviceChild) intent.getSerializableExtra("deviceChild");
                if (!Utils.isEmpty(noNet)){
                    Utils.showToast(ShareDeviceActivity.this,"网络已断开，请设置网络");
                }else {
                    if (!Utils.isEmpty(macAddress) && deviceChild2==null && deviceChild!=null && deviceChild.getMacAddress().equals(macAddress)){
                        Utils.showToast(ShareDeviceActivity.this,"该设备已被重置");
                        Intent intent2=new Intent(ShareDeviceActivity.this,MainActivity.class);
                        intent2.putExtra("houseId",houseId);
                        startActivity(intent2);
                    }else if (!Utils.isEmpty(macAddress) && deviceChild2!=null && deviceChild!=null && deviceChild.getMacAddress().equals(macAddress)){
                        deviceChild=deviceChild2;
                        deviceChildDao.update(deviceChild);
                        tv_version.setText(deviceChild.getWifiVersion()+","+deviceChild.getMcuVersion());
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
