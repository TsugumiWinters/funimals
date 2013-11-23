package com.picturebooks.mobilepicturebooks;

import com.picturebooks.mobilepicturebooks.database_entities.UserInformation;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BookActivity extends Activity {
	
	private ImageView imgUser;
	private TextView txtName;
	private TextView txtAge;
	private TextView txtLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userbook);
        
        imgUser = (ImageView) findViewById(R.id.userbook_img_user);
        txtName = (TextView) findViewById(R.id.userbook_name);
        txtAge = (TextView) findViewById(R.id.userbook_age);
        txtLevel = (TextView) findViewById(R.id.userbook_level);
        
        UserInformation user = ActiveUser.getActiveUser(this);
        
        imgUser.setImageResource(user.getImage());
        txtName.setText(user.getName());
        txtAge.setText(getResources().getString(R.string.user_age, user.getAge()));
        txtLevel.setText(user.getLevel());
	}
	
	public void clicked_btnHome(View v) {
        Intent mainIntent = new Intent(BookActivity.this, HomeActivity.class);
        startActivity(mainIntent);
        finish();
	}
	
	public void clicked_btnLibrary(View v) {
        Intent mainIntent = new Intent(BookActivity.this, StoriesActivity.class);
        startActivity(mainIntent);
        finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActiveUser.clearActiveUser(this);
	}
	
}
