package com.xr.happyFamily.jia.xnty;

import android.os.Bundle;
import android.os.Handler;
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
    int flag=0 ;
    int change=0 ;

    DiffuseView mDiffuseView;
    Handler handler;
    Runnable runnable ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xnty_jsq);
        unbinder = ButterKnife.bind(this);
        imageViewsd1.setTag("close");
        anim = AnimationUtils.loadAnimation(this, R.anim.rotate_jsqqp1);
        anim2 = AnimationUtils.loadAnimation(this, R.anim.rotate_jsqqp2);
        anim3 = AnimationUtils.loadAnimation(this, R.anim.rotate_jsqsd);
        initanim();

       mDiffuseView = (DiffuseView) findViewById(R.id.diffuseview);

      timerunner();

        inswitch ();

    }
    private void timerunner(){

        handler=new Handler();
        runnable=new Runnable(){
            public void run() {
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.color_light_blue1));
                imageViewjsq.setImageResource(R.mipmap.jsq_jsq2);
                textViewql.setText("水质良好");
                imageViewsd.setImageResource(R.mipmap.jsq_sd2);
                imageViewkg.setImageResource(R.mipmap.jsq_kgk2);
                imageViewsd1.setImageResource(R.mipmap.shudi12);
                change=1;
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
                    imageViewkg.setImageResource(R.mipmap.jsq_kgg);
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
                    imageViewkg.setImageResource(R.mipmap.jsq_kgk1);
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
                    timerunner();
                    flag=0;

                }
                }else if (change==1){
                    if (flag==0){
                        imageViewkg.setImageResource(R.mipmap.jsq_kgg);
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
                        imageViewkg.setImageResource(R.mipmap.jsq_kgk2);
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
                        timerunner();
                        flag=0;
                    }
                }

            }

        });

    }

    @OnClick({R.id.iv_jsq_sd})
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
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        mDiffuseView.start(); // 开始扩散
        // mDiffuseView.stop();// 停止扩散
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }

}
