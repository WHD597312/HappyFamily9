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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.ConfListAdapter;
import com.xr.happyFamily.bao.adapter.DingDanXQAdapter;
import com.xr.happyFamily.bao.alipay.PayActivity;
import com.xr.happyFamily.bao.bean.Goods;
import com.xr.happyFamily.bao.bean.GoodsPrice;
import com.xr.happyFamily.bao.bean.Receive;
import com.xr.happyFamily.bean.OrderBean;
import com.xr.happyFamily.bean.PostFreeBean;
import com.xr.happyFamily.bean.ShopCartBean;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.wxapi.WXPayActiviy;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    int money;
    Double weight;
    String type ;
    ConfListAdapter confListAdapter;
    DingDanXQAdapter dingDanXQAdapter;
    private MyDialog dialog;

    boolean isShopData=false,isPrice=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        MyApplication application = (MyApplication) getApplication();
        application.addActivity(this);
        setContentView(R.layout.activity_shop_conf);
        ButterKnife.bind(this);

        mContext = ShopConfActivity.this;
        titleText.setText("确认信息");
        titleRightText.setVisibility(View.GONE);

        dialog = MyDialog.showDialog(mContext);
        dialog.show();
        dingDanXQAdapter = new DingDanXQAdapter(ShopConfActivity.this, orderDetailsLists);
        //      调用按钮返回事件回调的方法
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        tv_pay = new TextView[]{tvZhifubao, tvWeixin, tvYinlian};
        pay_true = getResources().getDrawable(R.mipmap.xuanzhong_shop3x);
        pay_false = getResources().getDrawable(R.mipmap.weixuanzhong3x);
        pay_true.setBounds(0, 0, pay_true.getMinimumWidth(), pay_true.getMinimumHeight());
        pay_false.setBounds(0, 0, pay_false.getMinimumWidth(), pay_false.getMinimumHeight());
        Map<String, Object> params = new HashMap<>();
        SharedPreferences userSettings = getSharedPreferences("my", 0);
        String url = userSettings.getString("userId", "1000");
        params.put("userId", url);
        new getAddressAsync().execute(params);
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        if("DingDan".equals(type)){
            recyclerView.setAdapter(dingDanXQAdapter);
            orderNumber=bundle.get("orderNumber").toString();
            tvMoney.setText("¥"+bundle.get("money").toString());
            Map<String, Object> map = new HashMap<>();
            new getOrderAsync().execute(map);
        }
        else if ("Cart".equals(type)) {
            isShopData=true;
            money=bundle.getInt("money");
            weight=bundle.getDouble("weight");
            mGoPayList = (ArrayList<ShopCartBean>) getIntent().getSerializableExtra("mGoPayList");
            confListAdapter = new ConfListAdapter(ShopConfActivity.this, mGoPayList);
            recyclerView.setAdapter(confListAdapter);
            tvShopPrice.setText("¥"+bundle.get("money").toString());
            for (int i = 0; i < mGoPayList.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("goodsId", mGoPayList.get(i).getGoods().getGoodsId());
                map.put("num", mGoPayList.get(i).getQuantity());
                map.put("priceId", mGoPayList.get(i).getGoodsPrice().getPriceId());
                map.put("weight", mGoPayList.get(i).getGoods().getWeight());
                mlist.add(map);
            }

        } else {
            isShopData=true;
            ShopCartBean shopCartBean=new ShopCartBean();
            Goods goods=new Goods();
            goods.setGoodsName(bundle.getString("name"));
            goods.setSimpleDescribe(bundle.getString("context"));
            goods.setImage(bundle.getString("img"));
            ShopCartBean.GoodsPrice goodsPrice = new ShopCartBean.GoodsPrice();
            Log.e("qqqqqqqqqPPP",bundle.getString("price")+"?");
            goodsPrice.setPrice(Integer.parseInt(bundle.getString("price")));
            shopCartBean.setGoods(goods);
            shopCartBean.setGoodsPrice(goodsPrice);
            shopCartBean.setQuantity(Integer.parseInt(bundle.getString("num")));
            mGoPayList.add(shopCartBean);
            confListAdapter = new ConfListAdapter(ShopConfActivity.this, mGoPayList);
            recyclerView.setAdapter(confListAdapter);
            Map<String, Object> map = new HashMap<>();
            map.put("goodsId", bundle.getString("goodsId"));
            map.put("num", bundle.getString("num"));
            map.put("priceId", bundle.getString("priceId"));
            weight=Double.parseDouble(bundle.getString("weight"));
            mlist.add(map);
        }
        money = bundle.getInt("money");
        tvShopPrice.setText("¥" + money);
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
                    if(type.equals("DingDan")){

                    }
                    Map<String, Object> params = new HashMap<>();
                    params.put("receiveId", receiveId);
                    //支付方式  1：支付宝
                    params.put("paymentId", 1);
                    params.put("orderDetailsList", mlist);
                    //邮费
                    params.put("postFee", post_fee);
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
                    if (result.length() < 6) {
                        code=result;
                    }
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
                    tvConfAddress.setText("地址");
                    tvConfName.setText("姓名");
                    tvConfTel.setText("电话");
                    }
            }
        }
    }


    int post_fee ;
    List<PostFreeBean> postFreeBeans=new ArrayList<>();
    class getPostFeeAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "logistics/getPostFee";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code=result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    returnData = jsonObject.getString("returnData");
                    if (!Utils.isEmpty(returnData)) {
                        JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                        JsonArray list = content.getAsJsonArray("RecommendDetail");
                        Gson gson = new Gson();

                        for(int i=0;i<list.size();i++){
                            JsonElement use = list.get(i);
                            PostFreeBean userList = gson.fromJson(use, PostFreeBean.class);
                            postFreeBeans.add(userList);
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
//
                isPrice=true;
                 post_fee=(int)((postFreeBeans.get(0).getExpressList().get(0).getFee()*10+5)/10);
                tvYunfei.setText("+¥"+post_fee);
                tvMoney.setText("¥" + (money+post_fee));
//                tvMoney=
                if(isPrice&&isShopData)
                    MyDialog.closeDialog(dialog);
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

    String orderId,orderNumber;

    class postOrderAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "order/addOrder";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code=result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    if (!Utils.isEmpty(code) && "100".equals(code)) {
                        orderNumber = jsonObject.getString("returnData");
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
                if(sign_pay==2){
                    Toast.makeText(mContext,"银联",Toast.LENGTH_SHORT).show();
                }else {
                    if (sign_pay == 0) {
                        Intent intent = new Intent(ShopConfActivity.this, PayActivity.class);
                        intent.putExtra("orderNumber", orderNumber);
                        startActivity(intent);
                    } else if (sign_pay == 1) {
                        Intent intent = new Intent(ShopConfActivity.this, WXPayActiviy.class);
                        intent.putExtra("orderNumber", orderNumber);
                        startActivity(intent);
                    }

                    finish();
                }

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

    List<OrderBean.OrderDetailsList> orderDetailsLists = new ArrayList<>();
    class getOrderAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "/order/getOrderByOrderNumber";
            url = url + "?orderNumber=" +orderNumber;
            String result = HttpUtils.doGet(mContext, url);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");

                    JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                    JsonArray list = content.getAsJsonArray("orderDetailsList");
                    Gson gson = new Gson();
                    for (JsonElement user : list) {
                        //通过反射 得到UserBean.class
                        OrderBean.OrderDetailsList userList = gson.fromJson(user, OrderBean.OrderDetailsList.class);
                        orderDetailsLists.add(userList);
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
                isShopData=true;
                dingDanXQAdapter.notifyDataSetChanged();

                if(isShopData&&isAddress)
                MyDialog.closeDialog(dialog);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        Map<String, Object> params = new HashMap<>();
        SharedPreferences userSettings = getSharedPreferences("my", 0);
        String url = userSettings.getString("userId", "1000");
        params.put("userId", url);
        new getAddressAsync().execute(params);
    }
}
