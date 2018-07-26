package com.xr.happyFamily.le.BtClock;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xr.database.dao.daoimpl.TimeDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.xnty.Timepicker;
import com.xr.happyFamily.le.pojo.Time;
import com.xr.happyFamily.le.view.btClockjsDialog3;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.mqtt.ClockService;
import com.xr.happyFamily.together.util.mqtt.MQService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class addTimeActivity extends AppCompatActivity {
    @BindView(R.id.time_lebj1)
    Timepicker timepicker1;
    @BindView(R.id.time_lebj2)
    Timepicker timepicker2;
    @BindView(R.id.tv_bjtime_bq)
    TextView tv_bjtime_bq;
    @BindView(R.id.tv_bjtime_gb)
    TextView tv_bjtime_gb;
    @BindView(R.id.tv_bt_title)
    TextView tv_bt_title;
    @BindView(R.id.tv_bjclock_ring)
    TextView tv_bjclock_ring;
    private TimeDaoImpl timeDao;
    List<Time> times;
    Time time;
    int hour, minutes;
    SharedPreferences preferences;
    String userId;
    String lable, style;
    String ip = "http://47.98.131.11:8084";
    Intent service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_le_bjcomonclock);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        tv_bt_title.setText("添加闹钟");
        timeDao = new TimeDaoImpl(getApplicationContext());
        times = new ArrayList<>();

        preferences = this.getSharedPreferences("firstring", MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("first", 1);
        editor.commit();
//        userId= preferences.getString("userId","");
        times = timeDao.findByAllTime();
        time = new Time();
        Calendar calendar = Calendar.getInstance();
        timepicker1.setMaxValue(23);
        timepicker1.setMinValue(00);
        timepicker1.setValue(calendar.get(Calendar.HOUR_OF_DAY));
//        timepicker1.setBackgroundColor(Color.WHITE);
        timepicker1.setNumberPickerDividerColor(timepicker1);
        timepicker2.setMaxValue(59);
        timepicker2.setMinValue(00);
        timepicker2.setValue(calendar.get(Calendar.MINUTE));
//        timepicker2.setBackgroundColor(Color.WHITE);
        timepicker2.setNumberPickerDividerColor(timepicker2);


        service = new Intent(this, ClockService.class);
        startService(service);
        isBound = bindService(service, connection, Context.BIND_AUTO_CREATE);
    }

    boolean isBound;


    @OnClick({R.id.tv_lrbj_qx, R.id.tv_lrbj_qd, R.id.rl_bjtime_bq, R.id.rl_bjtime_gb, R.id.rl_bjcoloc})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_lrbj_qx:
                finish();
                break;
            case R.id.tv_lrbj_qd:

                hour = timepicker1.getValue();
                minutes = timepicker2.getValue();
                Log.i("zzzzzzzzz", "onClick:--> " + hour + "...." + minutes);
                time.setHour(hour);
                time.setMinutes(minutes);
                if (Utils.isEmpty(time.getLable())) {
                    time.setLable("别忘了明天的会议");
                }
                if (Utils.isEmpty(time.getStyle())) {
                    time.setStyle("听歌识曲");
                    time.setFlag(1);

                }if (Utils.isEmpty(time.getRingName())){
                time.setRingName("学猫叫");
            }
                int sumMin = hour * 60 + minutes;
                time.setSumMin(sumMin);
                time.setOpen(true);
                timeDao.insert(time);
                stopService(service);
                startService(service);
                if (mqService != null) {
                    mqService.startClock();
                }

                Utils.showToast(addTimeActivity.this, "添加闹铃成功");
                setResult(600);
                finish();

                break;
            case R.id.rl_bjtime_bq:
                //跳转到标签
                Intent intent2 = new Intent(this, bqOfColckActivity.class);
                startActivityForResult(intent2, 666);
                break;
            case R.id.rl_bjtime_gb:
                //选择关闭方式
                clolkDialog1();
                break;
            case R.id.rl_bjcoloc:
                Intent intent3 = new Intent(this, clockRingActivity.class);
                startActivityForResult(intent3, 111);
                break;
        }
    }

    String text1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 666) {

            text1 = data.getStringExtra("text");
            tv_bjtime_bq.setText(text1);
        }
        if (resultCode == 111) {

            int pos = data.getIntExtra("pos", 0);
            List<String> mData = new ArrayList<String>(Arrays.asList("学猫叫", "芙蓉雨", "浪人琵琶", "阿里郎", "that girl", "expression","梦醒时分","甜甜女生","38列车搞笑","小娘子"));
            if (pos != -1) {
                String text = mData.get(pos);
                tv_bjclock_ring.setText(text);
                time.setRingName(text);
            } else {
                tv_bjclock_ring.setText("系统自带");
                time.setRingName("系统自带");
            }
        }
    }

    ClockService mqService;
    boolean bound;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ClockService.LocalBinder binder = (ClockService.LocalBinder) service;
            mqService = binder.getService();
            mqService.acquireWakeLock(addTimeActivity.this);
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    int flag1 = 1;
    btClockjsDialog3 dialog;

    private void clolkDialog1() {
        dialog = new btClockjsDialog3(this);
//        Window dialogWindow = dialog.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
//        lp.x = 0; // 新位置X坐标
//        lp.y = 0; // 新位置Y坐标
//        lp.width = 500; // 宽度
//        lp.height = 500; // 高度
//        lp.alpha =1f; // 透明度
//        dialogWindow.setAttributes(lp);
        dialog.setOnNegativeClickListener(new btClockjsDialog3.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
                dialog.dismiss();
            }
        });
        dialog.setOnPositiveClickListener(new btClockjsDialog3.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {
                String text = dialog.getText();
                Log.e("text", "onPositiveClick: " + text);
                flag1 = dialog.getFlag();
                tv_bjtime_gb.setText(text);
                time.setStyle(text);
                time.setFlag(flag1);
                dialog.dismiss();


            }
        });
        dialog.setCanceledOnTouchOutside(false);
