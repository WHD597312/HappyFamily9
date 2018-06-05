package com.xr.happyFamily.jia;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ChangeEquipmentActivity extends AppCompatActivity {

    private List<String> mData = new ArrayList<String>(Arrays.asList("智能终端升级版", "户外空调", "智能插座基础版", "空气净化器"));
    private Context context;
    Unbinder unbinder ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        context = this;
        setContentView(R.layout.activity_home_xnsb);
        unbinder = ButterKnife.bind(this);
        ListView listView = (ListView) findViewById(R.id.xnsb_list);
        MyAdapter mAdapter = new MyAdapter(context, mData);
        listView.setAdapter(mAdapter);
    }
    @OnClick({R.id.ib_xnty})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_xnty:
              finish();
                break;

        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }


}





