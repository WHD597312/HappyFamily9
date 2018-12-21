package com.xr.database.dao.daoimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xr.database.dao.ClockBeanDao;
import com.xr.database.dao.DBManager;
import com.xr.database.dao.DaoMaster;
import com.xr.database.dao.DaoSession;
import com.xr.database.dao.ShopListDao;
import com.xr.happyFamily.bao.pojo.ShopList;
import com.xr.happyFamily.le.pojo.ClockBean;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;


public class ShopListDaoImpl {
    private Context context;
    private SQLiteDatabase db;
    private DaoMaster master;
    private ShopListDao shopListDao;
    private DaoSession session;
    public ShopListDaoImpl(Context context) {
        this.context = context;
        db= DBManager.getInstance(context).getWritableDasebase();
        master=new DaoMaster(db);
        session=master.newSession();
        shopListDao = session.getShopListDao();
    }

    /**
     * 添加商品信息
     * @param shopList
     */
    public void insert(ShopList shopList){
        shopListDao.insert(shopList);
    }

    /**
     * 删除商品信息
     * @param shopList
     */
    public void delete(ShopList shopList){
        shopListDao.delete(shopList);
    }

    /**
     * 更新商品信息
     * @param shopList
     */
    public void update(ShopList shopList){
        shopListDao.update(shopList);
    }

    public ShopList findById(long id){
        return shopListDao.load(id);
    }
    public List<ShopList> findAll(){
        return shopListDao.loadAll();
    }
    public void  deleteAll(){
        shopListDao.deleteAll();
    }

}
