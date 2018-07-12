package com.xr.database.dao.daoimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xr.database.dao.DBManager;
import com.xr.database.dao.DaoMaster;
import com.xr.database.dao.DaoSession;
import com.xr.database.dao.TimeDao;
import com.xr.database.dao.UserBeanDao;
import com.xr.happyFamily.le.pojo.Time;
import com.xr.happyFamily.le.pojo.UserBean;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;


public class UserBeanDaoImpl {
    private Context context;
    private SQLiteDatabase db;
    private DaoMaster master;
    private UserBeanDao userBeanDao;
    private DaoSession session;
    public UserBeanDaoImpl(Context context) {
        this.context = context;
        db= DBManager.getInstance(context).getWritableDasebase();
        master=new DaoMaster(db);
        session=master.newSession();
        userBeanDao = session.getUserBeanDao();

    }

    /**
     * 添加设备
     * @param userBean
     */
    public void insert(UserBean userBean){
        userBeanDao.insert(userBean);
    }

    /**
     * 删除设备
     * @param userBean
     */
    public void delete(UserBean userBean){
        userBeanDao.delete(userBean);
    }

    /**
     * 更新设备
     * @param
     */
    public void update(UserBean userBean){
        userBeanDao.update(userBean);
    }

    public UserBean findById(long userId){
        return userBeanDao.load(userId);
    }
//    public List<Time> findAllTimes(long userId){
//        List<Time> times=timeDao.queryBuilder().where(TimeDao.Properties.UserId.eq(userId)).orderAsc(TimeDao.Properties.UserId).list();
//        return times;
//    }
    public List<UserBean> findByAllUser(){
        return userBeanDao.loadAll();
    }


    public void  deleteAll(){
        userBeanDao.deleteAll();
    }
}
