package com.xr.happyFamily.le;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.base.ToastUtil;
import com.xr.happyFamily.le.adapter.FriendAdapter;
import com.xr.happyFamily.le.bean.ClickFriendBean;
import com.xr.happyFamily.le.clock.FriendFindActivity;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2018/5/22.
 */

public class FriendActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    SharedPreferences preferences;
    String userId;
    //type:判断从哪个个activity跳转  1000：乐，1001：有轨
    int type;
    Context mContext = FriendActivity.this;
    FriendAdapter friendAdapter;
    List<ClickFriendBean> list_friend = new ArrayList<>();
    MyDialog dialog;
    @BindView(R.id.tv_queding)
    TextView tvQueding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        Log.e("qqqqTTT", type + "?");
        preferences = this.getSharedPreferences("my", MODE_PRIVATE);
        userId = preferences.getString("userId", "");

        initView();
    }

    private void initData() {
        list_friend.clear();
        dialog = MyDialog.showDialog(this);
        dialog.show();
        new getClockFriends().execute();
    }

    //
    private void initView() {
        titleText.setTextColor(Color.parseColor("#242424"));
        titleText.setText("好友列表");
        imgRight.setImageResource(R.mipmap.ic_friend_add);
        if (type == 1000)
            tvQueding.setVisibility(View.GONE);
        friendAdapter = new FriendAdapter(this, list_friend, type, userId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(friendAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();

    }

    @OnClick({R.id.back, R.id.img_right, R.id.tv_queding})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.img_right:
                startActivity(new Intent(FriendActivity.this, FriendFindActivity.class));
                break;

            case R.id.tv_queding:
                String acceptorId = friendAdapter.getMember();
                Map<String, Object> params = new HashMap<>();
                params.put("acceptorId", acceptorId);
                new beDerailedAsync().execute(params);

        }
    }


    class getClockFriends extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {


            String url = "/happy/clock/getClockFriends";
            url = url + "?userId=" + userId;
            String result = HttpUtils.doGet(mContext, url);
            Log.e("qqqqqqqqRRR", userId + "?" + result);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");

                    JsonObject content = new JsonParser().parse(jsonObject.toString()).getAsJsonObject();
                    JsonArray list = content.getAsJsonArray("returnData");
                    Gson gson = new Gson();
                    for (JsonElement user : list) {
                        //通过反射 得到UserBean.class
                        ClickFriendBean userList = gson.fromJson(user, ClickFriendBean.class);
                        userList.setMemSign(0);
                        list_friend.add(userList);
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!Utils.isEmpty(s) && "100".equals(s)) {
                MyDialog.closeDialog(dialog);
                friendAdapter.notifyDataSetChanged();

            } else if (!Utils.isEmpty(s) && "401".equals(s)) {
                Toast.makeText(getApplicationContext(), "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences;
                preferences = getSharedPreferences("my", MODE_PRIVATE);
                MyDialog.setStart(false);
                if (preferences.contains("password")) {
                    preferences.edit().remove("password").commit();
                }
                startActivity(new Intent(mContext.getApplicationContext(), LoginActivity.class));
            }
        }
    }


    String returnMsg="";
    class beDerailedAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String url = "/happy/derailed/beDerailed?";
            Map<String, Object> params = maps[0];
            String acceptorId = params.get("acceptorId").toString();
            url = url + "senderId=" + userId + "&acceptorId=" + acceptorId;

            Log.e("qqqqqRRR",url);
            String result = HttpUtils.doGet(mContext, url);
            Log.e("qqqqqRRR222",result);

            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    }else {

                        JSONObject jsonObject = new JSONObject(result);
                        code = jsonObject.getString("returnCode");
                        returnMsg=jsonObject.getString("returnMsg");

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
//
            return code;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!Utils.isEmpty(s) && "100".equals(s)) {
                ToastUtil.showShortToast("发送成功，请等待对方同意");
            }else
                ToastUtil.showShortToast(returnMsg);
        }
    }

    public void setBangDing(int acceptorId) {
        Intent intent = new Intent();
        intent.putExtra("acceptorId", acceptorId);
        setResult(111, intent);
        finish();//结束当前activity

    }


}