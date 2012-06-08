package com.marakana.android.yamba;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

public class TimelineFragment extends ListFragment implements ViewBinder {
	private static final String[] FROM = {
		StatusContract.Columns.USER,
		StatusContract.Columns.MESSAGE,
		StatusContract.Columns.CREATED_AT
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
		
		ContentResolver cr = getActivity().getContentResolver();
		
		/*
		 * 	Perform the initial query of the status content provider.
		 * 	We're querying the cursor on the main thread -- a bad practice.
		 * 	We'll fix this later by using a Loader. Prior to the introduction of
		 * 	Loaders, the proper technique was to generate a Cursor in a worker
		 * 	thread, such as in an AsyncTask, and when the Cursor was ready,
		 * 	do a SimpleCursorAdapter.changeCursor() call to install the new Cursor.
 		 */
		
		mCursor = cr.query(StatusContract.CONTENT_URI, null, null, null, null);
		
		// Create the adapter and install the ViewBinder.
		mAdapter = new SimpleCursorAdapter(getActivity(),
										   R.layout.timeline_row,
										   mCursor, FROM, TO, 0);
		mAdapter.setViewBinder(this);
		setListAdapter(mAdapter);
	}

	@Override
	public void onStart() {
		super.onStart();
		
		// We're being naughty and re-querying the cursor on the main thread.
		// We'll fix this later by using a Loader.
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
