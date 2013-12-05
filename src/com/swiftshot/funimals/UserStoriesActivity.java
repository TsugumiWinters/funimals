package com.swiftshot.funimals;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swiftshot.funimals.R;
import com.swiftshot.funimals.models.ActiveUser;
import com.swiftshot.funimals.models.UserInformation;
import com.swiftshot.funimals.models.database.DatabaseHelper;
import com.swiftshot.funimals.models.database.entities.StoryFile;


public class UserStoriesActivity extends Activity {
    
	private ImageView imgStory;
	private TextView txtStory;
	private ListView lstStories;
	private int selectedStory;
	private boolean openingActivity;
	Bitmap icon;
	Drawable bg;
	BitmapFactory.Options options;
	RelativeLayout libraryBackground;
	
	Bitmap icon2;
	Drawable bg2;
	BitmapFactory.Options options2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userstories);
        openingActivity = false;
        selectedStory = -1;
		
        libraryBackground = (RelativeLayout) findViewById(R.id.library_bg);
        
        options = new BitmapFactory.Options(); 
		options.inPurgeable = true;
		options2 = new BitmapFactory.Options(); 
		options2.inPurgeable = true;
		
        icon = BitmapFactory.decodeResource(getResources(),R.drawable.library_openbook, options);
		bg = new BitmapDrawable(icon);
		libraryBackground.setBackground(bg);
        
        imgStory = (ImageView) findViewById(R.id.userstories_img_story);
        txtStory = (TextView) findViewById(R.id.userstories_txt_storytitle);
        lstStories = (ListView) findViewById(R.id.userstories_titles);
             
        icon2 = BitmapFactory.decodeResource(getResources(),R.drawable.library_preview_default, options2);
		bg2 = new BitmapDrawable(icon2);
		imgStory.setImageDrawable(bg2);
        
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
				showStories(adapter.getItem(arg2).getTitle());	
			}
        });
        
	}
	
	public void clicked_btnBack(View v) {
        Intent mainIntent = new Intent(UserStoriesActivity.this, UserbookActivity.class);
        startActivity(mainIntent);
	}
	
	public void clicked_btnAdd(View v) {
        Intent mainIntent = new Intent(UserStoriesActivity.this, PictureEditorActivity.class);
        startActivity(mainIntent);
	}
	
	public void showStories(String titled) {
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
        
        imgStory.setColorFilter(Color.rgb(123,73,122), android.graphics.PorterDuff.Mode.DARKEN);
        
		Intent mainIntent = new Intent(UserStoriesActivity.this, PictureEditorActivity.class);
        UserInformation user = ActiveUser.getActiveUser(this);
        
        mainIntent.putExtra("username", user.getName());
        mainIntent.putExtra("age", user.getAge());
        mainIntent.putExtra("tutorial", 0);
        mainIntent.putExtra("loadStory", true);
        mainIntent.putExtra("storyID", selectedStory);
        mainIntent.putExtra("isUserAuthor", 1);
        
        startActivity(mainIntent);
	}
	
	private Bitmap getImageFromFile(String fileName) {
		String path = Environment.getExternalStorageDirectory().toString()
				+ "/Funimals/SavedPictures/";
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPurgeable = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		
		return BitmapFactory.decodeFile(path + fileName, options);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		libraryBackground.setBackgroundResource(0);
		imgStory.setImageBitmap(null);
		if (!openingActivity) {
			ActiveUser.clearActiveUser(this);
		}
	}
	
}
