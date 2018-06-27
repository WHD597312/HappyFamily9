package com.xr.happyFamily.jia;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.xr.database.dao.RoomDao;
import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.Fragment.BalconyFragment;
import com.xr.happyFamily.jia.Fragment.BathroomFragment;
import com.xr.happyFamily.jia.Fragment.KitchenFragment;
import com.xr.happyFamily.jia.Fragment.LivingFragment;
import com.xr.happyFamily.jia.Fragment.RoomFragment;
import com.xr.happyFamily.jia.adapter.RoomAdapter;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.login.rigest.RegistActivity;
import com.xr.happyFamily.login.rigest.RegistFinishActivity;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

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
    RoomDaoImpl roomDao;
    Room room;
    List<Room> rooms;
    ArrayList str1;
    ArrayList str2;
    ArrayList str3;
    TextView textView;
    long houseId;

    private SharedPreferences mPositionPreferences;
    protected void onCreate(Bundle savadInstanceState) {
        super.onCreate(savadInstanceState);
        setContentView(R.layout.activity_home_change);
        unbinder = ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mPositionPreferences =getSharedPreferences("position", Context.MODE_PRIVATE);
        Intent intent = getIntent();
        houseId = intent.getLongExtra("houseId", 0);
        roomDao = new RoomDaoImpl(getApplicationContext());
        rooms = roomDao.findAllRoomInHouse(houseId);
        initRoom(); // 初始化
        RoomAdapter adapter = new RoomAdapter(ChangeRoomActivity.this, R.layout.activity_home_change_item, roomList);
        textView = (TextView) findViewById(R.id.tv_change_1);
//        ListView listView = (ListView) findViewById(R.id.change_list);

        change_list.setAdapter(adapter);
        change_list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("onItemClick","-->"+position);
                Intent intent = new Intent();
                intent.putExtra("houseId",houseId);
                SharedPreferences.Editor editor=mPositionPreferences.edit();
                editor.putInt("position",position+1);
                editor.commit();
                setResult(6000,intent);
                finish();
            }
        });
    }

    Integer imgs[] = {R.mipmap.chifang, R.mipmap.keting, R.mipmap.weishengjian, R.mipmap.woshi, R.mipmap.yangtai};

    private void initRoom() {
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            String roomType = room.getRoomType();
            if ("厨房".equals(roomType)) {
                room.setImgId(imgs[0]);
                roomList.add(room);
            } else if ("客厅".equals(roomType)) {
                room.setImgId(imgs[1]);
                roomList.add(room);

            } else if ("卫生间".equals(roomType)) {
                room.setImgId(imgs[2]);
                roomList.add(room);
            } else if ("卧室".equals(roomType)) {
                room.setImgId(imgs[3]);
                roomList.add(room);
            } else if ("阳台".equals(roomType)) {
                room.setImgId(imgs[4]);
                roomList.add(room);
            } else {
                room.setImgId(imgs[4]);
                roomList.add(room);
            }
        }
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
        if (unbinder != null) {
            unbinder.unbind();
        }
    }


}
