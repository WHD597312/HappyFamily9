package com.xr.happyFamily.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
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


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;
    String appid="wx44acbeed9571e8cf";
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
        int result = 0;
        //有时候支付结果还需要发送给服务器确认支付状态
        if (resp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
            if (resp.errCode==0){
                Toast.makeText(this,"支付成功",Toast.LENGTH_LONG).show();
            }else if (resp.errCode==-2){
                Toast.makeText(this,"取消支付",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,"支付失败",Toast.LENGTH_LONG).show();
                Log.e("qqqqqqSSS",resp.errCode+"?");
            }
            Intent intent = new Intent();
            intent.setAction("edu.jju.broadcastreceiver");
            intent.putExtra("returnCode",resp.errCode);
            sendBroadcast(intent);//发送普通广播
            finish();
        }


    }
}