package com.marakana.android.yamba;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	private static final String TAG = "MainActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate() invoked");
        setContentView(R.layout.main);
        Button buttonUpdate = (Button) findViewById(R.id.button_update);
        buttonUpdate.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onStart() {
		super.onStart();
        Log.v(TAG, "onStart() invoked");
	}

	@Override
	protected void onResume() {
		super.onResume();
        Log.v(TAG, "onResume() invoked");
	}

	@Override
	protected void onPause() {
		super.onPause();
        Log.v(TAG, "onPause() invoked");
	}

	@Override
	protected void onStop() {
		super.onStop();
        Log.v(TAG, "onStop() invoked");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
        Log.v(TAG, "onRestart() invoked");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
        Log.v(TAG, "onDestroy() invoked");
	}
}