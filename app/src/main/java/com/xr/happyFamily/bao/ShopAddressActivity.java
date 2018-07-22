package com.xr.happyFamily.bao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.AddressAdapter;
import com.xr.happyFamily.bean.AddressBean;
import com.xr.happyFamily.bean.ShopBean;
import com.xr.happyFamily.jia.MyApplication;
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

public class ShopAddressActivity extends AppCompatActivity implements AddressAdapter.InnerItemOnclickListener, View.OnClickListener {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.img_add)
    ImageView imgAdd;
    private AddressAdapter addressAdapter;
    private  List<AddressBean.ReturnData> mDatas;
    Context mContext;
    int sign_del=0;
    private MyDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        MyApplication application = (MyApplication) getApplication();
        application.addActivity(this);
        setContentView(R.layout.activity_shop_address);
        ButterKnife.bind(this);
        mContext=ShopAddressActivity.this;
        titleText.setText("收货地址");
        titleRightText.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
//      获取数据，向适配器传数据，绑定适配器
        mDatas=new ArrayList<AddressBean.ReturnData>();

        addressAdapter = new AddressAdapter(ShopAddressActivity.this, mDatas);
        recyclerView.setAdapter(addressAdapter);
        //      调用按钮返回事件回调的方法
        addressAdapter.buttonSetOnclick(new AddressAdapter.ButtonInterface() {
            @Override
            public void onclick(View view, int position) {
                addressAdapter.setDefSelect(position);
            }
        });
        addressAdapter.setOnItemClickListener(new AddressAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("receiveId",mDatas.get(position).getReceiveId()+"");
                intent.putExtra("name", mDatas.get(position).getContact());
                intent.putExtra("tel", mDatas.get(position).getTel());
                intent.putExtra("address", mDatas.get(position).getReceiveProvince()+" "+mDatas.get(position).getReceiveCity()+" "+mDatas.get(position).getReceiveCounty()+" "+mDatas.get(position).getReceiveAddress());
    /*
     * 调用setResult方法表示我将Intent对象返回给之前的那个Activity，这样就可以在onActivityResult方法中得到Intent对象，
     * 参数1：resultCode返回码，跳转之前的activity根据是这个resultCode，区分是哪一个activity返回的
     * 参数2：数据源
     */
                setResult(111, intent);
                finish();//结束当前activity
            }
        });
        addressAdapter.setOnInnerItemOnClickListener(this);
        getShopData();
    }



    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.img_bianji:
                Intent intent=new Intent(this,EditAddressActivity.class);
                intent.putExtra("receiveId", mDatas.get(position).getReceiveId());
                intent.putExtra("isDefault", mDatas.get(position).getIsDefault());
                intent.putExtra("name", mDatas.get(position).getContact());
                intent.putExtra("tel", mDatas.get(position).getTel());
                intent.putExtra("address", mDatas.get(position).getReceiveProvince()+" "+mDatas.get(position).getReceiveCity()+" "+mDatas.get(position).getReceiveCounty());
                intent.putExtra("receiveProvince", mDatas.get(position).getReceiveProvince());
                intent.putExtra("receiveCity", mDatas.get(position).getReceiveCity());
                intent.putExtra("receiveCounty", mDatas.get(position).getReceiveCounty());
                intent.putExtra("receiveAddress", mDatas.get(position).getReceiveAddress());
                startActivity(intent);
                break;
            case R.id.img_del:
                sign_del=mDatas.get(position).getReceiveId();
                showPopup();
                break;
            default:
                break;
        }
    }


    @OnClick({R.id.back, R.id.img_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img_add:
                startActivity(new Intent(ShopAddressActivity.this, ShopAddAddressActivity.class));
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mDatas.clear();
        getShopData();
        addressAdapter.notifyDataSetChanged();
    }

    private void getShopData(){
        dialog = MyDialog.showDialog(mContext);
        dialog.show();
        Map<String, Object> params = new HashMap<>();
        SharedPreferences userSettings= getSharedPreferences("my", 0);
        String url = userSettings.getString("userId","1000");
        params.put("userId", url);
        new AddressAsync().execute(params);
    }

    class AddressAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];

            String url = "receive/listReceive";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
            String code = "";

            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code=result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JsonObject content = new JsonParser().parse(jsonObject.toString()).getAsJsonObject();
                    JsonArray list = content.getAsJsonArray("returnData");
                    if ("100".equals(code)) {
                        Gson gson = new Gson();
                        for (JsonElement user : list) {
                            //通过反射 得到UserBean.class
                            AddressBean.ReturnData userList = gson.fromJson(user, AddressBean.ReturnData.class);
                            mDatas.add(userList);

                        }

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
                addressAdapter.notifyDataSetChanged();
            }else if (!Utils.isEmpty(s) && "401".equals(s)) {
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


    class DelAddressAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "receive/delReceive";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code=result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
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
                Toast.makeText(mContext, "删除地址成功", Toast.LENGTH_SHORT).show();
                mDatas.clear();
                getShopData();
            }else if (!Utils.isEmpty(s) && "401".equals(s)) {
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


    private View contentViewSign;
    private PopupWindow mPopWindow;
    private TextView tv_quxiao,tv_queding,tv_context;

    private void showPopup() {
        mContext=ShopAddressActivity.this;
        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_main, null);
        tv_quxiao = (TextView) contentViewSign.findViewById(R.id.tv_quxiao);
        tv_queding = (TextView) contentViewSign.findViewById(R.id.tv_queren);
        tv_context = (TextView) contentViewSign.findViewById(R.id.tv_context);
        tv_context.setText("确定删除收货地址？");
        ((TextView)contentViewSign.findViewById(R.id.tv_title)).setText("收货地址");
//        tv_shangcheng = (TextView) contentViewSign.findViewById(R.id.tv_shangcheng);
//        tv_shopcart.setOnClickListener(this);
        tv_quxiao.setOnClickListener(this);
        tv_queding.setOnClickListener(this);
        mPopWindow = new PopupWindow(contentViewSign);
        mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        mPopWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //点击空白处时，隐藏掉pop窗口
        mPopWindow.setFocusable(true);
//        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);
        backgroundAlpha(0.5f);
        //添加pop窗口关闭事件
        mPopWindow.setOnDismissListener(new ShopAddressActivity.poponDismissListener());
        mPopWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp); //添加pop窗口关闭事件
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_quxiao:
                mPopWindow.dismiss();
                break;
            case R.id.tv_queren:
                mPopWindow.dismiss();
                Map<String, Object> params = new HashMap<>();
                params.put("receiveId", sign_del);
                new DelAddressAsync().execute(params);
                break;
        }
    }


    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }

    }
}
