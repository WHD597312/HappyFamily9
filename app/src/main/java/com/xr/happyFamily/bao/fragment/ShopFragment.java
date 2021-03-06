package com.xr.happyFamily.bao.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.squareup.picasso.Picasso;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopAddressActivity;
import com.xr.happyFamily.bao.ShopConfActivity;
import com.xr.happyFamily.bao.ShopXQActivity;
import com.xr.happyFamily.bao.adapter.EvaluateAdapter;
import com.xr.happyFamily.bao.adapter.EvaluateXhAdapter;
import com.xr.happyFamily.bao.base.BaseFragment;
import com.xr.happyFamily.bao.base.ToastUtil;
import com.xr.happyFamily.bao.bean.GoodsPrice;
import com.xr.happyFamily.bao.bean.Receive;
import com.xr.happyFamily.bao.view.FlowTagView;
import com.xr.happyFamily.bao.view.MyHeadRefreshView;
import com.xr.happyFamily.bao.view.MyLoadMoreView;
import com.xr.happyFamily.bean.PostFreeBean;
import com.xr.happyFamily.bao.view.ShopBanner;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by TYQ on 2017/9/7.
 */

public class ShopFragment extends BaseFragment implements View.OnClickListener {

    Unbinder unbinder;
    private static ShopXQActivity father = new ShopXQActivity();
    Context mContext;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.tv_xinghao)
    TextView tvXinghao;

    String goodsId;
    int priceId = 0;
    String receiveId = "0";


    List<GoodsPrice> list_price;

    int num = 1;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.swipe_content)
    PullToRefreshLayout swipeContent;
    @BindView(R.id.view_bottom)
    View viewBottom;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.banner)
    ShopBanner banner;
    @BindView(R.id.img_address)
    ImageView imgAddress;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_shop_shop, container, false);
        unbinder = ButterKnife.bind(this, view);
        getData();
        Calendar c = Calendar.getInstance();//

        swipeContent.setHeaderView(new MyHeadRefreshView(getActivity()));
        swipeContent.setFooterView(new MyLoadMoreView(getActivity()));
        swipeContent.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                swipeContent.finishRefresh();
            }

            @Override
            public void loadMore() {
                swipeContent.finishLoadMore();
                final ShopXQActivity shopXQActivity = (ShopXQActivity) getActivity();
                shopXQActivity.gotoXiangQing();
            }
        });
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
//        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
//
//        Calendar  calendar =Calendar. getInstance();
//        calendar.add( Calendar. DATE, +1); //向前走一天
//        Date date= calendar.getTime();
//        System. out .println("前一天时间为" +date .toString());
//
//
//        tvTime.setText("23.00前下单，预计明天（"+date .toString() +"送达");
//        tvXinghao.setText(power);
//        tvPrice.setText(price);


        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_close:
            case R.id.view_dis:
                mPopWindow.dismiss();
                break;

            default:
                break;
        }
    }


    @OnClick({R.id.tv_xinghao, R.id.tv_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_address:
                startActivityForResult(new Intent(getActivity(), ShopAddressActivity.class), 101);
                break;
            case R.id.tv_xinghao:

                showPopup();
                break;
        }
    }

    /**
     * 所有的Activity对象的返回值都是由这个方法来接收 requestCode:
     * 表示的是启动一个Activity时传过去的requestCode值
     * resultCode：表示的是启动后的Activity回传值时的resultCode值
     * data：表示的是启动后的Activity回传过来的Intent对象
     */
    String address = "没有地址信息，请点击后添加地址信息";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == 111) {
            address = data.getStringExtra("address");// 拿到返回过来的地址
            receiveId = data.getStringExtra("receiveId");
            Log.e("qqqqqqqqRRR", receiveId + "???");
            // 把得到的数据显示到输入框内
            if ((tvAddress != null))
                tvAddress.setText(address);


        }
    }

    public static boolean running = false;

    @Override
    public void onStart() {
        super.onStart();
        running = true;


        SharedPreferences preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
        String price = preferences.getString("jyShopPrice", "");
        String power = preferences.getString("jyShopPower", "");
        if (!Utils.isEmpty(power)) {
            if (!"0".equals(power)) {
                Log.e("qqqqqqqqqPPPP", power);
                tvXinghao.setText("" + power);
                if (!Utils.isEmpty(price))
                    tvPrice.setText("¥" + price);
            }
        }
//        if (sign_type == 1) {
//            viewBottom.setFocusable(true);
//            viewBottom.setFocusableInTouchMode(true);
//            scrollView.post(new Runnable() {
//                public void run() {
//                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
//                }
//            });
//        }

    }

    @Override
    public void onStop() {
        super.onStop();
        running = false;
    }


    @Override
    public void onResume() {
        Map<String, Object> params2 = new HashMap<>();
        params2.put("userId", userId);
        new getAddressAsync().execute(params2);
        super.onResume();
    }

    String power;
    private View contentViewSign, view_dis;
    private PopupWindow mPopWindow;
    private ImageView img_close;
    private EvaluateAdapter adapter_xinghao;
    private FlowTagView labelsView;
    private TextView tv_price, tv_name, tv_jia, tv_jian, tv_num, tv_cart, tv_buy;
    private EvaluateXhAdapter adapter_xh;
    int sign = -1;
    private ImageView img_shop_pic;

    private void showPopup() {


//        priceId=list_price.get(0).getPriceId();
        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_shop_xinghao, null);
        img_close = (ImageView) contentViewSign.findViewById(R.id.img_close);
        view_dis = contentViewSign.findViewById(R.id.view_dis);
        labelsView = (FlowTagView) contentViewSign.findViewById(R.id.labels);
        tv_name = (TextView) contentViewSign.findViewById(R.id.tv_name);
        tv_name.setText(goodsName);
        img_shop_pic = (ImageView) contentViewSign.findViewById(R.id.img_shop_pic);
        Picasso.with(getActivity()).load(headerImg).into(img_shop_pic);
        adapter_xh = new EvaluateXhAdapter(context, R.layout.item_xinghao);
        list = new ArrayList<>();
        for (int i = 0; i < list_price.size(); i++) {
            list.add(list_price.get(i).getPower() + "");
        }
        adapter_xh.setItems(list);
        Log.e("qqqqqZZZZ222", sign + "???");
        if (sign != -1) {
            adapter_xh.setSelection(sign);
        }
        labelsView.setAdapter(adapter_xh);
        labelsView.setOne();
        labelsView.setItemClickListener(new FlowTagView.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                sign = position;
                adapter_xh.setSelection(position);
                adapter_xh.notifyDataSetChanged();
                String e = adapter_xh.getItem(position).toString();
                tv_price.setText("¥" + list_price.get(position).getPrice());
                tvPrice.setText("¥" + list_price.get(position).getPrice());
                power = list.get(position);
                tvXinghao.setText(list.get(position));
                price = list_price.get(position).getPrice() + "";
                priceId = list_price.get(position).getPriceId();
                goodsId = list_price.get(position).getGoodsId() + "";

                SharedPreferences preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("jyShopPrice", price);
                editor.putString("jyShopPower", power);
                editor.commit();

//                Toast.makeText(mContext, "i am:" + e, Toast.LENGTH_SHORT).show();
//                datas.clear();
//                datas.addAll(initData(e));
//                pinglunAdapter.notifyDataSetChanged();

            }
        });

        tv_price = (TextView) contentViewSign.findViewById(R.id.tv_price);
        tv_name = (TextView) contentViewSign.findViewById(R.id.tv_name);
        tv_jian = (TextView) contentViewSign.findViewById(R.id.tv_shop_reduce);
        tv_num = (TextView) contentViewSign.findViewById(R.id.tv_shop_num);
        tv_num.setText(num + "");
        tv_jia = (TextView) contentViewSign.findViewById(R.id.tv_shop_add);
        tv_cart = (TextView) contentViewSign.findViewById(R.id.tv_type_cart);
        tv_buy = (TextView) contentViewSign.findViewById(R.id.tv_type_bug);
        tv_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sign != -1) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("priceId", priceId);
                    params.put("quantity", num);
                    new addShopAsync().execute(params);
                } else {
                    ToastUtil.showShortToast("请选择商品规格");
                }

            }
        });
        tv_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sign != -1) {
                    Intent intent = new Intent(mContext, ShopConfActivity.class);
                    intent.putExtra("type", "XQ");
                    intent.putExtra("goodsId", goodsId);
                    intent.putExtra("num", num + "");
                    intent.putExtra("priceId", priceId + "");
                    intent.putExtra("money", Integer.parseInt(price) * num);
                    intent.putExtra("weight", weight * num + "");
                    intent.putExtra("price", price);
                    intent.putExtra("context", type);
                    intent.putExtra("img", img);
                    intent.putExtra("name", name);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "请选择商品规格", Toast.LENGTH_SHORT).show();
                }
            }
        });
        try {
            if (jsonObject != null)
                tv_name.setText(jsonObject.getString("goodsName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (sign != -1) {
            tv_price.setText("¥" + list_price.get(sign).getPrice());
        } else {
            tv_price.setText("");
        }
//        tv_shangcheng = (TextView) contentViewSign.findViewById(R.id.tv_shangcheng);
//        tv_shopcart.setOnClickListener(this);
        img_close.setOnClickListener(this);
        view_dis.setOnClickListener(this);
        tv_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num > 1) {
                    num--;
                    tv_num.setText(num + "");
                }
            }
        });
        tv_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num++;
                tv_num.setText(num + "");
            }
        });
        mPopWindow = new PopupWindow(contentViewSign);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
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
        mPopWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//        initView();
