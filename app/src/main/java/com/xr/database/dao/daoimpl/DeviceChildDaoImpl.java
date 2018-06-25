package com.xr.database.dao.daoimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xr.database.dao.DBManager;
import com.xr.database.dao.DaoMaster;
import com.xr.database.dao.DaoSession;
import com.xr.database.dao.DeviceChildDao;
import com.xr.happyFamily.jia.pojo.DeviceChild;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

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
    public boolean insert(DeviceChild deviceChild){
        long n=deviceChildDao.insert(deviceChild);
        return n>=1?true:false;
    }

    /**
     * 删除设备
     * @param deviceChild
     */
    public void delete(DeviceChild deviceChild){
        deviceChildDao.delete(deviceChild);
    }
    public void  deleteAll(){
        deviceChildDao.deleteAll();
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
    public List<DeviceChild> findAllDevice(){
        return deviceChildDao.loadAll();
    }
    public List<DeviceChild> findHouseDevices(long houseId){
        List<DeviceChild> deviceChildren=deviceChildDao.queryBuilder().where(DeviceChildDao.Properties.HouseId.eq(houseId)).orderAsc(DeviceChildDao.Properties.Id).list();
        return deviceChildren;
    }
    public List<DeviceChild> findHouseInRoomDevices(long houseId,long roomId){
        WhereCondition whereCondition=deviceChildDao.queryBuilder().and(DeviceChildDao.Properties.HouseId.eq(houseId),DeviceChildDao.Properties.RoomId.eq(roomId));
        return deviceChildDao.queryBuilder().where(whereCondition).list();
    }
}
