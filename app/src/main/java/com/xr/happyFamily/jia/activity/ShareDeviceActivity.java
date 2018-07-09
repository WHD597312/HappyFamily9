package com.xr.happyFamily.jia.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.together.http.HttpUtils;

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
    private String createQRCode= HttpUtils.ipAddress+"/family/device/createQRCode";
    @BindView(R.id.img_qrCode) ImageView img_qrCode;/**二维码*/
    private MyApplication application;
    private int deviceId;
    private DeviceChildDaoImpl deviceChildDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_device);
        unbinder=ButterKnife.bind(this);
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        if (application==null){
            application= (MyApplication) getApplication();
            application.addActivity(this);
        }
        deviceChildDao=new DeviceChildDaoImpl(getApplicationContext());
        Intent intent=getIntent();
        long id=intent.getLongExtra("deviceId",0);
        deviceChild=deviceChildDao.findById(id);
        new ShareQrCodeAsync().execute();
    }
    @OnClick({R.id.back,R.id.image_more})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.image_more:
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            application.removeActivity(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class ShareQrCodeAsync extends AsyncTask<Void,Void,Bitmap> {
        @Override
        protected Bitmap doInBackground(Void... maps) {
            Bitmap bitmap=null;
            if (deviceChild!=null){
                long deviceId=deviceChild.getDeviceId();
                String token = preferences.getString("token", "token");
                String userId=preferences.getString("userId","");
                String url=createQRCode+"?deviceId="+deviceId+"&userId="+userId;
                try {
                    GlideUrl glideUrl=new GlideUrl(url,new LazyHeaders.Builder().addHeader("authorization",token).build());
                    bitmap= Glide.with(ShareDeviceActivity.this)
                            .load(glideUrl)
                            .asBitmap()
                            .centerCrop()
                            .into(180,180)
                            .get();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap!=null){
                img_qrCode.setImageBitmap(bitmap);
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
