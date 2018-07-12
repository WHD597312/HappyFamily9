package com.xr.happyFamily.jia.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.share.PlatformUtil;
import com.xr.happyFamily.together.util.BitmapCompressUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ShareDeviceActivity extends AppCompatActivity {

    Unbinder unbinder;
    private DeviceChild deviceChild;
    private SharedPreferences preferences;
    private String createQRCode = HttpUtils.ipAddress + "/family/device/createQRCode";
    @BindView(R.id.img_qrCode)
    ImageView img_qrCode;
    /**
     * 二维码
     */
    @BindView(R.id.tv_device)
    TextView tv_device;
    /**
     * 设备名称
     */
    @BindView(R.id.image_more)
    ImageView image_more;
    /**
     * 分享图片
     */
    private MyApplication application;
    private int deviceId;
    private DeviceChildDaoImpl deviceChildDao;
    File file;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_device);
        unbinder = ButterKnife.bind(this);
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        if (application == null) {
            application = (MyApplication) getApplication();
            application.addActivity(this);
        }
        deviceChildDao = new DeviceChildDaoImpl(getApplicationContext());
        Intent intent = getIntent();
        long id = intent.getLongExtra("deviceId", 0);
        deviceChild = deviceChildDao.findById(id);
        if (deviceChild != null) {
            String name = deviceChild.getName();
            tv_device.setText(name);
        }
        new ShareQrCodeAsync().execute();
    }

    @OnClick({R.id.back, R.id.image_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.image_more:
                popupShare();
                break;
        }
    }

    private PopupWindow popupWindow;

    private void popupShare() {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        View view = View.inflate(this, R.layout.popview_share_device, null);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        TextView tv_save_phone = (TextView) view.findViewById(R.id.tv_save_phone);
        TextView tv_send_wechat = (TextView) view.findViewById(R.id.tv_send_wechat);
        TextView tv_send_qq = (TextView) view.findViewById(R.id.tv_send_qq);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);


        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //添加弹出、弹入的动画
        popupWindow.setAnimationStyle(R.style.Popupwindow);

        ColorDrawable dw = new ColorDrawable(0x30000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAtLocation(img_qrCode, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //添加按键事件监听

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_save_phone:
                        break;
                    case R.id.tv_send_wechat:
                        if (mBitmap!=null){
                            PlatformUtil.shareWechatFriend(getApplication(), mBitmap);
                        }
                        break;
                    case R.id.tv_send_qq:
                        if (mBitmap!=null){
                            PlatformUtil.shareImageToQQ(getApplication(),mBitmap);
                        }
                        break;
                    case R.id.tv_cancel:
                        popupWindow.dismiss();
                        break;

                }
            }
        };

        tv_save_phone.setOnClickListener(listener);
        tv_send_wechat.setOnClickListener(listener);
        tv_send_qq.setOnClickListener(listener);
        tv_cancel.setOnClickListener(listener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            application.removeActivity(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (popupWindow!=null){
            popupWindow.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (popupWindow!=null){
            popupWindow.dismiss();
        }
        if (mBitmap!=null){
            BitmapCompressUtils.recycleBitmap(mBitmap);
        }
    }

    class ShareQrCodeAsync extends AsyncTask<Void, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Void... maps) {
            Bitmap bitmap = null;
            if (deviceChild != null) {
                long deviceId = deviceChild.getDeviceId();
                String token = preferences.getString("token", "token");
                String userId = preferences.getString("userId", "");
                String url = createQRCode + "?deviceId=" + deviceId + "&userId=" + userId;
                try {
                    GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder().addHeader("authorization", token).build());
                    bitmap = Glide.with(ShareDeviceActivity.this)
                            .load(glideUrl)
                            .asBitmap()
                            .centerCrop()
                            .into(180, 180)
                            .get();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                img_qrCode.setImageBitmap(bitmap);
                mBitmap=bitmap;
            }
        }
    }

    private void onSaveBitmap(Bitmap mBitmap) {
        // 第一步：首先保存图片
        //将Bitmap保存图片到指定的路径/sdcard/Boohee/下，文件名以当前系统时间命名,但是这种方法保存的图片没有加入到系统图库中
        File appDir = new File(getExternalCacheDir(), "p99");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        // 第二步：其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
////   /storage/emulated/0/Boohee/1493711988333.jpg
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        // 第三步：最后通知图库更新
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
        //context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }
}
