package com.xr.happyFamily.jia;

import android.content.Context;
import android.util.AttributeSet;

import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.xr.happyFamily.R;

public class Myscrollview extends ScrollView {

    public Myscrollview(Context context)
    {
        super(context);
    }
    public Myscrollview(Context context, AttributeSet attributeSet)
    {
        super(context,attributeSet);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        View view = (View)getChildAt(getChildCount()-1);
        int d = view.getBottom() ;
        d -= (getHeight()+getScrollY());

        if(d==0) {

        }
        else{
            super.onScrollChanged(l,t,oldl,oldt);}
    }
}

