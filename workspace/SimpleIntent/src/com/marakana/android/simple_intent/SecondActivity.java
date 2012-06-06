package com.marakana.android.simple_intent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        
        // Get the Intent used to start this activity
        Intent intent = getIntent();
        
        // Retrieve the extra named "MESSAGE"
        String sentMsg = intent.getStringExtra("MESSAGE");
        
        // If the extra was present, display it in a TextView
        if (sentMsg != null) {
        	TextView msgView = (TextView) findViewById(R.id.sent_message);
        	msgView.setText(sentMsg);
        }
    }

}
