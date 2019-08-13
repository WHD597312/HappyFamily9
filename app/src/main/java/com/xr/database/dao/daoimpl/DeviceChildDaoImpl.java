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
        db = DBManager.getInstance(context).getWritableDasebase();
        master = new DaoMaster(db);
        session = master.newSession();
        deviceChildDao = session.getDeviceChildDao();
    }

    /**
     * 添加设备
     *
     * @param deviceChild
     */
    public boolean insert(DeviceChild deviceChild) {
        long n = deviceChildDao.insert(deviceChild);
        return n >= 1 ? true : false;
    }

    /**
     * 删除设备
     *
     * @param deviceChild
     */
    public void delete(DeviceChild deviceChild) {
        deviceChildDao.delete(deviceChild);
    }

    /**
     * 删除所有的设备
     */
    public void deleteAll() {
        deviceChildDao.deleteAll();
    }

    /**
     * 更新设备
     *
     * @param deviceChild
     */
    public void update(DeviceChild deviceChild) {
        deviceChildDao.update(deviceChild);
    }

    /**
     * 查询固定设备
     *
     * @param id
     * @return
     */
    public DeviceChild findById(Long id) {
        return deviceChildDao.queryBuilder().where(DeviceChildDao.Properties.Id.eq(id)).unique();
    }

    /**
     * 查询所有的设备
     *
     * @return
     */
    public List<DeviceChild> findAllDevice() {
        return deviceChildDao.loadAll();
    }

    /**
     * 查询家里面的所有设备
     *
     * @param houseId
     * @return
     */
    public List<DeviceChild> findHouseDevices(long houseId) {
        List<DeviceChild> deviceChildren = deviceChildDao.queryBuilder().where(DeviceChildDao.Properties.HouseId.eq(houseId)).orderAsc(DeviceChildDao.Properties.Id).list();
        return deviceChildren;
    }

    /**
     * 根据macAddress来查询设备
     *
     * @param macAddress
     * @return
     */
    public List<DeviceChild> findDeviceByMacAddress(String macAddress) {
        return deviceChildDao.queryBuilder().where(DeviceChildDao.Properties.MacAddress.eq(macAddress)).list();
    }

    /**
     * 根据macAddress来查询设备
     *
     * @param macAddress
     * @return
     */
    public DeviceChild findDeviceByMacAddress2(String macAddress) {
        return deviceChildDao.queryBuilder().where(DeviceChildDao.Properties.MacAddress.eq(macAddress)).unique();
    }

    /**
     * 批量删除设备
     *
     * @param list
     */
    public void deleteDevices(List<DeviceChild> list) {
        deviceChildDao.deleteInTx(list);
    }


    /**
     * 查询家庭里面房间中的设备
     *
     * @param houseId
     * @param roomId
     * @return
     */
    public List<DeviceChild> findHouseInRoomDevices(long houseId, long roomId) {
        WhereCondition whereCondition = deviceChildDao.queryBuilder().and(DeviceChildDao.Properties.HouseId.eq(houseId), DeviceChildDao.Properties.RoomId.eq(roomId));
        return deviceChildDao.queryBuilder().where(whereCondition).orderAsc(DeviceChildDao.Properties.DeviceId).list();
    }

    /***
     * 删除家庭里房间中的设备
     * @param houseId
     * @param roomId
     */
    public void deleteDeviceInHouseRoom(long houseId, long roomId) {
        List<DeviceChild> deviceChildren = findHouseInRoomDevices(houseId, roomId);
        if (!deviceChildren.isEmpty()) {
            deviceChildDao.deleteInTx(deviceChildren);
        }
    }

    /**
     * 查询家里面的常用设备
     *
     * @param houseId
     * @return
     */
    public List<DeviceChild> findHouseCommonDevices(long houseId) {
        return deviceChildDao.queryBuilder().where(DeviceChildDao.Properties.HouseId.eq(houseId)).orderDesc(DeviceChildDao.Properties.DeviceUsedCount).limit(4).list();
    }

    /**
     * 查询分享设备
     *
     * @param userId
     * @return
     */
    public List<DeviceChild> findShareDevice(int userId) {
        WhereCondition whereCondition = deviceChildDao.queryBuilder().and(DeviceChildDao.Properties.UserId.eq(userId), DeviceChildDao.Properties.Share.eq("share"));
        return deviceChildDao.queryBuilder().where(whereCondition).orderAsc(DeviceChildDao.Properties.DeviceId).list();
    }

    /***
     * 查询设备类型
     * @param houseId
     * @param roomId
     * @param type
     * @return
     */
    public List<DeviceChild> findDeviceType(long houseId, long roomId, int type) {
        WhereCondition whereCondition = deviceChildDao.queryBuilder().and(DeviceChildDao.Properties.HouseId.eq(houseId), DeviceChildDao.Properties.RoomId.eq(roomId), DeviceChildDao.Properties.Type.eq(type), DeviceChildDao.Properties.ShareId.notEq(Long.MAX_VALUE));
        return deviceChildDao.queryBuilder().where(whereCondition).orderAsc(DeviceChildDao.Properties.Id).list();
    }

    /**
     * 获取在线的外置传感器
     *
     * @param houseId
     * @param roomId
     * @return
     */
    public DeviceChild findOnlineEstDevice(long houseId, long roomId) {
        DeviceChild deviceChild = null;
        WhereCondition whereCondition = deviceChildDao.queryBuilder().and(DeviceChildDao.Properties.HouseId.eq(houseId), DeviceChildDao.Properties.RoomId.eq(roomId), DeviceChildDao.Properties.Type.eq(3), DeviceChildDao.Properties.Online.eq(true));
        List<DeviceChild> deviceChildren = deviceChildDao.queryBuilder().where(whereCondition).list();
        if (deviceChildren != null && !deviceChildren.isEmpty()) {
            deviceChild = deviceChildren.get(0);
        }
        return deviceChild;
    }

    /**
     * 查询家庭里房间中的设备类型是否在线
     *
     * @param houseId
     * @param roomId
     * @param type
     * @param online
     * @return
     */
    public List<DeviceChild> findDeviceByType(long houseId, long roomId, int type, boolean online) {
        WhereCondition whereCondition = deviceChildDao.queryBuilder().and(DeviceChildDao.Properties.HouseId.eq(houseId), DeviceChildDao.Properties.RoomId.eq(roomId), DeviceChildDao.Properties.Type.eq(type), DeviceChildDao.Properties.Online.eq(online));
        return deviceChildDao.queryBuilder().where(whereCondition).orderAsc(DeviceChildDao.Properties.Id).list();
    }

    /**
     * 查询智能终端已经联动且在线的设备
     *
     * @param houseId
     * @param roomId
     * @param type
     * @param linkedSensorId
     * @param linked
     * @param online
     * @return
     */
    public List<DeviceChild> findLinkedDevices(long houseId, long roomId, int type, int linkedSensorId, int linked, boolean online) {
        WhereCondition whereCondition = deviceChildDao.queryBuilder().and(DeviceChildDao.Properties.HouseId.eq(houseId), DeviceChildDao.Properties.RoomId.eq(roomId), DeviceChildDao.Properties.Type.eq(type), DeviceChildDao.Properties.LinkedSensorId.eq(linkedSensorId), DeviceChildDao.Properties.Linked.eq(linked), DeviceChildDao.Properties.Online.eq(online));
        return deviceChildDao.queryBuilder().where(whereCondition).orderAsc(DeviceChildDao.Properties.Id).list();
    }

    /**
     * 查询智能终端联动的设备
     *
     * @param houseId
     * @param roomId
     * @param type
     * @param linkedSensorId
     * @param linked
     * @return
     */
    public List<DeviceChild> findLinkedDevices(long houseId, long roomId, int type, int linkedSensorId, int linked) {
        WhereCondition whereCondition = deviceChildDao.queryBuilder().and(DeviceChildDao.Properties.HouseId.eq(houseId), DeviceChildDao.Properties.RoomId.eq(roomId), DeviceChildDao.Properties.Type.eq(type), DeviceChildDao.Properties.LinkedSensorId.eq(linkedSensorId), DeviceChildDao.Properties.Linked.eq(linked), DeviceChildDao.Properties.ShareId.notEq(Long.MAX_VALUE));
        return deviceChildDao.queryBuilder().where(whereCondition).orderAsc(DeviceChildDao.Properties.Id).list();
    }

    public DeviceChild findDeviceByDeviceId(long houseId, long roomId, int deviceId) {
        WhereCondition whereCondition = deviceChildDao.queryBuilder().and(DeviceChildDao.Properties.HouseId.eq(houseId), DeviceChildDao.Properties.RoomId.eq(roomId), DeviceChildDao.Properties.DeviceId.eq(deviceId));
        return deviceChildDao.queryBuilder().where(whereCondition).unique();
    }

    /**
     * 获取未知设备类型的设备
     *
     * @param type
     * @return
     */
    public List<DeviceChild> findZerosType(int type) {
        List<DeviceChild> children = deviceChildDao.queryBuilder().where(DeviceChildDao.Properties.Type.eq(type)).orderAsc(DeviceChildDao.Properties.DeviceId).list();
        return children;
    }

    /**
     * 获取类型为9的全部设备,且不是分享的设备
     *
     * @param houseId
     * @param roomId
     * @return
     */
    public List<DeviceChild> findHeaterDevice(long houseId, long roomId) {
        WhereCondition whereCondition = deviceChildDao.queryBuilder().
                and(DeviceChildDao.Properties.HouseId.eq(houseId),
                        DeviceChildDao.Properties.RoomId.eq(roomId),
                        DeviceChildDao.Properties.Type.eq(9),
                        DeviceChildDao.Properties.Share.isNull());
        return deviceChildDao.queryBuilder().where(whereCondition).list();
    }

    /**
     * 获取HousId(家)中的roomId(房间)中设备类型为9的主控设备
     *
     * @param houseId
     * @param roomId
     * @return
     */
    public DeviceChild findHeaderMasterDevice(long houseId, long roomId) {
        WhereCondition whereCondition = deviceChildDao.queryBuilder().
                and(DeviceChildDao.Properties.HouseId.eq(houseId),
                        DeviceChildDao.Properties.RoomId.eq(roomId),
                        DeviceChildDao.Properties.Type.eq(9),//p99新取暖器
                        DeviceChildDao.Properties.HeaterControl.eq(2),//主控设备
                        DeviceChildDao.Properties.Share.isNull());
        return deviceChildDao.queryBuilder().where(whereCondition).unique();
    }

    /**
     * 获取可以智能设备的列表
     *
     * @param houseId
     * @param roomId
     * @return
     */
    public List<DeviceChild> findHeaterSmartMayControlDevice(long houseId, long roomId) {
        WhereCondition whereCondition = deviceChildDao.queryBuilder().
                and(DeviceChildDao.Properties.HouseId.eq(houseId),//设备的家
                        DeviceChildDao.Properties.RoomId.eq(roomId),//设备家中的房间
                        DeviceChildDao.Properties.Type.eq(9),//p99新取暖器
                        DeviceChildDao.Properties.HeaterControl.notEq(2),//heaterControl为2为主控设备，3为受控设备，1为普通设备
                        DeviceChildDao.Properties.Share.isNull());
        List<DeviceChild> list=deviceChildDao.queryBuilder().where(whereCondition).list();
        int size=list.size();
        for (int i = 0; i < size; i++) {
            DeviceChild deviceChild=list.get(i);
            if (deviceChild.getHeaterControl()==3){
                deviceChild.setHeaterCheck(1);
            }else {
                deviceChild.setHeaterCheck(0);
            }
            list.set(i,deviceChild);
        }
        return list;
    }

    /**
     * 获取家中的房间下的设备类型为9,且是在线的，不是分享的，可以设置为主控的设备列表
     * @param houseId
     * @param roomId
     * @return
     */
    public List<DeviceChild> findHeaterSmartMayMasterDevcice(long houseId, long roomId){
        WhereCondition whereCondition = deviceChildDao.queryBuilder().
                     and(DeviceChildDao.Properties.HouseId.eq(houseId),//家的id
                        DeviceChildDao.Properties.RoomId.eq(roomId),//家中的房间id
                        DeviceChildDao.Properties.Type.eq(9),//设备类型为9
                        DeviceChildDao.Properties.HeaterControl.notEq(3),//heaterControl为2为主控设备，3为受控设备，1为普通设备
                        DeviceChildDao.Properties.Online.eq(true),//设备在线
                        DeviceChildDao.Properties.Share.isNull());//不为分享设备
        List<DeviceChild> list=deviceChildDao.queryBuilder().where(whereCondition).list();
        int size=list.size();
        for (int i = 0; i < size; i++) {
            DeviceChild deviceChild=list.get(i);
            if (deviceChild.getHeaterControl()==2){
                deviceChild.setHeaterCheck(1);
            }else {
                deviceChild.setHeaterCheck(0);
            }
            list.set(i,deviceChild);
        }
        return list;
    }

    /**
     * 获取家中的房间下的类型设备为9的的受控设备
     *
     * @param houseId
     * @param roomId
     * @return
     */
    public List<DeviceChild> findHeaderControlDevice(long houseId, long roomId) {
        WhereCondition whereCondition = deviceChildDao.queryBuilder().
                and(DeviceChildDao.Properties.HouseId.eq(houseId),
                        DeviceChildDao.Properties.RoomId.eq(roomId),
                        DeviceChildDao.Properties.Type.eq(9),
                        DeviceChildDao.Properties.HeaterControl.eq(3),////heaterControl为2为主控设备，3为受控设备，1为普通设备
                        DeviceChildDao.Properties.Share.isNull());
        return deviceChildDao.queryBuilder().where(whereCondition).list();
    }

}
