package com.hackers.promocatch.artools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ViewAnimator;

import com.hackers.promocatch.R;
import com.hackers.promocatch.activities.PlayActivity;
import com.hackers.promocatch.flying.AnimationFactory;
import com.hackers.promocatch.flying.AnimationFactoryRight;

public class ARObject extends View {

	public float azimuth; // Angle from north (clockwise) in degrees
	public float inclination; // angle from horizon (upwards) in degrees
	public float distance; // Distance from user in meters
	public String colour; // Colour of object
	public String offer;

	public boolean visible; // whether the object is available or has been
							// caught
	public boolean onFocus = true; // whether the object is inside the range of
									// camera
	public boolean catchable = true; // whether the object is in a catchable
										// distance to the user
	public boolean caught = false;
	
	public float randAzimuth = 0, randIncl = 0;

	private Bitmap bitmap;

	public int height = 1;
	public int width = 1;
	public int rotation = 0;
	public int id;
	public Location location;
	float altitude; //altitude of the object in meters

	int counter = 0; // this is used for animation
	public View xmlView;
	public Handler handler = new Handler();
	ViewAnimator viewAnimator;
	ViewAnimator viewAnimator2;
	LinearLayout myView;
	
	int xPos = 0, yPos = 0;
	
	Paint p = new Paint();

	// without GPS - without animation
	public ARObject(PlayActivity activity, float azimuth, float inclination,
					float distance, String colour, String offer) {
		super(activity);
		this.offer = offer;
		this.azimuth = azimuth;
		this.inclination = inclination;
		this.distance = distance;
		this.colour = colour;
		this.visible = true;
		xmlView = null;
		activity.virtualView.addARView(this);
	}

	// without GPS - with animation
	public ARObject(PlayActivity activity, float azimuth, float inclination,
			float distance) {
		super(activity);
		this.azimuth = azimuth;
		this.inclination = inclination;
		this.distance = distance;
		this.visible = true;
		this.xmlView = activity.getLayoutInflater()
				.inflate(R.layout.main, null);
		activity.frameLayout.addView(this.xmlView);
		activity.virtualView.addARView(this);
		startAnimation();
	}

	// with GPS - without animation
	public ARObject(PlayActivity activity, int id, Location objectLocation,
			float altitude, String colour, String offer) {
		super(activity);
		this.id = id;
		location = objectLocation;
		azimuth = PlayActivity.deviceLocation.bearingTo(objectLocation);
		distance = PlayActivity.deviceLocation.distanceTo(objectLocation);
		this.altitude = altitude;
		inclination = (float)(Math.atan((altitude)/distance)*180/Math.PI);
		this.colour = colour;
		this.offer = offer;
		this.visible = true;
		xmlView = null;
		activity.virtualView.addARView(this);
	}

	// with GPS - with animation
	public ARObject(PlayActivity activity, int id, Location objectLocation) {
		super(activity);
		this.id = id;
		location = objectLocation;
		azimuth = PlayActivity.deviceLocation.bearingTo(objectLocation);
		distance = PlayActivity.deviceLocation.distanceTo(objectLocation);
		this.inclination = 0; // (float)(Math.atan((altitude-deviceAltitude)/distance)*180/Math.PI);
		this.visible = true;
		this.xmlView = activity.getLayoutInflater()
				.inflate(R.layout.main, null);
		activity.frameLayout.addView(this.xmlView);
		activity.virtualView.addARView(this);
		startAnimation();
	}

	public void updateLocation() {
		azimuth = PlayActivity.deviceLocation.bearingTo(location);
		distance = PlayActivity.deviceLocation.distanceTo(location);
		inclination = (float)(Math.atan((altitude)/distance)*180/Math.PI);
		// inclination =
		// (float)(Math.atan((altitude-deviceAltitude)/distance)*180/Math.PI);
	}

	public void startAnimation() {
		viewAnimator = (ViewAnimator) xmlView.findViewById(R.id.viewFlipper);
		viewAnimator2 = (ViewAnimator) xmlView.findViewById(R.id.viewFlipper2);
		myView = (LinearLayout) xmlView.findViewById(R.id.Animation);

		/*
		 * ObjectAnimator moveToSide = ObjectAnimator.ofFloat(myView,
		 * "translationX", -100f, 100f); moveToSide.setDuration(10000);
		 * moveToSide.start();
		 * 
		 * ObjectAnimator moveToSide2 = ObjectAnimator.ofFloat(myView,
		 * "translationY", 100f, -100f);
		 * 
		 * moveToSide2.setDuration(10000); moveToSide2.start();
		 */

		/*
		 * ObjectAnimator moveToSide4 = ObjectAnimator.ofFloat(myView,
		 * "rotationX", 0f, 20f);
		 * 
		 * moveToSide4.setDuration(1); moveToSide4.start();
		 * 
		 * ObjectAnimator moveToSide6 = ObjectAnimator.ofFloat(myView,
		 * "rotation", 0f, 40);
		 * 
		 * moveToSide6.setDuration(1); moveToSide6.start();
		 */

		handler.postDelayed(flyingAnimation, 0);
	}

