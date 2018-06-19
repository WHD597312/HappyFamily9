package com.xr.database.dao.daoimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xr.database.dao.DBManager;
import com.xr.database.dao.DaoMaster;
import com.xr.database.dao.DaoSession;
import com.xr.database.dao.DeviceChildDao;
import com.xr.happyFamily.jia.pojo.DeviceChild;

public class DeviceChildDaoImpl {
    private Context context;
    private SQLiteDatabase db;
    private DaoMaster master;
    private DeviceChildDao deviceChildDao;
    private DaoSession session;
    public DeviceChildDaoImpl(Context context) {
        this.context = context;
        db= DBManager.getInstance(context).getWritableDasebase();
        master=new DaoMaster(db);
        session=master.newSession();
        deviceChildDao=session.getDeviceChildDao();
    }

    /**
     * 添加设备
     * @param deviceChild
     */
    public void insert(DeviceChild deviceChild){
        deviceChildDao.insert(deviceChild);
    }

    /**
     * 删除设备
     * @param deviceChild
     */
    public void delete(DeviceChild deviceChild){
        deviceChildDao.delete(deviceChild);
    }

    /**
     * 更新设备
     * @param deviceChild
     */
    public void update(DeviceChild deviceChild){
        deviceChildDao.update(deviceChild);
    }

    public DeviceChild findById(long id){
        return deviceChildDao.load(id);
    }
}
