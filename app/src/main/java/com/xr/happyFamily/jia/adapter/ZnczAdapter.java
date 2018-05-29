package com.xr.happyFamily.jia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;

import java.util.List;

import butterknife.BindView;

public class ZnczAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext ;
    private List<String> mList;
    public ZnczAdapter( Context mContext , List<String> mList){
        mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mList = mList;
    }
    public int getCount(){
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }
    public long getItemId (int position){
        return position ;
    }
    public View getView (int position , View contentView , ViewGroup parent){
        ZnczAdapter.ViewHolder holder ;
        if(contentView==null){
            holder = new ZnczAdapter.ViewHolder();
            contentView = mInflater.inflate(R.layout.activity_xnsb_zncz_item,parent,false);
            holder.tv_title=(TextView)contentView.findViewById(R.id.tv_zncz_1);
            holder.imageView= (ImageView)contentView.findViewById(R.id.iv_zncz_1);
            contentView.setTag(holder);
        }else {
            holder = (ZnczAdapter.ViewHolder)contentView.getTag();
        }
        holder.tv_title.setText(mList.get(position));
        // ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,70);//设置宽度和高度
        //contentView.setLayoutParams(params);
        return contentView;

    }
    class ViewHolder {
        private TextView tv_title;
        private ImageView imageView;
    }
}
