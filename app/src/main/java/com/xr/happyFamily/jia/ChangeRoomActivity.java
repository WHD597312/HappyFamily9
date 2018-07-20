package com.xr.happyFamily.jia;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.xr.happyFamily.main.MainActivity;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
    Bitmap bitmap;
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


//    public Bitmap getPicture(String path){
//        Bitmap bm=null;
//        URL url;
//        try {
//            url = new URL(path);//创建URL对象
//            URLConnection conn=url.openConnection();//获取URL对象对应的连接
//            conn.connect();//打开连接
//            InputStream is=conn.getInputStream();//获取输入流对象
//            bm=BitmapFactory.decodeStream(is);//根据输入流对象创建Bitmap对象
//        } catch (MalformedURLException e1) {
//            e1.printStackTrace();//输出异常信息
//        }catch (IOException e) {
//            e.printStackTrace();//输出异常信息
//        }
//
//
//        return bm;
//    }


//    Integer imgs[] = {R.mipmap.chifang, R.mipmap.keting, R.mipmap.weishengjian, R.mipmap.woshi, R.mipmap.yangtai};
    Integer imgs[] = {R.mipmap.chifang, R.mipmap.keting, R.mipmap.weishengjian, R.mipmap.woshi, R.mipmap.yangtai};

    private void initRoom() {

        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            String roomType = room.getRoomType();
//            if ("厨房".equals(roomType)) {
//
//                roomList.add(room);
//            } else if ("客厅".equals(roomType)) {
//
//                roomList.add(room);
//
//            } else if ("卫生间".equals(roomType)) {
//
//                roomList.add(room);
//            } else if ("卧室".equals(roomType)) {
//
//                roomList.add(room);
//            } else if ("阳台".equals(roomType)) {
//
//                roomList.add(room);
//            } else {
//
//                roomList.add(room);
//            }
            roomList.add(room);
        }
    }

    @OnClick({R.id.li_change})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.li_change:
                finish();
                overridePendingTransition(R.anim.topin,R.anim.topin);
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
