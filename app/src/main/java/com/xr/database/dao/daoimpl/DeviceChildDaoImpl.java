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

    /**
     * 删除所有的设备
     */
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

    /**
     * 查询固定设备
     * @param id
     * @return
     */
    public DeviceChild findById(long id){
        return deviceChildDao.load(id);
    }

    /**
     * 查询所有的设备
     * @return
     */
    public List<DeviceChild> findAllDevice(){
        return deviceChildDao.loadAll();
    }

    /**
     * 查询家里面的所有设备
     * @param houseId
     * @return
     */
    public List<DeviceChild> findHouseDevices(long houseId){
        List<DeviceChild> deviceChildren=deviceChildDao.queryBuilder().where(DeviceChildDao.Properties.HouseId.eq(houseId)).orderAsc(DeviceChildDao.Properties.Id).list();
        return deviceChildren;
    }

    /**
     * 查询家庭里面房间中的设备
     * @param houseId
     * @param roomId
     * @return
     */
    public List<DeviceChild> findHouseInRoomDevices(long houseId,long roomId){
        WhereCondition whereCondition=deviceChildDao.queryBuilder().and(DeviceChildDao.Properties.HouseId.eq(houseId),DeviceChildDao.Properties.RoomId.eq(roomId));
        return deviceChildDao.queryBuilder().where(whereCondition).orderDesc(DeviceChildDao.Properties.DeviceId).list();
    }

    /***
     * 删除家庭里房间中的设备
     * @param houseId
     * @param roomId
     */
    public void deleteDeviceInHouseRoom(long houseId,long roomId){
        List<DeviceChild> deviceChildren=findHouseInRoomDevices(houseId, roomId);
        if (!deviceChildren.isEmpty()){
            deviceChildDao.deleteInTx(deviceChildren);
        }
    }

    /**
     * 查询家里面的常用设备
     * @param houseId
     * @return
     */
    public List<DeviceChild> findHouseCommonDevices(long houseId){
        return deviceChildDao.queryBuilder().where(DeviceChildDao.Properties.HouseId.eq(houseId)).orderDesc(DeviceChildDao.Properties.DeviceUsedCount).limit(4).list();
    }

    /**
     * 查询分享设备
     * @param userId
     * @return
     */
    public List<DeviceChild> findShareDevice(int userId){
        WhereCondition whereCondition=deviceChildDao.queryBuilder().and(DeviceChildDao.Properties.UserId.eq(userId),DeviceChildDao.Properties.Share.eq("share"));
        return deviceChildDao.queryBuilder().where(whereCondition).orderDesc(DeviceChildDao.Properties.DeviceId).list();
    }

    /***
     * 查询设备类型
     * @param houseId
     * @param roomId
     * @param type
     * @return
     */
    public List<DeviceChild> findDeviceType(long houseId,long roomId,int type){
        WhereCondition whereCondition=deviceChildDao.queryBuilder().and(DeviceChildDao.Properties.HouseId.eq(houseId),DeviceChildDao.Properties.RoomId.eq(roomId),DeviceChildDao.Properties.Type.eq(type),DeviceChildDao.Properties.ShareId.notEq(Long.MAX_VALUE));
        return deviceChildDao.queryBuilder().where(whereCondition).orderAsc(DeviceChildDao.Properties.Id).list();
    }

    /**
     * 查询家庭里房间中的设备类型是否在线
     * @param houseId
     * @param roomId
     * @param type
     * @param online
     * @return
     */
    public List<DeviceChild> findDeviceByType(long houseId,long roomId,int type,boolean online){
        WhereCondition whereCondition=deviceChildDao.queryBuilder().and(DeviceChildDao.Properties.HouseId.eq(houseId),DeviceChildDao.Properties.RoomId.eq(roomId), DeviceChildDao.Properties.Type.eq(type),DeviceChildDao.Properties.Online.eq(online));
        return deviceChildDao.queryBuilder().where(whereCondition).orderAsc(DeviceChildDao.Properties.Id).list();
    }

    /**
     *  查询智能终端已经联动且在线的设备
     * @param houseId
     * @param roomId
     * @param type
     * @param linkedSensorId
     * @param linked
     * @param online
     * @return
     */
    public List<DeviceChild> findLinkedDevices(long houseId,long roomId,int type,int linkedSensorId,int linked,boolean online) {
        WhereCondition whereCondition=deviceChildDao.queryBuilder().and(DeviceChildDao.Properties.HouseId.eq(houseId),DeviceChildDao.Properties.RoomId.eq(roomId), DeviceChildDao.Properties.Type.eq(type),DeviceChildDao.Properties.LinkedSensorId.eq(linkedSensorId),DeviceChildDao.Properties.Linked.eq(linked),DeviceChildDao.Properties.Online.eq(online));
        return deviceChildDao.queryBuilder().where(whereCondition).orderAsc(DeviceChildDao.Properties.Id).list();
    }

    /**
     * 查询智能终端联动的设备
     * @param houseId
     * @param roomId
     * @param type
     * @param linkedSensorId
     * @param linked
     * @return
     */
    public List<DeviceChild> findLinkedDevices(long houseId,long roomId,int type,int linkedSensorId,int linked){
        WhereCondition whereCondition=deviceChildDao.queryBuilder().and(DeviceChildDao.Properties.HouseId.eq(houseId),DeviceChildDao.Properties.RoomId.eq(roomId), DeviceChildDao.Properties.Type.eq(type),DeviceChildDao.Properties.LinkedSensorId.eq(linkedSensorId),DeviceChildDao.Properties.Linked.eq(linked));
        return deviceChildDao.queryBuilder().where(whereCondition).orderAsc(DeviceChildDao.Properties.Id).list();
    }
    public DeviceChild findDeviceByDeviceId(long houseId,long roomId,int deviceId){
        WhereCondition whereCondition=deviceChildDao.queryBuilder().and(DeviceChildDao.Properties.HouseId.eq(houseId),DeviceChildDao.Properties.RoomId.eq(roomId), DeviceChildDao.Properties.DeviceId.eq(deviceId));
        return deviceChildDao.queryBuilder().where(whereCondition).list().get(0);
    }
}
