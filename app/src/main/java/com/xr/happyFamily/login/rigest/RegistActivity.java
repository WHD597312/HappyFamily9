package com.xr.happyFamily.login.rigest;

import android.content.Context;
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
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.together.util.Utils;
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

public class RegistActivity extends AppCompatActivity {

    private String TAG="RegistActivity";
    MyApplication application;
    Unbinder unbinder;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.btn_get_code)
    Button btn_get_code;
    private String url="http://47.98.131.11:8084/user/identifyCode";
    SharedPreferences preferences;
    @BindView(R.id.iv_register_tb)
    ImageView imageView6;
    GifDrawable gifDrawable;
    Context mContext;
    int firstClick = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
       unbinder= ButterKnife.bind(this);
        if (application==null){
            application= (MyApplication) getApplication();
        }
        application.addActivity(this);


        //图标动画
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
    @OnClick({R.id.btn_finish,R.id.btn_get_code,})
    public void onClick(View view){
        switch (view.getId()){

            case R.id.btn_finish:
                if (firstClick==1){
                    String phone2 = et_phone.getText().toString().trim();
                    String code=et_code.getText().toString().trim();
                    String password=et_password.getText().toString().trim();
                    if (TextUtils.isEmpty(phone2)){
                        Utils.showToast(this,"手机号码不能为空");
                        return;
                    }
                    if (TextUtils.isEmpty(code)){
                        Utils.showToast(this,"请输入验证码");
                        return;
                    }
                    if (TextUtils.isEmpty(password)){
                        Utils.showToast(this,"请输入密码");
                        return;
                    }

                    Map<String,Object> params=new HashMap<>();
                    params.put("phone",phone2);
                    params.put("code",code);
                    params.put("password",password);

                    new RegistAsyncTask().execute(params);
//                new getShopAsync().execute(params);
                    firstClick=0;
                }

                break;
            case R.id.btn_get_code:
                String phone = et_phone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Utils.showToast(this,"手机号码不能为空");
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
            if (!Utils.isEmpty(result)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");



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
                    Utils.showToast(RegistActivity.this, "手机验证码错误，请重试");
                    break;
                case "100":
                    Utils.showToast(RegistActivity.this, "创建成功");
                    SharedPreferences.Editor editor = preferences.edit();
                    String phone = et_phone.getText().toString().trim();
                    String password = et_password.getText().toString().trim();
                    editor.putString("phone", phone);
                    editor.putString("password", password);
                    if (editor.commit()) {
                        Intent intent = new Intent(RegistActivity.this, RegistFinishActivity.class);
                        intent.putExtra("phone",phone);
                        intent.putExtra("password",password);
                        startActivity(intent);
                    }
                    break;
            }
        }
    }
//        class getShopAsync extends AsyncTask<Map<String, Object>, Void, String> {
//            @Override
//            protected String doInBackground(Map<String, Object>... maps) {
//                Map<String, Object> params = maps[0];
//                String result = HttpUtils.myPostOkHpptRequest(mContext, url, params);
//                String code = "";
//                Log.e("qqqqqqqXQ",result);
//                try {
//                    if (!Utils.isEmpty(result)) {
//                        JSONObject jsonObject = new JSONObject(result);
//                        code = jsonObject.getString("returnCode");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return code;
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
////                if (!Utils.isEmpty(s) && "100".equals(s)) {
////                    Toast.makeText(mContext, "编辑地址成功", Toast.LENGTH_SHORT).show();
////
////                }
//                switch (s){
//                    case "10003":
//                        Utils.showToast(RegistActivity.this,"手机验证码错误，请重试");
//                        break;
//                    case "10001":
//                        Utils.showToast(RegistActivity.this,"手机帐号已被注册");
//                        break;
//                    case "100":
//                        Utils.showToast(RegistActivity.this,"创建成功");
//                        SharedPreferences.Editor editor=preferences.edit();
//                        String phone = et_phone.getText().toString().trim();
//                        String password=et_password.getText().toString().trim();
//
//                        editor.putString("phone",phone);
//                        editor.putString("password",password);
//                        if (editor.commit()){
//                            Intent intent=new Intent(RegistActivity.this,RegistFinishActivity.class);
//                            startActivity(intent);
//                        }
//                        break;
//                }
//            }
//        }
//    }

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
            if (btn_get_code!=null){
                btn_get_code.setTextSize(18);
                btn_get_code.setText("重新发送");
                btn_get_code.setClickable(true);
            }

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

