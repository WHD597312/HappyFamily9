package com.xr.database.dao.daoimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xr.database.dao.ClockBeanDao;
import com.xr.database.dao.DBManager;
import com.xr.database.dao.DaoMaster;
import com.xr.database.dao.DaoSession;
import com.xr.database.dao.FriendDataDao;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.FriendData;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;


public class FriendDataDaoImpl {
    private Context context;
    private SQLiteDatabase db;
    private DaoMaster master;
    private FriendDataDao friendDataDao;
    private DaoSession session;
    public FriendDataDaoImpl(Context context) {
        this.context = context;
        db= DBManager.getInstance(context).getWritableDasebase();
        master=new DaoMaster(db);
        session=master.newSession();
        friendDataDao = session.getFriendDataDao();
    }

    /**
     * 添加闹钟信息
     * @param friendData
     */
    public void insert(FriendData friendData){
        friendDataDao.insert(friendData);
    }

    /**
     * 删除闹钟信息
     * @param friendData
     */
    public void delete(FriendData friendData){
        friendDataDao.delete(friendData);
    }

    /**
     * 更新闹钟信息
     * @param friendData
     */
    public void update(FriendData friendData){
        friendDataDao.update(friendData);
    }

    public FriendData findById(long clockId){
        return friendDataDao.load(clockId);
    }
    public List<FriendData> findAll(){
        return friendDataDao.loadAll();
    }
    public void  deleteAll(){
        friendDataDao.deleteAll();
    }

    public List<FriendData> findFriendBySendId(int senderId){
        WhereCondition whereCondition=friendDataDao.queryBuilder().and(FriendDataDao.Properties.SenderId.eq(senderId),FriendDataDao.Properties.SenderId.eq(senderId));
        return friendDataDao.queryBuilder().where(whereCondition).list();
    }
}
