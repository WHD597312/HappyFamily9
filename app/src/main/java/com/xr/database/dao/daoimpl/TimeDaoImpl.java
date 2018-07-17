package com.xr.database.dao.daoimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xr.database.dao.DBManager;
import com.xr.database.dao.DaoMaster;
import com.xr.database.dao.DaoSession;
import com.xr.database.dao.DeviceChildDao;
import com.xr.database.dao.TimeDao;
import com.xr.happyFamily.le.pojo.Time;


import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;


public class TimeDaoImpl {
    private Context context;
    private SQLiteDatabase db;
    private DaoMaster master;
    private TimeDao timeDao;
    private DaoSession session;
    public TimeDaoImpl(Context context) {
        this.context = context;
        db= DBManager.getInstance(context).getWritableDasebase();
        master=new DaoMaster(db);
        session=master.newSession();
        timeDao = session.getTimeDao();
    }

    /**
     * 添加设备
     * @param time
     */
    public void insert(Time time){
        timeDao.insert(time);
    }

    public Time findById(long id){
        return timeDao.load(id);
    }
    /**
     * 删除设备
     * @param time
     */
    public void delete(Time time){
        timeDao.delete(time);
    }

    /**
     * 更新设备
     * @param time
     */
    public void update(Time time){
        timeDao.update(time);
    }


//    public Time findById(long userId){
//        return timeDao.load(userId);
//    }
//    public List<Time> findAllTimes(long userId){
//        List<Time> times=timeDao.queryBuilder().where(TimeDao.Properties.UserId.eq(userId)).orderAsc(TimeDao.Properties.UserId).list();
//        return times;
//    }
    public List<Time> findByAllTime(){
        return timeDao.loadAll();
    }
    public List<Time> findTimesByHourAndMin(int hour,int min){
        WhereCondition whereCondition=timeDao.queryBuilder().and(TimeDao.Properties.Hour.eq(hour),TimeDao.Properties.Minutes.eq(min));
        return timeDao.queryBuilder().where(whereCondition).list();
    }
    public List<Time> findTimeByMin(){
        return timeDao.queryBuilder().orderAsc(TimeDao.Properties.SumMin).list();
    }

    public void  deleteAll(){
        timeDao.deleteAll();
    }
}
