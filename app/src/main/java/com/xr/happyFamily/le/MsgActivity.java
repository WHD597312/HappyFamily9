package com.xr.happyFamily.le;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xr.happyFamily.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2018/5/22.
 */

public class MsgActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.img_all)
    ImageView imgAll;
    @BindView(R.id.rl_all)
    RelativeLayout rlAll;
    @BindView(R.id.img_yixiang)
    ImageView imgYixiang;
    @BindView(R.id.rl_yixiang)
    RelativeLayout rlYixiang;
    @BindView(R.id.list_msg)
    ListView listMsg;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.rl_pipei)
    RelativeLayout rlPipei;
    @BindView(R.id.gv_yixiang)
    GridView gvYixiang;
    @BindView(R.id.ll_yixiang)
    LinearLayout llYixiang;

    boolean isMsg=true;

    private int[] images = {R.mipmap.ic_air_error,R.mipmap.ic_air_error,R.mipmap.ic_air_error,R.mipmap.ic_air_error,
            R.mipmap.ic_air_error,R.mipmap.ic_air_error,R.mipmap.ic_air_error};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuyuan_msg);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        titleText.setText("消息");
        imgRight.setVisibility(View.GONE);

        gvYixiang.setAdapter(new MyAdapter(this,images));
        gvYixiang.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                startActivity(new Intent(MsgActivity.this,XQActivity.class));
            }
        });
    }


    @OnClick({R.id.rl_all,R.id.rl_yixiang,R.id.back,R.id.rl_pipei})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.rl_all:
                if (!isMsg) {
                    listMsg.setVisibility(View.VISIBLE);
                    llYixiang.setVisibility(View.GONE);
                    imgAll.setVisibility(View.VISIBLE);
                    imgYixiang.setVisibility(View.INVISIBLE);
                    isMsg = true;
                }
                break;

            case R.id.rl_yixiang:
                if (isMsg) {
                    listMsg.setVisibility(View.GONE);
                    llYixiang.setVisibility(View.VISIBLE);
                    imgAll.setVisibility(View.INVISIBLE);
                    imgYixiang.setVisibility(View.VISIBLE);
                    isMsg = false;
                }
                break;

            case R.id.rl_pipei:
                startActivity(new Intent(MsgActivity.this,HistoryActivity.class));
                break;
        }
    }



    private static class MyAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private int[] images;

        public MyAdapter(Context context, int[] images){
            this.images = images;
            layoutInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return images[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = layoutInflater.inflate(R.layout.item_msg_yixiang,null);
            ImageView iv = (ImageView) v.findViewById(R.id.iv_gridView_item);
            iv.setImageResource(images[position]);
            return v;
        }
    }
}