package com.xr.happyFamily.login.rigest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.http.HttpUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import pl.droidsonroids.gif.GifDrawable;

public class ForgetPswdActivity extends AppCompatActivity
{    private String TAG="ForgetPswdActivity";
    MyApplication application;
    Unbinder unbinder;
    SharedPreferences preferences;
    private String url="http://47.98.131.11:8084/user/forgetPassword";
    GifDrawable gifDrawable;
    @BindView(R.id.iv_fg_tb)
    ImageView imageView6;
    @BindView(R.id.et_fg_phone)
    EditText et_phone;
    @BindView(R.id.et_fg_code)
    EditText et_code;
    @BindView(R.id.et_fg_password)
    EditText et_password;
    @BindView(R.id.btn_fg_code)
    Button btn_get_code;
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
    @Override
    protected void onStart() {
        super.onStart();
        preferences=getSharedPreferences("my",MODE_PRIVATE);
        SMSSDK.registerEventHandler(eventHandler);

    }
    private EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            super.afterEvent(event, result, data);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                    @SuppressWarnings("unchecked") HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");
                    Log.d(TAG, "提交验证码成功--country=" + country + "--phone" + phone);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
                    Log.d(TAG, "获取验证码成功");
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                }
            } else {
                ((Throwable) data).printStackTrace();
            }
        }
    };
    @OnClick({R.id.btn_fg_finish,R.id.btn_fg_code,})
    public void onClick(View view){
        switch (view.getId()){

            case R.id.btn_fg_finish:
                String phone2 = et_phone.getText().toString().trim();
                String code=et_code.getText().toString().trim();
                String password=et_password.getText().toString().trim();
                if (TextUtils.isEmpty(phone2)){
                    com.xr.happyFamily.login.util.Utils.showToast(this,"手机号码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(code)){
                    com.xr.happyFamily.login.util.Utils.showToast(this,"请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    com.xr.happyFamily.login.util.Utils.showToast(this,"请输入密码");
                    return;
                }

                Map<String,Object> params=new HashMap<>();
                params.put("phone",phone2);
                params.put("code",code);
                params.put("password",password);

                new ForgetPswdActivity.RegistAsyncTask().execute(params);
//                new getShopAsync().execute(params);
                break;
            case R.id.btn_fg_code:
                String phone = et_phone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    com.xr.happyFamily.login.util.Utils.showToast(this,"手机号码不能为空");
                } else {
                    SMSSDK.getVerificationCode("86", phone);
                    CountTimer countTimer=new CountTimer(60000,1000);
                    countTimer.start();
                }
                break;
        }
    }

    class RegistAsyncTask extends AsyncTask<Map<String,Object>,Void,String> {

        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String code = "";
            Map<String, Object> params = maps[0];
            String result = HttpUtils.postOkHpptRequest(url, params);
            Log.i("back", "--->"+result);
            if (!com.xr.happyFamily.login.util.Utils.isEmpty(result)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");

                    SharedPreferences.Editor editor = preferences.edit();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return code;

        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            switch (s) {
                case "10003":
                    com.xr.happyFamily.login.util.Utils.showToast(ForgetPswdActivity.this, "手机验证码错误，请重试");
                    break;
                case "10002":
                    com.xr.happyFamily.login.util.Utils.showToast(ForgetPswdActivity.this, "账户不存在");
                    break;
                case "100":
                    com.xr.happyFamily.login.util.Utils.showToast(ForgetPswdActivity.this, "创建成功");
                    SharedPreferences.Editor editor = preferences.edit();
                    String phone = et_phone.getText().toString().trim();
                    String password = et_password.getText().toString().trim();
                    editor.putString("phone", phone);
                    editor.putString("password", password);
                    if (editor.commit()) {
                        Intent intent = new Intent(ForgetPswdActivity.this, LoginActivity.class);

                        startActivity(intent);
                    }
                    break;
            }
        }
    }
    class CountTimer extends CountDownTimer {
        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        /**
         * 倒计时过程中调用
         *
         * @param millisUntilFinished
         */
        @Override
        public void onTick(long millisUntilFinished) {
            Log.e("Tag", "倒计时=" + (millisUntilFinished/1000));
            if (btn_get_code!=null){
                btn_get_code.setText(millisUntilFinished / 1000 + "s后重新发送");
                //设置倒计时中的按钮外观
                btn_get_code.setClickable(false);//倒计时过程中将按钮设置为不可点击
//            btn_get_code.setBackgroundColor(Color.parseColor("#c7c7c7"));
                btn_get_code.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.darker_gray));
                btn_get_code.setTextSize(16);
            }

        }

        /**
         * 倒计时完成后调用
         */
        @Override
        public void onFinish() {
            Log.e("Tag", "倒计时完成");
            //设置倒计时结束之后的按钮样式
//            btn_get_code.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_light));
//            btn_get_code.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
            btn_get_code.setTextSize(18);
            btn_get_code.setText("重新发送");
            btn_get_code.setClickable(true);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}
