package com.hackers.promocatch.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hackers.promocatch.R;
import com.hackers.promocatch.adapters.ChallangeAdapter;
import com.hackers.promocatch.lanuage.SettUI;

public class ChallangeActivity extends ListActivity {

    static final String[] Android =
            new String[]{"Challange1", "Challange2", "Challange3", "Challange4"};

    ListView list;
    TextView title;
    ChallangeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challange);

        list = (ListView) findViewById(android.R.id.list);
        title = (TextView) findViewById(R.id.title);

        SettUI.setTextViewFontSizeBasedOnScreenDensity(this, title, 18.0f);
        adapter = new ChallangeAdapter(this, Android);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        //get selected items
        String selectedValue = (String) getListAdapter().getItem(position);
        Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
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
