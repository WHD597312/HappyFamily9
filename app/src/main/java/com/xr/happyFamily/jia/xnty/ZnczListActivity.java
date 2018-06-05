package com.xr.happyFamily.jia.xnty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.xr.happyFamily.R;
import android.widget.AdapterView.OnItemClickListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @BindView(R.id.zncz_add_list)
    ListView listView;//

    private String[] mData = { "户外空调", "空气净化器", "电暖气","除湿器","净水器"};
    private Integer[] img ={R.mipmap.zncz_hw,R.mipmap.zncz_kqjh,R.mipmap.zncz_dnq,
            R.mipmap.zncz_csq,R.mipmap.zncz_jsq};
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
       /* ListView listView = (ListView) findViewById(R.id.zncz_add_list);
        ZnczAdapter mAdapter = new ZnczAdapter(context,mData);
        listView.setAdapter(mAdapter);*/

        final List<HashMap<String, Object>> users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            HashMap<String, Object> user = new HashMap<>();
            user.put("img",img[i]);
            user.put("name", mData[i]);

            users.add(user);
        }
    /*    ListView listView = (ListView) findViewById(R.id.zncz_add_list);
        ZnczAdapter mAdapter = new ZnczAdapter(context,
                users);*/
        SimpleAdapter saImageItems = new SimpleAdapter(this,
                users,
                // 数据来源
                R.layout.activity_xnsb_zncz_item,//每一个user xml 相当ListView的一个组件
                new String[] { "img", "name" },
                // 分别对应view 的id
                new int[] { R.id.iv_zncz_1, R.id.tv_zncz_1});
        // 获取listview
       listView.setAdapter(saImageItems);
       /* listView.setAdapter(mAdapter);*/
        listView.setOnItemClickListener(new OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
// TODO Auto-generated method stub
                Map<String, Object> map = users.get(position);
                switch (position){
                    case 0:
                        Intent intent1 = new Intent();

//                        Intent intent = new Intent(ZnczListActivity.this,SmartSocket.class);
//                        startActivity(intent);
                        ZnczListActivity.this.setResult(1,intent1);
                        finish();
                        break;

                    case 1:
                        Intent intent2 = new Intent();


                        ZnczListActivity.this.setResult(2,intent2);
                        finish();

                        break;
                    case 2:
                        Intent intent3 = new Intent();


                        ZnczListActivity.this.setResult(3,intent3);
                        finish();

                        break;
                    case 3:
                        Intent intent4 = new Intent();


                        ZnczListActivity.this.setResult(4,intent4);
                        finish();

                        break;
                    case 4:
                        Intent intent5 = new Intent();


                        ZnczListActivity.this.setResult(5,intent5);
                        finish();

                        break;
                }


            }
        });
    }
    @OnClick({R.id.tv_add_fh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_fh:
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
