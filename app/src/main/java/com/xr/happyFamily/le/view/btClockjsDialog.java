package com.xr.happyFamily.le.view;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.xr.database.dao.daoimpl.TimeDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.le.pojo.Time;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.mqtt.ClockService;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 制懒闹钟计算关闭
 */
public class btClockjsDialog extends Dialog {

    @BindView(R.id.tv_zl_js)
    TextView tv_zl_js;
    @BindView(R.id.tv_zl_jg)
    TextView tv_zl_jg;
    private String name;
    int x;
    int y;
    String text;
    Context mcontext;
    private MediaPlayer mediaPlayer;
    private AudioManager audioMa;
    SharedPreferences preferences;
    TimeDaoImpl timeDao;
    List<Time> times;
    Time time;
    Intent service;
    boolean isBound;
//        public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;//定义屏蔽参数
    public btClockjsDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
        mcontext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_le_zldialog2);
        ButterKnife.bind(this);
        Random random = new Random();
        x = (int) (random.nextInt(900)) + 100;
        y = (int) (random.nextInt(900)) + 100;
        tv_zl_js.setText(x + "×" + y + "=");
        Log.e("test", "onCreate: -->"+(x*y) );
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);
        timeDao= new TimeDaoImpl(mcontext.getApplicationContext());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mcontext);
        String url = sharedPreferences.getString("pref_alarm_ringtone","");
        times= timeDao.findTimesByHourAndMin(hour,minute);
        time=times.get(0);
        String name =  time.getRingName();
        String[] str = {"阿里郎", "浪人琵琶", "学猫叫", "芙蓉雨", "七月上", "佛系少女", "离人愁", "不仅仅是喜欢", "纸短情长", "远走高飞"};
        int[] musicId = {R.raw.m1, R.raw.m2, R.raw.m3, R.raw.m4, R.raw.m5, R.raw.m6, R.raw.m7, R.raw.m8, R.raw.m9, R.raw.m10};
        for (int i = 0; i < str.length; i++) {
            if (name.equals(str[i])) {
                mediaPlayer = MediaPlayer.create(mcontext, musicId[i]);
                mediaPlayer.start();//一进来就播放
                mediaPlayer.setLooping(true);
            }
        }
       if ("系统自带".equals(name)){
            mediaPlayer = MediaPlayer.create(mcontext, Uri.parse(url));
            mediaPlayer.start();//一进来就播放
            mediaPlayer.setLooping(true);
        }
       //将音量调至最大
        audioMa = (AudioManager)mcontext.getSystemService(Context.AUDIO_SERVICE);
        audioMa.setStreamVolume(AudioManager.STREAM_MUSIC,audioMa.getStreamMaxVolume
                (AudioManager.STREAM_MUSIC),AudioManager.FLAG_SHOW_UI);
//        this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED); //onCreate中实现
        service = new Intent(mcontext, ClockService.class);

        isBound = mcontext.bindService(service, connection, Context.BIND_AUTO_CREATE);
        preferences=mcontext.getSharedPreferences("trueCount",Context.MODE_PRIVATE);
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//// TODO Auto-generated method stub
//        switch(keyCode){
//            case KeyEvent.KEYCODE_BACK:
//            case KeyEvent.KEYCODE_HOME:
//            case KeyEvent.KEYCODE_MENU:
//            case KeyEvent.KEYCODE_VOLUME_DOWN:
//            case KeyEvent.KEYCODE_VOLUME_UP:
//            case KeyEvent.KEYCODE_VOLUME_MUTE:
//                return true;
//            default:
//                return false;
//        }}
    //关闭音量减少
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


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound){
            mcontext.unbindService(connection);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @OnClick({R.id.ib_zl_sc, R.id.ib_zl_qd, R.id.bt_zl_sz1, R.id.bt_zl_sz2, R.id.bt_zl_sz3, R.id.bt_zl_sz4,
            R.id.bt_zl_sz5, R.id.bt_zl_sz6, R.id.bt_zl_sz7, R.id.bt_zl_sz8, R.id.bt_zl_sz9, R.id.bt_zl_sz0})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_zl_sc:
                if (onNegativeClickListener != null) {
                    onNegativeClickListener.onNegativeClick();
                }
                tv_zl_jg.setText("");
                break;
            case R.id.ib_zl_qd:
                //确定键
                if (onPositiveClickListener != null) {

                    onPositiveClickListener.onPositiveClick();
                }
                text = String.valueOf(tv_zl_jg.getText());
                if (!Utils.isEmpty(text)) {
                    int z = x * y;
                    int a = Integer.parseInt(text);
                    Log.i("ttttt", "onClick:---> " + z + "...." + a);
                    if (z == a) {
                        dismiss();
                        mediaPlayer.stop();
                        SharedPreferences.Editor editor= preferences.edit();
                        editor.putBoolean("ring",false);
                        editor.commit();
                        if (mqService != null) {
                            mqService.startClock();
                        }

                    } else {
                        Toast.makeText(mcontext, "输入错误请从新输入", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.bt_zl_sz1:
                tv_zl_jg.append("1");
                break;

            case R.id.bt_zl_sz2:
                tv_zl_jg.append("2");
                break;

            case R.id.bt_zl_sz3:
                tv_zl_jg.append("3");
                break;

            case R.id.bt_zl_sz4:
                tv_zl_jg.append("4");
                break;

            case R.id.bt_zl_sz5:
                tv_zl_jg.append("5");
                break;

            case R.id.bt_zl_sz6:
                tv_zl_jg.append("6");
                break;

            case R.id.bt_zl_sz7:
                tv_zl_jg.append("7");
                break;

            case R.id.bt_zl_sz8:
                tv_zl_jg.append("8");
                break;

            case R.id.bt_zl_sz9:
                tv_zl_jg.append("9");
                break;

            case R.id.bt_zl_sz0:
                tv_zl_jg.append("0");
                break;
        }
    }

    private OnKeyListener onKeyListener;
    private OnPositiveClickListener onPositiveClickListener;

    public void setOnKeyListener(OnKeyListener onKeyListener) {
        this.onKeyListener = onKeyListener;
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

    public interface OnKeyListener {
        void OnKeyListener();
    }
}
