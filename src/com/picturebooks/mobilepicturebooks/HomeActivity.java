package com.picturebooks.mobilepicturebooks;

import com.picturebooks.mobilepicturebooks.database_entities.UserInformation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
        
        FrameLayout frame = (FrameLayout) findViewById(R.id.home_usercanvas);
		LayoutInflater inflater = getLayoutInflater();
        View userCanvas = inflater.inflate(R.layout.user_canvas, frame, false);
        
        ImageView imgUser = (ImageView) userCanvas.findViewById(R.id.canvas_img_user);
        TextView txtUser = (TextView) userCanvas.findViewById(R.id.canvas_name);
        
        UserInformation user = ActiveUser.getActiveUser(this);
        
        if (user == null) {
            Intent mainIntent = new Intent(HomeActivity.this, MenuActivity.class);
            startActivity(mainIntent);
            finish();
        }
        
        imgUser.setImageResource(user.getImage());
        txtUser.setText(user.getName());
		
        frame.addView(userCanvas);
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
        mainIntent.putExtra("user_img", getIntent().getIntExtra("user_img", R.drawable.users0));
        mainIntent.putExtra("user_name", getIntent().getStringExtra("user_name"));
        mainIntent.putExtra("user_age", getIntent().getIntExtra("user_age", 6));
        mainIntent.putExtra("user_level", getIntent().getStringExtra("user_level"));
        startActivity(mainIntent);
        finish();
	}
	
	public void clicked_btnSeeTutorial(View v) {
		// TODO
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActiveUser.clearActiveUser(this);
	}
    
}
