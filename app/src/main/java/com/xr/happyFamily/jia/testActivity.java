package com.xr.happyFamily.jia;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xr.happyFamily.R;

public class testActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testac);
        ImageView imageView = (ImageView) findViewById(R.id.iv_test_test);
        String imageUrl = "http://p9zaf8j1m.bkt.clouddn.com/detailDescribe/%E5%8F%96%E6%9A%96%E5%99%A8_02.png";
        Glide.with(this).load(imageUrl).into(imageView);
    }
}
