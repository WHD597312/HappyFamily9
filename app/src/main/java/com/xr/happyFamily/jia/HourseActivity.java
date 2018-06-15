package com.xr.happyFamily.jia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.titleview.TitleView;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.http.HttpUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HourseActivity extends AppCompatActivity{
    Unbinder unbinder;
    TitleView titleView;
    @BindView(R.id.lv_hourse)
    ListView listView;//
    private String[] mhome = { "1的家", "2的家", "3的家",};
    private String[] mplace = { "北京", "上海", "杭州"};
    String url = "http://47.98.131.11:8084/login/auth";
    protected void onCreate(Bundle savadInstanceState) {
        super.onCreate(savadInstanceState);

        setContentView(R.layout.activity_home_hourse);
        unbinder = ButterKnife.bind(this);
        titleView = (TitleView) findViewById(R.id.title_addroom);
        titleView.setTitleText("家庭管理");
        final List<HashMap<String, Object>> users = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            HashMap<String, Object> user = new HashMap<>();
            user.put("mhome",mhome[i]);
            user.put("mplace", mplace[i]);

            users.add(user);
        }
    /*    ListView listView = (ListView) findViewById(R.id.zncz_add_list);
        ZnczAdapter mAdapter = new ZnczAdapter(context,
                users);*/
        SimpleAdapter saImageItems = new SimpleAdapter(this,
                users,
                // 数据来源
                R.layout.activity_home_hourseitem,//每一个user xml 相当ListView的一个组件

                new String[] { "mhome", "mplace" },
                // 分别对应view 的id
                new int[] { R.id.tv_hourse_h, R.id.tv_hourse_p});
        // 获取listview
        listView.setAdapter(saImageItems);
//        Map<String, Object> params = new HashMap<>();
//        params.put("houseid", houseid);
//        params.put("houseAddress", houseAddress);
//        new HourseAsyncTask().execute(params);
    }
    class HourseAsyncTask extends AsyncTask<Map<String, Object>, Void, Integer> {

        @Override
        protected Integer doInBackground(Map<String, Object>... maps) {
            int code = 0;
            Map<String, Object> params = maps[0];
            String result = HttpUtils.postOkHpptRequest(url, params);
            try {
                if (!com.xr.happyFamily.login.util.Utils.isEmpty(result)) {
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
//                case 10002:
//                    com.xr.happyFamily.login.util.Utils.showToast(LoginActivity.this, "手机号码未注册");
//                    break;
//                case 10004:
//                    com.xr.happyFamily.login.util.Utils.showToast(LoginActivity.this, "用户名或密码错误");
//
//                    break;
//                case 100:
//                    com.xr.happyFamily.login.util.Utils.showToast(LoginActivity.this, "登录成功");
//                    SharedPreferences.Editor editor=preferences.edit();
//                    String phone = et_name.getText().toString().trim();
//                    String password = et_pswd.getText().toString().trim();
//                    Log.i("phone", "---->: "+phone+",,,,"+password);
//                    editor.putString("phone", phone);
//                    editor.putString("password", password);
//                    editor.commit();
//                    Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    break;
                     }
            }
        }
    @OnClick({})
    public void onClick(View view) {
        switch (view.getId()) {
//            case
//                break;



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
