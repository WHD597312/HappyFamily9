package com.xr.happyFamily.le.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;

import com.xr.happyFamily.R;

/**
 * Created by youzehong on 16/4/19.
 */
public class TimeBar extends View {

    private Paint mArcPaint;
    private Paint mTextPaint;
    private Paint timePain;
    private Paint mDottedLinePaint;
    private Paint mRonudRectPaint;
    private RectF mArcRect;
    /**
     * 圆弧宽度
     */
    private float mArcWidth = 15.0f;
    /**
     * 圆弧颜色
     */
    private int mArcBgColor = 0xFFFF916D;
    /**
     * 虚线默认颜色
     */
    private int mDottedDefaultColor = 0xFF8D99A1;
    /**
     * 虚线变动颜色
     */
    private int mDottedRunColor = 0xFFf0724f;
    /**
     * 圆弧两边的距离
     */
    private int mPdDistance = 80;
    /**
     * 底部默认文字
     */
   private String mArcText= "";
    /**
     * 线条数
     */
    private int mDottedLineCount = 24*60;
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
    private int mArcCenterX;
    private int mArcRadius; // 圆弧半径
    private double bDistance;
    private double aDistance;
    private boolean isRestart = false;
    private int mRealProgress=0;
    int sign=0;

    public TimeBar(Context context) {
        this(context, null, 0);
    }

    public TimeBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeBar(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mArcText = a.getString(R.styleable.ArcProgressBar_arcText);
        curProgress=a.getInteger(R.styleable.ArcProgressBar_arcDistance,0);
      /*  if (TextUtils.isEmpty(mArcText)) {
            mArcText= "限时特卖";
        }*/
        a.recycle();
    }

    private void initView() {
        int[] screenWH = getScreenWH();
        mScressWidth = screenWH[0];
        // 外层圆弧的画笔
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mArcWidth);
        mArcPaint.setColor(getResources().getColor(R.color.color_gray_clock));
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        //
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(dp2px(getResources(), 16));
        mTextPaint.setColor(Color.WHITE);

        timePain = new Paint();
        timePain.setAntiAlias(true);
        timePain.setTextSize(dp2px(getResources(), 12));
        timePain.setColor(Color.parseColor("#918521"));
        timePain.setTextAlign(Paint.Align.CENTER);
        // 内测虚线的画笔
        mDottedLinePaint = new Paint();
        mDottedLinePaint.setAntiAlias(true);
//        mDottedLinePaint.setStrokeWidth(mDottedLineHeight);
        mDottedLinePaint.setColor(getResources().getColor(R.color.color_gray2));
        mDottedLinePaint.setColor(mDottedDefaultColor);
        //
        mRonudRectPaint = new Paint();
        mRonudRectPaint.setAntiAlias(true);
        mRonudRectPaint.setColor(mDottedRunColor);
        mRonudRectPaint.setStyle(Paint.Style.FILL);
        // 中间进度画笔
//        mProgressPaint = new Paint();
//        mProgressPaint.setAntiAlias(true);
////        mProgressPaint.setColor(mDottedRunColor);
//        mProgressPaint.setColor(getResources().getColor(R.color.green2));
//        mProgressPaint.setTextSize(dp2px(getResources(), mProgressTextSize));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = mScressWidth /*- 2 * mPdDistance*/;
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mArcCenterX = (int) (w / 2.f);

        mArcRect = new RectF();
        mArcRect.top = h/8;
        mArcRect.left = w/8;
        mArcRect.right = w/8*7;
        mArcRect.bottom = h/8*7;

        mArcRect.inset(mArcWidth / 2, mArcWidth / 2);
        mArcRadius = (int) (mArcRect.width() / 2);

        double sqrt = Math.sqrt(mArcRadius * mArcRadius + mArcRadius * mArcRadius);
        bDistance = Math.cos(Math.PI * 45 / 180) * mArcRadius;
        aDistance = Math.sin(Math.PI * 45 / 180) * mArcRadius;
        mExternalDottedLineRadius = mArcRadius ;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(mArcRect, 0, 360, false, mArcPaint);
        drawRunDottedLineArc(canvas);
    }

