package com.xr.happyFamily.together.util.mqtt;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import com.xr.database.dao.daoimpl.TimeDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.le.pojo.Time;
import com.xr.happyFamily.le.view.btClockjsDialog;
import com.xr.happyFamily.le.view.btClockjsDialog2;
import com.xr.happyFamily.le.view.btClockjsDialog4;
import java.util.Calendar;
import java.util.List;


public class ClockService extends Service {

    private TimeDaoImpl timeDao;
    private LocalBinder binder = new LocalBinder();
    SharedPreferences preferences;
    /**
     * 服务启动之后就初始化MQTT,连接MQTT
     */
    @Override
    public void onCreate() {
        super.onCreate();
        timeDao = new TimeDaoImpl(this);
        preferences = this.getSharedPreferences("trueCount", MODE_MULTI_PROCESS);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("Ibind","-->onBind");
        Notification notification = new Notification(R.mipmap.app, "title",System.currentTimeMillis());
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
        notification.contentIntent=pintent;
        startForeground(0, notification);
        return binder;

    }



    PowerManager.WakeLock  mWakeLock;
    //申请设备电源锁
    public  void acquireWakeLock(Context context)
    {
        if (null == mWakeLock)
        {
            PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "WakeLock");
            if (null != mWakeLock)
            {
                mWakeLock.acquire();
            }
        }
    }
    //释放设备电源锁
    public  void releaseWakeLock()
    {
        if (null != mWakeLock)
        {
            mWakeLock.release();
            mWakeLock = null;
        }
    }





    public class LocalBinder extends Binder {

        public ClockService getService() {

            return ClockService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        Notification notification=new Notification.Builder(getApplicationContext())
                .setContentText("前台服务")
                .setSmallIcon(R.mipmap.app)
                .setWhen(System.currentTimeMillis())
                .build();
        startForeground(110,notification);
        return START_NOT_STICKY;
    }

    /**
     * 销毁服务，则断开MQTT,释放资源
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
            if (countTimer!=null){
                countTimer.cancel();
                countTimer=null;
            }


    }


    class CountTimer extends CountDownTimer {
        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        /**
         * 倒计时过程中调用
         *
         * @param millisUntilFinished
         */
        @Override
        public void onTick(long millisUntilFinished) {

            Log.e("Tag", "倒计时=" + (millisUntilFinished / 1000));
        }

        /**
         * 倒计时完成后调用
         */
        @Override
        public void onFinish() {
            Log.e("Tag", "倒计时完成");
            //设置倒计时结束之后的按钮样式
            for (int i = 0; i < times.size(); i++) {
                time = times.get(i);
                boolean open = time.getOpen();
                sumMin = time.getSumMin()*60;
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);
                int nowminutes = hour * 60 *60 + minutes*60+second;

                if (sumMin == nowminutes&&true==open) {
                    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                    PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "wjc");
                    wakeLock.acquire();
//wakeLock.acquire(1000);
                    wakeLock.release();
                    ring();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("trueCount",false);
                    editor.commit();
                    break;
                }
            }

        }
    }
//    Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            if (countTimer!=null){
//                countTimer.cancel();
//            }
//        closeCountTimer();
//        }
//    };
//    public void closeCountTimer(){
//
//        close=2;
//        countTimer1 = new CountTimer(counttime * 1000, 1000);
//        countTimer1.start();
//    }

    public void ring() {
        if (time.getOpen()) {
            if (time.getFlag() == 1) {
//                time.setOpen(false);
//                timeDao.update(time);
                clolkDialog1();
            } else if (time.getFlag() == 2) {
//                time.setOpen(false);
//                timeDao.update(time);
                clolkDialog2();
            } else if (time.getFlag() == 3) {
//                time.setOpen(false);
//                timeDao.update(time);
                clolkDialog3();
            }

        }
    }

    btClockjsDialog dialog;
    btClockjsDialog2 dialog2;
    btClockjsDialog4 dialog4;

    private void clolkDialog1() {//听歌识曲
        dialog4 = new btClockjsDialog4(this);


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
        dialog2 = new btClockjsDialog2(this);


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
        dialog = new btClockjsDialog(this);
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

    Time time;
    CountTimer countTimer;
    int counttime;
    int sumMin;
    List<Time> times;
    private AlarmManager am;
    public void startClock() {
        int firstHour = 0;
        int firstMinutes = 0;
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int nowminutes = hour * 60 *60 + minutes*60+second;

       times = timeDao.findTimeByMin();
        for (int i = 0; i < times.size(); i++) {
            time = times.get(i);
            boolean open = time.getOpen();
            sumMin = time.getSumMin()*60;
            if (sumMin >= nowminutes&&true==open) {
                counttime = sumMin - nowminutes;
                countTimer = new CountTimer(counttime * 1000, 1000);
                countTimer.start();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("trueCount",true);
                editor.commit();
                break;
            }
        }
          int time = counttime*1000;
//        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//        Intent intent1=new Intent("com.zking.android29_alarm_notification.RING");
//        intent1.setFlags( Intent.FLAG_EXCLUDE_STOPPED_PACKAGES);//3.1以后的版本需要设置Intent.FLAG_INCLUDE_STOPPED_PACKAGES
//        PendingIntent sender = PendingIntent.getService(this, 0x101, intent1,
//                PendingIntent.FLAG_CANCEL_CURRENT);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            am.setExact(AlarmManager.RTC_WAKEUP, counttime*1000, sender);
//        } else {
//            am.set(AlarmManager.RTC_WAKEUP, counttime, sender);
//        }
//        PendingIntent sender = PendingIntent.getBroadcast(this, 0x101, intent1,
//                        PendingIntent.FLAG_CANCEL_CURRENT);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    am.setWindow(AlarmManager.RTC_WAKEUP, time,0, sender);
//
//                    Log.e("time", "startClock: "+time);
//                } else {
//                        am.set(AlarmManager.RTC_WAKEUP, time, sender);
//                    }

    }

    public void update(Time time){

        timeDao.update(time);

    }
}
