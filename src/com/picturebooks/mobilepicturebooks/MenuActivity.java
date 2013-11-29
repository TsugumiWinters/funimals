package com.picturebooks.mobilepicturebooks;

import com.picturebooks.mobilepicturebooks.models.UserListAdapter;

import database_entities.UserInformation;

import android.app.Activity;
import android.content.Intent;
/* ORIGINAL
import android.os.Bundle;
import org.apache.cordova.*;

import database.DatabaseHelper;*/
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MenuActivity extends /* ORIGINAL DroidGap */ Activity {
	
	private boolean openingActivity;
	private ListView accountList;
	private UserListAdapter adapter;
	
	/* ORIGINAL
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		super.init();
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		HTMLConnector mc = new HTMLConnector(this, appView, dbHelper, this);
		appView.addJavascriptInterface(mc, "Connector");
		
		super.loadUrl("file:///android_asset/www/main.html");
	}*/
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts);
		openingActivity = false;
		
        Log.d("MenuActivity", "Activity created.");
        
		accountList = (ListView) findViewById(R.id.accounts_list);
		
		adapter = new UserListAdapter(this);
		accountList.setAdapter(adapter);
	}
	
	public void clicked_addAccount(View v) {
        Intent mainIntent = new Intent(MenuActivity.this, RegistrationNameActivity.class);
        startActivity(mainIntent);
        openingActivity = true;
        finish();
	}
	
	public void clicked_openAccount(View v) {
		ImageView viewImage = (ImageView) v.findViewById(R.id.canvas_img_user);
		TextView txtName = (TextView) v.findViewById(R.id.canvas_name);
		
		/* Get Selected User */
		UserInformation user = adapter.getUser(txtName.getText().toString());
		
		int img = getResources().getIdentifier(viewImage.getContentDescription().toString(), "drawable", this.getPackageName());
		String username = user.getUsername();
		int age = user.getAge();
		String level = "Grade ";
		if (user.getGrade() > 0) {
			level.concat("" + user.getGrade());
		}
		else {
			level = "Prep";
		}
		ActiveUser.setActiveUser(this, img, username, age, level);
		
		/* Open HomeActivity */
		Intent mainIntent = new Intent(MenuActivity.this, HomeActivity.class);
        MenuActivity.this.startActivity(mainIntent);
        openingActivity = true;
        finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (!openingActivity) {
			ActiveUser.clearActiveUser(this);
		}
	}
}
