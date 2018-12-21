package com.xr.database.dao.daoimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xr.database.dao.DBManager;
import com.xr.database.dao.DaoMaster;
import com.xr.database.dao.DaoSession;
import com.xr.database.dao.DerailBeanDao;
import com.xr.database.dao.DerailResultDao;
import com.xr.happyFamily.le.pojo.DerailBean;
import com.xr.happyFamily.le.pojo.DerailResult;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;


public class DerailResultDaoImpl {
    private Context context;
    private SQLiteDatabase db;
    private DaoMaster master;
    private DerailResultDao beanDao;
    private DaoSession session;
    public DerailResultDaoImpl(Context context) {
        this.context = context;
        db= DBManager.getInstance(context).getWritableDasebase();
        master=new DaoMaster(db);
        session=master.newSession();
        beanDao = session.getDerailResultDao();
    }

    /**
     * 添加商品信息
     * @param bean
     */
    public void insert(DerailResult bean){
        beanDao.insert(bean);
    }

    /**
     * 删除商品信息
     * @param bean
     */
    public void delete(DerailResult bean){
        beanDao.delete(bean);
    }

    /**
     * 更新商品信息
     * @param bean
     */
    public void update(DerailResult bean){
        beanDao.update(bean);
    }


    public List<DerailResult> findAll(){
        return beanDao.loadAll();
    }
    public void  deleteAll(){
        beanDao.deleteAll();
    }


    public List<DerailResult> findByDerailId(int derailId){
        WhereCondition whereCondition=beanDao.queryBuilder().and(DerailResultDao.Properties.DerailId.eq(derailId),DerailResultDao.Properties.DerailId.eq(derailId));
        return beanDao.queryBuilder().where(whereCondition).list();
    }

}
