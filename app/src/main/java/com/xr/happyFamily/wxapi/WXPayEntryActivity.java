package com.xr.happyFamily.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.PayFailActivity;
import com.xr.happyFamily.bao.PaySuccessActivity;
import com.xr.happyFamily.bao.alipay.PayActivity;
import com.xr.happyFamily.together.util.ActivityManagerApplication;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;
    String appid="wx44acbeed9571e8cf";
    String orderNumber;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        api = WXAPIFactory.createWXAPI(this,appid);
        api.handleIntent(getIntent(), this);


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        SharedPreferences userSettings = getSharedPreferences("order", 0);
        orderNumber = userSettings.getString("orderNumber", "0");
        //有时候支付结果还需要发送给服务器确认支付状态
        if (resp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
            if (resp.errCode==0){
                Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(WXPayEntryActivity.this, PaySuccessActivity.class);
                intent.putExtra("orderNumber",orderNumber);
                startActivity(intent);
                finish();
            }else if (resp.errCode==-2){

                Toast.makeText(WXPayEntryActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(WXPayEntryActivity.this, PayFailActivity.class);
                intent.putExtra("type","Pay");
                intent.putExtra("orderNumber",orderNumber);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(this,"支付失败",Toast.LENGTH_LONG).show();
                Log.e("qqqqqqSSS",resp.errStr+"?");
            }
            ActivityManagerApplication.destoryActivity("WXPayActivity");
            finish();

        }


    }
}