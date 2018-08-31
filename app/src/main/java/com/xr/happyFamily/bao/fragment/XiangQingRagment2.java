package com.xr.happyFamily.bao.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.base.BaseFragment;

public class XiangQingRagment2 extends  BaseFragment implements View.OnClickListener {
    private WebView web;
    View view;
    String id ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.activity_shop_xq2, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {

        web = (WebView) view.findViewById(R.id.web);
        web.getSettings().setJavaScriptEnabled(true);

        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            web.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        web.getSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小
        web.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        web.getSettings().setLoadsImagesAutomatically(true); //支持自动加载图片
        web.loadUrl("http://47.98.131.11:8084/admin/goods/detail/show?goodsId="+id);
    }

    public void setId(String s){
         id =s ;
    }
        @Override
    public void onClick(View v) {

    }
}
