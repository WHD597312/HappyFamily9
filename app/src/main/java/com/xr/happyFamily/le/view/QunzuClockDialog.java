package com.xr.happyFamily.le.view;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.xr.database.dao.daoimpl.ClockDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.together.util.mqtt.ClockService;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 制懒闹钟脑筋急转弯关闭
 */
public class QunzuClockDialog extends Dialog {


    String ip = "http://47.98.131.11:8084";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_context)
    TextView tvContext;
    @BindView(R.id.tv_queren)
    TextView tvQueren;

    private String name;
    ClockDaoImpl clockDao;
    List<ClockBean> clockBeanList;
    ClockBean clockBean;
    String text;
    Context mcontext;

    private MediaPlayer mediaPlayer;
    private AudioManager audioMa;
    SharedPreferences preferences;
    Intent service;
    boolean isBound;

    String loveNmae, flag;

    public QunzuClockDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
        mcontext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_qunzuclock);
        ButterKnife.bind(this);
        clockDao = new ClockDaoImpl(mcontext.getApplicationContext());
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String name = "阿里郎";
        clockBeanList = clockDao.findTimesByHourAndMin(hour, minute);
        for (int i = 0; i < clockBeanList.size(); i++) {
            clockBean = clockBeanList.get(i);
            if (clockBean.getSwitchs() == 1) {
                name = clockBean.getMusic();
                loveNmae = clockBean.getCreaterName();
                flag = clockBean.getFlag();
                tvTitle.setText(loveNmae);
                tvContext.setText(flag);
            }
        }


        String[] str = {"阿里郎", "浪人琵琶", "学猫叫", "芙蓉雨", "七月上", "佛系少女", "离人愁", "不仅仅是喜欢", "纸短情长", "远走高飞"};
        int[] musicId = {R.raw.m1, R.raw.m2, R.raw.m3, R.raw.m4, R.raw.m5, R.raw.m6, R.raw.m7, R.raw.m8, R.raw.m9, R.raw.m10};
        for (int i = 0; i < str.length; i++) {
            if (name.equals(str[i])) {
                mediaPlayer = MediaPlayer.create(mcontext, musicId[i]);
                mediaPlayer.start();//一进来就播放
                mediaPlayer.setLooping(true);
            }
        }
//        if ("阿里郎".equals(name)) {
//            mediaPlayer = MediaPlayer.create(mcontext, R.raw.1);
//            mediaPlayer.start();//一进来就播放
//            mediaPlayer.setLooping(true);
//        } else if ("浪人琵琶".equals(name)) {
//            mediaPlayer = MediaPlayer.create(mcontext, R.raw.music2);
//            mediaPlayer.start();//一进来就播放
//            mediaPlayer.setLooping(true);
//        } else if ("学猫叫".equals(name)) {
//            mediaPlayer = MediaPlayer.create(mcontext, R.raw.lrpp);
//            mediaPlayer.start();//一进来就播放
//            mediaPlayer.setLooping(true);
//        } else if ("芙蓉雨".equals(name)) {
//            mediaPlayer = MediaPlayer.create(mcontext, R.raw.all);
//            mediaPlayer.start();//一进来就播放
//            mediaPlayer.setLooping(true);
//        } else if ("that girl".equals(name)) {
//            mediaPlayer = MediaPlayer.create(mcontext, R.raw.girl);
//            mediaPlayer.start();//一进来就播放
//            mediaPlayer.setLooping(true);
//        } else if ("expression".equals(name)) {
//            mediaPlayer = MediaPlayer.create(mcontext, R.raw.ex);
//            mediaPlayer.start();//一进来就播放
//            mediaPlayer.setLooping(true);
//        }
//        iv_zl_njxx1.setTag("open");
//        iv_zl_njxx2.setTag("close");
//        iv_zl_njxx3.setTag("close");

        preferences = mcontext.getSharedPreferences("trueCount", Context.MODE_PRIVATE);
        audioMa = (AudioManager) mcontext.getSystemService(Context.AUDIO_SERVICE);
        audioMa.setStreamVolume(AudioManager.STREAM_MUSIC, audioMa.getStreamMaxVolume
                (AudioManager.STREAM_MUSIC), AudioManager.FLAG_SHOW_UI);
        service = new Intent(mcontext, ClockService.class);

        isBound = mcontext.bindService(service, connection, Context.BIND_AUTO_CREATE);

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

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            mcontext.unbindService(connection);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    ClockService mqService;
    boolean bound;
    ServiceConnection connection = new ServiceConnection() {
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

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OnClick({R.id.tv_queren})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_queren:
                dismiss();
                mediaPlayer.stop();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("ring", false);
                if (mqService != null) {
                    mqService.startClock();
                }
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
