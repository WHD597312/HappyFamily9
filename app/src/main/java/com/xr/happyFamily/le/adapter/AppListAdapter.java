package com.xr.happyFamily.le.adapter;

import android.app.usage.UsageStats;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by LeeWonwoo on 2017. 1. 5..
 */

public class AppListAdapter extends BaseAdapter {
	private static final String TAG = AppListAdapter.class.getSimpleName();

	private LayoutInflater mInflater;
	private int mLayout;
	private List<UsageStats> mList;
	private PackageManager mPackageManager;

	public AppListAdapter(Context context, int layout, List<UsageStats> appCountList) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLayout = layout;
		mList = appCountList;
		mPackageManager = context.getPackageManager();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public UsageStats getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		UsageStats usageStat = mList.get(position);
		if (view == null) {
			view = mInflater.inflate(mLayout, parent, false);
		}

		TextView tvIdx = (TextView) view.findViewById(R.id.tvIdx);
		ImageView ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
		TextView tvAppName = (TextView) view.findViewById(R.id.tvAppName);
		TextView tvPackage = (TextView) view.findViewById(R.id.tvPackage);
		TextView tvTime = (TextView) view.findViewById(R.id.tvTime);

		try {
			tvIdx.setText(position + 1 + "");
			ivIcon.setImageDrawable(mPackageManager.getApplicationIcon(usageStat.getPackageName()));
			tvAppName.setText(mPackageManager.getApplicationLabel(mPackageManager
					.getApplicationInfo(usageStat.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES)));
			// tvPackage.setText(usageStat.getPackageName());

			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");

			long mStartTime = usageStat.getFirstTimeStamp();
			long mLastTime = usageStat.getLastTimeStamp();

			Date timeInDate = new Date(mStartTime);
			String startTime = sdf.format(timeInDate);
			timeInDate = new Date(mLastTime);
			String lastTime = sdf.format(timeInDate);

			tvPackage.setText(startTime + " / " + lastTime);
			tvTime.setText((usageStat.getTotalTimeInForeground() / 1000) + " Sec");
		} catch (Exception e) {
			Log.e(TAG, "getView Exception - " + e.getMessage());
		}

		return view;
	}
}
