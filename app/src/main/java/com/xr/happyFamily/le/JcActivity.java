package com.xr.happyFamily.le;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.le.pojo.MapAdress;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.GlideCircleTransform;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JcActivity extends AppCompatActivity {
    private MyApplication application;
    @BindView(R.id.iv_map_photo)
    ImageView image_user;/**用户头像*/
    SharedPreferences preferences;
    String ip = "http://47.98.131.11:8084";
    String derailId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_jc);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (application==null){
            application= (MyApplication) getApplication();
        }
        ButterKnife.bind(this);//绑定framgent
        application.addActivity(this);
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        derailId = preferences.getString("derailId", "");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (preferences.contains("image")){
            String image=preferences.getString("image","");
            if (!Utils.isEmpty(image)){
                File file=new File(image);
                Glide.with(this).load(file).transform(new GlideCircleTransform(this)).into(image_user);
            }
        }
    }
    @OnClick({R.id.iv_map_back,R.id.bt_jc})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_map_back:
                finish();
                break;
            case R.id.bt_jc:
                new getSiteData().execute();
                break;
        }

    }
    class getSiteData extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            int code = 0;
            String url = ip + "/happy/derailed/deleteDerailed?derailId=" + derailId;
            String result = HttpUtils.getOkHpptRequest(url);
            Log.e("ressssssss11", "doInBackground: -->" + result);
            try {
                if (!TextUtils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("returnCode");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            switch (integer) {
                case 100:
                    Utils.showToast(JcActivity.this,"解绑成功");
                    application.removeActivity(JcActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("derailPo", 0);
                    editor.commit();
                    startActivity(new Intent(JcActivity.this,MainActivity.class));

                    break;
                default:

                    Utils.showToast(JcActivity.this,"解绑失败，请重试");
                    break;
            }
        }
    }
}
