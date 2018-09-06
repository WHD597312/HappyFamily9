package com.xr.happyFamily.bao.view;

import android.content.Context;
import android.util.AttributeSet;

import com.youth.banner.Banner;

public class ShopBanner extends Banner {

    public ShopBanner(Context context) {
        super(context);
    }

    public ShopBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShopBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}