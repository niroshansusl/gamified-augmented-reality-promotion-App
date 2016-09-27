package com.hackers.promocatch.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;

import com.hackers.promocatch.R;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		PlayActivity.gcmLoaded = false;
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean downStatus = prefs.getBoolean("SplashSound", true);

		if (downStatus) {
			//MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.dialog);
			//mPlayer.start();
		}
		// to make gcm to initialize only once per launch

		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);

					Intent intent = new Intent(getApplicationContext(),
							TabViewActivity.class);
					intent.putExtra("tab", 0);
					intent.putExtra("language", MainActivity.LANGUAGE_ENGLISH);
					intent.putExtra("practice", false);
					startActivity(intent);

					/*startActivity(new Intent(getApplicationContext(),
							MainActivity1.class).putExtra("language", MainActivity.LANGUAGE_ENGLISH));*/
					finish();
				} catch (InterruptedException e) {

					Intent intent = new Intent(getApplicationContext(),
							TabViewActivity.class);
					intent.putExtra("tab", 0);
					intent.putExtra("language", MainActivity.LANGUAGE_ENGLISH);
					intent.putExtra("practice", false);
					startActivity(intent);

					/*startActivity(new Intent(getApplicationContext(),
							MainActivity1.class).putExtra("language", MainActivity.LANGUAGE_ENGLISH));*/
					finish();
				}
			}
		};
		t.start();

	}

}
