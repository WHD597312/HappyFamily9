package com.xr.happyFamily.jia.view_custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by win7 on 2018/5/21.
 */

//线性渲染View  用于模糊渐变
public class LinearGradientView extends View {


    public LinearGradientView(Context context)
    {
        super(context);
    }

    public LinearGradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setAlpha(60);
        LinearGradient linearGradient = new LinearGradient(0, 0, 0, getMeasuredHeight(),new int[]{Color.parseColor("#C8C8C8"),Color.parseColor("#f8f8f8")}, null, LinearGradient.TileMode.CLAMP);
        paint.setShader(linearGradient);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),paint);
    }
}
