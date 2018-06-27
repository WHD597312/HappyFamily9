package com.xr.happyFamily.jia.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.xr.happyFamily.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ShareDeviceActivity extends AppCompatActivity {

    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_device);
        unbinder=ButterKnife.bind(this);
    }
    @OnClick({R.id.image_more})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.image_more:
                break;
        }
    }
}
