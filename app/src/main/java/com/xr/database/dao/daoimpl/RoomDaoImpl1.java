package com.xr.database.dao.daoimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xr.database.dao.DBManager;
import com.xr.database.dao.DaoMaster;
import com.xr.database.dao.DaoSession;
import com.xr.database.dao.Room1Dao;
import com.xr.happyFamily.jia.pojo.Room1;

import java.util.List;


public class RoomDaoImpl1 {
    private Context context;
    private SQLiteDatabase db1;
    private DaoMaster master;
    private Room1Dao room1Dao;
    private DaoSession session;
    public RoomDaoImpl1(Context context) {
        this.context = context;
        db1= DBManager.getInstance(context).getWritableDasebase();
        master=new DaoMaster(db1);
        session=master.newSession();
        room1Dao = session.getRoom1Dao();

    }

    /**
     * 添加设备
     * @param room
     */
    public void insert(Room1 room){
        room1Dao.insert(room);
    }

    /**
     * 删除设备
     * @param room
     */
    public void delete(Room1 room){
        room1Dao.delete(room);
    }

    /**
     * 更新设备
     * @param room
     */
    public void update(Room1 room){
        room1Dao.update(room);
    }

   public Room1 findById(long roomId){
        return room1Dao.load(roomId);
   }
    public List<Room1> findByAllRoom(){
        return room1Dao.loadAll();
    }
}
