package com.xr.happyFamily.bao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
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
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.DingdanAdapter;
import com.xr.happyFamily.bean.OrderBean;
import com.xr.happyFamily.bean.OrderListBean;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONException;
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

public class ShopDingdanActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.view4)
    View view4;
    @BindView(R.id.tv5)
    TextView tv5;
    @BindView(R.id.view5)
    View view5;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    int sing_title = 0;
    View[] view_title;
    TextView[] tv_title;
    DingdanAdapter dingdanAdapter;
    Context mContext;
    List<OrderBean.OrderDetailsList> orderDetailsLists;

    List<OrderListBean.myList> orderBeans = new ArrayList<>();
    @BindView(R.id.swipe_content)
    PullToRefreshLayout swipeContent;
    private MyDialog dialog;
    int page = 1, lastSign = 0;
    boolean isLoading=false,isRefresh=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        MyApplication application = (MyApplication) getApplication();
        application.addActivity(this);
        setContentView(R.layout.activity_shop_dingdan);
        ButterKnife.bind(this);
        mContext = ShopDingdanActivity.this;

        titleText.setText("我的订单");
        titleRightText.setVisibility(View.GONE);
        view_title = new View[]{view1, view2, view3, view4, view5};
        tv_title = new TextView[]{tv1, tv2, tv3, tv4, tv5};
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(linearLayoutManager);
//      获取数据，向适配器传数据，绑定适配器
        orderDetailsLists = new ArrayList<>();
        dingdanAdapter = new DingdanAdapter(ShopDingdanActivity.this, orderDetailsLists);
        recyclerview.setAdapter(dingdanAdapter);


        swipeContent.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                page=1;
                isRefresh=true;
                orderDetailsLists.clear();
                getDingDan(lastSign, page);
            }

            @Override
            public void loadMore() {
                page++;
                isLoading=true;
                getDingDan(lastSign, page);
            }
        });
        //      调用按钮返回事件回调的方法
        dingdanAdapter.buttonSetOnclick(new DingdanAdapter.ButtonInterface() {
            @Override
            public void onclick(View view, int position) {
                dingdanAdapter.setDefSelect(position);
            }
        });

        getDingDan(0, page);

    }


    @OnClick({R.id.back, R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
//                if(isFastClick())
                finish();
                break;
            case R.id.tv1:
//                if(isFastClick())
                lastSign = 0;
                page = 1;
                upData(lastSign, "全部");
                break;
            case R.id.tv2:
                lastSign = 1;
                page = 1;
//                if(isFastClick())
                upData(lastSign, "待付款");
                break;
            case R.id.tv3:
                lastSign = 2;
                page = 1;
//                if(isFastClick())
                upData(lastSign, "待收货");
                break;
            case R.id.tv4:
                lastSign = 3;
                page = 1;
//                if(isFastClick())
                upData(lastSign, "已收货");
                break;

            case R.id.tv5:
//                if(isFastClick())
                lastSign = 4;
                page = 1;
                upData(lastSign, "退款/售后");
                break;

        }
    }

    private void upData(int i, String title) {


        orderDetailsLists.clear();
        orderBeans.clear();
        view_title[sing_title].setVisibility(View.INVISIBLE);
        tv_title[sing_title].setTextColor(Color.parseColor("#000000"));
        sing_title = i;
        view_title[sing_title].setVisibility(View.VISIBLE);
        tv_title[sing_title].setTextColor(Color.parseColor("#4FBA72"));
//        datas.clear();
//        datas.addAll(initData(title));
//        dingdanAdapter.notifyDataSetChanged();
        getDingDan(sing_title, page);
    }

    dingDanAsync dingDanAsync;

    public void getDingDan(int state, int page) {
        dialog = MyDialog.showDialog(mContext);

        dialog.show();
        Map<String, Object> params = new HashMap<>();
        SharedPreferences userSettings = mContext.getSharedPreferences("my", 0);
        String userId = userSettings.getString("userId", "userId");
        params.put("userId", userId);
        params.put("pageNum", page);
        if (state == 1)
            params.put("state", 1);
        else if (state == 2)
            params.put("state", 23);
        else if (state == 3)
            params.put("state", 4);
        else if (state == 4)
            params.put("state", 5);
        params.put("pageRow", "10");
        if (dingDanAsync != null && dingDanAsync.getStatus() == AsyncTask.Status.RUNNING) {
            dingDanAsync.cancel(true);
        } else {
            dingDanAsync = new dingDanAsync();
            dingDanAsync.execute(params);
        }

    }

    String orderId;


    class dingDanAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            Map<String, Object> params = maps[0];
            String url = "/order/getOrderByPage?";
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                url = url + entry.getKey() + "=" + entry.getValue() + "&";
            }
            url = url.substring(0, url.length() - 1);
            Log.e("qqqqqqEEE", url);
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
                    if (returnData.get("list").toString() != "null") {
                        JsonArray list = content.getAsJsonArray("list");

//                            JsonArray list = content.getAsJsonArray("returnData");
                        Gson gson = new Gson();
                        orderBeans.clear();

                        Log.e("qqqqqqqEEE", list.size() + "?");
                        for (int i = 0; i < list.size(); i++) {
//                        //通过反射 得到UserBean.class
                            JsonElement user = list.get(i);
                            Log.e("qqqqqqqEEE", i + ":" + user.toString());
                            OrderListBean.myList userList = gson.fromJson(user, OrderListBean.myList.class);
                            orderBeans.add(userList);
                            orderId = orderBeans.get(i).getOrderNumber();
                            Map<String, Object> map = new HashMap<>();
                            map.put("orderId", orderId);
                            map.put("state", orderBeans.get(i).getState());
                            map.put("time", orderBeans.get(i).getCreateTime());


//                            map.put("refundTime", userList.getRefundTime());
                            new getOrderAsync().execute(map);
                            SystemClock.sleep(50);
                        }
                    } else {

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return code;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!Utils.isEmpty(s) && "100".equals(s)) {
                if (orderBeans.size() == 0) {
                    MyDialog.closeDialog(dialog);
                    dingdanAdapter.notifyDataSetChanged();
                    if (page > 1)
                        page--;
                    Toast.makeText(ShopDingdanActivity.this, "无更多数据", Toast.LENGTH_SHORT).show();
                    if(isLoading) {
                        isLoading=false;
                        swipeContent.finishLoadMore();
                    }
                    if (isRefresh) {
                        isRefresh=false;
                        swipeContent.finishRefresh();
                    }
                }
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

    Boolean isFinish = false;

    class getOrderAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {


            Map<String, Object> params = maps[0];
            String url = "/order/getOrderByOrderNumber";
            url = url + "?orderNumber=" + params.get("orderId").toString();
            String state = params.get("state").toString();
            String time = params.get("time").toString();
            String result = HttpUtils.doGet(mContext, url);


            String code = "";

            try {
                if (orderBeans.get(orderBeans.size() - 1).getOrderNumber().equals(params.get("orderId").toString())) {
                    isFinish = true;
                }
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
                    for (int i = 0; i < list.size(); i++) {
                        //通过反射 得到UserBean.class
                        JsonElement use = list.get(i);
                        OrderBean.OrderDetailsList userList = gson.fromJson(use, OrderBean.OrderDetailsList.class);
                        if (i == 0)
                            userList.setFirst(true);
                        else
                            userList.setFirst(false);
                        if (i == list.size() - 1) {
                            userList.setFinish(true);
                        } else {
                            userList.setFinish(false);
                        }
                        userList.setOrderId(Integer.parseInt(returnData.get("orderId").toString()));
                        userList.setState(state);
                        userList.setTime(time);
                        Log.e("qqqqqqqqqII", returnData.getInt("isRate") + "??");
                        userList.setIsRate(returnData.getInt("isRate"));
                        if (state.equals("5")) {
                            JSONObject orderRefund = returnData.getJSONObject("orderRefund");
                            userList.setRefundTime(returnData.getString("refundTime"));
                            if (orderRefund.get("refundState") != null)
                                userList.setRefundState(orderRefund.getInt("refundState"));
                        }

                        userList.setLogisticCode(returnData.getString("logisticCode"));
                        userList.setShipperCode(returnData.getString("shipperCode"));
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

                if (isFinish) {
                    MyDialog.closeDialog(dialog);
                    dingdanAdapter.notifyDataSetChanged();
                    if(isLoading) {
                        isLoading=false;
                        swipeContent.finishLoadMore();
                    }
                    if (isRefresh) {
                        isRefresh=false;
                        swipeContent.finishRefresh();
                    }
                }
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


//    private static final int MIN_DELAY_TIME= 10;  // 两次点击间隔不能少于1000ms
//    private static long lastClickTime;
//
//    public static boolean isFastClick() {
//        boolean flag = true;
//        long currentClickTime = System.currentTimeMillis();
//        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
//            flag = false;
//        }
//        lastClickTime = currentClickTime;
//        return flag;
//    }


    @Override
    protected void onRestart() {
        super.onRestart();

        upData(lastSign, "返回刷新");
    }
}
