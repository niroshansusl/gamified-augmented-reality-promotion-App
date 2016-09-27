package com.hackers.promocatch.activities;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import com.hackers.promocatch.R;
import com.hackers.promocatch.lanuage.SettUI;

@SuppressWarnings("deprecation")
public class TabViewActivity extends TabActivity {

    public static Context context;

    public int mapIcon = 1;
    public int playIcon = 1;
    public int historyIcon = 1;
    public int infoIcon = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab_view);

        Button hunt = (Button) findViewById(R.id.button2);
        SettUI.setButtonFontSizeBasedOnScreenDensity(TabViewActivity.this, hunt, 20.0f);

        context = this;

        final Resources resources = getResources();
        final TabHost tabHost = getTabHost();

        final int startTab = getIntent().getIntExtra("tab", 0);
        boolean practice = getIntent().getBooleanExtra("practice", false);
        String mode = practice ? "Practice" : "Play";

        int language = getIntent().getIntExtra("language",
                MainActivity.LANGUAGE_ENGLISH);

        mapIcon = R.drawable.account;
        playIcon = R.drawable.cat;
        historyIcon = R.drawable.trophy_b;
        infoIcon = R.drawable.ghost;


        hunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplicationContext(),
                        TabViewActivity.class);
                intent.putExtra("tab", 1);
                intent.putExtra("practice", true);
                intent.putExtra("language", MainActivity.LANGUAGE_ENGLISH);
                startActivity(intent);
                TabViewActivity.this.finish();

            }
        });

        Intent promotionsIntent = new Intent().setClass(this, MainActivity1.class);
        promotionsIntent.putExtra("language", language);
        TabSpec promotionsTab = tabHost.newTabSpec("Promotions")
                .setIndicator("", resources.getDrawable(mapIcon))
                .setContent(promotionsIntent);

        Intent playIntent = new Intent().setClass(this, PlayActivity.class)
                .putExtra("practice", true);
        playIntent.putExtra("tab", 2);
        playIntent.putExtra("language", language);
        TabSpec playTab = tabHost.newTabSpec(mode)
                .setIndicator("", resources.getDrawable(playIcon))
                .setContent(playIntent);

        Intent challangeIntent = new Intent().setClass(this, ChallangeActivity.class);
            challangeIntent.putExtra("tab", 2);
            challangeIntent.putExtra("language", language);
            challangeIntent.putExtra("practice", false);
        TabSpec challangeTab = tabHost.newTabSpec(mode)
                .setIndicator("", resources.getDrawable(playIcon))
                .setContent(challangeIntent);

        Intent historyIntent = new Intent().setClass(this,
                ChallangeActivity.class);
        historyIntent.putExtra("language", language);
        TabSpec historyTab = tabHost.newTabSpec("History")
                .setIndicator("", resources.getDrawable(historyIcon))
                .setContent(historyIntent);

        Intent infoIntent = new Intent().setClass(this, InfoActivity.class);
        infoIntent.putExtra("language", language);
        TabSpec infoTab = tabHost.newTabSpec("Info")
                .setIndicator("", resources.getDrawable(infoIcon))
                .setContent(infoIntent);

        TabSpec emptyTab = tabHost.newTabSpec(" ")
                .setIndicator("", null)
                .setContent(new TabHost.TabContentFactory() {

                    @Override
                    public View createTabContent(String tag) {
                        return new View(TabViewActivity.this);
                    }
                });

        tabHost.addTab(promotionsTab);
        tabHost.addTab(playTab);
        tabHost.addTab(emptyTab);
        tabHost.addTab(historyTab);
        tabHost.addTab(infoTab);

        tabHost.setCurrentTab(startTab);

        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            if (i == startTab) {
                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())
                        .setBackgroundColor(Color.parseColor("#ffffff"));//m
                continue;
            }
            if (i == 0)
                tabHost.getTabWidget().getChildAt(i)
                        .setBackgroundColor(Color.parseColor("#ffffff"));
            else
                tabHost.getTabWidget().getChildAt(i)
                        .setBackgroundColor(Color.parseColor("#ffffff"));
        }

        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                // TODO Auto-generated method stub
                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    tabHost.getTabWidget().getChildAt(i)
                            .setBackgroundColor(Color.parseColor("#ffffff")); // unselected
                }
                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())
                        .setBackgroundColor(Color.parseColor("#ffffff")); // selected
            }
        });
    }

}
