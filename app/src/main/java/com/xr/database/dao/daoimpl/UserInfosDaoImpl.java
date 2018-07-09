package com.xr.database.dao.daoimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xr.database.dao.ClockBeanDao;
import com.xr.database.dao.DBManager;
import com.xr.database.dao.DaoMaster;
import com.xr.database.dao.DaoSession;
import com.xr.database.dao.UserInfoDao;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.UserInfo;


import java.util.List;


public class UserInfosDaoImpl {
    private Context context;
    private SQLiteDatabase db;
    private DaoMaster master;
    private UserInfoDao userInfoDao;
    private DaoSession session;
    public UserInfosDaoImpl(Context context) {
        this.context = context;
        db= DBManager.getInstance(context).getWritableDasebase();
        master=new DaoMaster(db);
        session=master.newSession();
        userInfoDao = session.getUserInfoDao();
    }

    /**
     * 添加闹钟信息
     * @param userInfo
     */
    public void insert(UserInfo userInfo){
        userInfoDao.insert(userInfo);
    }

    /**
     * 删除闹钟信息
     * @param userInfo
     */
    public void delete(UserInfo userInfo){
        userInfoDao.delete(userInfo);
    }

    /**
     * 更新闹钟信息
     * @param userInfo
     */
    public void update(UserInfo userInfo){
        userInfoDao.update(userInfo);
    }

    public UserInfo findById(long userId){
        return userInfoDao.load(userId);
    }
    public List<UserInfo> findAll(){
        return userInfoDao.loadAll();
    }
    public void  deleteAll(){
        userInfoDao.deleteAll();
    }
}
