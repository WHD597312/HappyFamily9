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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xr.database.dao.daoimpl.TimeDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.le.pojo.Time;
import com.xr.happyFamily.together.util.mqtt.ClockService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;


/**
 * 情侣/群组闹钟提示弹窗
 */
public class btClockjsDialog5 extends Dialog {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_context)
    TextView tvContext;
    @BindView(R.id.tv_queren)
    TextView tvQueren;
    private String name;

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
    int dialogSign;

    //        public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;//定义屏蔽参数
    public btClockjsDialog5(@NonNull Context context,int dialogSign) {
        super(context, R.style.MyDialog);
        mcontext = context;
        this.dialogSign=dialogSign;
        isShow=true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_clock);
        ButterKnife.bind(this);
        if(dialogSign==3) {
            tvTitle.setText("情侣闹钟");
            tvContext.setText("您有新的情侣闹钟");
        }else if(dialogSign==2){
            tvTitle.setText("群组闹钟");
            tvContext.setText("您有新的群组闹钟");
        }

        service = new Intent(mcontext, ClockService.class);
        isBound = mcontext.bindService(service, connection, Context.BIND_AUTO_CREATE);

    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            mcontext.unbindService(connection);
        }
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

    @OnClick({R.id.tv_queren})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_queren:
                if (mqService != null) {
                    mqService.startClock();
                }
                SharedPreferences preferences = mcontext.getSharedPreferences("my", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isClockPopShow", false);
                editor.commit();
                dismiss();
                isShow=false;
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

    boolean isShow=false;
    public boolean getShow(){
        return isShow;
    }
}
