package com.xr.happyFamily.le.BtClock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.xr.database.dao.daoimpl.TimeDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.le.pojo.Time;
import com.xr.happyFamily.le.utils.WakeAndLock;
import com.xr.happyFamily.le.view.btClockjsDialog;
import com.xr.happyFamily.le.view.btClockjsDialog2;
import com.xr.happyFamily.le.view.btClockjsDialog3;
import com.xr.happyFamily.le.view.btClockjsDialog4;

import java.util.List;


public class RingReceiver extends BroadcastReceiver{
    Context mContext;
    private MediaPlayer mediaPlayer;
    btClockjsDialog dialog;
    btClockjsDialog2 dialog2;
    btClockjsDialog4 dialog4;

    private TimeDaoImpl timeDao;

    private String op = "on";
    public void onReceive(Context context, Intent intent) {
        //广播的名字:包名+随便取一个名字
        if("com.zking.android29_alarm_notification.RING".equals(intent.getAction())){
            Log.i("test","闹钟响了！！！");
            mContext=context;
            timeDao=new TimeDaoImpl(mContext);
//            Intent intent2=new Intent(context,RingActivity.class);
//            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//需要把启动的新的Activity放到一个新的任务栈里面才能启动
//            context.startActivity(intent2);

            //跳转到Activity中
            int hour=intent.getIntExtra("hour",0);
            int minutes=intent.getIntExtra("minutes",0);
            List<Time> times=timeDao.findTimesByHourAndMin(hour,minutes);
            if (!times.isEmpty()){
                Time time=times.get(0);
                if (time.getOpen()){
                    if (time.getFlag()==1){
                        clolkDialog1();
                    }else if (time.getFlag()==2){
                        clolkDialog2();
                    }else if (time.getFlag()==3){
                        clolkDialog3();
                    }

                }
            }
//            android.util.Log.i("cxq", "ScreenControlBroadcast");
//            op = intent.getStringExtra("screen");
//            WakeAndLock wakeAndLock = new WakeAndLock(mContext);
//            if (op.equals("on")) {
//                wakeAndLock.screenOn();
//            }



//            mediaPlayer = MediaPlayer.create(context, R.raw.music1);
//            mediaPlayer.start();//一进来就播放
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
        int x=  dialog.getX();
        int y = dialog.getY();
        final String text1= dialog.getText();
        int z =x*y;

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

}
