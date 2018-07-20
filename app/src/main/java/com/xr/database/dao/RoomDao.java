package com.xr.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.xr.happyFamily.jia.pojo.Room;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ROOM".
*/
public class RoomDao extends AbstractDao<Room, Long> {

    public static final String TABLENAME = "ROOM";

    /**
     * Properties of entity Room.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property RoomId = new Property(0, Long.class, "roomId", true, "_id");
        public final static Property RoomName = new Property(1, String.class, "roomName", false, "ROOM_NAME");
        public final static Property HouseId = new Property(2, int.class, "houseId", false, "HOUSE_ID");
        public final static Property RoomType = new Property(3, String.class, "roomType", false, "ROOM_TYPE");
        public final static Property ImgId = new Property(4, int.class, "imgId", false, "IMG_ID");
        public final static Property ImgAddress = new Property(5, String.class, "imgAddress", false, "IMG_ADDRESS");
    }


    public RoomDao(DaoConfig config) {
        super(config);
    }
    
    public RoomDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ROOM\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: roomId
                "\"ROOM_NAME\" TEXT," + // 1: roomName
                "\"HOUSE_ID\" INTEGER NOT NULL ," + // 2: houseId
                "\"ROOM_TYPE\" TEXT," + // 3: roomType
                "\"IMG_ID\" INTEGER NOT NULL ," + // 4: imgId
                "\"IMG_ADDRESS\" TEXT);"); // 5: imgAddress
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ROOM\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Room entity) {
        stmt.clearBindings();
 
        Long roomId = entity.getRoomId();
        if (roomId != null) {
            stmt.bindLong(1, roomId);
        }
 
        String roomName = entity.getRoomName();
        if (roomName != null) {
            stmt.bindString(2, roomName);
        }
        stmt.bindLong(3, entity.getHouseId());
 
        String roomType = entity.getRoomType();
        if (roomType != null) {
            stmt.bindString(4, roomType);
        }
        stmt.bindLong(5, entity.getImgId());
 
        String imgAddress = entity.getImgAddress();
        if (imgAddress != null) {
            stmt.bindString(6, imgAddress);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Room entity) {
        stmt.clearBindings();
 
        Long roomId = entity.getRoomId();
        if (roomId != null) {
            stmt.bindLong(1, roomId);
        }
 
        String roomName = entity.getRoomName();
        if (roomName != null) {
            stmt.bindString(2, roomName);
        }
        stmt.bindLong(3, entity.getHouseId());
 
        String roomType = entity.getRoomType();
        if (roomType != null) {
            stmt.bindString(4, roomType);
        }
        stmt.bindLong(5, entity.getImgId());
 
        String imgAddress = entity.getImgAddress();
        if (imgAddress != null) {
            stmt.bindString(6, imgAddress);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Room readEntity(Cursor cursor, int offset) {
        Room entity = new Room( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // roomId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // roomName
            cursor.getInt(offset + 2), // houseId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // roomType
            cursor.getInt(offset + 4), // imgId
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // imgAddress
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Room entity, int offset) {
        entity.setRoomId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setRoomName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setHouseId(cursor.getInt(offset + 2));
        entity.setRoomType(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setImgId(cursor.getInt(offset + 4));
        entity.setImgAddress(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Room entity, long rowId) {
        entity.setRoomId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Room entity) {
        if(entity != null) {
            return entity.getRoomId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Room entity) {
        return entity.getRoomId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
