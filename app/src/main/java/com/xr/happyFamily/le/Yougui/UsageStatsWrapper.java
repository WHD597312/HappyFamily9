package com.xr.happyFamily.le.Yougui;

import android.app.usage.UsageStats;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class UsageStatsWrapper implements Comparable<UsageStatsWrapper> {

    private final UsageStats usageStats;
    private final Drawable appIcon;
    private final String appName;

    public UsageStatsWrapper(UsageStats usageStats, Drawable appIcon, String appName) {
        this.usageStats = usageStats;
        this.appIcon = appIcon;
        this.appName = appName;
    }

    public UsageStats getUsageStats() {
        return usageStats;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public String getAppName() {
        return appName;
    }
    public String TotleTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");

        long TotleTime =  (usageStats.getTotalTimeInForeground() / 1000);
        String totleTime= TotleTime+"";
        return totleTime;
    }
    public String lastTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
        long mLastTime = usageStats.getLastTimeStamp();
        Date timeInDate = new Date(mLastTime);
        String lastTime = sdf.format(timeInDate);
        return lastTime;
    }

    public String data(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        long mLastTime = usageStats.getFirstTimeStamp();
        Date timeInDate = new Date(mLastTime);
        String lastTime = sdf.format(timeInDate);
        return lastTime;
    }

    public  String getPackageName(){
        String packageName = usageStats.getPackageName();
        return packageName;
    }

    @Override
    public int compareTo(@NonNull UsageStatsWrapper usageStatsWrapper) {
        if (usageStats == null && usageStatsWrapper.getUsageStats() != null) {
            return 1;
        } else if (usageStatsWrapper.getUsageStats() == null && usageStats != null) {
            return -1;
        } else if (usageStatsWrapper.getUsageStats() == null && usageStats == null) {
            return 0;
        } else {
            return Long.compare(usageStatsWrapper.getUsageStats().getLastTimeUsed(),
                    usageStats.getLastTimeUsed());
        }
    }
}
