package com.xr.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.xr.happyFamily.le.pojo.ClockBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CLOCK_BEAN".
*/
public class ClockBeanDao extends AbstractDao<ClockBean, Long> {

    public static final String TABLENAME = "CLOCK_BEAN";

    /**
     * Properties of entity ClockBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ClockId = new Property(1, int.class, "clockId", false, "CLOCK_ID");
        public final static Property ClockHour = new Property(2, int.class, "clockHour", false, "CLOCK_HOUR");
        public final static Property ClockMinute = new Property(3, int.class, "clockMinute", false, "CLOCK_MINUTE");
        public final static Property SumMinute = new Property(4, int.class, "sumMinute", false, "SUM_MINUTE");
        public final static Property ClockDay = new Property(5, String.class, "clockDay", false, "CLOCK_DAY");
        public final static Property Flag = new Property(6, String.class, "flag", false, "FLAG");
        public final static Property Music = new Property(7, String.class, "music", false, "MUSIC");
        public final static Property Switchs = new Property(8, int.class, "switchs", false, "SWITCHS");
        public final static Property ClockCreater = new Property(9, int.class, "clockCreater", false, "CLOCK_CREATER");
        public final static Property ClockType = new Property(10, int.class, "clockType", false, "CLOCK_TYPE");
        public final static Property CreaterName = new Property(11, String.class, "createrName", false, "CREATER_NAME");
        public final static Property CreateTime = new Property(12, long.class, "createTime", false, "CREATE_TIME");
    }


    public ClockBeanDao(DaoConfig config) {
        super(config);
    }
    
    public ClockBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CLOCK_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"CLOCK_ID\" INTEGER NOT NULL ," + // 1: clockId
                "\"CLOCK_HOUR\" INTEGER NOT NULL ," + // 2: clockHour
                "\"CLOCK_MINUTE\" INTEGER NOT NULL ," + // 3: clockMinute
                "\"SUM_MINUTE\" INTEGER NOT NULL ," + // 4: sumMinute
                "\"CLOCK_DAY\" TEXT," + // 5: clockDay
                "\"FLAG\" TEXT," + // 6: flag
                "\"MUSIC\" TEXT," + // 7: music
                "\"SWITCHS\" INTEGER NOT NULL ," + // 8: switchs
                "\"CLOCK_CREATER\" INTEGER NOT NULL ," + // 9: clockCreater
                "\"CLOCK_TYPE\" INTEGER NOT NULL ," + // 10: clockType
                "\"CREATER_NAME\" TEXT," + // 11: createrName
                "\"CREATE_TIME\" INTEGER NOT NULL );"); // 12: createTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CLOCK_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ClockBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getClockId());
        stmt.bindLong(3, entity.getClockHour());
        stmt.bindLong(4, entity.getClockMinute());
        stmt.bindLong(5, entity.getSumMinute());
 
        String clockDay = entity.getClockDay();
        if (clockDay != null) {
            stmt.bindString(6, clockDay);
        }
 
        String flag = entity.getFlag();
        if (flag != null) {
            stmt.bindString(7, flag);
        }
 
        String music = entity.getMusic();
        if (music != null) {
            stmt.bindString(8, music);
        }
        stmt.bindLong(9, entity.getSwitchs());
        stmt.bindLong(10, entity.getClockCreater());
        stmt.bindLong(11, entity.getClockType());
 
        String createrName = entity.getCreaterName();
        if (createrName != null) {
            stmt.bindString(12, createrName);
        }
        stmt.bindLong(13, entity.getCreateTime());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ClockBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getClockId());
        stmt.bindLong(3, entity.getClockHour());
        stmt.bindLong(4, entity.getClockMinute());
        stmt.bindLong(5, entity.getSumMinute());
 
        String clockDay = entity.getClockDay();
        if (clockDay != null) {
            stmt.bindString(6, clockDay);
        }
 
        String flag = entity.getFlag();
        if (flag != null) {
            stmt.bindString(7, flag);
        }
 
        String music = entity.getMusic();
        if (music != null) {
            stmt.bindString(8, music);
        }
        stmt.bindLong(9, entity.getSwitchs());
        stmt.bindLong(10, entity.getClockCreater());
        stmt.bindLong(11, entity.getClockType());
 
        String createrName = entity.getCreaterName();
        if (createrName != null) {
            stmt.bindString(12, createrName);
        }
        stmt.bindLong(13, entity.getCreateTime());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ClockBean readEntity(Cursor cursor, int offset) {
        ClockBean entity = new ClockBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // clockId
            cursor.getInt(offset + 2), // clockHour
            cursor.getInt(offset + 3), // clockMinute
            cursor.getInt(offset + 4), // sumMinute
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // clockDay
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // flag
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // music
            cursor.getInt(offset + 8), // switchs
            cursor.getInt(offset + 9), // clockCreater
            cursor.getInt(offset + 10), // clockType
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // createrName
            cursor.getLong(offset + 12) // createTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ClockBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setClockId(cursor.getInt(offset + 1));
        entity.setClockHour(cursor.getInt(offset + 2));
        entity.setClockMinute(cursor.getInt(offset + 3));
        entity.setSumMinute(cursor.getInt(offset + 4));
        entity.setClockDay(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setFlag(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setMusic(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setSwitchs(cursor.getInt(offset + 8));
        entity.setClockCreater(cursor.getInt(offset + 9));
        entity.setClockType(cursor.getInt(offset + 10));
        entity.setCreaterName(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setCreateTime(cursor.getLong(offset + 12));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ClockBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ClockBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ClockBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
