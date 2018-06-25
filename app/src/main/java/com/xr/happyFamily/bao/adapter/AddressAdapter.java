package com.xr.happyFamily.bao.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bean.AddressBean;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//快递列表适配器

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<AddressBean.ReturnData> list;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;
    private InnerItemOnclickListener mListener;
    private MyDialog dialog;

    public AddressAdapter(Context context, List<AddressBean.ReturnData> list) {
        this.context=context;
        this.list=list;
    }
    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }


    public interface OnItemListener {
        void onClick(View v, int pos, String projectc);
    }
    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }


    /**
     *按钮点击事件需要的方法
     */
    public void buttonSetOnclick(ButtonInterface buttonInterface){
        this.buttonInterface=buttonInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface ButtonInterface{
        public void onclick( View view,int position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_address, parent,
                false),mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_name.setText(list.get(position).getContact());
        holder.tv_tel.setText(list.get(position).getTel());
        holder.tv_address.setText(list.get(position).getReceiveProvince()+" "+list.get(position).getReceiveCity()+" "+list.get(position).getReceiveCounty()+" "+list.get(position).getReceiveAddress());
        holder.img_bianji.setOnClickListener(this);
        holder.img_del.setOnClickListener(this);
        holder.img_bianji.setTag(position);
        holder.img_del.setTag(position);
        if(list.get(position).getIsDefault()==1){
            holder.img_choose.setImageResource(R.mipmap.xuanzhong);
        }else {
            holder.img_choose.setImageResource(R.mipmap.weixuanzhong3x);
        }
        if (defItem != -1) {
            if (defItem == position) {
                dialog = MyDialog.showDialog(context);
                dialog.show();
                holder.img_choose.setImageResource(R.mipmap.xuanzhong);
                Map<String, Object> params = new HashMap<>();
                params.put("receiveId", list.get(position).getReceiveId());
                new setAddressAsync().execute(params);
            } else {
                holder.img_choose.setImageResource(R.mipmap.weixuanzhong3x);
            }
        }
        holder.img_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonInterface!=null) {
//                  接口实例化后的而对象，调用重写后的方法
                    buttonInterface.onclick(v,position);
                }
            }
        });
    }

    public interface InnerItemOnclickListener {
        void itemClick(View v);
    }

    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener){
        this.mListener=listener;
    }
    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView img_choose,img_bianji,img_del;
        TextView tv_name,tv_tel,tv_address;
        private MyItemClickListener mListener;

        public MyViewHolder(View view,MyItemClickListener myItemClickListener) {
            super(view);
            img_choose = (ImageView) view.findViewById(R.id.img_choose);
            img_bianji = (ImageView) view.findViewById(R.id.img_bianji);
            img_del = (ImageView) view.findViewById(R.id.img_del);
            tv_name= (TextView) view.findViewById(R.id.tv_name);
            tv_tel= (TextView) view.findViewById(R.id.tv_tel);
            tv_address= (TextView) view.findViewById(R.id.tv_address);
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getLayoutPosition());
            }

        }
    }


    private MyItemClickListener mItemClickListener;
    public void setOnItemClickListener(MyItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }



    class setAddressAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "receive/setDefaultReceive";
            String result = HttpUtils.headerPostOkHpptRequest(context, url, params);
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
                MyDialog.closeDialog(dialog);

            }
        }
    }
}