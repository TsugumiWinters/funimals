package com.picturebooks.mobilepicturebooks;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.picturebooks.mobilepicturebooks.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
		
		level = v.getContentDescription().toString();
		String resource = v.getTag().toString() + "tapped";
		((ImageView) v).setImageResource(getResources().getIdentifier(resource, "drawable", this.getPackageName()));
		
		if (btnPrep != v) {
			btnPrep.setImageResource(R.id.registrationlevel_btn_prep);
		}
		if (btnGrade1 != v) {
			btnGrade1.setImageResource(R.id.registrationlevel_btn_grade1);
		}
		if (btnGrade2 != v) {
			btnGrade2.setImageResource(R.id.registrationlevel_btn_grade2);
		}
		if (btnGrade3 != v) {
			btnGrade3.setImageResource(R.id.registrationlevel_btn_grade3);
		}
		
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
		/* TODO: Save user to db */
		
		ActiveUser.setActiveUser(this,
				getRandomImage(),
				getIntent().getStringExtra("user_name"),
				getIntent().getIntExtra("age", 6),
				level);
		
		Intent mainIntent = new Intent(RegistrationLevelActivity.this, HomeActivity.class);
        startActivity(mainIntent);
        finish();
	}
	
	private int getRandomImage() {
		Random random = new Random();
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
	    	id = random.nextInt(drawables.size());
	    }
	    catch (Exception e) {}
	    
	    return id;
	}
	
}
