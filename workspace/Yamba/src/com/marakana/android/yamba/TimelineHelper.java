package com.marakana.android.yamba;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TimelineHelper extends SQLiteOpenHelper {
	private static final String TAG = "TimelineHelper";
	
	private static final String DB_NAME = "timeline.db";
	private static final int DB_VERSION = 2;
	
	public static final String TABLE = "timeline";
	
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_USER = "user";
	public static final String COLUMN_MESSAGE = "msg";
	public static final String COLUMN_CREATED_AT = "created_at";
	
	public static final String DB_CREATE
		= "create table " + TABLE + " ("
		+ COLUMN_ID + " int primary key, "
		+ COLUMN_USER + " text, "
		+ COLUMN_MESSAGE + " text, "
		+ COLUMN_CREATED_AT + " int"
		+ ");";

	public TimelineHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v(TAG, "onCreate() invoked");
		db.execSQL(DB_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v(TAG, "onUpgrade() invoked");
		// In real world, upgrade the database. But in our class...
		db.execSQL("drop table if exists " + TABLE);
		onCreate(db);
	}

}
