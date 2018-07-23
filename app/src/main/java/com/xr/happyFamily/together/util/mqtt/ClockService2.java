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

import com.xr.database.dao.ClockBeanDao;
import com.xr.database.dao.daoimpl.ClockDaoImpl;
import com.xr.database.dao.daoimpl.TimeDaoImpl;
import com.xr.database.dao.daoimpl.UserInfosDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.Time;
import com.xr.happyFamily.le.pojo.UserInfo;
import com.xr.happyFamily.le.view.QinglvClockDialog;
import com.xr.happyFamily.le.view.btClockjsDialog;
import com.xr.happyFamily.le.view.btClockjsDialog2;
import com.xr.happyFamily.le.view.btClockjsDialog4;

import java.util.Calendar;
import java.util.List;


public class ClockService2 extends Service {

    private TimeDaoImpl timeDao;
    private ClockDaoImpl clockDao;
    private UserInfosDaoImpl userInfosDao;
    private LocalBinder binder = new LocalBinder();
    SharedPreferences preferences;
    Boolean Ring = false;
    Time ti;
    ClockBean cl;
    ClockBeanDao clockBeanDao;
    ClockBean clockBean;
    List<ClockBean> clockBeanList;
    String userId;

    /**
     * 服务启动之后就初始化MQTT,连接MQTT
     */
    @Override
    public void onCreate() {
        super.onCreate();
        timeDao = new TimeDaoImpl(this);
        clockDao = new ClockDaoImpl(this);
        userInfosDao = new UserInfosDaoImpl(this);
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        userId = preferences.getString("userId", "");
        preferences = this.getSharedPreferences("trueCount", MODE_MULTI_PROCESS);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("Ibind", "-->onBind");
        Notification notification = new Notification(R.mipmap.app, "title", System.currentTimeMillis());
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
        notification.contentIntent = pintent;
        startForeground(0, notification);
        return binder;

    }


    PowerManager.WakeLock mWakeLock;

