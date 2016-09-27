package com.hackers.promocatch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hackers.promocatch.R;

public class LanguageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_language);
	}

	public void sinhala(View view) {

		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		//intent.putExtra("language", MainActivity.LANGUAGE_SINHALA);
		startActivity(intent);
		finish();
	}

	public void tamil(View view) {

		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		//intent.putExtra("language", MainActivity.LANGUAGE_TAMIL);
		startActivity(intent);
		finish();
	}

	public void english(View view) {

 		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		//intent.putExtra("language", MainActivity.LANGUAGE_ENGLISH);
		startActivity(intent);
		finish();
	}

}
