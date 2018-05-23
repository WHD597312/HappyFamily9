package com.xr.happyFamily.jia.xnty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.graphics.Paint.Style;
import android.view.View;
import android.view.animation.Transformation;

public class RunningNumView extends View {
    private int count, digit;
    private String text;
    private MyAnimation anim;
    private Paint textPaint;
    private int textSize;
    private RectF frameRectangle = new RectF();


    public RunningNumView(Context context) {
        super(context);
        init();
    }

    public RunningNumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RunningNumView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init(){
        text = "0";
        anim = new MyAnimation();

        final float scale = getContext().getResources().getDisplayMetrics().density;
        textSize = (int) (33 * scale + 0.5f);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.LINEAR_TEXT_FLAG);
        textPaint.setColor(0xFFFFFFFF);
        textPaint.setStyle(Style.FILL_AND_STROKE);
        textPaint.setTextSize(textSize);
    }

    @SuppressLint("DrawAllocation") @Override
    protected void onDraw(Canvas canvas){
//处理下要显示出来的数字，这里是为了获得个十百位中的一位
        String textStr = (count % (int)Math.pow(10,digit))/(int)Math.pow(10,digit-1) + "";

        Rect bounds = new Rect();
        textPaint.getTextBounds(textStr, 0, textStr.length(), bounds);

        //画出数字
        canvas.drawText(textStr,
                frameRectangle.centerX() - (textPaint.measureText(textStr) / 2),
                frameRectangle.centerY() + bounds.height() / 2,
                textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);

        frameRectangle.set(0, 0, min, min);
    }

    /**
     *
     * @param text 最终数字
     * @param digit 要显示的数位（1/2/3）
     */
    public RunningNumView setTextAndDigit(String text, int digit){
        this.text = text;
        this.digit = digit;
        return this;
    }

    public RunningNumView setDuration(long durationMillis){
        anim.setDuration(durationMillis);
        return this;
    }

    public void startAnimation(){
        this.startAnimation(anim);
    }

    public class MyAnimation extends Animation{
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if(interpolatedTime < 1.0f){
                //随着动画播放，interpolatedTime会由0变化到1，通过它，我们可以获取变化中的数字（乘以下总数就行了）
                count = (int)(interpolatedTime * Float.parseFloat(text));
            }else{
                count = Integer.parseInt(text);
            }
            postInvalidate();//调用onDraw()
        }
    }
}
