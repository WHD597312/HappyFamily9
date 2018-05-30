package com.xr.happyFamily.jia.xnty;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.animation.Animation;
import com.xr.happyFamily.R;

public class MyView extends View {
    public Animation mAnimationRoate;
    //define bitmap object
    Bitmap mBitmap = null;
    public MyView(Context context) {
        super(context);
        //load resource
        mBitmap = ((BitmapDrawable)getResources().getDrawable(R.mipmap.zncz_hw)).getBitmap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint mPaint = null;
        //draw pic
        canvas.drawBitmap(mBitmap,0,40,null);
    }
}