//        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
//        Window w = dialog.getWindow();
//        WindowManager.LayoutParams lp = w.getAttributes();
//        lp.x = 0;
//        dialog.onWindowAttributesChanged(lp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
        }
    }

//
//    private boolean mIsShowing = false;
//    private PopupWindow popupWindow;
//    ImageView image1, image2, image3, image4, image5, image6, image7, image8;
//    RelativeLayout relativeLayout1, relativeLayout2, relativeLayout3, relativeLayout4, relativeLayout5,
//            relativeLayout6, relativeLayout7, relativeLayout8;
//    Button buttonqx, buttonqd;
//    List<String> weeks;
//    String week=" ";
//
//    private void initPopup() {
//        if (popupWindow != null && popupWindow.isShowing()) {
//            return;
//        }
//        weeks=new ArrayList<>();
//        View parent = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
//        View pop = View.inflate(this, R.layout.fragment_addday_poup, null);
//        image1 = (ImageView) pop.findViewById(R.id.iv_add_day1);
//        image2 = (ImageView) pop.findViewById(R.id.iv_add_day2);
//        image3 = (ImageView) pop.findViewById(R.id.iv_add_day3);
//        image4 = (ImageView) pop.findViewById(R.id.iv_add_day4);
//        image5 = (ImageView) pop.findViewById(R.id.iv_add_day5);
//        image6 = (ImageView) pop.findViewById(R.id.iv_add_day6);
//        image7 = (ImageView) pop.findViewById(R.id.iv_add_day7);
//        image8 = (ImageView) pop.findViewById(R.id.iv_add_no);
//        relativeLayout1 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r1);
//        relativeLayout2 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r2);
//        relativeLayout3 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r3);
//        relativeLayout4 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r4);
//        relativeLayout5 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r5);
//        relativeLayout6 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r6);
//        relativeLayout7 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r7);
//        relativeLayout8 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r8);
//        buttonqd = (Button) pop.findViewById(R.id.bt_addday_qd);
//        buttonqx = (Button) pop.findViewById(R.id.bt_addday_qx);
//        image1.setTag("close");
//        image2.setTag("close");
//        image3.setTag("close");
//        image4.setTag("close");
//        image5.setTag("close");
//        image6.setTag("close");
//        image7.setTag("close");
//        image8.setTag("open");
//        image8.setImageResource(R.mipmap.lrclock_dh);
////        popupWindow = new PopupWindow(pop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow = new PopupWindow(pop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        //点击空白处时，隐藏掉pop窗口
//        popupWindow.setFocusable(true);
//        popupWindow.setTouchable(true);
//        popupWindow.setOutsideTouchable(true);
////        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
//        ColorDrawable dw = new ColorDrawable(0x30000000);
//        popupWindow.setBackgroundDrawable(dw);
//        popupWindow.setAnimationStyle(R.style.Popupwindow);
//        popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
////        mIsShowing = false;
//        View.OnClickListener listener = new View.OnClickListener() {
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.bt_addday_qx:
//                        popupWindow.dismiss();
//                        weeks.clear();
//                        break;
//                    case R.id.bt_addday_qd:
//                        weeks.clear();
//                        if ("open".equals(image1.getTag())) {
//                            weeks.add(" 周一 ");
//                        }
//                        if ("open".equals(image2.getTag())) {
//                            weeks.add(" 周二 ");
//                        }
//                        if ("open".equals(image3.getTag())) {
//                            weeks.add(" 周三 ");
//                        }
//                        if ("open".equals(image4.getTag())) {
//                            weeks.add(" 周四 ");
//                        }
//                        if ("open".equals(image5.getTag())) {
//                            weeks.add(" 周五 ");
//                        }
//                        if ("open".equals(image6.getTag())) {
//                            weeks.add(" 周六 ");
//                        }
//                        if ("open".equals(image7.getTag())) {
//                            weeks.add(" 周日 ");
//                        }
//                        if ("open".equals(image8.getTag())) {
//                            weeks.add(" 不重复 ");
//                        }
//                        for (int i=0;i<weeks.size();i++){
//                            week += weeks.get(i);
//                        }
//                        tv_lesd_week.setText(week);
//                        popupWindow.dismiss();
//                        break;
//                    case R.id.rl_addday_r8:
//                        if ("close".equals(image8.getTag())) {
//                            image8.setImageResource(R.mipmap.lrclock_dh);
//                            image1.setImageResource(0);
//                            image2.setImageResource(0);
//                            image3.setImageResource(0);
//                            image4.setImageResource(0);
//                            image5.setImageResource(0);
//                            image6.setImageResource(0);
//                            image7.setImageResource(0);
//                            image8.setTag("open");
//                            image7.setTag("close");
//                            image6.setTag("close");
//                            image5.setTag("close");
//                            image4.setTag("close");
//                            image3.setTag("close");
//                            image2.setTag("close");
//                            image1.setTag("close");
//
//                        } else if ("open".equals(image8.getTag())) {
//                            image8.setImageResource(0);
//                            image8.setTag("close");
//                        }
//
//                        break;
//                    case R.id.rl_addday_r7:
//                        if ("close".equals(image7.getTag())) {
//                            image8.setImageResource(0);
//                            image7.setImageResource(R.mipmap.lrclock_dh);
//                            image7.setTag("open");
//                        } else if ("open".equals(image7.getTag())) {
//                            image7.setImageResource(0);
//                            image7.setTag("close");
//                        }
//                        break;
//                    case R.id.rl_addday_r1:
//                        if ("close".equals(image1.getTag())) {
//                            image8.setImageResource(0);
//                            image1.setImageResource(R.mipmap.lrclock_dh);
//                            image1.setTag("open");
//                        } else if ("open".equals(image1.getTag())) {
//                            image1.setImageResource(0);
//                            image1.setTag("close");
//                        }
//                        break;
//                    case R.id.rl_addday_r2:
//                        if ("close".equals(image2.getTag())) {
//                            image8.setImageResource(0);
//                            image2.setImageResource(R.mipmap.lrclock_dh);
//                            image2.setTag("open");
//                        } else if ("open".equals(image2.getTag())) {
//                            image2.setImageResource(0);
//                            image2.setTag("close");
//                        }
//                        break;
//                    case R.id.rl_addday_r3:
//                        if ("close".equals(image3.getTag())) {
//                            image8.setImageResource(0);
//                            image3.setImageResource(R.mipmap.lrclock_dh);
//                            image3.setTag("open");
//                        } else if ("open".equals(image3.getTag())) {
//                            image3.setImageResource(0);
//                            image3.setTag("close");
//                        }
//                        break;
//                    case R.id.rl_addday_r4:
//                        if ("close".equals(image4.getTag())) {
//                            image8.setImageResource(0);
//                            image4.setImageResource(R.mipmap.lrclock_dh);
//                            image4.setTag("open");
//                        } else if ("open".equals(image4.getTag())) {
//                            image4.setImageResource(0);
//                            image4.setTag("close");
//                        }
//                        break;
//                    case R.id.rl_addday_r5:
//                        if ("close".equals(image5.getTag())) {
//                            image8.setImageResource(0);
//                            image5.setImageResource(R.mipmap.lrclock_dh);
//                            image5.setTag("open");
//                        } else if ("open".equals(image5.getTag())) {
//                            image5.setImageResource(0);
//                            image5.setTag("close");
//                        }
//                        break;
//                    case R.id.rl_addday_r6:
//                        if ("close".equals(image6.getTag())) {
//                            image8.setImageResource(0);
//                            image6.setImageResource(R.mipmap.lrclock_dh);
//                            image6.setTag("open");
//                        } else if ("open".equals(image6.getTag())) {
//                            image6.setImageResource(0);
//                            image6.setTag("close");
//                        }
//                        break;
//                }
//            }
//        };
//        relativeLayout1.setOnClickListener(listener);
//        relativeLayout2.setOnClickListener(listener);
//        relativeLayout3.setOnClickListener(listener);
//        relativeLayout4.setOnClickListener(listener);
//        relativeLayout5.setOnClickListener(listener);
//        relativeLayout6.setOnClickListener(listener);
//        relativeLayout7.setOnClickListener(listener);
//        relativeLayout8.setOnClickListener(listener);
//        buttonqd.setOnClickListener(listener);
//        buttonqx.setOnClickListener(listener);
//    }


}
