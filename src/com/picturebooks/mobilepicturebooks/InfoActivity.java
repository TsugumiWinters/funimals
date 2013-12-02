package com.picturebooks.mobilepicturebooks;

import com.picturebooks.mobilepicturebooks.models.ActiveUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InfoActivity extends Activity {
	
	private boolean openingActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		openingActivity = false;
	}
	
	public void clicked_btnBack(View v) {
		Intent mainIntent = new Intent(this, HomeActivity.class);
		startActivity(mainIntent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (!openingActivity) {
			ActiveUser.clearActiveUser(this);
		}
	}
	
}
