package com.xr.happyFamily.jia.xnty;

import android.app.Dialog;

import android.content.Context;
import android.content.Intent;


import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SmartSocket extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private int requestCode;
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetBehavior bottomSheetBehavior2;
    private BottomSheetBehavior bottomSheetBehavior3;
    private BottomSheetBehavior bottomSheetBehavior4;
    private BottomSheetBehavior bottomSheetBehavior5;
    Unbinder unbinder;
    Dialog dia ;
    Animation rotate;
    MyView myView;
    @BindView(R.id.zncz_adde)
    ImageView imageView1;

    @BindView(R.id. iv_zncz_dy)
    ImageView imageViewyuan;
    @BindView(R.id. zncz_znzd)
    ImageView imageView2;
    @BindView(R.id.tv_zncz_ljsb)
    TextView textViewsb;
    @BindView(R.id.tv_zncz_add)
    TextView textViewadd;
    @BindView(R.id.tv_zncz_wd)
    TextView textViewwd;
    @BindView(R.id.tv_zncz_kg)
    TextView textViewkg;
    @BindView(R.id.tv_zncz_xz)
    TextView textViewxz;
    @BindView(R.id.tv_zncz_ds)
    TextView textViewds;
    @BindView(R.id.tv_zncz_qhsb)
    TextView textViewqh;
    @BindView(R.id.tv_zncz_36)
    TextView textView36;
    @BindView(R.id.tv_zncz_sj)
    TextView textViewsj;
    @BindView(R.id.iv_zncz_cz)
    ImageView imageViewcz;
    @BindView(R.id.tv_zncz_sdwd)
    TextView textViewsdwd;
    @BindView(R.id.tv_zncz_sdxz)
    TextView textViewsdxz;
    @BindView(R.id. iv_zncz_addwd)
    ImageView imageViewwd;
    @BindView(R.id.iv_zncz_addxz)
    ImageView imageViewxz;
    @BindView(R.id.iv_zncz_kg)
    ImageView imageViewkg;
    @BindView(R.id.iv_zncz_ds)
    ImageView imageViewds;
    @BindView(R.id.tm_ks1)
    Timepicker timepicker1;
    @BindView(R.id.tm_ks2)
    Timepicker timepicker2;
    @BindView(R.id.tm_js1)
    Timepicker timepicker3;
    @BindView(R.id.tm_js2)
    Timepicker timepicker4;
    private Context context;
     int flag = -1 ;
    Animation anim;
    Animation anim1;
    private MySeekBarsd mSeekBar2;
    private MySeekBarwd mSeekBar1;
    private MySeekBar mSeekBar3;
    private MySeekBarPm25 mSeekBar4;
    int x ;
    public void onCreate( Bundle savedInstanceState ) {
     super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_xnty_zncz);
        unbinder = ButterKnife.bind(this);
        mSeekBar1 = (MySeekBarwd) findViewById(R.id.beautySeekBar1);
        mSeekBar1.setOnSeekBarChangeListener(this);
        mSeekBar2 = (MySeekBarsd) findViewById(R.id.beautySeekBar2);
        mSeekBar2.setOnSeekBarChangeListener(this);
        mSeekBar3 = (MySeekBar) findViewById(R.id.beautySeekBar3);
        mSeekBar3.setOnSeekBarChangeListener(this);
        mSeekBar4 = (MySeekBarPm25) findViewById(R.id.beautySeekBar4);
        mSeekBar4.setOnSeekBarChangeListener(this);
        Context context = SmartSocket.this;
        //提示图片
        dia = new Dialog(context, R.style.edit_AlertDialog_style);//设置进入时跳出提示框
        dia.setContentView(R.layout.activity_zncz_dialog);
        final ImageView imageView = (ImageView) dia.findViewById(R.id.iv_dialog1);
        imageView.setBackgroundResource(R.mipmap.zncz_dialog);
        dia.show();
        dia.setCanceledOnTouchOutside(true); // 设置屏幕点击退出
        Window w = dia.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        dia.onWindowAttributesChanged(lp);
        //上拉列表
        //设定温度界面
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayoutznwd));
        //定时界面
        bottomSheetBehavior2= BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout2));
        //设定湿度
        bottomSheetBehavior3 = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout3));
        //设定限制
        bottomSheetBehavior4 = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayoutznczxz));
        //设定PM25
        bottomSheetBehavior5 = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayoutpm25));//
        if (textViewxz.getText().equals("")){
            flag=3;
        }else if (!textViewxz.getText().equals("")){
            flag=0;
        }
        initTimer();

            imageViewwd.setClickable(false);
            imageViewxz.setClickable(false);


        //开关键逻辑
        imageViewkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag==0){
                    imageViewkg.setImageResource(R.mipmap.zncz_kgg);
                    imageViewwd.setImageResource(R.mipmap.zncz_hq);
                    imageViewxz.setImageResource(R.mipmap.zncz_hq);
                    textViewwd.setText("");
                    textViewxz.setText("");
                    textViewsdwd.setTextColor(getResources().getColor(R.color.color_gray2));
                    textViewsdxz.setTextColor(getResources().getColor(R.color.color_gray2));
                    imageView1.clearAnimation();
                    imageView2.clearAnimation();
                    imageViewwd.setClickable(false);
                    imageViewxz.setClickable(false);
                    textViewkg.setTextColor(getResources().getColor(R.color.color_gray2));
                    flag=1;
                }else if (flag==1){
                    imageViewkg.setImageResource(R.mipmap.zncz_kg);
                    imageViewwd.setImageResource(R.mipmap.zncz_lv);
                    imageViewxz.setImageResource(R.mipmap.zncz_lv);
                    imageView1.startAnimation(anim);
                    imageView2.startAnimation(anim1);
                    textViewwd.setText("24℃");
                    textViewxz.setText("2000W");
                    textViewsdwd.setTextColor(getResources().getColor(R.color.color_green4));
                    textViewsdxz.setTextColor(getResources().getColor(R.color.color_green4));
                    textViewkg.setTextColor(getResources().getColor(R.color.color_green4));
                    imageViewwd.setClickable(true);
                    imageViewxz.setClickable(true);
                    flag=0;
                }else if (flag==3){
                    imageViewkg.setImageResource(R.mipmap.zncz_kgg);
                    if (textViewxz.getText().equals("")){
                        flag=4;
                    }else   if (!textViewxz.getText().equals("")){
                        flag=1;
                        imageViewwd.setImageResource(R.mipmap.zncz_hq);
                        imageViewxz.setImageResource(R.mipmap.zncz_hq);
                        textViewwd.setText("");
                        textViewxz.setText("");
                        imageView1.clearAnimation();
                        imageView2.clearAnimation();
                        imageViewwd.setClickable(false);
                        imageViewxz.setClickable(false);
                        textViewsdwd.setTextColor(getResources().getColor(R.color.color_gray2));
                        textViewsdxz.setTextColor(getResources().getColor(R.color.color_gray2));
                        textViewkg.setTextColor(getResources().getColor(R.color.color_gray2));
                    }

                }else if (flag==4) {
                    imageViewkg.setImageResource(R.mipmap.zncz_kg);
                    if (textViewxz.getText().equals("")){
                        flag=3;
                    }else   if (!textViewxz.getText().equals("")){
                        textViewsdwd.setTextColor(getResources().getColor(R.color.color_gray2));
                        textViewsdxz.setTextColor(getResources().getColor(R.color.color_gray2));
                        textViewkg.setTextColor(getResources().getColor(R.color.color_gray2));
                        imageViewwd.setImageResource(R.mipmap.zncz_hq);
                        imageViewxz.setImageResource(R.mipmap.zncz_hq);
                        textViewwd.setText("");
                        textViewxz.setText("");
                        imageView1.clearAnimation();
                        imageView2.clearAnimation();
                        imageViewwd.setClickable(false);
                        imageViewxz.setClickable(false);
                        flag=1;
                    }

                }
            }
        });
    }
    private void initTimer(){//设置定时时间
        timepicker1.setMaxValue(23);
        timepicker1.setMinValue(00);
        timepicker1.setValue(49);

        timepicker1.setNumberPickerDividerColor(timepicker1);
        timepicker2.setMaxValue(59);
        timepicker2.setMinValue(00);
        timepicker2.setValue(49);

        timepicker2.setNumberPickerDividerColor(timepicker2);
        timepicker3.setMaxValue(23);
        timepicker3.setMinValue(00);
        timepicker3.setValue(49);

        timepicker3.setNumberPickerDividerColor(timepicker3);
        timepicker4.setMaxValue(59);
        timepicker4.setMinValue(00);
        timepicker4.setValue(49);

        timepicker4.setNumberPickerDividerColor(timepicker4);

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

    @OnClick({R.id.zncz_adde, R.id.iv_zncz_fanh ,R.id.tv_zncz_qhsb,R.id.iv_zncz_ds ,R.id.iv_b2_qx,R.id.iv_b2_qd
            ,R.id. iv_zncz_addwd,R.id.iv_zncz_qd,R.id.iv_zncz_addxz ,R.id.iv_znczxz_qd,R.id.iv_cnczsd_qd,
            R.id.iv_znczxz_qx,R.id.iv_znczpm25_qd ,R.id.iv_zncz_tb})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_zncz_tb:

                startActivity(new Intent(this, Smartsj.class));
                break;
            case R.id.zncz_adde:
//                startActivity(new Intent(this, ZnczListActivity.class));//点击跳转到设备列表
                //添加设备
                startActivityForResult(new Intent(this, ZnczListActivity.class), 0);
                break;
            case R.id.iv_zncz_fanh:
                finish();
                break;
            case R.id.tv_zncz_qhsb://切换设备
                startActivityForResult(new Intent(this, ZnczListActivity.class), 0);
                break;
            case R.id.iv_zncz_ds://定时设备
                if (bottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);按键可在隐藏
                } else if (bottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.iv_b2_qx:
                bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_b2_qd:
                int sm = timepicker2.getValue();
                int sh = timepicker1.getValue();
                int em = timepicker4.getValue();
                int eh = timepicker3.getValue();
//                textViewds.setText(sh + ":" + sm + "-" + eh + ":" + em);
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
            case R.id.iv_zncz_addwd://叫出设置湿度界面
                if (textViewsdwd.getText().equals("设定湿度")) {
                    if (bottomSheetBehavior3.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);按键可在隐藏
                    } else if (bottomSheetBehavior3.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior3.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                        bottomSheetBehavior3.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }

                } else if (textViewsdwd.getText().equals("设定温度")) {
                    if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);按键可在隐藏
                    } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                } else if (textViewsdwd.getText().equals("设定PM25")) {
                    if (bottomSheetBehavior5.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);按键可在隐藏
                    } else if (bottomSheetBehavior5.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior5.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                        bottomSheetBehavior5.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }


                break;
            case R.id.iv_zncz_qd://设定温度确定
                textViewwd.setText(String.valueOf(x+5)+"℃");
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_zncz_qx://取消
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_znczpm25_qd://设定pm25确定
                textViewwd.setText(String.valueOf(x));
                bottomSheetBehavior5.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_cnczsd_qd://设定湿度确定
                textViewwd.setText(String.valueOf(x+20));
                bottomSheetBehavior3.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_zncz_addxz://叫出设置限制

                    if(bottomSheetBehavior4.getState() == BottomSheetBehavior.STATE_EXPANDED ){
                        // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);按键可在隐藏
                    } else if(bottomSheetBehavior4.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior4 .getState() == BottomSheetBehavior.STATE_COLLAPSED){
                        bottomSheetBehavior4.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }

                break;
            case R.id.iv_znczxz_qd://设置瓦数
                textViewxz.setText(String.valueOf(x+1000)+"W");
                bottomSheetBehavior4.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_znczxz_qx://设置瓦数

                bottomSheetBehavior4.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }

    }
    public void move() {
     //图片旋转



             anim =new RotateAnimation(0f, 359f,  Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 3.5f);

        anim.setDuration(5000); // 设置动画时间
        anim.setRepeatCount(-1);

//        anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        anim.setInterpolator(lin);

       anim1 =new RotateAnimation(0f, 359f,  Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, -2.4f);
        anim1.setDuration(5000); // 设置动画时间
        anim1.setRepeatCount(-1);
        LinearInterpolator lin1 = new LinearInterpolator();//设置动画匀速运动
        anim1.setInterpolator(lin1);
        imageView1.startAnimation(anim);
        imageView2.startAnimation(anim1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                imageView1.setImageResource(R.mipmap.zncz_sbkt);
                move();
                textViewqh.setText("切换设备");
                textViewsb.setText("已连接空调");
                imageViewwd.setImageResource(R.mipmap.zncz_lv);
                textViewwd.setText("24℃");
                imageViewxz.setImageResource(R.mipmap.zncz_lv);
                textViewxz.setText("2000W");
                textViewsdwd.setText("设定温度");
                textViewsdwd.setTextColor(getResources().getColor(R.color.color_green4));
                textViewsdxz.setTextColor(getResources().getColor(R.color.color_green4));
                imageViewcz.setImageResource(R.drawable.kxyuan_shape);
                textView36.setText("36℃");
                textViewsj.setText("13:00—14:00");
                textViewadd.setText("");
                imageViewwd.setClickable(true);
                imageViewxz.setClickable(true);
                break;
            case 2:
                imageView1.setImageResource(R.mipmap.zncz_kqjhb);
                move();
                textViewqh.setText("切换设备");
                textViewsb.setText("已连接空气净化器");
                imageViewwd.setImageResource(R.mipmap.zncz_lv);
                textViewwd.setText("24");
                textViewsdwd.setText("设定PM25");
                imageViewxz.setImageResource(R.mipmap.zncz_lv);
                textViewxz.setText("2000W");
                textViewsdwd.setTextColor(getResources().getColor(R.color.color_green4));
                textViewsdxz.setTextColor(getResources().getColor(R.color.color_green4));
                imageViewcz.setImageResource(R.drawable.kxyuan_shape);
                textView36.setText("100");
                textViewsj.setText("13:00—14:00");
                textViewadd.setText("");
                imageViewwd.setClickable(true);
                imageViewxz.setClickable(true);
                break;
            case 3:
                imageView1.setImageResource(R.mipmap.zncz_dnqb);
                move();
                textViewqh.setText("切换设备");
                textViewsb.setText("已连接电暖气");
                imageViewwd.setImageResource(R.mipmap.zncz_lv);
                textViewwd.setText("24℃");
                textViewsdwd.setText("设定温度");
                imageViewxz.setImageResource(R.mipmap.zncz_lv);
                textViewxz.setText("2000W");
                textViewsdwd.setTextColor(getResources().getColor(R.color.color_green4));
                textViewsdxz.setTextColor(getResources().getColor(R.color.color_green4));
                imageViewcz.setImageResource(R.drawable.kxyuan_shape);
                textView36.setText("36℃");
                textViewsj.setText("13:00—14:00");
                textViewadd.setText("");
                imageViewwd.setClickable(true);
                imageViewxz.setClickable(true);
                break;
            case 4:
                imageView1.setImageResource(R.mipmap.zncz_csqb);
                move();
                textViewqh.setText("切换设备");
                textViewsb.setText("已连接除湿器");
                imageViewwd.setImageResource(R.mipmap.zncz_lv);
                textViewwd.setText("24");
                textViewsdwd.setText("设定湿度");
                imageViewxz.setImageResource(R.mipmap.zncz_lv);
                textViewxz.setText("2000W");
                textViewsdwd.setTextColor(getResources().getColor(R.color.color_green4));
                textViewsdxz.setTextColor(getResources().getColor(R.color.color_green4));
                imageViewcz.setImageResource(R.drawable.kxyuan_shape);
                textView36.setText("40");
                textViewsj.setText("13:00—14:00");
                textViewadd.setText("");
                imageViewwd.setClickable(true);
                imageViewxz.setClickable(true);
                break;
            case 5:
                imageView1.setImageResource(R.mipmap.zncz_jsqb);
                move();
                textViewqh.setText("切换设备");
                imageViewwd.setImageResource(R.mipmap.zncz_hq);
                textViewwd.setText("");
                textViewsdwd.setText("");
                imageViewxz.setImageResource(R.mipmap.zncz_lv);
                textViewxz.setText("2000W");
                textViewsdwd.setTextColor(getResources().getColor(R.color.color_green4));
                textViewsdxz.setTextColor(getResources().getColor(R.color.color_green4));
                imageViewcz.setImageResource(R.drawable.kxyuan_shape);
                textView36.setText("30");
                textViewsj.setText("13:00—14:00");
                textViewadd.setText("");
                imageViewwd.setClickable(true);
                imageViewxz.setClickable(true);
                break;
            default:
                break;
        }
    }
}
