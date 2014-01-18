package com.swiftshot.funimals;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.swiftshot.funimals.models.ActiveUser;

public class InfoActivity extends Activity {
	
	private boolean openingActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		openingActivity = false;
	}
	
	public void clicked_btnBack(View v) {
		onBackPressed();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (!openingActivity) {
			ActiveUser.clearActiveUser(this);
		}
	}
	
}
