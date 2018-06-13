package com.xr.happyFamily.le.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Random;

public class MyHorizontalScrollView extends HorizontalScrollView {

    private int mScreenWidth,mScreenHeight,mChildHeight;
    private Context context;

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setSmoothScrollingEnabled(true);
    }

    public void setAdapter(Context context, MyHorizontalScrollViewAdapter mAdapter) {
        this.context=context;
        try {
            fillAdapter(mAdapter);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void fillAdapter(MyHorizontalScrollViewAdapter mAdapter) throws Exception {
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;

        if (getChildCount() == 0) {
            throw new Exception("CmbHorizontalScrollView must have one child");
        }
        if (getChildCount() == 0 || mAdapter == null)
            return;

        ViewGroup parent = (ViewGroup) getChildAt(0);

        parent.removeAllViews();
        for (int i = 0; i < mAdapter.getCount(); i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if(i==0){
                layoutParams.leftMargin=new Random().nextInt(mScreenWidth);
            }
            else layoutParams.leftMargin=new Random().nextInt(mScreenWidth/6)+mScreenWidth/8;
            layoutParams.topMargin=new Random().nextInt(mScreenHeight/10);
            parent.addView(mAdapter.getView(i, null, parent),layoutParams);

        }
    }
}