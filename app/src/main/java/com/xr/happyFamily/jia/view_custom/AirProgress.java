package com.xr.happyFamily.jia.view_custom;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.xr.happyFamily.R;

/**
 * Created by youzehong on 16/4/19.
 */
public class AirProgress extends View {

    private Paint mArcPaint;
    private Paint mTextPaint;
    private Paint mDottedLinePaint;
    private Paint mRonudRectPaint;
    private RectF mArcRect;
    /**
     * 圆弧宽度
     */
    private float mArcWidth = 20.0f;
    /**
     * 圆弧颜色
     */
    private int mArcBgColor = 0xFFFF916D;
    /**
     * 虚线默认颜色
     */
    private int mDottedDefaultColor = 0xFFD6D7D2;
    /**
     * 虚线变动颜色
     */
    private int mDottedRunColor = 0xFFf0724f;
    /**
     * 圆弧两边的距离
     */
    private int mPdDistance = 80;
    /**
     * 线条数
     */
    private int mDottedLineCount = 22;
    /**
     * 线条宽度
     */
    private int mDottedLineWidth = 40;
    /**
     * 线条高度
     */
    private int mDottedLineHeight = 6;
    /**
     * 圆弧跟虚线之间的距离
     */
    private int mLineDistance = 20;
    /**
     * 进度条最大值
     */
    private int mProgressMax = 30;

    private int curProgress;/**进度*/
    /**
     * 进度文字大小
     */
    private int mProgressTextSize = 65;
    /**
     * 进度描述
     */
    private String mProgressDesc;

    private int mScressWidth;
    private int mProgress;
    private float mExternalDottedLineRadius;
    private float mInsideDottedLineRadius;
    private int mArcCenterX;
    private int mArcRadius; // 圆弧半径
    private int mRealProgress=0;
    int sign=0;

    public AirProgress(Context context) {
        this(context, null, 0);
    }

    public AirProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AirProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intiAttributes(context, attrs);
        initView();
    }

    private void intiAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ArcProgressBar);
        mPdDistance = a.getInteger(R.styleable.ArcProgressBar_arcDistance, mPdDistance);
        mArcBgColor = a.getColor(R.styleable.ArcProgressBar_arcBgColor, mArcBgColor);
        mDottedDefaultColor = a.getColor(R.styleable.ArcProgressBar_dottedDefaultColor, mDottedDefaultColor);
        mDottedRunColor = a.getColor(R.styleable.ArcProgressBar_dottedRunColor, mDottedRunColor);
        mDottedLineCount = a.getInteger(R.styleable.ArcProgressBar_dottedLineCount, mDottedLineCount);
        mDottedLineWidth = a.getInteger(R.styleable.ArcProgressBar_dottedLineWidth, mDottedLineWidth);
        mDottedLineHeight = a.getInteger(R.styleable.ArcProgressBar_dottedLineHeight, mDottedLineHeight);
        mLineDistance = a.getInteger(R.styleable.ArcProgressBar_lineDistance, mLineDistance);
        mProgressMax = a.getInteger(R.styleable.ArcProgressBar_progressMax, mProgressMax);
        mProgressTextSize = a.getInteger(R.styleable.ArcProgressBar_progressTextSize, mProgressTextSize);
        mProgressDesc = a.getString(R.styleable.ArcProgressBar_progressDesc);
        curProgress=a.getInteger(R.styleable.ArcProgressBar_arcDistance,0);
        a.recycle();
    }

    private void initView() {
        int[] screenWH = getScreenWH();
        mScressWidth = screenWH[0];
        Log.e("qqqqqqqHHHHH",mScressWidth+"??");
        // 外层圆弧的画笔
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mArcWidth);
        mArcPaint.setColor(getResources().getColor(R.color.white));
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        //
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(dp2px(getResources(), 16));
        mTextPaint.setColor(Color.WHITE);


        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(dp2px(getResources(), 12));
        mTextPaint.setColor(Color.parseColor("#72828B"));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        // 内测虚线的画笔
        mDottedLinePaint = new Paint();
        mDottedLinePaint.setAntiAlias(true);
