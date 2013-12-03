package com.picturebooks.funimals;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.picturebooks.funimals.models.ActiveUser;
import com.picturebooks.funimals.models.Preferences;
import com.picturebooks.funimals.models.UserInformation;
import com.picturebooks.funimals.R;

public class HomeActivity extends Activity {
	
	private boolean openingActivity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        openingActivity = false;
        
        Log.d("HomeActivity", "Activity created.");
        
        boolean musicOn = Preferences.getMusicOn(this);
        setMusic(musicOn);
        
        if (!musicOn) {
        	clicked_btnSound(findViewById(R.id.home_btn_sound));
        }
        
        FrameLayout frame = (FrameLayout) findViewById(R.id.home_usercanvas);
		LayoutInflater inflater = getLayoutInflater();
        View userCanvas = inflater.inflate(R.layout.user_canvas, frame, false);
        
        ImageView imgUser = (ImageView) userCanvas.findViewById(R.id.canvas_img_user);
        TextView txtUser = (TextView) userCanvas.findViewById(R.id.canvas_name);
        
        UserInformation user = ActiveUser.getActiveUser(this);
        
        if (user == null) {
            Intent mainIntent = new Intent(HomeActivity.this, MenuActivity.class);
            startActivity(mainIntent);
        }

        imgUser.setImageResource(user.getImage());
        txtUser.setText(user.getName());
        frame.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
		        Intent mainIntent = new Intent(HomeActivity.this, MenuActivity.class);
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
        Intent mainIntent = new Intent(HomeActivity.this, PictureEditor.class);
        startActivity(mainIntent);
	}
	
	public void clicked_btnViewLibrary(View v) {
        Intent mainIntent = new Intent(HomeActivity.this, BookActivity.class);
        startActivity(mainIntent);
	}
	
	public void clicked_btnSeeTutorial(View v) {
		Intent mainIntent = new Intent(HomeActivity.this, PictureEditor.class);
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
