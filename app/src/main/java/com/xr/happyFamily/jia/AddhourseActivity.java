package com.xr.happyFamily.jia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.titleview.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddhourseActivity extends AppCompatActivity {
    Unbinder unbinder;
    TitleView titleView;


//    String url = "http://47.98.131.11:8084/login/auth";

    int houseId;

    protected void onCreate(Bundle savadInstanceState) {
        super.onCreate(savadInstanceState);

        setContentView(R.layout.activity_home_addhourse);
        unbinder = ButterKnife.bind(this);
        titleView = (TitleView) findViewById(R.id.title_addhourse);
        titleView.setTitleText("新建家庭");
    }
}
