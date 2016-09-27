package com.hackers.promocatch.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.hackers.promocatch.R;
import com.hackers.promocatch.activities.MainActivity1;
import com.hackers.promocatch.activities.MyFragment;
import com.hackers.promocatch.model.LinearLayoutCarousel;

public class CarouselPagerAdapter extends FragmentPagerAdapter implements ViewPager.PageTransformer {
    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;

    private LinearLayoutCarousel cur = null;
    private LinearLayoutCarousel next = null;
    private MainActivity1 context;
    private FragmentManager fm;
    private float scale;

    public CarouselPagerAdapter(MainActivity1 context, FragmentManager fm) {
        super(fm);
        this.fm = fm;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        // make the first pager bigger than others
        if (position == MainActivity1.FIRST_PAGE)
            scale = BIG_SCALE;
        else
            scale = SMALL_SCALE;

        position = position % MainActivity1.PAGES;
        return MyFragment.newInstance(context, position, scale);
    }

    @Override
    public int getCount() {
        return MainActivity1.PAGES * MainActivity1.LOOPS;
    }

    @Override
    public void transformPage(View page, float position) {
        LinearLayoutCarousel myLinearLayout = (LinearLayoutCarousel) page.findViewById(R.id.root);
        float scale = BIG_SCALE;
        if (position > 0) {
            scale = scale - position * DIFF_SCALE;
        } else {
            scale = scale + position * DIFF_SCALE;
        }
        if (scale < 0) scale = 0;
        myLinearLayout.setScaleBoth(scale);
    }
}
