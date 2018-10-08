package com.xr.happyFamily.jia.xnty;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.together.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AirConditionerActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    Unbinder unbinder;
    Animation anim;
    Animation anim1;
    Animation anim2;
    Animation anim3;
    int x;
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
    @BindView(R.id.iv_kt_fh)
    ImageView imageViewfh;
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
    @BindView(R.id.iv_kt_ct)
    ImageView imageViewct;
    @BindView(R.id.iv_zncz_qx)
    ImageView imageViewqx;
    @BindView(R.id.iv_zncz_qd)
    ImageView imageViewqd;
    @BindView(R.id.tv_zncz_wd)
    TextView textViewwd;
    @BindView(R.id.fallingView)
    FallingView fallingView;
    @BindView(R.id.bottomSheetLayoutznwd)
    RelativeLayout relativeLayoutwd;

    private BottomSheetBehavior bottomSheetBehaviordsh;
    private BottomSheetBehavior bottomSheetBehaviordsl;
    private BottomSheetBehavior bottomSheetBehaviorwd;

    private int recLen = 36;
    private int recLen1 = 10;
    TimerTask task;
    TimerTask task1;
    Timer timer;
    Timer timer1;
    int i;
    int b;
    private MySeekBarwd mSeekBar1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xnty_kt);

        unbinder = ButterKnife.bind(this);
        mSeekBar1 = (MySeekBarwd) findViewById(R.id.beautySeekBar1);
        mSeekBar1.setOnSeekBarChangeListener(this);

