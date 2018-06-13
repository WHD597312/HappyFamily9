package com.xr.happyFamily.login.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopCartActivity;
import com.xr.happyFamily.bao.ShoppageActivity;
import com.xr.happyFamily.jia.ChangeEquipmentActivity;
import com.xr.happyFamily.jia.HomeActivity;
import com.xr.happyFamily.jia.HomepageActivity;
import com.xr.happyFamily.jia.MainActivity;
import com.xr.happyFamily.jia.MenuActivity;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.MyPaperActivity;
import com.xr.happyFamily.jia.xnty.AirConditionerActivity;
import com.xr.happyFamily.jia.xnty.AircleanerActivity;
import com.xr.happyFamily.jia.xnty.CsjActivity;
import com.xr.happyFamily.jia.xnty.HeaterActivity;
import com.xr.happyFamily.jia.xnty.MySeekBar;
import com.xr.happyFamily.jia.xnty.SmartSocket;
import com.xr.happyFamily.jia.xnty.WaterPurifierActivity;
import com.xr.happyFamily.jia.xnty.ZnWdActivity;
import com.xr.happyFamily.login.rigest.ForgetPswdActivity;
import com.xr.happyFamily.login.rigest.RegistActivity;
import com.xr.happyFamily.login.rigest.RegistFinishActivity;
import com.xr.happyFamily.login.util.Utils;
import com.xr.happyFamily.together.http.HttpUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifDrawable;

public class LoginActivity extends AppCompatActivity {

    Unbinder unbinder;
    MyApplication application;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_pswd)
    EditText et_pswd;
    String url = "http://47.98.131.11:8084/login/auth";
    @BindView(R.id.image_seepwd)
    ImageView imageView;
    @BindView(R.id.image_wx)
    ImageView imageViewwx;
    @BindView(R.id.imageView6)
    ImageView imageView6;
    boolean isHideFirst = true;
    IWXAPI wxapi;

    GifDrawable gifDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       unbinder = ButterKnife.bind(this);
       imageView.setImageResource(R.mipmap.yanjing13x);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        preferences = getSharedPreferences("my", MODE_PRIVATE);

        if (application == null) {
            application = (MyApplication) getApplication();
        }

        application.addActivity(this);
        et_name.setText(preferences.getString("phone", ""));
        et_pswd.setText(preferences.getString("password", ""));
    }

    SharedPreferences preferences;

    @Override
    protected void onStart() {
        super.onStart();
        try {
            gifDrawable=new GifDrawable(getResources(),R.mipmap.dtubiao);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (gifDrawable!=null){
            gifDrawable.start();
            imageView6.setImageDrawable(gifDrawable);
        }
        if (preferences.contains("phone")){
            String phone = preferences.getString("phone", "");
            et_name.setText(phone);
            et_pswd.setText("");
        }
        if (preferences.contains("phone") && preferences.contains("password")) {
            String phone = preferences.getString("phone", "");
            String password = preferences.getString("password", "");
            et_name.setText(phone);
            et_pswd.setText(password);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferences.contains("phone") && preferences.contains("password")){
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            if (preferences.contains("login")){
                startActivity(new Intent(this,HomepageActivity.class));
            }
        }
    }

    @OnClick({R.id.btn_login, R.id.tv_register,R.id.tv_forget_pswd,R.id.image_seepwd ,R.id.image_wx})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                startActivity(new Intent(this, RegistFinishActivity.class));
                break;
            case R.id.btn_login:
                String phone = et_name.getText().toString().trim();
                String password = et_pswd.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Utils.showToast(this, "账号码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Utils.showToast(this, "请输入密码");
                    return;
                }

               Map<String, Object> params = new HashMap<>();
                params.put("phone", phone);
                params.put("password", password);
                new LoginAsyncTask().execute(params);
                break;
            case R.id.tv_forget_pswd:
                startActivity(new Intent(this, ForgetPswdActivity.class));
                break;

            case R.id.image_seepwd:
                if (isHideFirst == true) {
                    imageView.setImageResource(R.mipmap.yanjing);
                    //密文
                    HideReturnsTransformationMethod method1 = HideReturnsTransformationMethod.getInstance();
                    et_pswd.setTransformationMethod(method1);
                    isHideFirst = false;
                } else {
                    imageView.setImageResource(R.mipmap.yanjing1);
                    //密文
                    TransformationMethod method = PasswordTransformationMethod.getInstance();
                    et_pswd.setTransformationMethod(method);
                    isHideFirst = true;

                }
                // 光标的位置
                int index = et_pswd.getText().toString().length();
                et_pswd.setSelection(index);
                break;
            case R.id.image_wx:
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo"; //授权域，snsapi_userinfo 表示获取用户个人信息
                req.state = "wechat_sdk_demo_test";
                wxapi.sendReq(req);
                break;
        }
    }

   @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            application.removeAllActivity();//**退出主页面*//*
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    class LoginAsyncTask extends AsyncTask<Map<String, Object>, Void, Integer> {

        @Override
        protected Integer doInBackground(Map<String, Object>... maps) {
            int code = 0;
            Map<String, Object> params = maps[0];
            String result = HttpUtils.postOkHpptRequest(url, params);
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("returnCode");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }


        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            switch (code) {
                case 10002:
                    Utils.showToast(LoginActivity.this, "手机号码未注册");
                    break;
                case 10004:
                    Utils.showToast(LoginActivity.this, "用户名或密码错误");
                    et_name.setText("");
                    et_pswd.setText("");
                    break;
                case 100:
                    Utils.showToast(LoginActivity.this, "登录成功");
                    SharedPreferences.Editor editor=preferences.edit();
                    String phone = et_name.getText().toString().trim();
                    String password = et_pswd.getText().toString().trim();
                    Log.i("phone", "---->: "+phone+",,,,"+password);
                    editor.putString("phone", phone);
                    editor.putString("password", password);
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
            }
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
