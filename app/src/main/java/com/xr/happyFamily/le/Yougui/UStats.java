package com.xr.happyFamily.le.Yougui;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by User on 3/2/15.
 */
public class UStats {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");
	public static final String TAG = UStats.class.getSimpleName();

	@SuppressWarnings("ResourceType")
	private static UsageStatsManager getUsageStatsManager(Context context) {
		UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
		return usm;
	}

	public static List<UsageStats> getUsageStatsList(Context context) {
		UsageStatsManager usm = getUsageStatsManager(context);
		Calendar calendar = Calendar.getInstance();
		long endTime = calendar.getTimeInMillis();
		calendar.add(Calendar.DATE, -1);
		long startTime = calendar.getTimeInMillis();

		Log.d(TAG, "Range start:" + dateFormat.format(startTime));
		Log.d(TAG, "Range end:" + dateFormat.format(endTime));

		List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
		return usageStatsList;
	}

	// @SuppressWarnings("ResourceType")
	// public static void getStats(Context context) {
	// UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
	// Calendar calendar = Calendar.getInstance();
	// long endTime = calendar.getTimeInMillis();
	// calendar.add(Calendar.DATE, -1);
	// long startTime = calendar.getTimeInMillis();
	//
	// Log.d(TAG, "Range start:" + dateFormat.format(startTime));
	// Log.d(TAG, "Range end:" + dateFormat.format(endTime));
	//
	// UsageEvents uEvents = usm.queryEvents(startTime, endTime);
	// while (uEvents.hasNextEvent()) {
	// UsageEvents.Event e = new UsageEvents.Event();
	// uEvents.getNextEvent(e);
	//
	// if (e != null) {
	// Log.d(TAG, "Event: " + e.getPackageName() + "\t" + dateFormat.format(e.getTimeStamp()));
	// }
	// }
	// }

	// public static void printUsageStats(List<UsageStats> usageStatsList) {
	// for (UsageStats u : usageStatsList) {
	// Log.d(TAG, "Pkg: " + u.getPackageName() + "\t" + "ForegroundTime: " + u.getTotalTimeInForeground());
	// }
	// }
	//
	// public static void printCurrentUsageStatus(Context context) {
	// printUsageStats(getUsageStatsList(context));
	// }

}
