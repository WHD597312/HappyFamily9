package com.xr.happyFamily.le.adapter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.IBinder;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.database.dao.daoimpl.TimeDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.FuWuActivity;
import com.xr.happyFamily.jia.RenameHourseActivity;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.jia.view_custom.DeleteDeviceDialog;
import com.xr.happyFamily.le.BtClock.bjTimeActivity;
import com.xr.happyFamily.le.pojo.Time;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.main.RoomFragment;
import com.xr.happyFamily.together.util.mqtt.ClockService;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

//快递列表适配器

public class ChooseTimeAdapter extends RecyclerView.Adapter<ChooseTimeAdapter.MyViewHolder> /*implements View.OnClickListener*/ {
    private Context context;
    private List<Time> data;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;
    //    private AdapterView.OnItemClickListener mOnItemClickListener;
    private int type = 0;
    private String shopId;
    private int clicked;
    private TimeDaoImpl timeDao;
    private ClockService clockService;
    Intent service;
    Boolean isBound;
    public ChooseTimeAdapter(Context context, List<Time> list) {
        this.context = context;
        this.data = list;
        timeDao=new TimeDaoImpl(context);
        service = new Intent(context, ClockService.class);
        isBound = context.bindService(service, connection, Context.BIND_AUTO_CREATE);

    }
    //绑定服务
    ClockService mqService;
    boolean bound;
    ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ClockService.LocalBinder binder = (ClockService.LocalBinder) service;
            mqService = binder.getService();
            bound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }
//    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener ){
//        this. mOnItemClickListener=onItemClickListener;
//    }

    public interface OnItemListener {
        void onClick(View v, int pos, String projectc);
    }

    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }


    /**
     * 按钮点击事件需要的方法
     */
    public void buttonSetOnclick(ButtonInterface buttonInterface) {
        this.buttonInterface = buttonInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface ButtonInterface {
        public void onclick(View view, int position);

        public void onLongClick(int position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.fragment_le_comonclock_item, parent,
                false));
        return holder;
    }
    Time ti;
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        holder.tv_shop_type.setText(data.get(position).getGoods().getSimpleDescribe());
        final Time time=data.get(position);
//        List<Time> times = timeDao.findByAllTime();
//        final Time ti =  times.get(position);
        holder.tv_time.setText(data.get(position).getHour()+":"+data.get(position).getMinutes());
        holder.tv_day1.setText(data.get(position).getDay());
        Log.i("day", "onBindViewHolder:--> "+data.get(position).getDay());
        final boolean[] open = {time.getOpen()};

           if (open[0]){
               holder.img_kg.setImageResource(R.mipmap.bt_kg);
           }else {
               holder.img_kg.setImageResource(R.mipmap.bt_kgg);

           }




//        tv_time = (TextView) view.findViewById(R.id.tv_clock_time);
//        tv_day1= (TextView) view.findViewById(R.id.tv_clock_week1);
//        img_kg = (ImageView) view.findViewById(R.id.iv_clock_kg);
//        img_kg.setTag("open");
//        rl_d1 = (RelativeLayout) view.findViewById(R.id.rl_le_commonitem);
//            rl_d1= (RelativeLayout) view.findViewById(R.id.rl_house_it2);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                        Intent intent = new  Intent(context,RenameHourseActivity.class);
//                        intent.putExtra("houseName",houseName);
//                        intent.putExtra("houseAddress",houseAddress);
//                        startActivity(intent);
//
//                }
//            });

        holder.img_kg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (open[0]){
                    holder.img_kg.setImageResource(R.mipmap.bt_kgg);
                    open[0] =false;
                    time.setOpen(false);
                    Time time2=timeDao.findById(time.getId());
                    Log.e("id", "onClick: -->"+time.getId() );
                    time2.setOpen(false);
//
// clockService.update(time2);
                    if (mqService != null) {
                        mqService.update(time);
                    }
//                     timeDao.update(time2);
                     Time t = timeDao.findById(time2.getId());

                    Log.e("id", "onClick: -->"+  t.getOpen());
                }else {
                    open[0] =true;
                    holder.img_kg.setImageResource(R.mipmap.bt_kg);
                    time.setOpen(true);
                    Time time2=timeDao.findById( time.getId());
                    time2.setOpen(true);
//                    clockService.update(time2);
                    if (mqService != null) {
                        mqService.update(time2);

                    }
                    Time t = timeDao.findById(time2.getId());

                    Log.e("id", "onClick: -->"+  t.getOpen());
                }
