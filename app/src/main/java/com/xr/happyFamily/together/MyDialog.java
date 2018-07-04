package com.xr.happyFamily.together;


import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.xr.happyFamily.R;

import pl.droidsonroids.gif.GifDrawable;

//加载动画
public class MyDialog extends Dialog {

    private static Context context;
    private static MyDialog dialog;
    private static CountTimer countTimer;
    private ImageView ivProgress;
    GifDrawable gifDrawable;

    public MyDialog(Context context) {
        super(context);
        this.context = context;
    }

    public MyDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;

    }
    //显示dialog的方法
    public static MyDialog showDialog(Context context){
        dialog = new MyDialog(context, R.style.JyDialog);//dialog样式
        dialog.setContentView(R.layout.dialog_layout);//dialog布局文件
        dialog.setCanceledOnTouchOutside(false);//点击外部不允许关闭dialog

        ImageView imageView= (ImageView) dialog.findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        countTimer=new CountTimer(5000,1000);
        countTimer.start();

        return dialog;
    }

    //关闭dialog
    public static void closeDialog(Dialog mDialogUtils) {
        if (mDialogUtils != null && mDialogUtils.isShowing()) {
            countTimer.cancel();
            mDialogUtils.dismiss();

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && dialog != null){

            ivProgress = (ImageView) dialog.findViewById(R.id.ivProgress);
            try {
                gifDrawable=new GifDrawable(context.getResources(),R.mipmap.loading);
            }catch (Exception e){
                e.printStackTrace();
            }
            if (gifDrawable!=null){
                gifDrawable.start();
                ivProgress.setImageDrawable(gifDrawable);
            }
        }
    }

    static class CountTimer extends CountDownTimer {
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
            Log.e("Tag", "倒计时完成"+context.toString());
            dialog.dismiss();
            Toast.makeText(context,"加载超时请重试",Toast.LENGTH_SHORT).show();
        }
    }

}