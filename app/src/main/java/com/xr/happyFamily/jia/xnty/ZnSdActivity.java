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
import com.xr.happyFamily.jia.titleview.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ZnSdActivity extends AppCompatActivity {

    Unbinder unbinder;
    TitleView titleView;
    @BindView(R.id.sd_d)
    ImageView dimage;
    @BindView(R.id.sd_xh)
    ImageView image;
    protected void onCreate(Bundle savadInstanceState) {
        super.onCreate(savadInstanceState);
        setContentView(R.layout.activty_xnty_sd);
        unbinder = ButterKnife.bind(this);
        titleView = (TitleView) findViewById(R.id.title);
        titleView.setTitleText("智能终端");
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }


    }
    @OnClick({R.id.bt_sd_wd,R.id.bt_sd_pm25})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_sd_wd:
                startActivity(new Intent(this, ZnWdActivity.class));
                break;
            case R.id.bt_sd_pm25:
                startActivity(new Intent(this, ZnPm25Activity.class));
                break;

        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        Glide.with(dimage.getContext())
                .load(R.mipmap.sd_sd3x)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(new GlideDrawableImageViewTarget(dimage, 100));
        Glide.with(image.getContext())
                .load(R.mipmap.pm25_xh)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(new GlideDrawableImageViewTarget(image, 100));
    }
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
}
