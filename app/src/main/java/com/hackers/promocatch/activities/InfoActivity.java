package com.hackers.promocatch.activities;

import android.app.Activity;
import android.os.Bundle;

import com.hackers.promocatch.R;
import com.hackers.promocatch.utill.TextViewPlus;

public class InfoActivity extends Activity {

	TextViewPlus info;
	public static int language;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);

		info = (TextViewPlus) findViewById(R.id.infotext);

		language = getIntent().getIntExtra("language",
				MainActivity.LANGUAGE_ENGLISH);

		String languageSting;

			languageSting = getResources().getString(
					R.string.info_en);


		info.setText(languageSting);
	}

}