    //申请设备电源锁
    public void acquireWakeLock(Context context) {
        if (null == mWakeLock) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "WakeLock");
            if (null != mWakeLock) {
                mWakeLock.acquire();
            }
        }
    }

    //释放设备电源锁
    public void releaseWakeLock() {
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }


    public class LocalBinder extends Binder {

        public ClockService2 getService() {

            return ClockService2.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentText("前台服务")
                .setSmallIcon(R.mipmap.app)
                .setWhen(System.currentTimeMillis())
                .build();
        startForeground(110, notification);
        return START_NOT_STICKY;
    }

    /**
     * 销毁服务，则断开MQTT,释放资源
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countTimer != null) {
            countTimer.cancel();
            countTimer = null;
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
            clockDao = new ClockDaoImpl(ClockService2.this);
            boolean isBt = false;
            boolean isJy = false;
            //设置倒计时结束之后的按钮样式
            List<Time> times = timeDao.findTimeByMin();
            List<ClockBean> clockBeanList = clockDao.findTimeByMin();
            for (int i = 0; i < times.size(); i++) {
                Log.e("open", "onFinish:... " + times.size());
                ti = times.get(i);
                boolean open = ti.getOpen();
                sumMin = ti.getSumMin() * 60;
                Log.e("open", "onFinish:--> " + open);
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);
                int nowminutes = hour * 60 * 60 + minutes * 60 + second;

                    Log.e("qqqqqLLLLLL", sumMin + ",,,," + nowminutes);
                    if (sumMin == nowminutes && true == open) {
                        isBt = true;
                        Log.e("qqqqqLLLLLL", isBt + "2222222");
//                    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//                    PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "wjc");
//                    wakeLock.acquire();
////wakeLock.acquire(1000);
//                    wakeLock.release();
//                    if (Ring == false) {
//                        ring();
//                        Ring = true;
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putBoolean("trueCount", false);
//                        editor.commit();
//                    }

                    break;
                }
            }

            if (!isBt) {
                for (int i = 0; i < clockBeanList.size(); i++) {
                    cl = clockBeanList.get(i);
                    int switchs = cl.getSwitchs();
                    boolean open;
                    if (switchs == 1) {
                        open = true;
                    } else {
                        open = false;
                    }
                    sumMin = cl.getSumMinute() * 60;
                    Log.e("open", "onFinish:--> " + open);
                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minutes = calendar.get(Calendar.MINUTE);
                    int second = calendar.get(Calendar.SECOND);
                    int nowminutes = hour * 60 * 60 + minutes * 60 + second;
                    if (sumMin == nowminutes && true == open) {
                        isJy = true;
                        isBt = false;
                        break;
                    }
                }
            }

            if (isBt || isJy) {
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "wjc");
                wakeLock.acquire();
//wakeLock.acquire(1000);
                wakeLock.release();
                if (Ring == false) {
                    if (isBt)
                        ring();
                    else if (isJy)
                        qinglvDialog();
                    Ring = true;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("trueCount", false);
                    editor.commit();
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

        if (ti.getOpen()) {
            if (ti.getFlag() == 1) {
//                time.setOpen(false);
//                timeDao.update(time);
                clolkDialog1();
            } else if (ti.getFlag() == 2) {
//                time.setOpen(false);
//                timeDao.update(time);
                clolkDialog2();
            } else if (ti.getFlag() == 3) {
//                time.setOpen(false);
//                timeDao.update(time);
                clolkDialog3();
            }
        }
    }

    btClockjsDialog dialog;
    btClockjsDialog2 dialog2;
    btClockjsDialog4 dialog4;
    QinglvClockDialog qinglvClockDialog;

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
                Ring = false;
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
                Ring = false;
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
                Ring = false;
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    private void qinglvDialog() {
        qinglvClockDialog = new QinglvClockDialog(this);

        qinglvClockDialog.setOnNegativeClickListener(new QinglvClockDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
//                dialog.dismiss();
            }
        });
        qinglvClockDialog.setOnPositiveClickListener(new QinglvClockDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {
                Ring = false;
            }
        });
        qinglvClockDialog.setCanceledOnTouchOutside(false);
        qinglvClockDialog.setCancelable(false);
        qinglvClockDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        qinglvClockDialog.show();
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
        int nowminutes = hour * 60 * 60 + minutes * 60 + second;

        clockDao=new ClockDaoImpl(this);


        int finishTime = 0;
        times = timeDao.findTimeByMin();
        for (int i = 0; i < times.size(); i++) {
            time = times.get(i);
            boolean open = time.getOpen();
            sumMin = time.getSumMin() * 60;
            if (sumMin >= nowminutes && true == open) {
                finishTime = sumMin;
                break;
            }
        }

        clockBeanList = clockDao.findTimeByMin();
        for (int i = 0; i < clockBeanList.size(); i++) {
            clockBean = clockBeanList.get(i);
            if (!(clockBean.getClockCreater() + "").equals(userId)) {
                Log.e("qqqqqqqqqqqLLLLpppp", clockBean.getClockHour() + "????" + clockBean.getClockMinute());
                int switchs = clockBean.getSwitchs();
                boolean open;
                if (switchs == 1) {
                    open = true;
                } else {
                    open = false;
                }
                sumMin = clockBean.getSumMinute() * 60;
                if (sumMin >= nowminutes && true == open) {
                    if (finishTime == 0)
                        finishTime = sumMin;
                    else if (sumMin < finishTime)
                        finishTime = sumMin;
                    Log.e("qqqqqqqqqqqLLLL", "Time-----" + finishTime + "????" + nowminutes);
                    Log.e("qqqqqqqqqqqLLLL1111", "Time-----" + finishTime + "????" + nowminutes);
                    break;
                }
            }
        }
        Log.e("qqqqqqqqqqqLLLL2222", "Time-----" + finishTime + "????" + nowminutes);
        counttime = finishTime - nowminutes;
        countTimer = new CountTimer(counttime * 1000, 1000);
        countTimer.start();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("trueCount", true);
        editor.commit();

        int time = counttime * 1000;
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

    public void update(Time time) {
        timeDao.update(time);
    }

}