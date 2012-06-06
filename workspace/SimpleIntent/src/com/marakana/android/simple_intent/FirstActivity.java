package com.marakana.android.simple_intent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FirstActivity extends Activity implements OnClickListener {

    private EditText mEditMsg;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);
        
        mEditMsg = (EditText) findViewById(R.id.msg);
        Button sendButton = (Button) findViewById(R.id.send);
        sendButton.setOnClickListener(this);
    }
    
	public void onClick(View v) {
		String msg = mEditMsg.getText().toString();
		mEditMsg.setText("");
		
		// Attach the String as an extra on the Intent
		Intent intent = new Intent(this, SecondActivity.class);
		intent.putExtra("MESSAGE", msg);
		startActivity(intent);
	}
}