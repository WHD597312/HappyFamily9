package com.xr.happyFamily.bao;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.WaterFallAdapter;
import com.xr.happyFamily.bao.bean.Receive;
import com.xr.happyFamily.bean.PersonCard;
import com.xr.happyFamily.bean.ShopBean;
import com.xr.happyFamily.main.MainActivity;
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

public class PaySuccessActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.ll_nodata)
    LinearLayout llNodata;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.gv_more)
    GridView gvMore;
    @BindView(R.id.ll_gv)
    LinearLayout llGv;
    @BindView(R.id.tv_chakan)
    TextView tvChakan;
    @BindView(R.id.tv_shouye)
    TextView tvShouye;
    private RecyclerView.LayoutManager mLayoutManager;
    private WaterFallAdapter shopAdapter;

    private String[] from = {"title"};
    private int[] to = {R.id.tv_search};
    String[] titles = new String[]{"从低到高", "从高到低"};
//    String[] titles2 = new String[]{"松下", "小米", "海尔", "格力", "松下"};
    SimpleAdapter jiageAdapter;
    SimpleAdapter pinpaiAdapter;
    private boolean isMore = false;
    private int sign = 0;
    private ImageView[] img;
    private List<ShopBean.ReturnData.MyList> list_shop;

    String orderId;
    Context mContext;

    int lastVisibleItem = 0,page=1;
    boolean isData=true;
    private MyDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mContext=PaySuccessActivity.this;
        setContentView(R.layout.activity_shop_pay_success);
        ButterKnife.bind(this);
//        orderId=getIntent().getExtras().getString("orderId");
        Bundle bundle=getIntent().getExtras();

        orderId=bundle.getString("orderNumber");

        init();

    }

    private void init() {
        titleText.setText("支付成功");
        back.setVisibility(View.GONE);
        Map<String,Object> map=new HashMap<>();
        new getOrderAsync().execute(map);

        list_shop = new ArrayList<>();
//        list_shop = buildData("电暖器");
        //设置布局管理器为2列，纵向
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        shopAdapter = new WaterFallAdapter(this, list_shop);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setAdapter(shopAdapter);

        jiageAdapter = new SimpleAdapter(this, getList(),
                R.layout.item_search_result, from, to);
//        pinpaiAdapter = new SimpleAdapter(this, getList2(),
//                R.layout.item_search_result, from, to);
        gvMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (sign == 1) {
                    list_shop.clear();
//                    list_shop.addAll(buildData(titles[position]));
                    shopAdapter.notifyDataSetChanged();

                } else {
                    list_shop.clear();
//                    list_shop.addAll(buildData(titles2[position]));
                    shopAdapter.notifyDataSetChanged();
                }
                llGv.setVisibility(View.GONE);
            }
        });

        getShopData(lastVisibleItem,1);
    }



    @OnClick({R.id.tv_chakan,R.id.tv_shouye})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_chakan:
                Intent intent=new Intent(this, ShopDingdanXQActivity.class);
                intent.putExtra("orderId",orderId);
                startActivity(intent);
                break;
            case R.id.tv_shouye:
                Intent intent1=new Intent(this, MainActivity.class);
                intent1.putExtra("sign","PaySuccess");
                startActivity(intent1);
                break;

        }
    }

    public List<Map<String, Object>> getList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        for (int i = 0; i < 2; i++) {
            map = new HashMap<String, Object>();
            map.put("title", titles[i]);
            list.add(map);
        }
        return list;
    }

//    public List<Map<String, Object>> getList2() {
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        Map<String, Object> map = null;
//
//
//        for (int i = 0; i < 5; i++) {
//            map = new HashMap<String, Object>();
//            map.put("title", titles2[i]);
//            list.add(map);
//        }
//        return list;
//    }
//





    String money;
    class getOrderAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "order/getOrderByOrderNumber";
            url=url+"?orderNumber="+orderId;
            String result = HttpUtils.doGet(mContext, url);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    money = returnData.get("paidAmount").toString();

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
                if (tvPrice!=null)
                tvPrice.setText(money+"");

            }
        }
    }








    private void getShopData(int id,int page){
        dialog = MyDialog.showDialog(mContext);
        dialog.show();
        Map<String, Object> params = new HashMap<>();
        params.put("categoryId", id + "");
        params.put("pageNum", page + "");
        params.put("pageRow", "6");
        new ShopAsync().execute(params);
    }

    class ShopAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            Map<String, Object> params = maps[0];
            String url = "goods/getGoodsByGoodsCategory";
            String result = HttpUtils.myPostOkHpptRequest(mContext, url, params);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                    JsonArray list = content.getAsJsonArray("list");
                    if ("100".equals(code)) {
                        Gson gson = new Gson();
                        if(list.size()>0) {
                            isData=true;
                            for (JsonElement user : list) {
                                //通过反射 得到UserBean.class
                                ShopBean.ReturnData.MyList userList = gson.fromJson(user, ShopBean.ReturnData.MyList.class);
                                list_shop.add(userList);
                            }
                        }else {
                            page--;
                            isData=false;
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
                if (shopAdapter!=null)
                shopAdapter.notifyDataSetChanged();
                if(!isData){
                    Toast.makeText(PaySuccessActivity.this,"无更多数据",Toast.LENGTH_SHORT).show();
                }
                if (dialog!=null)
                MyDialog.closeDialog(dialog);
            }
        }
    }
}
