package com.hackers.promocatch.utill;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageViewPlus extends ImageView {

	public ImageViewPlus(Context context) {
		super(context);
	}

	public ImageViewPlus(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ImageViewPlus(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		//int height = Math.min(MeasureSpec.getSize(heightMeasureSpec), width * getDrawable().getIntrinsicHeight() / getDrawable().getIntrinsicWidth() );
		int height = width * getDrawable().getIntrinsicHeight() / getDrawable().getIntrinsicWidth();
		setMeasuredDimension(width, height);
	}


}