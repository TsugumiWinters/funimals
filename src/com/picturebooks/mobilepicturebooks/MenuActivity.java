package com.picturebooks.mobilepicturebooks;

import android.app.Activity;
/* ORIGIN
import android.os.Bundle;
import org.apache.cordova.*;

import database.DatabaseHelper;*/
import android.os.Bundle;
import android.view.View;

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
	}
	
	public void clicked_addAccount(View v) {
		// TODO
	}
	
	public void clicked_openAccount(View v) {
		// TODO
	}
}
