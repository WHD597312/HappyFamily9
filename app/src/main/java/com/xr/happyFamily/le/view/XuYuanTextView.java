package com.xr.happyFamily.le.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xr.happyFamily.R;


public class XuYuanTextView extends LinearLayout {

    private ImageView mImgView = null;
    private TextView mTextView = null;
    private Context mContext;

    public XuYuanTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_yuanwang, this, true);
        mContext = context;
        mImgView = (ImageView)findViewById(R.id.img_touxiang);
        mTextView = (TextView)findViewById(R.id.text);
    }

    public XuYuanTextView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_yuanwang, this, true);
        mContext = context;
        mImgView = (ImageView)findViewById(R.id.img_touxiang);
        mTextView = (TextView)findViewById(R.id.text);
    }

    /*设置图片接口*/
    public void setImageResource(int resId){
        mImgView.setImageResource(resId);
    }

    /*设置文字接口*/
    public void setText(String str){
        mTextView.setText(str);
    }
    /*设置文字大小*/
    public void setTextSize(float size){
        mTextView.setTextSize(size);
    }


}