//        initData();

    }

    List<String> list;


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp); //添加pop窗口关闭事件
    }


    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }

    }

    int pos = 0;


    JSONObject jsonObject;
    String name, type, img, price = "0", goodsName, headerImg;
    Double weight;


    class getShopAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            Map<String, Object> params = maps[0];
            String url = "goods/getGoodsById";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
            String code = "";

            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    }
                    jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                    JsonArray list = content.getAsJsonArray("goodsPrice");
                    headerImg = returnData.getString("headerImg");
                    goodsName = returnData.getString("goodsName");
                    if ("100".equals(code)) {
                        Gson gson = new Gson();
                        for (JsonElement user : list) {
                            //通过反射 得到UserBean.class
                            GoodsPrice userList = gson.fromJson(user, GoodsPrice.class);
                            list_price.add(userList);
                            img = returnData.getString("headerImg");
                            name = returnData.getString("goodsName");
                            type = returnData.getString("simpleDescribe");
                            weight = returnData.getDouble("weight");
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
                if (tvName != null)
                    tvName.setText(name);
                if (tvWeight != null)
                    tvWeight.setText(weight + "kg");
                if (tvType != null)
                    tvType.setText(type);

                Log.e("qqqqqqqPPPP", img + "?");
                String imgStr[]=img.split(",");
                List<String> stringB = Arrays.asList(imgStr);
                if (banner != null) {
                    //设置图片加载器，图片加载器在下方
                    banner.setImageLoader(new MyLoader());
                    //设置图片网址或地址的集合
                    banner.setImages(stringB);
                    banner.isAutoPlay(false);
                    //设置指示器的位置，小点点，左中右。
                    banner.setIndicatorGravity(BannerConfig.CENTER)
                            //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
//                            .setOnBannerListener(this)
                            //必须最后调用的方法，启动轮播图。
                            .start();
                }
            } else if (!Utils.isEmpty(s) && "401".equals(s)) {
                Toast.makeText(getActivity(), "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences;
                preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
                MyDialog.setStart(false);
                if (preferences.contains("password")) {
                    preferences.edit().remove("password").commit();
                }
                startActivity(new Intent(mContext.getApplicationContext(), LoginActivity.class));
            }

            isWeight = true;
            if (isAddress)
                getTime();
        }
        //轮播图的监听方法
//        @Override
//        public void OnBannerClick(int position) {
//            Log.i("tag", "你点了第"+position+"张轮播图");
//        }
    }


    String returnData = "";
    Receive receive;

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
                        code = result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    returnData = jsonObject.getString("returnData");
                    if (!Utils.isEmpty(returnData)) {

                        Gson gson = new Gson();
                        receive = gson.fromJson(jsonObject.getString("returnData"), Receive.class);
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
                    if (tvAddress != null)
                        tvAddress.setText(receive.getReceiveProvince() + " " + receive.getReceiveCity() + " " + receive.getReceiveCounty() + " " + receive.getReceiveAddress());
                    isAddress = true;
                    receiveId = receive.getReceiveId() + "";
                    if (isWeight)
                        getTime();
                } else {
                    if (tvAddress != null)
                        tvAddress.setText("没有地址信息，请点击后添加地址");
                }
            }
        }
    }


    class addShopAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "shoppingcart/addOneToShoppingCart";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
            String code = "";

            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
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
                ToastUtil.showShortToast("已加入购物车");
                mPopWindow.dismiss();
            } else if (!Utils.isEmpty(s) && "401".equals(s)) {
                Toast.makeText(getActivity(), "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences;
                preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
                MyDialog.setStart(false);
                if (preferences.contains("password")) {
                    preferences.edit().remove("password").commit();
                }
                startActivity(new Intent(mContext.getApplicationContext(), LoginActivity.class));
            }
        }
    }



    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }


    Bundle bundle;
    String userId;

    private void getData() {
        list_price = new ArrayList<>();
        bundle = this.getArguments();

        // 步骤2:获取某一值
        goodsId = bundle.getString("goodsId");
        userId = bundle.getString("userId");
        if (!Utils.isEmpty(bundle.getString("sign_1"))) {
            Log.e("qqqqqqqqqqqqGGGG", bundle.getString("sign_1"));
        }
        Map<String, Object> params = new HashMap<>();

        params.put("goodsId", goodsId);
        new getShopAsync().execute(params);

        Map<String, Object> params2 = new HashMap<>();
        params2.put("userId", userId);
        new getAddressAsync().execute(params2);

    }

    public void sendMessage(ICallBack callBack) {

        callBack.get_message_from_Fragment(priceId + "", num);
        callBack.getPrice(price + "", priceId, num, goodsId, sign, power);

    }

    public interface ICallBack {
        void get_message_from_Fragment(String string, int num);

        void getPrice(String price, int priceId, int num, String goodId, int sign, String power);
    }


    String sign_1, sign_2;
    int sign_type;

    public void setData(String price, String power, int s, int type) {
        this.price = price;
        sign = s;
        sign_1 = price;
        sign_2 = power;
        sign_type = type;

        Log.e("QqqqqqqTTTT", type + "????");


    }


    long time, recLen;
    List<PostFreeBean> postFreeBeans = new ArrayList<>();

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
                        code = result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    returnData = jsonObject.getString("returnData");
                    if (!Utils.isEmpty(returnData)) {
                        JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                        JsonArray list = content.getAsJsonArray("RecommendDetail");
                        Gson gson = new Gson();

                        for (int i = 0; i < list.size(); i++) {
                            JsonElement use = list.get(i);
                            PostFreeBean userList = gson.fromJson(use, PostFreeBean.class);
                            postFreeBeans.add(userList);
                        }
                        time = (long) (((int) postFreeBeans.get(0).getExpressList().get(0).getEstimatedDeliveryTime()) + 1) * 60 * 60 * 1000;
                        long getNowTimeLong = System.currentTimeMillis();

                        recLen = time + getNowTimeLong;//这样得到的差值是级别

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
                Date date = new Date(recLen);
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
                String time = format.format(date);
                if (tvTime != null)
                    tvTime.setText("23.00前下单，预计（" + time.substring(5, time.length()) + "）送达");
            } else if (!Utils.isEmpty(s) && "401".equals(s)) {
                Toast.makeText(getActivity(), "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences;
                preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
                MyDialog.setStart(false);
                if (preferences.contains("password")) {
                    preferences.edit().remove("password").commit();
                }
                startActivity(new Intent(mContext.getApplicationContext(), LoginActivity.class));
            }
        }
    }

    boolean isWeight = false, isAddress = false;

    public void getTime() {
        Map<String, Object> map = new HashMap<>();
        map.put("goodsName", "hello");
        map.put("receiveId", receive.getReceiveId());
        map.put("weight", weight);
        new getPostFeeAsync().execute(map);
    }


}
