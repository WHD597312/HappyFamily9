package com.xr.happyFamily.jia.xnty;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import android.graphics.Paint.FontMetricsInt;
import com.xr.happyFamily.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class BeautySeekBarView extends View {
    private Semaphore sePoolTH=new Semaphore(0);//信号量，解决并发问题

    private int valueCountent;//等级点的数量
    private int pointColor;
    private int lineColor;
    private Bitmap mBitmap;
    private int bitmapWidth;
    private int bitmapHeight;
    private float bitmapPointX;
    private ArrayList<Float> pointList;//储存画出的点的point值
    private HashMap<Float, Float> mHashMap;////把差值和listX当做键值对保存起来，便于后期找出
    private int index=1;//索引
    private float mListX;//移动后最小的点
    private int smallPic;
    private int bigPic;
    private int viewPadding;

    private Paint pointPaint;
    private Paint linePaint;
    private Paint textPaint;
    private FontMetricsInt fontMetrics;

    private int minValue;
    private int maxValue;
    private int curValue;

    public BeautySeekBarView(Context context) {
        this(context,null);
    }
    public BeautySeekBarView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public BeautySeekBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BeautySeekBarView, defStyleAttr, 0);
        //等级数量即点的个数
        valueCountent=a.getInteger(R.styleable.BeautySeekBarView_valueCountent, 70);
        //点的颜色
        pointColor = a.getColor(R.styleable.BeautySeekBarView_pointColor,this.getResources().getColor(R.color.green2));
        //线的颜色
        lineColor = a.getColor(R.styleable.BeautySeekBarView_lineColor, this.getResources().getColor(R.color.color_gray2));
        //小图片
        smallPic=a.getResourceId(R.styleable.BeautySeekBarView_smallPic, R.mipmap.csj_bz3x);
        //滑动过程中的大图片
        bigPic=a.getResourceId(R.styleable.BeautySeekBarView_bigPic, R.mipmap.csj_bz3x);
        //控件的内边距
        viewPadding=a.getDimensionPixelSize(R.styleable.BeautySeekBarView_padding, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 46, getResources().getDisplayMetrics()));
        minValue=a.getInt(R.styleable.BeautySeekBarView_minValue,20);
        maxValue=a.getInt(R.styleable.BeautySeekBarView_maxValue,90);
        curValue=a.getInt(R.styleable.BeautySeekBarView_curValue,20);


        a.recycle();

        initData();//初始化数据
        initPaint();//初始化画笔
    }
    public void initData() {
//   valueCountent=7;
//   pointColor=Color.WHITE;
//   lineColor=Color.WHITE;
//   setBackgroundColor(Color.BLACK);
        setPadding(viewPadding, viewPadding, viewPadding, viewPadding);
        bitmapPointX=getPaddingLeft();
        mBitmap= BitmapFactory.decodeResource(getResources(), smallPic);
        bitmapWidth=mBitmap.getWidth();
        bitmapHeight=mBitmap.getHeight();
        pointList=new ArrayList<Float>();
        mHashMap=new HashMap<Float, Float>();
    }
    public void initPaint() {
        pointPaint=new Paint();
        pointPaint.setColor(pointColor);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setStrokeWidth(10);
        pointPaint.setStrokeJoin(Paint.Join.ROUND);
        pointPaint.setStrokeCap(Paint.Cap.ROUND);
        pointPaint.setAntiAlias(true);

        linePaint=new Paint();
        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(4);
        linePaint.setAntiAlias(true);

        textPaint=new Paint();
        textPaint.setStrokeWidth(3);
        textPaint.setTextSize(54);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        fontMetrics = textPaint.getFontMetricsInt();
        textPaint.setAntiAlias(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        float PointX = 0;
        float PointY=getHeight()/2;
        canvas.drawLine(0+getPaddingLeft(),PointY, getWidth()-getPaddingRight(), PointY, linePaint); //绘制直线

        int averageLength =(getWidth()-getPaddingLeft()-getPaddingRight())/(valueCountent-1);
        for(int i=0;i<valueCountent;i++){
            PointX=i*averageLength+getPaddingLeft();
            canvas.drawPoint(PointX, PointY, pointPaint);//绘制点

            if(pointList!=null && pointList.size()<valueCountent){
                pointList.add(PointX);//把每个点都放入集合中；
            }
        }
        sePoolTH.release();
        canvas.drawBitmap(mBitmap, bitmapPointX-bitmapWidth/2, PointY-bitmapHeight/2-90, null);//绘制拖动的图片
        canvas.drawText(""+index, bitmapPointX, (getHeight() - fontMetrics.ascent - fontMetrics.descent) / 2-90, textPaint); //绘制文字
    }

    long startTime = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取手指的操作--》按下、移动、松开
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startTime=System.currentTimeMillis();
                mBitmap=BitmapFactory.decodeResource(getResources(), bigPic);
                bitmapWidth=mBitmap.getWidth();
                bitmapHeight=mBitmap.getHeight();
                textPaint.setTextSize(70);
                updateIndex(bitmapPointX);
                invalidate();

                break;
            case MotionEvent.ACTION_MOVE:
                long endTimeMove=System.currentTimeMillis();
                if(endTimeMove-startTime>100){//如果按下，抬起时间过大才认为是拖动，要执行动画。
                    bitmapPointX=event.getX();
                    updateIndex(bitmapPointX);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                long endTime=System.currentTimeMillis();
                bitmapPointX=event.getX();
                mBitmap=BitmapFactory.decodeResource(getResources(),smallPic);
                bitmapWidth=mBitmap.getWidth();
                bitmapHeight=mBitmap.getHeight();
                textPaint.setTextSize(54);
                if(endTime-startTime>200){//如果按下，抬起时间过大才认为是拖动，要执行动画。
                    updateBitmapUI(bitmapPointX);
                }else{
                    bitmapPointX=updateIndex(bitmapPointX);
                    invalidate();
                }
                startTime = 0;
                break;
        }
        return true;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setCurValue(int curValue) {
        this.curValue = curValue;
    }

    public int getCurValue() {
        return curValue;
    }

    //更新索引
    public float updateIndex(float pointX){
        float lastValue=100000;
        float currentValue=0;
        float minValue=0;
        for(float listX:pointList){
            currentValue= Math.abs(pointX-listX);
            mHashMap.put(currentValue, listX);//把差值和listX当做键值对保存起来，便于后期找出
            minValue=Math.min(lastValue,currentValue);
            lastValue=minValue;
        }
        if(mHashMap.containsKey(minValue)){
            mListX=mHashMap.get(minValue);
        }else{
            Log.e("BeautySeekBarView", "updateBitmapUI--->mHashMap.containsKey(minValue) is null");
            return -1;
        }
        if(pointList.contains(mListX)){
            index=pointList.indexOf(mListX)+20;
            if(mListener!=null){
                mListener.getIndex(index);
            }
        }else{
            Log.e("BeautySeekBarView", "updateBitmapUI--->pointList.contains(mListX) is null");
            return -1;
        }
        return mListX;
    }

    //当手指抬起后更新Bitmap的位置
    private void updateBitmapUI(float PointX2) {
        mListX=updateIndex(PointX2);
        //执行动画
        ValueAnimator anim = ValueAnimator.ofFloat(PointX2, mListX);
        anim.setDuration(50);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bitmapPointX =(Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        anim.start();
    }

    //设置等级点的数量
    public void pointValueCountent(int countent){
        if(countent<2){
            valueCountent=2;
        }else{
            valueCountent=countent;
        }
        invalidate();
    }

    //设置默认位置
    public void setPointLocation(final int location){
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    sePoolTH.acquire();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if(location>0&&pointList!=null&& !pointList.isEmpty()){
                    bitmapPointX=pointList.get(location-1);
                    postInvalidate();
                }

            }
        }).start();

    }

    //提供接口回调，获取索引
    private indexListener mListener=null;
    public interface indexListener{
        void getIndex(int index);
    }
    public void setIndexListener(indexListener listener){
        mListener=listener;
    }

}

