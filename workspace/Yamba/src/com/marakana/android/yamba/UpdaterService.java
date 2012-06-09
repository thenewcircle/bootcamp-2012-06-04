package com.marakana.android.yamba;

import java.util.Date;
import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

public class UpdaterService extends IntentService {
	
	private static final String TAG = "UpdaterService";

	public UpdaterService() {
		super("UpdaterService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		int count = 0;
		try {
			List<Twitter.Status> timeline = YambaApplication.getInstance().getTwitter().getHomeTimeline();
			
			ContentResolver cr = getContentResolver();
			ContentValues values = new ContentValues();
			
			for (Twitter.Status status: timeline) {
				long id = status.id;
				String msg = status.text;
				String name = status.user.name;
				Date createdAt = status.createdAt;
				Log.v(TAG, id + ": " + name + " posted at " + createdAt + ": " + msg);
				
				values.clear();
				values.put(StatusContract.Columns._ID, id);
				values.put(StatusContract.Columns.USER, name);
				values.put(StatusContract.Columns.MESSAGE, msg);
				values.put(StatusContract.Columns.CREATED_AT, createdAt.getTime());
				
				// Insert the status in the StatusProvider
				Uri uri = cr.insert(StatusContract.CONTENT_URI, values);
				if (uri != null) count++;
			}
		} catch (TwitterException e) {
			Log.w(TAG, "Failed to fetch timeline");
		} catch (SQLiteException e) {
			Log.w(TAG, "Unable to open timeline database");
		}
		if (count > 0) newStatusNotification(count);
	}
	
	private void newStatusNotification(int count) {
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		Notification notification = new Notification(android.R.drawable.stat_notify_chat,
											"New Yamba status", System.currentTimeMillis());
		
		Intent startMain = new Intent(this, MainActivity.class);
		PendingIntent notificationPi = PendingIntent.getActivity(this, 1, startMain,
														PendingIntent.FLAG_UPDATE_CURRENT);
		
		notification.setLatestEventInfo(this, "New Yamba Status", "You have new Yamba messages", notificationPi);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		nm.notify(1, notification);
		
		// Send a broadcast message notifying that there are new statuses retrieved.
		Intent intent = new Intent(YambaApplication.ACTION_NEW_STATUS);
		intent.putExtra(YambaApplication.EXTRA_NEW_STATUS_COUNT, count);
		sendBroadcast(intent, YambaApplication.PERM_RECEIVE_NEW_STATUS);
	}

}
