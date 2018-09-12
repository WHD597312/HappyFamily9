package com.xr.happyFamily.zhen;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.base.ToastUtil;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.BitmapCompressUtils;
import com.xr.happyFamily.together.util.GlideCircleTransform;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PersonInfoActivity extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.list_set) ListView list_set;
    private PersonAdapter adapter;
    MyApplication application;
    SharedPreferences preferences;
    private String upLoadImg=HttpUtils.ipAddress+"/user/headImg/";
    private String updatePerson=HttpUtils.ipAddress+"/user/updateUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (application==null){
            application= (MyApplication) getApplication();
            application.addActivity(this);
        }
        unbinder=ButterKnife.bind(this);
        preferences = getSharedPreferences("my", MODE_PRIVATE);

        adapter=new PersonAdapter(this);
        list_set.setAdapter(adapter);
    }

    private String username;
    @Override
    protected void onStart() {
        super.onStart();
        if (preferences.contains("username")){
            username=preferences.getString("username","");
        }

        list_set.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        if (popupWindow==null || !popupWindow.isShowing()){
                            showPopup();
                        }
                        break;
                    case 4:
                        if (popupWindow==null || !popupWindow.isShowing()){
                            updatePersonDialog();
                        }
                        break;
                    case 6:
                        if (popupWindow==null || !popupWindow.isShowing()){
                            popupSexView();
                        }
                        break;
                    case 8:

//                        if (popupWindow==null || !popupWindow.isShowing()){
//                            popupWindow2();
//                        }
                        try {
                            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                            Date date=format.parse("1970-1-1");
                            long minDay=date.getTime();
                            String birthday2=preferences.getString("birthday","");
                            if (!TextUtils.isEmpty(birthday2)){
                                Date date2=new Date(Long.parseLong(birthday2));
                                Calendar calendar=Calendar.getInstance();
                                calendar.setTime(date2);
                                long current=calendar.getTimeInMillis();
                                Date date3=new Date();
                                long maxTime=date3.getTime();
                                TimePickerDialog dialogYearMonthDay = new TimePickerDialog.Builder()
                                        .setType(Type.YEAR_MONTH_DAY)
                                        .setThemeColor(Color.parseColor("#4fc586"))
                                        .setTitleStringId("修改生日")
                                        .setMinMillseconds(minDay)
                                        .setCurrentMillseconds(current)
                                        .setMaxMillseconds(maxTime)
                                        .setCallBack(new OnDateSetListener() {
                                            @Override
                                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                                Map<String, Object> params = new HashMap<>();
                                                String userId = preferences.getString("userId", "");
                                                boolean sex2 = preferences.getBoolean("sex", false);
                                                Date date=new Date(millseconds);
                                                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                                                birthday=format.format(date);
                                                int flag = 0;
                                                if (sex2) {
                                                    flag = 1;
                                                } else {
                                                    flag = 0;
                                                }
                                                sex=-1;
                                                params.put("userId", userId);
                                                params.put("sex", flag);
                                                params.put("username", username);
                                                params.put("birthday", birthday);
                                                new UpdatePersonAsync().execute(params);
                                            }
                                        })
                                        .build();
                                dialogYearMonthDay.show(getSupportFragmentManager(), "YEAR_MONTH_DAY");
                            }else {
                                Date date2=new Date();
                                Calendar calendar=Calendar.getInstance();
                                long current=calendar.getTimeInMillis();
                                long maxTime=date2.getTime();
                                TimePickerDialog dialogYearMonthDay = new TimePickerDialog.Builder()
                                        .setType(Type.YEAR_MONTH_DAY)
                                        .setThemeColor(Color.parseColor("#4fc586"))
                                        .setTitleStringId("修改生日")
                                        .setMinMillseconds(minDay)
                                        .setCurrentMillseconds(current)
                                        .setMaxMillseconds(maxTime)
                                        .setCallBack(new OnDateSetListener() {
                                            @Override
                                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                                Map<String, Object> params = new HashMap<>();
                                                String userId = preferences.getString("userId", "");
                                                boolean sex2 = preferences.getBoolean("sex", false);
                                                Date date=new Date(millseconds);
                                                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                                                birthday=format.format(date);
                                                int flag = 0;
                                                if (sex2) {
                                                    flag = 1;
                                                } else {
                                                    flag = 0;
                                                }
                                                sex=-1;
                                                params.put("userId", userId);
                                                params.put("sex", flag);
                                                params.put("username", username);
                                                params.put("birthday", birthday);
                                                new UpdatePersonAsync().execute(params);
                                            }
                                        })
                                        .build();
                                dialogYearMonthDay.show(getSupportFragmentManager(), "YEAR_MONTH_DAY");
                            }

                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                        break;
                }
            }
        });
    }

    private void updatePersonDialog() {
        final PersonDialog dialog = new PersonDialog(this);
        dialog.setOnNegativeClickListener(new PersonDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
                dialog.dismiss();
            }
        });
        dialog.setOnPositiveClickListener(new PersonDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {
                username=dialog.getName();
                if (TextUtils.isEmpty(username)){
                    Toast.makeText(PersonInfoActivity.this,"用户名称不能为空",Toast.LENGTH_SHORT).show();
                }else {

                    Map<String,Object> params=new HashMap<>();
                    String userId=preferences.getString("userId","");
                    boolean sex2=preferences.getBoolean("sex",false);
                    String birthday2=preferences.getString("birthday","");
                    Date date =new Date(Long.parseLong(birthday2));
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                    String time=sdf.format(date);//将Date对象转化为yyyy-MM-dd形式的字符串
                    int flag=0;
                    if (sex2){
                        flag=1;
                    }else {
                        flag=0;
                    }
                    sex=-1;
                    params.put("userId",userId);
                    params.put("sex",flag);
                    params.put("username",username);
                    params.put("birthday",time);
                    new UpdatePersonAsync().execute(params);

                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @OnClick({R.id.back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                if (popupWindow!=null && popupWindow.isShowing()){
                    backgroundAlpha(1.0f);
                    popupWindow.dismiss();
                    break;
                }
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (popupWindow!=null && popupWindow.isShowing()){
                backgroundAlpha(1.0f);
                popupWindow.dismiss();
                return false;
            }
            application.removeActivity(this);
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
    int year=0;
    int month;
    int day=0;
    int hour=0;
    String birthday;
    //底部popupWindow
    public void popupWindow2() {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        View view = View.inflate(this, R.layout.popup_birthday, null);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        DatePicker datePicker= (DatePicker) view.findViewById(R.id.datePicker);
        Button btn_cancle= (Button) view.findViewById(R.id.btn_cancle);
        Button btn_ensure= (Button) view.findViewById(R.id.btn_ensure);

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        backgroundAlpha(0.4f);
        //添加弹出、弹入的动画
        popupWindow.setAnimationStyle(R.style.Popupwindow);

        ColorDrawable dw = new ColorDrawable(0x30000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAtLocation(list_set, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //添加按键事件监听

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_cancle:
                            backgroundAlpha(1.0f);
                            popupWindow.dismiss();
                        break;
                    case R.id.btn_ensure:
                            Map<String, Object> params = new HashMap<>();
                            String userId = preferences.getString("userId", "");
                            boolean sex2 = preferences.getBoolean("sex", false);
                            birthday = year + "-" + month + "-" + day;

                            int flag = 0;
                            if (sex2) {
                                flag = 1;
                            } else {
                                flag = 0;
                            }
                            sex=-1;
                            params.put("userId", userId);
                            params.put("sex", flag);
                            params.put("username", username);
                            params.put("birthday", birthday);
                            new UpdatePersonAsync().execute(params);
                            backgroundAlpha(1.0f);
                            popupWindow.dismiss();
                        break;
                }
            }
        };

        String birthday=preferences.getString("birthday","");
        Calendar calendar= Calendar.getInstance();
        if (!TextUtils.isEmpty(birthday)){
            try {
                Date date=new Date(Long.parseLong(birthday));
                calendar.setTime(date);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        hour=calendar.get(Calendar.HOUR_OF_DAY);
        datePicker.setMaxDate(new Date().getTime());

        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int mYear, int monthOfYear, int dayOfMonth) {
                if (mYear>year){
                    mYear=year;
                }
                year=mYear;
                month=monthOfYear+1;
                day=dayOfMonth;
            }
        });
        month=month+1;


        btn_cancle.setOnClickListener(listener);
        btn_ensure.setOnClickListener(listener);
    }
    class PersonAdapter extends BaseAdapter{

        String strs[]={"头像","手机号","昵称","性别","生日","修改密码"};
        private Context context;
        public PersonAdapter(Context context){
            this.context=context;
        }
        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            ViewHolder2 viewHolder2=null;
            ViewHolder3 viewHolder3=null;
            ViewHolder4 viewHolder4=null;
            switch (position){
                case 0:
                    convertView=View.inflate(context,R.layout.person_header,null);
                    viewHolder=new ViewHolder(convertView);
                    String image=preferences.getString("image","");
                    if (!Utils.isEmpty(image)){
                        File file=new File(image);
                        Glide.with(PersonInfoActivity.this).load(file).transform(new GlideCircleTransform(getApplicationContext())).into(viewHolder.image_user);
                    }
                    break;
                case 1:
                    convertView=View.inflate(context,R.layout.view3,null);
                    convertView.setMinimumHeight(3);
                    break;
                case 2:
                    convertView=View.inflate(context,R.layout.person_common,null);
                    viewHolder2=new ViewHolder2(convertView);
                    viewHolder2.tv_left.setText(strs[1]);
                    String phone=preferences.getString("phone","");
                    viewHolder2.tv_right.setText(phone);
                    break;
                case 3:
                    convertView=View.inflate(context,R.layout.view3,null);
                    convertView.setMinimumHeight(3);
                    break;
                case 4:
                    convertView=View.inflate(context,R.layout.person_name,null);
                    viewHolder3=new ViewHolder3(convertView);
                    if (TextUtils.isEmpty(username)){
                        viewHolder3.tv_name.setText("aamin");
                    }else {
                        viewHolder3.tv_name.setText(username);
                    }

                    break;
                case 5:
                    convertView=View.inflate(context,R.layout.view3,null);
                    convertView.setMinimumHeight(3);
                    break;
                case 6:
                    convertView=View.inflate(context,R.layout.person_name,null);
                    viewHolder3=new ViewHolder3(convertView);
                    viewHolder3.tv_head.setText(strs[3]);
                    boolean sex=preferences.getBoolean("sex",false);
                    if (sex){
                        viewHolder3.tv_name.setText("男");
                    }else {
                        viewHolder3.tv_name.setText("女");
                    }
                    break;
                case 7:
                    convertView=View.inflate(context,R.layout.view3,null);
                    convertView.setMinimumHeight(3);
                    break;
                case 8:
                    convertView=View.inflate(context,R.layout.person_birth,null);
                    viewHolder4=new ViewHolder4(convertView);
                    viewHolder4.tv_birth.setText(strs[4]);

                    String birthday=preferences.getString("birthday","");
                    if (!TextUtils.isEmpty(birthday)){
                        Date date =new Date(Long.parseLong(birthday));
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");

                        //设置转化格式
                        String time=sdf.format(date);//将Date对象转化为yyyy-MM-dd形式的字符串
                        viewHolder4.tv_birthday.setText(time);
                    }else {
                        viewHolder4.tv_birthday.setText("");
                        viewHolder4.tv_birthday.setHint("请完善你的生日信息");
                    }
                    break;
            }
            return convertView;
        }
        class ViewHolder{
            @BindView(R.id.image_user) ImageView image_user;
            public ViewHolder(View view){
                ButterKnife.bind(this,view);
            }
        }
        class ViewHolder2{
            @BindView(R.id.tv_left) TextView tv_left;
            @BindView(R.id.tv_right) TextView tv_right;
            public ViewHolder2(View view){
                ButterKnife.bind(this,view);
            }
        }
        class ViewHolder3{
            @BindView(R.id.tv_head) TextView tv_head;
            @BindView(R.id.tv_name) TextView tv_name;
            public ViewHolder3(View view){
                ButterKnife.bind(this,view);
            }
        }
        class ViewHolder4{
            @BindView(R.id.tv_birth) TextView tv_birth;
            @BindView(R.id.tv_birthday) TextView tv_birthday;
            public ViewHolder4(View view){
                ButterKnife.bind(this,view);
            }
        }
    }


    //设置蒙版
    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp =getWindow().getAttributes();
        lp.alpha = f;
        getWindow().setAttributes(lp);
    }




    private PopupWindow popupWindow;

    //底部popupWindow
    public void popupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        View view = View.inflate(this, R.layout.changepicture, null);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        //添加弹出、弹入的动画
        popupWindow.setAnimationStyle(R.style.Popupwindow);
        backgroundAlpha(0.4f);
        ColorDrawable dw = new ColorDrawable(0x30000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAtLocation(list_set, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //添加按键事件监听
        setButtonListeners(view);
    }

    private void setButtonListeners(View layout) {
        Button camera = (Button) layout.findViewById(R.id.camera);
        Button gallery = (Button) layout.findViewById(R.id.gallery);
        TextView cancel = (TextView) layout.findViewById(R.id.cancel);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    //在此处添加你的按键处理 xxx
                    if (Build.VERSION.SDK_INT >= 23) {
                        //android 6.0权限问题
                        if (ContextCompat.checkSelfPermission(PersonInfoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(PersonInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(PersonInfoActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERAPRESS);
                        } else {
                            startCamera();
                        }
                    } else {
                        startCamera();
                    }
                }
                backgroundAlpha(1.0f);
                popupWindow.dismiss();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        //android 6.0权限问题
                        if (ContextCompat.checkSelfPermission(PersonInfoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(PersonInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(PersonInfoActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, ICONPRESS);
                        } else {
                            startGallery();
                        }

                    } else {
                        startGallery();
                    }
                }
                backgroundAlpha(1.0f);
                popupWindow.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    backgroundAlpha(1.0f);
                    popupWindow.dismiss();
                }
            }
        });
    }

    final static int CAMERA = 1;//拍照
    final static int ICON = 2;//相册
    final static int CAMERAPRESS = 3;//拍照权限
    final static int ICONPRESS = 4;//相册权限
    final static int PICTURE_CUT = 5;//剪切图片
    private static final String TAG = "RoomContentActivity";
    private Uri outputUri;//裁剪完照片保存地址
    Uri imageUri; //图片路径
    File imageFile; //图片文件
    String imagePath;
    private boolean isClickCamera;//是否是拍照裁剪

    //拍照
    public void startCamera() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        imageFile = new File(getExternalCacheDir(), "background3.png");
        backgroundAlpha(1.0f);
        try {
            if (imageFile.exists()) {
                imageFile.delete();
            }
            imageFile.createNewFile();
        } catch (IOException e) {

            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24) {
            imageUri = Uri.fromFile(imageFile);
        } else {
            //Android 7.0系统开始 使用本地真实的Uri路径不安全,使用FileProvider封装共享Uri
            //参数二:fileprovider绝对路径 com.dyb.testcamerademo：项目包名

            imageUri = FileProvider.getUriForFile(PersonInfoActivity.this, "com.hm.camerademo.fileprovider2", imageFile);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //照相
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); //指定图片输出地址
        startActivityForResult(intent, CAMERA); //启动照相
    }

    //打开相册
    public void startGallery() {
        backgroundAlpha(1.0f);
        Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        startActivityForResult(intent1, ICON);
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        // 创建File对象，用于存储裁剪后的图片，避免更改原图
        File file = new File(getExternalCacheDir(), "crop_image2.jpg");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        outputUri = Uri.fromFile(file);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        //裁剪图片的宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("crop", "true");//可裁剪
        // 裁剪后输出图片的尺寸大小
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
        intent.putExtra("scale", true);//支持缩放
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出图片格式
        intent.putExtra("noFaceDetection", true);//取消人脸识别
        startActivityForResult(intent, PICTURE_CUT);
    }

    // 4.4及以上系统使用这个方法处理图片 相册图片返回的不再是真实的Uri,而是分装过的Uri
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        cropPhoto(uri);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        imagePath = getImagePath(uri, null);
        cropPhoto(uri);
    }

    File file2;

    private void upImage(File file) {

        if (file != null) {
            new LoadUserInfo().execute(file);
            file2 = file;
        }
    }

    class LoadUserInfo extends AsyncTask<File, Void, Integer> {

        @Override
        protected Integer doInBackground(File... files) {
            int code = 0;
            File file = files[0];
            String userId = preferences.getString("userId", "");
            String url =upLoadImg+userId;
            String result = HttpUtils.upLoadFile(url, "HeadPortrait2.jpg", file);
            if (!Utils.isEmpty(result)) {
                code = Integer.parseInt(result);
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            switch (code) {
                case 200:
                    Utils.showToast(PersonInfoActivity.this, "上传成功");
                    String image=preferences.getString("image","");
                    if (!Utils.isEmpty(image)){
                        File file=new File(image);
                        if (file.exists()){
                            file.delete();
                        }
                    }
                    preferences.edit().putString("image",file2.getPath()).commit();
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    Utils.showToast(PersonInfoActivity.this, "上传失败");
                    break;
            }

        }
    }

    class UpdatePersonAsync extends AsyncTask<Map<String,Object>,Void,Integer>{

        @Override
        protected Integer doInBackground(Map<String, Object>... maps) {
            int code=0;
            Map<String,Object> params=maps[0];
            String result=HttpUtils.postOkHpptRequest(updatePerson,params);
            try {
                if (!TextUtils.isEmpty(result)){
                    JSONObject jsonObject=new JSONObject(result);
                    String returnCode=jsonObject.getString("returnCode");
                    if ("100".equals(returnCode)){
                        code=100;
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("username",username);
                        editor.commit();
                        if (sex!=-1){
                            if (sex==1){
                                editor.putBoolean("sex",true);
                            }else if (sex==0){
                                editor.putBoolean("sex",false);
                            }
                            editor.commit();
                        }else {
                            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                            Date date=format.parse(birthday);
                            long ts = date.getTime();
                            String res = String.valueOf(ts);
                            editor.putString("birthday",res);
                            editor.commit();
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            switch (code){
                case 100:
                    Toast.makeText(PersonInfoActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    break;
                    default:
                        break;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        File file;

        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult", "requestCode" + requestCode + "resultCode" + resultCode);
        switch (requestCode) {
            case CAMERA:
                if (resultCode == RESULT_OK) {
                    cropPhoto(imageUri);
                }
                break;
            case ICON:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case PICTURE_CUT://裁剪完成
                isClickCamera = true;
                Bitmap bitmap2 = null;
                try {
                    if (isClickCamera) {
                        bitmap2 = BitmapFactory.decodeStream(getContentResolver().openInputStream(outputUri));
                    } else {
                        bitmap2 = BitmapFactory.decodeFile(imagePath);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (bitmap2 == null) {
                    break;
                }
                File file2 = BitmapCompressUtils.compressImage(bitmap2);
                upImage(file2);
                break;
        }
    }

    int sex=-1;
    public void popupSexView(){
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        View view = View.inflate(this, R.layout.popview_sex, null);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        TextView image_cancle= (TextView) view.findViewById(R.id.cancel);

        Button boy= (Button) view.findViewById(R.id.camera);
        Button girl= (Button) view.findViewById(R.id.gallery);


        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);

        backgroundAlpha2(0.5f);

        popupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        //添加按键事件监听

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.cancel:
                        backgroundAlpha(1.0f);
                        popupWindow.dismiss();
                        break;
                    case R.id.camera:
                        backgroundAlpha(1.0f);
                        Map<String,Object> params=new HashMap<>();
                        String userId=preferences.getString("userId","");
                        sex=1;
                        birthday=preferences.getString("birthday","");
                        Date date =new Date(Long.parseLong(birthday));
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                        String time=sdf.format(date);//将Date对象转化为yyyy-MM-dd形式的字符串

                        params.put("userId",userId);
                        params.put("sex",1);
                        params.put("username",username);
                        params.put("birthday",time);
                        new UpdatePersonAsync().execute(params);
                        popupWindow.dismiss();
                        break;
                    case R.id.gallery:
                        backgroundAlpha(1.0f);
                        Map<String,Object> params2=new HashMap<>();
                        String userId2=preferences.getString("userId","");
                        birthday=preferences.getString("birthday","");
                        Date date2 =new Date(Long.parseLong(birthday));
                        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
                        String time2=sdf2.format(date2);//将Date对象转化为yyyy-MM-dd形式的字符串
                        sex=0;
                        params2.put("userId",userId2);
                        params2.put("sex",0);
                        params2.put("username",username);
                        params2.put("birthday",time2);
                        new UpdatePersonAsync().execute(params2);
                        popupWindow.dismiss();
                        break;

                }
            }
        };
        image_cancle.setOnClickListener(listener);
        boy.setOnClickListener(listener);
        girl.setOnClickListener(listener);
    }


    private View contentViewSign;
    private Context mContext;
    private Button camera, gallery;
    TextView cancel;
    boolean isPopup=false;
    private void showPopup() {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        contentViewSign = LayoutInflater.from(PersonInfoActivity.this).inflate(R.layout.changepicture, null);
        popupWindow = new PopupWindow(contentViewSign);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(false);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);
        backgroundAlpha2(0.5f);
        //添加pop窗口关闭事件
        popupWindow.setOnDismissListener(new poponDismissListener());
        popupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        camera = (Button) contentViewSign.findViewById(R.id.camera);
        gallery = (Button) contentViewSign.findViewById(R.id.gallery);
        cancel = (TextView) contentViewSign.findViewById(R.id.cancel);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    //在此处添加你的按键处理 xxx
                    if (Build.VERSION.SDK_INT >= 23) {
                        //android 6.0权限问题
                        if (ContextCompat.checkSelfPermission(PersonInfoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(PersonInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(PersonInfoActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERAPRESS);
                        } else {
                            startCamera();
                        }
                    } else {
                        startCamera();
                    }
                }
                backgroundAlpha(1.0f);
                popupWindow.dismiss();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        //android 6.0权限问题
                        if (ContextCompat.checkSelfPermission(PersonInfoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(PersonInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(PersonInfoActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, ICONPRESS);
                        } else {
                            startGallery();
                        }

                    } else {
                        startGallery();
                    }
                }
                backgroundAlpha(1.0f);
                popupWindow.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    isPopup=false;
                    backgroundAlpha(1.0f);
                    popupWindow.dismiss();
                }
            }
        });
    }


    public void backgroundAlpha2(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp); //添加pop窗口关闭事件
    }




    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }

    }
}
