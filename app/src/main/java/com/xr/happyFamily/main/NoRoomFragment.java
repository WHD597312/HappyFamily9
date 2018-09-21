package com.xr.happyFamily.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.ChangeEquipmentActivity;
import com.xr.happyFamily.jia.ChangeRoomActivity;
import com.xr.happyFamily.jia.ChooseHourseActivity;
import com.xr.happyFamily.jia.ManagementActivity;
import com.xr.happyFamily.jia.adapter.RoomAdapter;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.jia.pojo.Room;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

public class NoRoomFragment extends Fragment {
    View view;
    Unbinder unbinder;
    public static final int MREQUEST_CODE=2;
    private HourseDaoImpl hourseDao;
    @BindView(R.id.tv_23_my1) TextView tv_23_my1;/**家庭温度*/
    Hourse hourse;
    private long houseId;
    @BindView(R.id.rl_page) RelativeLayout rl_page;
    @BindView(R.id.rl_noroom_rl) RelativeLayout rl_noroom_rl;
    @BindView(R.id.ib_croom) ImageButton ib_croom;
    @BindView(R.id.tv_noroom_hoursename)
    TextView textViewhousename;
    @BindView(R.id.tv_bz1)
    TextView tv_bz1;

    private String temperature;
    SharedPreferences mPositionPreferences;
    private RoomDaoImpl roomDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_noroom_family,container,false);
        unbinder=ButterKnife.bind(this,view);
        hourseDao=new HourseDaoImpl(getActivity());
        roomDao = new RoomDaoImpl(getActivity());
        hourse=hourseDao.findById(houseId);
        mPositionPreferences = getActivity().getSharedPreferences("position", MODE_PRIVATE);

        if (hourse!=null){
            if (!TextUtils.isEmpty(temperature)){
                tv_23_my1.setText(temperature);
            }
            String hourseName = hourse.getHouseName();
            String address=hourse.getHouseAddress();
            textViewhousename.setText(hourseName+"·"+address);
        }

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!TextUtils.isEmpty(temperature)){
            tv_23_my1.setText(temperature);
        }
    }

    @OnClick({R.id.rl_page,R.id.btn_add_room,R.id.tv_noroom_hoursename,R.id.ib_croom})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_page:
                Intent intent4=new Intent(getActivity(), ChangeEquipmentActivity.class);
                startActivity(intent4);
                break;
            case R.id.btn_add_room:
                Intent intent=new Intent(getActivity(), ManagementActivity.class);
                intent.putExtra("houseId",houseId);
                startActivityForResult(intent,MREQUEST_CODE);
                break;
            case R.id.tv_noroom_hoursename:
                Intent intent2=new Intent(getActivity(), ChooseHourseActivity.class);
                startActivity(intent2);
                break;
            case R.id.ib_croom:
                List<Room> rooms=roomDao.findAllRoomInHouse(houseId);
                if (rooms==null || rooms.isEmpty()){
                    Toast.makeText(getActivity(),"您还没有创建房间!",Toast.LENGTH_SHORT).show();
                }else {
                    popupmenuWindow();
                }

//                Intent intent3 = new Intent(getActivity(), ChangeRoomActivity.class);
//                intent3.putExtra("houseId", houseId);
//                startActivityForResult(intent3, MREQUEST_CODE);
//                getActivity().overridePendingTransition(R.anim.topout, R.anim.topout);
//
                break;
        }
    }




    Room room;
    List<Room> rooms;
    RoomAdapter adapter;
    private PopupWindow popupWindow1;
    public void popupmenuWindow() {
        if (popupWindow1 != null && popupWindow1.isShowing()) {
            return;
        }

        View view = View.inflate(getActivity(), R.layout.activity_home_change, null);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        ListView change_list = (ListView) view.findViewById(R.id.change_list);
        RelativeLayout li_change = (RelativeLayout) view.findViewById(R.id.li_change);
        popupWindow1 = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        //点击空白处时，隐藏掉pop窗口
        popupWindow1.setFocusable(true);
        popupWindow1.setOutsideTouchable(true);
        //添加弹出、弹入的动画
        popupWindow1.setAnimationStyle(R.style.ChangroomPopupwindow);
//        ColorDrawable dw = new ColorDrawable(getActivity().getResources().getColor(R.color.white));
//        popupWindow1.setBackgroundDrawable(dw);
        popupWindow1.showAsDropDown(tv_bz1, 0, 0);
//        popupWindow.showAtLocation(tv_home_manager, Gravity.RIGHT, 0, 0);
        //添加按键事件监听

        rooms = roomDao.findAllRoomInHouse(houseId);
        adapter = new RoomAdapter(getActivity(), R.layout.activity_home_change_item, rooms);
//        ListView listView = (ListView) findViewById(R.id.change_list);
        change_list.setVerticalScrollBarEnabled(false);
        change_list.setFastScrollEnabled(false);
        change_list.setAdapter(adapter);
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.li_change:
                        rl_noroom_rl.setVisibility(View.VISIBLE);
                        popupWindow1.dismiss();

                        break;
                }
            }
        };

        change_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("onItemClick","-->"+position);
                Intent intent = new Intent(getActivity(),MainActivity.class);
                intent.putExtra("houseId",houseId);
                SharedPreferences.Editor editor=mPositionPreferences.edit();
                editor.putInt("position",position+1);
                editor.commit();
                popupWindow1.dismiss();
                startActivity(intent);
            }
        });
        li_change.setOnClickListener(listener);
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
        if (!TextUtils.isEmpty(temperature)){
            if (tv_23_my1!=null){
                tv_23_my1.setText(temperature);
            }
        }
    }

    public void setHouseId(long houseId) {
        this.houseId = houseId;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
}