//    private void drawRunText(Canvas canvas) {
//        String progressStr = this.mRealProgress + "%";
//        if (!TextUtils.isEmpty(mProgressDesc)) {
//            progressStr = mProgressDesc;
//        }
//        canvas.drawText(progressStr, mArcCenterX - mProgressPaint.measureText(progressStr) / 2,
//                mArcCenterX - (mProgressPaint.descent() + mProgressPaint.ascent()) / 2 - 20, mProgressPaint);
//    }

    public void restart() {
//        isRestart = true;
     /*   this.mRealProgress = 0;
        this.mProgressDesc = "";
        invalidate();*/
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
        isRestart = false;
        this.mProgress = ((mDottedLineCount * 3 / 4) * progress) / mProgressMax;
        postInvalidate();
    }
    float degrees2,evenryDegrees2,startDegress2;
    private void drawRunDottedLineArc(Canvas canvas) {
//        mDottedLinePaint.setColor(mDottedRunColor);
        mDottedLinePaint.setColor(getResources().getColor(R.color.green2));
        float evenryDegrees = (float) (2.0f * Math.PI / mDottedLineCount);

//        float startDegress = (float) (225 * Math.PI / 180) + evenryDegrees / 4;
        float startDegress =  evenryDegrees / 4;
        Log.i("start", "---->"+evenryDegrees);



        for (int i = 0; i <myTime.length ; i++) {
            sign=myTime[i][0]*60+myTime[i][1];
            float degrees = sign * evenryDegrees + startDegress;
            float startX = mArcCenterX + (float) Math.sin(degrees) * mExternalDottedLineRadius;
            float startY = mArcCenterX - (float) Math.cos(degrees) * mExternalDottedLineRadius;

            Bitmap bitmap;
            if(type==1)
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_clock_qinglv);
            else
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_clock_qunzu);
            canvas.drawBitmap(bitmap, (startX-bitmap.getWidth()/2), (startY-bitmap.getHeight()/2), mDottedLinePaint);


            degrees2= sign * evenryDegrees ;
            Log.e("qqqqqqqqq",degrees2+"??");
//            if(sign<360){
//                evenryDegrees2=evenryDegrees*95/90;
//                startDegress2=(float) (2.0f * Math.PI / 360*5);
//                degrees2= sign * evenryDegrees2 - startDegress2+ startDegress;
//            }else if(sign<720){
//                evenryDegrees2=evenryDegrees;
//                startDegress2=(float) (2.0f * Math.PI / 360*5);
//                degrees2= (float) (2.0f * Math.PI / 360*90)+(sign-360) * evenryDegrees2 + startDegress2+ startDegress;
//            }else if(sign<1080){
//                evenryDegrees2=evenryDegrees*80/90;
//                startDegress2=(float) (2.0f * Math.PI / 360*5);
//                degrees2= (float) (2.0f * Math.PI / 360*180)+(sign-720) * evenryDegrees2 + startDegress2+ startDegress;
//            }else if(sign<1441){
//                evenryDegrees2=evenryDegrees*80/90;
//                startDegress2=(float) (2.0f * Math.PI / 360*5);
//                degrees2= (float) (2.0f * Math.PI / 360*270)+(sign-1080) * evenryDegrees2 - startDegress2+ startDegress;
//            }

            float startX2 = mArcCenterX + (float) Math.sin(degrees2) * (mExternalDottedLineRadius+85);
            float startY2 = mArcCenterX - (float) Math.cos(degrees2) * (mExternalDottedLineRadius+85);

            String time = "";
            if(myTime[i][0]<10){
                time="0"+myTime[i][0];
            }else time=+myTime[i][0]+"";
            if(myTime[i][1]<10){
                time=time+":0"+myTime[i][1];
            }else time=time+":"+myTime[i][1];
            canvas.drawText(time, startX2, startY2+10 , timePain);
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



    public int getSign(){
        this.sign = sign;
        return sign;

    }

    public interface OnSeekBarChangeListener {
        void onChanged(TimeBar seekbar, int curValue);
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


    int[][] myTime={{6,0},{12,00},{18,00},{24,00}};


    int type=0;
    //type判断模式，0：群组，1：情侣
    public void setTime(int[][] time,int type){
        this.type=type;
        myTime=new int[time.length][2];
        myTime=time;
        invalidate();

    }
}
