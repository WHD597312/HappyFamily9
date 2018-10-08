
package com.xr.happyFamily.jia.xnty;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class WaterPurifierActivity extends AppCompatActivity{
    Unbinder unbinder;
    Animation anim;
    Animation anim2;
    Animation anim3;
    @BindView(R.id.iv_jsq_qp1)
    ImageView image1;
    @BindView(R.id.iv_jsq_qp2)
    ImageView image2;
    @BindView(R.id.iv_jsq_qp3)
    ImageView image3;
    @BindView(R.id.iv_jsq_qp4)
    ImageView image4;
    @BindView(R.id.iv_jsq_qp5)
    ImageView image5;
    @BindView(R.id.iv_jsq_qp6)
    ImageView image6;
    @BindView(R.id.iv_jsq_qpr1)
    ImageView imager1;
    @BindView(R.id.iv_jsq_qpr2)
    ImageView imager2;
    @BindView(R.id.iv_jsq_qpr3)
    ImageView imager3;
    @BindView(R.id.iv_jsq_qpr4)
    ImageView imager4;
    @BindView(R.id.iv_jsq_qpr5)
    ImageView imager5;
    @BindView(R.id.iv_jsq_qpr6)
    ImageView imager6;
    @BindView(R.id.iv_jsq_kg)
    ImageView imageViewkg;
    @BindView(R.id.iv_jsq_fh)
    ImageView iv_jsq_fh;
    @BindView(R.id.tv_jsq_ql)
    TextView textViewql;
    @BindView(R.id.rl_jsq_r1)
    RelativeLayout relativeLayout;
    @BindView(R.id.iv_jsq_jsq)
    ImageView imageViewjsq;
    @BindView(R.id.iv_jsq_sd)
    ImageView imageViewsd;
    @BindView(R.id.iv_jsq_sd1)
    ImageView imageViewsd1;
    @BindView(R.id.tv_jsq_time)
    TextView textView;
    int flag=0 ;
    int change=0 ;
    private MyCountDownTimer timer;
    private final long TIME = 6 * 1000L;
    private final long INTERVAL = 1000L;
    @BindView(R.id.diffuseview)
    DiffuseView mDiffuseView;
    Handler handler;
    Runnable runnable ;
    Handler handler1;
    Runnable runnable1 ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xnty_jsq);
        unbinder = ButterKnife.bind(this);
        anim = AnimationUtils.loadAnimation(this, R.anim.rotate_jsqqp1);
        anim2 = AnimationUtils.loadAnimation(this, R.anim.rotate_jsqqp2);
        anim3 = AnimationUtils.loadAnimation(this, R.anim.rotate_jsqsd);
        iv_jsq_fh .setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(WaterPurifierActivity.this,R.color.white)));

        initanim();
        imageViewsd1.setTag("close");
        startTimer();
        timerunner();
        inswitch ();



    }
    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long time = millisUntilFinished / 1000;
            textView.setText(String.format(" %02d", time));

        }

        @Override
        public void onFinish() {
            textView.setText("");
            cancelTimer();
        }
    }
    public void start(View view) {
        startTimer();
    }

    public void cancel(View view) {
        textView.setText("");
        cancelTimer();
    }


 //开始倒计时

    private void startTimer() {
        if (timer == null) {
            timer = new MyCountDownTimer(TIME, INTERVAL);
        }
        timer.start();
    }


    /**
     * 取消倒计时
     */

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    private void timerunner(){

        handler=new Handler();
        runnable=new Runnable(){
            public void run() {
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.color_light_blue1));
                imageViewjsq .setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(WaterPurifierActivity.this,R.color.color_light_blue1)));
                textViewql.setText("水质良好");
                imageViewsd .setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(WaterPurifierActivity.this,R.color.color_light_blue1)));
                imageViewkg .setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(WaterPurifierActivity.this,R.color.color_light_blue1)));
                imageViewsd1.setImageResource(R.mipmap.shudi12);
                change=1;
                mDiffuseView.start();
            }
        };
        handler.postDelayed(runnable, 6000);   //6秒 }
    }

    private void initanim(){
        image1.startAnimation(anim);
        image2.startAnimation(anim);
        image3.startAnimation(anim);
        image4.startAnimation(anim);
        image5.startAnimation(anim);
        image6.startAnimation(anim);
        imager1.startAnimation(anim2);
        imager2.startAnimation(anim2);
        imager3.startAnimation(anim2);
        imager4.startAnimation(anim2);
        imager5.startAnimation(anim2);
        imager6.startAnimation(anim2);
    }
    private void opeanorclose(){

    }
    private void inswitch(){//开关
        imageViewkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (change==0){
                    if (flag==0){
                        imageViewkg .setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(WaterPurifierActivity.this,R.color.color_kqh)));
                        mDiffuseView.stop();// 停止扩散
                        image1.clearAnimation();
                        image6.clearAnimation();
                        image3.clearAnimation();
                        image3.clearAnimation();
                        image4.clearAnimation();
                        image5.clearAnimation();
                        image2.clearAnimation();
                        imager1.clearAnimation();
                        imager2.clearAnimation();
                        imager3.clearAnimation();
                        imager4.clearAnimation();
                        imager5.clearAnimation();
                        imager6.clearAnimation();
                        cancelTimer();
                        handler.removeCallbacks(runnable);
                        flag=1;

                    }else {
                        imageViewkg .setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(WaterPurifierActivity.this,R.color.color_qnq)));

                        mDiffuseView.start();
                        image1.startAnimation(anim);
                        image6.startAnimation(anim);
                        image3.startAnimation(anim);
                        image2.startAnimation(anim);
                        image4.startAnimation(anim);
                        image5.startAnimation(anim);
                        image3.startAnimation(anim);
                        imager1.startAnimation(anim2);
                        imager2.startAnimation(anim2);
                        imager3.startAnimation(anim2);
                        imager4.startAnimation(anim2);
                        imager5.startAnimation(anim2);
                        imager6.startAnimation(anim2);
                        startTimer();
                        timerunner();
                        flag=0;

                    }
                }else if (change==1){
                    if (flag==0){
                        imageViewkg .setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(WaterPurifierActivity.this,R.color.color_kqh)));
                        mDiffuseView.stop();// 停止扩散
                        image1.clearAnimation();
                        image6.clearAnimation();
                        image3.clearAnimation();
                        image3.clearAnimation();
                        image4.clearAnimation();
                        image5.clearAnimation();
                        image2.clearAnimation();
                        imager1.clearAnimation();
                        imager2.clearAnimation();
                        imager3.clearAnimation();
                        imager4.clearAnimation();
                        imager5.clearAnimation();
                        imager6.clearAnimation();
                        handler.removeCallbacks(runnable);
                        flag=1;
                    }else {
                        imageViewkg .setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(WaterPurifierActivity.this,R.color.color_light_blue1)));
                        mDiffuseView.start();
                        image1.startAnimation(anim);
                        image6.startAnimation(anim);
                        image3.startAnimation(anim);
                        image2.startAnimation(anim);
                        image4.startAnimation(anim);
                        image5.startAnimation(anim);
                        image3.startAnimation(anim);
                        imager1.startAnimation(anim2);
                        imager2.startAnimation(anim2);
                        imager3.startAnimation(anim2);
                        imager4.startAnimation(anim2);
                        imager5.startAnimation(anim2);
                        imager6.startAnimation(anim2);


                        flag=0;
                    }
                }

            }

        });

    }

    @OnClick({R.id.iv_jsq_sd,R.id.iv_jsq_fh})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_jsq_sd:
                if (change==0){
                    Toast.makeText(this, "正在净水中", Toast.LENGTH_SHORT).show();
                }else if (change==1){
                    if ("close".equals(imageViewsd1.getTag())){
                        imageViewsd1.startAnimation(anim3);
                        imageViewsd1.setTag("open");
                    }else {
                        imageViewsd1.clearAnimation();
                        imageViewsd1.setTag("close");
                    }

                }

                break;
            case R.id.iv_jsq_fh:

                finish();
                break;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        mDiffuseView.start();
        // mDiffuseView.stop();// 停止扩散
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
        handler.removeCallbacks(runnable);

        cancelTimer();
    }

}
