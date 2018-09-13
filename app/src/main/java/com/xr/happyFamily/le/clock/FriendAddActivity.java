package com.xr.happyFamily.le.clock;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.base.ToastUtil;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.le.adapter.FriendFindAdapter;
import com.xr.happyFamily.le.bean.ClickFriendBean;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendAddActivity extends AppCompatActivity {


    Context mContext = FriendAddActivity.this;
    Dialog dialog;
    SharedPreferences preferences;
    ClickFriendBean clickFriendBean;

    String userId;
    String userName;
    String info;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.img_touxiang)
    ImageView imgTouxiang;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.rl_item)
    RelativeLayout rlItem;
    @BindView(R.id.et_info)
    EditText etInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_add);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        MyApplication application = (MyApplication) getApplication();
        application.addActivity(this);
        titleText.setText("添加好友");
        titleRightText.setText("发送");
        preferences = this.getSharedPreferences("my", MODE_PRIVATE);
        userId = preferences.getString("userId", "");
        userName = preferences.getString("username", "");
        clickFriendBean = (ClickFriendBean) getIntent().getSerializableExtra("friendData");
        info = "我是" + userName;
        etInfo.setHint(info);
        tvName.setText(clickFriendBean.getUsername());
        tvTel.setText(clickFriendBean.getPhone());
        if (clickFriendBean.getSex())
            tvSex.setText("男");
        else
            tvSex.setText("女");
        tvAge.setText(clickFriendBean.getAge() + "岁");
    }

    @OnClick({R.id.title_rightText, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.title_rightText:
                dialog = MyDialog.showDialog(this);
                dialog.show();
                if ((clickFriendBean.getUserId() + "").equals(userId))
                    ToastUtil.showShortToast("无法添加自己为好友");
                else
                    new getClockUsers().execute();
                break;
            case R.id.back:
                finish();
                break;

        }
    }

    List<ClickFriendBean> list_friend = new ArrayList<>();
    String returnMsg;

    class getClockUsers extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {


            if (!Utils.isEmpty(etInfo.getText().toString()))
                info = etInfo.getText().toString();

            String url = "/happy/clock/addClockFriend";
            url = url + "?senderId=" + userId + "&acceptorId=" + clickFriendBean.getUserId() + "&remark=" + info;
            String result = HttpUtils.doGet(mContext, url);
            Log.e("qqqqqqqqRRR", result);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    returnMsg = jsonObject.getString("returnMsg");
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
            MyDialog.closeDialog(dialog);
            if (!Utils.isEmpty(s) && "100".equals(s)) {
                Toast.makeText(mContext, "发送好友申请成功", Toast.LENGTH_SHORT).show();
                finish();
            } else if (!Utils.isEmpty(s) && "401".equals(s)) {
                Toast.makeText(getApplicationContext(), "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences;
                preferences = getSharedPreferences("my", MODE_PRIVATE);
                MyDialog.setStart(false);
                if (preferences.contains("password")) {
                    preferences.edit().remove("password").commit();
                }
                startActivity(new Intent(mContext.getApplicationContext(), LoginActivity.class));
            } else {
                ToastUtil.showShortToast( returnMsg);

            }
        }
    }
}
