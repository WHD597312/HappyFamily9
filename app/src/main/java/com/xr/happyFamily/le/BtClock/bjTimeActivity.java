package com.xr.happyFamily.le.BtClock;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.xr.database.dao.daoimpl.TimeDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.xnty.Timepicker;
import com.xr.happyFamily.le.ClockActivity;
import com.xr.happyFamily.le.pojo.Time;
import com.xr.happyFamily.le.view.btClockjsDialog3;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.mqtt.ClockService;
import com.xr.happyFamily.together.util.mqtt.MQService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/***
 * 编辑闹钟界面
 *
 * **/
public class bjTimeActivity extends AppCompatActivity {
    @BindView(R.id.time_lebj1)
    Timepicker timepicker1;
    @BindView(R.id.time_lebj2)
    Timepicker timepicker2;
    @BindView(R.id.tv_bjtime_bq) TextView tv_bjtime_bq;
    @BindView(R.id.tv_bjtime_gb) TextView tv_bjtime_gb;
    @BindView(R.id.tv_bjclock_ring) TextView tv_bjclock_ring;
    private TimeDaoImpl timeDao;
    List<Time> times;
    Time time;
    int hour, minutes;
    SharedPreferences preferences;
    String userId;
    String lable ,style;
    private AlarmManager am;
    private PendingIntent pendingIntent;
    private NotificationManager notificationManager;
    SharedPreferences timrClock;
    Intent service;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_le_bjcomonclock);
        ButterKnife.bind(this);
        timeDao = new TimeDaoImpl(getApplicationContext());
        times = new ArrayList<>();

//        userId= preferences.getString("userId","");
        times=timeDao.findByAllTime();
        Calendar calendar = Calendar.getInstance();

       Intent intent = getIntent();
       int hour= intent.getIntExtra("hour",0);
       int minutes=intent.getIntExtra("minutes",0);
       int position = intent.getIntExtra("position",0);
       time=times.get(position);
       lable=time.getLable();
       style=time.getStyle();
       String ring = time.getRingName();
        tv_bjtime_gb.setText(style);
       if ("".equals(ring)){
           tv_bjclock_ring.setText("阿里郎");
           time.setRingName("阿里郎");
           timeDao.update(time);
       }else {
           tv_bjclock_ring.setText(ring);
       }

       //闹钟数字
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
        timrClock=getSharedPreferences("password",Context.MODE_PRIVATE);
        service = new Intent(this, ClockService.class);
        startService(service);
        isBound = bindService(service, connection, Context.BIND_AUTO_CREATE);

    }
    boolean isBound;

    @OnClick({ R.id.tv_lrbj_qx, R.id.tv_lrbj_qd ,R.id.rl_bjtime_bq,R.id.rl_bjtime_gb,R.id.rl_bjcoloc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_lrbj_qx:
                finish();
                break;
            case R.id.tv_lrbj_qd:
                long id=time.getId();
                time=timeDao.findById(id);
                hour = timepicker1.getValue();
                minutes = timepicker2.getValue();

//                time.setUserId(Long.parseLong(userId));
                time.setHour(hour);
                time.setMinutes(minutes);
                if (Utils.isEmpty(time.getLable())){
                    time.setLable("别忘了明天的会议");
                }
                if (Utils.isEmpty(time.getStyle())){
                    time.setStyle("听歌识曲");
                    time.setFlag(1);
                    time.setRingName("阿里郎");
                }
                time.setOpen(true);
                int sumMin=hour*60+minutes;
                time.setSumMin(sumMin);

                stopService(service);
                startService(service);
                if (mqService != null) {
                    mqService.update(time);
                    mqService.startClock();
                }
//                Calendar calendar=Calendar.getInstance();
//                int hour1=calendar.get(Calendar.HOUR_OF_DAY);
//                int minute1=calendar.get(Calendar.MINUTE);

//                c.set(Calendar.HOUR_OF_DAY,hour);//把小时设为你选择的小时
//                c.set(Calendar.MINUTE,minutes);
////                PendingIntent pendingIntent= PendingIntent.getBroadcast(this,0x101,new Intent("com.zking.android29_alarm_notification.RING"),0);//上下文 请求码  启动哪一个广播 标志位
////                am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);//RTC_WAKEUP:唤醒屏幕  getTimeInMillis():拿到这个时间点的毫秒值 pendingIntent:发送广播
////                am.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),60*60*24*1000, pendingIntent);
//

//                PendingIntent pendIntent = PendingIntent.getBroadcast(getApplicationContext(),0x101, new Intent("com.zking.android29_alarm_notification.RING"), PendingIntent.FLAG_UPDATE_CURRENT);
//                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
//                {
//                    am.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,c.getTimeInMillis(),pendIntent);
//                    am.setWindow(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),0,pendIntent);
//                    am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,c.getTimeInMillis(),pendIntent);
//                }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//                    am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,c.getTimeInMillis(),pendIntent);
//                }else{
//                    am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,c.getTimeInMillis(),pendIntent);
//                }
//                Calendar c=Calendar.getInstance();//c：当前系统时间
//                AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//                Intent intent1=new Intent("com.zking.android29_alarm_notification.RING");
//                intent1.setFlags( Intent.FLAG_EXCLUDE_STOPPED_PACKAGES);//3.1以后的版本需要设置Intent.FLAG_INCLUDE_STOPPED_PACKAGES
//
//                Log.e("time", "onClick: "+hour+"...."+minutes );
//                PendingIntent sender = PendingIntent.getBroadcast(this, 0x101, intent1,
//                        PendingIntent.FLAG_CANCEL_CURRENT);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    am.setWindow(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 0, sender);
//                } else {
//                        am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
//                    }
//////                Intent intent = new Intent(this,TimeClockActivity.class);
//////                intent.putExtra("fragid",1);
//////                startActivity(intent);

                finish();
                break;
            case R.id.rl_bjtime_bq:
                Intent intent2 = new Intent(this,bqOfColckActivity.class);
                startActivityForResult(intent2,666);
                break;
            case R.id.rl_bjtime_gb:
                    clolkDialog1();
                break;
            case R.id.rl_bjcoloc:
                Intent intent3 = new Intent(this,clockRingActivity.class);
                startActivityForResult(intent3,111);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==666){
            String text=data.getStringExtra("text");
            tv_bjtime_bq.setText(text);
            time.setLable(text);
        }
        if (resultCode==111){
            String[] str = {"阿里郎", "浪人琵琶", "学猫叫", "芙蓉雨", "七月上", "佛系少女", "离人愁", "不仅仅是喜欢", "纸短情长", "远走高飞"};
            int pos=data.getIntExtra("pos",0);
            List<String> mData = new ArrayList<String>(Arrays.asList(str));
            if (pos!=-1){
                String text=mData.get(pos);
                tv_bjclock_ring.setText(text);
                time.setRingName(text);
            }else {
                tv_bjclock_ring.setText("系统自带");
                time.setRingName("系统自带");
            }
        }
    }
    ClockService mqService;
    boolean bound;
    ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ClockService.LocalBinder binder = (ClockService.LocalBinder) service;
            mqService = binder.getService();
            mqService.acquireWakeLock(bjTimeActivity.this);
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
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
               String text=dialog.getText();
               int flag = dialog.getFlag();
                tv_bjtime_gb.setText(text);
                   time.setStyle(text);
                   time.setFlag(flag);
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
        if (isBound){
            unbindService(connection);
        }
    }
}
