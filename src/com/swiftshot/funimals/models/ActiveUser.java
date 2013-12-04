package com.swiftshot.funimals.models;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class ActiveUser {
	
	private static final String PREFERENCES = "funimals_active";
	private static final int MODE = Activity.MODE_PRIVATE;
	private static UserInformation ACTIVE_USER = null;
	
	public static UserInformation getActiveUser(Context context) {
		if (ACTIVE_USER == null) {
			Log.d("Retrieving", "ACTIVE_USER is null");
			
			SharedPreferences pref = context.getSharedPreferences(PREFERENCES, MODE);
			
			ACTIVE_USER = new UserInformation();
			
			ACTIVE_USER.setImage(pref.getInt("user_img", 0));
			ACTIVE_USER.setName(pref.getString("user_name", null));
			ACTIVE_USER.setAge(pref.getInt("user_age", 0));
			ACTIVE_USER.setLevel(pref.getString("user_level", null));
		}
		Log.d("Retrieving", "ACTIVE_USER is " + ACTIVE_USER.getName());
		Log.d("Retrieving", "ACTIVE_USER image is " + ACTIVE_USER.getImage());
		Log.d("Retrieving", "ACTIVE_USER is " + ACTIVE_USER.getAge() + " years old");
		Log.d("Retrieving", "ACTIVE_USER is in " + ACTIVE_USER.getLevel());
		
		if (ACTIVE_USER.getName() == null) {
			ACTIVE_USER = null;
			Log.d("Retrieving", "Retrieved ACTIVE_USER is null");
		}
		
		return ACTIVE_USER;
	}
	
	public static void setActiveUser(Context context, int img, String name, int age, String level) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES, MODE);
		SharedPreferences.Editor editor = pref.edit();
		
		Log.d("Saving", "user_img: " + img);
		editor.putInt("user_img", img);
		Log.d("Saving", "user_name: " + name);
		editor.putString("user_name", name);
		Log.d("Saving", "user_age: " + age);
		editor.putInt("user_age", age);
		Log.d("Saving", "user_level: " + level);
		editor.putString("user_level", level);
		
		Log.d("Saving", "committing...");
		editor.commit();
		Log.d("Saving", "committed.");
		
		if(ACTIVE_USER == null) {		
			ACTIVE_USER = new UserInformation();
		}
		
		ACTIVE_USER.setImage(img);
		ACTIVE_USER.setName(name);
		ACTIVE_USER.setAge(age);
		ACTIVE_USER.setLevel(level);
		
	}
	
	public static void clearActiveUser(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES, MODE);
		SharedPreferences.Editor editor = pref.edit();
		Log.d("Clearing", "Clearing preferences...");
		editor.remove("user_img");
		editor.remove("user_name");
		editor.remove("user_age");
		editor.remove("user_level");
		editor.commit();
		Log.d("Clearing", "Cleared preferences.");
	}
	
}
