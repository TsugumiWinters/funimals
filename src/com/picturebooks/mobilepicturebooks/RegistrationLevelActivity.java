package com.picturebooks.mobilepicturebooks;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.picturebooks.mobilepicturebooks.R.layout;
import com.picturebooks.mobilepicturebooks.models.ActiveUser;

import database.DatabaseHelper;
import database_entities.UserInformation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class RegistrationLevelActivity extends Activity {
	
	private String level;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_register_yearlevel);
		level = null;
	}
	
	public void clicked_btnLevel(View v) {
		ImageView btnPrep = (ImageView) findViewById(R.id.registrationlevel_btn_prep);
		ImageView btnGrade1 = (ImageView) findViewById(R.id.registrationlevel_btn_grade1);
		ImageView btnGrade2 = (ImageView) findViewById(R.id.registrationlevel_btn_grade2);
		ImageView btnGrade3 = (ImageView) findViewById(R.id.registrationlevel_btn_grade3);
		
		btnPrep.setImageResource(R.drawable.general_prep);
		btnGrade1.setImageResource(R.drawable.general_grade1);
		btnGrade2.setImageResource(R.drawable.general_grade2);
		btnGrade3.setImageResource(R.drawable.general_grade3);
		
		level = v.getContentDescription().toString();
		String resource = v.getTag().toString() + "tapped";
		((ImageView) v).setImageResource(getResources().getIdentifier(resource, "drawable", this.getPackageName()));

		ImageView btnNext = (ImageView) findViewById(R.id.registrationlevel_btn_next);
		btnNext.setVisibility(View.VISIBLE);
	}
	
	public void clicked_btnBack(View v) {
        Intent mainIntent = new Intent(RegistrationLevelActivity.this, RegistrationAgeActivity.class);
        mainIntent.putExtra("user_name", getIntent().getStringExtra("user_name"));
        startActivity(mainIntent);
        finish();
	}
	
	public void clicked_btnNext(View v) {
		UserInformation user = new UserInformation();
		user.setUsername(getIntent().getStringExtra("user_name"));
		user.setAge(getIntent().getIntExtra("user_age", 6));
		user.setGrade((level.equals("Prep")) ? 0 : Integer.parseInt("" + level.charAt(level.length() - 1)));
		
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		dbHelper.openDataBase();
		dbHelper.addUserInformation(user);
		dbHelper.close();
		
		ActiveUser.setActiveUser(this,
				getNewImage(),
				getIntent().getStringExtra("user_name"),
				getIntent().getIntExtra("user_age", 6),
				level);
		
		Intent mainIntent = new Intent(RegistrationLevelActivity.this, HomeActivity.class);
        startActivity(mainIntent);
        finish();
	}
	
	private int getNewImage() {
		Field[] fields = R.drawable.class.getFields();
	    List<Integer> drawables = new ArrayList<Integer>();
	    
	    for (Field field : fields) {
	        /* Take only those with name starting with "users" */
	        if (field.getName().startsWith("users")) {
	            try {
	            	if ( !(field.getName().contains("default") || field.getName().contains("add"))) {
	            		drawables.add(field.getInt(null));
	            	}
				} catch (Exception e) {}
	        }
	    }
		
	    int id = R.drawable.users0;
	    try {
	    	DatabaseHelper dbHelper = new DatabaseHelper(this);
	    	dbHelper.openDataBase();
	    	int index = dbHelper.getUserInformation().size() - 1;
	    	id = drawables.get(index);
	    	Log.d("Chosen Image", "User No." + index);
	    }
	    catch (Exception e) {}
	    
	    return id;
	}
	
}
