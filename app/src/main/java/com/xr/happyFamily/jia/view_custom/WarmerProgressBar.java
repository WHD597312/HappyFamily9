package com.xr.happyFamily.jia.view_custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.xr.happyFamily.R;


public class WarmerProgressBar extends View {
    private Paint paint;//定义一个画笔
    private float ring_width;//圆环宽度
    private int ring_color;//圆环颜色
    private boolean isCanTouch;//能否触摸
    private float current_angle=0;//当前角度
    float centerX;
    float centerY;
    private int rangRadus=0;
    private Context context;
    public WarmerProgressBar(Context context) {
        this(context, null);
        this.context=context;
    }

    public WarmerProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WarmerProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
//        给画笔设置颜色
        paint.setColor(Color.RED);
//        设置画笔属性
//        paint.setStyle(Paint.Style.FILL);//画笔属性是实心圆
        paint.setStyle(Paint.Style.STROKE);//画笔属性是空心圆
        paint.setStrokeWidth(ring_width);//设置画笔粗细
        paint.setAntiAlias(true);

    }

    private void initAttrs(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomCircleProgressBar, defStyle, 0);
        ring_width = a.getDimension(R.styleable.CustomCircleProgressBar_ring_width, getDimen(R.dimen.dp_jy_5));
        ring_color = a.getColor(R.styleable.CustomCircleProgressBar_ring_color, getResources().getColor(R.color.gray3));
        isCanTouch = a.getBoolean(R.styleable.CustomCircleProgressBar_touch_enable, false);
        current_angle = a.getFloat(R.styleable.CustomCircleProgressBar_current_angle, 0);
        a.recycle();
    }

    private float getDimen(int dimenId) {
        return getResources().getDimension(dimenId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);
        rangRadus=width/2;
        setMeasuredDimension(min, min);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();


        float left =getPaddingLeft()+ring_width;
        float top = getPaddingTop()+ring_width;
        float right = width - getPaddingRight()-ring_width;
        float bottom = height - getPaddingBottom()-ring_width;
        centerX = (left + right) / 2;
        centerY = (top + bottom) / 2;
//        float radius = canvas.getWidth() / 2 - ring_width;
        paint.setColor(getResources().getColor(R.color.gray3));
        canvas.rotate(150,centerX,centerY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(new RectF(left,top,right,bottom),0,240,false,paint);
        canvas.save();


        Log.i("CurrentAngle","-->"+current_angle);
        if (current_angle>=0){
            paint.setColor(getResources().getColor(R.color.color_orange));
            canvas.drawArc(new RectF(left,top,right,bottom),0,current_angle,false,paint);
            canvas.save();
            int x0=width/2;
            int y0=height/2;
            float R2 = (width)/2-ring_width;

            float Point_x= (float) (x0+R2*Math.cos(current_angle*3.1415926/180));
            float Point_y= (float) (y0+R2*Math.sin(current_angle * 3.1415926 / 180));
            paint.setColor(getResources().getColor(R.color.white));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(Point_x,Point_y,23,paint);
        }


//        Log.i("current_angle","-->"+current_angle);
//        if (current_angle>=0){
//            paint.setColor(getResources().getColor(R.color.color_orange));
//            for (int i = 0; i <current_angle/7.5 ; i++) {
//
//                canvas.drawRect(centerX - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()),
//                        getPaddingTop() + ring_width + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()),
//                        centerX + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()),
//                        getPaddingTop() + ring_width + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics()), paint);
//                canvas.rotate(7.5f, centerX, centerY);
//            }
//            canvas.save();
//        }

    }

    private int value=10;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float Action_x = event.getX();
        float Action_y = event.getY();
        /*根据坐标转换成对应的角度*/
        float get_x0 = Action_x - centerX;
        float get_y0 = Action_y - centerY;
        /*01：左下角区域*/

        Log.i("get_x0", "(" + get_x0 + "," + get_y0 + ")");

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mode==1){
                    Toast toast=Toast.makeText(getContext(),"设备已关机",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    break;
                }
                if (!isInCiecle(Action_x,Action_y) && isCanTouch){
                    break;
                }
               break;
            case MotionEvent.ACTION_MOVE:
                //左下角
                if (isInCiecle(Action_x,Action_y) && mode==0){
                    if (get_x0 <= 0 & get_y0 >= 0) {
                        float tan_x = get_x0 * (-1);
                        float tan_y = get_y0;
                        double atan = Math.atan(tan_x / tan_y);
                        current_angle = (int) Math.toDegrees(atan);
                        current_angle = (int) Math.toDegrees(atan)-60;
                        Log.i("current_angle", "左下角-->" + current_angle);
                        if (current_angle < 0) {
                            current_angle=0;
                            break;
                        }
                    }

                    //左上角

                    if (get_x0 <= 0 & get_y0 <= 0) {
                        float tan_x = get_x0 * (-1);
                        float tan_y = get_y0 * (-1);
                        double atan = Math.atan(tan_y / tan_x);
                        current_angle = (int) Math.toDegrees(atan);
//                        Log.i("current_angle", "左上角-->" + current_angle);
                        current_angle = (int) Math.toDegrees(atan)+30;
                        Log.i("current_angle", "左上角-->" + current_angle);
                    }

                    //右上角

                    if (get_x0 >= 0 & get_y0 <= 0) {
                        float tan_x = get_x0;
                        float tan_y = get_y0 * (-1);
                        double atan = Math.atan(tan_x / tan_y);
                        current_angle = (int) Math.toDegrees(atan) + 135;
                        Log.i("current_angle", "右上角-->" + current_angle);
                    }

                    //右下角

                    if (get_x0 >= 0 & get_y0 >= 0) {
                        float tan_x = get_x0;
                        float tan_y = get_y0;
                        double atan = Math.atan(tan_y / tan_x);
                        int current_angle2 = (int) Math.toDegrees(atan);
                        Log.i("current_angle", "右下角-->" + current_angle);
                        if (current_angle2 >= 0 && current_angle2 <= 32) {
                            current_angle = current_angle2 + 210;
                            if (current_angle>240){
                                current_angle=240;
                            }
                        } else if (current_angle2 > 32) {
                            return false;
                        }
                        Log.i("current_angle", "右下角-->" + current_angle);
                    }
                    Log.i("current_tttttttt", "-->" + current_angle);
                    float temp = current_angle /6.86f +10;
                    value=Math.round(temp);
                    Log.i("ValueSet", "-->" + temp);

                    if (value >= 45) {
                        value = 45;
                    }
                    if (value<=10){
                        value=10;
                    }
                    Log.i("AngleEEEEEE","-->"+value);
                    invalidate();
                }


//                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (onMoveListener!=null && mode==0){
                    onMoveListener.setOnMoveListener(value);
                }
                break;
        }
        return true;
    }

    /**判断落点是否在圆环上*/
    public boolean isInCiecle(float x,float y){
        Log.i("x","-->"+x);
        Log.i("y","-->"+y);
        float distance = (float) Math.sqrt((x-rangRadus)*(x-rangRadus)+(y-rangRadus)*(y-rangRadus));
        Log.i("distance","-->"+distance);
        int smallCircleRadus=rangRadus/2+50;
        Log.i("smallCircleRadus","-->"+smallCircleRadus);
        if (distance>=smallCircleRadus && distance<=rangRadus)
            return true;
        else
            return false;
    }

    public int getValue() {
        return value;
    }


    public boolean isCanTouch() {
        return isCanTouch;
    }

    public float getCurrent_angle() {
        return current_angle;
    }

    public void setCurrentAngle(int setTemp) {
        value=setTemp;
        current_angle=(setTemp-10)*6.857f;
        Log.i("MotionEvent","--->"+current_angle+",value:"+value);
        invalidate();
    }

    private int progress=10;

    public void setProgress(int progress) {
        this.progress=progress;
        current_angle=(progress-10)*6.857f;
        Log.i("MotionEvent","--->"+current_angle+",value:"+value);
        invalidate();
    }

    public int getProgress() {
        return progress;
    }

    /**
     * 能够触摸滑动盘
     * @param canTouch
     */
    public void setCanTouch(boolean canTouch) {
        isCanTouch = canTouch;
    }
    public OnMoveListener onMoveListener;

    public OnMoveListener getOnMoveListener() {
        return onMoveListener;
    }

    public void setOnMoveListener(OnMoveListener onMoveListener) {
        this.onMoveListener = onMoveListener;
    }

    private int mode;

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public interface OnMoveListener{
        void setOnMoveListener(int value);
    }
}
