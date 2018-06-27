package com.xr.happyFamily.jia.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.ChangeRoomActivity;
import com.xr.happyFamily.jia.MyPaperActivity;
import com.xr.happyFamily.jia.activity.AddDeviceActivity;
import com.xr.happyFamily.jia.activity.DeviceDetailActivity;
import com.xr.happyFamily.jia.adapter.GridViewAdapter;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.jia.view_custom.HomeDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RoomFragment extends Fragment {
    private ImageButton mLeftMenu;
    private String[] localCartoonText = {"客厅", "厨房", "卧室", "阳台", "阳台", "阳台",};
    private Integer[] img = {R.mipmap.t, R.mipmap.t, R.mipmap.t, R.mipmap.t, R.mipmap.t, R.mipmap.t};
    Dialog dia ;
    String ip = "http://47.98.131.11:8084";
    private GridViewAdapter mGridViewAdapter = null;
    private List<DeviceChild> mGridData = null;
    Unbinder unbinder;
    @BindView(R.id.bt_balcony_add)
    Button buttonadd;
    @BindView(R.id.tv_balcony_gl)
    TextView textViewgl;
    @BindView(R.id.balcony_li)
    LinearLayout li;
    @BindView(R.id.tv_roomname)
    TextView textViewname;
    String roomName,roomType,roomId;
    @BindView(R.id.gv_balcony_home)
    com.xr.happyFamily.jia.MyGridview mGridView;
    RoomDaoImpl roomDao;
    List<Room> rooms;
    Room room;
    ArrayList str1;
    ArrayList str2;
    ArrayList str3;
    private DeviceChildDaoImpl deviceChildDao;
    private HourseDaoImpl hourseDao;
    long houseId;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_home_balcony, container, false);
        unbinder=ButterKnife.bind(this,view);
        PercentRelativeLayout percentRelativeLayout=
                ( PercentRelativeLayout)view.findViewById(R.id.pr_view);
        TextView textViewr = (TextView) view.findViewById(R.id.tv_roomname);
        percentRelativeLayout.setBackground(getResources().getDrawable(R.mipmap.bg_room));
        str1=new ArrayList();
        str2=new ArrayList();
        str3=new ArrayList();
        roomDao = new RoomDaoImpl(getActivity());
        hourseDao=new HourseDaoImpl(getActivity());

        List<Hourse> hourses=hourseDao.findAllHouse();
        Hourse hourse=hourses.get(0);
        houseId=hourse.getHouseId();
        rooms= roomDao.findByAllRoom();
        for (int i = 0 ;i<rooms.size();i++){
            room=rooms.get(i);
            String type = room.getRoomType();
            str1.add(type);
        }

        Bundle bundle=getArguments();
        if (bundle!=null){
             roomName=bundle.getString("roomName");
             roomType=bundle.getString("roomType");
             roomId=bundle.getString("roomId");
            textViewr.setText(roomName);
        }

        buttonadd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), AddDeviceActivity.class);
                intent.putExtra("roomId",roomId);
                startActivityForResult(intent,6000);

            }
        });
        li.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(getActivity(), ChangeRoomActivity.class),2);
            }
        });
        return view;

    }


@OnClick({R.id.tv_balcony_gl,R.id.iv_home_fh})
public void onClick(View view) {
    switch (view.getId()) {
        case R.id.tv_balcony_gl:
            showPopupMenu(textViewgl);
            break;

        case R.id.iv_home_fh:
            startActivityForResult(new Intent(getActivity(), MyPaperActivity.class),5000);
            break;

    }
}
    @Override
    public void onStart() {
        super.onStart();
        deviceChildDao=new DeviceChildDaoImpl(getActivity());
        mGridData=deviceChildDao.findHouseInRoomDevices(houseId,Long.parseLong(roomId));
        int size=mGridData.size();
        Log.i("size","size:"+size);
        mGridViewAdapter = new GridViewAdapter(getActivity(), R.layout.activity_home_item, mGridData);
        mGridView.setAdapter(mGridViewAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeviceChild deviceChild=mGridData.get(position);
                String deviceName=deviceChild.getName();
                long deviceId=deviceChild.getId();
                Intent intent=new Intent(getActivity(), DeviceDetailActivity.class);
                intent.putExtra("deviceName",deviceName);
                intent.putExtra("deviceId",deviceId);
                startActivity(intent);
            }
        });
    }
    String title;
    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT).show();
//                Log.i("title", "---->: "+item.getTitle());
                title = String.valueOf(item.getTitle());
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                if ("更改房间名".equals(title)){
                    buildUpdateHomeDialog();
                }
                if ("删除房间".equals(title)){
                    dia = new Dialog(getActivity(), R.style.edit_AlertDialog_style);//设置进入时跳出提示框
                    dia.setContentView(R.layout.popview_delete_home);
//                    relativeLayoutre.setBackgroundResource(R.drawable.bg_shape);
                    dia.show();
                    dia.setCanceledOnTouchOutside(true); // 设置屏幕点击退出
                    Window w = dia.getWindow();
                    WindowManager.LayoutParams lp = w.getAttributes();
                    lp.x = 0;
                    dia.onWindowAttributesChanged(lp);
                }
            }
        });

        popupMenu.show();
    }
    private String houseName;
    private void buildUpdateHomeDialog() {
        final HomeDialog dialog = new HomeDialog(getActivity());
        dialog.setOnNegativeClickListener(new HomeDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
                dialog.dismiss();
            }
        });
        dialog.setOnPositiveClickListener(new HomeDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {
                roomName = dialog.getName();
                if (Utils.isEmpty(roomName)) {
                    Utils.showToast(getActivity(), "住所名称不能为空");
                } else {

                    for (int i=0;i<str1.size();i++){
                        if ("卧室".equals(str1.get(i))){

                            new RoomFragment.ChangeNameAsyncTask().execute();
                            textViewname.setText(roomName);
                            dialog.dismiss();
                        }
                    }

                }
            }
        });
        dialog.show();
    }
    class ChangeNameAsyncTask extends AsyncTask<Map<String, Object>, Void, Integer> {

        @Override
        protected Integer doInBackground(Map<String, Object>... maps) {
            int code = 0;

            String url = ip + "/family/room/changeRoomName?roomName=" + roomName + "&roomId=" + roomId;
            String result = HttpUtils.getOkHpptRequest(url);

            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("returnCode");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

    }



}
