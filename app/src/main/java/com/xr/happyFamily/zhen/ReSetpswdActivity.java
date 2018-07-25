package com.xr.happyFamily.zhen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.together.http.HttpUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ReSetpswdActivity extends AppCompatActivity {

    Unbinder unbinder;
    MyApplication application;
    private String oldPswd;/**旧密码*/
    private String newPswd;/**新密码*/
    private String ensurePswd;/**确认密码*/
    @BindView(R.id.et_oldpswd) EditText et_oldpswd;
    @BindView(R.id.et_newpswd) EditText et_newpswd;
    @BindView(R.id.et_ensurepswd) EditText et_ensurepswd;
    private SharedPreferences preferences;
    private String password;
    private String phone;
    String url=HttpUtils.ipAddress+"/user/resetPassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_re_setpswd);
        if (application==null){
            application= (MyApplication) getApplication();
            application.addActivity(this);
        }
        unbinder=ButterKnife.bind(this);
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        password=preferences.getString("password","");
        phone=preferences.getString("phone","");
    }

    @OnClick({R.id.back,R.id.btn_ensure})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_ensure:
                oldPswd=et_oldpswd.getText().toString().trim();
                newPswd=et_newpswd.getText().toString().trim();
                ensurePswd=et_ensurepswd.getText().toString().trim();
                if (TextUtils.isEmpty(oldPswd)){
                    Toast.makeText(this,"请输入旧密码",Toast.LENGTH_SHORT).show();
                    break;
                }else if(TextUtils.isEmpty(newPswd)){
                    Toast.makeText(this,"请输入新密码",Toast.LENGTH_SHORT).show();
                    break;
                }else if (TextUtils.isEmpty(ensurePswd)){
                    Toast.makeText(this,"请输入确认密码",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!TextUtils.isEmpty(oldPswd)){
                    if (!oldPswd.equals(password)){
                        Toast.makeText(this,"旧密码错误",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if (newPswd.equals(ensurePswd)){

                }else {
                    Toast.makeText(this,"新密码与确认密码不同",Toast.LENGTH_SHORT).show();
                    break;
                }
                Map<String,Object> params=new HashMap<>();
                params.put("phone",phone);
                params.put("oldPassword",password);
                params.put("password",ensurePswd);
                new ResetpswdAsync().execute(params);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            application.removeActivity(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }

    class ResetpswdAsync extends AsyncTask<Map<String,Object>,Void,Integer>{

        @Override
        protected Integer doInBackground(Map<String, Object>... maps) {
            int code=0;
            Map<String,Object> params=maps[0];
            String result=HttpUtils.postOkHpptRequest(url,params);
            if (!TextUtils.isEmpty(result)){
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    String returnCode=jsonObject.getString("returnCode");
                    if ("100".equals(returnCode)){
                        code=100;
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("password",ensurePswd);
                        editor.commit();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            switch (code){
                case 100:
                    Toast.makeText(ReSetpswdActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                    default:
                        Toast.makeText(ReSetpswdActivity.this,"设置失败",Toast.LENGTH_SHORT).show();
                        break;
            }
        }
    }
}
