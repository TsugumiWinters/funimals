package com.picturebooks.mobilepicturebooks;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.picturebooks.mobilepicturebooks.database_entities.UserInformation;

public class ActiveUser {
	
	private static String PREFERENCES = "funimals_active";
	private static int MODE = Activity.MODE_PRIVATE;
	private static UserInformation ACTIVE_USER;
	
	public static UserInformation getActiveUser(Context context) {
		if (ACTIVE_USER == null) {
			SharedPreferences pref = context.getSharedPreferences(PREFERENCES, MODE);
			
			ACTIVE_USER.setImage(pref.getInt("user_img", 0));
			ACTIVE_USER.setName(pref.getString("user_name", null));
			ACTIVE_USER.setAge(pref.getInt("user_age", 0));
			ACTIVE_USER.setLevel(pref.getString("user_level", null));
		}
		
		if (ACTIVE_USER.getName() == null) {
			ACTIVE_USER = null;
		}
		
		return ACTIVE_USER;
	}
	
	public static void setActiveUser(Context context, int img, String name, int age, String level) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES, MODE);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putInt("user_img", img);
		editor.putString("user_name", name);
		editor.putInt("user_age", age);
		editor.putString("user_level", level);
		
		editor.commit();
	}
	
}
