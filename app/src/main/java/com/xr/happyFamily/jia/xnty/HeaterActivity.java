package com.xr.happyFamily.jia.xnty;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HeaterActivity extends AppCompatActivity implements View.OnClickListener {
    Unbinder unbinder;
    Animation rotate;
    Animation anim1;
    Animation anim2;
    Animation anim3;
    @BindView(R.id.tm_kth1)
    Timepicker2 timepickerh1;
    @BindView(R.id.tm_kth2)
    Timepicker2 timepickerh2;
    @BindView(R.id.tm_kth3)
    Timepicker2 timepickerh3;
    @BindView(R.id.tm_kth4)
    Timepicker2 timepickerh4;
    @BindView(R.id.iv_qnq_fs)
    ImageView imageViewfs;
    @BindView(R.id.iv_qnq_yw1)
    ImageView imageView1;
    @BindView(R.id.iv_qnq_yw2)
    ImageView imageView2;
    @BindView(R.id.iv_qnq_yw3)
    ImageView imageView3;
    @BindView(R.id.iv_qnq_yw7)
    ImageView imageView7;
    @BindView(R.id.iv_qnq_yw8)
    ImageView imageView8;
    @BindView(R.id.iv_qnq_yw9)
    ImageView imageView9;
    @BindView(R.id.iv_qnq_pb)
    ImageView imageViewpb;
    @BindView(R.id.iv_qnq_gl)
    ImageView imageViewgl;
    @BindView(R.id.iv_qnq_js)
    ImageView imageViewjs;
    @BindView(R.id.iv_qnq_kg)
    ImageView imageViewkg;
    @BindView(R.id.tv_qnq_ds)
    TextView textViewds;
    int  flag = 0;
    boolean isFirst=true;
    Handler handler ;
     Runnable runnable;
    private BottomSheetBehavior bottomSheetBehaviordsh;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xnty_qnq);
        unbinder = ButterKnife.bind(this);

        initanim();
        timerun();
        initTimer();
        bottomSheetBehaviordsh= BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayoutdsh));
        anim1 = AnimationUtils.loadAnimation(this,R.anim.rotate_qnqyw);
        imageView1.startAnimation(anim1);
        imageView2.startAnimation(anim1);
        imageView3.startAnimation(anim1);
        anim3 = AnimationUtils.loadAnimation(this,R.anim.rotate_qnqyw3);
        imageViewpb.setTag("close");
        imageViewgl.setTag("close");
        imageViewjs.setTag("close");
        imageViewkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag==0){
                    imageViewkg.setImageResource(R.mipmap.qnq_kgg);
                    imageViewfs.clearAnimation();
                    imageView1.clearAnimation();
                    imageView2.clearAnimation();
                    imageView3.clearAnimation();
                    imageView7.clearAnimation();
                    imageView8.clearAnimation();
                    imageView9.clearAnimation();
                    handler.removeCallbacks(runnable);
                    flag=1;
                }else {
                    imageViewkg.setImageResource(R.mipmap.qnq_kgk);
                    imageView1.startAnimation(anim1);
                    imageView2.startAnimation(anim1);
                    imageView3.startAnimation(anim1);
                    imageViewfs.startAnimation(rotate);
                    timerun();
                    flag=0;
                }
            }
        });
    }
    private void initTimer(){//设置定时时间


        timepickerh1.setMaxValue(23);
        timepickerh1.setMinValue(00);
        timepickerh1.setValue(12);
        timepickerh1.setNumberPickerDividerColor(timepickerh1);

        timepickerh2.setMaxValue(59);
        timepickerh2.setMinValue(00);
        timepickerh2.setValue(00);

        timepickerh2.setNumberPickerDividerColor(timepickerh2);
        timepickerh3.setMaxValue(23);
        timepickerh3.setMinValue(00);
        timepickerh3.setValue(13);

        timepickerh3.setNumberPickerDividerColor(timepickerh3);
        timepickerh4.setMaxValue(59);
        timepickerh4.setMinValue(00);
        timepickerh4.setValue(00);

        timepickerh4.setNumberPickerDividerColor(timepickerh4);
    }
        private void initanim(){
            rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_kqjh);
            LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
            rotate.setInterpolator(lin);
            imageViewfs.startAnimation(rotate);
        }
        private void timerun(){
            handler = new Handler();
           runnable = new Runnable(){
                @Override
                public void run() {
                    // TODO Auto-generated method stub


                    imageView7.startAnimation(anim3);
                    imageView8.startAnimation(anim3);
                    imageView9.startAnimation(anim3);

                    handler.postDelayed(this, 3000);

                }

            };
            handler.postDelayed(runnable, 3900);// 打开定时器，执行操作
        }

        @OnClick({R.id.iv_qnq_pb,R.id.iv_qnq_gl,R.id.iv_qnq_js,R.id.iv_ktn_qd,R.id.iv_ktn_qx,R.id.iv_qnq_ds})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_qnq_pb :
              if ("open".equals(imageViewpb.getTag())){
                  imageViewpb.setImageResource(R.mipmap.qnq_pb);
                  imageViewpb.setTag("close");
              }else if ("close".equals(imageViewpb.getTag())){
                  imageViewpb.setImageResource(R.mipmap.qnq_pbk);
                  imageViewpb.setTag("open");
              }
                break;
            case R.id.iv_qnq_gl :
                if ("open".equals(imageViewgl.getTag())){
                    imageViewgl.setImageResource(R.mipmap.qnq_gl);
                    imageViewgl.setTag("close");
                }else if ("close".equals(imageViewpb.getTag())){
                    imageViewgl.setImageResource(R.mipmap.qnq_glk);
                    imageViewgl.setTag("open");
                }
                break;
            case R.id.iv_qnq_js :
                if ("open".equals(imageViewjs.getTag())){
                    imageViewjs.setImageResource(R.mipmap.qnq_sd);
                    imageViewjs.setTag("close");
                }else if ("close".equals(imageViewjs.getTag())){
                    imageViewjs.setImageResource(R.mipmap.qnq_sdk);
                    imageViewjs.setTag("open");
                }
                break;
            case R.id.iv_qnq_ds :
            if (bottomSheetBehaviordsh.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);按键可在隐藏
            } else if (bottomSheetBehaviordsh.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehaviordsh.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehaviordsh.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            break;
            case R.id.iv_ktn_qd:
                int sm = timepickerh2.getValue();
                int sh = timepickerh1.getValue();
                int em = timepickerh4.getValue();
                int eh = timepickerh3.getValue();
                String sml;
                String eml;
                if (0<=sm&&sm<10){
                 sml = "0"+sm ;
                }else {
                    sml = ""+sm ;
                }
                if (0<=em&&em<10){
                    eml = "0"+em ;
                }else {
                    eml = ""+em ;
                }


                textViewds.setText(sh + ":" +sml + "-" + eh + ":" + eml);
                bottomSheetBehaviordsh.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_ktn_qx:
                bottomSheetBehaviordsh.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
        }

        }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
        handler.removeCallbacks(runnable);
    }

}
