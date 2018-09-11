package com.xr.happyFamily.jia.view_custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.pojo.SmartWheelInfo;

import java.util.List;


/**
 * 作者：请叫我百米冲刺 on 2016/10/17 14:34
 * 邮箱：mail@hezhilin.cc
 */

public class SmartWheelBar extends View {


    /**
     * 两种种状态
     * 0 表示未被选中
     * 1 表示选中
     **/
    private static final int TYPE_UNCHECKED = 0;
    private static final int TYPE_CHECKED = 1;

    /**
     * 控件要绘制的宽度
     **/
    private int mDrawWidth;

    /**
     * 避免重复计算宽度
     **/
    private boolean hasMeasured = false;

    /**
     * 500毫秒内的点击被认为是点击事件
     **/
    private long timeClick = 500;

    int deviceState=0;

    /**
     * 保存点击的时间
     **/
    private long nowClick;

    /**
     * 被选中的position,默认第0个选中
     **/
    private int checkPosition = 0;

    /**
     * 绘画区域的图片。这边会将各个小图片拼接成一张图片
     **/
    private Bitmap fontBitmap;


    private List<SmartWheelInfo> bitInfos;


    /**
     * 选项的个数
     **/
    private int mItemCount;

    /**
     * 保存的画布
     **/
    private Canvas mCanvas;

    /**
     * 非选中和选择的颜色色块
     */
    private int[] mColors = new int[]{0xFFFFFF, 0xFFFFFF};

    /**
     * 触摸点的坐标位置
     **/
    private float touchX;
    private float touchY;

    /**
     * 绘制盘块的范围
     */
    private RectF mRange = new RectF();

    /**
     * 绘制盘快的画笔
     */
    private Paint mArcPaint;

    /**
     * 绘制文字的画笔
     */
    private TextPaint mTextPaint;

    /**
     * 绘制分割线的画笔
     */
    private Paint mLinePaint;

    /**
     * 圆形背景的画笔
     **/
    private Paint mBackColorPaint;

    private Paint mPaint;/**刻度画笔*/
    /**
     * 圆盘角度
     **/
    private float mStartAngle = 30;
    /**
     * 大圆
     */
    private int bigCircleRadus = 0;
    /**
     * 小圆
     */
    private int smallCircleRadus = 0;
    private Paint smallPaint;

    /**
     * 控件的中心位置,处于中心位置。x和y是相等的
     */
    private int mCenter;

    /**
     * 圆形背景的宽度，这边是在1080p下的780
     **/
    private float mBackColorWidth = 780;

    /**
     * 内圈画盘小圆的宽度，这边是在1080p下的730，绘画区域
     **/
    private float mRangeWidth = 730;

    /**
     * 里面的小图大小，这边是1080p下的115
     **/
    private float mLitterBitWidth = 115;
    private int smallWidth;

    /**
     * 文字的大小为26px
     */
    private float mTextSize = 26;


    private float mCheckBitmapWidth = 300;//1080p下的270

    private boolean isCanTouch;


    public SmartWheelBar(Context context) {
        this(context, null);
    }


