package com.xr.happyFamily.bao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.ConfListAdapter;
import com.xr.happyFamily.bao.alipay.PayActivity;
import com.xr.happyFamily.bao.bean.Receive;
import com.xr.happyFamily.bean.ShopCartBean;
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

public class ShopConfActivity extends AppCompatActivity {


    int sign_pay = 0;
    TextView[] tv_pay;
    Drawable pay_true, pay_false;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    Context mContext;
    Receive receive;
    boolean isData = false;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.tv_conf_name)
    TextView tvConfName;
    @BindView(R.id.tv_conf_tel)
    TextView tvConfTel;
    @BindView(R.id.tv_conf_address)
    TextView tvConfAddress;
    @BindView(R.id.image_more_address)
    ImageView imageMoreAddress;
    @BindView(R.id.tv_zhifubao)
    TextView tvZhifubao;
    @BindView(R.id.tv_weixin)
    TextView tvWeixin;
    @BindView(R.id.tv_yinlian)
    TextView tvYinlian;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_shopcart_submit)
    TextView tvShopcartSubmit;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_shop_price)
    TextView tvShopPrice;
    @BindView(R.id.tv_yunfei)
    TextView tvYunfei;
    Boolean isAddress=true;

    private List<ShopCartBean> mGoPayList = new ArrayList<>();
    List<Map<String, Object>> mlist = new ArrayList<>();
    String money,weight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_shop_conf);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        String type = bundle.getString("type");
        if ("Cart".equals(type)) {
            mGoPayList = (ArrayList<ShopCartBean>) getIntent().getSerializableExtra("mGoPayList");
            for (int i = 0; i < mGoPayList.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("goodsId", mGoPayList.get(i).getGoods().getGoodsId());
                map.put("num", mGoPayList.get(i).getQuantity());
                map.put("priceId", mGoPayList.get(i).getGoodsPrice().getPriceId());
                map.put("weight", mGoPayList.get(i).getGoods().getWeight());
                mlist.add(map);
            }
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("goodsId", bundle.getString("goodsId"));
            map.put("num", bundle.getString("num"));
            map.put("priceId", bundle.getString("priceId"));
            weight=bundle.getString("weight");
            mlist.add(map);
        }
        money = bundle.getString("money");
        mContext = ShopConfActivity.this;
        titleText.setText("确认信息");
        titleRightText.setVisibility(View.GONE);
        tvShopPrice.setText("¥" + money);
        tvMoney.setText("¥" + money);
        ConfListAdapter confListAdapter = new ConfListAdapter(ShopConfActivity.this, mGoPayList);
        //      调用按钮返回事件回调的方法
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(confListAdapter);

        tv_pay = new TextView[]{tvZhifubao, tvWeixin, tvYinlian};
        pay_true = getResources().getDrawable(R.mipmap.xuanzhong_shop3x);
        pay_false = getResources().getDrawable(R.mipmap.weixuanzhong3x);
        pay_true.setBounds(0, 0, pay_true.getMinimumWidth(), pay_true.getMinimumHeight());
        pay_false.setBounds(0, 0, pay_false.getMinimumWidth(), pay_false.getMinimumHeight());
        Map<String, Object> params = new HashMap<>();
        SharedPreferences userSettings = getSharedPreferences("login", 0);
        String url = userSettings.getString("userId", "1000");
        params.put("userId", url);
        new getAddressAsync().execute(params);


    }

    @OnClick({R.id.rl_address, R.id.back, R.id.tv_zhifubao, R.id.tv_weixin, R.id.tv_yinlian, R.id.tv_shopcart_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_address:
                startActivityForResult(new Intent(this, ShopAddressActivity.class), 101);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.tv_zhifubao:
                tvZhifubao.setCompoundDrawables(null, null, pay_true, null);
                tv_pay[sign_pay].setCompoundDrawables(null, null, pay_false, null);
                sign_pay = 0;
                break;
            case R.id.tv_weixin:
                tvWeixin.setCompoundDrawables(null, null, pay_true, null);
                tv_pay[sign_pay].setCompoundDrawables(null, null, pay_false, null);
                sign_pay = 1;
                break;
            case R.id.tv_yinlian:
                tvYinlian.setCompoundDrawables(null, null, pay_true, null);
                tv_pay[sign_pay].setCompoundDrawables(null, null, pay_false, null);
                sign_pay = 2;
                break;
            case R.id.tv_shopcart_submit:
                if(isAddress) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("receiveId", receiveId);
                    //支付方式  1：支付宝
                    params.put("paymentId", 1);
                    params.put("orderDetailsList", mlist);
                    //邮费
                    params.put("postFee", 0);
                    new postOrderAsync().execute(params);
                }
                else {
                    Toast.makeText(mContext,"请先设置收货地址",Toast.LENGTH_SHORT).show();
                }
//                startActivityForResult(new Intent(this, PaySuccessActivity.class), 101);
                break;

        }
    }


    String receiveId="-1";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == 111) {
            String address = data.getStringExtra("address");// 拿到返回过来的地址
            receiveId = data.getStringExtra("receiveId");
            // 把得到的数据显示到输入框内
            tvConfAddress.setText(address);
            tvConfName.setText(data.getStringExtra("name"));
            tvConfTel.setText(data.getStringExtra("tel"));

            Map<String, Object> params = new HashMap<>();
            params.put("goodsName", "hello");
//                    params.put("weight", );
            params.put("receiveId", receiveId);
            params.put("weight", weight);
            new getPostFeeAsync().execute(params);


        }

    }

    String returnData = "";

    class getAddressAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "receive/getDefaultReceive";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
            String code = "";

            try {
                if (!Utils.isEmpty(result)) {

                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    returnData = jsonObject.getString("returnData");
                    if (!Utils.isEmpty(returnData)) {

                        Gson gson = new Gson();
                        receive = gson.fromJson(jsonObject.getString("returnData"), Receive.class);
                        receiveId = receive.getReceiveId() + "";
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
                if (!"null".equals(returnData)) {
                    isAddress=true;
                    tvConfAddress.setText(receive.getReceiveProvince() + " " + receive.getReceiveCity() + " " + receive.getReceiveCounty() + " " + receive.getReceiveAddress());
                    tvConfName.setText(receive.getContact());
                    tvConfTel.setText(receive.getTel());
                    Map<String, Object> params = new HashMap<>();
                    params.put("goodsName", "hello");
//                    params.put("weight", );
                    params.put("receiveId", receiveId);
                    params.put("weight", weight);
                    new getPostFeeAsync().execute(params);


                }
                else {
                        isAddress=false;
                        Toast.makeText(mContext,"请先填写默认地址",Toast.LENGTH_SHORT).show();

                    }
            }
        }
    }


    String post_fee = "0";

    class getPostFeeAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "logistics/getPostFee";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
            String code = "";

            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    returnData = jsonObject.getString("returnData");
                    if (!Utils.isEmpty(returnData)) {
                        JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                        JsonArray list = content.getAsJsonArray("ExpressList");
//                        for(int i=0;i<list.size();i++)
                        post_fee = list.get(0).getAsJsonObject().get("Fee").toString();

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
//
                tvYunfei.setText("+"+post_fee);
            }
        }
    }

    String orderId;

    class postOrderAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "order/addOrder";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    if (!Utils.isEmpty(code) && "100".equals(code)) {
                        orderId = jsonObject.getString("returnData");
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
                Intent intent = new Intent(ShopConfActivity.this, PayActivity.class);
                intent.putExtra("orderNumber", orderId);
                startActivity(intent);

            }
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Map<String, Object> params = new HashMap<>();
        SharedPreferences userSettings = getSharedPreferences("login", 0);
        String url = userSettings.getString("userId", "1000");
        params.put("userId", url);
        new getAddressAsync().execute(params);
    }
}
