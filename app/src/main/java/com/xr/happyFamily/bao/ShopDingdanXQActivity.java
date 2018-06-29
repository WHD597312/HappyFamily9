package com.xr.happyFamily.bao;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.DingDanXQAdapter;
import com.xr.happyFamily.bao.alipay.PayActivity;
import com.xr.happyFamily.bao.bean.myOrderBean;
import com.xr.happyFamily.bean.OrderBean;
import com.xr.happyFamily.bean.ShopBean;
import com.xr.happyFamily.bean.ShopCartBean;
import com.xr.happyFamily.bean.WuLiuBean;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.PublicData;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2018/5/22.
 */

public class ShopDingdanXQActivity extends AppCompatActivity implements View.OnClickListener {


    String orderId;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.img_wuliu)
    ImageView imgWuliu;
    @BindView(R.id.tv_wuliu)
    TextView tvWuliu;
    @BindView(R.id.img_address)
    ImageView imgAddress;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tv_yunfei)
    TextView tvYunfei;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_dingdan)
    TextView tvDingdan;
    @BindView(R.id.tv_zhifubao)
    TextView tvZhifubao;
    @BindView(R.id.tv_changjian)
    TextView tvChangjian;
    @BindView(R.id.tv_fukuan)
    TextView tvFukuan;
    @BindView(R.id.tv_fahuo)
    TextView tvFahuo;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_wuliu_time)
    TextView tvWuliuTime;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.img3)
    ImageView img3;
    @BindView(R.id.tv_daojishi)
    TextView tvDaojishi;
    @BindView(R.id.ll_daifukuan)
    LinearLayout llDaifukuan;
    @BindView(R.id.img_tui)
    ImageView imgTui;
    @BindView(R.id.rl_bot)
    RelativeLayout rlBot;
    @BindView(R.id.rl_wuliu)
    RelativeLayout rlWuliu;
    @BindView(R.id.tv_state)
    TextView tvState;
    private MyDialog dialog;

    myOrderBean orderBean;
    DingDanXQAdapter confListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mContext = ShopDingdanXQActivity.this;
        setContentView(R.layout.activity_shop_dingdan_xq);
        ButterKnife.bind(this);
        titleRightText.setVisibility(View.GONE);
        titleText.setText("订单详情");
        confListAdapter = new DingDanXQAdapter(ShopDingdanXQActivity.this, orderDetailsLists);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(confListAdapter);

        orderNumber = getIntent().getExtras().getString("orderId");
