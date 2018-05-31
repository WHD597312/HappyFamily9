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
import android.widget.TextView;

import com.xr.happyFamily.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SmartSocket extends AppCompatActivity {
    private int requestCode;
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetBehavior bottomSheetBehavior2;
    private BottomSheetBehavior bottomSheetBehavior3;
    Unbinder unbinder;
    Dialog dia ;
    Animation rotate;
    Animation rotate1;
    Animation rotate2;
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
    public void onCreate( Bundle savedInstanceState ) {
     super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_xnty_zncz);
        unbinder = ButterKnife.bind(this);
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
//        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
        bottomSheetBehavior2= BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout2));
//        bottomSheetBehavior3 = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout3));
        if (textViewwd.getText().equals("")){
            flag=3;
        }else if (!textViewwd.getText().equals("")){
            flag=0;
        }
        initTimer();
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
                    if (textViewwd.getText().equals("")){
                        flag=4;
                    }else   if (!textViewwd.getText().equals("")){
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
                    if (textViewwd.getText().equals("")){
                        flag=3;
                    }else   if (!textViewwd.getText().equals("")){
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

    @OnClick({R.id.zncz_adde, R.id.iv_zncz_fanh ,R.id.tv_zncz_qhsb,R.id.iv_zncz_ds ,R.id.iv_b2_qx,R.id.iv_b2_qd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zncz_adde:
//                startActivity(new Intent(this, ZnczListActivity.class));//点击跳转到设备列表
                   //添加设备
                startActivityForResult(new Intent(this, ZnczListActivity.class),0);
                break;
            case R.id.iv_zncz_fanh:
                finish();
                break;
            case R.id.tv_zncz_qhsb://切换设备
                startActivityForResult(new Intent(this, ZnczListActivity.class),0);
                break;
            case R.id.iv_zncz_ds://切换设备
                if(bottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_EXPANDED ){
                    // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);按键可在隐藏
                } else if(bottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.iv_b2_qx:
                bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_b2_qd:
                int sm= timepicker2.getValue();
                int sh= timepicker1.getValue();
                int em = timepicker4.getValue();
                int eh = timepicker3.getValue();
                textViewds.setText(sh+":"+sm+"——"+eh+":"+em);
                bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_HIDDEN);
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



             anim =new RotateAnimation(0f, 359f,  Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 3.4f);

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
                textViewsdwd.setTextColor(getResources().getColor(R.color.color_green4));
                textViewsdxz.setTextColor(getResources().getColor(R.color.color_green4));
                imageViewcz.setImageResource(R.drawable.kxyuan_shape);
                textView36.setText("36℃");
                textViewsj.setText("13:00—14:00");
                textViewadd.setText("");

                break;
            case 2:
                imageView1.setImageResource(R.mipmap.zncz_kqjhb);
                move();
                textViewqh.setText("切换设备");
                textViewsb.setText("已连接空气净化器");
                textViewadd.setText("");
                break;
            case 3:
                imageView1.setImageResource(R.mipmap.zncz_dnqb);
                move();
                textViewqh.setText("切换设备");
                textViewsb.setText("已连接电暖气");
                textViewadd.setText("");
                break;
            case 4:
                imageView1.setImageResource(R.mipmap.zncz_csqb);
                move();
                textViewqh.setText("切换设备");
                textViewsb.setText("已连接除湿器");
                textViewadd.setText("");
                break;
            case 5:
                imageView1.setImageResource(R.mipmap.zncz_jsqb);
                move();
                textViewqh.setText("切换设备");
                textViewsb.setText("已连接净水器");
                textViewadd.setText("");
                break;
            default:
                break;
        }
    }
}
