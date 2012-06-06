package com.marakana.android.launchweb;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LaunchActivity extends Activity implements OnClickListener {
	private EditText mEditUrl;
	private Toast mToast;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mToast = Toast.makeText(this, R.string.toast_activity_not_found, Toast.LENGTH_LONG);
        
		mEditUrl = (EditText) findViewById(R.id.edit_url);
        Button buttonGo = (Button) findViewById(R.id.button_go);
        buttonGo.setOnClickListener(this);
    }

	public void onClick(View view) {
		// Retrieve the string from the EditText and generate a Uri from it
		String uriString = mEditUrl.getText().toString();
		mEditUrl.setText("");
		Uri uri = Uri.parse(uriString);
		
		// Request the system to start an Activity to view the Uri
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			mToast.show();
		}
	}
}