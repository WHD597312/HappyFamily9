package com.xr.happyFamily.bao;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.alipay.PayActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

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
        mContext = PayFailActivity.this;
        setContentView(R.layout.activity_shop_pay_fail);
        ButterKnife.bind(this);
//        orderId=getIntent().getExtras().getString("orderId");
        Bundle bundle = getIntent().getExtras();

        orderNumber = bundle.getString("orderNumber");
        tv_pay = new TextView[]{tvZhifubao, tvWeixin, tvYinlian};
        pay_true = getResources().getDrawable(R.mipmap.xuanzhong_shop3x);
        pay_false = getResources().getDrawable(R.mipmap.weixuanzhong3x);
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


    @OnClick({R.id.tv_chakan, R.id.tv_zhifubao, R.id.tv_weixin, R.id.tv_yinlian})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_chakan:
                Intent intent = new Intent(this, ShopDingdanXQActivity.class);
                intent.putExtra("orderId", orderNumber);
                startActivity(intent);
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
                tvMoney.setText("¥"+paidAmount);
                tvJine.setText("¥"+totalAmount);
                tvNum.setText(orderNumber);
                tvAdd.setText(address);
            }
        }
    }


    @OnClick({R.id.tv_zhifu, R.id.back, R.id.tv_chakan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_zhifu:
                Intent intent = new Intent(PayFailActivity.this, PayActivity.class);
                intent.putExtra("orderNumber", orderNumber);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_chakan:
                Intent intent2 = new Intent(PayFailActivity.this, ShopDingdanXQActivity.class);
                intent2.putExtra("orderId", orderNumber);
                startActivity(intent2);
                break;
        }

    }

}
