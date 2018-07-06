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
import com.xr.happyFamily.le.view.btClockjsDialog;

import java.util.List;


public class RingReceiver extends BroadcastReceiver{
    Context mContext;
    private MediaPlayer mediaPlayer;
    btClockjsDialog dialog;
    private TimeDaoImpl timeDao;
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
                    clolkDialog();
                }else {

                }
            }

//            mediaPlayer = MediaPlayer.create(context, R.raw.music1);
//            mediaPlayer.start();//一进来就播放
        }
    }
    private void clolkDialog() {
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
        Window w = dialog.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        dialog.onWindowAttributesChanged(lp);
        
    }

}
