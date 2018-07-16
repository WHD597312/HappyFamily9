package com.xr.happyFamily.le.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.xr.database.dao.daoimpl.ClockDaoImpl;
import com.xr.database.dao.daoimpl.UserInfosDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.PingLunActivity;
import com.xr.happyFamily.le.ClockActivity;
import com.xr.happyFamily.le.clock.QunzuAddActivity;
import com.xr.happyFamily.le.clock.QunzuEditActivity;
import com.xr.happyFamily.le.fragment.QunZuFragment;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.UserInfo;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.RoundTransform;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//快递列表适配器

public class ClockQunzuAdapter extends RecyclerView.Adapter<ClockQunzuAdapter.MyViewHolder> implements View.OnClickListener {
    private ClockActivity context;
    private ButtonInterface buttonInterface;
    List<ClockBean> clockBeanList;
    List<UserInfo> userInfoList;
    private int defItem = -1;

    private int type = 0;
    private ClockDaoImpl clockBeanDao;
    private UserInfosDaoImpl userInfosDao;

    public ClockQunzuAdapter(ClockActivity context, List<ClockBean> clockBeanList,List<UserInfo> userInfoList) {
        this.context = context;
        this.clockBeanList = clockBeanList;
        this.userInfoList = userInfoList;
        getData();
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
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_clock_qunzu, parent,
                false));
        return holder;
    }


    public void getData() {
        clockBeanList=new ArrayList<>();
        clockBeanDao=new ClockDaoImpl(context.getApplicationContext());
        userInfosDao = new UserInfosDaoImpl(context.getApplicationContext());
        List<ClockBean> allClockList=clockBeanDao.findAll();
        for(int i=0;i<allClockList.size();i++){
            if (allClockList.get(i).getClockType()==2)
                clockBeanList.add(allClockList.get(i));
        }

        Log.e("qqqqqqSSSSQQQ",clockBeanList.size()+"??????"+allClockList.size());

        userInfoList = userInfosDao.findAll();
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        String hourStr="";
        int hour=clockBeanList.get(position).getClockHour();
        if(hour<10){
            hourStr="0"+hour;
        }else hourStr=hour+"";
        String minStr="";
        int min=clockBeanList.get(position).getClockMinute();
        if(min<10){
            minStr="0"+min;
        }else minStr=min+"";
        holder.tv_time.setText(hourStr+":"+minStr);
        holder.tv_context.setText(clockBeanList.get(position).getFlag());

        final List<UserInfo> myUserInfoList=userInfosDao.findUserInfoByClockId(clockBeanList.get(position).getClockId());

//            for(int j=0;j<userInfoList.size();j++){
//                if (userInfoList.get(j).getClockId()==clockBeanList.get(position).getClockId())
//                    myUserInfoList.add(userInfoList.get(j));
//            }

            int size=0;
        if(myUserInfoList.size()<5) {
           size=myUserInfoList.size();
        }else
            size=5;
        for(int a=0;a<5;a++)
            holder.imgs[a].setVisibility(View.GONE);
        for (int i = 0; i < size; i++) {

            holder.imgs[i].setVisibility(View.VISIBLE);

            if (!Utils.isEmpty(myUserInfoList.get(i).getHeadImgUrl()))
                Picasso.with(context)
                        .load(myUserInfoList.get(i).getHeadImgUrl())
                        .transform(new RoundTransform(20))
                        .error(R.mipmap.ic_touxiang_moren)
                        .into(holder.imgs[i]);
        }

        final int[] sign = {Integer.parseInt(clockBeanList.get(position).getSwitchs() + "")};


            if (sign[0] == 0)
                holder.img_btn.setImageResource(R.drawable.btn_clock_false);
            else
                holder.img_btn.setImageResource(R.mipmap.btn_clock_qunzu);


        holder.img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("qqqqqqqqSSS", sign[0] + "?" + position);

                if (sign[0] == 1) {
                    sign[0] = 0;
                    holder.img_btn.setImageResource(R.drawable.btn_clock_false);
                } else {
                    sign[0] = 1;
                    holder.img_btn.setImageResource(R.mipmap.btn_clock_qunzu);
                }

            }
        });

        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, QunzuEditActivity.class);
                intent.putExtra("clock",  clockBeanList.get(position));
                intent.putExtra("uesr", (Serializable) myUserInfoList);
                context.startActivity(intent);
            }
        });
        holder.rl_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopup(position);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_zhuiping:
                context.startActivity(new Intent(context, PingLunActivity.class));
                break;

        }
    }


    @Override
    public int getItemCount() {
        return clockBeanList.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_btn,img1,img2,img3,img4,img5;
        ImageView[] imgs;
        TextView tv_time, tv_context;
        RelativeLayout rl_item;

        public MyViewHolder(View view) {
            super(view);

            img_btn = (ImageView) view.findViewById(R.id.img_btn);
            img1 = (ImageView) view.findViewById(R.id.img1);
            img2 = (ImageView) view.findViewById(R.id.img2);
            img3 = (ImageView) view.findViewById(R.id.img3);
            img4 = (ImageView) view.findViewById(R.id.img4);
            img5 = (ImageView) view.findViewById(R.id.img5);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            imgs= new ImageView[]{img1, img2, img3, img4, img5};
            tv_context = (TextView) view.findViewById(R.id.tv_context);

            rl_item= (RelativeLayout) view.findViewById(R.id.rl_item);

        }


    }




    private View contentViewSign;
    private PopupWindow mPopWindow;
    private TextView tv_quxiao, tv_queding, tv_context;

    private void showPopup(final int pos) {
        contentViewSign = LayoutInflater.from(context).inflate(R.layout.popup_main, null);
        tv_quxiao = (TextView) contentViewSign.findViewById(R.id.tv_quxiao);
        tv_queding = (TextView) contentViewSign.findViewById(R.id.tv_queren);
        tv_context = (TextView) contentViewSign.findViewById(R.id.tv_context);
        tv_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        tv_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delPos=pos;
                clockCreater=clockBeanList.get(pos).getClockCreater();
                clockId=clockBeanList.get(pos).getClockId();
                new deleteClock().execute();

                //刷新数据
            }
        });
        ((TextView) contentViewSign.findViewById(R.id.tv_title)).setText("删除闹钟");
        tv_context.setText("是否确认删除闹钟？");
        mPopWindow = new PopupWindow(contentViewSign);
        mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        mPopWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //点击空白处时，隐藏掉pop窗口
        mPopWindow.setFocusable(true);
//        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);
        backgroundAlpha(0.5f);
        //添加pop窗口关闭事件
        mPopWindow.setOnDismissListener(new poponDismissListener());
        mPopWindow.showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) context).getWindow().setAttributes(lp); //添加pop窗口关闭事件
    }


    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }

    }



    private int clockCreater , clockId,delPos;
    class deleteClock extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String url = "/happy/clock/deleteClock";
            url=url+"?clockCreater="+clockCreater+"&clockId="+clockId;
            String result = HttpUtils.doGet(context, url);
            Log.e("qqqqqqqRRR",result);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!Utils.isEmpty(s) && "100".equals(s)) {
                Toast.makeText(context, "删除闹钟成功", Toast.LENGTH_SHORT).show();
                clockBeanDao.delete(clockBeanList.get(delPos));
                clockBeanList.remove(clockBeanList.get(delPos));
                context.upData();
                mPopWindow.dismiss();
                notifyDataSetChanged();
            }
        }
    }


}