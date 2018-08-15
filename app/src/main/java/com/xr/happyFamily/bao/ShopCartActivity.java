package com.xr.happyFamily.bao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.ShopCartAdapter;
import com.xr.happyFamily.bao.base.ToastUtil;
import com.xr.happyFamily.bao.bean.ShoppingCart;
import com.xr.happyFamily.bean.ShopBean;
import com.xr.happyFamily.bean.ShopCartBean;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;


//购物车界面

public class ShopCartActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.tv_sum)
    TextView tvSum;

    private TextView tvShopCartSubmit, tvShopCartSelect;
    private Context mContext;
    private RecyclerView rlvShopCart;
    private ShopCartAdapter mShopCartAdapter;
    private List<ShopCartBean> mAllOrderList = new ArrayList<>();
    private ArrayList<ShopCartBean> mGoPayList = new ArrayList<>();
    private TextView tvShopCartTotalPrice;
    private int mTotalPrice1;
    private boolean mSelect, isEdit = false;
    private MyDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shopcart);
        mContext = ShopCartActivity.this;
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        MyApplication application = (MyApplication) getApplication();
        application.addActivity(this);
       getData();
        titleText.setText("购物车");
        tvShopCartSelect = (TextView) findViewById(R.id.tv_shopcart_addselect);
        tvShopCartTotalPrice = (TextView) findViewById(R.id.tv_shopcart_totalprice);
        rlvShopCart = (RecyclerView) findViewById(R.id.rlv_shopcart);
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//        llPay.setLayoutParams(lp);

        tvShopCartSubmit = (TextView) findViewById(R.id.tv_shopcart_submit);

        rlvShopCart.setLayoutManager(new LinearLayoutManager(this));
        mShopCartAdapter = new ShopCartAdapter(this, mAllOrderList);
        rlvShopCart.setAdapter(mShopCartAdapter);
        //删除商品接口
        mShopCartAdapter.setOnDeleteClickListener(new ShopCartAdapter.OnDeleteClickListener() {


            @Override
            public void onDeleteClick(View view, int position, int cartid) {

            }
        });

        mShopCartAdapter.setOnItemClickListener(onItemOnClickListener);
        //实时监控全选按钮
        mShopCartAdapter.setResfreshListener(new ShopCartAdapter.OnResfreshListener() {
            @Override
            public void onResfresh(boolean isSelect) {
                mSelect = isSelect;
                if (isSelect) {
                    Drawable left = ContextCompat.getDrawable(mContext, R.mipmap.xuanzhong_shop3x);
                    tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                } else {
                    Drawable left = ContextCompat.getDrawable(mContext, R.mipmap.weixuanzhong3x);
                    tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                }
                int mTotalPrice = 0;
                int mTotalNum = 0;
                mTotalPrice1 = 0;
                mGoPayList.clear();
                for (int i = 0; i < mAllOrderList.size(); i++)
                    if (mAllOrderList.get(i).getIsSelect()) {
                        mTotalPrice += mAllOrderList.get(i).getGoodsPrice().getPrice() * mAllOrderList.get(i).getQuantity();
                        mTotalNum += 1;
                        mGoPayList.add(mAllOrderList.get(i));
                    }
                mTotalPrice1 = mTotalPrice;

                Log.e("qqqqqqqqqqqqPPP",mTotalPrice+"???");
                tvShopCartTotalPrice.setText("¥"+mTotalPrice);
            }
        });

        //全选
        tvShopCartSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelect = !mSelect;
                if (mSelect) {
                    Drawable left = ContextCompat.getDrawable(mContext, R.mipmap.xuanzhong_shop3x);
                    tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                    for (int i = 0; i < mAllOrderList.size(); i++) {
                        mAllOrderList.get(i).setSelect(true);
                        mAllOrderList.get(i).setShopSelect(true);
                    }
                } else {
                    Drawable left = ContextCompat.getDrawable(mContext, R.mipmap.weixuanzhong3x);
                    tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                    for (int i = 0; i < mAllOrderList.size(); i++) {
                        mAllOrderList.get(i).setSelect(false);
                        mAllOrderList.get(i).setShopSelect(false);
                    }
                }
                mShopCartAdapter.notifyDataSetChanged();

            }
        });


    }

    ShopCartAdapter.OnItemOnClickListener onItemOnClickListener = new ShopCartAdapter.OnItemOnClickListener() {
        @Override
        public void onItemOnClick(View view, int pos) {
            Intent intent = new Intent(ShopCartActivity.this, ShopXQActivity.class);
            intent.putExtra("goodsId", mAllOrderList.get(pos).getGoods().getGoodsId() + "");
            startActivity(intent);
        }

        @Override
        public void onItemLongOnClick(View view, int pos) {

//            mPosition = pos;
            Toast.makeText(ShopCartActivity.this, "点击" + pos, Toast.LENGTH_SHORT).show();

        }
    };


    public static void isSelectFirst(List<ShopCartBean> list) {
        if (list.size() > 0) {
            list.get(0).setIsFirst(1);
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i).getShopId() == list.get(i - 1).getShopId()) {
                    list.get(i).setIsFirst(2);
                } else {
                    list.get(i).setIsFirst(1);
                }
            }
        }

    }


    @OnClick({R.id.back, R.id.title_rightText, R.id.tv_shopcart_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.title_rightText:
                upDataUI();
                break;
            case R.id.tv_shopcart_submit:
                String s = "";
                if (!isEdit) {
//                    s = "选中支付";
                    if(mGoPayList.size()>0) {
                        Intent intent = new Intent(ShopCartActivity.this, ShopConfActivity.class);
                        intent.putExtra("type",  "Cart");
                        intent.putExtra("mGoPayList", (Serializable) mGoPayList);
                        intent.putExtra("money", mTotalPrice1);
                        double weight=0;
                        for(int i=0;i<mGoPayList.size();i++){
                            double w=mGoPayList.get(i).getGoods().getWeight();
                            int num=mGoPayList.get(i).getQuantity();
                            weight=weight+w*num;
                        }
                        intent.putExtra("weight", weight);
                        startActivity(intent);
                    }else  ToastUtil.showShortToast("请选择购买商品");



                } else {
//                    s = "删除";
                    showPopup();

                }


                break;
        }
    }

    public void upDataUI() {
        if(mAllOrderList.size()==0){
            Toast.makeText(mContext,"购物车内暂无商品",Toast.LENGTH_SHORT).show();
        }else {
            if (!isEdit) {
                titleRightText.setText("完成");
                tvShopCartSubmit.setText("删除");
                tvSum.setVisibility(View.GONE);
                tvShopCartTotalPrice.setVisibility(View.GONE);
                isEdit = true;
            } else {
                titleRightText.setText("编辑");
                tvShopCartSubmit.setText("狠心买");
                tvSum.setVisibility(View.VISIBLE);
                tvShopCartTotalPrice.setVisibility(View.VISIBLE);
                isEdit = false;
            }
        }
    }

    List<ShopCartBean> shoppingCartList;

    class addShopAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            shoppingCartList = new ArrayList<>();
            String url = "shoppingcart/queryShoppingCartInfo";
            String result = HttpUtils.doGet(mContext, url);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
