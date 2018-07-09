package com.xr.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.xr.happyFamily.le.pojo.Time;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TIME".
*/
public class TimeDao extends AbstractDao<Time, Long> {

    public static final String TABLENAME = "TIME";

    /**
     * Properties of entity Time.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "Id", true, "_id");
        public final static Property Hour = new Property(1, int.class, "hour", false, "HOUR");
        public final static Property Minutes = new Property(2, int.class, "minutes", false, "MINUTES");
        public final static Property Day = new Property(3, String.class, "day", false, "DAY");
        public final static Property Lable = new Property(4, String.class, "lable", false, "LABLE");
        public final static Property Style = new Property(5, String.class, "Style", false, "STYLE");
        public final static Property Flag = new Property(6, int.class, "flag", false, "FLAG");
        public final static Property Open = new Property(7, boolean.class, "open", false, "OPEN");
    }


    public TimeDao(DaoConfig config) {
        super(config);
    }
    
    public TimeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TIME\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: Id
                "\"HOUR\" INTEGER NOT NULL ," + // 1: hour
                "\"MINUTES\" INTEGER NOT NULL ," + // 2: minutes
                "\"DAY\" TEXT," + // 3: day
                "\"LABLE\" TEXT," + // 4: lable
                "\"STYLE\" TEXT," + // 5: Style
                "\"FLAG\" INTEGER NOT NULL ," + // 6: flag
                "\"OPEN\" INTEGER NOT NULL );"); // 7: open
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TIME\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Time entity) {
        stmt.clearBindings();
 
        Long Id = entity.getId();
        if (Id != null) {
            stmt.bindLong(1, Id);
        }
        stmt.bindLong(2, entity.getHour());
        stmt.bindLong(3, entity.getMinutes());
 
        String day = entity.getDay();
        if (day != null) {
            stmt.bindString(4, day);
        }
 
        String lable = entity.getLable();
        if (lable != null) {
            stmt.bindString(5, lable);
        }
 
        String Style = entity.getStyle();
        if (Style != null) {
            stmt.bindString(6, Style);
        }
        stmt.bindLong(7, entity.getFlag());
        stmt.bindLong(8, entity.getOpen() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Time entity) {
        stmt.clearBindings();
 
        Long Id = entity.getId();
        if (Id != null) {
            stmt.bindLong(1, Id);
        }
        stmt.bindLong(2, entity.getHour());
        stmt.bindLong(3, entity.getMinutes());
 
        String day = entity.getDay();
        if (day != null) {
            stmt.bindString(4, day);
        }
 
        String lable = entity.getLable();
        if (lable != null) {
            stmt.bindString(5, lable);
        }
 
        String Style = entity.getStyle();
        if (Style != null) {
            stmt.bindString(6, Style);
        }
        stmt.bindLong(7, entity.getFlag());
        stmt.bindLong(8, entity.getOpen() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Time readEntity(Cursor cursor, int offset) {
        Time entity = new Time( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // Id
            cursor.getInt(offset + 1), // hour
            cursor.getInt(offset + 2), // minutes
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // day
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // lable
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // Style
            cursor.getInt(offset + 6), // flag
            cursor.getShort(offset + 7) != 0 // open
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Time entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setHour(cursor.getInt(offset + 1));
        entity.setMinutes(cursor.getInt(offset + 2));
        entity.setDay(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setLable(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setStyle(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setFlag(cursor.getInt(offset + 6));
        entity.setOpen(cursor.getShort(offset + 7) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Time entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Time entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Time entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}