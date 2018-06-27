package com.xr.happyFamily.bao;

import android.Manifest;
import android.app.Dialog;
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
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.PublicData;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.TimeUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

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

public class TuiKuanXQActivity extends AppCompatActivity {


    DingDanXQAdapter confListAdapter;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.tv_wuliu)
    TextView tvWuliu;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_tui)
    TextView tvTui;
    @BindView(R.id.tv_chexiao)
    TextView tvChexiao;
    @BindView(R.id.tv_xiugai)
    TextView tvXiugai;
    @BindView(R.id.ll_tuikuan)
    LinearLayout llTuikuan;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_yuanyin)
    TextView tvYuanyin;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_time_tuikuan)
    TextView tvTimeTuikuan;
    @BindView(R.id.tv_bianhao)
    TextView tvBianhao;
    @BindView(R.id.tv_call)
    TextView tvCall;
    private MyDialog dialog;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mContext = TuiKuanXQActivity.this;
        setContentView(R.layout.activity_shop_tuikuan_xq);
        ButterKnife.bind(this);
        titleRightText.setVisibility(View.GONE);
        titleText.setText("退款详情");

        confListAdapter = new DingDanXQAdapter(TuiKuanXQActivity.this, orderDetailsLists);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(confListAdapter);

        orderNumber = getIntent().getExtras().getString("orderId");
        dialog = MyDialog.showDialog(TuiKuanXQActivity.this);
        dialog.show();
        Map<String, Object> map = new HashMap<>();
        new getOrderAsync().execute(map);
    }


    @OnClick({R.id.back, R.id.tv_chexiao, R.id.tv_xiugai, R.id.tv_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_chexiao:
                dialog = MyDialog.showDialog(TuiKuanXQActivity.this);
                dialog.show();
                Map<String, Object> map = new HashMap<>();
                map.put("orderId", orderId);
                new cancelRefundOrderAsync().execute(map);

                break;
            case R.id.tv_xiugai:
                Intent intent = new Intent(TuiKuanXQActivity.this, TuiKuanActivity.class);
                intent.putExtra("orderNumber", orderNumber);
                intent.putExtra("type", "XG");
                intent.putExtra("shipperCode", shipperCode);
                intent.putExtra("logisticCode", logisticCode);
                intent.putExtra("reason", reason);
                intent.putExtra("refundTime", refundTime);
                startActivity(intent);
                break;
            case R.id.tv_call:
                showPopup();
                break;


        }
    }

    String money, orderNumber;
    List<OrderBean.OrderDetailsList> orderDetailsLists = new ArrayList<>();

    String shipperCode, logisticCode, reason, refundTime, price;

    class getOrderAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "/order/getOrderByOrderNumber";
            url = url + "?orderNumber=" + orderNumber;
            String result = HttpUtils.doGet(TuiKuanXQActivity.this, url);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    money = returnData.get("paidAmount").toString();
                    orderId = returnData.get("orderId").toString();
                    JSONObject orderRefund = returnData.getJSONObject("orderRefund");
                    logisticCode = orderRefund.getString("logisticCode");
                    shipperCode = orderRefund.getString("shipperCode");
                    reason = orderRefund.getString("reason");
                    refundTime = returnData.getString("refundTime");

                    JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                    JsonArray list = content.getAsJsonArray("orderDetailsList");
                    Gson gson = new Gson();
                    for (JsonElement user : list) {
                        //通过反射 得到UserBean.class
                        OrderBean.OrderDetailsList userList = gson.fromJson(user, OrderBean.OrderDetailsList.class);
                        orderDetailsLists.add(userList);
                    }
                    JSONObject receive = returnData.getJSONObject("receive");


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


                long getNowTimeLong = System.currentTimeMillis();

                recLen = ((int) (getNowTimeLong - Long.parseLong(refundTime))) / 1000;//这样得到的差值是级别

                timer.schedule(task, 1000, 1000);      //计时
                tvYuanyin.setText("退款原因:" + reason);
                tvPrice.setText("退款金额:" + money);
                tvTimeTuikuan.setText("申请时间:" + TimeUtils.getTime(refundTime));
                tvBianhao.setText("退货编号:" + orderNumber);
            }
        }
    }

    private int recLen;
    Timer timer = new Timer();


    TimerTask task = new TimerTask() {
        @Override
        public void run() {


            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    recLen++;

                    long day = 0;
                    long hours = recLen / (60 * 60);
                    if (hours > 24) {
                        day = hours / 24;
                        hours = hours - day * 24;
                    }
                    long minutes = (recLen - hours * (60 * 60)-day*24*60*60) / (60);
                    long s = (recLen-day*24*60*60 - hours * (60 * 60) - minutes * 60);

                    tvTime.setText("处理时间:" + day + "天" + hours + "小时" + minutes + "分" + s + "秒");
                    if (recLen < 0) {
                        timer.cancel();
                        Map<String, Object> params = new HashMap<>();
                        params.put("orderId", orderId);
//                    new cancelOrderAsync().execute(params);
                    }
                }
            });
        }
    };


    class cancelRefundOrderAsync extends AsyncTask<Map<String, Object>, Void, String> {

        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            Map<String, Object> params = maps[0];
            String url = "/order/cancelRefundOrder";
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
                MyDialog.closeDialog(dialog);
                tvTime.setVisibility(View.GONE);
                tvWuliu.setText("退款关闭");
                tvTui.setText("因您撤销退款申请，退款已关闭");
                llTuikuan.setVisibility(View.GONE);
            }
        }
    }

    private View contentViewSign;
    private PopupWindow mPopWindow;
    private Context mContext;
    private TextView tv_quxiao, tv_queding, tv_context;

    private void showPopup() {

        contentViewSign = LayoutInflater.from(TuiKuanXQActivity.this).inflate(R.layout.popup_main, null);
        tv_quxiao = (TextView) contentViewSign.findViewById(R.id.tv_quxiao);
        tv_queding = (TextView) contentViewSign.findViewById(R.id.tv_queren);
        tv_context = (TextView) contentViewSign.findViewById(R.id.tv_context);
        tv_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        tv_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TuiKuanXQActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                            100);
                    return;
                } else {
                    PublicData publicDate = new PublicData();
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + publicDate.getTel_kefu()));
                    startActivity(intent);
                }
            }
        });

        tv_context.setText("是否拨打客服电话？");
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
