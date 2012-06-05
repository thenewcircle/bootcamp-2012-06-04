package com.marakana.android.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private static final String TAG = "MainActivity";
	private EditText mEditStatus;
	private Toast mToast;
	private Twitter mTwitter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate() invoked");
        setContentView(R.layout.main);
        
        // Initialize the user interface components
        Button buttonUpdate = (Button) findViewById(R.id.button_update);
        buttonUpdate.setOnClickListener(this);
        mEditStatus = (EditText) findViewById(R.id.edit_status);
        
        // Initialize our Toast notification
        mToast = Toast.makeText(this, null, Toast.LENGTH_LONG);
        
        // Initialize the Twitter object
        mTwitter = new Twitter("student", "password");
        mTwitter.setAPIRootUrl("http://yamba.marakana.com/api");
    }

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.button_update:
			// Handle the click of the Update button
			
			// Read the content of the EditText
			String msg = mEditStatus.getText().toString();
			Log.v(TAG, "User entered: " + msg);
			mEditStatus.setText("");

			if (!TextUtils.isEmpty(msg)) {
				new PostToTwitter().execute(msg);
			}
			
			break;
		default:
			// Unrecognized button? We should never get here!
		}
	}
	
	private class PostToTwitter extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... args) {
			boolean result = true;
			// Post the message to the Yamba server
			try {
				mTwitter.setStatus(args[0]);
			} catch (TwitterException e) {
				Log.w(TAG, "Failed to post message", e);
				result = false;
			}
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				// Message posted successfully
				mToast.setText(R.string.post_status_success);
			}
			else {
				// Message failed to post
				mToast.setText(R.string.post_status_fail);
			}
			mToast.show();
		}
		
	}
	
	
	
}



