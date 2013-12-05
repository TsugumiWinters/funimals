package com.swiftshot.funimals;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swiftshot.funimals.R;
import com.swiftshot.funimals.models.ActiveUser;
import com.swiftshot.funimals.models.UserInformation;

public class UserbookActivity extends Activity {
	
	private ImageView imgUser;
	private TextView txtName;
	private TextView txtAge;
	private TextView txtLevel;
	private boolean openingActivity;
	private Bitmap icon;
	private Drawable bg;
	private BitmapFactory.Options options;
	private RelativeLayout bookBackground;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userbook);
        openingActivity = false;
        
        bookBackground = (RelativeLayout) findViewById(R.id.book_bg);
        
        options = new BitmapFactory.Options(); 
		options.inPurgeable = true;
        
		icon = BitmapFactory.decodeResource(getResources(),R.drawable.library_openbook, options);
		bg = new BitmapDrawable(getResources(), icon);
		bookBackground.setBackground(bg);
		
        Log.d("BookActivity", "Activity created.");
        
        imgUser = (ImageView) findViewById(R.id.userbook_img_user);
        txtName = (TextView) findViewById(R.id.userbook_name);
        txtAge = (TextView) findViewById(R.id.userbook_age);
        txtLevel = (TextView) findViewById(R.id.userbook_level);
        
        UserInformation user = ActiveUser.getActiveUser(this);
        
        if (user == null) {
            Intent mainIntent = new Intent(UserbookActivity.this, AccountsActivity.class);
            startActivity(mainIntent);
        }
        
        imgUser.setImageResource(user.getImage());
        txtName.setText(user.getName());
        txtAge.setText(getResources().getString(R.string.user_age, user.getAge()));
        txtLevel.setText(user.getLevel());
	}
	
	public void clicked_btnHome(View v) {
        Intent mainIntent = new Intent(UserbookActivity.this, HomeActivity.class);
        startActivity(mainIntent);
	}
	
	public void clicked_btnLibrary(View v) {
        Intent mainIntent = new Intent(UserbookActivity.this, UserStoriesActivity.class);
        startActivity(mainIntent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		bookBackground.setBackgroundResource(0);
		if (!openingActivity) {
			ActiveUser.clearActiveUser(this);
		}
	}
	
}
