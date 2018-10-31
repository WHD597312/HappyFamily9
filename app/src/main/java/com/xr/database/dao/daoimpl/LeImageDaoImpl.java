package com.xr.database.dao.daoimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xr.database.dao.DBManager;
import com.xr.database.dao.DaoMaster;
import com.xr.database.dao.DaoSession;
import com.xr.database.dao.LeImageDao;
import com.xr.database.dao.ShopListDao;
import com.xr.happyFamily.bao.pojo.LeImage;
import com.xr.happyFamily.bao.pojo.ShopList;

import java.util.List;


public class LeImageDaoImpl {
    private Context context;
    private SQLiteDatabase db;
    private DaoMaster master;
    private LeImageDao leImageDao;
    private DaoSession session;
    public LeImageDaoImpl(Context context) {
        this.context = context;
        db= DBManager.getInstance(context).getWritableDasebase();
        master=new DaoMaster(db);
        session=master.newSession();
        leImageDao = session.getLeImageDao();
    }

    /**
     * 添加商品信息
     * @param leImage
     */
    public void insert(LeImage leImage){
        leImageDao.insert(leImage);
    }

    /**
     * 删除商品信息
     * @param leImage
     */
    public void delete(LeImage leImage){
        leImageDao.delete(leImage);
    }

    /**
     * 更新商品信息
     * @param leImage
     */
    public void update(LeImage leImage){
        leImageDao.update(leImage);
    }


    public List<LeImage> findAll(){
        return leImageDao.loadAll();
    }
    public void  deleteAll(){
        leImageDao.deleteAll();
    }

}
