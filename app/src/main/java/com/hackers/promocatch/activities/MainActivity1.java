package com.hackers.promocatch.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import com.hackers.promocatch.R;
import com.hackers.promocatch.adapters.CarouselPagerAdapter;
import com.hackers.promocatch.lanuage.SettUI;
import com.hackers.promocatch.utill.ImageViewPlus;
import com.hackers.promocatch.utill.TextViewPlus;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity1 extends FragmentActivity {

	public static final int LANGUAGE_ENGLISH = 3;

	public static int language = LANGUAGE_ENGLISH;
	public final static int PAGES = 4;
	public final static int LOOPS = 1000;
	public final static int FIRST_PAGE = PAGES * LOOPS / 2;

	public CarouselPagerAdapter adapter;

	ImageViewPlus img;
	LinearLayout middle;
	boolean buttonState = false;
	TextViewPlus startcatching;
	TextViewPlus textChallange;
	TextViewPlus textpoints;
	TextViewPlus textName;
	ViewPager viewpager;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setDefaultFontPath("Montserrat-Black.otf")
				.setFontAttrId(R.attr.fontPath)
				.build());
		setContentView(R.layout.activity_main2);
		language = getIntent().getIntExtra("language", LANGUAGE_ENGLISH);

		Log.e("LangMain", "" + language);

		img = (ImageViewPlus) findViewById(R.id.background);
		startcatching = (TextViewPlus) findViewById(R.id.startcatching);
		textChallange = (TextViewPlus) findViewById(R.id.textChallange);
		textpoints = (TextViewPlus) findViewById(R.id.textpoints);
		textName = (TextViewPlus) findViewById(R.id.textName);
		viewpager = (ViewPager) findViewById(R.id.viewpager);

		SettUI.setTextViewFontSizeBasedOnScreenDensity(this, startcatching, 18.0f);
		SettUI.setTextViewFontSizeBasedOnScreenDensity(this, textChallange, 23.0f);
		SettUI.setTextViewFontSizeBasedOnScreenDensity(this, textpoints, 27.0f);
		SettUI.setTextViewFontSizeBasedOnScreenDensity(this, textName, 20.0f);

		startcatching.setText(getResources().getString(R.string.startcaching_en));

		adapter = new CarouselPagerAdapter(this, this.getSupportFragmentManager());
		viewpager.setAdapter(adapter);
		viewpager.setPageTransformer(false, adapter);
		viewpager.setCurrentItem(FIRST_PAGE);
		viewpager.setOffscreenPageLimit(3);
		viewpager.setPageMargin(-230);

	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	public void catchPress(View view) {
		buttonState = true;
		//img.setImageResource(R.drawable.dashboard_button_back2);

		Animation animationfadein = AnimationUtils.loadAnimation(this,
				R.anim.fadein);

		middle.setVisibility(View.VISIBLE);

		middle.startAnimation(animationfadein);

		//catchButton.setVisibility(View.GONE);
	}

	public void map() {
		Intent intent = new Intent(getApplicationContext(),
				TabViewActivity.class);
		intent.putExtra("tab", 0);
		intent.putExtra("language", language);
		intent.putExtra("practice", false);
		startActivity(intent);
	}

	public void play(View view) {

		if (isConnectingToInternet()) {
			Intent intent = new Intent(getApplicationContext(),
					TabViewActivity.class);
			intent.putExtra("tab", 1);
			intent.putExtra("practice", false);
			intent.putExtra("language", language);
			startActivity(intent);
		} else {
			String languageSting;

				languageSting = getResources().getString(
						R.string.nointernet_en);

			SettUI.createDialog2(this, languageSting, this).show();
		}

	}

	public void practice(View view) {
		Intent intent = new Intent(getApplicationContext(),
				TabViewActivity.class);
		intent.putExtra("tab", 1);
		intent.putExtra("practice", true);
		intent.putExtra("language", language);
		startActivity(intent);
	}

	public void history(View view) {
		Intent intent = new Intent(getApplicationContext(),
				TabViewActivity.class);
		intent.putExtra("tab", 2);
		intent.putExtra("language", language);
		intent.putExtra("practice", false);
		startActivity(intent);
	}

	public void info(View view) {
		Intent intent = new Intent(getApplicationContext(),
				TabViewActivity.class);
		intent.putExtra("tab", 3);
		intent.putExtra("practice", false);
		intent.putExtra("language", language);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (buttonState) {
			middle.setVisibility(View.GONE);
			//img.setImageResource(R.drawable.dashboard_button_back);
			Animation animationfadein = AnimationUtils.loadAnimation(this,
					R.anim.fadein);
			//catchButton.startAnimation(animationfadein);
			//catchButton.setVisibility(View.VISIBLE);
			buttonState = false;
		} else {
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent i = new Intent(this, PreferenceActivity.class);
			startActivity(i);
			break;
		case R.id.action_aboutus:
			Intent i2 = new Intent(this, AboutUs.class);
			startActivity(i2);
			break;
		}
		return super.onOptionsItemSelected(item);

	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

}
