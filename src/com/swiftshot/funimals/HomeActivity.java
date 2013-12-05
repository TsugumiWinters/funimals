package com.swiftshot.funimals;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swiftshot.funimals.R;
import com.swiftshot.funimals.models.ActiveUser;
import com.swiftshot.funimals.models.Preferences;
import com.swiftshot.funimals.models.UserInformation;

public class HomeActivity extends Activity {
	
	private boolean openingActivity;
	private Bitmap icon, icon2;
	private Drawable bg, bg2;
	private BitmapFactory.Options options;
	private ImageView general_title;
	private RelativeLayout homeBackground;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        openingActivity = false;
        
        general_title = (ImageView) findViewById(R.id.home_title);
        homeBackground = (RelativeLayout) findViewById(R.id.home_relative);
        
        options = new BitmapFactory.Options();
		options.inPurgeable = true;
		
        icon = BitmapFactory.decodeResource(getResources(),R.drawable.general_title, options);
		bg = new BitmapDrawable(getResources(), icon);    
		general_title.setImageDrawable(bg);
		
		icon2 = BitmapFactory.decodeResource(getResources(),R.drawable.general_background, options);
		bg2 = new BitmapDrawable(getResources(), icon2);
		homeBackground.setBackground(bg2);
		
        Log.d("HomeActivity", "Activity created.");
        
        boolean musicOn = Preferences.getMusicOn(this);
        setMusic(musicOn);
        
        if (!musicOn) {
        	clicked_btnSound(findViewById(R.id.home_btn_sound));
        }
        
        FrameLayout frame = (FrameLayout) findViewById(R.id.home_usercanvas);
		LayoutInflater inflater = getLayoutInflater();
        View userCanvas = inflater.inflate(R.layout.user_canvas, frame, false);
        
        final ImageView imgUser = (ImageView) userCanvas.findViewById(R.id.canvas_img_user);
        TextView txtUser = (TextView) userCanvas.findViewById(R.id.canvas_name);
        
        UserInformation user = ActiveUser.getActiveUser(this);
        
        if (user == null) {
            Intent mainIntent = new Intent(HomeActivity.this, AccountsActivity.class);
            startActivity(mainIntent);
        }

        imgUser.setImageResource(user.getImage());
        txtUser.setText(user.getName());
        frame.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				imgUser.setColorFilter(Color.rgb(123,73,122), android.graphics.PorterDuff.Mode.DARKEN );
		        Intent mainIntent = new Intent(HomeActivity.this, AccountsActivity.class);
		        startActivity(mainIntent);
			}
        	
        });
		
        frame.addView(userCanvas);
       
	}
	
	public void clicked_btnSound(View v) {
		ImageView img = (ImageView) v;
		boolean musicOn = img.getContentDescription().equals(this.getResources().getString(R.string.button_sound_on));
		
		if (musicOn) {
			img.setImageResource(R.drawable.general_soundoff);
			img.setContentDescription(this.getResources().getString(R.string.button_sound_off));
		}
		else {
			img.setImageResource(R.drawable.general_soundon);
			img.setContentDescription(this.getResources().getString(R.string.button_sound_on));
		}
		
		setMusic(!musicOn);
		Preferences.setMusicOn(this, !musicOn);
	}
	
	public void clicked_btnInfo(View v) {
		Intent mainIntent = new Intent(HomeActivity.this, InfoActivity.class);
        startActivity(mainIntent);
	}
	
	public void clicked_btnNewPicture(View v) {
        Intent mainIntent = new Intent(HomeActivity.this, PictureEditorActivity.class);
        startActivity(mainIntent);
	}
	
	public void clicked_btnViewLibrary(View v) {
        Intent mainIntent = new Intent(HomeActivity.this, UserbookActivity.class);
        startActivity(mainIntent);
	}
	
	public void clicked_btnSeeTutorial(View v) {
		Intent mainIntent = new Intent(HomeActivity.this, PictureEditorActivity.class);
		mainIntent.putExtra("tutorial", 1);
        startActivity(mainIntent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (!openingActivity) {
			ActiveUser.clearActiveUser(this);
		}
	}
	
	private void setMusic(boolean musicOn) {
		if (musicOn) {
			// TODO logic for opening music
		}
		else {
			// TODO logic for closing music
		}
	}
    
}
