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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
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

public class QRScannerActivity extends AppCompatActivity implements SurfaceHolder.Callback {

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
        Intent service = new Intent(QRScannerActivity.this, MQService.class);
        isBound = bindService(service, connection, Context.BIND_AUTO_CREATE);
//        deviceGroupDao = new DeviceGroupDaoImpl(getApplicationContext());
//        deviceChildDao = new DeviceChildDaoImpl(getApplicationContext());
//        preferences = getSharedPreferences("my", Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            handler = null;
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
    }

    /**
     * 处理扫描结果
     */
    public void handleDecode(Result result) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();

        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(QRScannerActivity.this, "扫描失败!", Toast.LENGTH_SHORT).show();
        } else {
            String content = resultString;
            if (!Utils.isEmpty(content)) {
                content = new String(Base64.decode(content, Base64.DEFAULT));
//                Toast.makeText(QRScannerActivity.this, content, Toast.LENGTH_SHORT).show();
                if (!Utils.isEmpty(content)) {
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
                            deviceChild.setType(Integer.parseInt(typeType));
                            deviceChild.setName(deviceName);
                            deviceChild.setShareId(Long.MAX_VALUE);
                            deviceChildDao.insert(deviceChild);
                            int deviceType = deviceChild.getType();

                            String onlineTopicName = "";
                            String offlineTopicName = "";
                            if (2 == deviceType) {
                                onlineTopicName = "p99/warmer/" + macAddress + "/transfer";
                                offlineTopicName = "p99/warmer/" + macAddress + "/lwt";
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
                    startActivity(intent2);
                    break;
                default:
                    Utils.showToast(QRScannerActivity.this, "分享设备添加失败");
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