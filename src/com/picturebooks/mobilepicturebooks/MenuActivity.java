package com.picturebooks.mobilepicturebooks;

import database_entities.UserInformation;
import android.app.Activity;
import android.content.Intent;
/* ORIGIN
import android.os.Bundle;
import org.apache.cordova.*;

import database.DatabaseHelper;*/
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;

public class MenuActivity extends /* ORIGIN DroidGap */ Activity {
	
	/* ORIGIN
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
		
		/* TODO ArrayAdapter<UserInformation> users = new ArrayAdapter<UserInformation>(this, R.layout.user_canvas); */
		
		ActiveUser.setActiveUser(this, R.drawable.users0, "User1", 6, "Prep");
        
        /* TODO: Remove this eventually */
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(MenuActivity.this, HomeActivity.class);
                MenuActivity.this.startActivity(mainIntent);
                MenuActivity.this.finish();
            }
        }, 3000);
	}
	
	public void clicked_addAccount(View v) {
        Intent mainIntent = new Intent(MenuActivity.this, RegistrationNameActivity.class);
        startActivity(mainIntent);
        finish();
	}
	
	public void clicked_openAccount(View v) {
		// TODO
	}
}
