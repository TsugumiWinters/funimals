package com.picturebooks.mobilepicturebooks;

import android.os.Bundle;
import org.apache.cordova.*;

import database.DatabaseHelper;

public class LibraryActivity extends DroidGap {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		super.init();
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		HTMLConnector mc = new HTMLConnector(this, appView, dbHelper, this, 2);
		appView.addJavascriptInterface(mc, "Connector");
		
		super.loadUrl("file:///android_asset/www/main.html");
	}
    
}
