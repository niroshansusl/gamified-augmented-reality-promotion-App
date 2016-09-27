package com.hackers.promocatch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.hackers.promocatch.R;

public class ChallangeAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public ChallangeAdapter(Context context, String[] values) {
        super(context, R.layout.cell_challange_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.cell_challange_item, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imgItem);

        String s = values[position];

        if (position == 0) {
            imageView.setImageResource(R.drawable.challenge1);
        } else if (position == 1) {
            imageView.setImageResource(R.drawable.challenge2);
        } else if (position == 2) {
            imageView.setImageResource(R.drawable.challenge1);
        }else if (position == 3) {
            imageView.setImageResource(R.drawable.challenge2);
        }

        return rowView;
    }
}
