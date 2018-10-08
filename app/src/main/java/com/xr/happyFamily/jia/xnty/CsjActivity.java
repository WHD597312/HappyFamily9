package com.xr.happyFamily.jia.xnty;



import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
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
import pl.droidsonroids.gif.GifDrawable;



public class CsjActivity extends AppCompatActivity implements View.OnClickListener , SeekBar.OnSeekBarChangeListener {
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetBehavior bottomSheetBehavior2;
    private BottomSheetBehavior bottomSheetBehavior3;
    private ImageView show;
    ScrollNumber scrollNumber;
    Animation rotate;
    Unbinder unbinder;
    @BindView(R.id.iv_csj_bj)
    ImageView image;
    @BindView(R.id.iv_b_1)
    ImageView image1;
    @BindView(R.id.iv_b_2)
    ImageView image2;
    @BindView(R.id.iv_b_3)
    ImageView image3;
    @BindView(R.id.ib_csj_fh)
    ImageButton ib_csj_fh;
    @BindView(R.id.tm_ks1)
    Timepicker timepicker1;
    @BindView(R.id.tm_ks2)
    Timepicker timepicker2;
    @BindView(R.id.tm_js1)
    Timepicker timepicker3;
    @BindView(R.id.tm_js2)
    Timepicker timepicker4;
    @BindView(R.id.iv_csj_fs)
    ImageView imagefs;
    @BindView(R.id.tv_csj_kg)
    TextView textViewkq;
    @BindView(R.id.tv_csj_f1)
    TextView textViewf1;
    @BindView(R.id.iv_csj_kg)
    ImageView imageViewkg;
    @BindView(R.id.tv_csj_ds)
    TextView textViewds;
    @BindView(R.id.tv_b_1)
    TextView textb1;
    @BindView(R.id.tv_b_2)
    TextView textb2;
    @BindView(R.id.tv_b_3)
    TextView textb3;
    @BindView(R.id.iv_cnczsd_qd)
    ImageView imageViewb3qd;
    @BindView(R.id.tv_csj_29)
    TextView text29;
    @BindView(R.id.tv_csj_36)
    TextView textView36;
    private MySeekBarsd mSeekBar2;
    int hour = -1;
    int minute=-1;
    int i;
    int x ;
    private int recLen = 36;
    TimerTask task;
    Timer timer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_csj);
        unbinder = ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initViews();
        initListeners();
        initTimer();
        ib_csj_fh .setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(CsjActivity.this,R.color.white)));
        mSeekBar2 = (MySeekBarsd) findViewById(R.id.beautySeekBar2);
        mSeekBar2.setOnSeekBarChangeListener(this);
         rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        /*imagefs.setAnimation(rotate);
        imagefs.startAnimation(rotate);*/
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        rotate.setInterpolator(lin);
        imagefs.startAnimation(rotate);

        imageViewkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag==0){


                    imageViewkg .setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(CsjActivity.this,R.color.color_csjg)));
                    imagefs .setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(CsjActivity.this,R.color.color_csjg)));
                    imagefs.clearAnimation();
                    imagefs.setClickable(false);
                    timer.cancel();
                    textViewkq.setText("关机状态");
                    flag=1;
                }else {
                    imageViewkg .setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(CsjActivity.this,R.color.white)));
                    imagefs .setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(CsjActivity.this,R.color.white)));
                    imagefs.startAnimation(rotate);
                    imagefs.setClickable(true);
                    initstar();
                    textViewkq.setText("开机状态");
                    flag=0;
                }
            }
        });
        if ((x+29)<36){
            i=30;
            initstar();
        }
        try {
            gifDrawable=new GifDrawable(getResources(),R.mipmap.man2x);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (gifDrawable!=null){
            gifDrawable.setLoopCount(10000);
            gifDrawable.start();
            image.setImageDrawable(gifDrawable);
        }

    }


    private void  initstar(){//温度提升与温度下降
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {   // UI thread
                    @Override
                    public void run() {
                        recLen--;
                        textView36.setText(""+recLen);
                        if (recLen < (x+i)) {
                            timer.cancel();
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);    // timeTask
    }


    private void initTimer(){//设置定时时间
        timepicker1.setMaxValue(23);
       timepicker1.setMinValue(00);
        timepicker1.setValue(49);
        //timepicker1.setBackgroundColor(Color.LTGRAY);
       timepicker1.setNumberPickerDividerColor(timepicker1);
        timepicker2.setMaxValue(59);
        timepicker2.setMinValue(00);
        timepicker2.setValue(49);
        //timepicker2.setBackgroundColor(Color.LTGRAY);
        timepicker2.setNumberPickerDividerColor(timepicker2);
        timepicker3.setMaxValue(23);
        timepicker3.setMinValue(00);
        timepicker3.setValue(49);
        //timepicker3.setBackgroundColor(Color.LTGRAY);
       timepicker3.setNumberPickerDividerColor(timepicker3);
        timepicker4.setMaxValue(59);
        timepicker4.setMinValue(00);
        timepicker4.setValue(49);
        //timepicker4.setBackgroundColor(Color.LTGRAY);
        timepicker4.setNumberPickerDividerColor(timepicker4);

    }

    private void initViews() {


        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
        bottomSheetBehavior2= BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout2));
        bottomSheetBehavior3 = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout3));
        show = (ImageView) findViewById(R.id.iv_csj_fs);


    }
    private void initListeners() {


        show.setOnClickListener(this);


        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {



                // Check Logs to see how bottom sheets behaves
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.e("Bottom Sheet Behaviour", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.e("Bottom Sheet Behaviour", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.e("Bottom Sheet Behaviour", "STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.e("Bottom Sheet Behaviour", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.e("Bottom Sheet Behaviour", "STATE_SETTLING");
                        break;
                }
            }


            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });


    }
    GifDrawable gifDrawable;
    private int position=-1;
    private int flag=0;
    @OnClick({R.id.ib_csj_fh, R.id.iv_b_qx,R.id.iv_b_qd,R.id.iv_b_2,R.id.iv_b_3,R.id.iv_b_1,R.id.iv_csj_ds
            , R.id.iv_b2_qx,R.id.tv_csj_29, R.id.iv_b3_qx,R.id.iv_b2_qd,R.id.iv_cnczsd_qd
    })

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_csj_fs://叫出风速页面
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED ){
                   // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);按键可在隐藏
                } else if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.tv_csj_29://叫出设置湿度界面
                if(bottomSheetBehavior3.getState() == BottomSheetBehavior.STATE_EXPANDED ){
                    // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);按键可在隐藏
                } else if(bottomSheetBehavior3.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior3.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    bottomSheetBehavior3.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.ib_csj_fh:
              finish();
                break;

            case R.id.iv_b_qx:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_b2_qx:
                bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_b3_qx:
                bottomSheetBehavior3.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_b2_qd:
                int sm= timepicker2.getValue();
                int sh= timepicker1.getValue();
                int em = timepicker4.getValue();
                int eh = timepicker3.getValue();
               /* if (eh>sh&&em>sm){//倒计时效果
                     hour = eh-sh;
                     minute = em-sm;
                }else if (eh>sh &&em<sm)
                    {
                    hour = eh-sh-1;
                    minute = 60-sm+em;
                }else if (eh>sh &&em==sm)
                {
                    hour = eh-sh;
                    minute = 0;
                }
                else if (eh==sh&&em>=sm){
                    hour=0;
                    minute=em-sm;
                }else if (eh==sh&&em<sm){
                    hour=23;
                    minute=60-sm+em;
                }else if (eh<sh&&em<sm){
                    hour=23-sh+eh;
                    minute=60-sm+em;
                }else if (eh<sh&&em>sm){
                    hour=24-sh+eh;
                    minute=em-sm;
                }else if (eh<sh&&em==sm){
                    hour=24-sh+eh;
                    minute=0;
                }
                textViewkq.setText(hour+"小时"+minute+"分钟后开启");*/
//               textViewds.setText(sh+":"+sm+"——"+eh+":"+em);
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
                bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_b_qd:

                if (position==0){
                  /*  Glide.with(image.getContext())
                            .load(R.mipmap.man2x)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .centerCrop()
                            .into(new GlideDrawableImageViewTarget(image, 1));*/

                    try {
                        gifDrawable=new GifDrawable(getResources(),R.mipmap.man2x);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if (gifDrawable!=null){
                        gifDrawable.setLoopCount(10000);
                        gifDrawable.start();
                        image.setImageDrawable(gifDrawable);
                    }
                    rotate.setDuration(2000);//设置动画持续周期
                    rotate.setRepeatCount(-1);//设置重复次数
                    textViewf1.setText("风速1级");
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }else if (position==1){
             /*       Glide.with(image.getContext())
                            .load(R.mipmap.zhong2x)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .centerCrop()
                            .into(new GlideDrawableImageViewTarget(image, 1));*/

                    try {
                        gifDrawable=new GifDrawable(getResources(),R.mipmap.man2x);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if (gifDrawable!=null){
                        gifDrawable.setLoopCount(10000);
                        gifDrawable.setSpeed(3.0f);
                        gifDrawable.start();
                        image.setImageDrawable(gifDrawable);
                    }
                    rotate.setDuration(1000);//设置动画持续周期
                    rotate.setRepeatCount(-1);//设置重复次数
                    textViewf1.setText("风速2级");
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }else if (position==2){
              /*      Glide.with(CsjActivity.this)
                            .load(R.mipmap.kuai3x)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .centerCrop()
                            .into(new GlideDrawableImageViewTarget(image, 1));*/

                    try {
                        gifDrawable=new GifDrawable(getResources(),R.mipmap.man2x);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if (gifDrawable!=null){
                        gifDrawable.setLoopCount(10000);
                        gifDrawable.setSpeed(7.0f);
                        gifDrawable.start();
                        image.setImageDrawable(gifDrawable);
                    }
                    rotate.setDuration(500);//设置动画持续周期
                    rotate.setRepeatCount(-1);//设置重复次数
                    textViewf1.setText("风速3级");
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                break;
            case R.id.iv_b_2:
                position=1;
                image2.setImageResource(R.mipmap.csj_dj);
                image1.setImageResource(R.mipmap.csj_djnull);
                image3.setImageResource(R.mipmap.csj_djnull);

                break;
            case R.id.iv_b_1:
                position=0;
                image1.setImageResource(R.mipmap.csj_dj);
                image2.setImageResource(R.mipmap.csj_djnull);
                image3.setImageResource(R.mipmap.csj_djnull);

                break;
            case R.id.iv_b_3:
                position=2;
                image3.setImageResource(R.mipmap.csj_dj);
                image1.setImageResource(R.mipmap.csj_djnull);
                image2.setImageResource(R.mipmap.csj_djnull);

                break;
            case R.id.iv_csj_ds://定时显示隐藏
                if(bottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_EXPANDED ){
                    // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);按键可在隐藏
                } else if(bottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.tv_b_2:
                position=1;
                image2.setImageResource(R.mipmap.csj_dj);
                image1.setImageResource(R.mipmap.csj_djnull);
                image3.setImageResource(R.mipmap.csj_djnull);

                break;
            case R.id.tv_b_1:
                position=0;
                image1.setImageResource(R.mipmap.csj_dj);
                image2.setImageResource(R.mipmap.csj_djnull);
                image3.setImageResource(R.mipmap.csj_djnull);

                break;
            case R.id.tv_b_3:
                position=2;
                image3.setImageResource(R.mipmap.csj_dj);
                image1.setImageResource(R.mipmap.csj_djnull);
                image2.setImageResource(R.mipmap.csj_djnull);

                break;
            case R.id.iv_cnczsd_qd:
                text29.setText(String.valueOf(x+20));
                timer.cancel();
                if ((x+20)<36){
                    i=21;
                    initstar();
                }
                bottomSheetBehavior3.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;

        }
    }


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
    protected void onStart() {
        super.onStart();



    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
        task.cancel();
    }


}
