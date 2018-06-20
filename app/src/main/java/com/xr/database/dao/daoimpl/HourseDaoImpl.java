package com.xr.database.dao.daoimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xr.database.dao.DBManager;
import com.xr.database.dao.DaoMaster;
import com.xr.database.dao.DaoSession;
import com.xr.database.dao.HourseDao;
import com.xr.happyFamily.jia.pojo.Hourse;

import java.util.List;


public class HourseDaoImpl {
    private Context context;
    private SQLiteDatabase db;
    private DaoMaster master;
    private HourseDao hourseDao;
    private DaoSession session;
    public HourseDaoImpl(Context context) {
        this.context = context;
        db= DBManager.getInstance(context).getWritableDasebase();
        master=new DaoMaster(db);
        session=master.newSession();
        hourseDao = session.getHourseDao();

    }

    /**
     * 添加设备
     * @param hourse
     */
    public void insert(Hourse hourse){
        hourseDao.insert(hourse);
    }

    /**
     * 删除设备
     * @param hourse
     */
    public void delete(Hourse hourse){
        hourseDao.delete(hourse);
    }

    /**
     * 更新设备
     * @param hourse
     */
    public void update(Hourse hourse){
        hourseDao.update(hourse);
    }

    public Hourse findById(long hourseId){
        return hourseDao.load(hourseId);
    }
    public List<Hourse> findAllHouse(){
        return hourseDao.loadAll();
    }
}
