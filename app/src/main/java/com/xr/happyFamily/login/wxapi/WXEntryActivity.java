package com.xr.happyFamily.login.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化api并向微信注册应用。
        api = WXAPIFactory.createWXAPI(this, "");
        api.registerApp("");
        api.handleIntent(getIntent(), this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq baseReq) {

    }


    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //如果集成了分享和登录功能，那么可以通过baseResp.getType()来判断是哪个回调信息，1为登录授权，2为分享
                //如果是登录授权，那么调用了登录api以后，这里会返回获取accessToken需要的code
                SendAuth.Resp sendResp = (SendAuth.Resp) baseResp;
                String code = sendResp.code;

                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //如果分享的时候，该已经开启，那么微信开始这个activity时，会调用onNewIntent，所以这里要处理微信的返回结果
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    public void getAccessToken(String code, String secret) {
        //微信登录时，需要通过code请求url获取accesstoken。
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + "" +
                "&secret=" + secret +
                "&code=" + code +
                "&grant_type=authorization_code";
        //以下是网络请求拿到accesstoken以及openid.

    }

    public void getUserInfo(String accessToken, String openId) {
        //如果获取到了AccessToken和openid，请求url获取用户信息
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" +
                accessToken + "&openid=" + openId;

    }



}

