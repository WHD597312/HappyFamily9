package com.xr.database.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.xr.happyFamily.jia.pojo.TimeTask;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.Hourse;

import com.xr.database.dao.TimeTaskDao;
import com.xr.database.dao.RoomDao;
import com.xr.database.dao.DeviceChildDao;
import com.xr.database.dao.HourseDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig timeTaskDaoConfig;
    private final DaoConfig roomDaoConfig;
    private final DaoConfig deviceChildDaoConfig;
    private final DaoConfig hourseDaoConfig;

    private final TimeTaskDao timeTaskDao;
    private final RoomDao roomDao;
    private final DeviceChildDao deviceChildDao;
    private final HourseDao hourseDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        timeTaskDaoConfig = daoConfigMap.get(TimeTaskDao.class).clone();
        timeTaskDaoConfig.initIdentityScope(type);

        roomDaoConfig = daoConfigMap.get(RoomDao.class).clone();
        roomDaoConfig.initIdentityScope(type);

        deviceChildDaoConfig = daoConfigMap.get(DeviceChildDao.class).clone();
        deviceChildDaoConfig.initIdentityScope(type);

        hourseDaoConfig = daoConfigMap.get(HourseDao.class).clone();
        hourseDaoConfig.initIdentityScope(type);

        timeTaskDao = new TimeTaskDao(timeTaskDaoConfig, this);
        roomDao = new RoomDao(roomDaoConfig, this);
        deviceChildDao = new DeviceChildDao(deviceChildDaoConfig, this);
        hourseDao = new HourseDao(hourseDaoConfig, this);

        registerDao(TimeTask.class, timeTaskDao);
        registerDao(Room.class, roomDao);
        registerDao(DeviceChild.class, deviceChildDao);
        registerDao(Hourse.class, hourseDao);
    }
    
    public void clear() {
        timeTaskDaoConfig.clearIdentityScope();
        roomDaoConfig.clearIdentityScope();
        deviceChildDaoConfig.clearIdentityScope();
        hourseDaoConfig.clearIdentityScope();
    }

    public TimeTaskDao getTimeTaskDao() {
        return timeTaskDao;
    }

    public RoomDao getRoomDao() {
        return roomDao;
    }

    public DeviceChildDao getDeviceChildDao() {
        return deviceChildDao;
    }

    public HourseDao getHourseDao() {
        return hourseDao;
    }

}
