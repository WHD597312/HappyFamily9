package com.xr.happyFamily.le.view;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.xr.happyFamily.R;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.mqtt.ClockService;

import org.json.JSONObject;
import java.io.IOException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 制懒闹钟听歌识曲
 */
public class btClockjsDialog4 extends Dialog {


    @BindView(R.id.et_tg_name)
    EditText et_tg_name;
    @BindView(R.id.bt_tg_qd)
    Button bt_tg_qd;
    String ip = "http://47.98.131.11:8084";
    private String name;
    private AudioManager audioMa;
    String text;
    Context mcontext;
    private MediaPlayer mediaPlayer;
    SharedPreferences preferences;
    Intent service;
    boolean isBound;
    public btClockjsDialog4(@NonNull Context context) {
        super(context, R.style.MyDialog);
        mcontext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_le_zldialog4);
        ButterKnife.bind(this);
       new getMusicAsync().execute();
        audioMa = (AudioManager)mcontext.getSystemService(Context.AUDIO_SERVICE);
        audioMa.setStreamVolume(AudioManager.STREAM_MUSIC,audioMa.getStreamMaxVolume
                (AudioManager.STREAM_MUSIC),AudioManager.FLAG_SHOW_UI);
        service = new Intent(mcontext, ClockService.class);

        isBound = mcontext.bindService(service, connection, Context.BIND_AUTO_CREATE);
        preferences=mcontext.getSharedPreferences("trueCount",Context.MODE_PRIVATE);
//        Window win =getWindow();
//        WindowManager.LayoutParams winParams = win.getAttributes();
//        winParams.flags |= (WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
//                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound){
            mcontext.unbindService(connection);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    ClockService mqService;
    boolean bound;
    ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ClockService.LocalBinder binder = (ClockService.LocalBinder) service;
            mqService = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

   public String getText(){
        return text;
   }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OnClick({R.id.bt_tg_qd})
    public void onClick(View view){
        switch(view.getId()){

            case R.id.bt_tg_qd:
                if (onPositiveClickListener!=null){

                    onPositiveClickListener.onPositiveClick();
                }
                String name = et_tg_name.getText().toString();
                if (name.equals(songName)){
                    mediaPlayer.stop();
                    dismiss();
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putBoolean("ring",false);
                    editor.commit();
                    if (mqService != null) {
                        mqService.startClock();
                    }
//                    Calendar c=Calendar.getInstance();//c：当前系统时间
//                    AlarmManager am = (AlarmManager) mcontext.getSystemService(Context.ALARM_SERVICE);
//                    Intent intent1=new Intent("com.zking.android29_alarm_notification.RING");
//
//                    PendingIntent sender = PendingIntent.getBroadcast(mcontext, 0x101, intent1,
//                            PendingIntent.FLAG_CANCEL_CURRENT);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        am.setWindow(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 0, sender);
//                    } else {
//                        am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
//                    }
                }else {
                    Toast.makeText(mcontext, "输入错误请从新输入", Toast.LENGTH_SHORT).show();

                }
                break;






        }
    }

    String songName;
    String songUrl;
    class getMusicAsync extends AsyncTask<Void,Void,Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            int code=0;
            String url=ip+"/happy/clock/getClockLazyMusic";
            String result= HttpUtils.getOkHpptRequest(url);
            Log.i("result","-->"+result);
            try {
                if (!TextUtils.isEmpty(result)){
                    JSONObject jsonObject=new JSONObject(result);
                    String returnCode=jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");

                    if ("100".equals(returnCode)) {
                        code = 100;
                        songName = returnData.getString("songName");
                         songUrl = returnData.getString("songUrl");


                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return code;
        }
        @Override
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            switch (code){
                case 100:
//                    if(mediaPlayer==null){

//实例化MediaPlayer

//设置类型
                        mediaPlayer=new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//设置音源
                        try {

//播放网络音乐

                            mediaPlayer.setDataSource(mcontext, Uri.parse(songUrl));

//准备(网络)
//                                mediaPlayer.prepareAsync();
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            mediaPlayer.setLooping(true);

                        } catch (IOException e) {

                            e.printStackTrace();

                        }

//监听：准备完成的监听

//                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//
//                                @Override
//
//                                public void onPrepared(MediaPlayer mediaPlayer) {
//
//                                    mediaPlayer.start();
//
//
//                                }
//
//                            });

//                    }




                    break;
                default:

                    break;
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
// TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            Log.i("11111", "======KEYCODE_VOLUME_DOWN======>>>");
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            Log.i("11111", "======KEYCODE_VOLUME_UP======>>>");
            return true;
        }
        return false;
    }


    private   OnKeyListener onKeyListener;
    private OnPositiveClickListener onPositiveClickListener;

    public void setOnKeyListener(OnKeyListener onKeyListener){
        this.onKeyListener=onKeyListener;
    }
    public void setOnPositiveClickListener(OnPositiveClickListener onPositiveClickListener) {


        this.onPositiveClickListener = onPositiveClickListener;
    }

    private OnNegativeClickListener onNegativeClickListener;

    public void setOnNegativeClickListener(OnNegativeClickListener onNegativeClickListener) {

        this.onNegativeClickListener = onNegativeClickListener;
    }

    public interface OnPositiveClickListener {
        void onPositiveClick();
    }

    public interface OnNegativeClickListener {
        void onNegativeClick();
    }
    public interface OnKeyListener{
        void  OnKeyListener();
    }
}