//                if ("open".equals(img_kg.getTag())) {
//                    img_kg.setImageResource(R.mipmap.bt_kgg);
//                    img_kg.setTag("close");
//                }else if ("close".equals(img_kg.getTag())){
//                    img_kg.setImageResource(R.mipmap.bt_kg);
//                    img_kg.setTag("open");
//                }
            }
        });

        holder.rl_d1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Log.i("dddddddd", "???????????");

                TimeDaoImpl timeDao=new TimeDaoImpl(context);
                List<Time> times = timeDao.findByAllTime();
                Time time= times.get(position);
                int hour = time.getHour();
                int minutes = time.getMinutes();
                String day= time.getDay();
                Intent intent = new Intent(context, bjTimeActivity.class);
                intent.putExtra("hour", hour);
                intent.putExtra("minutes", minutes);
                intent.putExtra("day", day);
                intent.putExtra("position",position);
//                context.startActivity(intent);
                ((Activity) context).startActivityForResult(intent, 600);

            }

        });
        holder.rl_d1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final TimeDaoImpl timeDao=new TimeDaoImpl(context);
                final List<Time> times = timeDao.findByAllTime();
                final Time time= times.get(position);
                final DeleteDeviceDialog dialog = new DeleteDeviceDialog(context);
                dialog.setOnNegativeClickListener(new DeleteDeviceDialog.OnNegativeClickListener() {
                    @Override
                    public void onNegativeClick() {
                        dialog.dismiss();
                    }
                });
                dialog.setOnPositiveClickListener(new DeleteDeviceDialog.OnPositiveClickListener() {
                    @Override
                    public void onPositiveClick() {
                        Log.i("dddddd2", "onPositiveClick: --->"+data.size());

                       timeDao.delete(time);
                        List<Time> day= new ArrayList();
                        day= timeDao.findByAllTime();
                        data = day;
                        notifyDataSetChanged();
                        Log.i("dddddd1", "onPositiveClick: --->"+data.size());
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return false;
            }
        });
    }

    public ClockService getClockService() {
        return clockService;
    }

    public void setClockService(ClockService clockService) {
        this.clockService = clockService;
    }

    //    @OnClick({R.id.iv_clock_kg})
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.iv_clock_kg:
//
//                break;
//
//
//        }
//    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_kg;

        TextView tv_day1,tv_day2, tv_time,tv_day3,tv_day4,tv_day5,tv_day6,tv_day7;
        RelativeLayout rl_d1;
        RelativeLayout rl_d2;

        public MyViewHolder(View view) {
            super(view);


            tv_time = (TextView) view.findViewById(R.id.tv_clock_time);
            tv_day1= (TextView) view.findViewById(R.id.tv_clock_week1);
            img_kg = (ImageView) view.findViewById(R.id.iv_clock_kg);
//            img_kg.setTag("open");
            rl_d1 = (RelativeLayout) view.findViewById(R.id.rl_le_commonitem);
//            rl_d1= (RelativeLayout) view.findViewById(R.id.rl_house_it2);
////            itemView.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////
////                        Intent intent = new  Intent(context,RenameHourseActivity.class);
////                        intent.putExtra("houseName",houseName);
////                        intent.putExtra("houseAddress",houseAddress);
////                        startActivity(intent);
////
////                }
////            });
//            img_kg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if ("open".equals(img_kg.getTag())) {
//                        img_kg.setImageResource(R.mipmap.bt_kgg);
//                        img_kg.setTag("close");
//                    }else if ("close".equals(img_kg.getTag())){
//                        img_kg.setImageResource(R.mipmap.bt_kg);
//                        img_kg.setTag("open");
//                    }
//                }
//            });
        }
    }
}