package com.swiftshot.funimals;

import com.swiftshot.funimals.R;
import com.swiftshot.funimals.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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
				ImageView btnNext = (ImageView) findViewById(R.id.registrationname_btn_next);
				String result = s.toString().trim();
				
				if (result.length() > 0) {
					btnNext.setVisibility(View.VISIBLE);
				}
				else {
					btnNext.setVisibility(View.GONE);
				}
			}
		});
	}
	
	public void clicked_btnBack(View v) {
        Intent mainIntent = new Intent(RegistrationNameActivity.this, AccountsActivity.class);
        startActivity(mainIntent);
	}
	
	public void clicked_btnNext(View v) {
        Intent mainIntent = new Intent(RegistrationNameActivity.this, RegistrationAgeActivity.class);
        mainIntent.putExtra("user_name", txtName.getText().toString());
        startActivity(mainIntent);
	}
	
}
