package com.xr.happyFamily.le.BtClock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.WindowManager;

import com.xr.happyFamily.R;
import com.xr.happyFamily.le.view.btClockjsDialog;


public class RingReceiver extends BroadcastReceiver{
    Context mContext;
    private MediaPlayer mediaPlayer;
    btClockjsDialog dialog;
    public void onReceive(Context context, Intent intent) {
        //广播的名字:包名+随便取一个名字
        if("com.zking.android29_alarm_notification.RING".equals(intent.getAction())){
            Log.i("test","闹钟响了！！！");
            mContext=context;
//            Intent intent2=new Intent(context,RingActivity.class);
//            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//需要把启动的新的Activity放到一个新的任务栈里面才能启动
//            context.startActivity(intent2);

            //跳转到Activity中
            clolkDialog();
            mediaPlayer = MediaPlayer.create(context, R.raw.music1);
            mediaPlayer.start();//一进来就播放
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
//                dialog.dismiss();
                if ("z".equals(text1)){
                    mediaPlayer.stop();
                   dialog.dismiss();
                }

            }
        });
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

}