	public void drawXml() {
		xmlView.setX(getLeft());
		xmlView.setY(getTop());
		// LayoutParams params = xmlView.getLayoutParams();
		// params.height = height;
		// params.width = width;
		// params.setMargins(getLeft(), getTop(), 0, 0);
		// xmlView.setLayoutParams(params);
		xmlView.invalidate();
	}

	public void draw(Canvas c) {

		if (xmlView != null) {
			drawXml();
			return;
		}

		bitmap = BitmapFactory.decodeResource(getResources(), getOpenImg());

		Matrix matrix = new Matrix();
		matrix.postRotate(-rotation);

		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height,
				false);
		
		Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, 
				scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);

		c.drawBitmap(rotatedBitmap, getLeft(), getTop(), p);

	}

	final Runnable flyingAnimation = new Runnable() {
		public void run() {

			// int i = 0;

			/*
			 * if (i % 2 == 0) { AnimationFactory.flipTransition(viewAnimator,
			 * FlipDirection.LEFT_RIGHT); AnimationFactoryRight .flipTransition(
			 * viewAnimator2,
			 * lk.bhasha.flying.AnimationFactoryRight.FlipDirection.RIGHT_LEFT);
			 * } else {
			 */
			
			AnimationFactory.flipTransition(viewAnimator,
					AnimationFactory.FlipDirection.LEFT_RIGHT);
			AnimationFactoryRight
					.flipTransition(
							viewAnimator2,
							com.hackers.promocatch.flying.AnimationFactoryRight.FlipDirection.RIGHT_LEFT);
			// }
			// i++;
			if (caught)
				handler.postDelayed(caughtAnimation, 1000);
			else
				handler.postDelayed(this, 1000);
		}
	};

	final Runnable caughtAnimation = new Runnable() {
		public void run() {

			/*
			 * ObjectAnimator moveToSide = ObjectAnimator.ofFloat(myView,
			 * "rotation", 0f, 30f); moveToSide.setDuration(1000);
			 * moveToSide.start();
			 * 
			 * ObjectAnimator moveToSide2 = ObjectAnimator.ofFloat(myView,
			 * "translationY", 0f, height / 4.0f);
			 * moveToSide2.setDuration(1000); moveToSide2.start();
			 */

			AnimationFactory.flipTransition2(viewAnimator,
					AnimationFactory.FlipDirection.HALF_FLIP);
			AnimationFactoryRight
					.flipTransition2(
							viewAnimator2,
							com.hackers.promocatch.flying.AnimationFactoryRight.FlipDirection.HALF_FLIP);

			if (visible)
				handler.postDelayed(this, 1000);
		}
	};

	public int getOpenImg() {
		if (this.colour.equalsIgnoreCase("red")) {
			return catchable ? R.drawable.game_ball_red_catchable : R.drawable.game_ball_red;
		} else if (this.colour.equalsIgnoreCase("purple")) {
			return catchable ? R.drawable.game_ball_purple_catchable : R.drawable.game_ball_purple;
		} else if (this.colour.equalsIgnoreCase("green")) {
			return catchable ? R.drawable.game_ball_green_catchable : R.drawable.game_ball_green;
		} else if (this.colour.equalsIgnoreCase("gold")) {
			return catchable ? R.drawable.game_ball_gold_catchable : R.drawable.game_ball_gold;
		} else {
			return catchable ? R.drawable.game_ball_blue_catchable : R.drawable.game_ball_blue;
		}
	}
	
	public int getImageWhenCaught() {
		if (colour.equalsIgnoreCase("Blue")) {
			return R.drawable.game_ball_blue;
		} else if (colour.equalsIgnoreCase("Gold")) {
			return R.drawable.game_ball_gold;
		} else if (colour.equalsIgnoreCase("Green")) {
			return R.drawable.game_ball_green;
		} else if (colour.equalsIgnoreCase("Purple")) {
			return R.drawable.game_ball_purple;
		} else if (colour.equalsIgnoreCase("Red")) {
			return R.drawable.game_ball_red;
		} else {
			return R.drawable.game_ball_blue;
		}
	}

	/*
	 * public int getClosedImg() { if (this.colour.equals("red")) { return
	 * R.drawable.butterfly_closed_red; } else if (this.colour.equals("green"))
	 * { return R.drawable.butterfly_closed_green; } else if
	 * (this.colour.equals("yellow")) { return
	 * R.drawable.butterfly_closed_yellow; } else { return
	 * R.drawable.butterfly_closed; } }
	 */

}
