package com.xr.happyFamily.jia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.activity.QRScannerActivity;
import com.xr.happyFamily.jia.adapter.ManagementGridViewAdapter;
import com.xr.happyFamily.jia.pojo.Equipment;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.jia.titleview.TitleView;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.login.rigest.RegistActivity;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.BitmapCompressUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ManagementActivity extends AppCompatActivity {
    private String[] localCartoonText = {"厨房", "卧室", "客厅","餐厅", "卫生间","浴室","儿童房","婴儿房","活动室","媒体房",
            "办公室","休闲室","书房","工作室","衣帽间","后院"};
    private Integer[] img ={R.mipmap.house_cf,R.mipmap.house_ws,R.mipmap.house_kt,R.mipmap.house_ct,
            R.mipmap.house_wsj, R.mipmap.house_ys,R.mipmap.house_etf,R.mipmap.house_yef,
            R.mipmap.house_hds, R.mipmap.house_mt,R.mipmap.house_bgs,R.mipmap.house_xxs,
            R.mipmap.house_sf, R.mipmap.house_gzs,R.mipmap.house_ym,R.mipmap.house_hy};
    private GridView mGridView = null;
    private ManagementGridViewAdapter mGridViewAdapter = null;
    private ArrayList<Equipment> mGridData = null;
    String ip = "http://47.98.131.11:8084";
    Unbinder unbinder;
    TitleView titleView;
    String  roomType;
    String  roomName;
    long roomId;
    List<Room> rooms;
    private SharedPreferences mPositionPreferences;

    int mPoistion=-1;
    private RoomDaoImpl roomDao;
    private HourseDaoImpl hourseDao;
    long houseId;
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_management);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        unbinder = ButterKnife.bind(this);
        roomDao= new RoomDaoImpl(getApplicationContext());
       Intent intent=getIntent();
       houseId=intent.getLongExtra("houseId",0);
        mPositionPreferences = getSharedPreferences("position", Context.MODE_PRIVATE);
        Log.e("rrrrrrrrr", "onCreate: ---->"+houseId );
        mGridView = (GridView) findViewById(R.id.gv_management_my);
        mGridData = new ArrayList<>();
        for (int i=0; i<img.length; i++) {
            Equipment item = new Equipment();
            item.setName(localCartoonText[i]);
            item.setImgeId(img[i]);
            mGridData.add(item);
        }
        mGridViewAdapter = new ManagementGridViewAdapter(this, R.layout.activity_management_item, mGridData);
        mGridView.setAdapter(mGridViewAdapter);

        mGridViewAdapter.setSelectedPosition(mPoistion);
        titleView = (TitleView) findViewById(R.id.title1);
        titleView.setTitleText("添加房间");
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {

                String name=localCartoonText[position];
                roomName=name;
                roomType=roomName;
                mPoistion=position;
                mGridViewAdapter.setSelectedPosition(mPoistion);
                mGridViewAdapter.notifyDataSetInvalidated();

            }
        });
    }

    class AddroomAsyncTask extends AsyncTask<Map<String, Object>, Void, Integer> {
        @Override
        protected Integer doInBackground(Map<String, Object>... maps) {
            int code = 0;
            Map<String, Object> params = maps[0];
            String result = HttpUtils.postOkHpptRequest(ip+"/family/room/registerRoom", params);
            Log.i("aaaaaa", "doInBackground:---> "+result);
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("returnCode");
                    JSONObject returnData=jsonObject.getJSONObject("returnData");
                    if (code == 100) {
                        int roomId=returnData.getInt("roomId");
                        String roomName=returnData.getString("roomName");
                        String roomType=returnData.getString("roomType");
                        int houseId=returnData.getInt("houseId");

                        Room room = new Room((long)roomId,  roomName,  houseId, roomType,0);
                        room.setRoomId((long)roomId);
                        room.setHouseId(houseId);
                        room.setRoomType(roomType);
//                        String imgAddress=room.getImgAddress();
//                        List<Room> roomList=roomDao.findRoomByType(roomType);
//                        if (roomList.isEmpty()){
//                            new LoadRoomsImage().execute(room);
//                        }else {
//                            Room room1=roomList.get(0);
//                            room.setImgAddress(room1.getImgAddress());
//                        }
                        roomDao.insert(room);
                        List<Room> rooms=roomDao.findAllRoomInHouse(houseId);
                        SharedPreferences.Editor editor=mPositionPreferences.edit();
                        int postion=rooms.size()+1;
                        editor.putInt("position",postion);
                        editor.commit();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            switch (code) {
                case 3001:
                    Utils.showToast(ManagementActivity.this, "添加房间失败，请重试");
                    break;
                case 100:
                    Toast.makeText(ManagementActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.putExtra("houseId",houseId);
                    setResult(6000,intent);
                    finish();
                    break;
            }
        }
    }

    private String imageUrl="http://p9zaf8j1m.bkt.clouddn.com/room/choose/";
    private Map<String,Room> roomMap=new LinkedHashMap<>();
    class LoadRoomsImage extends AsyncTask<Room,Void,Void>{

        @Override
        protected Void doInBackground(Room... rooms) {
            try {
                Room room2=rooms[0];
                String roomType2=room2.getRoomType();
                List<Room> rooms2=roomDao.findRoomByType(roomType2);
                for (Room room:rooms2){
                    String roomType=room.getRoomType();
                    roomMap.put(roomType,room);
                }
                for (Map.Entry<String,Room> entry:roomMap.entrySet()){
                    String roomType=entry.getKey();
                    Room room=entry.getValue();
                    String image="";
                    if ("客厅".equals(roomType)){
                        image="living_room.png";
                    }else if ("卧室".equals(roomType)){
                        image="bedroom.png";
                    }else if ("餐厅".equals(roomType)){
                        image="canteen.png";
                    }else if ("卫生间".equals(roomType)){
                        image="toilet.png";
                    }else if ("卧室".equals(roomType)){
                        image="bedroom.png";
                    }else if ("餐厅".equals(roomType)){
                        image="canteen.png";
                    }else if ("卫生间".equals(roomType)){
                        image="toilet.png";
                    }else if ("浴室".equals(roomType)){
                        image="bedroom.png";
                    }else if ("厨房".equals(roomType)){
                        image="kitchen.png";
                    }else if ("儿童房".equals(roomType)){
                        image="childrens_room.png";
                    }else if ("婴儿房".equals(roomType)){
                        image="baby_room.png";
                    }else if ("活动室".equals(roomType)){
                        image="activity_room.png";
                    }else if ("媒体房".equals(roomType)){
                        image="media_room.png";
                    }else if ("办公室".equals(roomType)){
                        image="office.png";
                    }else if ("休闲室".equals(roomType)){
                        image="lounge.png";
                    }else if ("书房".equals(roomType)){
                        image="study.png";
                    }else if ("工作室".equals(roomType)){
                        image="studio.png";
                    }else if ("衣帽间".equals(roomType)){
                        image="cloakroom.png";
                    }else if ("后院".equals(roomType)){
                        image="backyard.png";
                    }
                    String url=imageUrl+image;
                    SharedPreferences my=getSharedPreferences("my", Context.MODE_PRIVATE);
                    String token = my.getString("token", "token");
                    GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder().addHeader("authorization", token).build());
                    Bitmap bitmap = Glide.with(ManagementActivity.this)
                            .load(glideUrl)
                            .asBitmap()
                            .centerCrop()
                            .into(1440, 442)
                            .get();
                    if (bitmap != null) {
                        File file = BitmapCompressUtils.compressImage(bitmap);
                        String path=file.getPath();
                        room.setImgAddress(path);
                        List<Room> roomList=roomDao.findRoomByType(roomType);
                        roomDao.updateRooms(roomList);
                    }
                    Thread.sleep(2000);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
    @OnClick({R.id.bt_management_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_management_ok:
                Map<String,Object> params=new HashMap<>();
                params.put("roomName",roomName);
                params.put("roomType",roomType);
                params.put("houseId",houseId);
                new ManagementActivity.AddroomAsyncTask().execute(params);
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
