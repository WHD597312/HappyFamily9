package com.xr.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.xr.happyFamily.le.pojo.DerailBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DERAIL_BEAN".
*/
public class DerailBeanDao extends AbstractDao<DerailBean, Long> {

    public static final String TABLENAME = "DERAIL_BEAN";

    /**
     * Properties of entity DerailBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property DerailName = new Property(1, String.class, "derailName", false, "DERAIL_NAME");
        public final static Property CreateTime = new Property(2, long.class, "createTime", false, "CREATE_TIME");
        public final static Property DerailId = new Property(3, int.class, "derailId", false, "DERAIL_ID");
    }


    public DerailBeanDao(DaoConfig config) {
        super(config);
    }
    
    public DerailBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DERAIL_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"DERAIL_NAME\" TEXT," + // 1: derailName
                "\"CREATE_TIME\" INTEGER NOT NULL ," + // 2: createTime
                "\"DERAIL_ID\" INTEGER NOT NULL );"); // 3: derailId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DERAIL_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DerailBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String derailName = entity.getDerailName();
        if (derailName != null) {
            stmt.bindString(2, derailName);
        }
        stmt.bindLong(3, entity.getCreateTime());
        stmt.bindLong(4, entity.getDerailId());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DerailBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String derailName = entity.getDerailName();
        if (derailName != null) {
            stmt.bindString(2, derailName);
        }
        stmt.bindLong(3, entity.getCreateTime());
        stmt.bindLong(4, entity.getDerailId());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public DerailBean readEntity(Cursor cursor, int offset) {
        DerailBean entity = new DerailBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // derailName
            cursor.getLong(offset + 2), // createTime
            cursor.getInt(offset + 3) // derailId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DerailBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDerailName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCreateTime(cursor.getLong(offset + 2));
        entity.setDerailId(cursor.getInt(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(DerailBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(DerailBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(DerailBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}