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
import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.adapter.RoomAdapter;
import com.xr.happyFamily.jia.pojo.Room;
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
    RoomAdapter adapter;
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
        adapter = new RoomAdapter(ChangeRoomActivity.this, R.layout.activity_home_change_item, rooms);
        textView = (TextView) findViewById(R.id.tv_change_1);
//        ListView listView = (ListView) findViewById(R.id.change_list);
        change_list.setVerticalScrollBarEnabled(false);
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
