package com.xr.happyFamily.jia.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.mikephil.charting.formatter.IFillFormatter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.IsBase64;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.camera.CameraManager;
import com.xr.happyFamily.together.util.decoding.CaptureActivityHandler;
import com.xr.happyFamily.together.util.decoding.InactivityTimer;
import com.xr.happyFamily.together.util.mqtt.MQService;
import com.xr.happyFamily.together.util.view.ViewfinderView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class QRScannerActivity extends AppCompatActivity implements SurfaceHolder.Callback,EasyPermissions.PermissionCallbacks {

    ViewfinderView viewfinderView;
    private CaptureActivityHandler handler;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;

    String shareDeviceId;
    String shareContent;
    String shareMacAddress;
    private SharedPreferences mPositionPreferences;
    private String userId;

    ImageView back;
    Unbinder unbinder;
    MyApplication application;
    private long houseId;
    private String shareDeviceUrl = HttpUtils.ipAddress + "/family/device/createShareDevice";
    private DeviceChildDaoImpl deviceChildDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_qrscanner);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (application == null) {
            application = (MyApplication) getApplication();
            application.addActivity(this);
        }

        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        unbinder = ButterKnife.bind(this);
        init();

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            }
        }
        deviceChildDao=new DeviceChildDaoImpl(getApplicationContext());
        mPositionPreferences =getSharedPreferences("position", Context.MODE_PRIVATE);
        SharedPreferences my = getSharedPreferences("my", MODE_PRIVATE);
        userId = my.getString("userId", "");
        Intent intent = getIntent();
        houseId = intent.getLongExtra("houseId", 0);
        roomId=intent.getLongExtra("roomId",0);
        roomName=intent.getStringExtra("roomName");
        userId=intent.getStringExtra("userId");
        city=intent.getStringExtra("city");
        province=intent.getStringExtra("province");
        Intent service = new Intent(QRScannerActivity.this, MQService.class);
        isBound = bindService(service, connection, Context.BIND_AUTO_CREATE);
    }

    private void init() {
        CameraManager.init(getApplication());
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    //    DeviceChildDaoImpl deviceChildDao;
//    DeviceGroupDaoImpl deviceGroupDao;
    SharedPreferences preferences;

    private boolean isBound;
    @Override
    protected void onStart() {
        super.onStart();

//        deviceGroupDao = new DeviceGroupDaoImpl(getApplicationContext());
//        deviceChildDao = new DeviceChildDaoImpl(getApplicationContext());
//        preferences = getSharedPreferences("my", Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        permissionGrantedSuccess();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
//        initBeepSound();
        vibrate = true;
    }

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            application.removeActivity(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (isBound){
            unbindService(connection);
        }
        if (handler!=null){
            handler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 处理扫描结果
     */
    public void handleDecode(Result result) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        try {
            if (TextUtils.isEmpty(resultString)) {
                Toast.makeText(QRScannerActivity.this, "扫描失败!", Toast.LENGTH_SHORT).show();
            } else {
                String content = resultString;
                if (!Utils.isEmpty(content)) {
                    boolean isBase64=IsBase64.isBase64(content);
                    if (isBase64){
                        content = new String(Base64.decode(content, Base64.DEFAULT));
                        if (!Utils.isEmpty(content)) {
                            if (!content.contains("macAddress") && !content.contains("deviceId")){
                                Toast.makeText(QRScannerActivity.this, "扫描内容不符合!", Toast.LENGTH_SHORT).show();
                            }else {
                                shareContent = content;
                                String[] ss = content.split("&");
                                String s0 = ss[0];
                                String deviceId = s0.substring(s0.indexOf("'") + 1);
                                Map<String, Object> params = new HashMap<>();
                                params.put("deviceId", deviceId);
                                params.put("userId", userId);
                                new QrCodeAsync().execute(params);
                            }
                        }
                    }else {
                        if (content.startsWith("P99")){
                            String macAddress=content.substring(content.indexOf(":")+1);
                            Map<String, Object> params = new HashMap<>();
                            params.put("deviceName", macAddress);
                            params.put("deviceType", 0);
                            params.put("deviceMacAddress", macAddress);
                            params.put("houseId", houseId);
                            params.put("roomId", roomId);
                            params.put("userId", userId);
                            new AddDeviceInOldRoomAsync().execute(params);
                        }else {
                            Toast.makeText(QRScannerActivity.this, "扫描内容不符合!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
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
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog
                    .Builder(this)
                    .setTitle("提示")
                    .setRationale("请点击\"设置\"打开相机权限。")
                    .setPositiveButton("设置")
                    .setNegativeButton("取消")
                    .build()
                    .show();
            isNeedCheck=false;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (isNeedCheck){
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        }
    }

    private static final int PERMISSION_CAMERA=0;
    private boolean isNeedCheck=true;
    @AfterPermissionGranted(PERMISSION_CAMERA)
    private void permissionGrantedSuccess(){
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {

        } else {
//             没有申请过权限，现在去申请
            if (isNeedCheck){
                EasyPermissions.requestPermissions(this, getString(R.string.camer),
                        PERMISSION_CAMERA, perms);
            }
        }
    }


    class QrCodeAsync extends AsyncTask<Map<String, Object>, Void, Integer> {
        @Override
        protected Integer doInBackground(Map<String, Object>... maps) {
            int code = 0;
            Map<String, Object> params = maps[0];
            String result = HttpUtils.postOkHpptRequest(shareDeviceUrl, params);
            if (!Utils.isEmpty(result)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                   String returnCode = jsonObject.getString("returnCode");
                    if ("100".equals(returnCode)) {
                        code=100;
                        if (!Utils.isEmpty(shareContent)) {
                            String[] ss = shareContent.split("&");
                            String s0 = ss[0];
                            String deviceId = s0.substring(s0.indexOf("'") + 1);
                            String s2 = ss[2];
                            String macAddress = s2.substring(s2.indexOf("'") + 1);
                            shareMacAddress = macAddress;
                            String s3 = ss[3];
                            String typeType = s3.substring(s3.indexOf("'") + 1);
                            String s4 = ss[4];
                            String room = s4.substring(s4.indexOf("'") + 1);
                            String s6 = ss[6];
                            String deviceName = s6.substring(s6.indexOf("'") + 1);

                            DeviceChild deviceChild = new DeviceChild();
                            deviceChild.setDeviceId(Integer.parseInt(deviceId));
                            deviceChild.setMacAddress(macAddress);
                            deviceChild.setUserId(Integer.parseInt(userId));
                            deviceChild.setType(Integer.parseInt(typeType));
                            deviceChild.setName(deviceName);
                            deviceChild.setRoomId(Long.parseLong(room));
                            deviceChild.setShareId(Long.MAX_VALUE);
                            deviceChild.setShare("share");
                            List<DeviceChild> deleteDevices=deviceChildDao.findDeviceByMacAddress(macAddress);
                            if (deleteDevices!=null && !deleteDevices.isEmpty()){
                                deviceChildDao.deleteDevices(deleteDevices);
                            }

//                            List<DeviceChild> deviceChildren=deviceChildDao.findAllDevice();
//                            for (DeviceChild deviceChild2 : deviceChildren) {
//                                if (macAddress.equals(deviceChild2.getMacAddress())) {
//                                    deviceChildDao.delete(deviceChild2);
//                                    break;
//                                }
//                            }

                            deviceChildDao.insert(deviceChild);
                            int deviceType = deviceChild.getType();

                            String onlineTopicName = "";
                            String offlineTopicName = "";
                            if (2 == deviceType) {
                                onlineTopicName = "p99/warmer/" + macAddress + "/transfer";
                                offlineTopicName="p99/warmer/"+macAddress+"/lwt";
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
                            if (!TextUtils.isEmpty(onlineTopicName)) {
                                boolean success = mqService.subscribe(onlineTopicName, 1);
                                boolean success2 = mqService.subscribe(offlineTopicName, 1);
                                if (!success) {
                                    mqService.subscribe(onlineTopicName, 1);
                                }
                                if (!success2) {
                                    mqService.subscribe(offlineTopicName, 1);
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
                    mPositionPreferences.edit().clear().commit();
                    Utils.showToast(QRScannerActivity.this, "分享设备添加成功");
                    Intent intent2 = new Intent(QRScannerActivity.this, MainActivity.class);
                    intent2.putExtra("houseId",houseId);
                    intent2.putExtra("share","share");
                    startActivity(intent2);
                    break;
                default:
                    Utils.showToast(QRScannerActivity.this, "分享设备添加失败");
                    break;

            }
        }
    }

    long roomId;
    int mPosition;
    String roomName;
    String houseAddress;
    private String city="";
    private String province="";
    private String inOldRoom = HttpUtils.ipAddress + "/family/device/registerDeviceInOldRoom";
    class AddDeviceInOldRoomAsync extends AsyncTask<Map<String, Object>, Void, Integer> {

        @Override
        protected Integer doInBackground(Map<String, Object>... maps) {
            int code = 0;
            Map<String, Object> params = maps[0];
            String result = HttpUtils.postOkHpptRequest(inOldRoom, params);
            Log.i("resultAdddevice", "-->" + result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String returnCode = jsonObject.getString("returnCode");
                    if ("100".equals(returnCode)) {
                        code=100;

                        JSONObject returnData = jsonObject.getJSONObject("returnData");
                        int deviceType = returnData.getInt("deviceType");
                        String deviceMacAddress = returnData.getString("deviceMacAddress");
                        int deviceId = returnData.getInt("deviceId");
                        String deviceName = returnData.getString("deviceName");
                        List<DeviceChild> deleteDevices = deviceChildDao.findDeviceByMacAddress(deviceMacAddress);
                        if (deleteDevices != null && !deleteDevices.isEmpty()) {
                            deviceChildDao.deleteDevices(deleteDevices);
                        }
                        if (TextUtils.isEmpty(city)) {
                            city = houseAddress;
                            if (city.contains("市")) {
                                city = city.substring(0, city.length() - 1);
                            }
                        } else {
                            if (city.contains("市")) {
                                city = city.substring(0, city.length() - 1);
                            }
                        }
                        if (TextUtils.isEmpty(province)){
                            province="";
                        }
                        DeviceChild deviceChild = new DeviceChild();
                        deviceChild.setRoomName(roomName);
                        deviceChild.setName(deviceName);
                        deviceChild.setType(deviceType);
                        deviceChild.setDeviceId(deviceId);
                        deviceChild.setHouseId(houseId);
                        deviceChild.setRoomId(roomId);
                        deviceChild.setHouseAddress(city);
                        deviceChild.setMacAddress(deviceMacAddress);
                        deviceChild.setProvince(province);
                        deviceChildDao.insert(deviceChild);
                        String topicName2 = "p99/" + deviceMacAddress + "/transfer";
                        if (mqService != null) {
                            boolean success = mqService.subscribe(topicName2, 1);
                            if (success) {
                                String topicName = "p99/" + deviceMacAddress + "/set";
                                String payLoad = "getType";
                                boolean step2 = mqService.publish(topicName, 1, payLoad);
                                if (!step2){
                                    step2 = mqService.publish(topicName, 1, payLoad);
                                }
                            }
                        }

//                        String macAddress = deviceMacAddress;
//                        code = Integer.parseInt(returnCode);
//                        String onlineTopicName = "";
//                        String offlineTopicName = "";
//                        if (2 == deviceType) {
//                            onlineTopicName = "p99/warmer1/" + macAddress + "/transfer";
//                            offlineTopicName = "p99/warmer1/" + macAddress + "/lwt";
//                        } else if (3 == deviceType) {
//                            onlineTopicName = "p99/sensor1/" + macAddress + "/transfer";
//                            offlineTopicName = "p99/sensor1/" + macAddress + "/lwt";
//                        } else if (4 == deviceType) {
//                            onlineTopicName = "p99/socket1/" + macAddress + "/transfer";
//                            offlineTopicName = "p99/socket1/" + macAddress + "/lwt";
//                        } else if (5 == deviceType) {
//                            onlineTopicName = "p99/dehumidifier1/" + macAddress + "/transfer";
//                            offlineTopicName = "p99/dehumidifier1/" + macAddress + "/lwt";
//                        } else if (6 == deviceType) {
//                            onlineTopicName = "p99/aConditioning1/" + macAddress + "/transfer";
//                            offlineTopicName = "p99/aConditioning1/" + macAddress + "/lwt";
//                        } else if (7 == deviceType) {
//                            onlineTopicName = "p99/aPurifier1/" + macAddress + "/transfer";
//                            offlineTopicName = "p99/aPurifier1/" + macAddress + "/lwt";
//                        } else if (8 == deviceType) {
//                            onlineTopicName = "p99/wPurifier1/" + macAddress + "/transfer";
//                            offlineTopicName = "p99/wPurifier1/" + macAddress + "/lwt";
//                        }
//                        if (!TextUtils.isEmpty(onlineTopicName) && !TextUtils.isEmpty(offlineTopicName)) {
//                            boolean success = mqService.subscribe(onlineTopicName, 1);
//                            boolean success2 = mqService.subscribe(offlineTopicName, 1);
//                            if (!success) {
//                                mqService.subscribe(onlineTopicName, 1);
//                            }
//                            if (!success2) {
//                                mqService.subscribe(offlineTopicName, 1);
//                            }
//                            if (deviceType == 3) {
//                                String topicName = "p99/sensor1/" + macAddress + "/set";
//                                if (TextUtils.isEmpty(city)) {
//                                    city = houseAddress;
//                                    if (city.contains("市")) {
//                                        city = city.substring(0, city.length() - 1);
//                                    }
//                                    String info = "url:http://apicloud.mob.com/v1/weather/query?key=257a640199764&city=" + URLEncoder.encode(city, "utf-8");
//                                    mqService.publish(topicName, 1, info);
//                                } else {
//                                    if (city.contains("市")) {
//                                        city = city.substring(0, city.length() - 1);
//                                    }
//                                    String info = "url:http://apicloud.mob.com/v1/weather/query?key=257a640199764&city=" + URLEncoder.encode(city, "utf-8") + "&province=" + URLEncoder.encode(province, "utf-8");
//                                    mqService.publish(topicName, 1, info);
//                                }
//                            }
//                        }
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
                    Toast.makeText(QRScannerActivity.this, "添加设备成功", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(QRScannerActivity.this,MainActivity.class);
                    intent.putExtra("houseId", houseId);
                    intent.putExtra("room","room");
                    startActivity(intent);
                    break;
                    default:
                        Toast.makeText(QRScannerActivity.this, "添加设备设备", Toast.LENGTH_LONG).show();
                        break;

            }
        }
    }
    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
//            handler = new CaptureActivityHandler(QRScannerActivity.this, decodeFormats, characterSet);
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }


    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

}