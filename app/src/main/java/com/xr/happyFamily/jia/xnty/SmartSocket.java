package com.xr.happyFamily.jia.xnty;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;


import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SmartSocket extends AppCompatActivity {
    Unbinder unbinder;
    Dialog dia ;
    Animation rotate;
    @BindView(R.id.zncz_adde)
    ImageView imageView1;
    @BindView(R.id. iv_zncz_dy)
    ImageView imageViewyuan;



    private Context context;
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xnty_zncz);
        unbinder = ButterKnife.bind(this);
        Context context = SmartSocket.this;
        dia = new Dialog(context, R.style.edit_AlertDialog_style);//设置进入时跳出提示框
        dia.setContentView(R.layout.activity_zncz_dialog);
        ImageView imageView = (ImageView) dia.findViewById(R.id.iv_dialog1);
        imageView.setBackgroundResource(R.mipmap.zncz_dialog);
        dia.show();

        dia.setCanceledOnTouchOutside(true); // 设置屏幕点击退出
        Window w = dia.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
                  dia.onWindowAttributesChanged(lp);
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_zncz);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        rotate.setInterpolator(lin);
        imageViewyuan.startAnimation(rotate);
    }
    @OnClick({R.id.zncz_adde, R.id.iv_zncz_fanh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zncz_adde:
                startActivity(new Intent(this, ZnczListActivity.class));//点击跳转到设备列表
                break;
            case R.id.iv_zncz_fanh:
                finish();
                break;

        }
    }
}
