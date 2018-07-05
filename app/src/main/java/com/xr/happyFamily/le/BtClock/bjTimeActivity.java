package com.xr.happyFamily.le.BtClock;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xr.database.dao.daoimpl.TimeDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.xnty.Timepicker;
import com.xr.happyFamily.le.pojo.Time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class bjTimeActivity extends AppCompatActivity {
    @BindView(R.id.time_lebj1)
    Timepicker timepicker1;
    @BindView(R.id.time_lebj2)
    Timepicker timepicker2;

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
        setContentView(R.layout.fragment_le_bjcomonclock);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        timeDao = new TimeDaoImpl(getApplicationContext());
        times = new ArrayList<>();
        preferences = this.getSharedPreferences("my", MODE_PRIVATE);
//        userId= preferences.getString("userId","");
        times=timeDao.findByAllTime();

       Intent intent = getIntent();
      String day= intent.getStringExtra("day");
       int hour= intent.getIntExtra("hour",0);
       int minutes=intent.getIntExtra("minutes",0);
       int position = intent.getIntExtra("position",0);
       time=times.get(position);


        timepicker1.setMaxValue(23);
        timepicker1.setMinValue(00);
        timepicker1.setValue(hour);
//        timepicker1.setBackgroundColor(Color.WHITE);
        timepicker1.setNumberPickerDividerColor(timepicker1);
        timepicker2.setMaxValue(59);
        timepicker2.setMinValue(00);
        timepicker2.setValue(minutes);
//        timepicker2.setBackgroundColor(Color.WHITE);
        timepicker2.setNumberPickerDividerColor(timepicker2);
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //获取通知管理器
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        AlarmManager am = (AlarmManager)
            getSystemService(Context.ALARM_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            am.setExact(AlarmManager.RTC_WAKEUP, TimeUtils
//                    .stringToLong(recordTime, TimeUtils.NO_SECOND_FORMAT), sender);
//        }else {
//            am.set(AlarmManager.RTC_WAKEUP, TimeUtils
//                    .stringToLong(recordTime, TimeUtils.NO_SECOND_FORMAT), sender);
//        }

    }

    @OnClick({ R.id.tv_lrbj_qx, R.id.tv_lrbj_qd})
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.tv_lrbj_qx:
                finish();
                break;
            case R.id.tv_lrbj_qd:
                hour = timepicker1.getValue();
                minutes = timepicker2.getValue();
                Log.i("zzzzzzzzz", "onClick:--> " + hour + "...." + minutes);
//                time.setUserId(Long.parseLong(userId));
                time.setHour(hour);
                time.setMinutes(minutes);
                timeDao.update(time);

//                Calendar calendar=Calendar.getInstance();
//                int hour1=calendar.get(Calendar.HOUR_OF_DAY);
//                int minute1=calendar.get(Calendar.MINUTE);
                Calendar c=Calendar.getInstance();//c：当前系统时间
                c.set(Calendar.HOUR_OF_DAY,hour);//把小时设为你选择的小时
                c.set(Calendar.MINUTE,minutes);
                PendingIntent pendingIntent= PendingIntent.getBroadcast(this,0x101,new Intent("com.zking.android29_alarm_notification.RING"),0);//上下文 请求码  启动哪一个广播 标志位
                am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);//RTC_WAKEUP:唤醒屏幕  getTimeInMillis():拿到这个时间点的毫秒值 pendingIntent:发送广播
                am.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),60*60*24*1000, pendingIntent);
                Intent intent = new Intent(this,TimeClockActivity.class);
                intent.putExtra("fragid",1);
                startActivity(intent);
                break;
        }
    }




}
