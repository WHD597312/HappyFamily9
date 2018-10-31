package com.xr.database.dao.daoimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xr.database.dao.DBManager;
import com.xr.database.dao.DaoMaster;
import com.xr.database.dao.DaoSession;
import com.xr.database.dao.DerailBeanDao;
import com.xr.database.dao.LeImageDao;
import com.xr.happyFamily.bao.pojo.LeImage;
import com.xr.happyFamily.le.pojo.DerailBean;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;


public class DerailBeanDaoImpl {
    private Context context;
    private SQLiteDatabase db;
    private DaoMaster master;
    private DerailBeanDao derailBeanDao;
    private DaoSession session;
    public DerailBeanDaoImpl(Context context) {
        this.context = context;
        db= DBManager.getInstance(context).getWritableDasebase();
        master=new DaoMaster(db);
        session=master.newSession();
        derailBeanDao = session.getDerailBeanDao();
    }

    /**
     * 添加商品信息
     * @param derailBean
     */
    public void insert(DerailBean derailBean){
        derailBeanDao.insert(derailBean);
    }

    /**
     * 删除商品信息
     * @param derailBean
     */
    public void delete(DerailBean derailBean){
        derailBeanDao.delete(derailBean);
    }

    /**
     * 更新商品信息
     * @param derailBean
     */
    public void update(DerailBean derailBean){
        derailBeanDao.update(derailBean);
    }


    public List<DerailBean> findAll(){
        return derailBeanDao.loadAll();
    }
    public void  deleteAll(){
        derailBeanDao.deleteAll();
    }


    public List<DerailBean> findByDerailId(int derailId){
        WhereCondition whereCondition=derailBeanDao.queryBuilder().and(DerailBeanDao.Properties.DerailId.eq(derailId),DerailBeanDao.Properties.DerailId.eq(derailId));
        return derailBeanDao.queryBuilder().where(whereCondition).list();
    }

}
