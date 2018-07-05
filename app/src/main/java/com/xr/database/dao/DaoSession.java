package com.xr.database.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.jia.pojo.TimeTask;
import com.xr.happyFamily.le.pojo.Time;

import com.xr.database.dao.DeviceChildDao;
import com.xr.database.dao.HourseDao;
import com.xr.database.dao.RoomDao;
import com.xr.database.dao.TimeTaskDao;
import com.xr.database.dao.TimeDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig deviceChildDaoConfig;
    private final DaoConfig hourseDaoConfig;
    private final DaoConfig roomDaoConfig;
    private final DaoConfig timeTaskDaoConfig;
    private final DaoConfig timeDaoConfig;

    private final DeviceChildDao deviceChildDao;
    private final HourseDao hourseDao;
    private final RoomDao roomDao;
    private final TimeTaskDao timeTaskDao;
    private final TimeDao timeDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        deviceChildDaoConfig = daoConfigMap.get(DeviceChildDao.class).clone();
        deviceChildDaoConfig.initIdentityScope(type);

        hourseDaoConfig = daoConfigMap.get(HourseDao.class).clone();
        hourseDaoConfig.initIdentityScope(type);

        roomDaoConfig = daoConfigMap.get(RoomDao.class).clone();
        roomDaoConfig.initIdentityScope(type);

        timeTaskDaoConfig = daoConfigMap.get(TimeTaskDao.class).clone();
        timeTaskDaoConfig.initIdentityScope(type);

        timeDaoConfig = daoConfigMap.get(TimeDao.class).clone();
        timeDaoConfig.initIdentityScope(type);

        deviceChildDao = new DeviceChildDao(deviceChildDaoConfig, this);
        hourseDao = new HourseDao(hourseDaoConfig, this);
        roomDao = new RoomDao(roomDaoConfig, this);
        timeTaskDao = new TimeTaskDao(timeTaskDaoConfig, this);
        timeDao = new TimeDao(timeDaoConfig, this);

        registerDao(DeviceChild.class, deviceChildDao);
        registerDao(Hourse.class, hourseDao);
        registerDao(Room.class, roomDao);
        registerDao(TimeTask.class, timeTaskDao);
        registerDao(Time.class, timeDao);
    }
    
    public void clear() {
        deviceChildDaoConfig.clearIdentityScope();
        hourseDaoConfig.clearIdentityScope();
        roomDaoConfig.clearIdentityScope();
        timeTaskDaoConfig.clearIdentityScope();
        timeDaoConfig.clearIdentityScope();
    }

    public DeviceChildDao getDeviceChildDao() {
        return deviceChildDao;
    }

    public HourseDao getHourseDao() {
        return hourseDao;
    }

    public RoomDao getRoomDao() {
        return roomDao;
    }

    public TimeTaskDao getTimeTaskDao() {
        return timeTaskDao;
    }

    public TimeDao getTimeDao() {
        return timeDao;
    }

}