//                    if("null".equals(returnData.getJSONObject("shoppingCartItem").toString())) {
//                    }else {
                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();

                    if ( returnData.get("shoppingCartItem").toString()!="null") {

                        JsonArray list = content.getAsJsonArray("shoppingCartItem");
//                            JsonArray list = content.getAsJsonArray("returnData");
                        Gson gson = new Gson();
                        for (JsonElement user : list) {
                            //通过反射 得到UserBean.class
                            ShopCartBean userList = gson.fromJson(user, ShopCartBean.class);
                            userList.setIsCoupon(false);
                            userList.setSelect(false);
                            mAllOrderList.add(userList);
                        }
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
                MyDialog.closeDialog(dialog);
                mShopCartAdapter.notifyDataSetChanged();
                if(mAllOrderList.size()==0){
                    Log.e("qqqqqqqqMMM","没了");
                    Drawable left = ContextCompat.getDrawable(mContext, R.mipmap.weixuanzhong3x);
                    tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                    mSelect=false;
                    tvShopCartTotalPrice.setText("0.0");
                }
            } else if (!Utils.isEmpty(s) && "10005".equals(s)) {
                startActivity(new Intent(ShopCartActivity.this, LoginActivity.class));
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

    class delShopAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            shoppingCartList = new ArrayList<>();
            String url = "shoppingcart/delCurrentUserSomeShoppingCart";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url,params);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code=result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
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
               getData();

            } else if (!Utils.isEmpty(s) && "10005".equals(s)) {
                startActivity(new Intent(ShopCartActivity.this, LoginActivity.class));
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
        getData();
    }




    private void getData(){
        dialog = MyDialog.showDialog(mContext);
        dialog.show();
        mAllOrderList.clear();
        Map<String, Object> params = new HashMap<>();
        new addShopAsync().execute(params);
    }












    private View contentViewSign;
    private PopupWindow mPopWindow;
    private TextView tv_quxiao,tv_queding,tv_context;

    private void showPopup() {
        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_main, null);
        tv_quxiao = (TextView) contentViewSign.findViewById(R.id.tv_quxiao);
        tv_queding = (TextView) contentViewSign.findViewById(R.id.tv_queren);
        tv_context = (TextView) contentViewSign.findViewById(R.id.tv_context);
        ((TextView)contentViewSign.findViewById(R.id.tv_title)).setText("删除商品");
        tv_context.setText("是否删除商品？");
        tv_quxiao.setText("否");
        tv_queding  .setText("是");
//        tv_shangcheng = (TextView) contentViewSign.findViewById(R.id.tv_shangcheng);
//        tv_shopcart.setOnClickListener(this);
        tv_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        tv_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> map=new HashMap<>();
                String a = "";
                for(int i=0;i<mGoPayList.size();i++){
                    String b=mGoPayList.get(i).getGoodsPrice().getPriceId()+"";
                    a=a+b+",";
                }
                a = a.substring(0,a.length() - 1);
                map.put("priceIds",a);
                new delShopAsync().execute(map);
                mPopWindow.dismiss();


            }
        });
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
        mPopWindow.setOnDismissListener(new poponDismissListener());
        mPopWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp); //添加pop窗口关闭事件
    }




    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }

    }

}
