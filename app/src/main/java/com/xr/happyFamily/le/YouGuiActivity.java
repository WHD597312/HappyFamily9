package com.xr.happyFamily.le;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.xr.database.dao.daoimpl.DerailResultDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.le.pojo.DerailResult;
import com.xr.happyFamily.le.view.YouguiDialog;
import com.xr.happyFamily.le.view.noBirthdayDialog;
import com.xr.happyFamily.zhen.PersonInfoActivity;

import java.util.List;

import crossoverone.statuslib.StatusUtil;

public class YouGuiActivity extends AppCompatActivity {
    private MyApplication application;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yougui);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (application==null){
            application= (MyApplication) getApplication();
        }
        application.addActivity(this);
        if (UStats.getUsageStatsList(this).isEmpty()) {

            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
        StatusUtil.setUseStatusBarColor(this, Color.TRANSPARENT, Color.parseColor("#33000000"));
        StatusUtil.setSystemStatus(this, false, true);
        Button button = (Button) findViewById(R.id.bt_bdfriend);
        ImageView iv_yougui_back = (ImageView) findViewById(R.id.iv_yougui_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YouGuiActivity.this, FriendActivity.class);
                intent.putExtra("type",1001);
                /*
                 * 带返回值的跳转方法，参数1：intent意图， 第二个参数请求码，是一个requestCode值，如果有多个按钮都要启动Activity，
                 * 则requestCode标志着每个按钮所启动的Activity
                 */
                startActivityForResult(intent, 1000);
            }
        });
        iv_yougui_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // 判断请求码和返回码是不是正确的，这两个码都是我们自己设置的
//        if (requestCode == 1000 && resultCode == 111) {
//            String name = data.getStringExtra("name");// 拿到返回过来的输入的账号
//            String pwd = data.getStringExtra("pwd");// 拿到返回过来的输入的账号
//            // 把得到的数据显示到输入框内
//            edt_name.setText(name);
//            edt_pwd.setText(pwd);
//
//        }
//
//    }


}
