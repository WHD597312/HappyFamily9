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
import com.xr.happyFamily.login.rigest.RegistActivity;
import com.xr.happyFamily.login.rigest.RegistFinishActivity;

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
                int a=position;
                Log.i("ppppppp", "---->: "+a);
                switch (position){
                    case 0:
                        Intent intent1 = new Intent();
                        ChangeRoomActivity.this.setResult(1,intent1);
                        finish();
//                        Intent intent = new Intent(ChangeRoomActivity.this,MyPaperActivity.class);
//                        intent.putExtra("item",1);
//                        startActivity(intent);
                        break;

                    case 1:
                        Intent intent2 = new Intent();
                        ChangeRoomActivity.this.setResult(2,intent2);
                        finish();
//                        Intent intent1 = new Intent(ChangeRoomActivity.this,MyPaperActivity.class);
//                        intent1.putExtra("item",2);
//                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent3 = new Intent();
                        ChangeRoomActivity.this.setResult(3,intent3);
                        finish();
//                        Intent intent2 = new Intent(ChangeRoomActivity.this,MyPaperActivity.class);
//                        intent2.putExtra("item",3);
//                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent4 = new Intent();
                        ChangeRoomActivity.this.setResult(4,intent4);
                        finish();
//                        Intent intent3 = new Intent(ChangeRoomActivity.this,MyPaperActivity.class);
//                        intent3.putExtra("item",4);
//                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent5 = new Intent();
                        ChangeRoomActivity.this.setResult(5,intent5);
                        finish();
//                        Intent intent5 = new Intent(ChangeRoomActivity.this,MyPaperActivity.class);
//                        intent5.putExtra("item",5);
//                        startActivity(intent5);
                        break;
                }

            }  });
    }

    private void initRoom() {

        Room chufang = new Room((long)0,"",0,"",R.mipmap.chifang);
        roomList.add(chufang);
        Room keting = new Room((long)0,"",0,"",R.mipmap.keting);
        roomList.add(keting);
        Room weishengjian = new Room((long)0,"",0,"",R.mipmap.weishengjian);
        roomList.add(weishengjian);
        Room woshi = new Room((long)0,"",0,"",R.mipmap.woshi);
        roomList.add(woshi);
        Room yangtai = new Room((long)0,"",0,"",R.mipmap.yangtai);
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
