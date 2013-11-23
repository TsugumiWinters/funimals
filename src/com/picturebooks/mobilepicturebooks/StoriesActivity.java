package com.picturebooks.mobilepicturebooks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/* ORIGINAL
import android.os.Bundle;
import org.apache.cordova.*;

import database.DatabaseHelper;*/

public class StoriesActivity extends /* ORIGINAL DroidGap */ Activity {

	/* ORIGINAL
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		super.init();
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		HTMLConnector mc = new HTMLConnector(this, appView, dbHelper, this, 3);
		appView.addJavascriptInterface(mc, "Connector");
		
		super.loadUrl("file:///android_asset/www/main.html");
	}*/
    
	private ImageView imgStory;
	private TextView txtStory;
	private ListView lstStories;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userstories);
		
        imgStory = (ImageView) findViewById(R.id.userstories_img_story);
        txtStory = (TextView) findViewById(R.id.userstories_txt_storytitle);
        lstStories = (ListView) findViewById(R.id.userstories_titles);
	}
	
	public void clicked_btnBack(View v) {
        Intent mainIntent = new Intent(StoriesActivity.this, HomeActivity.class);
        startActivity(mainIntent);
        finish();
	}
	
	public void clicked_btnAdd(View v) {
        Intent mainIntent = new Intent(StoriesActivity.this, PictureEditor.class);
        startActivity(mainIntent);
        finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActiveUser.clearActiveUser(this);
	}
	
}
