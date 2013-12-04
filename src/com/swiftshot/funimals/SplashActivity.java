package com.swiftshot.funimals;

import java.io.IOException;

import com.swiftshot.funimals.R;
import com.swiftshot.funimals.models.ActiveUser;

import database.DatabaseHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashActivity extends Activity {
	
    private final int SPLASH_DISPLAY_LENGHT = 3000;
    private boolean openingActivity;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splashscreen);
        openingActivity = false;
        
        Log.d("SplashActivity", "Activity created.");
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			Log.d("SplashActivity", "Error: " + e.getMessage());
		}

        /* New Handler to start the Menu-Activity 
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this, MenuActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                openingActivity = true;
                
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (!openingActivity) {
			ActiveUser.clearActiveUser(this);
		}
	}
}