    public SmartWheelBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartWheelBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(attrs, defStyle);
    }


    private void initAttrs(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SmartWheelBar, defStyle, 0);
        mStartAngle = a.getFloat(R.styleable.SmartWheelBar_mStartAngle, 0);
        isCanTouch=a.getBoolean(R.styleable.SmartWheelBar_isCanTouch,true);
        deviceState=a.getInt(R.styleable.SmartWheelBar_deviceState,0);
        a.recycle();
    }

    /**
     * 设置控件为正方形
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);
        // 中心点
        mCenter = width / 2;
        mDrawWidth = width;
        bigCircleRadus = min / 2;
        smallCircleRadus = bigCircleRadus / 2 + 50;
        //屏幕的宽度
        setMeasuredDimension(min, min);
        init();
    }

    /**
     * 当前切图的比例都是在1080p下进行的，所以这边的比例就是1080的
     * <p>
     * mDrawWidth 表示为控件的宽度
     **/
    private float getDrawWidth(float width) {
        return width * mDrawWidth / 1080f;
    }


    public void setBitInfos(List<SmartWheelInfo> bitInfos) {
        this.bitInfos = bitInfos;
        mItemCount = this.bitInfos.size();
        onDrawInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mCanvas = canvas;
        drawCanvas();
    }

    private void init() {
        if (!hasMeasured) {
            //得到各个比例下的图片大小
            mBackColorWidth = getDrawWidth(mBackColorWidth);
            mRangeWidth = getDrawWidth(mRangeWidth);
            mCheckBitmapWidth = getDrawWidth(mCheckBitmapWidth);
            mLitterBitWidth = getDrawWidth(mLitterBitWidth);
            mTextSize = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP, mTextSize / 3, getResources().getDisplayMetrics());
            hasMeasured = true;
        }
        // 初始化绘制圆弧的画笔
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);
        // 初始化绘制文字的画笔
        mTextPaint = new TextPaint();
        mTextPaint.setColor(0xff222222);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        //圆形背景的画笔
        mBackColorPaint = new Paint();
        mBackColorPaint.setColor(Color.BLUE);
        mBackColorPaint.setAntiAlias(true);
        mBackColorPaint.setDither(true);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);//去除边缘锯齿，优化绘制效果
        mPaint.setColor(getResources().getColor(R.color.color_gray2));

        smallPaint = new Paint();
        smallPaint.setColor(Color.WHITE);
        smallPaint.setAntiAlias(true);
        smallPaint.setDither(true);
        mRange = new RectF(mCenter - mRangeWidth / 2, mCenter - mRangeWidth / 2, mCenter + mRangeWidth / 2, mCenter + mRangeWidth / 2);
    }

    /**
     * 对每个选项进行绘制
     **/
    private Bitmap getDrawItemBitmap(float tmpAngle, float sweepAngle, int position) {
        //是否需要重新绘制
        boolean needToNew = false;
        //根据状态判断是否需要重新绘制
        if (checkPosition == position && bitInfos.get(position).info.bitmapType == TYPE_UNCHECKED) {//这次选中，上次没选中的要更新
            needToNew = true;
            bitInfos.get(position).info.bitmapType = TYPE_CHECKED;
        } else if (checkPosition != position && bitInfos.get(position).info.bitmapType == TYPE_CHECKED) {//这次没选中，上次选中的要更新
            needToNew = true;
            bitInfos.get(position).info.bitmapType = TYPE_UNCHECKED;
        }
        if (bitInfos.get(position).info.itemBitmap == null || needToNew) {
            //选择背景颜色
            if (checkPosition == position) {
                mArcPaint.setColor(mColors[1]);
            } else {
                mArcPaint.setColor(mColors[0]);
            }
            //绘制每一个小块
            bitInfos.get(position).info.itemBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas itemCanvas = new Canvas(bitInfos.get(position).info.itemBitmap);
            //根据角度进行同步旋转
            itemCanvas.rotate(tmpAngle, mCenter, mCenter);
            //绘制背景颜色,从最上边开始画
            itemCanvas.drawArc(mRange, -sweepAngle / 2 - 90, sweepAngle, true, mArcPaint);
            //绘制小图片和文本，因为一起画好画点
            drawIconAndText(position, itemCanvas);
            //绘制分割线，这里保证没一个小块都有一条分割线，分割线的位置是在最右侧
//            drawCanvasLine(itemCanvas);
        } else {
            Canvas itemCanvas = new Canvas(bitInfos.get(position).info.itemBitmap);
            //根据角度进行同步旋转
            itemCanvas.rotate(tmpAngle, mCenter, mCenter);
        }
        return bitInfos.get(position).info.itemBitmap;
    }

    public void setCanTouch(boolean canTouch) {
        this.isCanTouch = canTouch;
    }

    public boolean isCanTouch() {
        return isCanTouch;
    }

    private void drawCanvas() {
        if (bitInfos == null || bitInfos.size() == 0) {
            return;
        }
        float left = getPaddingLeft() + 25 / 2;
        float top = getPaddingTop() + 25 / 2;
        float right = mCanvas.getWidth() - getPaddingRight() - 25 / 2;
        float bottom = mCanvas.getHeight() - getPaddingBottom() - 25 / 2;
        float centerX = (left + right) / 2;
        float centerY = (top + bottom) / 2;
        int mUnreachedWidth = 25;

//        for (int i = 0; i < 80; i++) {//总共45个点  所以绘制45次  //绘制一圈的小黑点
//            if (i == 0) {
//                mPaint.setColor(getResources().getColor(R.color.green));
//                mCanvas.drawRect(mCenter - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()),
//                        getPaddingTop() + mUnreachedWidth + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()),
//                        centerX + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()),
//                        getPaddingTop() + mUnreachedWidth + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -20, getResources().getDisplayMetrics()), mPaint);
//            } else {
//                mPaint.setColor(getResources().getColor(R.color.color_gray2));
//                mCanvas.drawRect(centerX - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()),
//                        getPaddingTop() + mUnreachedWidth + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()),
//                        centerX + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()),
//                        getPaddingTop() + mUnreachedWidth + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -20, getResources().getDisplayMetrics()), mPaint);
//            }
//
//            mCanvas.rotate(4.5f, centerX, centerY);//360度  绘制72次   每次旋转5度
//        }
//        mCanvas.save();
        //绘制背景图
        mBackColorPaint.setColor(Color.parseColor("#00000000"));
        mCanvas.drawCircle(mCenter, mCenter, bigCircleRadus, mBackColorPaint);

//        mBackColorPaint.setAlpha(1);
        smallPaint.setColor(Color.parseColor("#00000000"));
//        smallPaint.setAlpha(1);
        mCanvas.drawCircle(mCenter, mCenter, smallCircleRadus, smallPaint);
        //画前景图片
        mCanvas.drawBitmap(getFontBitmap(), 0, 0, null);
        //绘制中心图片
//        mCanvas.drawBitmap(mCheckBitmap, null, mCheckBitmapRect, null);
    }

    //绘制前景图片,这里包含的是图片信息和文字信息,还有背景圆弧背景展示
    private Bitmap getFontBitmap() {
        fontBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(fontBitmap);
        //根据角度进行同步旋转
        canvas.rotate(mStartAngle, mCenter, mCenter);
        float tmpAngle = 0;
        float sweepAngle = (float) (360 / mItemCount);
        for (int i = 0; i < mItemCount; i++) {
            //这边可以得到新的bitmap
            canvas.drawBitmap(getDrawItemBitmap(tmpAngle, sweepAngle, i), 0, 0, null);
            tmpAngle += sweepAngle;
        }
        return fontBitmap;
    }


    /**
     * 根据当前旋转的mStartAngle计算当前滚动到的区域
     *
     * @param startAngle
     */
    public int calInExactArea(float startAngle) {
        float size = 360f / mItemCount;
        // 确保rotate是正的，且在0-360度之间
        float rotate = (startAngle % 360.0f + 360.0f) % 360.0f;

        for (int i = 0; i < mItemCount; i++) {
            // 每个的中奖范围
            if (i == 0) {
                if ((rotate > 360 - size / 2) || (rotate < size / 2)) {
                    return i;
                }
            } else {
                float from = size * (i - 1) + size / 2;
                float to = from + size;
                if ((rotate > from) && (rotate < to)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 绘制小图片和文字
     *
     * @param i
     */
    private void drawIconAndText(int i, Canvas canvas) {
        //根据的标注，比例为115/730
        float rt = mLitterBitWidth / mRangeWidth;
        //计算绘画区域的直径
        int mRadius = (int) (mRange.right - mRange.left);
        float left = getPaddingLeft() + 25 / 2;
        float top = getPaddingTop() + 25 / 2;
        float right = canvas.getWidth() - getPaddingRight() - 25 / 2;
        float bottom = canvas.getHeight() - getPaddingBottom() - 25 / 2;
        float centerX = (left + right) / 2;
        float centerY = (top + bottom) / 2;
        //获取中心点坐标
        int x = mCenter;

        //确定小图片的区域
        Rect rect = new Rect();

        //绘制文本
        if (!TextUtils.isEmpty(bitInfos.get(i).text)) {

            mTextPaint.getTextBounds(bitInfos.get(i).text, 0, bitInfos.get(i).text.length(), rect);
            canvas.drawText(bitInfos.get(i).text, mCenter - rect.width() / 2,
                    getPaddingTop() + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics()) + rect.height(), mTextPaint);
            canvas.rotate(45, centerX, centerY);
            //画完之后移动回来
//            canvas.translate(-mCenter, -(rect.bottom + dip2px(2)));
        }

        for (int j = 0; j < 80; j++) {//总共45个点  所以绘制45次  //绘制一圈的小黑点
            mPaint.setColor(getResources().getColor(R.color.color_gray2));
            canvas.drawRect(centerX - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()),
                    getPaddingTop() + 25 + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics()),
                    centerX + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()),
                    getPaddingTop() + 25 + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -4, getResources().getDisplayMetrics()), mPaint);
            canvas.rotate(4.5f, centerX, centerY);//360度  绘制72次   每次旋转5度
        }
        canvas.save();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //判断按点是否在圆内
