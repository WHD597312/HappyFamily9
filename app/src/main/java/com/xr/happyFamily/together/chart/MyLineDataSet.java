package com.xr.happyFamily.together.chart;

import android.graphics.Color;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class MyLineDataSet extends LineDataSet {
    private List<Integer> mCircleColors = null;
    public MyLineDataSet(List<Entry> yVals, String label) {
        super(yVals, label);
        if (mCircleColors == null) {
            mCircleColors = new ArrayList<Integer>();
        }
        mCircleColors.clear();

        // default colors
        // mColors.add(Color.rgb(192, 255, 140));
        // mColors.add(Color.rgb(255, 247, 140));
        mCircleColors.add(Color.rgb(80, 81, 83));
    }
}
