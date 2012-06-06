package com.marakana.android.yamba;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

public class YambaApplication extends Application implements OnSharedPreferenceChangeListener {
	private static YambaApplication sApp;
	private Twitter mTwitter;
	
	private SharedPreferences mPrefs;
	private String mPrefUserKey;
	private String mPrefPasswordKey;
	private String mPrefSiteUrlKey;

	@Override
	public void onCreate() {
		super.onCreate();
		sApp = this;
		
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mPrefs.registerOnSharedPreferenceChangeListener(this);
		
		mPrefUserKey = getString(R.string.pref_user_key);
		mPrefPasswordKey = getString(R.string.pref_password_key);
		mPrefSiteUrlKey = getString(R.string.pref_site_url_key);
	}

	public static YambaApplication getInstance() {
		return sApp;
	}
	
	public synchronized Twitter getTwitter() {
		if (mTwitter == null) {
			// Create a Twitter object using the current preference values
			String user = mPrefs.getString(mPrefUserKey, null);
			String password = mPrefs.getString(mPrefPasswordKey, null);
			String siteUrl = mPrefs.getString(mPrefSiteUrlKey, null);
			
			mTwitter = new Twitter(user, password);
			mTwitter.setAPIRootUrl(siteUrl);
		}
		return mTwitter;
	}

	@Override
	public synchronized void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		mTwitter = null;
	}

}
