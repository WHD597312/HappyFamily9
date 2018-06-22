package com.xr.happyFamily.jia;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.activity.QRScannerActivity;
import com.xr.happyFamily.jia.adapter.ManagementGridViewAdapter;
import com.xr.happyFamily.jia.pojo.Equipment;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.jia.titleview.TitleView;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.login.rigest.RegistActivity;
import com.xr.happyFamily.together.http.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
//String ip = "http://192.168.168.27:8084";

    Unbinder unbinder;
    TitleView titleView;
    String  roomType;
    String  roomName;
    long houseId=1;
    long roomId;
    List<Room> rooms;

    int mPoistion=-1;
    private RoomDaoImpl roomDao;
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_management);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        unbinder = ButterKnife.bind(this);
        roomDao= new RoomDaoImpl(getApplicationContext());
        rooms = roomDao.findByAllRoom();
        roomId= rooms.size()+1;
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

//                view.setBackgroundResource(R.color.color_gray2);
//              //   gridView中点击 item为选中状态(背景颜色)
//                for(int n=0;n<mGridData.size();n++){
//                    View item = mGridView.getChildAt(n).findViewById(R.id.li_item_addroom);
//                    if (position == n) {//当前选中的Item改变背景颜色
//                        item.setBackgroundResource(R.color.color_gray2);
//                    } else {
//                        item.setBackgroundResource(R.color.white);
//                    }
//                }
                mPoistion=position;
                mGridViewAdapter.setSelectedPosition(mPoistion);
                mGridViewAdapter.notifyDataSetInvalidated();
                switch (position) {
                    case 0:
                        roomType="厨房";
                        roomName="厨房";
                        Toast.makeText(ManagementActivity.this,"1",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        roomType="卧室";
                        roomName="卧室";
                        Toast.makeText(ManagementActivity.this,"2",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        roomType="客厅";
                        roomName="客厅";
                        Toast.makeText(ManagementActivity.this,"1",Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        roomType="餐厅";
                        roomName="餐厅";
                        Toast.makeText(ManagementActivity.this,"2",Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        roomType="卫生间";
                        roomName="餐厅";
                        Toast.makeText(ManagementActivity.this,"1",Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }
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
                if (!com.xr.happyFamily.login.util.Utils.isEmpty(result)) {
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
                    com.xr.happyFamily.login.util.Utils.showToast(ManagementActivity.this, "添加房间失败，请重试");
                    break;

                case 100:

                    startActivity(new Intent(ManagementActivity.this, MyPaperActivity.class));

                    break;
            }
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
                params.put("roomId",roomId);
                new ManagementActivity.AddroomAsyncTask().execute(params);
//                startActivity(new Intent(this, AddEquipmentActivity.class));
                break;
//            case R.id.iv_mzxing:
//                startActivity(new Intent(this, QRScannerActivity.class));
//                break;
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
