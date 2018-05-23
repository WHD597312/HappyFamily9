package com.xr.happyFamily.bao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopCartActivity;
import com.xr.happyFamily.bean.ShopCartBean;

import java.util.List;
import java.util.Map;

//快递列表适配器

public class TimeLineAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,Object>> list;
    private LayoutInflater inflater;

    public TimeLineAdapter(Context context, List<Map<String, Object>> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        TimeLineHolder viewHolder = null;
        if (convertView == null) {
            inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item_logistics, null);
            viewHolder = new TimeLineHolder();

            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.view1 = convertView.findViewById(R.id.view_1);
            viewHolder.view2 = convertView.findViewById(R.id.view_2);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TimeLineHolder) convertView.getTag();
        }

        String titleStr = list.get(position).get("title").toString();
        String time = list.get(position).get("time").toString();
        String state = list.get(position).get("state").toString();

        viewHolder.title.setText(titleStr);
        viewHolder.time.setText(time);
        if(state.equals("0"))
        {
            viewHolder.view1.setVisibility(View.INVISIBLE);
            viewHolder.image.setImageResource(R.mipmap.xuanzhong_wuliu3x);
            viewHolder.view2.setVisibility(View.VISIBLE);
        }else if(state.equals("2")){
            viewHolder.view2.setVisibility(View.INVISIBLE);
        }else {

        }

        return convertView;

    }

    static class TimeLineHolder{
        private TextView title,time;
        private View view1,view2;
        private ImageView image;
    }

}