//        orderId="H201806191500005";
        dialog = MyDialog.showDialog(mContext);
        dialog.show();
        Map<String, Object> map = new HashMap<>();
        new getOrderAsync().execute(map);
    }


    ArrayList<String> img = new ArrayList<>();
    ArrayList<Integer> myOrderId = new ArrayList<>();
    ArrayList<Integer> myPriceId = new ArrayList<>();
    ArrayList<Integer> myGoodsId = new ArrayList<>();

    @OnClick({R.id.back, R.id.tv_dingdan, R.id.img1, R.id.img2, R.id.img3, R.id.img_tui,R.id.rl_wuliu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.tv_dingdan:
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", orderNumber);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                Toast.makeText(mContext,"复制成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.img1:
                showPopup(1);
                break;
            case R.id.img3:
                //拨打电话
                showPopup(2);
                break;
            case R.id.img2:
                switch (state) {
                    case "1":
                        Intent intent=new Intent(ShopDingdanXQActivity.this, PayFailActivity.class);
                        intent.putExtra("type", "DingDan");
                        intent.putExtra("orderNumber", orderNumber);
//                        intent.putExtra("orderDetailsLists", (Serializable) orderDetailsLists);
                        intent.putExtra("money", paidAmount);

                        startActivity(intent);
                        break;
                    case "2":
                        Toast.makeText(ShopDingdanXQActivity.this, "该商品暂未发货", Toast.LENGTH_SHORT).show();
                        break;
                    case "3":
                        Intent intent3 = new Intent(ShopDingdanXQActivity.this, WuLiuActivity.class);
                        intent3.putExtra("orderNumber",orderNumber);
                        intent3.putExtra("logisticCode", logisticCode);
                        intent3.putExtra("shipperCode", shipperCode);
                        startActivity(intent3);
                        break;
                    case "4":
                        for (int i = 0; i < orderDetailsLists.size(); i++) {
                            img.add(orderDetailsLists.get(i).getImage());
                            myGoodsId.add(Integer.parseInt(orderDetailsLists.get(i).getGoodsId()));
                            myOrderId.add(orderDetailsLists.get(i).getOrderId());
                            myPriceId.add(orderDetailsLists.get(i).getPriceId());
                        }
                        Intent intent1 = new Intent(this, PingLunActivity.class);
                        intent1.putStringArrayListExtra("img", img);
                        startActivity(intent1);
                        break;
                    case "5":
                        //退款详情
                        Intent intent2 = new Intent(this, TuiKuanXQActivity.class);
                        intent2.putExtra("type", orderNumber);
                        startActivity(intent2);
                        break;
                }
                break;
            case R.id.img_tui:
                //退货
                Intent intent=new Intent(ShopDingdanXQActivity.this,TuiKuanActivity.class);
                intent.putExtra("orderNumber", orderNumber);
                intent.putExtra("type", "XQ");
                startActivity(intent);
//                Map<String, Object> params = new HashMap<>();
//                params.put("orderNumber", orderNumber);
//                new refundOrderAsync().execute(params);
                break;
            case R.id.rl_wuliu:
                Intent intent3 = new Intent(ShopDingdanXQActivity.this, WuLiuActivity.class);
                intent3.putExtra("orderNumber",orderNumber);
                intent3.putExtra("logisticCode", logisticCode);
                intent3.putExtra("shipperCode", shipperCode);
                startActivity(intent3);
                break;
        }
    }


    private View contentViewSign;
    private PopupWindow mPopWindow;
    private Context mContext;
    private TextView tv_quxiao, tv_queding, tv_context;

    private void showPopup(final int sign) {

        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_main, null);
        tv_quxiao = (TextView) contentViewSign.findViewById(R.id.tv_quxiao);
        tv_queding = (TextView) contentViewSign.findViewById(R.id.tv_queren);
        tv_context = (TextView) contentViewSign.findViewById(R.id.tv_context);
        tv_quxiao.setOnClickListener(this);
        tv_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sign == 1) {
                    mPopWindow.dismiss();
                    Map<String, Object> params = new HashMap<>();
                    params.put("orderId", orderId);
                    new cancelOrderAsync().execute(params);
                } else {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ShopDingdanXQActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                                100);
                        return;
                    } else {
                        PublicData publicDate = new PublicData();
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + publicDate.getTel_kefu()));
                        startActivity(intent);
                    }
                }
            }
        });
        if (sign == 1) {
            ((TextView)contentViewSign.findViewById(R.id.tv_title)).setText("取消支付");
            tv_context.setText("是否确认取消支付？");
        }
        else {
            ((TextView)contentViewSign.findViewById(R.id.tv_title)).setText("拨打客服");
            tv_context.setText("是否拨打客服电话？");};
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_quxiao:
                mPopWindow.dismiss();
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


    String money;
    List<OrderBean.OrderDetailsList> orderDetailsLists = new ArrayList<>();
    String wuliu, wuliu_time, name, address, tel, postFee, paidAmount, orderNumber, state, paymentSeq;
    //发货时间  创建时间  付款时间
    String sendTime, createTime, paymentTime;
    //物流信息
    String logisticCode, shipperCode;


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
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    money = returnData.get("paidAmount").toString();
                    JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                    JsonArray list = content.getAsJsonArray("orderDetailsList");
                    Gson gson = new Gson();
                    for (JsonElement user : list) {
                        //通过反射 得到UserBean.class
                        OrderBean.OrderDetailsList userList = gson.fromJson(user, OrderBean.OrderDetailsList.class);
                        orderDetailsLists.add(userList);
                    }
                    wuliu = "暂无物流信息";
                    wuliu_time = "暂无物流信息";
                    JSONObject receive = returnData.getJSONObject("receive");
                    address = receive.get("receiveProvince").toString() + " " + receive.get("receiveCity").toString() + " " +
                            receive.get("receiveCounty").toString() + " " + receive.get("receiveAddress").toString();
                    name = receive.get("contact").toString();
                    tel = receive.get("tel").toString();
                    state = returnData.get("state").toString();
                    postFee = returnData.get("postFee").toString();
                    paidAmount = returnData.get("paidAmount").toString();
                    orderId = returnData.get("orderId").toString();
                    createTime = returnData.get("createTime").toString();
                    paymentTime = returnData.get("paymentTime").toString();
                    logisticCode = returnData.get("logisticCode").toString();
                    shipperCode = returnData.get("shipperCode").toString();
                    paymentSeq = returnData.get("paymentSeq").toString();
                    if(returnData.get("sendTime")!=null)
                        sendTime = returnData.get("sendTime").toString();

                    if(paymentSeq.equals("null"))
                        paymentSeq="暂无支付宝交易号";
                    if(sendTime.equals("null"))
                        sendTime="暂未发货";
                    if(paymentTime.equals("null"))
                        paymentTime="暂未付款";

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
                confListAdapter.notifyDataSetChanged();
                MyDialog.closeDialog(dialog);
                Map<String, Object> params = new HashMap<>();


                switch (state) {
                    case "1":
                        imgTui.setVisibility(View.GONE);
                        rlWuliu.setVisibility(View.GONE);
                        img1.setVisibility(View.VISIBLE);
                        img1.setImageResource(R.mipmap.btn_dd_quxiao);
                        img2.setImageResource(R.mipmap.btn_dd_pay);
                        tvDaojishi.setVisibility(View.VISIBLE);
                        rlWuliu.setVisibility(View.GONE);
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        long getNowTimeLong = System.currentTimeMillis();

                            recLen = ((int) (Long.parseLong(createTime)+24*60*60*1000 - getNowTimeLong)) / 1000;//这样得到的差值是级别

                        timer.schedule(task, 1000, 1000);       // timeTask
                        break;
                    case "2":
                        imgTui.setVisibility(View.GONE);
                        tvState.setText("等待商家发货");
                        rlWuliu.setVisibility(View.GONE);
                        img2.setImageResource(R.mipmap.btn_dd_wuliu);
                        break;
                    case "3":
                        imgTui.setVisibility(View.GONE);
                        tvState.setText("卖家已发货");
                        img2.setImageResource(R.mipmap.btn_dd_wuliu);
                        params.put("shipperCode", shipperCode);
                        params.put("logisticCode", logisticCode);
                        new expressInfoAsync().execute(params);
                        break;
                    case "4":
                        tvState.setText("已签收");
                        img2.setImageResource(R.mipmap.btn_dd_pingjia);
                        params.put("shipperCode", shipperCode);
                        params.put("logisticCode", logisticCode);
                        new expressInfoAsync().execute(params);
                        break;
//                    case "5":
//
//                        img2.setImageResource(R.mipmap.btn_dd_tuikuan);
//                        break;
                }
                tvName.setText(name);
                tvTel.setText(tel);
                tvAddress.setText(address);
                tvChangjian.setText("创建时间:" + createTime);
                tvFukuan.setText("付款时间:" + paymentTime);
                tvFahuo.setText("发货时间:" + sendTime);
                tvZhifubao.setText("支付宝交易号:" + paymentSeq);
                tvDingdan.setText("订单编号:" + orderNumber);
                tvYunfei.setText("¥" + postFee);
                tvPrice.setText("¥" + paidAmount);
            }
        }
    }


    class cancelOrderAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "/order/cancelOrder";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    wuliu = returnData.get("paidAmount").toString();
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
                finish();
                Toast.makeText(mContext, "取消订单成功", Toast.LENGTH_SHORT).show();

            }
        }
    }




    class expressInfoAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "/logistics/expressInfo";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                    JsonArray list = content.getAsJsonArray("traces");
                    Gson gson = new Gson();
                    //通过反射 得到UserBean.class
                    JsonElement user = list.get(0);
                    WuLiuBean userList = gson.fromJson(user, WuLiuBean.class);
                    wuliu = userList.getAcceptStation();
                    wuliu_time = userList.getAcceptTime();

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
                tvWuliu.setText(wuliu);
                tvWuliuTime.setText(wuliu_time);

            }
        }
    }


    //距离支付结束时间  单位秒
    private int recLen;
    Timer timer = new Timer();


    TimerTask task = new TimerTask() {
        @Override
        public void run() {


            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    recLen--;
                    tvDaojishi.setText("" + recLen);

                    long hours = recLen / (60 * 60);
                    long minutes = (recLen - hours * (60 * 60)) / (60);
                    long s = (recLen - hours * (60 * 60) - minutes * 60);

                    tvDaojishi.setText(hours + "小时" + minutes + "分" + s + "秒后订单关闭");
                    if (recLen < 0) {
                        timer.cancel();
                        finish();
//                        Map<String, Object> params = new HashMap<>();
//                        params.put("orderId", orderId);
//                    new cancelOrderAsync().execute(params);
                    }
                }
            });
        }
    };
}
