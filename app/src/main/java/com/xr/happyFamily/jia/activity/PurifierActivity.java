package com.xr.happyFamily.jia.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.view.DoubleWaveView;
import com.xr.happyFamily.jia.LiveActivity;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.view_custom.HomeDialog;
import com.xr.happyFamily.jia.view_custom.Themometer;
import com.xr.happyFamily.jia.view_custom.VerticalProgressBar;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.GlideCircleTransform;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PurifierActivity extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.doubleW) DoubleWaveView doubleWaveView;
//    @BindView(R.id.themometer) Themometer themometer;
    @BindView(R.id.vp_progress) VerticalProgressBar vp_progress;
    @BindView(R.id.tv_title) TextView tv_title;/**设备名称*/
    @BindView(R.id.image_more) ImageView image_more;
    private MyApplication application;
    private DeviceChildDaoImpl deviceChildDao;
    private DeviceChild deviceChild;
    private String deviceName;
    private String updateDeviceNameUrl= HttpUtils.ipAddress+"/family/device/changeDeviceName";
    private long houseId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_purifier);
        if (application==null){
            application= (MyApplication) getApplication();
            application.addActivity(this);
        }
        unbinder=ButterKnife.bind(this);
        deviceChildDao=new DeviceChildDaoImpl(getApplicationContext());
        Intent intent=getIntent();
        long id=intent.getLongExtra("deviceId",0);
        houseId=intent.getLongExtra("houseId",0);
        deviceChild=deviceChildDao.findById(id);
        if (deviceChild!=null){
            deviceName=deviceChild.getName();
            tv_title.setText(deviceName);
        }
        doubleWaveView.setProHeight(30);
//        themometer.setTemperature(80);
        vp_progress.setProgress(30);

    }
    @OnClick({R.id.image_back,R.id.layout_wt_record,R.id.layout_living,R.id.image_more})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.layout_wt_record:
                Intent wtRecord=new Intent(this,UseWaterRecordActivity.class);
                startActivityForResult(wtRecord,8000);
                break;
            case R.id.layout_living:
                Intent live=new Intent(this, LiveActivity.class);
                startActivityForResult(live,8000);
                break;
            case R.id.image_back:
                Intent intent=new Intent();
                intent.putExtra("houseId",houseId);
                setResult(6000,intent);
                finish();
                break;
            case R.id.image_more:
               popupmenuWindow();
                break;
        }
    }

    private PopupWindow popupWindow1;
    public void popupmenuWindow() {
        if (popupWindow1 != null && popupWindow1.isShowing()) {
            return;
        }

        View view = View.inflate(this, R.layout.popview_room_homemanerge, null);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        RelativeLayout rl_room_rename = (RelativeLayout) view.findViewById(R.id.rl_room_rename);
        RelativeLayout tv_timer = (RelativeLayout) view.findViewById(R.id.rl_room_del);
        TextView tv_rname_r1 = (TextView) view.findViewById(R.id.tv_rname_r1);
        TextView tv_del_r1 = (TextView) view.findViewById(R.id.tv_del_r1);
        ImageView iv_del_r1 = (ImageView) view.findViewById(R.id.iv_del_r1);
        tv_rname_r1.setText("修改名称");
        tv_del_r1.setText("分享设备");
        iv_del_r1.setImageResource(R.mipmap.pop_share);
        popupWindow1 = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //点击空白处时，隐藏掉pop窗口
        popupWindow1.setFocusable(true);
        popupWindow1.setOutsideTouchable(true);
        //添加弹出、弹入的动画
        popupWindow1.setAnimationStyle(R.style.Popupwindow);

//        ColorDrawable dw = new ColorDrawable(0x30000000);
//        popupWindow.setBackgroundDrawable(dw);
        popupWindow1.showAsDropDown(image_more, 0, -20);
//        popupWindow.showAtLocation(tv_home_manager, Gravity.RIGHT, 0, 0);
        //添加按键事件监听

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl_room_rename:
                        buildUpdateDeviceDialog();
                        popupWindow1.dismiss();
                        break;
                    case R.id.rl_room_del:
                        Intent intent=new Intent(PurifierActivity.this,ShareDeviceActivity.class);
                        long id=deviceChild.getId();
                        intent.putExtra("deviceId",id);
                        startActivity(intent);
                        popupWindow1.dismiss();
                        break;
                }
            }
        };

        rl_room_rename.setOnClickListener(listener);
        tv_timer.setOnClickListener(listener);
    }

    private void buildUpdateDeviceDialog() {
        final HomeDialog dialog = new HomeDialog(this);
        dialog.setOnNegativeClickListener(new HomeDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
                dialog.dismiss();
            }
        });
        dialog.setOnPositiveClickListener(new HomeDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {
                deviceName = dialog.getName();
                if (TextUtils.isEmpty(deviceName)) {
                    Utils.showToast(PurifierActivity.this, "设备名称不能为空");
                } else {
                    new UpdateDeviceAsync().execute();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    class UpdateDeviceAsync extends AsyncTask<Void,Void,Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            int code=0;
            try {
                int deviceId=deviceChild.getDeviceId();
                String url=updateDeviceNameUrl+"?deviceName="+ URLEncoder.encode(deviceName,"utf-8")+"&deviceId="+deviceId;
                String result= HttpUtils.getOkHpptRequest(url);
                JSONObject jsonObject=new JSONObject(result);
                String returnCode=jsonObject.getString("returnCode");
                if ("100".equals(returnCode)){
                    code=100;
                    deviceChild.setName(deviceName);
                    deviceChildDao.update(deviceChild);
                }
                Log.i("result","-->"+result);
            }catch (Exception e){
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            try {
                switch (code){
                    case 100:
                        Utils.showToast(PurifierActivity.this, "修改成功");
                        tv_title.setText(deviceName);
                        break;
                    default:
                        Utils.showToast(PurifierActivity.this, "修改失败");
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.ACTION_DOWN){
            Intent intent=new Intent();
            intent.putExtra("houseId",houseId);
            setResult(6000,intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
