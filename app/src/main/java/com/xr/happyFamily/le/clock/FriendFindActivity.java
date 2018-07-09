package com.xr.happyFamily.le.clock;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xr.happyFamily.R;
import com.xr.happyFamily.le.adapter.FriendFindAdapter;
import com.xr.happyFamily.le.bean.ClickFriendBean;
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

public class FriendFindActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tv_queding)
    TextView tvQueding;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    FriendFindAdapter FriendFindAdapter;
    @BindView(R.id.et_info)
    EditText etInfo;

    Context mContext=FriendFindActivity.this;
    Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_find);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        titleText.setText("添加好友");

        FriendFindAdapter = new FriendFindAdapter(this, list_friend);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(FriendFindAdapter);
    }

    @OnClick({R.id.tv_queding, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_queding:
                dialog = MyDialog.showDialog(this);
                dialog.show();
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


            String url = "/happy/clock/getClockUsers";
            url = url + "?userInfo=" +etInfo.getText().toString();
            String result = HttpUtils.doGet(mContext, url);
            Log.e("qqqqqqqqRRR",  result);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    returnMsg= jsonObject.getString("returnMsg");
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
                FriendFindAdapter.notifyDataSetChanged();

            }else  if (!Utils.isEmpty(s) && "5004".equals(s)) {
                MyDialog.closeDialog(dialog);
                Toast.makeText(mContext,returnMsg,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
