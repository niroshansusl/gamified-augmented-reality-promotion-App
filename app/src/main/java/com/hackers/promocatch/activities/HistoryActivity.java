package com.hackers.promocatch.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import com.hackers.promocatch.R;
import com.hackers.promocatch.adapters.HistoryView;
import com.hackers.promocatch.model.Butterfly;
import com.hackers.promocatch.utill.DatabeaseHandler;

import java.util.List;

public class HistoryActivity extends Activity {

	HistoryView historyadapter;

	DatabeaseHandler db;

	public static int language = MainActivity.LANGUAGE_ENGLISH;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);

		db = new DatabeaseHandler(this);

		language = getIntent().getIntExtra("language",
				MainActivity.LANGUAGE_ENGLISH);

		Log.e("HisLanguage", "" + language);

		List<Butterfly> cList = db.fethall();

		historyadapter = new HistoryView(this, R.layout.history, cList, language);

		ListView listView = (ListView) findViewById(R.id.historyList);

		listView.setAdapter(historyadapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

}
