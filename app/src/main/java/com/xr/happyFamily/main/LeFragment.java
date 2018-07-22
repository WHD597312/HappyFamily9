package com.xr.happyFamily.main;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.xr.happyFamily.le.ClockActivity;
import com.xr.happyFamily.le.adapter.HappyFootAdapter;
import com.xr.happyFamily.le.adapter.HappyViewPagerAdapter;
import com.xr.happyFamily.le.bean.HappyBannerBean;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.UserBean;
import com.xr.happyFamily.le.pojo.UserInfo;
import com.xr.happyFamily.le.view.noBirthdayDialog;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
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

    public static final int MAIN_CODE = 1000;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
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

    UserBeanDaoImpl userBeanDao;
    int page = 1;
    HappyFootAdapter happyFootAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_happy_mypage, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((ImageView) view.findViewById(imgae_dots[lastBanner])).setImageResource(R.mipmap.ic_shop_banner);
        happyBannerBeans = new ArrayList<>();
        footBean=new ArrayList<>();
        isStop = false;
        dialog = MyDialog.showDialog(getActivity());
        dialog.show();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        happyFootAdapter = new HappyFootAdapter(getActivity(), footBean);
        recyclerView.setAdapter(happyFootAdapter);
        isFinish = false;
        page=1;
        getBanner("head", page);
        getBanner("foot", 1);

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
        if (happyBannerBeans.size() == 1) {
            isStop = true;
        }
        ImageView iv;
        for (int i = 0; i < happyBannerBeans.size(); i++) {
            iv = new ImageView(getActivity());
//            iv.setBackgroundResource(imageRess[i]);//设置图片
            Picasso.with(getActivity()).load(happyBannerBeans.get(i).getPicUrl()).into(iv);
//            iv.setId(imgae_ids[i]);//顺便给图片设置id
            mImageList.add(iv);
        }
    }


    /**
     * 第三步、给PagerViw设置适配器，并实现自动轮播功能
     */
    public void initView() {
        Log.e("qqqqqqqSSSS", happyBannerBeans.size() + "??");
        for (int i = 0; i < happyBannerBeans.size(); i++) {
            view.findViewById(imgae_dots[i]).setVisibility(View.VISIBLE);
        }
        HappyViewPagerAdapter viewPagerAdapter = new HappyViewPagerAdapter(getActivity(), mImageList, viewPager, happyBannerBeans);
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
                for (int i = 0; i < happyBannerBeans.size(); i++) {
                    if (i == newPosition) {
                        img_new.setImageResource(R.mipmap.ic_shop_banner);
                    } else {
                        ImageView img_old = (ImageView) view.findViewById(imgae_dots[i]);
                        img_old.setImageResource(R.mipmap.ic_shop_banner_wei);
                    }
                }
                lastBanner = position % happyBannerBeans.size();
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


//
//                if (birthday == null) {
//
//
//
//                } else {
//
//
//                    if ("Xiaomi".equals(Build.MANUFACTURER)) {//小米手机
//
//                        requestPermission();
//                    } else if ("Meizu".equals(Build.MANUFACTURER)) {//魅族手机
//
//                        requestPermission();
//                    } else {//其他手机
//
//                        if (Build.VERSION.SDK_INT >= 23) {
//                            if (!Settings.canDrawOverlays(getActivity())) {
//                                Toast.makeText(getActivity(), "请允许app在最上层显示否则某些功能无法使用", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                                startActivityForResult(intent, 12);
//                            } else {
                switchActivity();
//                            }
//                        } else {
//                            switchActivity();
//                        }
//                    }
//                }
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

                Toast.makeText(getActivity(), "开启悬浮窗失败", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 12) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(getActivity())) {

                    Toast.makeText(getActivity(), "权限授予失败,无法开启悬浮窗", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(getActivity(), ClockActivity.class);
        intent.putExtra("type", "wenchao");
        startActivityForResult(intent, MAIN_CODE);
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


    List<HappyBannerBean> happyBannerBeans;
    List<HappyBannerBean> footBean;

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

    boolean isFinish = false;

    class getAdByPageAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "/happy/happy/getHappy";
            String site = params.get("site").toString();
            String page = params.get("page").toString();
            url = url + "?site=" + site + "&page=" + page;
            String result = HttpUtils.doGet(getActivity(), url);
            String code = "";
            Log.e("qqqqqqqqMMM", url);

            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    }
                    Log.e("qqqqqqqqMMM22", result);
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JsonObject content = new JsonParser().parse(jsonObject.toString()).getAsJsonObject();
                    if (jsonObject.get("returnCode").toString() != "null") {
                        JsonArray list = content.getAsJsonArray("returnData");
                        Gson gson = new Gson();
                        if (list.size() == 0)
                            isFinish = true;
                        Log.e("qqqqqTTTT", list.size() + "?" + isFinish);
                        for (int i = 0; i < list.size(); i++) {
////                        //通过反射 得到UserBean.class
                            JsonElement user = list.get(i);
                            HappyBannerBean userList = gson.fromJson(user, HappyBannerBean.class);
                            if (userList.getSite().equals("head"))
                                happyBannerBeans.add(userList);
                            else
                                footBean.add(userList);
                        }
                    }
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
                if (isFinish) {
                    MyDialog.closeDialog(dialog);
                    happyFootAdapter.notifyDataSetChanged();
                    initData();//初始化数据
                    initView();//初始化View，设置适配器
                    autoPlayView();//开启线程，自动播放
                } else {
                    page++;
                    getBanner("head", page);
                }


            }else if (!Utils.isEmpty(s) && "401".equals(s)) {
                Toast.makeText(getActivity().getApplicationContext(), "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences;
                preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
                MyDialog.setStart(false);
                if (preferences.contains("password")) {
                    preferences.edit().remove("password").commit();
                }
                startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
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
                    if (result.length() < 6) {
                        code = result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");

                    //开始
                    JSONObject returnData1 = jsonObject.getJSONObject("returnData");
                    String birthday = returnData1.getString("birthday");
                    userBean.setBirthday(birthday);
                    userBeanDao.insert(userBean);
                    //结束


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

            }else if (!Utils.isEmpty(s) && "401".equals(s)) {
                Toast.makeText(getActivity().getApplicationContext(), "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences;
                preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
                MyDialog.setStart(false);
                if (preferences.contains("password")) {
                    preferences.edit().remove("password").commit();
                }
                startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
            }
        }
    }

    public void getBanner(String site, int page) {
        Map<String, Object> params = new HashMap<>();
        params.put("site", site);
        params.put("page", page);
        new getAdByPageAsync().execute(params);
    }
}
