package com.swiftshot.funimals;

import com.devsmart.android.ui.HorizontalListView;
import com.swiftshot.funimals.R;
import com.swiftshot.funimals.models.ActiveUser;
import com.swiftshot.funimals.models.database.entities.UserInformation;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
/* ORIGINAL
import android.os.Bundle;
import org.apache.cordova.*;

import database.DatabaseHelper;*/
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AccountsActivity extends Activity {
	
	private boolean openingActivity;
	private HorizontalListView accountList;
	private AccountListAdapter adapter;
	
	private Bitmap icon;
	private BitmapFactory.Options options;
	private Drawable bg;
	private RelativeLayout generalBackground;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts);
		openingActivity = false;
		
        Log.d("MenuActivity", "Activity created.");
        
        options = new BitmapFactory.Options(); 
		options.inPurgeable = true;
        
        generalBackground = (RelativeLayout) findViewById(R.id.accounts_bg);
        icon = BitmapFactory.decodeResource(getResources(),R.drawable.general_background, options);
		bg = new BitmapDrawable(getResources(), icon);
		generalBackground.setBackground(bg);
        
		accountList = (HorizontalListView) findViewById(R.id.accounts_list);
		
		/* Create "Add Account" user_canvas */
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.user_canvas, accountList, false);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AccountsActivity.this.clicked_addAccount(v);
			}
		});
		
		ImageView img = (ImageView) view.findViewById(R.id.canvas_img_user);
		TextView name = (TextView) view.findViewById(R.id.canvas_name);
		img.setImageResource(R.drawable.usersadduser);
		img.setContentDescription("usersadduser");
		name.setText("Add Account");
		
		adapter = new AccountListAdapter(this);
		accountList.setAdapter(adapter);
		ActiveUser.clearActiveUser(this);
	}
	
	public void clicked_addAccount(View v) {
        Intent mainIntent = new Intent(AccountsActivity.this, RegistrationNameActivity.class);
        startActivity(mainIntent);
	}
	
	public void clicked_openAccount(View v) {
		ActiveUser.clearActiveUser(this);
		
		ImageView viewImage = (ImageView) v.findViewById(R.id.canvas_img_user);
		TextView txtName = (TextView) v.findViewById(R.id.canvas_name);
       	viewImage.setColorFilter(Color.rgb(123,73,122), android.graphics.PorterDuff.Mode.DARKEN);
       	
		/* Get Selected User */
		UserInformation user = adapter.getUser(txtName.getText().toString());
		Log.d("Selected User", user.getUsername() + " : Age " + user.getAge() + " : Level " + user.getGrade());
		
		int img = getResources().getIdentifier(viewImage.getContentDescription().toString(), "drawable", this.getPackageName());
		String username = user.getUsername();
		int age = user.getAge();
		String level = "Grade " + user.getGrade();
		if (user.getGrade() <= 0) {
			level = "Prep";
		}
		ActiveUser.setActiveUser(this, img, username, age, level);
		
		/* Open HomeActivity */
		Intent mainIntent = new Intent(AccountsActivity.this, HomeActivity.class);
        AccountsActivity.this.startActivity(mainIntent);

	}
	
	@Override
	protected void onResume() {
		ActiveUser.clearActiveUser(this);
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (!openingActivity) {
			ActiveUser.clearActiveUser(this);
		}
	}
}
