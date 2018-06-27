package com.xr.happyFamily.jia;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.activity.QRScannerActivity;
import com.xr.happyFamily.jia.adapter.ManagementGridViewAdapter;
import com.xr.happyFamily.jia.pojo.Equipment;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.jia.titleview.TitleView;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddRoomActivity extends AppCompatActivity {
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
    List<Room> rooms;
    long houseId=1;
    long roomId;
    int mPoistion=-1;
    private RoomDaoImpl roomDao;
    protected void onCreate(Bundle savadInstanceState) {
        super.onCreate(savadInstanceState);
        setContentView(R.layout.activity_home_addroom);
        unbinder = ButterKnife.bind(this);
        titleView = (TitleView) findViewById(R.id.title_addroom);
        titleView.setTitleText("添加房间");
        mGridView = (GridView) findViewById(R.id.gv_management_room);
        mGridData = new ArrayList<>();
        roomDao= new RoomDaoImpl(getApplicationContext());
        rooms = roomDao.findByAllRoom();
        roomId= rooms.size()+1;
        for (int i=0; i<img.length; i++) {
            Equipment item = new Equipment();
            item.setName(localCartoonText[i]);
            item.setImgeId(img[i]);
            mGridData.add(item);
        }
        mGridViewAdapter = new ManagementGridViewAdapter(this, R.layout.activity_management_item, mGridData);
        mGridView.setAdapter(mGridViewAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {


                mPoistion=position;
                mGridViewAdapter.setSelectedPosition(mPoistion);
                mGridViewAdapter.notifyDataSetInvalidated();
                switch (position) {
                    case 0:
                        roomType="厨房";
                        roomName="厨房";
                        Toast.makeText(AddRoomActivity.this,"1",Toast.LENGTH_SHORT).show();
                        Map<String,Object> params=new HashMap<>();
                        params.put("roomName",roomName);
                        params.put("roomType",roomType);
                        params.put("houseId",houseId);
                        new AddRoomActivity.AddroomAsyncTask().execute(params);
                        break;
                    case 1:
                        roomType="卧室";
                        roomName="卧室";
                        Toast.makeText(AddRoomActivity.this,"2",Toast.LENGTH_SHORT).show();
                        Map<String,Object> params1=new HashMap<>();
                        params1.put("roomName",roomName);
                        params1.put("roomType",roomType);
                        params1.put("houseId",houseId);

                        new AddRoomActivity.AddroomAsyncTask().execute(params1);
                        break;
                    case 3:
                        roomType="客厅";
                        roomName="客厅";
                        Toast.makeText(AddRoomActivity.this,"3",Toast.LENGTH_SHORT).show();
                        Map<String,Object> params2=new HashMap<>();
                        params2.put("roomName",roomName);
                        params2.put("roomType",roomType);
                        params2.put("houseId",houseId);

                        new AddRoomActivity.AddroomAsyncTask().execute(params2);
                        break;
                    case 4:
                        roomType="阳台";
                        roomName="阳台";
                        Toast.makeText(AddRoomActivity.this,"4",Toast.LENGTH_SHORT).show();
                        Map<String,Object> params3=new HashMap<>();
                        params3.put("roomName",roomName);
                        params3.put("roomType",roomType);
                        params3.put("houseId",houseId);

                        new AddRoomActivity.AddroomAsyncTask().execute(params3);
                        break;
                    case 5:
                        Toast.makeText(AddRoomActivity.this,"5",Toast.LENGTH_SHORT).show();
                        roomType="卫生间";
                        roomName="卫生间";
                        Map<String,Object> params4=new HashMap<>();
                        params4.put("roomName",roomName);
                        params4.put("roomType",roomType);
                        params4.put("houseId",houseId);

                        new AddRoomActivity.AddroomAsyncTask().execute(params4);
                        break;

                    default:
                        break;
                }
            }
        });
        roomDao= new RoomDaoImpl(getApplicationContext());

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

                        Room room = roomDao.findById((long) roomId);
                        if (room!=null){
                            room.setRoomId((long)roomId);
                            room.setHouseId((int)houseId);
                            room.setRoomType(roomType);
                            roomDao.update(room);
                            Log.i("dddddd11qqq1", "doInBackground:---> "+room);
                        }else {
                            room = new Room((long)roomId,  roomName,  (int)houseId, roomType,0);
                            roomDao.insert(room);
                            Log.i("dddddd1111", "doInBackground:---> "+room);
                        }

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
                    Utils.showToast(AddRoomActivity.this, "添加房间失败，请重试");
                    break;

                case 100:

                    startActivity(new Intent(AddRoomActivity.this, MyPaperActivity.class));

                    break;
            }
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