//        if (!mRange.contains(event.getX(), event.getY())) {
//            return true;
//        }
        float x = event.getX();
        float y = event.getY();

//        if (event.getAction()==MotionEvent.ACTION_MOVE && isInCiecle(x,y)){
//            float moveX = event.getX();
//            float moveY = event.getY();
//            //得到旋转的角度
//            float arc = getRoundArc(touchX, touchY, moveX, moveY);
//            //重新赋值
//            touchX = moveX;
//            touchY = moveY;
//            //起始角度变化下，然后进行重新绘制
//            mStartAngle += arc;
//            onDrawInvalidate();
//            return true;
//        }else {
//            return super.onTouchEvent(event);
//        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isCanTouch==false && deviceState==0){
                    Toast toast= Toast.makeText(getContext(),"设备已关机",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    break;
                }
                if (isCanTouch){
                    touchX = event.getX();
                    touchY = event.getY();
//                    if (isInCiecle(touchX, touchY)) {
//                        //按下时的时间
//                        nowClick = System.currentTimeMillis();
//                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (isCanTouch && deviceState==1){
                    float moveX = event.getX();
                    float moveY = event.getY();
                    //得到旋转的角度
                    float arc = getRoundArc(touchX, touchY, moveX, moveY);
                    //重新赋值
                    touchX = moveX;
                    touchY = moveY;
                    //起始角度变化下，然后进行重新绘制
                    mStartAngle += arc;
                    Log.i("mStartAngle", "-->" + mStartAngle);
                    if (mStartAngle >= 360) {
                        mStartAngle = 0;
                    }
                    if (mStartAngle <= -360) {
                        mStartAngle = 0;
                    }
                    if (isInCiecle(touchX, touchY)) {
                        onDrawInvalidate();
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                    touchX = event.getX();
                    touchY = event.getY();

                    //落点的角度加上偏移量基于初始的点的位置

                    if (mStartAngle % 4.5f != 0) {
                        int t = (int) (mStartAngle / 4.5f);
                        mStartAngle = t * 4.5f;
                        Log.i("mStartAngle3", "-->" + mStartAngle);
                    }
                    checkPosition = calInExactArea(getRoundArc(event.getX(), event.getY()) - mStartAngle);

                    onDrawInvalidate();
                    if (mOnWheelCheckListener != null) {
//                        mOnWheelCheckListener.onCheck(checkPosition);
                        if (isCanTouch){
                            mOnWheelCheckListener.onChanged(this, mStartAngle);
                        }
                    }
                break;
        }
        return true;
    }

    /**
     * 判断落点是否在圆环上
     */
    /**
     * 判断落点是否在圆环上
     */
    public boolean isInCiecle(float x, float y) {
        Log.i("x", "-->" + x);
        Log.i("y", "-->" + y);
        float distance = (float) Math.sqrt((x - bigCircleRadus) * (x - bigCircleRadus) + (y - bigCircleRadus) * (y - bigCircleRadus));
        Log.i("distance", "-->" + distance);
        int smallCircleRadus = bigCircleRadus / 2 + 50;
        Log.i("smallCircleRadus", "-->" + smallCircleRadus);
        if (distance >= smallCircleRadus && distance <= bigCircleRadus)
            return true;
        else
            return false;
    }

    //根据落点计算角度
    private float getRoundArc(float upX, float upY) {
        float arc = 0;
        //首先计算三边的长度
        float a = (float) Math.sqrt(Math.pow(mCenter - mCenter, 2) + Math.pow(0 - mCenter, 2));
        float b = (float) Math.sqrt(Math.pow(upX - mCenter, 2) + Math.pow(upY - mCenter, 2));
        float c = (float) Math.sqrt(Math.pow(upX - mCenter, 2) + Math.pow(upY - 0, 2));
        //判断是否为三角形
        if (a + b > c) {//两边之和大于第三边为三角形
            /**
             * 接下来计算角度
             *
             * acos((a2+b2-c2)/2ab)
             *
             * **/
            arc = (float) (Math.acos((Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2)) / (2 * a * b)) * 180 / Math.PI);

            //判断是大于左边还是右边，也就是180以内还是以外
            if (upX < mCenter) {//此时是180以外的
                arc = 360 - arc;
            }
        } else {//上下边界的问题
            if (upX == mCenter) {
                if (upY < mCenter) {
                    arc = 0;
                } else {
                    arc = 180;
                }
            }
        }
        Log.i("arc", "-->" + arc);
        return arc;
    }

    //根据三点的坐标计算旋转的角度
    private float getRoundArc(float startX, float startY, float endX, float endY) {
        float arc = 0;
        //首先计算三边的长度
        float a = (float) Math.sqrt(Math.pow(startX - mCenter, 2) + Math.pow(startY - mCenter, 2));
        float b = (float) Math.sqrt(Math.pow(endX - mCenter, 2) + Math.pow(endY - mCenter, 2));
        float c = (float) Math.sqrt(Math.pow(startX - endX, 2) + Math.pow(startY - endY, 2));
        //判断是否为三角形
        if (a + b > c) {//两边之和大于第三边为三角形
            /**
             * 接下来计算角度
             *
             * acos((a2+b2-c2)/2ab)
             *
             * **/
            arc = (float) (Math.acos((Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2)) / (2 * a * b)) * 180 / Math.PI);

            if (startX <= mCenter && endX >= mCenter && startY < mCenter && endY < mCenter) {//上边顺时针越界，不管他
            } else if (startX >= mCenter && endX <= mCenter && startY < mCenter && endY < mCenter) {//上边逆时针越界
                arc = -arc;
            } else if (startX <= mCenter && endX >= mCenter && startY > mCenter && endY > mCenter) {//下边逆时针越界
                arc = -arc;
            } else if (startX <= mCenter && endX >= mCenter && startY < mCenter && endY < mCenter) {//下边顺时针越界，不管他
            } else if (endX >= mCenter && startX >= mCenter) {//这个时候表示在右半区
                if (startY > endY) {
                    arc = -arc;
                }
            } else if (endX < mCenter && startX < mCenter) {//此时在左半区
                if (startY < endY) {
                    arc = -arc;
                }
            }
        }
        if (Math.abs(arc) >= 0 && Math.abs(arc) <= 180) {//主要解决nan的问题
            return arc;
        } else {
            return 0;
        }
    }

    public void setmStartAngle(float mStartAngle) {
        this.mStartAngle = mStartAngle;
    }

    public float getmStartAngle() {
        return mStartAngle;
    }

    /**
     * dp转像素
     *
     * @param dpValue
     * @return
     */
    public final int dip2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getContext().getResources().getDisplayMetrics());
    }

    public void setDeviceState(int deviceState) {
        this.deviceState = deviceState;
    }

    public int getDeviceState() {
        return deviceState;
    }

    /**
     * 刷新画布
     **/
    private void onDrawInvalidate() {
        invalidate();
    }

    public int getCheckPosition() {
        return checkPosition;
    }

    private OnWheelCheckListener mOnWheelCheckListener;

    public void setOnWheelCheckListener(OnWheelCheckListener mOnWheelCheckListener) {
        this.mOnWheelCheckListener = mOnWheelCheckListener;
    }
    public interface OnWheelCheckListener {
        //        void onCheck(int position);
        void onChanged(SmartWheelBar wheelBar, float curAngle);
    }
}
