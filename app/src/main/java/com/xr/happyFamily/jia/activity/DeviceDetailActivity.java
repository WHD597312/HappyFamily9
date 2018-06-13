package com.xr.happyFamily.jia.activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.pojo.SmartWheelInfo;
import com.xr.happyFamily.jia.view_custom.SemicircleBar;
import com.xr.happyFamily.jia.view_custom.SmartWheelBar;
import com.xr.happyFamily.jia.xnty.Timepicker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DeviceDetailActivity extends AppCompatActivity {

    private List<SmartWheelInfo> infos = new ArrayList<>();
    /**
     * 旋转的数字
     */
    private String[] mStrs = new String[]{"5", "10", "15", "20", "25", "30", "35","40"};
    @BindView(R.id.tv_set_temp) TextView tv_set_temp;/**设定温度*/
    @BindView(R.id.layout_body) RelativeLayout layout_body;/**屏幕中间布局*/
    @BindView(R.id.image_timer) ImageView image_timer;/**定时任务*/
    @BindView(R.id.semicBar)
    SmartWheelBar wheelBar;
    @BindView(R.id.relative4) RelativeLayout relative4;/**底部布局*/
    private List<String> list=new ArrayList<>();
    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_device_detail);
        unbinder=ButterKnife.bind(this);
        WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        Log.w("width","width"+width);

//
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(width,width);
        params.leftMargin=100;
        params.rightMargin=100;
        wheelBar.setLayoutParams(params);
        getBitWheelInfos();
        wheelBar.setBitInfos(infos);
    }

    @Override
    protected void onStart() {
        super.onStart();



//        params.leftMargin=200;
//        params.rightMargin=200;
//        wheelBar.setLayoutParams(params);

//        wheelBar.setOnSeekBarChangeListener(new SemicircleBar.OnSeekBarChangeListener() {
//            @Override
//            public void onChanged(SemicircleBar seekbar, double curValue) {
//
//            }
//        });
        wheelBar.setOnWheelCheckListener(new SmartWheelBar.OnWheelCheckListener() {
            @Override
            public void onChanged(SmartWheelBar wheelBar, float curAngle) {
                Log.i("SmartWheelBar","-->"+curAngle);
                if (curAngle>0){/**顺时钟转*/
                    int temp2=0;
//                    float tempSet=(curAngle2/4.5f * 0.5f);
                    if (curAngle % 4.5f != 0){
                        int t = (int) (curAngle / 4.5f);
                        curAngle = t * 4.5f;
                    }
                    float temp=45-curAngle/4.5f * 0.5f;
                    Log.i("cur","-->"+temp);
                    temp2=(int)temp;
                    if (temp2>=42){
                        temp2=42;
                    }
                    tv_set_temp.setText(temp2+"");
                }else if (curAngle<=0){/**逆时针转*/
                    if (curAngle % 4.5f != 0){
                        int t = (int) (curAngle / 4.5f);
                        curAngle = t * 4.5f;
                    }
                    float tempSet= (-curAngle/4.5f) * 0.5f+5;
                    int temp=(int) tempSet;
                    Log.i("cur2","-->"+temp);
                    if (temp>=42){
                        temp=42;
                    }
                    tv_set_temp.setText(temp+"");
                }
            }
        });
    }
    @OnClick({R.id.image_timer})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.image_timer:
                popupWindow();
                break;
        }
    }
    private PopupWindow popupWindow;
    public void popupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        View view = View.inflate(this, R.layout.popview_timetask, null);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        ImageView image_cancle= (ImageView) view.findViewById(R.id.image_cancle);
        TextView tv_timer= (TextView) view.findViewById(R.id.tv_timer);
        ImageView image_ensure= (ImageView) view.findViewById(R.id.image_ensure);
        TimePicker tv_timer_hour= (TimePicker) view.findViewById(R.id.tv_timer_picker);
        tv_timer_hour.setIs24HourView(true);


        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //添加弹出、弹入的动画
        popupWindow.setAnimationStyle(R.style.Popupwindow);

        ColorDrawable dw = new ColorDrawable(0x30000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAtLocation(relative4, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //添加按键事件监听

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.image_cancle:
                        popupWindow.dismiss();
                        break;
                    case R.id.image_ensure:
                        popupWindow.dismiss();
                        break;

                }

            }
        };

        image_cancle.setOnClickListener(listener);
        image_ensure.setOnClickListener(listener);
    }
    /**
     * 设置时间选择器的分割线颜色
     *
     * @param datePicker
     */
    private void setDatePickerDividerColor(TimePicker datePicker) {
        // Divider changing:

        // 获取 mSpinners
        LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);

        // 获取 NumberPicker
        LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);

            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field pf : pickerFields) {
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    try {
                        pf.set(picker, new ColorDrawable(Color.parseColor("#cccccc")));//设置分割线颜色
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
    public void getBitWheelInfos() {
        for (int i = 0; i < mStrs.length; i++) {
            infos.add(new SmartWheelInfo(mStrs[i], null));
        }
    }
}
