package com.xr.database.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.xr.happyFamily.jia.pojo.TimeTask;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.Time;
import com.xr.happyFamily.le.pojo.UserInfo;
import com.xr.happyFamily.le.pojo.FriendData;
import com.xr.happyFamily.le.pojo.MsgData;

import com.xr.database.dao.TimeTaskDao;
import com.xr.database.dao.DeviceChildDao;
import com.xr.database.dao.RoomDao;
import com.xr.database.dao.HourseDao;
import com.xr.database.dao.ClockBeanDao;
import com.xr.database.dao.TimeDao;
import com.xr.database.dao.UserInfoDao;
import com.xr.database.dao.FriendDataDao;
import com.xr.database.dao.MsgDataDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig timeTaskDaoConfig;
    private final DaoConfig deviceChildDaoConfig;
    private final DaoConfig roomDaoConfig;
    private final DaoConfig hourseDaoConfig;
    private final DaoConfig clockBeanDaoConfig;
    private final DaoConfig timeDaoConfig;
    private final DaoConfig userInfoDaoConfig;
    private final DaoConfig friendDataDaoConfig;
    private final DaoConfig msgDataDaoConfig;

    private final TimeTaskDao timeTaskDao;
    private final DeviceChildDao deviceChildDao;
    private final RoomDao roomDao;
    private final HourseDao hourseDao;
    private final ClockBeanDao clockBeanDao;
    private final TimeDao timeDao;
    private final UserInfoDao userInfoDao;
    private final FriendDataDao friendDataDao;
    private final MsgDataDao msgDataDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        timeTaskDaoConfig = daoConfigMap.get(TimeTaskDao.class).clone();
        timeTaskDaoConfig.initIdentityScope(type);

        deviceChildDaoConfig = daoConfigMap.get(DeviceChildDao.class).clone();
        deviceChildDaoConfig.initIdentityScope(type);

        roomDaoConfig = daoConfigMap.get(RoomDao.class).clone();
        roomDaoConfig.initIdentityScope(type);

        hourseDaoConfig = daoConfigMap.get(HourseDao.class).clone();
        hourseDaoConfig.initIdentityScope(type);

        clockBeanDaoConfig = daoConfigMap.get(ClockBeanDao.class).clone();
        clockBeanDaoConfig.initIdentityScope(type);

        timeDaoConfig = daoConfigMap.get(TimeDao.class).clone();
        timeDaoConfig.initIdentityScope(type);

        userInfoDaoConfig = daoConfigMap.get(UserInfoDao.class).clone();
        userInfoDaoConfig.initIdentityScope(type);

        friendDataDaoConfig = daoConfigMap.get(FriendDataDao.class).clone();
        friendDataDaoConfig.initIdentityScope(type);

        msgDataDaoConfig = daoConfigMap.get(MsgDataDao.class).clone();
        msgDataDaoConfig.initIdentityScope(type);

        timeTaskDao = new TimeTaskDao(timeTaskDaoConfig, this);
        deviceChildDao = new DeviceChildDao(deviceChildDaoConfig, this);
        roomDao = new RoomDao(roomDaoConfig, this);
        hourseDao = new HourseDao(hourseDaoConfig, this);
        clockBeanDao = new ClockBeanDao(clockBeanDaoConfig, this);
        timeDao = new TimeDao(timeDaoConfig, this);
        userInfoDao = new UserInfoDao(userInfoDaoConfig, this);
        friendDataDao = new FriendDataDao(friendDataDaoConfig, this);
        msgDataDao = new MsgDataDao(msgDataDaoConfig, this);

        registerDao(TimeTask.class, timeTaskDao);
        registerDao(DeviceChild.class, deviceChildDao);
        registerDao(Room.class, roomDao);
        registerDao(Hourse.class, hourseDao);
        registerDao(ClockBean.class, clockBeanDao);
        registerDao(Time.class, timeDao);
        registerDao(UserInfo.class, userInfoDao);
        registerDao(FriendData.class, friendDataDao);
        registerDao(MsgData.class, msgDataDao);
    }
    
    public void clear() {
        timeTaskDaoConfig.clearIdentityScope();
        deviceChildDaoConfig.clearIdentityScope();
        roomDaoConfig.clearIdentityScope();
        hourseDaoConfig.clearIdentityScope();
        clockBeanDaoConfig.clearIdentityScope();
        timeDaoConfig.clearIdentityScope();
        userInfoDaoConfig.clearIdentityScope();
        friendDataDaoConfig.clearIdentityScope();
        msgDataDaoConfig.clearIdentityScope();
    }

    public TimeTaskDao getTimeTaskDao() {
        return timeTaskDao;
    }

    public DeviceChildDao getDeviceChildDao() {
        return deviceChildDao;
    }

    public RoomDao getRoomDao() {
        return roomDao;
    }

    public HourseDao getHourseDao() {
        return hourseDao;
    }

    public ClockBeanDao getClockBeanDao() {
        return clockBeanDao;
    }

    public TimeDao getTimeDao() {
        return timeDao;
    }

    public UserInfoDao getUserInfoDao() {
        return userInfoDao;
    }

    public FriendDataDao getFriendDataDao() {
        return friendDataDao;
    }

    public MsgDataDao getMsgDataDao() {
        return msgDataDao;
    }

}
