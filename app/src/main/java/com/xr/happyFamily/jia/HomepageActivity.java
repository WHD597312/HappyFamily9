package com.xr.happyFamily.jia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xr.happyFamily.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomepageActivity extends AppCompatActivity {
    Unbinder unbinder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_mypage1);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        unbinder = ButterKnife.bind(this);
    }


    @OnClick({R.id.bt_add_equipment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_add_equipment:
                startActivity(new Intent(this, AddEquipmentActivity.class));
                break;

        }

    }

}