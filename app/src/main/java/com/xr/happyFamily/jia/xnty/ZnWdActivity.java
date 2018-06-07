package com.xr.happyFamily.jia.xnty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MenuActivity;
import com.xr.happyFamily.jia.ZixingAddActivity;
import com.xr.happyFamily.jia.titleview.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifDrawable;

public class ZnWdActivity extends AppCompatActivity {
    Unbinder unbinder;
    GifDrawable gifDrawable1;
    GifDrawable gifDrawable2;
    GifDrawable gifDrawable3;
    TitleView titleView;
    @BindView(R.id.wd_d)
    ImageView dimage;
    @BindView(R.id.wd_xh)
    ImageView image;
    @BindView(R.id.iv_wd_dh)
    ImageView image1;
    protected void onCreate(Bundle savadInstanceState) {
        super.onCreate(savadInstanceState);
        setContentView(R.layout.activty_xnty_wd);
        unbinder = ButterKnife.bind(this);
        titleView = (TitleView) findViewById(R.id.title);
        titleView.setTitleText("智能终端");
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


    }
    @OnClick({R.id.bt_wd_sd,R.id.bt_wd_pm25})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_wd_sd:
                startActivity(new Intent(this, ZnSdActivity.class));
                break;
            case R.id.bt_wd_pm25:
                startActivity(new Intent(this, ZnPm25Activity.class));
                break;

        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        try {//加载动画
            gifDrawable1=new GifDrawable(getResources(),R.mipmap.wd_fs);
            gifDrawable2=new GifDrawable(getResources(),R.mipmap.pm25_xh);
            gifDrawable3=new GifDrawable(getResources(),R.mipmap.wd_kt);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (gifDrawable1!=null && gifDrawable2!=null&&gifDrawable3!=null){
            gifDrawable1.start();
            gifDrawable2.start();
            gifDrawable3.setSpeed(5.0f);
            gifDrawable3.start();
            dimage.setImageDrawable(gifDrawable1);
            image.setImageDrawable(gifDrawable2);
            image1.setImageDrawable(gifDrawable3);
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