package com.swiftshot.funimals;

import com.swiftshot.funimals.R;
import com.swiftshot.funimals.models.ActiveUser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class InfoActivity extends Activity {
	
	private boolean openingActivity;
	private Bitmap icon;
	private Drawable bg;
	private BitmapFactory.Options options;
	private RelativeLayout creditsBackground;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		creditsBackground = (RelativeLayout) findViewById(R.id.info_credits);
		options = new BitmapFactory.Options(); 
		options.inPurgeable = true;
		
		icon = BitmapFactory.decodeResource(getResources(),R.drawable.general_info_credits, options);
		bg = new BitmapDrawable(getResources(), icon);
		creditsBackground.setBackground(bg);
		
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
