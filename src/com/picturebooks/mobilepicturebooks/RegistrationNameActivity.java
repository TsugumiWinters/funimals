package com.picturebooks.mobilepicturebooks;

import com.picturebooks.mobilepicturebooks.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegistrationNameActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_register_name);
	}
	
	public void clicked_btnBack(View v) {
        Intent mainIntent = new Intent(RegistrationNameActivity.this, MenuActivity.class);
        startActivity(mainIntent);
        finish();
	}
	
	public void clicked_btnNext(View v) {
        Intent mainIntent = new Intent(RegistrationNameActivity.this, RegistrationAgeActivity.class);
        startActivity(mainIntent);
        finish();
	}
	
}
