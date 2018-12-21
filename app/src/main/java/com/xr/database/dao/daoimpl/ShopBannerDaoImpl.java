package com.xr.database.dao.daoimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xr.database.dao.DBManager;
import com.xr.database.dao.DaoMaster;
import com.xr.database.dao.DaoSession;
import com.xr.database.dao.ShopBannerDao;
import com.xr.database.dao.ShopListDao;
import com.xr.happyFamily.bao.pojo.ShopBanner;
import com.xr.happyFamily.bao.pojo.ShopList;

import java.util.List;


public class ShopBannerDaoImpl {
    private Context context;
    private SQLiteDatabase db;
    private DaoMaster master;
    private ShopBannerDao shopBannerDao;
    private DaoSession session;
    public ShopBannerDaoImpl(Context context) {
        this.context = context;
        db= DBManager.getInstance(context).getWritableDasebase();
        master=new DaoMaster(db);
        session=master.newSession();
        shopBannerDao = session.getShopBannerDao();
    }

    /**
     * 添加商品信息
     * @param shopBanner
     */
    public void insert(ShopBanner shopBanner){
        shopBannerDao.insert(shopBanner);
    }

    /**
     * 删除商品信息
     * @param shopBanner
     */
    public void delete(ShopBanner shopBanner){
        shopBannerDao.delete(shopBanner);
    }

    /**
     * 更新商品信息
     * @param shopBanner
     */
    public void update(ShopBanner shopBanner){
        shopBannerDao.update(shopBanner);
    }

    public List<ShopBanner> findAll(){
        return shopBannerDao.loadAll();
    }
    public void  deleteAll(){
        shopBannerDao.deleteAll();
    }

}
