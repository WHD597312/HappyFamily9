package com.xr.happyFamily.jia.xnty;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.xr.happyFamily.R;

import java.util.ArrayList;
import java.util.List;

public class DiffuseView extends View {

    /** 扩散圆圈颜色 */
    private int mColor = getResources().getColor(R.color.white);
    /** 圆圈中心颜色 */
    private int mCoreColor = getResources().getColor(R.color.colorPrimary);
    /** 圆圈中心图片 */
    private Bitmap mBitmap;
    /** 中心圆半径 */
    private float mCoreRadius = 250;
    /** 扩散圆宽度 */
    private int mDiffuseWidth = 5;
    /** 最大宽度 */
    private Integer mMaxWidth = 255;
    /** 是否正在扩散中 */
    private boolean mIsDiffuse = false;
    // 透明度集合
    private List<Integer> mAlphas = new ArrayList<>();
    // 扩散圆半径集合
    private List<Integer> mWidths = new ArrayList<>();
    private Paint mPaint;
    private int mScressWidth;
    public DiffuseView(Context context) {
        this(context, null);
    }

    public DiffuseView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DiffuseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DiffuseView, defStyleAttr, 0);
        mColor = a.getColor(R.styleable.DiffuseView_diffuse_color, mColor);
        mCoreColor = a.getColor(R.styleable.DiffuseView_diffuse_coreColor, mCoreColor);
        mCoreRadius = a.getFloat(R.styleable.DiffuseView_diffuse_coreRadius, mCoreRadius);
        mDiffuseWidth = a.getInt(R.styleable.DiffuseView_diffuse_width, mDiffuseWidth);
        mMaxWidth = a.getInt(R.styleable.DiffuseView_diffuse_maxWidth, mMaxWidth);
        int imageId = a.getResourceId(R.styleable.DiffuseView_diffuse_coreImage, -1);
        if(imageId != -1) mBitmap = BitmapFactory.decodeResource(getResources(), imageId);
        a.recycle();
    }

    private void init() {
        int[] screenWH = getScreenWH();
        mScressWidth = screenWH[0];
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mAlphas.add(110);
        mWidths.add(0);
    }

//    @Override
//    public void invalidate() {
//        if(hasWindowFocus()){
//            super.invalidate();
//        }
//    }
private int[] getScreenWH() {
    WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics displayMetrics = new DisplayMetrics();
    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
    int[] wh = {displayMetrics.widthPixels, displayMetrics.heightPixels};
    return wh;
}
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = mScressWidth /*- 2 * mPdDistance*/;
        mCoreRadius=3*width/9;
        setMeasuredDimension(width, width);
    }//获取屏幕宽度

    private float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    @Override
    public void onDraw(Canvas canvas) {
        // 绘制扩散圆
        mPaint.setColor(mColor);
        for (int i = 0; i < mAlphas.size(); i++) {
            // 设置透明度
            Integer alpha = mAlphas.get(i);
            mPaint.setAlpha(alpha);
            // 绘制扩散圆
            Integer width = mWidths.get(i);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mCoreRadius + width, mPaint);

            if(alpha > 0 && width < mMaxWidth){
                mAlphas.set(i, alpha - 1);
                mWidths.set(i, width + 1);
            }
        }
        // 判断当扩散圆扩散到指定宽度时添加新扩散圆
        if (mWidths.get(mWidths.size() - 1) == mMaxWidth / mDiffuseWidth) {
            mAlphas.add(110);
            mWidths.add(0);
        }
        // 超过10个扩散圆，删除最外层
        if(mWidths.size() >= 10){
            mWidths.remove(0);
            mAlphas.remove(0);
        }

        // 绘制中心圆及图片
        mPaint.setAlpha(110);
        mPaint.setColor(mCoreColor);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mCoreRadius, mPaint);

        if(mBitmap != null){
            canvas.drawBitmap(mBitmap, getWidth() / 2 - mBitmap.getWidth() / 2
                    , getHeight() / 2 - mBitmap.getHeight() / 2, mPaint);
        }

        if(mIsDiffuse){
            invalidate();
        }
    }

    /**
     * 开始扩散
     */
    public void start() {
        mIsDiffuse = true;
        invalidate();
    }

    /**
     * 停止扩散
     */
    public void stop() {
        mIsDiffuse = false;
    }

    /**
     * 是否扩散中
     */
    public boolean isDiffuse(){
        return mIsDiffuse;
    }

    /**
     * 设置扩散圆颜色
     */
    public void setColor(int colorId){
        mColor = colorId;
    }

    /**
     * 设置中心圆颜色
     */
    public void setCoreColor(int colorId){
        mCoreColor = colorId;
    }

    /**
     * 设置中心圆图片
     */
    public void setCoreImage(int imageId){
        mBitmap = BitmapFactory.decodeResource(getResources(), imageId);
    }

    /**
     * 设置中心圆半径
     */
    public void setCoreRadius(int radius){
        mCoreRadius = radius;
    }

    /**
     * 设置扩散圆宽度(值越小宽度越大)
     */
    public void setDiffuseWidth(int width){
        mDiffuseWidth = width;
    }

    /**
     * 设置最大宽度
     */
    public void setMaxWidth(int maxWidth){
        mMaxWidth = maxWidth;
    }
}
