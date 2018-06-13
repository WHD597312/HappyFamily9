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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HeaterActivity extends AppCompatActivity implements View.OnClickListener , SeekBar.OnSeekBarChangeListener {
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
    @BindView(R.id.iv_zncz_qx)
    ImageView imageViewqx;
    @BindView(R.id.iv_zncz_qd)
    ImageView imageViewqd;
    @BindView(R.id.tv_zncz_wd)
    TextView textViewwd;
    @BindView(R.id.iv_qnq_kg)
    ImageView imageViewkg;
    @BindView(R.id.tv_qnq_ds)
    TextView textViewds;
    @BindView(R.id.tv_qnq_18)
    TextView textView18;
    @BindView(R.id.tv_qnq_32)
    TextView textView32;
    int  flag = 0;
    boolean isFirst=true;
    Handler handler ;
     Runnable runnable;
    private int recLen1 = 18;
    TimerTask task1;
    Timer timer1;
    private BottomSheetBehavior bottomSheetBehaviorwd;
    private BottomSheetBehavior bottomSheetBehaviordsh;
    private MySeekBarwd mSeekBar1;
    int x ;
    int i;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xnty_qnq);
        unbinder = ButterKnife.bind(this);
        mSeekBar1 = (MySeekBarwd) findViewById(R.id.beautySeekBar1);
        mSeekBar1.setOnSeekBarChangeListener(this);
        mSeekBar1.setProgressDrawable(getResources().getDrawable(R.drawable.bg_seekbar_progress_drawable1));
        initanim();
        timerun();
        initTimer();

            initstar1();
            i=31;
        bottomSheetBehaviorwd= BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayoutznwd));
        bottomSheetBehaviordsh= BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayoutdsh));
        anim1 = AnimationUtils.loadAnimation(this,R.anim.rotate_qnqyw);
        imageView1.startAnimation(anim1);
        imageView2.startAnimation(anim1);
        imageView3.startAnimation(anim1);
        anim3 = AnimationUtils.loadAnimation(this,R.anim.rotate_qnqyw3);
        imageViewpb.setTag("close");
        imageViewgl.setTag("close");
        imageViewjs.setTag("close");
        imageViewqx.setImageResource(R.mipmap.kt_qxn);
        imageViewqd.setImageResource(R.mipmap.kt_qdn);
        textViewwd.setTextColor(getResources().getColor(R.color.color_yellow_yuan));
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
                    timer1.cancel();
                    handler.removeCallbacks(runnable);
                    flag=1;
                }else {
                    imageViewkg.setImageResource(R.mipmap.qnq_kgk);
                    imageView1.startAnimation(anim1);
                    imageView2.startAnimation(anim1);
                    imageView3.startAnimation(anim1);
                    imageViewfs.startAnimation(rotate);
                    initstar1();
                    timerun();
                    flag=0;
                }
            }
        });
    }
    private void  initstar1(){//温度提升
        timer1 = new Timer();
        task1 = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {   // UI thread
                    @Override
                    public void run() {
                        recLen1++;
                        textView18.setText(""+recLen1);
                        if (recLen1 > x+i) {
                            timer1.cancel();

                        }
                    }
                });
            }
        };
        timer1.schedule(task1, 1000, 1000);    // timeTask
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

        @OnClick({R.id.iv_qnq_pb,R.id.iv_qnq_gl,R.id.iv_qnq_js,R.id.iv_ktn_qd,R.id.iv_ktn_qx,R.id.iv_qnq_ds,R.id.iv_qnq_fh
        ,R.id.iv_zncz_qd ,R.id.iv_zncz_qx,R.id.tv_qnq_32
        })
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
//                String sml;
//                String eml;
//                if (0<=sm&&sm<10){
//                 sml = "0"+sm ;
//                }else {
//                    sml = ""+sm ;
//                }
//                if (0<=em&&em<10){
//                    eml = "0"+em ;
//                }else {
//                    eml = ""+em ;
//                }
//
//
//                textViewds.setText(sh + ":" +sml + "-" + eh + ":" + eml);
                if (((eh*60+em)-(sh*60+sm))>0){
                    if (sm<10&&em<10){
                        textViewds.setText(sh+":0"+sm+"-"+eh+":0"+em);
                    }else if (sm>=10&&em<10){
                        textViewds.setText(sh+":"+sm+"-"+eh+":0"+em);
                    }else if (sm<10&&em>=10){
                        textViewds.setText(sh+":0"+sm+"-"+eh+":"+em);
                    }else if (sm>=10&&em>=10){
                        textViewds.setText(sh+":"+sm+"-"+eh+":"+em);
                    }

                }else {
                    Toast.makeText(this ,"结束时间需要大于开始时间", Toast.LENGTH_SHORT).show();
                }
                bottomSheetBehaviordsh.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_ktn_qx:
                bottomSheetBehaviordsh.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_qnq_fh:
                finish();
                break;
            case R.id.iv_zncz_qx:
                bottomSheetBehaviorwd.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_zncz_qd:
                textView32.setText(String.valueOf(x+5));
                timer1.cancel();

                    if ((x+5)>recLen1) {
                        i = 4;
                        initstar1();
                    }
                bottomSheetBehaviorwd.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.tv_qnq_32:
                if (bottomSheetBehaviorwd.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);按键可在隐藏
                } else if (bottomSheetBehaviorwd.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehaviorwd.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehaviorwd.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                task1.cancel();
                break;
        }

        }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        x=  seekBar.getProgress();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
        handler.removeCallbacks(runnable);
        task1.cancel();
    }

}
