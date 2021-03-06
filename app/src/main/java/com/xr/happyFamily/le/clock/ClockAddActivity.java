package com.xr.happyFamily.le.clock;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xr.database.dao.daoimpl.TimeDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.le.pojo.Time;
import com.xr.happyFamily.le.view.JyTimepicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClockAddActivity extends AppCompatActivity {


    @BindView(R.id.tv_lrsd_qx)
    TextView tvLrsdQx;
    @BindView(R.id.tv_lrsd_qd)
    TextView tvLrsdQd;
    @BindView(R.id.time_le1)
    JyTimepicker timeLe1;
    @BindView(R.id.time_le2)
    JyTimepicker timeLe2;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.sdclock_layout_mian)
    LinearLayout sdclockLayoutMian;
    private TimeDaoImpl timeDao;
    List<Time> times;
    Time time;
    int hour, minutes;
    SharedPreferences preferences;
    String userId;
    private AlarmManager am;
    private PendingIntent pendingIntent;
    private NotificationManager notificationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_add);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        MyApplication application = (MyApplication) getApplication();
        application.addActivity(this);
        timeDao = new TimeDaoImpl(getApplicationContext());
        times = new ArrayList<>();

        preferences = this.getSharedPreferences("my", MODE_PRIVATE);
//        userId= preferences.getString("userId","");

        times = timeDao.findByAllTime();
        timeLe1.setMaxValue(23);
        timeLe1.setMinValue(00);
        timeLe1.setValue(49);
//        timepicker1.setBackgroundColor(Color.WHITE);
        timeLe1.setNumberPickerDividerColor(timeLe1);
        timeLe2.setMaxValue(59);
        timeLe2.setMinValue(00);
        timeLe2.setValue(49);
//        timepicker2.setBackgroundColor(Color.WHITE);
        timeLe2.setNumberPickerDividerColor(timeLe2);
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //获取通知管理器
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @OnClick({R.id.tv_lrsd_qx, R.id.tv_lrsd_qd})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_lrsd_qx:
                finish();
                break;
            case R.id.tv_lrsd_qd:
                hour = timeLe1.getValue();
                minutes = timeLe2.getValue();
                Log.i("zzzzzzzzz", "onClick:--> " + hour + "...." + minutes);
                if (time == null) {
                    time = new Time();
                }
                time.setHour(hour);
                time.setMinutes(minutes);
                timeDao.insert(time);
                Calendar c = Calendar.getInstance();//c：当前系统时间
                c.set(Calendar.HOUR_OF_DAY, hour);//把小时设为你选择的小时
                c.set(Calendar.MINUTE, minutes);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0x101, new Intent("com.zking.android29_alarm_notification.RING"), 0);//上下文 请求码  启动哪一个广播 标志位
                am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);//RTC_WAKEUP:唤醒屏幕  getTimeInMillis():拿到这个时间点的毫秒值 pendingIntent:发送广播
                am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 60 * 60 * 24 * 1000, pendingIntent);
                setResult(600);
                finish();
                break;
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
