package com.xr.happyFamily.login.rigest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifDrawable;

public class ForgetPswdActivity extends AppCompatActivity
{
    MyApplication application;
    Unbinder unbinder;

    private String url="";
    GifDrawable gifDrawable;
    @BindView(R.id.iv_fg_tb)
    ImageView imageView6;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgtpassword);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        unbinder= ButterKnife.bind(this);
        /*if (application==null){
            application= (MyApplication) getApplication();
        }
        application.addActivity(this);*/
        try {
            gifDrawable=new GifDrawable(getResources(),R.mipmap.dtubiao);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (gifDrawable!=null){
            gifDrawable.start();
            imageView6.setImageDrawable(gifDrawable);
        }
    }
}
