package com.xr.happyFamily.jia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.activity.AddDeviceActivity;

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


    @OnClick({R.id.bt_add_equipment,R.id.rl_page})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_add_equipment:
                startActivity(new Intent(this, ManagementActivity.class));
                break;
            case R.id.rl_page:
                startActivity(new Intent(this, ChangeEquipmentActivity.class));
                break;
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
