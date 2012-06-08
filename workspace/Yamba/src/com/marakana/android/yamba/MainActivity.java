package com.marakana.android.yamba;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {
	private static final String TAG = "MainActivity";
	private static final String COMPOSE_TAG = "compose_fragment";
	private static final String TIMELINE_TAG = "timeline_fragment";
	
	private static final String FRAGMENT_VISIBLE = "FRAGMENT_VISIBLE";
	private static final int COMPOSE_FRAGMENT_VISIBLE = 1;
	private static final int TIMELINE_FRAGMENT_VISIBLE = 2;
	
	private int mFragmentVisible;
	
	private FragmentManager mFragmentManager;
	private ComposeFragment mComposeFragment;
	private TimelineFragment mTimelineFragment;
	
	private MenuItem mComposeMenuItem;
	private MenuItem mTimelineMenuItem;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate() invoked");
        setContentView(R.layout.main);
        
        mFragmentManager = getSupportFragmentManager();
        
        if (savedInstanceState == null) {
        	// Initialize our fragments.
	        mComposeFragment = new ComposeFragment();
	        mTimelineFragment = new TimelineFragment();
	        
	        mFragmentManager.beginTransaction()
	        	.add(R.id.fragment_container, mComposeFragment, COMPOSE_TAG)
	        	.add(R.id.fragment_container, mTimelineFragment, TIMELINE_TAG)
	        	.commit();
	        
	        // Default to displaying the TimelineFragment.
	        showFragment(TIMELINE_FRAGMENT_VISIBLE);
        }
        else {
        	// Retrieve references to the fragments that were re-created automatically.
        	mComposeFragment = (ComposeFragment) mFragmentManager.findFragmentByTag(COMPOSE_TAG);
        	mTimelineFragment = (TimelineFragment) mFragmentManager.findFragmentByTag(TIMELINE_TAG);
        	
        	// Determine which fragment was visible previously, so that we can ensure
        	// it's visible now and the action bar/option menu state is correct.
        	showFragment( savedInstanceState.getInt(FRAGMENT_VISIBLE, TIMELINE_FRAGMENT_VISIBLE) );
        }
    }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		// Save the fragment visibility state in the bundle.
		outState.putInt(FRAGMENT_VISIBLE, mFragmentVisible);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.options_main, menu);
		
		// Store references to the menu items controlling fragment visibility so that we
		// can show/hide the menu items dynamically.
		mComposeMenuItem = menu.findItem(R.id.menu_compose);
		mTimelineMenuItem = menu.findItem(R.id.menu_timeline);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		
		// Show the menu item(s) appropriate for the current fragment visibility.
		switch (mFragmentVisible) {
		case COMPOSE_FRAGMENT_VISIBLE:
			mComposeMenuItem.setVisible(false);
			mTimelineMenuItem.setVisible(true);
			break;
		default:
			// Assume TimelineFragment is visible.
			mTimelineMenuItem.setVisible(false);
			mComposeMenuItem.setVisible(true);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		Intent intent;
		switch (id) {
		case R.id.menu_preferences:
			// Display the preference activity
			intent = new Intent(this, PrefsActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_refresh:
			// Refresh the timeline
			intent = new Intent(this, UpdaterService.class);
			startService(intent);
			return true;
		case R.id.menu_compose:
			// Show the ComposeFragment
			showFragment(COMPOSE_FRAGMENT_VISIBLE);
			return true;
		case R.id.menu_timeline:
			// Show the TimelineFragment
			showFragment(TIMELINE_FRAGMENT_VISIBLE);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void showFragment(int visibleFragment) {
		mFragmentVisible = visibleFragment;
		switch (visibleFragment) {
		case COMPOSE_FRAGMENT_VISIBLE:
			// Show the ComposeFragment, hide the other(s)
			mFragmentManager.beginTransaction()
				.hide(mTimelineFragment)
				.show(mComposeFragment)
				.commit();
			break;
		default:
			// Show the TimelineFragment, hide the other(s)
			mFragmentManager.beginTransaction()
			.hide(mComposeFragment)
			.show(mTimelineFragment)
			.commit();
		}
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Force refresh of the action bar in Honeycomb and later.
			invalidateOptionsMenu();
		}
	}
	
}



