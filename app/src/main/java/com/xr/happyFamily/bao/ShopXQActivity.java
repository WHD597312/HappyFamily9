package com.xr.happyFamily.bao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.donkingliang.labels.LabelsView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qzs.android.fuzzybackgroundlibrary.Fuzzy_Background;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.EvaluateAdapter;
import com.xr.happyFamily.bao.adapter.EvaluateXhAdapter;
import com.xr.happyFamily.bao.adapter.MyViewPageAdapter;
import com.xr.happyFamily.bao.base.BaseFragment;
import com.xr.happyFamily.bao.bean.GoodsPrice;
import com.xr.happyFamily.bao.fragment.PingJiaFragment;
import com.xr.happyFamily.bao.fragment.ShopFragment;
import com.xr.happyFamily.bao.fragment.XiangQingFragment;
import com.xr.happyFamily.bao.view.FlowTagView;
import com.xr.happyFamily.bean.ShopCartBean;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by win7 on 2018/5/22.
 */

public class ShopXQActivity extends AppCompatActivity {


    @BindView(R.id.tl_flower)
    TabLayout tl_flower;
    @BindView(R.id.vp_flower)
    ViewPager vp_flower;
    Unbinder unbinder;
    List<String> circle = new ArrayList<>();
    List<BaseFragment> fragmentList = new ArrayList<>();
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.img_fenxiang)
    ImageView imgFenxiang;
    @BindView(R.id.tv_shopcart)
    TextView tvShopcart;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    private View contentViewSign;
    private PopupWindow mPopWindow;
    private Context mContext;
    String goodsId;
    ShopFragment shopFragment;
    PingJiaFragment pingJiaFragment;
    int priceId = -1;
    int pos = 0;
    ArrayList<ShopCartBean> mGoPayList = new ArrayList<>();
    private MyDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mContext = ShopXQActivity.this;
        setContentView(R.layout.activity_shopxq);
        ButterKnife.bind(this);
        initView();
        setmTitle();
    }

    private void initView() {
        circle.add("商品");
        circle.add("评价");
        circle.add("详情");
        Bundle extras = getIntent().getExtras();
        goodsId = extras.getString("goodsId");
        SharedPreferences userSettings = getSharedPreferences("my", 0);
        String userId = userSettings.getString("userId", "1000");

        shopFragment = new ShopFragment();
        pingJiaFragment = new PingJiaFragment();
        Bundle bundle = new Bundle();
        // 步骤5:往bundle中添加数据
        bundle.putString("goodsId", goodsId);
        bundle.putString("userId", userId);
        // 步骤6:把数据设置到Fragment中
        shopFragment.setArguments(bundle);
        pingJiaFragment.setArguments(bundle);
        fragmentList.add(shopFragment);
        fragmentList.add(pingJiaFragment);
        fragmentList.add(new XiangQingFragment());

        for (int i = 0; i < circle.size(); i++) {
            tl_flower.addTab(tl_flower.newTab());
        }
        tl_flower.setTabGravity(TabLayout.GRAVITY_FILL);
        tl_flower.setTabMode(TabLayout.MODE_SCROLLABLE);
        vp_flower.setAdapter(new MyViewPageAdapter(getSupportFragmentManager(), fragmentList));
        initTab();
    }

    private void initTab() {
        tl_flower.setTabGravity(TabLayout.GRAVITY_FILL);
        tl_flower.setTabMode(TabLayout.MODE_FIXED);
        tl_flower.setTabTextColors(ContextCompat.getColor(this, R.color.black), ContextCompat.getColor(this, R.color.green3));
        tl_flower.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.green3));
        tl_flower.setupWithViewPager(vp_flower);
        for (int i = 0; i < circle.size(); i++) {
            tl_flower.getTabAt(i).setText(circle.get(i));
        }
        reflex(tl_flower);
    }

    public static void reflex(final TabLayout tabLayout) {
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    Field mTabStripField = tabLayout.getClass().getDeclaredField("mTabStrip");
                    mTabStripField.setAccessible(true);
                    LinearLayout mTabStrip = (LinearLayout) mTabStripField.get(tabLayout);
                    int dp10 = dp2px(tabLayout.getContext(), 10);
                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);
                        //拿到tabView的mTextView属性
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);
                        TextView mTextView = (TextView) mTextViewField.get(tabView);
                        tabView.setPadding(0, 0, 0, 0);
                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);
                        tabView.invalidate();
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * dp转px
     */
    public static int dp2px(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnClick({R.id.back, R.id.img_fenxiang, R.id.tv_shopcart, R.id.tv_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img_fenxiang:
//                showPopup();
                Intent fenxiang = new Intent(Intent.ACTION_SEND);
                fenxiang.setType("text/plain");
                fenxiang.putExtra(Intent.EXTRA_TEXT, "测试内容");
                startActivity(Intent.createChooser(fenxiang, "测试标题"));
                break;
            case R.id.tv_shopcart:
                if (priceId == -1) {
                    shopFragment.sendMessage(new ShopFragment.ICallBack() {
                        @Override
                        public void get_message_from_Fragment(String string,int num) {
                            if ("0".equals(string)) {
                                showTypePopup();
                            } else {
                                Map<String, Object> params = new HashMap<>();
                                params.put("priceId", string);
                                params.put("quantity", num);
                                new addShopAsync().execute(params);
                            }
                        }

                        @Override
                        public void getPrice(String price, int priceId, int num, String goodId, int sign) {

                        }


                    });
                } else {
                    Map<String, Object> params = new HashMap<>();
                    params.put("priceId", priceId);
                    params.put("quantity", num);
                    new addShopAsync().execute(params);
                }

                break;
            case R.id.tv_buy:
                Log.e("qqqqqqqqqqNNNNN22",priceId+"!!!!!!");
                if (priceId == -1) {
                    shopFragment.sendMessage(new ShopFragment.ICallBack() {
                        @Override
                        public void get_message_from_Fragment(String string,int num) {
                            if ("0".equals(string)) {
                                showTypePopup();
                            }
                        }

                        @Override
                        public void getPrice(String price, int priceId, int num, String myGoodId, int sign) {
                            if (sign != -1) {
                                choosePrice = price;
                                Intent intent = new Intent(ShopXQActivity.this, ShopConfActivity.class);
                                intent.putExtra("type", (Serializable) "XQ");
                                intent.putExtra("goodsId", myGoodId + "");
                                intent.putExtra("num", num + "");
                                intent.putExtra("priceId", priceId + "");
                                intent.putExtra("money", Integer.parseInt(price) * num);
                                intent.putExtra("weight", weight*num + "");

                                startActivity(intent);
                            }

                        }
                    });
                } else {
                    Intent intent = new Intent(this, ShopConfActivity.class);
                    intent.putExtra("type", (Serializable) "XQ");
                    intent.putExtra("goodsId", goodsId);
                    intent.putExtra("num", num+"");

                    intent.putExtra("priceId", priceId+"");

                    Log.e("qqqqqqqNNN",num+","+priceId);
                    intent.putExtra("money", Integer.parseInt(price) * num);
                    intent.putExtra("weight", weight + "");
                    startActivity(intent);
                }

                break;
        }
    }


    private LinearLayout rl_bg;

    public static Bitmap captureScreen(Activity activity) {

        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);

        Bitmap bmp = activity.getWindow().getDecorView().getDrawingCache();


        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        Bitmap bitmap = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.e("TAG", "" + statusBarHeight);

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap b = Bitmap.createBitmap(bitmap, 0, statusBarHeight, width, height - statusBarHeight);


        return b;

    }

    private GradientDrawable mBackShadowDrawableLR;
    private ImageView img_close;
    private TextView tv_dis;
    private RelativeLayout rl_dis;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showPopup() {

        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_shop_fenxiang, null);
        rl_bg = (LinearLayout) contentViewSign.findViewById(R.id.rl_bg);
        img_close = (ImageView) contentViewSign.findViewById(R.id.img_close);
        tv_dis = (TextView) contentViewSign.findViewById(R.id.tv_dis);

        rl_dis = (RelativeLayout) contentViewSign.findViewById(R.id.rl_dis);
