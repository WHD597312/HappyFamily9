package com.xr.database.dao.daoimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xr.database.dao.ClockBeanDao;
import com.xr.database.dao.DBManager;
import com.xr.database.dao.DaoMaster;
import com.xr.database.dao.DaoSession;
import com.xr.database.dao.HourseDao;
import com.xr.database.dao.UserInfoDao;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.UserInfo;

import java.util.List;


public class ClockDaoImpl {
    private Context context;
    private SQLiteDatabase db;
    private DaoMaster master;
    private ClockBeanDao clockBeanDao;
    private DaoSession session;
    public ClockDaoImpl(Context context) {
        this.context = context;
        db= DBManager.getInstance(context).getWritableDasebase();
        master=new DaoMaster(db);
        session=master.newSession();
        clockBeanDao = session.getClockBeanDao();
    }

    /**
     * 添加闹钟信息
     * @param clockBean
     */
    public void insert(ClockBean clockBean){
        clockBeanDao.insert(clockBean);
    }

    /**
     * 删除闹钟信息
     * @param clockBean
     */
    public void delete(ClockBean clockBean){
        clockBeanDao.delete(clockBean);
    }

    /**
     * 更新闹钟信息
     * @param clockBean
     */
    public void update(ClockBean clockBean){
        clockBeanDao.update(clockBean);
    }

    public ClockBean findById(long clockId){
        return clockBeanDao.load(clockId);
    }
    public List<ClockBean> findAll(){
        return clockBeanDao.loadAll();
    }
    public void  deleteAll(){
        clockBeanDao.deleteAll();
    }
}
