package com.xr.happyFamily.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.PayFailActivity;
import com.xr.happyFamily.bao.PaySuccessActivity;
import com.xr.happyFamily.bao.alipay.PayActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

import java.util.Map;

import butterknife.ButterKnife;

public class WXPayActiviy  extends AppCompatActivity {

    String orderNumber;
    private IWXAPI iwxapi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Bundle extras = getIntent().getExtras();
        orderNumber = extras.getString("orderNumber");

        iwxapi = WXAPIFactory.createWXAPI(this, appid,false); //初始化微信api
        iwxapi.registerApp(appid); //注册appid  appid可以在开发平台获取
        new wxpay().execute();


    }

    public class MyReceiver extends BroadcastReceiver {
        public MyReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int returenCode=Integer.parseInt(intent.getExtras().get("returnCode").toString());
            Log.e("qqqqqqqqCCC",returenCode+"???");
        }

    }

    String appid="wx44acbeed9571e8cf";
    PayReq request;
    class wxpay extends AsyncTask<Map<String, Object>, Void, String>{
        @Override
        protected String doInBackground(Map<String, Object>... maps) {


            String url = "/wxpay/pay";
            url = url + "?orderNumber=" +orderNumber;
            String result = HttpUtils.doGet(WXPayActiviy.this, url);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");


                   request = new PayReq();
                    request.appId = returnData.getString("appid");
                    request.partnerId = returnData.getString("partnerid");
                    request.prepayId = returnData.getString("prepayid");
                    request.packageValue=returnData.getString("package");
                    request.nonceStr = returnData.getString("noncestr");
                    request.timeStamp = returnData.getString("timestamp");
                    request.sign = returnData.getString("sign");


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
                iwxapi.sendReq(request);

            }
        }
    }


}