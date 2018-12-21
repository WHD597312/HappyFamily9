package com.xr.happyFamily.le.Yougui;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.le.pojo.AppUsing;

import java.util.ArrayList;
import java.util.List;

public class UsageStatAdapter extends RecyclerView.Adapter<UsageStatAdapter.UsageStatVH> {

    private List<AppUsing> list = new ArrayList<>();
    private Context context;
    public UsageStatAdapter(Context context, List<AppUsing> list ){

        this.context= context;
        this.list= list;
    }

    @Override
    public UsageStatVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usage_stat_item, parent, false);
        return new UsageStatVH(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(UsageStatVH holder, int position) {
        String url = list.get(position).getIconAdress();
        Glide.with(context).load(url).into(holder.appIcon);
        holder.appName.setText(list.get(position).getAppName());
        holder.lastTimeUsed.setText(list.get(position).getAppUseLastTime());
        holder.usingTime.setText(list.get(position).getUseTime()+"  秒");
        if (position==list.size()-1){
            holder.view.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<AppUsing> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    class UsageStatVH extends RecyclerView.ViewHolder {

        private ImageView appIcon;
        private TextView appName;
        private TextView lastTimeUsed ,usingTime;
        private View view;

        public UsageStatVH(View itemView) {
            super(itemView);

            appIcon = (ImageView) itemView.findViewById(R.id.icon);
            appName = (TextView) itemView.findViewById(R.id.title);
            lastTimeUsed = (TextView) itemView.findViewById(R.id.tv_zjtime);
            usingTime = (TextView) itemView.findViewById(R.id.tv_cstime);
            view = (View) itemView.findViewById(R.id.app_db);
        }


    }
}
