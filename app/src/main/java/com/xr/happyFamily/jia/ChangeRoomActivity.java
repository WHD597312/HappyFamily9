package com.xr.happyFamily.jia;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.Fragment.BalconyFragment;
import com.xr.happyFamily.jia.Fragment.BathroomFragment;
import com.xr.happyFamily.jia.Fragment.KitchenFragment;
import com.xr.happyFamily.jia.Fragment.LivingFragment;
import com.xr.happyFamily.jia.Fragment.RoomFragment;
import com.xr.happyFamily.jia.adapter.RoomAdapter;
import com.xr.happyFamily.jia.pojo.Room;
import android.widget.AdapterView.OnItemClickListener;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ChangeRoomActivity extends AppCompatActivity {
    private List<Room> roomList = new ArrayList<>();
    private RecyclerView mRecyvlerView = null;
    private ImageView back;
    private Context context;
    @BindView(R.id.change_list)
    ListView change_list;
    Unbinder unbinder;

    protected void onCreate(Bundle savadInstanceState) {
        super.onCreate(savadInstanceState);
        setContentView(R.layout.activity_home_change);
        unbinder = ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initRoom(); // 初始化
        RoomAdapter adapter = new RoomAdapter(ChangeRoomActivity.this, R.layout.activity_home_change_item, roomList);
//        ListView listView = (ListView) findViewById(R.id.change_list);

        change_list.setAdapter(adapter);
        change_list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent localIntent1=new Intent(ChangeRoomActivity.this,KitchenFragment.class);
                        break;

                    case 1:
                        Intent localIntent2=new Intent(ChangeRoomActivity.this,RoomFragment.class);
                        break;
                    case 2:
                        Intent localIntent3=new Intent(getApplicationContext(),BathroomFragment.class);
                        break;
                    case 3:
                        Intent localIntent4=new Intent(getApplicationContext(),LivingFragment.class);
                        break;
                    case 4:
                        Intent localIntent=new Intent(getApplicationContext(),BalconyFragment.class);
                        break;
                }

            }  });
    }

    private void initRoom() {

        Room chufang = new Room(R.mipmap.chifang);
        roomList.add(chufang);
        Room keting = new Room(R.mipmap.keting);
        roomList.add(keting);
        Room weishengjian = new Room(R.mipmap.weishengjian);
        roomList.add(weishengjian);
        Room woshi = new Room(R.mipmap.woshi);
        roomList.add(woshi);
        Room yangtai = new Room(R.mipmap.yangtai);
        roomList.add(yangtai);
    }

    @OnClick({R.id.li_change})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.li_change:
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
