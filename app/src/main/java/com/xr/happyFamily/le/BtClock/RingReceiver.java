package com.xr.happyFamily.le.BtClock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.xr.database.dao.daoimpl.ClockDaoImpl;
import com.xr.database.dao.daoimpl.TimeDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.Time;
import com.xr.happyFamily.le.utils.WakeAndLock;
import com.xr.happyFamily.le.view.QinglvClockDialog;
import com.xr.happyFamily.le.view.btClockjsDialog;
import com.xr.happyFamily.le.view.btClockjsDialog2;
import com.xr.happyFamily.le.view.btClockjsDialog3;
import com.xr.happyFamily.le.view.btClockjsDialog4;
import com.xr.happyFamily.together.MyDialog;

import java.util.Calendar;
import java.util.List;


public class RingReceiver extends BroadcastReceiver {
    Context mContext;
    private MediaPlayer mediaPlayer;
    QinglvClockDialog qldialog;
    btClockjsDialog dialog;
    btClockjsDialog2 dialog2;
    btClockjsDialog4 dialog4;
    //治懒闹钟
    List<Time> times;
    //情侣、群组闹钟数据
    List<ClockBean>  clockBeanList;
    Time time;
    ClockBean clockBean;
    private TimeDaoImpl timeDao;
    private ClockDaoImpl clockDao;
    int firstHour=-1;
    int firstMinutes=-1;
    int first;
    private String op = "on";
    SharedPreferences preferences;

    //判断是治懒闹钟:1 还是情侣（群组）:2
    int sign=0;
    public void onReceive(Context context, Intent intent) {
        //广播的名字:包名+随便取一个名字
        if ("com.zking.android29_alarm_notification.RING".equals(intent.getAction())) {
            Log.i("test", "闹钟响了！！！");
            mContext = context;
            timeDao = new TimeDaoImpl(mContext);
            clockDao=new ClockDaoImpl(mContext);
            times = timeDao.findTimeByMin();
            clockBeanList=clockDao.findTimeByMin();
            preferences = mContext.getSharedPreferences("this", Context.MODE_PRIVATE);

            first = preferences.getInt("first", 0);
            Calendar calendar = Calendar.getInstance();
//            int minutes=(int) calendar.getTimeInMillis()/(1000*60);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            int nowminutes = hour * 60 + minutes;
            int sumMin = 0;
            for (int i = 0; i < times.size(); i++) {
                time = times.get(i);
                sumMin = time.getSumMin();
                if (sumMin >= nowminutes) {
                    firstHour = time.getHour();
                    firstMinutes = time.getMinutes();
                    sign=1;
                    break;
                }

            }

            for (int i = 0; i < clockBeanList.size(); i++) {
                clockBean = clockBeanList.get(i);
                int sumMin2 = clockBean.getSumMinute();
                if (sumMin2 >= nowminutes && sumMin2<sumMin) {
                    firstHour = time.getHour();
                    firstMinutes = time.getMinutes();
                    sign=2;
                    break;
                }
            }

            if (firstMinutes != -1 && firstHour != -1) {
                if (first == 1) {
                    Calendar c = Calendar.getInstance();//c：当前系统时间
                    c.set(Calendar.HOUR_OF_DAY, firstHour);//把小时设为你选择的小时
                    c.set(Calendar.MINUTE, firstMinutes);
                    AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                    Intent intent1 = new Intent("com.zking.android29_alarm_notification.RING");
                    PendingIntent sender = PendingIntent.getBroadcast(mContext, 0x101, intent1,
                            PendingIntent.FLAG_CANCEL_CURRENT);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        am.setWindow(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 0, sender);
                    } else {
                        am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
                    }
                    if (hour == firstHour && minutes == firstMinutes) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("first",2);
                        editor.apply();
                        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
/*        PARTIAL_WAKE_LOCK:保持CPU 运转，屏幕和键盘灯有可能是关闭的。
        SCREEN_DIM_WAKE_LOCK：保持CPU 运转，允许保持屏幕显示但有可能是灰的，允许关闭键盘灯
        SCREEN_BRIGHT_WAKE_LOCK：保持CPU 运转，允许保持屏幕高亮显示，允许关闭键盘灯
        FULL_WAKE_LOCK：保持CPU 运转，保持屏幕高亮显示，键盘灯也保持亮度
        ACQUIRE_CAUSES_WAKEUP：强制使屏幕亮起，这种锁主要针对一些必须通知用户的操作.
                ON_AFTER_RELEASE：当锁被释放时，保持屏幕亮起一段时间*/
                        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "wjc");
                        wakeLock.acquire();
//wakeLock.acquire(1000);
                        wakeLock.release();


                        if(sign==1) {
                            List<Time> times = timeDao.findTimesByHourAndMin(hour, minutes);
                            if (!times.isEmpty()) {
                                Time time = times.get(0);
                                if (time.getOpen()) {
                                    if (time.getFlag() == 1) {

                                        clolkDialog1();
                                    } else if (time.getFlag() == 2) {

                                        clolkDialog2();
                                    } else if (time.getFlag() == 3) {

                                        clolkDialog3();
                                    }

                                }
                            }
                        }else
                            clolkDialog4();
                    }
                }
            }

        }
    }

    private void clolkDialog1() {//听歌识曲
        dialog4 = new btClockjsDialog4(mContext);


        dialog4.setOnNegativeClickListener(new btClockjsDialog4.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
//                dialog.dismiss();
            }
        });
        dialog4.setOnPositiveClickListener(new btClockjsDialog4.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {

            }
        });

        dialog4.setCanceledOnTouchOutside(false);
        dialog4.setCancelable(false);
        dialog4.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog4.show();
    }

    private void clolkDialog2() {//脑筋急转弯
        dialog2 = new btClockjsDialog2(mContext);


        dialog2.setOnNegativeClickListener(new btClockjsDialog2.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
//                dialog.dismiss();
            }
        });
        dialog2.setOnPositiveClickListener(new btClockjsDialog2.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {


            }
        });
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);
        dialog2.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog2.show();


    }

    private void clolkDialog3() {//算一算
        dialog = new btClockjsDialog(mContext);
        int x = dialog.getX();
        int y = dialog.getY();
        final String text1 = dialog.getText();
        int z = x * y;

        dialog.setOnNegativeClickListener(new btClockjsDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
//                dialog.dismiss();
            }
        });
        dialog.setOnPositiveClickListener(new btClockjsDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {

            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    private void clolkDialog4() {//情侣、群组闹钟
        qldialog = new QinglvClockDialog(mContext);



        qldialog.show();
    }

}
