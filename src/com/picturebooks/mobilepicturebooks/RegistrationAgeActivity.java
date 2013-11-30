package com.picturebooks.mobilepicturebooks;

import com.picturebooks.mobilepicturebooks.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class RegistrationAgeActivity extends Activity {
	
	private int age;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_register_age);
		age = 0;
	}
	
	public void clicked_btnAge(View v) {
		ImageView btnAge6 = (ImageView) findViewById(R.id.registrationage_btn_6yrs);
		ImageView btnAge7 = (ImageView) findViewById(R.id.registrationage_btn_7yrs);
		ImageView btnAge8 = (ImageView) findViewById(R.id.registrationage_btn_8yrs);
		btnAge6.setImageResource(R.drawable.general_six);
		btnAge7.setImageResource(R.drawable.general_seven);
		btnAge8.setImageResource(R.drawable.general_eight);
		
		age = Integer.parseInt("" + v.getContentDescription().charAt(0));
		String resource = v.getTag().toString() + "tapped";
		((ImageView) v).setImageResource(getResources().getIdentifier(resource, "drawable", this.getPackageName()));
		
		ImageView btnNext = (ImageView) findViewById(R.id.registrationage_btn_next);
		btnNext.setVisibility(View.VISIBLE);
	}
	
	public void clicked_btnBack(View v) {
        Intent mainIntent = new Intent(RegistrationAgeActivity.this, RegistrationNameActivity.class);
        startActivity(mainIntent);
        finish();
	}
	
	public void clicked_btnNext(View v) {
        Intent mainIntent = new Intent(RegistrationAgeActivity.this, RegistrationLevelActivity.class);
        mainIntent.putExtra("user_name", getIntent().getStringExtra("user_name"));
        mainIntent.putExtra("user_age", age);
        startActivity(mainIntent);
        finish();
	}
	
}
