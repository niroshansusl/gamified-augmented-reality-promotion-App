package com.hackers.promocatch.lanuage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hackers.promocatch.R;
import com.hackers.promocatch.utill.TextViewPlus;

public class SettUI {

	public static AlertDialog getLoadingDialog(Activity activity,
			Context context, String text) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View layout = inflater.inflate(R.layout.progress_dialog,
				(ViewGroup) activity.findViewById(R.id.layout_root));
		TextViewPlus textView = (TextViewPlus) layout
				.findViewById(R.id.message);
		textView.setText(text);
		textView.setTextSize(16);
		textView.setPadding(5, 5, 20, 20);
		textView.setTextColor(Color.WHITE);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(layout);
		return builder.create();
	}

	public static void showToast(Activity activity, Context context, String txt) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast_layout,
				(ViewGroup) activity.findViewById(R.id.toast_layout_root));
		TextViewPlus text = (TextViewPlus) layout.findViewById(R.id.text);
		text.setText(txt);
		Toast toast = new Toast(context.getApplicationContext());
		toast.setDuration(50);
		toast.setView(layout);
		toast.show();
	}

	public static Dialog createDialog(Context context, String text) {
		Dialog dialog;
		TextViewPlus textViewMessage = new TextViewPlus(context);
		textViewMessage.setText(text);
		textViewMessage.setTextSize(18);
		textViewMessage.setGravity(Gravity.CENTER_HORIZONTAL);
		textViewMessage.setPadding(5, 5, 20, 20);
		textViewMessage.setTextColor(Color.WHITE);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();

					}
				});
		builder.setView(textViewMessage);
		AlertDialog alert = builder.create();
		dialog = alert;
		return dialog;
	}

	public static Dialog createDialog(Context context, String text,
			final Activity activity) {
		Dialog dialog;
		TextViewPlus textViewMessage = new TextViewPlus(context);
		textViewMessage.setText(text);
		textViewMessage.setTextSize(18);
		textViewMessage.setPadding(10, 20, 10, 20);
		textViewMessage.setTextColor(Color.WHITE);

		textViewMessage.setGravity(Gravity.CENTER_HORIZONTAL);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						//activity.finish();
					}
				});
		builder.setView(textViewMessage);
		AlertDialog alert = builder.create();
		dialog = alert;
		return dialog;
	}

	public static Dialog createDialog2(Context context, String text,
			final Activity activity) {
		Dialog dialog;
		TextViewPlus textViewMessage = new TextViewPlus(context);
		textViewMessage.setText(text);
		textViewMessage.setTextSize(18);
		textViewMessage.setPadding(10, 20, 10, 20);
		textViewMessage.setTextColor(Color.WHITE);

		textViewMessage.setGravity(Gravity.CENTER_HORIZONTAL);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();

					}
				});
		builder.setView(textViewMessage);
		AlertDialog alert = builder.create();
		dialog = alert;
		return dialog;
	}

	public static Dialog createDialog(Context context, String title, String text) {
		Dialog dialog;
		TextViewPlus textViewMessage = new TextViewPlus(context);
		textViewMessage.setText(text);
		textViewMessage.setTextSize(18);
		textViewMessage.setPadding(10, 20, 10, 20);
		textViewMessage.setTextColor(Color.WHITE);
		textViewMessage.setGravity(Gravity.CENTER_HORIZONTAL);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		builder.setView(textViewMessage);
		AlertDialog alert = builder.create();
		dialog = alert;
		return dialog;
	}

	public static void setButtonFontSizeBasedOnScreenDensity(Activity activity,
															 Button btn, double size) {

		DisplayMetrics dMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		final float WIDE = activity.getResources().getDisplayMetrics().widthPixels;
		int valueWide = (int) (WIDE / size / (dMetrics.scaledDensity));
		btn.setTextSize(valueWide);
	}

	public static void setButtonFontSizeBasedOnScreenDensity(Activity activity,
															 Button btn, double size, int style) {

		DisplayMetrics dMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		final float WIDE = activity.getResources().getDisplayMetrics().widthPixels;
		int valueWide = (int) (WIDE / size / (dMetrics.scaledDensity));
		btn.setTextSize(valueWide);
		btn.setTypeface(btn.getTypeface(), style);
	}

	public static void setTextViewFontSizeBasedOnScreenDensity(
			Activity activity, TextView tv, double size) {

		DisplayMetrics dMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		final float WIDE = activity.getResources().getDisplayMetrics().widthPixels;
		int valueWide = (int) (WIDE / size / (dMetrics.scaledDensity));
		tv.setTextSize(valueWide);
	}

	public static void setTextViewFontSizeBasedOnScreenDensity(
			Activity activity, TextView tv, double size, int style) {

		DisplayMetrics dMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		final float WIDE = activity.getResources().getDisplayMetrics().widthPixels;
		int valueWide = (int) (WIDE / size / (dMetrics.scaledDensity));
		tv.setTextSize(valueWide);
		tv.setTypeface(tv.getTypeface(), style);
	}

	public static void setTextViewFontSizeBasedOnScreenDensity(
			Activity activity, TextView tv, double size, boolean makeBold) {

		DisplayMetrics dMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		final float WIDE = activity.getResources().getDisplayMetrics().widthPixels;
		int valueWide = (int) (WIDE / size / (dMetrics.scaledDensity));
		if (makeBold) {
			tv.setTypeface(Typeface.DEFAULT_BOLD);
		}
		tv.setTextSize(valueWide);
	}


	public static void setEditTextFontSizeBasedOnScreenDensity(
			Activity activity, EditText et, double size, boolean makeBold) {

		DisplayMetrics dMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		final float WIDE = activity.getResources().getDisplayMetrics().widthPixels;
		int valueWide = (int) (WIDE / size / (dMetrics.scaledDensity));
		if (makeBold) {
			et.setTypeface(Typeface.DEFAULT_BOLD);
		}
		et.setTextSize(valueWide);
	}

	public static void setTextViewFontSizeBasedOnScreenDensity(
			Activity activity, TextView tv) {

		DisplayMetrics dMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		final float WIDE = activity.getResources().getDisplayMetrics().widthPixels;
		int valueWide = (int) (WIDE / 19.0f / (dMetrics.scaledDensity));
		tv.setTextSize(valueWide);
	}


	public static Dialog createDialogforSettings(Context context, String text,
			final Activity activity) {
		Dialog dialog;
		TextViewPlus textViewMessage = new TextViewPlus(context);
		textViewMessage.setText(text);
		textViewMessage.setTextSize(18);
		textViewMessage.setPadding(10, 20, 10, 20);
		textViewMessage.setTextColor(Color.WHITE);
		textViewMessage.setGravity(Gravity.CENTER_HORIZONTAL);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								activity.startActivityForResult(
										new Intent(
												Settings.ACTION_LOCATION_SOURCE_SETTINGS),
										0);
								return;
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						activity.finish();
					}
				});
		builder.setView(textViewMessage);
		AlertDialog alert = builder.create();
		dialog = alert;
		return dialog;

	}

	public static Typeface getCustomFont(Context ctx) {
		Typeface tf = null;
		try {
			tf = Typeface.createFromAsset(ctx.getAssets(),
					"BhashaSETTSinhalaTamil.dat");
		} catch (Exception e) {
			Log.e(SettUI.class.getSimpleName(),
					"Could not get typeface: " + e.getMessage());
			return null;
		}
		return tf;
	}

	public static void setText(Context context, TextView textView, String text) {
		textView.setTypeface(getCustomFont(context));
		textView.setText(text);
	}

	public static void setText(Context context, Button button, String text) {
		button.setTypeface(getCustomFont(context));
		button.setText(text);
	}

}
