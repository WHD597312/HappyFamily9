package com.xr.happyFamily.jia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.pojo.Room;
import java.util.List;

public class RoomAdapter extends ArrayAdapter {
    private final int resourceId;

    public RoomAdapter(Context context, int textViewResourceId, List<Room> objects) {
       super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Room room = (Room) getItem(position); // 获取当前项的Room实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        ImageView roomImage = (ImageView) view.findViewById(R.id.iv_change);//获取该布局内的图片视图
        TextView roomName = (TextView) view.findViewById(R.id.tv_change_1);//获取该布局内的文本视图
        roomImage.setImageResource(room.getImgId());//为图片视图设置图片资源
        roomName.setText(room.getName());//为文本视图设置文本内容
        return view;

    }

}