//        tv_dingdan = (TextView) contentViewSign.findViewById(R.id.tv_dingdan);
//        tv_shangcheng = (TextView) contentViewSign.findViewById(R.id.tv_shangcheng);
//        tv_shopcart.setOnClickListener(this);
//        tv_dingdan.setOnClickListener(this);
//        tv_shangcheng.setOnClickListener(this);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        tv_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        rl_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        Resources res = getResources();
        Bitmap finalBitmap = Fuzzy_Background.with(this)
                .bitmap(captureScreen(this)) //要模糊的图片
                .radius(20)//模糊半径
                .blur();
        Drawable drawable2 = new BitmapDrawable(finalBitmap);
        rl_bg.setBackground(drawable2);
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
        mPopWindow.setOnDismissListener(new ShopXQActivity.poponDismissListener());
        mPopWindow.showAsDropDown(findViewById(R.id.view_pop));
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


    class addShopAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "shoppingcart/addOneToShoppingCart";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
            String code = "";

            try {
                if (!Utils.isEmpty(result)) {
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
                Toast.makeText(ShopXQActivity.this, "已加入购物车", Toast.LENGTH_SHORT).show();
                if (mPopWindow != null) {
                    mPopWindow.dismiss();
                }
            }
        }
    }


    private View view_dis;
    private EvaluateAdapter adapter_xinghao;
    private FlowTagView labelsView;
    private TextView tv_price, tv_name, tv_type_cart, tv_type_bug, tv_jia, tv_jian, tv_num, tv_cart, tv_buy;
    int num = 1;
    private EvaluateXhAdapter adapter_xh;
    int sign = -1;

    private void showTypePopup() {
        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_shop_xinghao, null);
        img_close = (ImageView) contentViewSign.findViewById(R.id.img_close);
        view_dis = contentViewSign.findViewById(R.id.view_dis);
        labelsView = (FlowTagView) contentViewSign.findViewById(R.id.labels);
        adapter_xh = new EvaluateXhAdapter(this, R.layout.item_xinghao);
        list = new ArrayList<>();
        for (int i = 0; i < list_price.size(); i++) {
            list.add(list_price.get(i).getPower() + "");
        }
        adapter_xh.setItems(list);

        labelsView.setAdapter(adapter_xh);
        labelsView.setOne();
        labelsView.setItemClickListener(new FlowTagView.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                Log.e("qqqqqqNNNN","??????????");
                sign = position;
                adapter_xh.setSelection(position);
                adapter_xh.notifyDataSetChanged();
                String e = adapter_xh.getItem(position).toString();
                tv_price.setText("¥" + list_price.get(position).getPrice());

                price = list_price.get(position).getPrice() + "";
                priceId = list_price.get(position).getPriceId();
                Log.e("qqqqqqNNNN",priceId+"!!!!!!");
                goodsId = list_price.get(position).getGoodsId() + "";
                shopFragment.setData(price, list_price.get(position).getPower() + "", sign);
//                Toast.makeText(mContext, "i am:" + e, Toast.LENGTH_SHORT).show();
//                datas.clear();
//                datas.addAll(initData(e));
//                pinglunAdapter.notifyDataSetChanged();

            }
        });

        tv_price = (TextView) contentViewSign.findViewById(R.id.tv_price);

        if (sign != -1) {
            adapter_xh.setSelection(sign);
            tv_price.setText("¥" + getList().get(sign).getPrice());
        } else {
            tv_price.setText("");
        }
        tv_name = (TextView) contentViewSign.findViewById(R.id.tv_name);
        tv_type_cart = (TextView) contentViewSign.findViewById(R.id.tv_type_cart);
        tv_type_bug = (TextView) contentViewSign.findViewById(R.id.tv_type_bug);

        tv_jian = (TextView) contentViewSign.findViewById(R.id.tv_shop_reduce);
        tv_num = (TextView) contentViewSign.findViewById(R.id.tv_shop_num);
        tv_jia = (TextView) contentViewSign.findViewById(R.id.tv_shop_add);
        tv_cart = (TextView) contentViewSign.findViewById(R.id.tv_type_cart);
        tv_buy = (TextView) contentViewSign.findViewById(R.id.tv_type_bug);
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
        tv_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (priceId != -1) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("priceId", priceId);
                    params.put("quantity", num);
                    new addShopAsync().execute(params);
                } else {
                    Toast.makeText(mContext, "请选择商品规格", Toast.LENGTH_SHORT).show();
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
                    intent.putExtra("money", Integer.parseInt(price) * num );
                    intent.putExtra("weight", weight*num + "");
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "请选择商品规格", Toast.LENGTH_SHORT).show();
                }
            }
        });

        try {
            tv_name.setText(jsonObject.getString("goodsName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setBiaoqian();
//        tv_shangcheng = (TextView) contentViewSign.findViewById(R.id.tv_shangcheng);
//        tv_shopcart.setOnClickListener(this);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        view_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
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
        mPopWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//        initView();
//        initData();

    }

    List<String> list;
    String choosePrice;

    private void setBiaoqian() {

        list = new ArrayList<>();
        for (int i = 0; i < list_price.size(); i++) {
            list.add(list_price.get(i).getPower() + "");
        }
//
//        labelsView.setLabels(list); //直接设置一个字符串数组就可以了。


//        //标签的点击监听
//        labelsView.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
//            @Override
//            public void onLabelClick(TextView label, Object data, int position) {
//                //label是被点击的标签，data是标签所对应的数据，position是标签的位置。
//
//                if (shopFragment != null) //可能没有实例化
//                {
//                    choosePrice = list_price.get(position).getPrice() + "";
//                    if (shopFragment.getView() != null) {
//                        shopFragment.setData("¥" + choosePrice, list.get(position));//自定义方法更新
//                    }
//                }
//                tv_price.setText("¥" + choosePrice);
//                pos = position;
//                priceId = list_price.get(position).getPriceId();
//            }
//        });
////标签的选中监听
//        labelsView.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
//            @Override
//            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
//                //label是被选中的标签，data是标签所对应的数据，isSelect是是否选中，position是标签的位置。
//
//            }
//        });
//
//        labelsView.setSelects(pos);
    }

    private List<GoodsPrice> list_price = new ArrayList<>();


    public List<GoodsPrice> getList() {
        return list_price;
    }

    public void setmTitle() {
        Map<String, Object> params = new HashMap<>();
        params.put("goodsId", goodsId);
        dialog = MyDialog.showDialog(mContext);
        dialog.show();
        new getShopAsync().execute(params);

    }

    JSONObject jsonObject;
    String name, type, img, price;
    double weight;

    class getShopAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            Map<String, Object> params = maps[0];
            String url = "goods/getGoodsById";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                    JsonArray list = content.getAsJsonArray("goodsPrice");
                    if ("100".equals(code)) {
                        Gson gson = new Gson();
                        for (JsonElement user : list) {
                            //通过反射 得到UserBean.class
                            GoodsPrice userList = gson.fromJson(user, GoodsPrice.class);
                            list_price.add(userList);
                            img = returnData.getString("image");
                            name = returnData.getString("goodsName");
                            type = returnData.getString("simpleDescribe");
                            price = list_price.get(0).getPrice() + "";
                            weight = returnData.getDouble("weight") ;
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
            }
        }
    }


}
