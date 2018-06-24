package com.xr.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.xr.happyFamily.jia.pojo.DeviceChild;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DEVICE_CHILD".
*/
public class DeviceChildDao extends AbstractDao<DeviceChild, Long> {

    public static final String TABLENAME = "DEVICE_CHILD";

    /**
     * Properties of entity DeviceChild.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property HouseId = new Property(1, long.class, "houseId", false, "HOUSE_ID");
        public final static Property RoomId = new Property(2, long.class, "roomId", false, "ROOM_ID");
        public final static Property DeviceUsedCount = new Property(3, int.class, "deviceUsedCount", false, "DEVICE_USED_COUNT");
        public final static Property Type = new Property(4, int.class, "type", false, "TYPE");
        public final static Property BusModel = new Property(5, int.class, "busModel", false, "BUS_MODEL");
        public final static Property MacAddress = new Property(6, String.class, "macAddress", false, "MAC_ADDRESS");
        public final static Property Name = new Property(7, String.class, "name", false, "NAME");
        public final static Property TimerMoudle = new Property(8, int.class, "timerMoudle", false, "TIMER_MOUDLE");
        public final static Property McuVersion = new Property(9, String.class, "mcuVersion", false, "MCU_VERSION");
        public final static Property WifiVersion = new Property(10, String.class, "wifiVersion", false, "WIFI_VERSION");
        public final static Property WaramerSetTemp = new Property(11, int.class, "waramerSetTemp", false, "WARAMER_SET_TEMP");
        public final static Property WarmerCurTemp = new Property(12, int.class, "warmerCurTemp", false, "WARMER_CUR_TEMP");
        public final static Property WarmerSampleData = new Property(13, int.class, "warmerSampleData", false, "WARMER_SAMPLE_DATA");
        public final static Property WarmerRatePower = new Property(14, int.class, "warmerRatePower", false, "WARMER_RATE_POWER");
        public final static Property WarmerCurRunRoatePower = new Property(15, int.class, "warmerCurRunRoatePower", false, "WARMER_CUR_RUN_ROATE_POWER");
        public final static Property WarmerRunState = new Property(16, int.class, "warmerRunState", false, "WARMER_RUN_STATE");
        public final static Property DeviceState = new Property(17, int.class, "deviceState", false, "DEVICE_STATE");
        public final static Property RateState = new Property(18, String.class, "rateState", false, "RATE_STATE");
        public final static Property LockState = new Property(19, int.class, "lockState", false, "LOCK_STATE");
        public final static Property ScreenState = new Property(20, int.class, "screenState", false, "SCREEN_STATE");
        public final static Property CurRunState2 = new Property(21, int.class, "curRunState2", false, "CUR_RUN_STATE2");
        public final static Property CurRunState3 = new Property(22, int.class, "curRunState3", false, "CUR_RUN_STATE3");
        public final static Property TimerHour = new Property(23, int.class, "timerHour", false, "TIMER_HOUR");
        public final static Property TimerMin = new Property(24, int.class, "timerMin", false, "TIMER_MIN");
        public final static Property CheckCode = new Property(25, int.class, "checkCode", false, "CHECK_CODE");
        public final static Property EndCode = new Property(26, int.class, "endCode", false, "END_CODE");
        public final static Property UserId = new Property(27, int.class, "userId", false, "USER_ID");
    }


    public DeviceChildDao(DaoConfig config) {
        super(config);
    }
    
    public DeviceChildDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DEVICE_CHILD\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"HOUSE_ID\" INTEGER NOT NULL ," + // 1: houseId
                "\"ROOM_ID\" INTEGER NOT NULL ," + // 2: roomId
                "\"DEVICE_USED_COUNT\" INTEGER NOT NULL ," + // 3: deviceUsedCount
                "\"TYPE\" INTEGER NOT NULL ," + // 4: type
                "\"BUS_MODEL\" INTEGER NOT NULL ," + // 5: busModel
                "\"MAC_ADDRESS\" TEXT," + // 6: macAddress
                "\"NAME\" TEXT," + // 7: name
                "\"TIMER_MOUDLE\" INTEGER NOT NULL ," + // 8: timerMoudle
                "\"MCU_VERSION\" TEXT," + // 9: mcuVersion
                "\"WIFI_VERSION\" TEXT," + // 10: wifiVersion
                "\"WARAMER_SET_TEMP\" INTEGER NOT NULL ," + // 11: waramerSetTemp
                "\"WARMER_CUR_TEMP\" INTEGER NOT NULL ," + // 12: warmerCurTemp
                "\"WARMER_SAMPLE_DATA\" INTEGER NOT NULL ," + // 13: warmerSampleData
                "\"WARMER_RATE_POWER\" INTEGER NOT NULL ," + // 14: warmerRatePower
                "\"WARMER_CUR_RUN_ROATE_POWER\" INTEGER NOT NULL ," + // 15: warmerCurRunRoatePower
                "\"WARMER_RUN_STATE\" INTEGER NOT NULL ," + // 16: warmerRunState
                "\"DEVICE_STATE\" INTEGER NOT NULL ," + // 17: deviceState
                "\"RATE_STATE\" TEXT," + // 18: rateState
                "\"LOCK_STATE\" INTEGER NOT NULL ," + // 19: lockState
                "\"SCREEN_STATE\" INTEGER NOT NULL ," + // 20: screenState
                "\"CUR_RUN_STATE2\" INTEGER NOT NULL ," + // 21: curRunState2
                "\"CUR_RUN_STATE3\" INTEGER NOT NULL ," + // 22: curRunState3
                "\"TIMER_HOUR\" INTEGER NOT NULL ," + // 23: timerHour
                "\"TIMER_MIN\" INTEGER NOT NULL ," + // 24: timerMin
                "\"CHECK_CODE\" INTEGER NOT NULL ," + // 25: checkCode
                "\"END_CODE\" INTEGER NOT NULL ," + // 26: endCode
                "\"USER_ID\" INTEGER NOT NULL );"); // 27: userId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DEVICE_CHILD\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DeviceChild entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getHouseId());
        stmt.bindLong(3, entity.getRoomId());
        stmt.bindLong(4, entity.getDeviceUsedCount());
        stmt.bindLong(5, entity.getType());
        stmt.bindLong(6, entity.getBusModel());
 
        String macAddress = entity.getMacAddress();
        if (macAddress != null) {
            stmt.bindString(7, macAddress);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(8, name);
        }
        stmt.bindLong(9, entity.getTimerMoudle());
 
        String mcuVersion = entity.getMcuVersion();
        if (mcuVersion != null) {
            stmt.bindString(10, mcuVersion);
        }
 
        String wifiVersion = entity.getWifiVersion();
        if (wifiVersion != null) {
            stmt.bindString(11, wifiVersion);
        }
        stmt.bindLong(12, entity.getWaramerSetTemp());
        stmt.bindLong(13, entity.getWarmerCurTemp());
        stmt.bindLong(14, entity.getWarmerSampleData());
        stmt.bindLong(15, entity.getWarmerRatePower());
        stmt.bindLong(16, entity.getWarmerCurRunRoatePower());
        stmt.bindLong(17, entity.getWarmerRunState());
        stmt.bindLong(18, entity.getDeviceState());
 
        String rateState = entity.getRateState();
        if (rateState != null) {
            stmt.bindString(19, rateState);
        }
        stmt.bindLong(20, entity.getLockState());
        stmt.bindLong(21, entity.getScreenState());
        stmt.bindLong(22, entity.getCurRunState2());
        stmt.bindLong(23, entity.getCurRunState3());
        stmt.bindLong(24, entity.getTimerHour());
        stmt.bindLong(25, entity.getTimerMin());
        stmt.bindLong(26, entity.getCheckCode());
        stmt.bindLong(27, entity.getEndCode());
        stmt.bindLong(28, entity.getUserId());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DeviceChild entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getHouseId());
        stmt.bindLong(3, entity.getRoomId());
        stmt.bindLong(4, entity.getDeviceUsedCount());
        stmt.bindLong(5, entity.getType());
        stmt.bindLong(6, entity.getBusModel());
 
        String macAddress = entity.getMacAddress();
        if (macAddress != null) {
            stmt.bindString(7, macAddress);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(8, name);
        }
        stmt.bindLong(9, entity.getTimerMoudle());
 
        String mcuVersion = entity.getMcuVersion();
        if (mcuVersion != null) {
            stmt.bindString(10, mcuVersion);
        }
 
        String wifiVersion = entity.getWifiVersion();
        if (wifiVersion != null) {
            stmt.bindString(11, wifiVersion);
        }
        stmt.bindLong(12, entity.getWaramerSetTemp());
        stmt.bindLong(13, entity.getWarmerCurTemp());
        stmt.bindLong(14, entity.getWarmerSampleData());
        stmt.bindLong(15, entity.getWarmerRatePower());
        stmt.bindLong(16, entity.getWarmerCurRunRoatePower());
        stmt.bindLong(17, entity.getWarmerRunState());
        stmt.bindLong(18, entity.getDeviceState());
 
        String rateState = entity.getRateState();
        if (rateState != null) {
            stmt.bindString(19, rateState);
        }
        stmt.bindLong(20, entity.getLockState());
        stmt.bindLong(21, entity.getScreenState());
        stmt.bindLong(22, entity.getCurRunState2());
        stmt.bindLong(23, entity.getCurRunState3());
        stmt.bindLong(24, entity.getTimerHour());
        stmt.bindLong(25, entity.getTimerMin());
        stmt.bindLong(26, entity.getCheckCode());
        stmt.bindLong(27, entity.getEndCode());
        stmt.bindLong(28, entity.getUserId());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public DeviceChild readEntity(Cursor cursor, int offset) {
        DeviceChild entity = new DeviceChild( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // houseId
            cursor.getLong(offset + 2), // roomId
            cursor.getInt(offset + 3), // deviceUsedCount
            cursor.getInt(offset + 4), // type
            cursor.getInt(offset + 5), // busModel
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // macAddress
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // name
            cursor.getInt(offset + 8), // timerMoudle
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // mcuVersion
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // wifiVersion
            cursor.getInt(offset + 11), // waramerSetTemp
            cursor.getInt(offset + 12), // warmerCurTemp
            cursor.getInt(offset + 13), // warmerSampleData
            cursor.getInt(offset + 14), // warmerRatePower
            cursor.getInt(offset + 15), // warmerCurRunRoatePower
            cursor.getInt(offset + 16), // warmerRunState
            cursor.getInt(offset + 17), // deviceState
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // rateState
            cursor.getInt(offset + 19), // lockState
            cursor.getInt(offset + 20), // screenState
            cursor.getInt(offset + 21), // curRunState2
            cursor.getInt(offset + 22), // curRunState3
            cursor.getInt(offset + 23), // timerHour
            cursor.getInt(offset + 24), // timerMin
            cursor.getInt(offset + 25), // checkCode
            cursor.getInt(offset + 26), // endCode
            cursor.getInt(offset + 27) // userId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DeviceChild entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setHouseId(cursor.getLong(offset + 1));
        entity.setRoomId(cursor.getLong(offset + 2));
        entity.setDeviceUsedCount(cursor.getInt(offset + 3));
        entity.setType(cursor.getInt(offset + 4));
        entity.setBusModel(cursor.getInt(offset + 5));
        entity.setMacAddress(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setName(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setTimerMoudle(cursor.getInt(offset + 8));
        entity.setMcuVersion(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setWifiVersion(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setWaramerSetTemp(cursor.getInt(offset + 11));
        entity.setWarmerCurTemp(cursor.getInt(offset + 12));
        entity.setWarmerSampleData(cursor.getInt(offset + 13));
        entity.setWarmerRatePower(cursor.getInt(offset + 14));
        entity.setWarmerCurRunRoatePower(cursor.getInt(offset + 15));
        entity.setWarmerRunState(cursor.getInt(offset + 16));
        entity.setDeviceState(cursor.getInt(offset + 17));
        entity.setRateState(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setLockState(cursor.getInt(offset + 19));
        entity.setScreenState(cursor.getInt(offset + 20));
        entity.setCurRunState2(cursor.getInt(offset + 21));
        entity.setCurRunState3(cursor.getInt(offset + 22));
        entity.setTimerHour(cursor.getInt(offset + 23));
        entity.setTimerMin(cursor.getInt(offset + 24));
        entity.setCheckCode(cursor.getInt(offset + 25));
        entity.setEndCode(cursor.getInt(offset + 26));
        entity.setUserId(cursor.getInt(offset + 27));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(DeviceChild entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(DeviceChild entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(DeviceChild entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
