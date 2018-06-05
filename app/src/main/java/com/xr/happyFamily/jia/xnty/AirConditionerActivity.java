package com.xr.happyFamily.jia.xnty;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.xr.happyFamily.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AirConditionerActivity extends AppCompatActivity {
    Unbinder unbinder;
    Animation anim;
    @BindView(R.id.tm_ks1)
    Timepicker timepicker1;
    @BindView(R.id.tm_ks2)
    Timepicker timepicker2;
    @BindView(R.id.tm_js1)
    Timepicker timepicker3;
    @BindView(R.id.tm_js2)
    Timepicker timepicker4;
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
    @BindView(R.id.kt_dd18)
    ImageView image30;
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xnty_kt);
        unbinder = ButterKnife.bind(this);
        initTimer();
        initAnima();
    }
    private  void initAnima(){
        anim = AnimationUtils.loadAnimation(this, R.anim.rotate_znczs);

        image1.startAnimation(anim);
        image2.startAnimation(anim);
        image3.startAnimation(anim);
        image4.startAnimation(anim);
        image5.startAnimation(anim);
        image6.startAnimation(anim);
        image7.startAnimation(anim);
        image8.startAnimation(anim);
        image9.startAnimation(anim);
        image10.startAnimation(anim);
        image11.startAnimation(anim);
        image12.startAnimation(anim);
        image13.startAnimation(anim);
        image14.startAnimation(anim);
        image15.startAnimation(anim);
        image16.startAnimation(anim);
        image17.startAnimation(anim);
        image18.startAnimation(anim);
        image19.startAnimation(anim);
        image20.startAnimation(anim);
        image21.startAnimation(anim);
        image22.startAnimation(anim);
        image23.startAnimation(anim);
        image24.startAnimation(anim);
        image25.startAnimation(anim);
        image26.startAnimation(anim);
        image27.startAnimation(anim);
        image28.startAnimation(anim);
        image29.startAnimation(anim);
        image30.startAnimation(anim);

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
}
