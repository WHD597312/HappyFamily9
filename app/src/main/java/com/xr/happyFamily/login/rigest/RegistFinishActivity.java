package com.xr.happyFamily.login.rigest;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bean.UserBean;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.xnty.SmartSocket;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.zhen.PersonInfoActivity;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.smssdk.SMSSDK;
import pl.droidsonroids.gif.GifDrawable;

public class RegistFinishActivity extends AppCompatActivity {
    Unbinder unbinder;
    Dialog dia ;
    private String url = "http://47.98.131.11:8084/user/register";

    @BindView(R.id.iv_registerf_tb)
    ImageView imageView6;
    @BindView(R.id.et_fphone)
    EditText editTextf;
    @BindView(R.id.rgMale)
    RadioGroup radioGroup;
    @BindView(R.id.rbMale)
    RadioButton radioButtonm;
    @BindView(R.id.rbfaMale)
    RadioButton radioButtonfm;
    @BindView(R.id.tv_birthday)
    TextView textViewb;
    @BindView(R.id.btn_ffinish)
    Button buttonf;
    GifDrawable gifDrawable;
    int temp=-1;
    SharedPreferences preferences;
    Calendar calendar;
    String phone;
    String password;
    String birthday;
    Animation rotate;
    MyApplication application;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registfinish);
        if (application == null) {
            application = (MyApplication) getApplication();
        }
        application.addActivity(this);
        unbinder = ButterKnife.bind(this);
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        /*imagefs.setAnimation(rotate);
        imagefs.startAnimation(rotate);*/
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        rotate.setInterpolator(lin);
        imageView6.startAnimation(rotate);

        initsex();
        calendar = Calendar.getInstance();
        Log.i("aaaaa1", "----> "+temp);
        Bundle bundle=getIntent().getExtras();
        phone=bundle.getString("phone");
        password=bundle.getString("password");
        Log.e("asd", "onCreate: "+phone );
    }


    private void initsex() {
        this.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                if(radioButtonm.getId()==checkedId){
                    temp=1;
                    Log.i("aaaaa", "----> "+temp);
                }
                if(radioButtonfm.getId()==checkedId){
                    temp=0;
                    Log.i("aaa11", "----> "+temp);
                }

            }
        });
    }

    @OnClick({R.id.btn_ffinish,R.id.tv_birthday})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_birthday:
                try {
                    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                    Date date=format.parse("1970-1-1");
                    long minDay=date.getTime();
                Date date2=new Date();
                Calendar calendar=Calendar.getInstance();
                long current=calendar.getTimeInMillis();
                long maxTime=date2.getTime();
                TimePickerDialog dialogYearMonthDay = new TimePickerDialog.Builder()
                        .setType(Type.YEAR_MONTH_DAY)
                        .setThemeColor(Color.parseColor("#4fc586"))
                        .setTitleStringId("修改生日")
                        .setMinMillseconds(minDay)
                        .setCurrentMillseconds(current)
                        .setMaxMillseconds(maxTime)
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                Date date=new Date(millseconds);
                                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                                birthday=format.format(date);
                                textViewb.setText(birthday);

                            }
                        })
                        .build();
                dialogYearMonthDay.show(getSupportFragmentManager(), "YEAR_MONTH_DAY");


    } catch (Exception e) {
        // TODO: handle exception
    }
                break;
            case R.id.btn_ffinish:

                    String name = editTextf.getText().toString().trim();
                    if (TextUtils.isEmpty(name)) {
                        Utils.showToast(this, "昵称不能为空");
                        return;
                    }
                    if (temp==-1){
                        Utils.showToast(this, "性别不能为空");
                        return;
                    }

                    if (TextUtils.isEmpty(birthday)) {
                        Utils.showToast(this, "生日不能为空");
                        return;
                    }
                    Map<String,Object> params=new HashMap<>();
                    params.put("username",name);
                    params.put("sex",temp);
                    params.put("birthday",birthday);
                    params.put("phone",phone);
                    params.put("password",password);
                    Log.e("ttttt", "onClick: "+birthday );
                    new RegistAsyncTask().execute(params);


                break;
        }
    }


    class RegistAsyncTask extends AsyncTask<Map<String,Object>,Void,String> {

        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String code = "";
            Map<String, Object> params = maps[0];
            String result = HttpUtils.postOkHpptRequest(url, params);
            Log.i("iiiiii", "---->: "+result);
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

                case "100":

                        Context mcontext = RegistFinishActivity.this;
                        dia = new Dialog(mcontext, R.style.edit_AlertDialog_style);//设置进入时跳出提示框
                        dia.setContentView(R.layout.activity_regist_dialog);
                        final ImageView imageView = (ImageView) dia.findViewById(R.id.iv_dialogr);
                        imageView.setBackgroundResource(R.mipmap.regest_success);
                        dia.show();
                        dia.setCanceledOnTouchOutside(true); // 设置屏幕点击退出
                        Window w = dia.getWindow();
                        WindowManager.LayoutParams lp = w.getAttributes();
                        lp.x = 0;
                        dia.onWindowAttributesChanged(lp);

                        final Thread newThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                    Intent intent = new Intent(RegistFinishActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        newThread.start();

                    break;
                case "10001":
                    Utils.showToast(RegistFinishActivity.this, "账户已存在");
                    break;
            }
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