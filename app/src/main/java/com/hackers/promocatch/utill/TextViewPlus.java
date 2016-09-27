package com.hackers.promocatch.utill;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Custom textview with SETT font applied
 * 
 * @author chamika
 * 
 */
public class TextViewPlus extends TextView {
	private static final String TAG = "TextView";

	Context context;

	public TextViewPlus(Context context) {
		super(context);
		this.context = context;
		setCustomFont(context, "BhashaSETTSinhalaTamil.dat");
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		// TODO Auto-generated method stub

		/** For applying rendering conversion within TextView */
		// SettRenderingEngine sett = new SettRenderingEngine(getContext());
		// CharSequence newText = sett.getSettString(text.toString());
		// super.setText(newText, type);

		super.setText(text, type);

	}

	public TextViewPlus(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(context, attrs);
	}

	public TextViewPlus(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomFont(context, attrs);
	}

	private void setCustomFont(Context ctx, AttributeSet attrs) {
		// TypedArray a = ctx.obtainStyledAttributes(attrs,
		// R.styleable.TextViewPlus);
		// String customFont = a.getString(R.styleable.TextViewPlus_customFont);
		// setCustomFont(ctx, customFont);
		setCustomFont(ctx, "BhashaSETTSinhalaTamil.dat");
		// a.recycle();
	}

	public boolean setCustomFont(Context ctx, String asset) {
		Typeface tf = null;
		try {
			tf = Typeface.createFromAsset(ctx.getAssets(), asset);
		} catch (Exception e) {
			Log.e(TAG, "Could not get typeface: " + e.getMessage());
			return false;
		}

		setTypeface(tf);
		return true;
	}

	public boolean setCustomFont(String asset) {
		Typeface tf = null;
		try {
			tf = Typeface.createFromAsset(context.getAssets(), asset);
		} catch (Exception e) {
			Log.e(TAG, "Could not get typeface: " + e.getMessage());
			return false;
		}

		setTypeface(tf);
		return true;
	}

}
