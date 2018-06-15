package com.xr.happyFamily.jia.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.pojo.SmartSet;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by win7 on 2018/3/10.
 */

public class HourseAdapter extends BaseAdapter {

    private Context context;


    public HourseAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public SmartSet getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        String[] str1={"家庭名称","家庭位置"};
        String[] str={"xxx的家","杭州西湖"};
        switch (position){
            case 0:
                convertView= View.inflate(context, R.layout.activity_home_hourseitem,null);
                viewHolder=new ViewHolder(convertView);
                viewHolder.tv_hourse.setText(str[0]);
                break;
            case 1:
                convertView= View.inflate(context, R.layout.activity_home_hourseitem,null);
                viewHolder=new ViewHolder(convertView);
                viewHolder.tv_hourse.setText(str[1]);
                break;
            case 2:
                convertView=View.inflate(context,R.layout.activity_home_hourseitem1,null);
                convertView.setMinimumHeight(50);
                break;


        }
        return convertView;
    }
    class ViewHolder{

        @BindView(R.id.tv_hourse_h)
        TextView tv_hourse;
        @BindView(R.id.tv_hourse_n)
        TextView tv_hoursen;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }

}
