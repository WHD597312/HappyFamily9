package com.xr.database.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.xr.happyFamily.bao.pojo.LeImage;
import com.xr.happyFamily.bao.pojo.ShopBanner;
import com.xr.happyFamily.bao.pojo.ShopList;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.jia.pojo.TimeTask;
import com.xr.happyFamily.le.pojo.AppUsing;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.DerailBean;
import com.xr.happyFamily.le.pojo.DerailResult;
import com.xr.happyFamily.le.pojo.FriendData;
import com.xr.happyFamily.le.pojo.MsgData;
import com.xr.happyFamily.le.pojo.Time;
import com.xr.happyFamily.le.pojo.UserInfo;

import com.xr.database.dao.LeImageDao;
import com.xr.database.dao.ShopBannerDao;
import com.xr.database.dao.ShopListDao;
import com.xr.database.dao.DeviceChildDao;
import com.xr.database.dao.HourseDao;
import com.xr.database.dao.RoomDao;
import com.xr.database.dao.TimeTaskDao;
import com.xr.database.dao.AppUsingDao;
import com.xr.database.dao.ClockBeanDao;
import com.xr.database.dao.DerailBeanDao;
import com.xr.database.dao.DerailResultDao;
import com.xr.database.dao.FriendDataDao;
import com.xr.database.dao.MsgDataDao;
import com.xr.database.dao.TimeDao;
import com.xr.database.dao.UserInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig leImageDaoConfig;
    private final DaoConfig shopBannerDaoConfig;
    private final DaoConfig shopListDaoConfig;
    private final DaoConfig deviceChildDaoConfig;
    private final DaoConfig hourseDaoConfig;
    private final DaoConfig roomDaoConfig;
    private final DaoConfig timeTaskDaoConfig;
    private final DaoConfig appUsingDaoConfig;
    private final DaoConfig clockBeanDaoConfig;
    private final DaoConfig derailBeanDaoConfig;
    private final DaoConfig derailResultDaoConfig;
    private final DaoConfig friendDataDaoConfig;
    private final DaoConfig msgDataDaoConfig;
    private final DaoConfig timeDaoConfig;
    private final DaoConfig userInfoDaoConfig;

    private final LeImageDao leImageDao;
    private final ShopBannerDao shopBannerDao;
    private final ShopListDao shopListDao;
    private final DeviceChildDao deviceChildDao;
    private final HourseDao hourseDao;
    private final RoomDao roomDao;
    private final TimeTaskDao timeTaskDao;
    private final AppUsingDao appUsingDao;
    private final ClockBeanDao clockBeanDao;
    private final DerailBeanDao derailBeanDao;
    private final DerailResultDao derailResultDao;
    private final FriendDataDao friendDataDao;
    private final MsgDataDao msgDataDao;
    private final TimeDao timeDao;
    private final UserInfoDao userInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        leImageDaoConfig = daoConfigMap.get(LeImageDao.class).clone();
        leImageDaoConfig.initIdentityScope(type);

        shopBannerDaoConfig = daoConfigMap.get(ShopBannerDao.class).clone();
        shopBannerDaoConfig.initIdentityScope(type);

        shopListDaoConfig = daoConfigMap.get(ShopListDao.class).clone();
        shopListDaoConfig.initIdentityScope(type);

        deviceChildDaoConfig = daoConfigMap.get(DeviceChildDao.class).clone();
        deviceChildDaoConfig.initIdentityScope(type);

        hourseDaoConfig = daoConfigMap.get(HourseDao.class).clone();
        hourseDaoConfig.initIdentityScope(type);

        roomDaoConfig = daoConfigMap.get(RoomDao.class).clone();
        roomDaoConfig.initIdentityScope(type);

        timeTaskDaoConfig = daoConfigMap.get(TimeTaskDao.class).clone();
        timeTaskDaoConfig.initIdentityScope(type);

        appUsingDaoConfig = daoConfigMap.get(AppUsingDao.class).clone();
        appUsingDaoConfig.initIdentityScope(type);

        clockBeanDaoConfig = daoConfigMap.get(ClockBeanDao.class).clone();
        clockBeanDaoConfig.initIdentityScope(type);

        derailBeanDaoConfig = daoConfigMap.get(DerailBeanDao.class).clone();
        derailBeanDaoConfig.initIdentityScope(type);

        derailResultDaoConfig = daoConfigMap.get(DerailResultDao.class).clone();
        derailResultDaoConfig.initIdentityScope(type);

        friendDataDaoConfig = daoConfigMap.get(FriendDataDao.class).clone();
        friendDataDaoConfig.initIdentityScope(type);

        msgDataDaoConfig = daoConfigMap.get(MsgDataDao.class).clone();
        msgDataDaoConfig.initIdentityScope(type);

        timeDaoConfig = daoConfigMap.get(TimeDao.class).clone();
        timeDaoConfig.initIdentityScope(type);

        userInfoDaoConfig = daoConfigMap.get(UserInfoDao.class).clone();
        userInfoDaoConfig.initIdentityScope(type);

        leImageDao = new LeImageDao(leImageDaoConfig, this);
        shopBannerDao = new ShopBannerDao(shopBannerDaoConfig, this);
        shopListDao = new ShopListDao(shopListDaoConfig, this);
        deviceChildDao = new DeviceChildDao(deviceChildDaoConfig, this);
        hourseDao = new HourseDao(hourseDaoConfig, this);
        roomDao = new RoomDao(roomDaoConfig, this);
        timeTaskDao = new TimeTaskDao(timeTaskDaoConfig, this);
        appUsingDao = new AppUsingDao(appUsingDaoConfig, this);
        clockBeanDao = new ClockBeanDao(clockBeanDaoConfig, this);
        derailBeanDao = new DerailBeanDao(derailBeanDaoConfig, this);
        derailResultDao = new DerailResultDao(derailResultDaoConfig, this);
        friendDataDao = new FriendDataDao(friendDataDaoConfig, this);
        msgDataDao = new MsgDataDao(msgDataDaoConfig, this);
        timeDao = new TimeDao(timeDaoConfig, this);
        userInfoDao = new UserInfoDao(userInfoDaoConfig, this);

        registerDao(LeImage.class, leImageDao);
        registerDao(ShopBanner.class, shopBannerDao);
        registerDao(ShopList.class, shopListDao);
        registerDao(DeviceChild.class, deviceChildDao);
        registerDao(Hourse.class, hourseDao);
        registerDao(Room.class, roomDao);
        registerDao(TimeTask.class, timeTaskDao);
        registerDao(AppUsing.class, appUsingDao);
        registerDao(ClockBean.class, clockBeanDao);
        registerDao(DerailBean.class, derailBeanDao);
        registerDao(DerailResult.class, derailResultDao);
        registerDao(FriendData.class, friendDataDao);
        registerDao(MsgData.class, msgDataDao);
        registerDao(Time.class, timeDao);
        registerDao(UserInfo.class, userInfoDao);
    }
    
    public void clear() {
        leImageDaoConfig.clearIdentityScope();
        shopBannerDaoConfig.clearIdentityScope();
        shopListDaoConfig.clearIdentityScope();
        deviceChildDaoConfig.clearIdentityScope();
        hourseDaoConfig.clearIdentityScope();
        roomDaoConfig.clearIdentityScope();
        timeTaskDaoConfig.clearIdentityScope();
        appUsingDaoConfig.clearIdentityScope();
        clockBeanDaoConfig.clearIdentityScope();
        derailBeanDaoConfig.clearIdentityScope();
        derailResultDaoConfig.clearIdentityScope();
        friendDataDaoConfig.clearIdentityScope();
        msgDataDaoConfig.clearIdentityScope();
        timeDaoConfig.clearIdentityScope();
        userInfoDaoConfig.clearIdentityScope();
    }

    public LeImageDao getLeImageDao() {
        return leImageDao;
    }

    public ShopBannerDao getShopBannerDao() {
        return shopBannerDao;
    }

    public ShopListDao getShopListDao() {
        return shopListDao;
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

    public AppUsingDao getAppUsingDao() {
        return appUsingDao;
    }

    public ClockBeanDao getClockBeanDao() {
        return clockBeanDao;
    }

    public DerailBeanDao getDerailBeanDao() {
        return derailBeanDao;
    }

    public DerailResultDao getDerailResultDao() {
        return derailResultDao;
    }

    public FriendDataDao getFriendDataDao() {
        return friendDataDao;
    }

    public MsgDataDao getMsgDataDao() {
        return msgDataDao;
    }

    public TimeDao getTimeDao() {
        return timeDao;
    }

    public UserInfoDao getUserInfoDao() {
        return userInfoDao;
    }

}
