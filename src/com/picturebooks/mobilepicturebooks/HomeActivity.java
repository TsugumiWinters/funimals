package com.picturebooks.mobilepicturebooks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/* ORIGINAL
import org.apache.cordova.*;
import database.DatabaseHelper; */

public class HomeActivity extends /* ORIGIN DroidGap */ Activity {

	/* ORIGINAL
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		super.init();
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		HTMLConnector mc = new HTMLConnector(this, appView, dbHelper, this, 1);
		appView.addJavascriptInterface(mc, "Connector");
		
		super.loadUrl("file:///android_asset/www/main.html");
		
	}*/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
		
		// TODO
	}
	
	public void clicked_btnSound(View v) {
		ImageView img = (ImageView) v;
		if (img.getContentDescription().equals(this.getResources().getString(R.string.button_sound_on))) {
			img.setImageResource(R.drawable.general_soundoff);
			img.setContentDescription(this.getResources().getString(R.string.button_sound_off));
			
			/* TODO: Logic for closing sounds */
		}
		else {
			img.setImageResource(R.drawable.general_soundon);
			img.setContentDescription(this.getResources().getString(R.string.button_sound_on));
			
			/* TODO: Logic for adding sounds */
		}
	}
	
	public void clicked_btnInfo(View v) {
		// TODO
	}
	
	public void clicked_btnNewPicture(View v) {
        Intent mainIntent = new Intent(HomeActivity.this, PictureEditor.class);
        startActivity(mainIntent);
        finish();
	}
	
	public void clicked_btnViewLibrary(View v) {
        Intent mainIntent = new Intent(HomeActivity.this, BookActivity.class);
        startActivity(mainIntent);
        finish();
	}
	
	public void clicked_btnSeeTutorial(View v) {
		// TODO
	}
    
}
