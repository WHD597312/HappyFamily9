package com.xr.happyFamily.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.xr.database.dao.daoimpl.ClockDaoImpl;
import com.xr.database.dao.daoimpl.UserBeanDaoImpl;
import com.xr.database.dao.daoimpl.UserInfosDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.WuLiuActivity;
import com.xr.happyFamily.bao.adapter.ViewPagerAdapter;
import com.xr.happyFamily.bean.ShopBannerBean;
import com.xr.happyFamily.le.ClockActivity;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.UserBean;
import com.xr.happyFamily.le.pojo.UserInfo;
import com.xr.happyFamily.le.view.btClockjsDialog3;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.PublicData;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.le.view.noBirthdayDialog;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

public class LeFragment extends Fragment {

    int lastBanner = 0;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.img3)
    ImageView img3;
    @BindView(R.id.img4)
    ImageView img4;
    @BindView(R.id.lineLayout_dot)
    LinearLayout lineLayoutDot;
    @BindView(R.id.ll_xuyuan)
    LinearLayout llXuyuan;
    @BindView(R.id.ll_clock)
    LinearLayout llClock;
    @BindView(R.id.img5)
    ImageView img5;
    @BindView(R.id.img6)
    ImageView img6;
    @BindView(R.id.img7)
    ImageView img7;
    @BindView(R.id.img8)
    ImageView img8;
    @BindView(R.id.img9)
    ImageView img9;
    @BindView(R.id.img10)
    ImageView img10;


    private List<ImageView> mImageList;//轮播的图片集合
    private boolean isStop = false;//线程是否停止
    private static int PAGER_TIOME = 2500;//间隔时间
    // 在values文件假下创建了pager_image_ids.xml文件，并定义了4张轮播图对应的id，用于点击事件
    //轮播点
    private int[] imgae_dots = new int[]{R.id.img1, R.id.img2, R.id.img3, R.id.img4, R.id.img5, R.id.img6, R.id.img7, R.id.img8, R.id.img9, R.id.img10};
    View view;
    Unbinder unbinder;
    private MyDialog dialog;
    UserBean userBean;
    SharedPreferences preferences;
    String userId;
    private ClockDaoImpl clockBeanDao;
    private UserInfosDaoImpl userInfosDao;
    UserBeanDaoImpl userBeanDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_happy_mypage, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((ImageView) view.findViewById(imgae_dots[lastBanner])).setImageResource(R.mipmap.ic_shop_banner);
        shopBannerBeans = new ArrayList<>();
        isStop = false;
        dialog = MyDialog.showDialog(getActivity());
        dialog.show();

        new getAdByPageAsync().execute();

        clockBeanDao = new ClockDaoImpl(getActivity().getApplicationContext());
        userInfosDao = new UserInfosDaoImpl(getActivity().getApplicationContext());
        userBeanDao = new UserBeanDaoImpl(getActivity().getApplicationContext());
        userBeanDao.deleteAll();
        userBean = new UserBean();
        preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
        birthday = preferences.getString("birthday", "");
        userId = preferences.getString("userId", "");
        new getClocksByUserId().execute();
        return view;
    }

    String birthday;

    public void initData() {
        //初始化和图片

        //添加图片到图片列表里
        mImageList = new ArrayList<>();
        if (shopBannerBeans.size() == 1) {
            isStop = true;
        }
        ImageView iv;
        for (int i = 0; i < shopBannerBeans.size(); i++) {
            iv = new ImageView(getActivity());
//            iv.setBackgroundResource(imageRess[i]);//设置图片
            Picasso.with(getActivity()).load(shopBannerBeans.get(i).getImage()).into(iv);
//            iv.setId(imgae_ids[i]);//顺便给图片设置id
            mImageList.add(iv);
        }
    }


    /**
     * 第三步、给PagerViw设置适配器，并实现自动轮播功能
     */
    public void initView() {

        for (int i = 0; i < shopBannerBeans.size(); i++) {
            view.findViewById(imgae_dots[i]).setVisibility(View.VISIBLE);
        }
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity(), mImageList, viewPager, shopBannerBeans);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//伪无限循环，滑到最后一张图片又从新进入第一张图片
                int newPosition = position % mImageList.size();
                ImageView img_new = (ImageView) view.findViewById(imgae_dots[newPosition]);
                for (int i = 0; i < shopBannerBeans.size(); i++) {
                    if (i == newPosition) {
                        img_new.setImageResource(R.mipmap.ic_shop_banner);
                    } else {
                        ImageView img_old = (ImageView) view.findViewById(imgae_dots[i]);
                        img_old.setImageResource(R.mipmap.ic_shop_banner_wei);
                    }
                }
                lastBanner = position % shopBannerBeans.size();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    Thread thread;


    /**
     * 第五步: 设置自动播放,每隔PAGER_TIOME秒换一张图片
     */
    private void autoPlayView() {
        //自动播放图片
        if (thread == null) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!isStop) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                                }
                            });
                            SystemClock.sleep(PAGER_TIOME);
                        }

                    }
                }
            });
            thread.start();
        }
    }
    noBirthdayDialog birthdaydialog;
    private void clolkDialog1() {
        birthdaydialog = new noBirthdayDialog(getActivity());

        birthdaydialog.setOnNegativeClickListener(new noBirthdayDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
                birthdaydialog.dismiss();
            }
        });
        birthdaydialog.setOnPositiveClickListener(new noBirthdayDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {
                     
                birthdaydialog.dismiss();
            }
        });

        birthdaydialog.setCanceledOnTouchOutside(false);

        birthdaydialog.show();

    }

    @OnClick({R.id.ll_xuyuan, R.id.ll_clock})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_clock:
                if (birthday == null) {



                } else {


                    if ("Xiaomi".equals(Build.MANUFACTURER)) {//小米手机

                        requestPermission();
                    } else if ("Meizu".equals(Build.MANUFACTURER)) {//魅族手机

                        requestPermission();
                    } else {//其他手机

                        if (Build.VERSION.SDK_INT >= 23) {
                            if (!Settings.canDrawOverlays(getActivity())) {
                                Toast.makeText(getActivity(), "请允许app在最上层显示否则某些功能无法使用", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                startActivityForResult(intent, 12);
                            } else {
                                switchActivity();
                            }
                        } else {
                            switchActivity();
                        }
                    }
                }


                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
            if (isFloatWindowOpAllowed(getActivity())) {//已经开启
                switchActivity();
            } else {

//                Toast.makeText(getActivity(), "开启悬浮窗失败", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 12) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(getActivity())) {

//                    Toast.makeText(getActivity(), "权限授予失败,无法开启悬浮窗", Toast.LENGTH_SHORT).show();
                } else {
                    switchActivity();
                }
            }
        }

    }

    /**
     * 跳转Activity
     */
    private void switchActivity() {

        startActivity(new Intent(getActivity(), ClockActivity.class));


    }

    /**
     * 判断悬浮窗权限
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isFloatWindowOpAllowed(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            return checkOp(context, 24);  // AppOpsManager.OP_SYSTEM_ALERT_WINDOW
        } else {
            if ((context.getApplicationInfo().flags & 1 << 27) == 1 << 27) {
                return true;
            } else {
                return false;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;

        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class<?> spClazz = Class.forName(manager.getClass().getName());
                Method method = manager.getClass().getDeclaredMethod("checkOp", int.class, int.class, String.class);
                int property = (Integer) method.invoke(manager, op,
                        Binder.getCallingUid(), context.getPackageName());
                Log.e("399", " property: " + property);

                if (AppOpsManager.MODE_ALLOWED == property) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.e("399", "Below API 19 cannot invoke!");
        }
        return false;
    }

    /**
     * 请求用户给予悬浮窗的权限
     */
    public void requestPermission() {
        if (isFloatWindowOpAllowed(getActivity())) {//已经开启
            switchActivity();
        } else {
            Toast.makeText(getActivity(), "请允许app在最上层显示否则某些功能无法使用", Toast.LENGTH_SHORT).show();
            openSetting();
        }
    }

    /**
     * 打开权限设置界面
     */
    public void openSetting() {
        try {
            Intent localIntent = new Intent(
                    "miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter",
                    "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", getActivity().getPackageName());
            startActivityForResult(localIntent, 11);
            Toast.makeText(getActivity(), "启动小米悬浮窗设置界面", Toast.LENGTH_SHORT).show();

        } catch (ActivityNotFoundException localActivityNotFoundException) {
            Intent intent1 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
            intent1.setData(uri);
            startActivityForResult(intent1, 11);
            Toast.makeText(getActivity(), "启动悬浮窗界面", Toast.LENGTH_SHORT).show();
        }

    }


    List<ShopBannerBean> shopBannerBeans;

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    class getAdByPageAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            String url = "/ad/getAdByPage?pageNum=1&pageRow=10";

            String result = HttpUtils.doGet(getActivity(), url);

            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                    if (returnData.get("list").toString() != "null") {
                        JsonArray list = content.getAsJsonArray("list");
                        Gson gson = new Gson();
                        shopBannerBeans.clear();
                        for (int i = 0; i < list.size(); i++) {
//                        //通过反射 得到UserBean.class
                            JsonElement user = list.get(i);
                            ShopBannerBean userList = gson.fromJson(user, ShopBannerBean.class);
                            shopBannerBeans.add(userList);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
//
            return code;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!Utils.isEmpty(s) && "100".equals(s)) {
                MyDialog.closeDialog(dialog);
                initData();//初始化数据
                initView();//初始化View，设置适配器
                autoPlayView();//开启线程，自动播放


            }
        }
    }


    class getClocksByUserId extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {


            String url = "/happy/clock/getClocksByUserId";
            url = url + "?userId=" + userId;
            String result = HttpUtils.doGet(getActivity(), url);
            Log.e("qqqqqqqqRRR", userId + "?" + result);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!Utils.isEmpty(s) && "100".equals(s)) {

            }
        }
    }
}