//        mSeekBar1.setBgResId(R.mipmap.wd_bzl);
        mSeekBar1.setmBackgroundBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.wd_bzl));
        mSeekBar1.invalidate();
        imageViewqx.setImageResource(R.mipmap.kt_qx);
        imageViewqd.setImageResource(R.mipmap.kt_qd);
        textViewwd.setTextColor(getResources().getColor(R.color.color_blue_yuan));
        mSeekBar1.setProgressDrawable(getResources().getDrawable(R.drawable.bg_seekbar_progress_drawable2));
        imageViewzl.setTag("open");
        imageViewkg.setTag("open");
        imageViewzd.setTag("close");
        imageViewsm.setTag("close");
        imageViewsf.setTag("close");
        initTimer();
        initview();
        anim3 = AnimationUtils.loadAnimation(this, R.anim.animdong);
        imageViewct.startAnimation(anim3);
        bottomSheetBehaviordsh = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayoutdsh));
        bottomSheetBehaviordsl = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayoutdslan));
        bottomSheetBehaviorwd = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayoutznwd));

        if ((x + 5) < 36) {
            i = 26;
            initstar();
        }

        imageViewzl.setClickable(false);
        imageViewzr.setClickable(true);

        //自动按钮实现
        imageViewzd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("open".equals(imageViewkg.getTag())) {
                    if ("close".equals(imageViewzd.getTag())) {
                        imageViewzd.setTag("open");
                        imageViewsm.setTag("close");
                        imageViewsf.setTag("close");
                        imageViewzd.setClickable(false);
                        imageViewsm.setClickable(true);
                        imageViewsf.setClickable(true);
                        fallingView.removAll();
                        initview();
                    }
                }
            }
        });
        //睡眠方法实现
        imageViewsm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("open".equals(imageViewkg.getTag())) {
                    if ("close".equals(imageViewsm.getTag())) {
                        imageViewsm.setTag("open");
                        imageViewzd.setTag("close");
                        imageViewsf.setTag("close");
                        imageViewsm.setClickable(false);
                        imageViewzd.setClickable(true);
                        imageViewsf.setClickable(true);
                        fallingView.removAll();
                        initview();
                    }
                }
            }
        });
        //送风方法实现
        imageViewsf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("open".equals(imageViewkg.getTag())) {
                    if ("close".equals(imageViewsf.getTag())) {
                        imageViewsf.setTag("open");
                        imageViewzd.setTag("close");
                        imageViewsm.setTag("close");
                        imageViewsf.setClickable(false);
                        imageViewzd.setClickable(true);
                        imageViewsm.setClickable(true);
                        fallingView.removAll();
                        initview();
                    }
                }
            }
        });

        //开关实现代码
        imageViewkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("open".equals(imageViewkg.getTag())) {
                    imageViewkg.setTag("close");
                    imageViewkg.setImageResource(R.mipmap.kt_kgg);
                    imageViewct.clearAnimation();
                    fallingView.removAll();
                } else {
                    imageViewkg.setTag("open");
                    if ("open".equals(imageViewzl.getTag())) {
                        imageViewkg.setImageResource(R.mipmap.kt_kgl);
                    } else {
                        imageViewkg.setImageResource(R.mipmap.kt_kgn);
//                        imageViewkg.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(AirConditionerActivity. this,R.color.color_yellown)));
                    }
                    imageViewct.startAnimation(anim3);
                    initview();
                }
            }
        });

    }

    int f = 5;

    private void initview() {

        FallObject.Builder builder1;
        FallObject.Builder builder2;
        FallObject.Builder builder3;
        FallObject.Builder builder4;
        FallObject.Builder builder5;
        FallObject fallObject1;
        FallObject fallObject2;
        FallObject fallObject3;
        FallObject fallObject4;
        FallObject fallObject5;

        if ("open".equals(imageViewkg.getTag())) {

            if ("open".equals(imageViewzl.getTag())) {
                builder1 = new FallObject.Builder(getResources().getDrawable(R.mipmap.kt_xh1));
                builder2 = new FallObject.Builder(getResources().getDrawable(R.mipmap.kt_xh2));
                builder3 = new FallObject.Builder(getResources().getDrawable(R.mipmap.kt_xh3));
                builder4 = new FallObject.Builder(getResources().getDrawable(R.mipmap.kt_xh4));
                builder5 = new FallObject.Builder(getResources().getDrawable(R.mipmap.kt_xh5));

            } else {
                builder1 = new FallObject.Builder(getResources().getDrawable(R.mipmap.kt_xh1n));
                builder2 = new FallObject.Builder(getResources().getDrawable(R.mipmap.kt_xh2n));
                builder3 = new FallObject.Builder(getResources().getDrawable(R.mipmap.kt_xh3n));
                builder4 = new FallObject.Builder(getResources().getDrawable(R.mipmap.kt_xh4n));
                builder5 = new FallObject.Builder(getResources().getDrawable(R.mipmap.kt_xh5n));

            }
            if ("open".equals(imageViewzd.getTag())) {
                if ("open".equals(imageViewzl.getTag())) {
                    imageViewzd.setImageResource(R.mipmap.kt_zdk);
                } else {
                    imageViewzd.setImageResource(R.mipmap.kt_zdn);
                }
                imageViewsm.setImageResource(R.mipmap.kt_smg);
                imageViewsf.setImageResource(R.mipmap.kt_sfg);
                imageViewsfbz.setImageResource(R.mipmap.kt_sfg);
                f = 5;
            } else if ("open".equals(imageViewsm.getTag())) {
                if ("open".equals(imageViewzl.getTag())) {
                    imageViewsm.setImageResource(R.mipmap.kt_smk);
                } else {
                    imageViewsm.setImageResource(R.mipmap.kt_smn);
                }
                imageViewzd.setImageResource(R.mipmap.kt_zdg);
                imageViewsf.setImageResource(R.mipmap.kt_sfg);
                imageViewsfbz.setImageResource(R.mipmap.kt_sfg);
                f = 3;

            } else if ("open".equals(imageViewsf.getTag())) {

                if ("open".equals(imageViewzl.getTag())) {
                    imageViewsf.setImageResource(R.mipmap.kt_sfk);
                    imageViewsfbz.setImageResource(R.mipmap.kt_sfk);
                } else {
                    imageViewsf.setImageResource(R.mipmap.kt_sfn);
                    imageViewsfbz.setImageResource(R.mipmap.kt_sfn);
                }
                imageViewzd.setImageResource(R.mipmap.kt_zdg);
                imageViewsm.setImageResource(R.mipmap.kt_smg);
                f = 8;
            }
            fallObject1 = builder1
                    .setSpeed(f, true)
//                .setSize(30,30,false)
                    .build();

            fallObject2 = builder2
                    .setSpeed(f, true)
//                .setSize(30,30,false)
                    .build();

            fallObject3 = builder3
                    .setSpeed(f, true)
//                .setSize(30,30,false)
                    .build();

            fallObject4 = builder4
                    .setSpeed(f, true)
//                .setSize(30,30,false)
                    .build();

            fallObject5 = builder5
                    .setSpeed(f, true)
//                .setSize(30,30,false)
                    .build();
            Log.e("fsize", "initview: -->" + f);
            List<FallObject> list = new ArrayList<>();
            list.add(fallObject1);
            list.add(fallObject2);
            list.add(fallObject3);
            list.add(fallObject4);
            list.add(fallObject5);
            fallingView.addFallObjects(list, 11);
        }
    }

    private void initstar() {//温度提升与温度下降
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {   // UI thread
                    @Override
                    public void run() {
                        recLen--;
                        textView33.setText("" + recLen + "℃");
                        if (recLen < x + i) {
                            timer.cancel();

                        }
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);    // timeTask
    }

    private void initstar1() {
        timer1 = new Timer();
        task1 = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {   // UI thread
                    @Override
                    public void run() {
                        recLen1++;
                        textView33.setText("" + recLen1 + "℃");
                        if (recLen1 > x + b) {
                            timer1.cancel();

                        }
                    }
                });
            }
        };
        timer1.schedule(task1, 1000, 1000);    // timeTask
    }


    private void initTimer() {//设置定时时间
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

    @OnClick({R.id.iv_kt_ds, R.id.iv_ktl_qx, R.id.iv_kt_zr, R.id.iv_kt_zl, R.id.iv_ktn_qx,
            R.id.iv_ktl_qd, R.id.iv_ktn_qd, R.id.iv_kt_fh, R.id.tv_kt_23, R.id.iv_zncz_qd, R.id.iv_zncz_qx})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_kt_ds://定时设备
                if (flag == 0) {
                    if (bottomSheetBehaviordsl.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);按键可在隐藏
                    } else if (bottomSheetBehaviordsl.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehaviordsl.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                        bottomSheetBehaviordsl.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                } else if (flag == 1) {
                    if (bottomSheetBehaviordsh.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);按键可在隐藏
                    } else if (bottomSheetBehaviordsh.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehaviordsh.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                        bottomSheetBehaviordsh.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }
                break;
            case R.id.tv_kt_23:
                if (bottomSheetBehaviorwd.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);按键可在隐藏
                } else if (bottomSheetBehaviorwd.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehaviorwd.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehaviorwd.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                if (flag == 0) {
                    task.cancel();
                } else if (flag == 1) {
                    task1.cancel();
                }

                break;
            case R.id.iv_zncz_qx:
                bottomSheetBehaviorwd.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_zncz_qd:
                textView23.setText(String.valueOf(x + 5) + "℃");
                timer.cancel();
                if (flag == 0) {
                    if ("open".equals(imageViewzr.getTag())) {
                        task1.cancel();//增加温度关闭
                    }
                    if ((x + 5) < 36) {
                        i = 6;

                        initstar();
                    }
                } else if (flag == 1) {
                    if ("open".equals(imageViewzl.getTag())) {
                        task.cancel();
                    }
                    if ((x + 5) > 10) {
                        b = 4;
                        initstar1();
                    }
                }

                bottomSheetBehaviorwd.setState(BottomSheetBehavior.STATE_HIDDEN);
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
//                String smh;
//                String emh;
                if (((eh * 60 + em) - (sh * 60 + sm)) > 0) {
                    if (sm < 10 && em < 10) {
                        textViewds.setText(sh + ":0" + sm + "-" + eh + ":0" + em);
                    } else if (sm >= 10 && em < 10) {
                        textViewds.setText(sh + ":" + sm + "-" + eh + ":0" + em);
                    } else if (sm < 10 && em >= 10) {
                        textViewds.setText(sh + ":0" + sm + "-" + eh + ":" + em);
                    } else if (sm >= 10 && em >= 10) {
                        textViewds.setText(sh + ":" + sm + "-" + eh + ":" + em);
                    }

                } else {
                    Toast.makeText(this, "结束时间需要大于开始时间", Toast.LENGTH_SHORT).show();
                }

                bottomSheetBehaviordsh.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_ktl_qd://分清1和L
                int sm1 = timepickerl2.getValue();
                int sh1 = timepickerl1.getValue();
                int em1 = timepickerl4.getValue();
                int eh1 = timepickerl3.getValue();

                if (((eh1 * 60 + em1) - (sh1 * 60 + sm1)) > 0) {
                    if (sm1 < 10 && em1 < 10) {
                        textViewds.setText(sh1 + ":0" + sm1 + "-" + eh1 + ":0" + em1);
                    } else if (sm1 >= 10 && em1 < 10) {
                        textViewds.setText(sh1 + ":" + sm1 + "-" + eh1 + ":0" + em1);
                    } else if (sm1 < 10 && em1 >= 10) {
                        textViewds.setText(sh1 + ":0" + sm1 + "-" + eh1 + ":" + em1);
                    } else if (sm1 >= 10 && em1 >= 10) {
                        textViewds.setText(sh1 + ":" + sm1 + "-" + eh1 + ":" + em1);
                    }

                } else {
                    Toast.makeText(this, "结束时间需要大于开始时间", Toast.LENGTH_SHORT).show();
                }
                bottomSheetBehaviordsl.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_kt_zr://制热设备
                if ("open".equals(imageViewkg.getTag())) {
                if (NoFastClickUtils.isFastClick()) {
                    if (task != null) {
                        task.cancel();
                    }
                    if (task1 != null) {
                        task1.cancel();
                    }
                        imageViewzr.setTag("open");
                        imageViewzl.setTag("close");
                        flag = 1;
                        imageViewzr.setImageResource(R.mipmap.kt_zrn);
                        imageViewzl.setImageResource(R.mipmap.kt_zlg);
                        textView33.setTextColor(getResources().getColor(R.color.color_yellow_yuan));
                        textView23.setTextColor(getResources().getColor(R.color.color_yellow_yuan));
                        relativeLayoutbg.setBackground(getResources().getDrawable(R.color.color_yellow1));
                        imageViewkg.setImageResource(R.mipmap.kt_kgn);
                        imageViewct.setImageResource(R.mipmap.kt_ctn);
                        imageViewkt.setImageResource(R.mipmap.kt_ktn);
                        imageViewds.setImageResource(R.mipmap.kt_dsn);
                        imageViewsf.setImageResource(R.mipmap.kt_sfg);
                        imageViewzd.setImageResource(R.mipmap.kt_zdg);
                        imageViewsm.setImageResource(R.mipmap.kt_smg);
                        imageViewsfbz.setImageResource(R.mipmap.kt_sfg);
                        textView33.setText("10℃");
                        textView23.setText("25℃");
                        recLen1 = 10;
                        x = 0;

                        if ((x + 5) < 25) {
                            b = 24;
                            initstar1();
                        }
                        mSeekBar1.setProgress(0);
                        imageViewqx.setImageResource(R.mipmap.kt_qxn);
                        imageViewqd.setImageResource(R.mipmap.kt_qdn);
                        textViewwd.setTextColor(getResources().getColor(R.color.color_yellow_yuan));
                        mSeekBar1.setProgressDrawable(getResources().getDrawable(R.drawable.bg_seekbar_progress_drawable1));
                        mSeekBar1.setmBackgroundBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.wd_bzn));
                        mSeekBar1.invalidate();
                        imageViewzl.setClickable(true);
                        imageViewzr.setClickable(false);
                        fallingView.removAll();
                        initview();

                    }
                }
                break;
            case R.id.iv_kt_zl://开启制冷设备
                if ("open".equals(imageViewkg.getTag())) {
                if (NoFastClickUtils.isFastClick()) {
//
                    if (task != null) {
                        task.cancel();
                    }
                    if (task1 != null) {
                        task1.cancel();
                    }
                        imageViewzl.setTag("open");
                        imageViewzr.setImageResource(R.mipmap.kt_zrg);
                        imageViewzl.setImageResource(R.mipmap.kt_zlk);
                        imageViewds.setImageResource(R.mipmap.kt_dsk);
                        textView33.setTextColor(getResources().getColor(R.color.color_blue_yuan));
                        textView23.setTextColor(getResources().getColor(R.color.color_blue_yuan));
                        relativeLayoutbg.setBackground(getResources().getDrawable(R.color.color_blue1));
                        imageViewkg.setImageResource(R.mipmap.kt_kgl);
                        imageViewct.setImageResource(R.mipmap.kt_ct);
                        imageViewkt.setImageResource(R.mipmap.kt_kt);
                        imageViewsf.setImageResource(R.mipmap.kt_sfg);
                        imageViewzd.setImageResource(R.mipmap.kt_zdg);
                        imageViewsm.setImageResource(R.mipmap.kt_smg);
                        imageViewsfbz.setImageResource(R.mipmap.kt_sfg);

                        textView33.setText("36℃");
                        textView23.setText("25℃");
                        recLen = 36;
                        x = 0;
                        if ((x + 5) < 36) {
                            i = 26;
                            initstar();
                        }

                        imageViewzr.setTag("close");
                        mSeekBar1.setProgress(0);
                        imageViewqx.setImageResource(R.mipmap.kt_qx);
                        imageViewqd.setImageResource(R.mipmap.kt_qd);
                        textViewwd.setTextColor(getResources().getColor(R.color.color_blue_yuan));
                        mSeekBar1.setProgressDrawable(getResources().getDrawable(R.drawable.bg_seekbar_progress_drawable2));
                        mSeekBar1.setmBackgroundBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.wd_bzl));
                        mSeekBar1.invalidate();
                        imageViewzl.setClickable(false);
                        imageViewzr.setClickable(true);
                        fallingView.removAll();
                        initview();


                    }
                }

                break;
            case R.id.iv_kt_fh:
                finish();
                break;
        }


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        x = seekBar.getProgress();
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
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (task != null) {
            task.cancel();
        }
        if (task1 != null) {
            task1.cancel();
        }


    }

}

