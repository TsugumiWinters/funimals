package com.picturebooks.mobilepicturebooks;

import com.picturebooks.mobilepicturebooks.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegistrationLevelActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_register_yearlevel);
	}
	
	public void clicked_btnBack(View v) {
        Intent mainIntent = new Intent(RegistrationLevelActivity.this, RegistrationAgeActivity.class);
        startActivity(mainIntent);
        finish();
	}
	
	public void clicked_btnNext(View v) {
        Intent mainIntent = new Intent(RegistrationLevelActivity.this, HomeActivity.class);
        startActivity(mainIntent);
        finish();
	}
	
}