//        mDottedLinePaint.setStrokeWidth(mDottedLineHeight);
        mDottedLinePaint.setColor(Color.parseColor("#D6D7D2"));
        mRonudRectPaint = new Paint();
        mRonudRectPaint.setAntiAlias(true);
        mRonudRectPaint.setColor(mDottedRunColor);
        mRonudRectPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 4*mScressWidth/5 /*- 2 * mPdDistance*/;

        Log.e("qqqqqqqqqqXYXXXXX",mScressWidth+"?");
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mArcCenterX = (int) (w / 2.f);
        mArcRect = new RectF();
        mArcRect.top = 0;
        mArcRect.left = 0;
        mArcRect.right = w;
        mArcRect.bottom = h;

        mArcRect.inset(mArcWidth / 2, mArcWidth / 2);
        mArcRadius = (int) (mArcRect.width() / 2);

        // 内部虚线的外部半径
        mExternalDottedLineRadius = mArcRadius - mArcWidth / 2 - mLineDistance+30;
        // 内部虚线的内部半径
        mInsideDottedLineRadius = mExternalDottedLineRadius - mDottedLineWidth;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDottedLineArc(canvas);
        drawRunDottedLineArc(canvas);
    }

    public void restart() {
        mRealProgress=0;
        sign=0;
        invalidate();
    }

    /**
     * 设置中间进度描述
     *
     * @param desc
     */
    public void setProgressDesc(String desc) {
        this.mProgressDesc = desc;
        postInvalidate();
    }

    /**
     * 设置最大进度
     *
     * @param max
     */
    public void setMaxProgress(int max) {
        this.mProgressMax = max;
    }

    public void setCurProgress(int curProgress) {
        this.curProgress = curProgress;
    }

    public int getCurProgress() {
        return curProgress;
    }

    /**
     * 设置当前进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        // 进度100% = 控件的75%

        this.mRealProgress = progress;
        this.mProgress = ((mDottedLineCount * 3 / 4) * progress) / mProgressMax;
        postInvalidate();
    }

    float degrees2;
    private void drawRunDottedLineArc(Canvas canvas) {
//        mDottedLinePaint.setColor(mDottedRunColor);
        mDottedLinePaint.setColor(getResources().getColor(R.color.green2));
        float evenryDegrees = (float) (2.0f * Math.PI / mDottedLineCount);

        float startDegress = (float) (225 * Math.PI / 180) + evenryDegrees / 4;
        Log.i("start", "---->"+mProgress);

        Log.e("qqqqSSS22222",sign+"?");
        for (int i = 0; i <=sign; i++) {
            float degrees = i * evenryDegrees + startDegress;
            float startX = mArcCenterX + (float) Math.sin(degrees) * mInsideDottedLineRadius;
            float startY = mArcCenterX - (float) Math.cos(degrees) * mInsideDottedLineRadius;

            float stopX = mArcCenterX + (float) Math.sin(degrees) * mExternalDottedLineRadius;
            float stopY = mArcCenterX - (float) Math.cos(degrees) * mExternalDottedLineRadius;
            if(i==sign){
                canvas.drawCircle((startX+stopX)/2,  (startY +stopY)/2, 26, mDottedLinePaint);
            }

//            canvas.drawLine(startX, startY, stopX, stopY, mDottedLinePaint);
            else canvas.drawCircle((startX+stopX)/2,  (startY +stopY)/2, 18, mDottedLinePaint);
        }
    }

    private void drawDottedLineArc(Canvas canvas) {
        mDottedLinePaint.setColor(mDottedDefaultColor);
        // 360 * Math.PI / 180
        float evenryDegrees = (float) (2.0f * Math.PI / mDottedLineCount);

        float startDegress = (float) (135 * Math.PI / 180);
        float endDegress = (float) (225 * Math.PI / 180);

        for (int i = 0; i < mDottedLineCount; i++) {
            float degrees = i * evenryDegrees;
            // 过滤底部90度的弧长
            if (degrees > startDegress && degrees < endDegress) {
                continue;
            }

            float startX = mArcCenterX + (float) Math.sin(degrees) * mInsideDottedLineRadius;
            float startY = mArcCenterX - (float) Math.cos(degrees) * mInsideDottedLineRadius;

            float stopX = mArcCenterX + (float) Math.sin(degrees) * mExternalDottedLineRadius;
            float stopY = mArcCenterX - (float) Math.cos(degrees) * mExternalDottedLineRadius;


//            canvas.drawLine(startX, startY, stopX, stopY, mDottedLinePaint);
            canvas.drawCircle((startX+stopX)/2,  (startY +stopY)/2, 18, mDottedLinePaint);

            degrees2= i * evenryDegrees ;
            float startX2 = mArcCenterX + (float) Math.sin(degrees2) * (mExternalDottedLineRadius-80);
            float startY2 = mArcCenterX - (float) Math.cos(degrees2) * (mExternalDottedLineRadius-80);

            Log.e("qqqqqq",startX2+"?"+startX2+"?"+mArcCenterX);
            int text=0;
            if(i>13)
                text=i+2;
            else
                text=i+24;
            if (text%2==0)
            canvas.drawText(text+"", startX2, startY2+10 , mTextPaint);
        }
    }

    private int[] getScreenWH() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int[] wh = {displayMetrics.widthPixels, displayMetrics.heightPixels};
        return wh;
    }

    private float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    private static final double RADIAN = 180 / Math.PI;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isOpen) {
            float x = event.getX();
            float y = event.getY();
            float cos = computeCos(x, y);
            int b = 0;
            float a = 145 / 9;
            double angle;
            if (x < getWidth() / 2) { // 滑动超过180度
                angle = Math.PI * RADIAN + Math.acos(cos) * RADIAN;
            } else { // 没有超过180度
                angle = Math.PI * RADIAN - Math.acos(cos) * RADIAN;
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if (angle > 225) {
                        sign = (int) ((angle - 225) / a);
                    } else {
                        sign = (int) ((angle) / a) + 9;
                    }
//                mRealProgress=sign*100/23;
                    Log.e("qqqqqSSS", sign + "?");
                    if (sign > 16 && sign < 20) {
                        mRealProgress = 16;
                        sign = 16;
                        invalidate();
                    } else if (sign >= 20) {
                        mRealProgress = 0;
                        sign = 0;
                    }
                    if (sign == 0)
                        invalidate();
                    if (sign != b) {
                        invalidate();
                    }
                    mDottedLinePaint.setColor(mDottedRunColor);
                    Log.i("start", "---->" + sign);

                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                    this.mProgress = ((mDottedLineCount * 3 / 4) * curProgress) / mProgressMax;
                        mChangListener.onChanged(this, sign);
                    }
                    return true;
                case MotionEvent.ACTION_DOWN:
                    b = sign;
                    return true;
                case MotionEvent.ACTION_UP:
                    mUpListener.onFinish(sign);
                    return true;
            }
        }

        return super.onTouchEvent(event);
    }
    private OnSeekBarChangeListener mChangListener;
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener) {
        mChangListener = listener;
    }

    public int getSign(){
        this.sign = sign;
        return sign;

    }

    public void setSign(int sign){
        this.sign=sign;
        postInvalidate();
    }

    public interface OnSeekBarChangeListener {
        void onChanged(AirProgress seekbar, int curValue);
    }


    public interface  onActionUpListener{
        void onFinish(int curValue);
    }
    private onActionUpListener mUpListener;
    public void setOnActionUpListener(onActionUpListener mUpListener) {
        this.mUpListener = mUpListener;
    }





    /**
     * 拿到倾斜的cos值
     */
    private float computeCos(float x, float y) {
        float width = x - getWidth() / 2;
        float height = y - getHeight() / 2;
        float slope = (float) Math.sqrt(width * width + height * height);
        return height / slope;
    }

    boolean isOpen=false;
    public void setOpen(boolean isOpen){
        this.isOpen=isOpen;
    }


    public void setHeight(int height){
        Log.e("qqqqqqqqXYMMMM","???????????");
        mScressWidth=height;
        setMeasuredDimension(getScreenWH()[0], height);
    }
}
