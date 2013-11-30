package com.picturebooks.mobilepicturebooks.models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Preferences {

	private static final String PREFERENCES = "funimals_preferences";
	private static final int MODE = Activity.MODE_PRIVATE;
	
	public static boolean getMusicOn(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES, MODE);
		boolean musicOn = pref.getBoolean("music_on", true);
		Log.d("Retrieving", "music_on: " + musicOn);
		return musicOn;
	}
	
	public static void setMusicOn(Context context, boolean musicOn) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES, MODE);
		SharedPreferences.Editor editor = pref.edit();
		
		Log.d("Saving", "music_on: " + musicOn);
		editor.putBoolean("music_on", musicOn);
		
		Log.d("Saving", "committing...");
		editor.commit();
		Log.d("Saving", "committed.");
	}
	
}
