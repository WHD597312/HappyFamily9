package com.xr.happyFamily.jia.xnty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xr.happyFamily.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AirConditionerActivity extends AppCompatActivity implements View.OnClickListener {
    Unbinder unbinder;
    Animation anim;
    Animation anim1;
    Animation anim2;
    @BindView(R.id.tm_ktl1)
    Timepicker1 timepickerl1;
    @BindView(R.id.tm_ktl2)
    Timepicker1 timepickerl2;
    @BindView(R.id.tm_ktl3)
    Timepicker1 timepickerl3;
    @BindView(R.id.tm_ktl4)
    Timepicker1 timepickerl4;
    @BindView(R.id.tm_kth1)
    Timepicker2 timepickerh1;
    @BindView(R.id.tm_kth2)
    Timepicker2 timepickerh2;
    @BindView(R.id.tm_kth3)
    Timepicker2 timepickerh3;
    @BindView(R.id.tm_kth4)
    Timepicker2 timepickerh4;
    @BindView(R.id.kt_d1)
    ImageView image1;
    @BindView(R.id.kt_d2)
    ImageView image2;
    @BindView(R.id.kt_d3)
    ImageView image3;
    @BindView(R.id.kt_d4)
    ImageView image4;
    @BindView(R.id.kt_d5)
    ImageView image5;
    @BindView(R.id.kt_d6)
    ImageView image6;
    @BindView(R.id.kt_d7)
    ImageView image7;
    @BindView(R.id.kt_d8)
    ImageView image8;
    @BindView(R.id.kt_d9)
    ImageView image9;
    @BindView(R.id.kt_d10)
    ImageView image10;
    @BindView(R.id.kt_d11)
    ImageView image11;
    @BindView(R.id.kt_d12)
    ImageView image12;
    @BindView(R.id.kt_dd1)
    ImageView image13;
    @BindView(R.id.kt_dd2)
    ImageView image14;
    @BindView(R.id.kt_dd3)
    ImageView image15;
    @BindView(R.id.kt_dd4)
    ImageView image16;
    @BindView(R.id.kt_dd5)
    ImageView image17;
    @BindView(R.id.kt_dd6)
    ImageView image18;
    @BindView(R.id.kt_dd7)
    ImageView image19;
    @BindView(R.id.kt_dd8)
    ImageView image20;
    @BindView(R.id.kt_dd9)
    ImageView image21;
    @BindView(R.id.kt_dd10)
    ImageView image22;
    @BindView(R.id.kt_dd11)
    ImageView image23;
    @BindView(R.id.kt_dd12)
    ImageView image24;
    @BindView(R.id.kt_dd13)
    ImageView image25;
    @BindView(R.id.kt_dd14)
    ImageView image26;
    @BindView(R.id.kt_dd15)
    ImageView image27;
    @BindView(R.id.kt_dd16)
    ImageView image28;
    @BindView(R.id.kt_dd17)
    ImageView image29;
    @BindView(R.id.iv_kt_zr)
    ImageView imageViewzr;
    @BindView(R.id.iv_kt_zl)
    ImageView imageViewzl;
    @BindView(R.id.iv_kt_kt1)
    ImageView imageViewkt;
    @BindView(R.id.rl_kt_1)
    RelativeLayout relativeLayoutbg;
    @BindView(R.id.tv_kt_33)
    TextView textView33;
    @BindView(R.id.tv_kt_23)
    TextView textView23;
    @BindView(R.id.tv_kt_sfbz)
    ImageView imageViewsfbz;
    @BindView(R.id.iv_kt_ds)
    ImageView imageViewds;
    @BindView(R.id.iv_kt_kg)
    ImageView imageViewkg;
    @BindView(R.id.iv_kt_sf)
    ImageView imageViewsf;
    @BindView(R.id.iv_kt_zd)
    ImageView imageViewzd;
    @BindView(R.id.iv_kt_sm)
    ImageView imageViewsm;
    @BindView(R.id.tv_qnq_ds)
    TextView textViewds;
    private BottomSheetBehavior bottomSheetBehaviordsh;
    private BottomSheetBehavior bottomSheetBehaviordsl;
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xnty_kt);
        unbinder = ButterKnife.bind(this);
        initTimer();
        anim = AnimationUtils.loadAnimation(this, R.anim.rotate_znczs);
        anim2 = AnimationUtils.loadAnimation(this, R.anim.rotate_znczx1);
        anim1 = AnimationUtils.loadAnimation(this, R.anim.rotate_znczx);
        initAnima();
        bottomSheetBehaviordsh= BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayoutdsh));
        bottomSheetBehaviordsl= BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayoutdslan));
        initbutton();


    }
    private  void initAnima(){//添加降落动画

        image1.startAnimation(anim);

        image3.startAnimation(anim);
        image4.startAnimation(anim);
        image28.startAnimation(anim);
        image29.startAnimation(anim);
        image7.startAnimation(anim);
        image15.startAnimation(anim);
        image9.startAnimation(anim);
        image13.startAnimation(anim);
        image10.startAnimation(anim);
        image11.startAnimation(anim2);
        image12.startAnimation(anim2);
        image24.startAnimation(anim2);
        image14.startAnimation(anim2);
        image16.startAnimation(anim2);
        image8.startAnimation(anim2);
        image17.startAnimation(anim2);
        image18.startAnimation(anim1);
        image19.startAnimation(anim1);
        image20.startAnimation(anim1);
        image21.startAnimation(anim1);
        image22.startAnimation(anim1);
        image23.startAnimation(anim1);
        image5.startAnimation(anim1);
        image6.startAnimation(anim1);
        image25.startAnimation(anim1);
        image26.startAnimation(anim1);
        image27.startAnimation(anim1);
        image2.startAnimation(anim1);

    }
    private void stopanim(){//清除动画效果
        image1.clearAnimation();
        image2.clearAnimation();
        image3.clearAnimation();
        image4.clearAnimation();
        image5.clearAnimation();
        image6.clearAnimation();
        image7.clearAnimation();
        image8.clearAnimation();
        image9.clearAnimation();
        image10.clearAnimation();
        image11.clearAnimation();
        image12.clearAnimation();
        image13.clearAnimation();
        image14.clearAnimation();
        image15.clearAnimation();
        image16.clearAnimation();
        image17.clearAnimation();
        image18.clearAnimation();
        image19.clearAnimation();
        image20.clearAnimation();
        image21.clearAnimation();
        image22.clearAnimation();
        image23.clearAnimation();
        image24.clearAnimation();
        image25.clearAnimation();
        image26.clearAnimation();
        image27.clearAnimation();
        image28.clearAnimation();
        image29.clearAnimation();

    }
    private void fast(){//雪花下落的速度快中慢
        anim.setDuration(1100);
        anim1.setDuration(1300);
        anim2.setDuration(1000);
        initAnima();
    }
    private void nomal(){
        anim.setDuration(2450);
        anim1.setDuration(2700);
        anim2.setDuration(2300);
        initAnima();
    }
    private void slow(){
        anim.setDuration(2750);
        anim1.setDuration(3200);
        anim2.setDuration(2900);
        initAnima();
    }
    private void changImgn(){//切换暖色调图片
        image7.setImageResource(R.mipmap.kt_xh1n);
        image13.setImageResource(R.mipmap.kt_xh2n);
        image8.setImageResource(R.mipmap.kt_xh3n);
        image14.setImageResource(R.mipmap.kt_xh4n);
        image9.setImageResource(R.mipmap.kt_xh1n);
        image15.setImageResource(R.mipmap.kt_xh2n);//dd3
        image10.setImageResource(R.mipmap.kt_xh3n);
        image16.setImageResource(R.mipmap.kt_xh4n);
        image17.setImageResource(R.mipmap.kt_xh1n);
        image11.setImageResource(R.mipmap.kt_xh2n);
        image18.setImageResource(R.mipmap.kt_xh3n);//dd6
        image12.setImageResource(R.mipmap.kt_xh4n);
        image19.setImageResource(R.mipmap.kt_xh1n);
        image20.setImageResource(R.mipmap.kt_xh2n);
        image21.setImageResource(R.mipmap.kt_xh3n);
        image22.setImageResource(R.mipmap.kt_xh4n);
        image2.setImageResource(R.mipmap.kt_xh1n);
        image1.setImageResource(R.mipmap.kt_xh2n);
        image23.setImageResource(R.mipmap.kt_xh3n);//dd11
        image3.setImageResource(R.mipmap.kt_xh4n);
        image24.setImageResource(R.mipmap.kt_xh1n);
        image4.setImageResource(R.mipmap.kt_xh2n);
        image25.setImageResource(R.mipmap.kt_xh3n);
        image26.setImageResource(R.mipmap.kt_xh4n);
        image5.setImageResource(R.mipmap.kt_xh1n);
        image27.setImageResource(R.mipmap.kt_xh2n);
        image6.setImageResource(R.mipmap.kt_xh3n);
        image28.setImageResource(R.mipmap.kt_xh4n);
        image29.setImageResource(R.mipmap.kt_xh1n);
        imageViewkt.setImageResource(R.mipmap.kt_ktn);
        relativeLayoutbg.setBackgroundColor(this.getResources().getColor(R.color.color_yellow1));
        textView33.setTextColor(getResources().getColor(R.color.color_yellow_yuan));
        textView23.setTextColor(getResources().getColor(R.color.color_yellow_yuan));
        imageViewsfbz.setImageResource(R.mipmap.kt_sfn);
        imageViewds.setImageResource(R.mipmap.kt_dsn);
        imageViewkg.setImageResource(R.mipmap.kt_kgn);
    }
    private void changImgl(){//切换冷色调页面
        image7.setImageResource(R.mipmap.kt_xh1);
        image13.setImageResource(R.mipmap.kt_xh2);
        image8.setImageResource(R.mipmap.kt_xh3);
        image14.setImageResource(R.mipmap.kt_xh4);
        image9.setImageResource(R.mipmap.kt_xh1);
        image15.setImageResource(R.mipmap.kt_xh2);//dd3
        image10.setImageResource(R.mipmap.kt_xh3);
        image16.setImageResource(R.mipmap.kt_xh4);
        image17.setImageResource(R.mipmap.kt_xh1);
        image11.setImageResource(R.mipmap.kt_xh2);
        image18.setImageResource(R.mipmap.kt_xh3);//dd6
        image12.setImageResource(R.mipmap.kt_xh4);
        image19.setImageResource(R.mipmap.kt_xh1);
        image20.setImageResource(R.mipmap.kt_xh2);
        image21.setImageResource(R.mipmap.kt_xh3);
        image22.setImageResource(R.mipmap.kt_xh4);
        image2.setImageResource(R.mipmap.kt_xh1);
        image1.setImageResource(R.mipmap.kt_xh2);
        image23.setImageResource(R.mipmap.kt_xh3);//dd11
        image3.setImageResource(R.mipmap.kt_xh4);
        image24.setImageResource(R.mipmap.kt_xh1);
        image4.setImageResource(R.mipmap.kt_xh2);
        image25.setImageResource(R.mipmap.kt_xh3);
        image26.setImageResource(R.mipmap.kt_xh4);
        image5.setImageResource(R.mipmap.kt_xh1);
        image27.setImageResource(R.mipmap.kt_xh2);
        image6.setImageResource(R.mipmap.kt_xh3);
        image28.setImageResource(R.mipmap.kt_xh4);
        image29.setImageResource(R.mipmap.kt_xh1);
        imageViewkt.setImageResource(R.mipmap.kt_kt);
        relativeLayoutbg.setBackgroundColor(this.getResources().getColor(R.color.color_blue1));
        textView23.setTextColor(getResources().getColor(R.color.color_blue_yuan));
        textView33.setTextColor(getResources().getColor(R.color.color_blue_yuan));
        imageViewsfbz.setImageResource(R.mipmap.kt_sfk);
        imageViewds.setImageResource(R.mipmap.kt_dsk);
        imageViewkg.setImageResource(R.mipmap.kt_kg);
    }
    int change=0;
    int change1=0;
    int change2=0;
    int change3=0;
   private  void  initbutton(){
       imageViewkg.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {//开关键

           if (flag==0){
               if (change==0){
                   imageViewkg.setImageResource(R.mipmap.kt_kgg);
                   stopanim();
                   change=1;
               }else if (change==1){
                   imageViewkg.setImageResource(R.mipmap.kt_kg);
                   initAnima();
                   change=0;
               }
           }
           else if (flag==1){

                   if (change==0){
                       imageViewkg.setImageResource(R.mipmap.kt_kgg);
                       stopanim();
                       change=1;
                   }else if (change==1){
                       imageViewkg.setImageResource(R.mipmap.kt_kgn);
                       initAnima();
                       change=0;
                   }
           }


           }
       });

       imageViewsf.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {//送风按钮

               if (flag==0){
                   if (change1==1){
                       imageViewsf.setImageResource(R.mipmap.kt_sfg);
                       nomal();
                       change1=0;
                   }else if (change1==0){
                       imageViewsf.setImageResource(R.mipmap.kt_sfk);
                       imageViewsm.setImageResource(R.mipmap.kt_smg);
                       imageViewzd.setImageResource(R.mipmap.kt_zdg);
                       fast();
                       change1=1;
                   }
               }
               else if (flag==1){

                   if (change1==1){
                       imageViewsf.setImageResource(R.mipmap.kt_sfg);
                       nomal();
                       change1=0;
                   }else if (change1==0){
                       imageViewsf.setImageResource(R.mipmap.kt_sfn);
                       imageViewsm.setImageResource(R.mipmap.kt_smg);
                       imageViewzd.setImageResource(R.mipmap.kt_zdg);
                       fast();
                       change1=1;
                   }
               }


           }
       });

       imageViewsm.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {//睡眠按钮

               if (flag==0){
                   if (change2==1){
                       imageViewsm.setImageResource(R.mipmap.kt_smg);
                       nomal();
                       change2=0;
                   }else if (change2==0){
                       imageViewsm.setImageResource(R.mipmap.kt_smk);
                       imageViewsf.setImageResource(R.mipmap.kt_sfg);
                       imageViewzd.setImageResource(R.mipmap.kt_zdg);
                       slow();
                       change2=1;
                   }
               }
               else if (flag==1){

                   if (change2==1){
                       imageViewsm.setImageResource(R.mipmap.kt_smg);
                       nomal();
                       change2=0;
                   }else if (change==0){
                       imageViewsm.setImageResource(R.mipmap.kt_smn);
                       imageViewsf.setImageResource(R.mipmap.kt_sfg);
                       imageViewzd.setImageResource(R.mipmap.kt_zdg);
                       slow();
                       change2=1;
                   }
               }


           }
       });
       imageViewzd.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {//自动按钮

               nomal();
               if (flag==0){
                   if (change3==1){
                       imageViewzd.setImageResource(R.mipmap.kt_zdg);
                       change3=0;
                   }else if (change3==0){
                       imageViewzd.setImageResource(R.mipmap.kt_zdk);
                       imageViewsm.setImageResource(R.mipmap.kt_smg);
                       imageViewsf.setImageResource(R.mipmap.kt_sfg);

                       change3=1;
                   }
               }
               else if (flag==1){

                   if (change3==1){
                       imageViewzd.setImageResource(R.mipmap.kt_zdg);

                       change3=0;
                   }else if (change3==0){
                       imageViewzd.setImageResource(R.mipmap.kt_zdn);
                       imageViewsm.setImageResource(R.mipmap.kt_smg);
                       imageViewsf.setImageResource(R.mipmap.kt_sfg);
                       change3=1;
                   }
               }


           }
       });


   }


    private void initTimer(){//设置定时时间
        timepickerl1.setMaxValue(23);
        timepickerl1.setMinValue(00);
        timepickerl1.setValue(12);
        timepickerl1.setNumberPickerDividerColor(timepickerl1);

        timepickerl2.setMaxValue(59);
        timepickerl2.setMinValue(00);
        timepickerl2.setValue(00);
        //timepicker2.setBackgroundColor(Color.LTGRAY);
        timepickerl2.setNumberPickerDividerColor(timepickerl2);
        timepickerl3.setMaxValue(23);
        timepickerl3.setMinValue(00);
        timepickerl3.setValue(13);
        //timepicker3.setBackgroundColor(Color.LTGRAY);
        timepickerl3.setNumberPickerDividerColor(timepickerl3);
        timepickerl4.setMaxValue(59);
        timepickerl4.setMinValue(00);
        timepickerl4.setValue(00);
        //timepicker4.setBackgroundColor(Color.LTGRAY);
        timepickerl4.setNumberPickerDividerColor(timepickerl4);

        timepickerh1.setMaxValue(23);
        timepickerh1.setMinValue(00);
        timepickerh1.setValue(12);
        timepickerh1.setNumberPickerDividerColor(timepickerh1);

        timepickerh2.setMaxValue(59);
        timepickerh2.setMinValue(00);
        timepickerh2.setValue(00);
        //timepicker2.setBackgroundColor(Color.LTGRAY);
        timepickerh2.setNumberPickerDividerColor(timepickerh2);
        timepickerh3.setMaxValue(23);
        timepickerh3.setMinValue(00);
        timepickerh3.setValue(13);
        //timepicker3.setBackgroundColor(Color.LTGRAY);
        timepickerh3.setNumberPickerDividerColor(timepickerh3);
        timepickerh4.setMaxValue(59);
        timepickerh4.setMinValue(00);
        timepickerh4.setValue(00);
        //timepicker4.setBackgroundColor(Color.LTGRAY);
        timepickerh4.setNumberPickerDividerColor(timepickerh4);
    }
    int flag = 0;
    @OnClick({R.id.iv_kt_ds,R.id.iv_ktl_qx,R.id.iv_kt_zr,R.id.iv_kt_zl,R.id.iv_ktn_qx,R.id.iv_ktl_qd,R.id.iv_ktn_qd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_kt_ds://定时设备
                if (flag==0){
                    if (bottomSheetBehaviordsl.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);按键可在隐藏
                    } else if (bottomSheetBehaviordsl.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehaviordsl.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                        bottomSheetBehaviordsl.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }else if (flag==1){
                    if (bottomSheetBehaviordsh.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);按键可在隐藏
                    } else if (bottomSheetBehaviordsh.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehaviordsh.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                        bottomSheetBehaviordsh.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }
                break;
            case R.id.iv_ktl_qx:
                bottomSheetBehaviordsl.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_ktn_qx:
                bottomSheetBehaviordsh.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_ktn_qd:
                int sm = timepickerh2.getValue();
                int sh = timepickerh1.getValue();
                int em = timepickerh4.getValue();
                int eh = timepickerh3.getValue();
                String smh;
                String emh;
                if (0<=sm&&sm<10){
                    smh = "0"+sm ;
                }else {
                    smh = ""+sm ;
                }
                if (0<=em&&em<10){
                    emh = "0"+em ;
                }else {
                    emh = ""+em ;
                }


                textViewds.setText(sh + ":" +smh + "-" + eh + ":" + emh);
                bottomSheetBehaviordsh.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_ktl_qd://分清1和L
                int sm1 = timepickerl2.getValue();
                int sh1 = timepickerl1.getValue();
                int em1 = timepickerl4.getValue();
                int eh1 = timepickerl3.getValue();
                String sml;
                String eml;
                if (0<=sm1&&sm1<10){
                    sml = "0"+sm1 ;
                }else {
                    sml = ""+sm1 ;
                }
                if (0<=em1&&em1<10){
                    eml = "0"+em1 ;
                }else {
                    eml = ""+em1 ;
                }


                textViewds.setText(sh1 + ":" +sml + "-" + eh1 + ":" + eml);
                bottomSheetBehaviordsl.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case   R.id.iv_kt_zr://制热设备
                    imageViewzr.setImageResource(R.mipmap.kt_zrn);
                    imageViewzl.setImageResource(R.mipmap.kt_zlg);
                    imageViewsf.setImageResource(R.mipmap.kt_sfg);
                    imageViewzd.setImageResource(R.mipmap.kt_zdg);
                    imageViewsm.setImageResource(R.mipmap.kt_smg);
                    changImgn();
                    nomal();
                    flag=1;

                break;
            case    R.id.iv_kt_zl://开启制冷设备
                    imageViewzr.setImageResource(R.mipmap.kt_zrg);
                    imageViewzl.setImageResource(R.mipmap.kt_zlk);
                    imageViewsf.setImageResource(R.mipmap.kt_sfg);
                    imageViewzd.setImageResource(R.mipmap.kt_zdg);
                    imageViewsm.setImageResource(R.mipmap.kt_smg);
                    changImgl();
                    nomal();
                    flag =0;

                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }

}

