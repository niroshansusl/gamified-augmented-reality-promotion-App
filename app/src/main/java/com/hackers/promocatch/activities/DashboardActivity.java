package com.hackers.promocatch.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.hackers.promocatch.R;

@SuppressWarnings("deprecation")
public class DashboardActivity extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dashboard);

		Resources resources = getResources();
		TabHost tabHost = getTabHost();

		Intent promotionsIntent = new Intent()
				.setClass(this, MapActivity.class);
		TabSpec promotionsTab = tabHost
				.newTabSpec("Promotions")
				.setIndicator("",
						resources.getDrawable(R.drawable.dashboard_map))
				.setContent(promotionsIntent);

		Intent practiceIntent = new Intent().setClass(this, PlayActivity.class)
				.putExtra("practice", true);
		TabSpec practiceTab = tabHost
				.newTabSpec("Practice")
				.setIndicator(
						"",
						resources
								.getDrawable(R.drawable.dashboard_practiceplay))
				.setContent(practiceIntent);

		Intent playIntent = new Intent().setClass(this, PlayActivity.class)
				.putExtra("practice", false);
		TabSpec playTab = tabHost
				.newTabSpec("Play")
				.setIndicator("",
						resources.getDrawable(R.drawable.dashboard_liveplay))
				.setContent(playIntent);

		Intent historyIntent = new Intent().setClass(this,
				HistoryActivity.class);
		TabSpec historyTab = tabHost
				.newTabSpec("History")
				.setIndicator("",
						resources.getDrawable(R.drawable.dashboard_history))
				.setContent(historyIntent);

		tabHost.addTab(promotionsTab);
		tabHost.addTab(practiceTab);
		tabHost.addTab(playTab);
		tabHost.addTab(historyTab);

		// set Windows tab as default (zero based)
		tabHost.setCurrentTab(0);
	}

}
