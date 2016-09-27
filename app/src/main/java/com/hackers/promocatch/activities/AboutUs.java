package com.hackers.promocatch.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import com.hackers.promocatch.R;

public class AboutUs extends Activity {

	LinearLayout rate;
	LinearLayout moreapps;
	LinearLayout close;
	LinearLayout appsupport;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);

		rate = (LinearLayout) findViewById(R.id.RateLayout);
		moreapps = (LinearLayout) findViewById(R.id.MoreAppsLayout);
		close = (LinearLayout) findViewById(R.id.CloseLayout);
		appsupport = (LinearLayout) findViewById(R.id.AppSupportLayout);

		rate.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				Intent i = new Intent(Intent.ACTION_VIEW, Uri
						.parse("market://details?id=lk.bhasha.katchapp"));
				startActivity(i);
			}
		});

		moreapps.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				Intent i = new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://www.bhasha.lk"));
				startActivity(i);
			}
		});

		appsupport.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				Intent emailIntent = new Intent(
						Intent.ACTION_SEND);

				emailIntent.setType("plain/text");
				emailIntent.putExtra(Intent.EXTRA_EMAIL,
						new String[] { "support@bhasha.lk" });
				emailIntent.putExtra(Intent.EXTRA_SUBJECT,
						"Katchapp Inquery");

				startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			}
		});

		close.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

}
