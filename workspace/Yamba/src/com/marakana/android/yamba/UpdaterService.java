package com.marakana.android.yamba;

import java.util.Date;
import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class UpdaterService extends IntentService {
	
	private static final String TAG = "UpdaterService";

	public UpdaterService() {
		super("UpdaterService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			List<Twitter.Status> timeline = YambaApplication.getInstance().getTwitter().getHomeTimeline();
			
			SQLiteDatabase db = YambaApplication.getInstance().getDb();
			ContentValues values = new ContentValues();
			
			for (Twitter.Status status: timeline) {
				long id = status.id;
				String msg = status.text;
				String name = status.user.name;
				Date createdAt = status.createdAt;
				Log.v(TAG, id + ": " + name + " posted at " + createdAt + ": " + msg);
				
				values.clear();
				values.put(TimelineHelper.COLUMN_ID, id);
				values.put(TimelineHelper.COLUMN_USER, name);
				values.put(TimelineHelper.COLUMN_MESSAGE, msg);
				values.put(TimelineHelper.COLUMN_CREATED_AT, createdAt.getTime());
				
				// Insert the status in the timeline database
				db.insert(TimelineHelper.TABLE, null, values);
			}
		} catch (TwitterException e) {
			Log.w(TAG, "Failed to fetch timeline");
		} catch (SQLiteException e) {
			Log.w(TAG, "Unable to open timeline database");
		}
	}

}
