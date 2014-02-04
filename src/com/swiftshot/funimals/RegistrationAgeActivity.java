package com.swiftshot.funimals;

import com.swiftshot.funimals.R;
import com.swiftshot.funimals.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class RegistrationAgeActivity extends Activity {
	
	private int age;
	
	private Bitmap icon;
	private BitmapFactory.Options options;
	private Drawable bg;
	private RelativeLayout generalBackground;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_register_age);
		age = 0;
		
		options = new BitmapFactory.Options(); 
		options.inPurgeable = true;
        
        generalBackground = (RelativeLayout) findViewById(R.id.age_bg);
        icon = BitmapFactory.decodeResource(getResources(),R.drawable.general_background, options);
		bg = new BitmapDrawable(getResources(), icon);
		generalBackground.setBackground(bg);
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
	}
	
	public void clicked_btnNext(View v) {
        Intent mainIntent = new Intent(RegistrationAgeActivity.this, RegistrationLevelActivity.class);
        mainIntent.putExtra("user_name", getIntent().getStringExtra("user_name"));
        mainIntent.putExtra("user_age", age);
        startActivity(mainIntent);
	}
	
}
