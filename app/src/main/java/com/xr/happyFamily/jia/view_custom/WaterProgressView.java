package com.xr.happyFamily.jia.view_custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.widget.ProgressBar;

import com.xr.happyFamily.R;

public class WaterProgressView extends ProgressBar {
    //默认圆的背景色
    public static final int DEFAULT_CIRCLE_COLOR = 0xff00cccc;
    //默认进度的颜色
    public static final int DEFAULT_PROGRESS_COLOR = 0xff00CC66;
    //默认文字的颜色
    public static final int DEFAULT_TEXT_COLOR = 0xffffffff;
    //默认文字的大小
    public static final int DEFAULT_TEXT_SIZE = 18;
    //默认的波峰最高点
    public static final int DEFAULT_RIPPLE_TOPHEIGHT = 10;

    private Context mContext;
    private Canvas mPaintCanvas;
    private Bitmap mBitmap;

    //画圆的画笔
    private Paint mCirclePaint;
    //画圆的画笔的颜色
    private int mCircleColor;

    //画进度的画笔
    private Paint mProgressPaint;
    //画进度的画笔的颜色
    private int mProgressColor ;
    //画进度的path
    private Path mProgressPath;
    //贝塞尔曲线波峰最大值
    private int mRippleTop = 10;

    //进度文字的画笔
    private Paint mTextPaint;
    //进度文字的颜色
    private int mTextColor;
    private int mTextSize = 18;
    //目标进度，也就是双击时处理任务的进度，会影响曲线的振幅
    private int mTargetProgress = 50;

    //监听双击和单击事件
    private GestureDetector mGestureDetector;
    //当new该类时调用此构造函数
    public WaterProgressView(Context context) {
        this(context,null);
    }

    //当xml文件中定义该自定义View时调用此构造函数
    public WaterProgressView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaterProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        getAttrValue(attrs);
        //初始化画笔的相关属性
        initPaint();
        mProgressPath = new Path();
    }
    private void getAttrValue(AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.WaterProgressView);
        mCircleColor = ta.getColor(R.styleable.WaterProgressView_circle_color,DEFAULT_CIRCLE_COLOR);
        mProgressColor = ta.getColor(R.styleable.WaterProgressView_progress_color,DEFAULT_PROGRESS_COLOR);
        mTextColor = ta.getColor(R.styleable.WaterProgressView_text_color,DEFAULT_TEXT_COLOR);
        mTextSize = (int) ta.getDimension(R.styleable.WaterProgressView_text_size, dip2px(DEFAULT_TEXT_SIZE));
        mRippleTop = (int)ta.getDimension(R.styleable.WaterProgressView_ripple_topheight,dip2px(DEFAULT_RIPPLE_TOPHEIGHT));
        ta.recycle();
    }

    public int dip2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getContext().getResources().getDisplayMetrics());
    }
    private void initPaint() {
        //初始化画圆的paint
        mCirclePaint = new Paint();
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);

        //初始化画进度的paint
        mProgressPaint = new Paint();
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setDither(true);
        mProgressPaint.setStyle(Paint.Style.FILL);
        //其实mProgressPaint画的也是矩形，当设置xfermode为PorterDuff.Mode.SRC_IN后则显示的为圆与进度矩形的交集，则为半圆
        mProgressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //初始化画进度文字的画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(mTextSize);

    }
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //使用时，需要明确定义该View的尺寸，即用测量模式为MeasureSpec.EXACTLY
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width,height);

        //初始化Bitmap，让所有的drawCircle，drawPath，drawText都draw在该bitmap所在的canvas上，然后再将该bitmap 画在onDraw方法的canvas上，
        //所以此bitmap的width,height需要减去left,top,right,bottom的padding
        mBitmap = Bitmap.createBitmap(width-getPaddingLeft()-getPaddingRight(),height- getPaddingTop()-getPaddingBottom(), Bitmap.Config.ARGB_8888);
        mPaintCanvas = new Canvas(mBitmap);
    }
}
