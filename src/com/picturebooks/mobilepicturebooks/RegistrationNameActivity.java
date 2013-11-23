package com.picturebooks.mobilepicturebooks;

import com.picturebooks.mobilepicturebooks.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationNameActivity extends Activity {
	
	private EditText txtName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_register_name);
		
		txtName = (EditText) findViewById(R.id.registration_txt_name);
		txtName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				Button btnNext = (Button) findViewById(R.id.registrationname_btn_next);
				if (s.length() > 0) {
					btnNext.setVisibility(Button.VISIBLE);
				}
				else {
					btnNext.setVisibility(Button.INVISIBLE);
				}
			}
		});
	}
	
	public void clicked_btnBack(View v) {
        Intent mainIntent = new Intent(RegistrationNameActivity.this, MenuActivity.class);
        startActivity(mainIntent);
        finish();
	}
	
	public void clicked_btnNext(View v) {
        Intent mainIntent = new Intent(RegistrationNameActivity.this, RegistrationAgeActivity.class);
        mainIntent.putExtra("user_name", txtName.getText().toString());
        startActivity(mainIntent);
        finish();
	}
	
}
