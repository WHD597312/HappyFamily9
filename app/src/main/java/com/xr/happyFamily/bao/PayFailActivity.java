package com.xr.happyFamily.bao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.alipay.PayActivity;
import com.xr.happyFamily.bao.base.ToastUtil;
import com.xr.happyFamily.bao.util.WXUtil;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.wxapi.WXPayActiviy;
import com.xr.happyFamily.wxapi.WXPayEntryActivity;

import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2018/5/22.
 */

public class PayFailActivity extends AppCompatActivity {


    String orderNumber, orderId;
    Context mContext;


    boolean isData = true;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.tv_jine)
    TextView tvJine;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_zhifubao)
    TextView tvZhifubao;
    @BindView(R.id.tv_weixin)
    TextView tvWeixin;
    @BindView(R.id.tv_yinlian)
    TextView tvYinlian;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_chakan)
    TextView tvChakan;
    @BindView(R.id.tv_zhifu)
    TextView tvZhifu;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_state)
    TextView tvState;
    private MyDialog dialog;

    int sign_pay = 0;
    TextView[] tv_pay;
    Drawable pay_true, pay_false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        MyApplication application = (MyApplication) getApplication();
        application.addActivity(this);
        mContext = PayFailActivity.this;
        setContentView(R.layout.activity_shop_pay_fail);
        ButterKnife.bind(this);
//        orderId=getIntent().getExtras().getString("orderId");
        Bundle bundle = getIntent().getExtras();

        orderNumber = bundle.getString("orderNumber");
        tv_pay = new TextView[]{tvZhifubao, tvWeixin, tvYinlian};
        pay_true = getResources().getDrawable(R.mipmap.xuanzhong_shop3x);
        pay_false = getResources().getDrawable(R.mipmap.weixuanzhong3x);
        pay_true.setBounds(0, 0, pay_true.getMinimumWidth(), pay_true.getMinimumHeight());
        pay_false.setBounds(0, 0, pay_false.getMinimumWidth(), pay_false.getMinimumHeight());
        init();

    }

    private void init() {
        Bundle extras = getIntent().getExtras();
        String type =extras.getString("type");
        Log.e("qqqqqqqTT",type);
        if ("Pay".equals(type))
            titleText.setText("支付失败");
        else {
            titleText.setText("订单成功");
            img.setImageResource(R.mipmap.ic_dingdan_pay);
            tvState.setText("订单提交成功");
            tvState.setTextColor(Color.parseColor("#00B41C"));
            tvChakan.setVisibility(View.GONE);
        }
        titleRightText.setVisibility(View.GONE);
        new getOrderAsync().execute();


    }


    @OnClick({R.id.tv_zhifu, R.id.back,R.id.tv_chakan, R.id.tv_zhifubao, R.id.tv_weixin, R.id.tv_yinlian})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_zhifu:
                if(sign_pay==2){
                    ToastUtil.showShortToast("暂不支持银联支付");
                }else {
                    if (sign_pay == 0) {
                        Intent intent = new Intent(PayFailActivity.this, PayActivity.class);
                        intent.putExtra("orderNumber", orderNumber);
                        startActivity(intent);
                    } else if (sign_pay == 1) {
                        if(WXUtil.isWeChatAppInstalled(mContext)) {
                            Intent intent = new Intent(mContext, WXPayActiviy.class);
                            intent.putExtra("orderNumber", orderNumber);
                            startActivity(intent);
                        }
                        else
                            ToastUtil.showShortToast("请先安装微信");
                    }

                    finish();
                }
                break;
            case R.id.tv_chakan:
                Intent intent2 = new Intent(this, ShopDingdanXQActivity.class);
                intent2.putExtra("orderId", orderNumber);
                startActivity(intent2);
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

        }
    }


    String totalAmount,paidAmount, num, address;

    class getOrderAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            String url = "order/getOrderByOrderNumber";
            url = url + "?orderNumber=" + orderNumber;
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
                    totalAmount = returnData.get("totalAmount").toString();
                    paidAmount = returnData.get("paidAmount").toString();
                    orderId = returnData.get("orderId").toString();
                    JSONObject receive = returnData.getJSONObject("receive");
                    address = receive.get("receiveProvince").toString() + " " + receive.get("receiveCity").toString() + " "
                            + receive.get("receiveCounty").toString() + " " + receive.get("receiveAddress").toString();

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
                if(tvMoney!=null)
                tvMoney.setText("¥"+paidAmount);
                if(tvJine!=null)
                tvJine.setText("¥"+totalAmount);
                if (tvNum!=null)
                tvNum.setText(orderNumber);
                if (tvAdd!=null)
                tvAdd.setText(address);
            }else if (!Utils.isEmpty(s) && "401".equals(s)) {
                ToastUtil.showShortToast("用户信息超时请重新登陆");
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




}
