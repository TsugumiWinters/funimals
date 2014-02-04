package com.swiftshot.funimals;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.swiftshot.funimals.models.ActiveUser;

public class InfoActivity extends Activity {
	
	private boolean openingActivity;
	
	private Bitmap icon;
	private BitmapFactory.Options options;
	private Drawable bg;
	private RelativeLayout infoBackground;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		openingActivity = false;
		
		options = new BitmapFactory.Options(); 
		options.inPurgeable = true;
        
        infoBackground = (RelativeLayout) findViewById(R.id.info_bg);
        icon = BitmapFactory.decodeResource(getResources(),R.drawable.general_background_info, options);
		bg = new BitmapDrawable(getResources(), icon);
		infoBackground.setBackground(bg);
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
