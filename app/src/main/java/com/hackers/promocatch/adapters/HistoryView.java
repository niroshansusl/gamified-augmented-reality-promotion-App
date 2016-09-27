package com.hackers.promocatch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackers.promocatch.R;
import com.hackers.promocatch.activities.MainActivity;
import com.hackers.promocatch.model.Butterfly;
import com.hackers.promocatch.utill.TextViewPlus;

import java.util.List;

public class HistoryView extends ArrayAdapter<Butterfly> {

	private List<Butterfly> items;
	Context context;
	int langid;

	public HistoryView(Context context, int textViewResourceId,
			List<Butterfly> objects, int langid) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.items = objects;
		this.langid = langid;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (v == null) {
			v = vi.inflate(R.layout.history, null);
		}

		if (items.get(position) != null) {

			ImageView butterfly = (ImageView) v.findViewById(R.id.butterflyImg);

			TextViewPlus catchtime = (TextViewPlus) v
					.findViewById(R.id.txtCatchTime);

			TextViewPlus refNo = (TextViewPlus) v.findViewById(R.id.txtRefNo);

			TextView reward = (TextView) v.findViewById(R.id.txtReward);

			String languageCaught;
			String refID;

				languageCaught = context.getResources().getString(
						R.string.caughton_en);
				refID = context.getResources().getString(
						R.string.refID_en);

			catchtime.setText(languageCaught + " "
					+ items.get(position).getCtachTimeStamp());

			refNo.setText(refID + " " + items.get(position).getButterflyID());

			reward.setText(items.get(position).getReward());

			String Colour = items.get(position).getColour();

			if (Colour.equalsIgnoreCase("Blue")) {
				butterfly.setImageResource(R.drawable.game_ball_blue);
			} else if (Colour.equalsIgnoreCase("Gold")) {
				butterfly.setImageResource(R.drawable.game_ball_gold);
			} else if (Colour.equalsIgnoreCase("Green")) {
				butterfly.setImageResource(R.drawable.game_ball_green);
			} else if (Colour.equalsIgnoreCase("Purple")) {
				butterfly.setImageResource(R.drawable.game_ball_purple);
			} else if (Colour.equalsIgnoreCase("Red")) {
				butterfly.setImageResource(R.drawable.game_ball_red);
			} else {
				butterfly.setImageResource(R.drawable.game_ball_blue);
			}

		}

		return v;
	}

}
