package com.xr.database.dao.daoimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.xr.database.dao.AppUsingDao;
import com.xr.database.dao.DBManager;
import com.xr.database.dao.DaoMaster;
import com.xr.database.dao.DaoSession;
import com.xr.happyFamily.le.pojo.AppUsing;
import java.util.List;


public class AppUsingImpl {
    private Context context;
    private SQLiteDatabase db;
    private DaoMaster master;
    private AppUsingDao appUsingDao;
    private DaoSession session;
    public AppUsingImpl(Context context) {
        this.context = context;
        db= DBManager.getInstance(context).getWritableDasebase();
        master=new DaoMaster(db);
        session=master.newSession();
        appUsingDao = session.getAppUsingDao();
    }

    /**
     * 添加闹钟信息
     * @param appUsing
     */
    public void insert(AppUsing appUsing){
        appUsingDao.insert(appUsing);
    }

    /**
     * 删除闹钟信息
     * @param
     */
    public void delete(AppUsing appUsing){
        appUsingDao.delete(appUsing);
    }

    /**
     * 更新闹钟信息
     * @param appUsing
     */
    public void update(AppUsing appUsing){
        appUsingDao.update(appUsing);
    }

    public AppUsing findById(int Id){
        return appUsingDao.load(Id);
    }
    public List<AppUsing> findAll(){
        return appUsingDao.loadAll();
    }

    public void  deleteAll(){
        appUsingDao.deleteAll();
    }

    public AppUsing findClockByAppname(String name){
        return appUsingDao.queryBuilder().where(AppUsingDao.Properties.AppName.eq(name)).unique();
    }


}
