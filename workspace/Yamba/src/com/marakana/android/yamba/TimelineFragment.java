package com.marakana.android.yamba;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

public class TimelineFragment extends ListFragment implements ViewBinder {
	private static final String[] FROM = {
		TimelineHelper.COLUMN_USER,
		TimelineHelper.COLUMN_MESSAGE,
		TimelineHelper.COLUMN_CREATED_AT
	};
	private static final int[] TO = {
		R.id.status_user,
		R.id.status_msg,
		R.id.status_date
	};
	
	private SimpleCursorAdapter mAdapter;
	private Cursor mCursor;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		SQLiteDatabase db = YambaApplication.getInstance().getDb();
		mCursor = db.query(TimelineHelper.TABLE,
						   null, null, null, null, null,
						   TimelineHelper.COLUMN_CREATED_AT + " desc");
		mAdapter = new SimpleCursorAdapter(getActivity(),
										   R.layout.timeline_row,
										   mCursor, FROM, TO, 0);
		mAdapter.setViewBinder(this);
		setListAdapter(mAdapter);
	}

	@Override
	public void onStart() {
		super.onStart();
		mCursor.requery();
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onStop() {
		super.onStop();
		mCursor.deactivate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mCursor.close();
	}

	@Override
	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		int id = view.getId();
		switch (id) {
		case R.id.status_date:
			// Create a user-friendly formatted date string
			long timestamp = cursor.getLong(columnIndex);
			CharSequence relTime = DateUtils.getRelativeTimeSpanString(timestamp);
			TextView tv = (TextView) view;
			tv.setText(relTime);
			return true;
		default:
			return false;
		}
	}

}
