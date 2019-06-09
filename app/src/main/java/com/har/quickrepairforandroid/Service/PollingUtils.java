package com.har.quickrepairforandroid.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.har.quickrepairforandroid.Models.AccountHolder;

public class PollingUtils {

	private static Context mContext;
	private static Class<?> mService;
	private static final long mInterval = 3000;

	public static void startPolling(Context context, Class<?> service) {
		mContext = context;
		mService = service;

		AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(mContext, mService);
		PendingIntent pendingIntent = PendingIntent.getService(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		long triggerAtTime = SystemClock.elapsedRealtime();
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime, mInterval, pendingIntent);
	}

	public static void setUpPolling() {
		Intent intent = new Intent(mContext, mService);
		Bundle bundle = new Bundle();
		bundle.putString(PollOrderService.PollOrderChangeAccount, AccountHolder.getInstance().getAccount());
		bundle.putBoolean(PollOrderService.PollOrderAccountLogin, AccountHolder.getInstance().getIsLogin());
		bundle.putBoolean(PollOrderService.PollOrderIsCustomer, AccountHolder.getInstance().getIsCustomer());
		intent.putExtras(bundle);

		PendingIntent pendingIntent = PendingIntent.getService(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		long triggerAtTime = SystemClock.elapsedRealtime();

		AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime, mInterval, pendingIntent);
	}
}
