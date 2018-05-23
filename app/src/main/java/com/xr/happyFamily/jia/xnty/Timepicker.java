package com.xr.happyFamily.jia.xnty;

import android.content.Context;
import android.content.res.Resources;
import java.lang.reflect.Field;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.xr.happyFamily.R;

public class Timepicker extends NumberPicker {
    public Timepicker(Context context, AttributeSet attrs,
                              int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Timepicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Timepicker(Context context) {
        super(context);

    }

    @Override
    public void addView(View child) {
        this.addView(child, null);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        this.addView(child, -1, params);
    }

    @Override
    public void addView(View child, int index,
                        android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        setNumberPicker(child);
    }

    /**
     * 设置CustomNumberPicker的属性 颜色 大小
     * @param view
     */
    public void setNumberPicker(View view) {
        if (view instanceof EditText) {
            ((EditText) view).setTextColor(this.getResources().getColor(R.color.green2));
            ((EditText) view).setTextSize(18);
        }
    }

    /**
     * 设置分割线的颜色值
     * @param numberPicker
     */
    @SuppressWarnings("unused")
    public void setNumberPickerDividerColor(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                  pf.set(picker, new ColorDrawable(this.getResources().getColor(R.color.white)));
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
