package com.hackers.promocatch.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hackers.promocatch.R;
import com.hackers.promocatch.model.LinearLayoutCarousel;
import com.hackers.promocatch.utill.ImageViewPlus;

public class MyFragment extends Fragment {

    public static Fragment newInstance(MainActivity1 context, int pos, float scale) {
        Bundle b = new Bundle();
        b.putInt("pos", pos);
        b.putFloat("scale", scale);
        return Fragment.instantiate(context, MyFragment.class.getName(), b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        LinearLayout l = (LinearLayout)
                inflater.inflate(R.layout.adapterpager_item, container, false);

        int pos = this.getArguments().getInt("pos");

        ImageViewPlus im = (ImageViewPlus) l.findViewById(R.id.carousel_background);

        if(pos == 0){
            im.setImageDrawable(getResources().getDrawable(R.drawable.challenge1));
        } else if(pos == 2){
            im.setImageDrawable(getResources().getDrawable(R.drawable.challenge1));
        } else {
            im.setImageDrawable(getResources().getDrawable(R.drawable.challenge2));
        }

        LinearLayoutCarousel root = (LinearLayoutCarousel) l.findViewById(R.id.root);
        float scale = this.getArguments().getFloat("scale");
        root.setScaleBoth(scale);

        return l;
    }
}
