package com.xr.happyFamily.le.view;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 制懒闹钟计算关闭
 */
public class QinglvClockDialog extends Dialog {



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
//        public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;//定义屏蔽参数
    public QinglvClockDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
        mcontext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_main);
        ButterKnife.bind(this);
        Random random = new Random();

        Calendar calendar = Calendar.getInstance();
        preferences = mcontext.getSharedPreferences("this", mcontext.MODE_PRIVATE);

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



    @OnClick({R.id.tv_quxiao,R.id.tv_queding})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_quxiao:

                Toast.makeText(mcontext,"取消???",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_queding:
                //确定键

                        dismiss();
                        mediaPlayer.stop();
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("first",1);
                        editor.apply();
                        Calendar c=Calendar.getInstance();//c：当前系统时间
                        AlarmManager am = (AlarmManager) mcontext.getSystemService(Context.ALARM_SERVICE);
                        Intent intent1=new Intent("com.zking.android29_alarm_notification.RING");

                        PendingIntent sender = PendingIntent.getBroadcast(mcontext, 0x101, intent1,
                                PendingIntent.FLAG_CANCEL_CURRENT);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            am.setWindow(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 0, sender);
                        } else {
                            am.set(AlarmManager.RTC_WAKEUP,  c.getTimeInMillis(), sender);
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
