package com.xr.happyFamily.liwenchao.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.xr.happyFamily.R;

import butterknife.BindView;
import butterknife.Unbinder;

public class ForgetPswdActivity extends AppCompatActivity
{
    MyApplication application;
    Unbinder unbinder;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_code) EditText et_code;
    @BindView(R.id.et_password) EditText et_password;
    @BindView(R.id.btn_get_code)
    Button btn_get_code;
    private String url="";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgtpassword);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        /*unbinder= ButterKnife.bind(this);
        if (application==null){
            application= (MyApplication) getApplication();
        }
        application.addActivity(this);*/
    }
}
