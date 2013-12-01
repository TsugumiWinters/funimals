package com.picturebooks.mobilepicturebooks;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.picturebooks.mobilepicturebooks.models.ActiveUser;
import com.picturebooks.mobilepicturebooks.models.StoryAdapter;
import com.picturebooks.mobilepicturebooks.models.UserInformation;

import database.DatabaseHelper;
import database_entities.StoryFile;

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
	private int selectedStory;
	private boolean openingActivity;
	Bitmap bm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userstories);
        openingActivity = false;
        selectedStory = -1;
		
        imgStory = (ImageView) findViewById(R.id.userstories_img_story);
        txtStory = (TextView) findViewById(R.id.userstories_txt_storytitle);
        lstStories = (ListView) findViewById(R.id.userstories_titles);
        
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.openDataBase();
        ArrayList<StoryFile> stories = new ArrayList<StoryFile>(dbHelper
        		.getStoryFilesById(ActiveUser.getActiveUser(this).getName()));
        dbHelper.close();
        lstStories.setDividerHeight(3);
        final StoryAdapter<StoryFile> adapter = new StoryAdapter<StoryFile>(this, R.layout.story_title, stories);
        lstStories.setAdapter(adapter);       
             
        lstStories.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				adapter.getItem(arg2);
				copy_lstStories(adapter.getItem(arg2).getTitle());
				
			}
        });
        
	}
	
	public void clicked_btnBack(View v) {
        Intent mainIntent = new Intent(StoriesActivity.this, HomeActivity.class);
        startActivity(mainIntent);
        openingActivity = true;
        finish();
	}
	
	public void clicked_btnAdd(View v) {
        Intent mainIntent = new Intent(StoriesActivity.this, PictureEditor.class);
        startActivity(mainIntent);
        openingActivity = true;
        finish();
	}
	
	public void copy_lstStories(String titled) {
		String title = titled;
		StoryFile story = null;
		String filePath = null;
		
		Log.e("log", title);
		
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		story = dbHelper.findStoryFileByTitle(ActiveUser.getActiveUser(this).getName(), title);
		filePath = story.getUsername() + "_" + story.getStoryID() + ".png";
		
		Log.e("selectedstory", selectedStory + "");
		Log.e("txtstory", title);
		Log.e("imgstory", filePath);
		
		selectedStory = story.getStoryID();
		txtStory.setText(title);
		imgStory.setImageBitmap(getImageFromFile(filePath));
	}
	
	public void clicked_lstStories(View v) {
		String title = ((TextView) v).getText().toString();
		StoryFile story = null;
		String filePath = null;
		
		Log.e("log", title);
		
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		story = dbHelper.findStoryFileByTitle(ActiveUser.getActiveUser(this).getName(), title);
		filePath = story.getUsername() + "_" + story.getStoryID() + ".png";
		
		Log.e("selectedstory", selectedStory + "");
		Log.e("txtstory", title);
		Log.e("imgstory", filePath);
		
		selectedStory = story.getStoryID();
		txtStory.setText(title);
		imgStory.setImageBitmap(getImageFromFile(filePath));
	}
	
	public void clicked_selectedStory(View v) {
        if (selectedStory == -1) {
        	return;
        }
		
		Intent mainIntent = new Intent(StoriesActivity.this, PictureEditor.class);
        UserInformation user = ActiveUser.getActiveUser(this);
        
        mainIntent.putExtra("username", user.getName());
        mainIntent.putExtra("age", user.getAge());
        mainIntent.putExtra("tutorial", 0);
        mainIntent.putExtra("loadStory", true);
        mainIntent.putExtra("storyID", selectedStory);
        mainIntent.putExtra("isUserAuthor", 1);
        
        startActivity(mainIntent);
        openingActivity = true;
        finish();
	}
	
	private Bitmap getImageFromFile(String fileName) {
		String path = Environment.getExternalStorageDirectory().toString()
				+ "/MobilePictureBooks/SavedPictures/";
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		return BitmapFactory.decodeFile(path + fileName, options);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (!openingActivity) {
			ActiveUser.clearActiveUser(this);
		}
	}
	
}
