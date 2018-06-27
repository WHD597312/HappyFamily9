package com.xr.happyFamily.jia;

import android.content.Context;
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

import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.Fragment.homeFragment;
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
import com.xr.happyFamily.together.util.Utils;

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
    private String[] localCartoonText = {"厨房", "卧室", "客厅","阳台", "卫生间","浴室","儿童房","婴儿房","活动室","媒体房",
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
//                switch (position) {
//                    case 0:
//                        roomType="厨房";
//                        roomName="厨房";
//                        Toast.makeText(ManagementActivity.this,"1",Toast.LENGTH_SHORT).show();
//                        break;
//                    case 1:
//                        roomType="卧室";
//                        roomName="卧室";
//                        Toast.makeText(ManagementActivity.this,"2",Toast.LENGTH_SHORT).show();
//                        break;
//                    case 2:
//                        roomType="客厅";
//                        roomName="客厅";
//                        Toast.makeText(ManagementActivity.this,"2",Toast.LENGTH_SHORT).show();
//                        break;
//                    case 3:
//                        roomType="阳台";
//                        roomName="阳台";
//                        Toast.makeText(ManagementActivity.this,"3",Toast.LENGTH_SHORT).show();
//                        break;
//                    case 4:
//                        roomType="卫生间";
//                        roomName="卫生间";
//                        Toast.makeText(ManagementActivity.this,"4",Toast.LENGTH_SHORT).show();
//                        break;
//
//                    default:
//                        break;
//                }
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
