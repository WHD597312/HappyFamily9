package com.xr.happyFamily.bao;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xr.happyFamily.R;

import com.xr.happyFamily.bao.adapter.DingDanXQAdapter;
import com.xr.happyFamily.bean.OrderBean;
import com.xr.happyFamily.le.view.KeywordsFlow;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by win7 on 2018/5/22.
 */

public class TestActivity extends AppCompatActivity {

    private KeywordsFlow keywordsFlow;
    private String[] keywords;

    DingDanXQAdapter confListAdapter;
    List<OrderBean.OrderDetailsList> orderDetailsLists=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        confListAdapter = new DingDanXQAdapter(TestActivity.this, orderDetailsLists);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(confListAdapter);
        Map<String, Object> map = new HashMap<>();
        new getOrderAsync().execute(map);
    }

    String wuliu,wuliu_time,name,address,tel,postFee,paidAmount,orderNumber,paymentId;
    //发货时间  创建时间  付款时间
    String sendTime , createTime ,paymentTime;

    class getOrderAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {


            String result = "{\"returnCode\":\"100\",\"returnMsg\":\"请求成功\",\"returnData\":{\"orderId\":249,\"orderNumber\":\"H201806191500004\",\"user\":{\"userId\":0,\"username\":null,\"phone\":\"123456\",\"password\":null,\"birthday\":null,\"sex\":false,\"oAuthType\":null,\"oAuthId\":null,\"headImgUrl\":null},\"totalAmount\":3600,\"paidAmount\":3600,\"discountAmount\":null,\"postFee\":null,\"receive\":{\"receiveId\":null,\"userId\":null,\"contact\":\"那姣龙\",\"tel\":\"13750846823\",\"receiveProvince\":\"黑龙江\",\"receiveCity\":\"绥化\",\"receiveCounty\":\"海伦\",\"receiveAddress\":\"八字桥\",\"isDefault\":null,\"delState\":null,\"createTime\":null,\"updateTime\":null},\"sendTime\":null,\"paymentTime\":null,\"logisticsState\":1,\"invoiceTag\":0,\"comment\":null,\"delState\":2,\"payment\":null,\"paymentSeq\":null,\"payState\":1,\"state\":1,\"createTime\":\"2018-06-19 10:51:28\",\"updateTime\":\"2018-06-19 10:51:28\",\"userId\":null,\"paymentId\":null,\"receiveId\":null,\"orderDetailsList\":[{\"orderDetailsId\":411,\"orderId\":null,\"orderNumber\":\"H201806191500004\",\"goodsId\":1,\"goodsName\":\"电暖器\",\"num\":3,\"priceId\":6,\"detailsAmount\":2700,\"createTime\":null,\"image\":\"http://p9zaf8j1m.bkt.clouddn.com/good/1.png\",\"simpleDescribe\":\"对流式加热,通电加热,无感静音,双重安装保护\",\"price\":900},{\"orderDetailsId\":412,\"orderId\":null,\"orderNumber\":\"H201806191500004\",\"goodsId\":4,\"goodsName\":\"净水器\",\"num\":1,\"priceId\":4,\"detailsAmount\":900,\"createTime\":null,\"image\":\"http://p9zaf8j1m.bkt.clouddn.com/good/4.png\",\"simpleDescribe\":\"对流式加热,通电加热,无感静音,双重安装保护\",\"price\":900}],\"orderCertify\":null,\"startTime\":null,\"endTime\":null,\"startAmount\":null,\"endAmount\":null,\"phone\":\"123456\",\"logisticCode\":null,\"shipperCode\":null}}\n" +
                    "06-20 12:07:03.829 24786-24820/com.xr.happyFamily E/qqqqqqqq: {\"returnCode\":\"100\",\"returnMsg\":\"请求成功\",\"returnData\":{\"orderId\":249,\"orderNumber\":\"H201806191500004\",\"user\":{\"userId\":0,\"username\":null,\"phone\":\"123456\",\"password\":null,\"birthday\":null,\"sex\":false,\"oAuthType\":null,\"oAuthId\":null,\"headImgUrl\":null},\"totalAmount\":3600,\"paidAmount\":3600,\"discountAmount\":null,\"postFee\":null,\"receive\":{\"receiveId\":null,\"userId\":null,\"contact\":\"那姣龙\",\"tel\":\"13750846823\",\"receiveProvince\":\"黑龙江\",\"receiveCity\":\"绥化\",\"receiveCounty\":\"海伦\",\"receiveAddress\":\"八字桥\",\"isDefault\":null,\"delState\":null,\"createTime\":null,\"updateTime\":null},\"sendTime\":null,\"paymentTime\":null,\"logisticsState\":1,\"invoiceTag\":0,\"comment\":null,\"delState\":2,\"payment\":null,\"paymentSeq\":null,\"payState\":1,\"state\":1,\"createTime\":\"2018-06-19 10:51:28\",\"updateTime\":\"2018-06-19 10:51:28\",\"userId\":null,\"paymentId\":null,\"receiveId\":null,\"orderDetailsList\":[{\"orderDetailsId\":411,\"orderId\":null,\"orderNumber\":\"H201806191500004\",\"goodsId\":1,\"goodsName\":\"电暖器\",\"num\":3,\"priceId\":6,\"detailsAmount\":2700,\"createTime\":null,\"image\":\"http://p9zaf8j1m.bkt.clouddn.com/good/1.png\",\"simpleDescribe\":\"对流式加热,通电加热,无感静音,双重安装保护\",\"price\":900},{\"orderDetailsId\":412,\"orderId\":null,\"orderNumber\":\"H201806191500004\",\"goodsId\":4,\"goodsName\":\"净水器\",\"num\":1,\"priceId\":4,\"detailsAmount\":900,\"createTime\":null,\"image\":\"http://p9zaf8j1m.bkt.clouddn.com/good/4.png\",\"simpleDescribe\":\"对流式加热,通电加热,无感静音,双重安装保护\",\"price\":900}],\"orderCertify\":null,\"startTime\":null,\"endTime\":null,\"startAmount\":null,\"endAmount\":null,\"phone\":\"123456\",\"logisticCode\":null,\"shipperCode\":null}}";
            try {
                if (!com.xr.happyFamily.login.util.Utils.isEmpty(result)) {
                    Log.e("qqqqqqqq", result);
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    Log.e("qqqqqqqM", returnData.get("paidAmount").toString());

                    JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                    JsonArray list = content.getAsJsonArray("orderDetailsList");
                    Gson gson = new Gson();

                    Log.e("qqqqqqqqqSIze", list.size() + "???");
                    for (JsonElement user : list) {
                        //通过反射 得到UserBean.class
                        OrderBean.OrderDetailsList userList = gson.fromJson(user, OrderBean.OrderDetailsList.class);
                        orderDetailsLists.add(userList);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "100";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!com.xr.happyFamily.login.util.Utils.isEmpty(s) && "100".equals(s)) {
                confListAdapter.notifyDataSetChanged();
            }
        }
    }

}