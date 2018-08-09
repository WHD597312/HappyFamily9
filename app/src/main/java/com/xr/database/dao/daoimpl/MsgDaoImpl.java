package com.xr.database.dao.daoimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xr.database.dao.ClockBeanDao;
import com.xr.database.dao.DBManager;
import com.xr.database.dao.DaoMaster;
import com.xr.database.dao.DaoSession;
import com.xr.database.dao.MsgDataDao;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.MsgData;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;


public class MsgDaoImpl {
    private Context context;
    private SQLiteDatabase db;
    private DaoMaster master;
    private MsgDataDao msgDataDao;
    private DaoSession session;
    public MsgDaoImpl(Context context) {
        this.context = context;
        db= DBManager.getInstance(context).getWritableDasebase();
        master=new DaoMaster(db);
        session=master.newSession();
        msgDataDao = session.getMsgDataDao();
    }

    /**
     * 添加闹钟信息
     * @param clockBean
     */
    public void insert(MsgData clockBean){
        msgDataDao.insert(clockBean);
    }

    /**
     * 删除闹钟信息
     * @param clockBean
     */
    public void delete(MsgData clockBean){
        msgDataDao.delete(clockBean);
    }

    /**
     * 更新闹钟信息
     * @param clockBean
     */
    public void update(MsgData clockBean){
        msgDataDao.update(clockBean);
    }

    public MsgData findById(long clockId){
        return msgDataDao.load(clockId);
    }
    public List<MsgData> findAll(){
        return msgDataDao.loadAll();
    }

    public List<MsgData> findMsgByTime(){
        return msgDataDao.queryBuilder().orderDesc(MsgDataDao.Properties.CreateTime).list();
    }

    public int findNumbByTime(long time){
        List<MsgData> msgDataList=msgDataDao.queryBuilder().where(MsgDataDao.Properties.CreateTime.gt(time)).list();
        return msgDataList.size();
    }
    public void  deleteAll(){
        msgDataDao.deleteAll();
    }

    public List<MsgData> findMsgByTime(long time){
        WhereCondition whereCondition=msgDataDao.queryBuilder().and(MsgDataDao.Properties.CreateTime.eq(time),MsgDataDao.Properties.CreateTime.eq(time));
        return msgDataDao.queryBuilder().where(whereCondition).list();
    }
}
