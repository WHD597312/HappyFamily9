package com.xr.database.dao.daoimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xr.database.dao.DBManager;
import com.xr.database.dao.DaoMaster;
import com.xr.database.dao.DaoSession;
import com.xr.database.dao.HourseDao;
import com.xr.database.dao.RoomDao;
import com.xr.happyFamily.jia.pojo.Room;

import java.util.List;


public class RoomDaoImpl {
    private Context context;
    private SQLiteDatabase db;
    private DaoMaster master;
    private RoomDao roomDao;
    private DaoSession session;
    public RoomDaoImpl(Context context) {
        this.context = context;
        db= DBManager.getInstance(context).getWritableDasebase();
        master=new DaoMaster(db);
        session=master.newSession();
        roomDao = session.getRoomDao();

    }

    /**
     * 添加设备
     * @param room
     */
    public void insert(Room room){
        roomDao.insert(room);
    }

    /**
     * 删除设备
     * @param room
     */
    public void delete(Room room){
        roomDao.delete(room);
    }

    /**
     * 更新设备
     * @param room
     */
    public void update(Room room){
        roomDao.update(room);
    }

    public Room findById(long roomId){
        return roomDao.load(roomId);
    }
    public List<Room> findAllRoomInHouse(long houseId){
        List<Room> rooms=roomDao.queryBuilder().where(RoomDao.Properties.HouseId.eq(houseId)).orderAsc(RoomDao.Properties.RoomId).list();
        return rooms;
    }
    public List<Room> findByAllRoom(){
        return roomDao.loadAll();
    }
    public List<Room> findRoomByType(String roomType){
        return roomDao.queryBuilder().where(RoomDao.Properties.RoomType.eq(roomType)).orderDesc(RoomDao.Properties.RoomId).limit(4).list();
    }
    public void updateRooms(List<Room> rooms){
        roomDao.updateInTx(rooms);
    }
    public void  deleteAll(){
        roomDao.deleteAll();
    }
}
