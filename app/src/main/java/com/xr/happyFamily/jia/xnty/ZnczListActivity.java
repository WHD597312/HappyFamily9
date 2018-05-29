package com.xr.happyFamily.jia.xnty;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.adapter.ZnczAdapter;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ZnczListActivity extends AppCompatActivity {
//    @BindView(R.id.zncz_adde)
//    ImageView imageView1;
//    @BindView(R.id.iv_zncz_1)
//    ImageView imageView;
//    @BindView(R.id.tv_zncz_1)
//    TextView textView1;//
    private List<String> mData = new ArrayList<String>(Arrays.asList("智能终端传感器", "户外空调", "空气净化器", "电暖气","除湿器","净水器"));
   /* private Integer[] img ={R.mipmap.zncz_sb1,R.mipmap.zncz_hw,R.mipmap.zncz_kqjh,R.mipmap.zncz_dnq,
            R.mipmap.zncz_csq,R.mipmap.zncz_jsq};*/
    private Context context;
    Unbinder unbinder ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        context = this;
        setContentView(R.layout.activity_zncz_add);
        unbinder = ButterKnife.bind(this);
        ListView listView = (ListView) findViewById(R.id.zncz_add_list);
        ZnczAdapter mAdapter = new ZnczAdapter(context,mData);
        listView.setAdapter(mAdapter);

    }
    @OnClick({R.id.tv_add_fh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_fh:
                finish();
                break;

        }

    }

}
