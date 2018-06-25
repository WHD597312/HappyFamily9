package com.xr.happyFamily.le.adapter;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.le.view.RadioTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//标签类相关适配器
public class RadioEvaluateAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<String> list;
    private int layout;
    private String[] color={"#ffa6a6","#ff99cb","#f79bff","#ca9bff","#9ba4ff","#9bedff","#99f2bf","#a0f789","#f6dd90","#ffc89b"};

    public RadioEvaluateAdapter(Context context, int layout) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.list =  new ArrayList<String>();
        this.layout=layout;

    }

    public List<String> getList(){
        return list;
    }

    public void setItems(List<String> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(
                    layout, null);
            holder.evaluate_tv = (RadioTextView)convertView.findViewById(R.id.tv_search);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }
        Random random = new Random();

        int i = random.nextInt(color.length);
        holder.evaluate_tv.setBackground(color[i]);

        final String ee = getItem(position).toString();
        holder.evaluate_tv.setmTitleText(ee);

        return convertView;
    }

    private final class ViewHolder {
        private RadioTextView evaluate_tv;
    }

          private int clickTemp = -1;
      public void setSelection(int position){
          clickTemp = position;
          notifyDataSetChanged();
      }
}