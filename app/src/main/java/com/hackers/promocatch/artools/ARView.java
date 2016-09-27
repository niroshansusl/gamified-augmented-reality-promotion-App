package com.hackers.promocatch.artools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hackers.promocatch.R;
import com.hackers.promocatch.activities.MainActivity;
import com.hackers.promocatch.activities.PlayActivity;
import com.hackers.promocatch.lanuage.SettUI;
import com.hackers.promocatch.model.Butterfly;
import com.hackers.promocatch.utill.DatabeaseHandler;
import com.hackers.promocatch.utill.ImageViewPlus;
import com.hackers.promocatch.utill.TextViewPlus;

//import lk.bhasha.ar.model.JsonFormat3;

@TargetApi(11)
public class ARView extends View implements SensorEventListener {

	private final float xAngleWidth = 29; // horizontal range of camera view in
											// degrees (in landscape mode)
	private final float yAngleWidth = 19; // vertical range of camera view in
											// degrees (in landscape mode)

	Boolean st = true;

	@SuppressWarnings("unused")
	private boolean catchFound = false;
	// this contains all the AR objects
	public HashSet<ARObject> arObjects = new HashSet<ARObject>();

	public SensorManager sensorManager;
	private Context context;

	public int screenWidth = 480;
	public int screenHeight = 800; // will be set

	public Location currentLocation = null; // device location
	public float direction = 0f; // direction of the camera (angle from north -
									// in degrees - clockwise)
	public float inclination = 0f; // inclination of the camera (from horizon -
									// in degrees - upwards)

	private float objectHeight = 0.32f; // width of objects in meters
	public float objectWidth = 0.32f; // height of objects in meters
	public static final int catchableDistance = 10; // in meters
	private int visibleRadius = 20; // in meters

	final LinearLayout popup;
	final ImageView ball;
	final ImageViewPlus net;
	final ImageViewPlus netReady;
	final TextViewPlus youGot;
	final TextViewPlus tapTo;
	final TextView offerValue;

	final ProgressBar progressBar;

	Paint p = new Paint();

	float z_gravity = 10;
	float x_axis_rotation = 0;
	// int updown = 1;

	private final float maxOffset = 20f;

	public static boolean paused = false;
	public static boolean onHit = false;
	public static boolean hitProcessing = false;

	@SuppressWarnings("deprecation")
	public ARView(Context context, int screenWidth, int screenHeight) {
		super(context);
		this.context = context;

		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;

		popup = (LinearLayout) PlayActivity.myView
				.findViewById(R.id.popupLayout);
		ball = (ImageView) PlayActivity.myView
				.findViewById(R.id.imageBallThatCaought);
		net = (ImageViewPlus) PlayActivity.myView
				.findViewById(R.id.imageNetCaughtPaused);
		netReady = (ImageViewPlus) PlayActivity.myView
				.findViewById(R.id.imageNet);
		youGot = (TextViewPlus) PlayActivity.myView.findViewById(R.id.youGot);
		offerValue = (TextView) PlayActivity.myView
				.findViewById(R.id.offerValue);
		tapTo = (TextViewPlus) PlayActivity.myView.findViewById(R.id.tapto);
		progressBar = (ProgressBar) PlayActivity.myView
				.findViewById(R.id.progressBar);

		sensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_FASTEST);

		new Thread() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(200);
						updateObjects();
						ARView.this.postInvalidate();
					} catch (Exception e) {
					}
				}
			}
		}.start();
	}

	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	@SuppressWarnings("deprecation")
	public void onSensorChanged(SensorEvent evt) {

		float vals[] = evt.values;

		if (evt.sensor.getType() == Sensor.TYPE_ORIENTATION) {

			float y_inc = vals[1], x_inc = vals[2];
			int inc_sign = (y_inc > -90 && y_inc < 90) ? -1 : 1;
			if (x_inc < 0)
				x_inc = -x_inc;
			if (y_inc < 0)
				y_inc = -y_inc;
			if (y_inc > 90)
				y_inc = 180 - y_inc;
			inclination = inc_sign * (90 - (x_inc + y_inc));

			float angle;

			if (vals[2] == 0) {
				angle = ((inc_sign * vals[1]) < 0) ? 180 : 0;
			} else if (vals[1] == 0 || inclination == 0) {
				angle = (vals[2] > 0) ? 90 : -90;
			} else {
				if (vals[1] < 0)
					angle = (float) (Math.atan(x_inc / y_inc) * 180 / Math.PI);
				else
					angle = 180 - (float) (Math.atan(x_inc / y_inc) * 180 / Math.PI);
				if (vals[2] < 0)
					angle = -angle;
			}

			direction = vals[0] + angle;

			if (direction < 0)
				direction += 360;
			if (direction >= 360)
				direction -= 360;

			if (inclination == -90 || inclination == 90)
				x_axis_rotation = 0;
			else {
				if (vals[1] < 0 && vals[2] >= 0)
					x_axis_rotation = (y_inc * 90) / (x_inc + y_inc);
				else if (vals[1] < 0 && vals[2] < 0)
					x_axis_rotation = 180 - (y_inc * 90) / (x_inc + y_inc);
				else if (vals[1] > 0 && vals[2] < 0)
					x_axis_rotation = 180 + (y_inc * 90) / (x_inc + y_inc);
				else
					x_axis_rotation = 360 - (y_inc * 90) / (x_inc + y_inc);
			}
		}

		if (evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

			final float filterCoefficient = 0.8f;
			z_gravity = filterCoefficient * z_gravity + (1 - filterCoefficient)
					* vals[2];
			float z_acc = vals[2] - z_gravity;

			if (z_acc > 4) {
				if (!onHit) {
					if (!hitProcessing) {
						onHit = true;
						hitProcessing = true;
						catchObjects();
					}
				} else {
					animateNet();
				}

			} else
				onHit = false;
		}
	}

	public void animateNet() {
		final ImageViewPlus ima = (ImageViewPlus) PlayActivity.myView
				.findViewById(R.id.imageNet);

		final ImageViewPlus imag = (ImageViewPlus) PlayActivity.myView
				.findViewById(R.id.imageNetCaught);

		ima.setVisibility(View.GONE);

		Animation animationFalling = AnimationUtils.loadAnimation(
				PlayActivity.context, R.anim.falling);

		imag.startAnimation(animationFalling);

		imag.setVisibility(View.VISIBLE);

		animationFalling.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				if (st) {
					st = false;

					SharedPreferences prefs = PreferenceManager
							.getDefaultSharedPreferences(context);
					boolean downStatus = prefs.getBoolean("GameSound", true);

					if (downStatus) {
						MediaPlayer mPlayer = MediaPlayer.create(context,
								R.raw.swoosh);
						mPlayer.start();
					}
				}
			}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				Animation animationFalling2 = AnimationUtils.loadAnimation(
						PlayActivity.context, R.anim.fallingdown);

				imag.startAnimation(animationFalling2);

				animationFalling2.setAnimationListener(new AnimationListener() {

					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated
						// method stub

					}

					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated
						// method stub

					}

					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated
						// method stub

						imag.setVisibility(View.GONE);
						ima.setVisibility(View.VISIBLE);
						st = true;

					}
				});

			}
		});
	}

	public void catchObjects() {
		Iterator<ARObject> i = arObjects.iterator();
		boolean foundVisible = false, foundCatchable = false;

		while (i.hasNext()) {
			final ARObject object = i.next();
			if (object.visible) {
				foundVisible = true;
				if (object.catchable) {
					foundCatchable = true;
					DatabeaseHandler db = new DatabeaseHandler(context);
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy/MM/dd HH:mm:ss");
					Calendar cal = Calendar.getInstance();

					String ts = dateFormat.format(cal.getTime());

					if (PlayActivity.isPractice) {
						db.addButterfly(new Butterfly(object.id, ts,
								object.location.getLatitude(), object.location
										.getLongitude(), object.offer + " Values",
								object.colour, "Practice"));
						object.caught = true;
						caughtAnimation1(object);
						object.visible = false;
						object.setVisibility(View.GONE);
						// sendPush(object.id);
						// catchNotify(object);
						hitProcessing = false;
						return;

					} else {
						sendPush(object.id);
						catchNotify(object);
						caughtAnimation1(object);
						return;
					}
				}
			}
		}
		hitProcessing = false;
		if (foundVisible && (!foundCatchable)) {

			String languageSting;

				languageSting = getResources().getString(
						R.string.fartocatch_en);

			SettUI.showToast(PlayActivity.activity, context, languageSting);

			// Toast.makeText(PlayActivity.activity, "Too far to catch!",
			// 500).show();
		}

		/*
		 * Toast.makeText( PlayActivity.activity, "true : "+
		 * String.valueOf(direction)+" , "+String.valueOf(inclination),
		 * Toast.LENGTH_LONG).show();
		 */
	}

	private void sendPush(int id) {
		final int objectId = id;
		new Thread() {
			@Override
			public void run() {
				try {
					HttpGet httpGet = new HttpGet(PlayActivity.gcmPushUrl
							+ objectId);
					HttpClient httpclient = new DefaultHttpClient();
					httpclient.execute(httpGet);
				} catch (Exception e) {
					hitProcessing = false;
					PlayActivity.activity.internetHandler.sendEmptyMessage(-1);
				}
			}
		}.start();
	}

	private void catchNotify(ARObject object) {
		final ARObject obj = object;
		new Thread() {
			@Override
			public void run() {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("imsi",
						PlayActivity.imsi));
				// nameValuePairs.add(new BasicNameValuePair("imsi",
				// "413022600404011")); // test
				nameValuePairs.add(new BasicNameValuePair("id", String
						.valueOf(obj.id)));
				try {
					HttpPost httppost = new HttpPost(
							PlayActivity.catchObjectUrl);
					HttpClient httpclient = new DefaultHttpClient();
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
					BufferedReader in = new BufferedReader(
							new InputStreamReader(response.getEntity()
									.getContent()));
					PlayActivity.jsonCatchResponse = in.readLine();
					PlayActivity.caughtObject = obj;
					PlayActivity.activity.internetHandler.sendEmptyMessage(2);
				} catch (Exception e) {
					hitProcessing = false;
					PlayActivity.jsonCatchResponse = null;
					PlayActivity.activity.internetHandler.sendEmptyMessage(-1);
				}
			}
		}.start();
	}

	public void addARView(ARObject view) {
		arObjects.add(view);
	}

	public void removeARView(ARObject view) {
		arObjects.remove(view);
	}

	public void clearARViews() {
		arObjects.removeAll(arObjects);
	}

	private boolean isCatchable(float xPosition, float yPosition, float distance) {
		return (xPosition > 0 && xPosition < screenWidth && yPosition > 0
				&& yPosition < screenHeight && distance <= catchableDistance);
	}

	private boolean isVisible(float xPosition, float yPosition, float xGap,
			float yGap) {
		return (xPosition + xGap > 0 && xPosition - xGap < screenWidth
				&& yPosition + yGap > 0 && yPosition - yGap < screenHeight);
	}

	private float[] calcPosition(float objectDirection, float objectInclination) {

		float xOffset = objectDirection - direction;
		if (xOffset > 180)
			xOffset -= 360;
		if (xOffset < -180)
			xOffset += 360;
		// xOffset *= (screenWidth / xAngleWidth);
		xOffset *= (screenHeight / xAngleWidth); // changed from landscape mode
													// to portrait

		float yOffset = objectInclination - inclination;
		if (yOffset > 180)
			yOffset -= 360;
		if (yOffset < -180)
			yOffset += 360;
		// yOffset *= (screenHeight / yAngleWidth);
		yOffset *= (screenWidth / yAngleWidth); // changed from landscape mode
												// to portrait

		float r = (float) Math.sqrt((xOffset * xOffset) + (yOffset * yOffset));
		float theta = (float) (Math.atan2(yOffset, xOffset) * 180 / Math.PI);

		theta = theta + x_axis_rotation; // screen rotation correction
		theta = theta - 90; // conversion from landscape to portrait
		if (theta > 180)
			theta -= 360;
		if (theta < -180)
			theta += 360;

		float[] xy = new float[2];
		xy[0] = r * (float) Math.cos(theta * Math.PI / 180.0);
		xy[1] = -r * (float) Math.sin(theta * Math.PI / 180.0);

		return xy;
	}

	private float calcXsize(float dist) {
		// return 147;
		float d = 1f;
		if (dist > 1) // if distance > 1 meter
			d = 1f + ((dist - 1f) / 2);
		return (float) Math.atan(objectWidth / (2 * d)) * 7500;
	}

	private float calcYsize(float dist) {
		// return 200;
		float d = 1f;
		if (dist > 1)
			d = 1f + ((dist - 1f) / 2);
		return (float) Math.atan(objectHeight / (2 * d)) * 7500;
	}

	public void onDraw(Canvas c) {

		p.setColor(Color.BLUE);
		p.setAlpha(100);
		c.drawCircle(60, 140, 50, p);
		p.setColor(Color.WHITE);
		p.setAlpha(255);
		c.drawText(String.valueOf(visibleRadius) + "m", 50, 90, p);

		c.drawLine(60, 140, 75, 90, p);
		c.drawLine(60, 140, 45, 90, p);

		p.setColor(Color.WHITE);
		/*
		 * c.drawText("Compass: " + String.valueOf(direction), 140, 20, p);
		 * c.drawText("Inclination: " + String.valueOf(inclination), 280, 20,
		 * p); c.drawText("� 2012 Bhasha Lanka (Pvt) Ltd", 600, 20, p);
		 */

		// c.drawText(String.format("Lat : %.6f", PlayActivity.latitude), 140,
		// 40,
		// p);
		// c.drawText(String.format("Lon : %.6f", PlayActivity.longitude), 280,
		// 40, p);

		p.setColor(Color.RED);

		Iterator<ARObject> i = arObjects.iterator();

		while (i.hasNext()) {
			ARObject view = i.next();
			if (view.visible && view.distance <= visibleRadius) {
				float az = direction - view.azimuth + 90;
				if (az < -180)
					az += 360;
				if (az > 180)
					az -= 360;
				float d = (view.distance * 50 / visibleRadius);
				c.drawCircle(60 + (d * (float) Math.cos(az * Math.PI / 180)),
						140 - (d * (float) Math.sin(az * Math.PI / 180)), 2, p);
			}
			if (view.visible && view.onFocus && view.distance < visibleRadius) {
				view.draw(c);
				// view.xmlView.setVisibility(VISIBLE);
			} // else
				// view.xmlView.setVisibility(INVISIBLE);
		}
	}

	public void updateObjects() {

		/*
		 * float leftArm = direction -(xAngleWidth/2); float rightArm =
		 * direction +(xAngleWidth/2);
		 * 
		 * if(leftArm<0) leftArm = leftArm + 360; if(rightArm>360) rightArm =
		 * rightArm - 360;
		 * 
		 * float upperArm = inclination + (yAngleWidth/2); float lowerArm =
		 * inclination - (yAngleWidth/2);
		 */

		Iterator<ARObject> i = arObjects.iterator();

		catchFound = false;

		while (i.hasNext()) {

			ARObject currentObject = i.next();

			int xGap = (int) calcXsize(currentObject.distance) / 2;
			int yGap = (int) calcYsize(currentObject.distance) / 2;

			// int xCenter = (int)calcXposition(leftArm, rightArm,
			// currentObject.azimuth);
			// int yCenter = (int)calcYposition(lowerArm, upperArm,
			// currentObject.inclination);

			// if (currentObject.caught)
			// continue;

			randomizeLocation(currentObject);
			float[] position = calcPosition(currentObject.azimuth
					+ currentObject.randAzimuth, currentObject.inclination
					+ currentObject.randIncl);

			int xCenter = (int) position[0] + (screenWidth / 2);
			int yCenter = (int) position[1] + (screenHeight / 2);

			currentObject.onFocus = isVisible(xCenter, yCenter, xGap, yGap);
			//currentObject.catchable = isCatchable(xCenter, yCenter,
					//currentObject.distance);
			currentObject.catchable = currentObject.onFocus && 
					currentObject.distance <= catchableDistance;
			if (currentObject.catchable)
				catchFound = true;

			currentObject.width = xGap;
			currentObject.height = yGap;
			currentObject.rotation = (int) x_axis_rotation - 90;

			// Remove following 2 lines only if animation is on
			// currentObject.setLeft(xCenter - xGap);
			// currentObject.setTop(yCenter - yGap);

			// /// Animation move added here ///////

			currentObject.setLeft(currentObject.xPos);
			currentObject.setTop(currentObject.yPos);
			TranslateAnimation anim = new TranslateAnimation(
					currentObject.xPos, xCenter - xGap, currentObject.yPos,
					yCenter - yGap);
			anim.setDuration(200);
			currentObject.startAnimation(anim);

			currentObject.xPos = xCenter - xGap;
			currentObject.yPos = yCenter - yGap;

			// ///Animation move end here /////
		}

	}

	private void randomizeLocation(ARObject object) {
		object.randAzimuth += (Math.random() - 0.5) * 2;
		object.randIncl += (Math.random() - 0.5) * 2;
		if (object.randAzimuth > maxOffset)
			object.randAzimuth = maxOffset;
		if (object.randIncl > maxOffset)
			object.randIncl = maxOffset;
	}

	public void close() {
		sensorManager.unregisterListener(this);
		// locMan.removeUpdates(this);
	}

	public void caughtAnimation1(final ARObject object) {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean downStatus = prefs.getBoolean("GameSound", true);

		if (downStatus) {
			MediaPlayer mPlayer = MediaPlayer.create(context, R.raw.tada);
			mPlayer.start();
		}
		netReady.clearAnimation();
		netReady.setAlpha(0.0f);

		ball.setImageResource(object.getImageWhenCaught());

		popup.setVisibility(View.VISIBLE);
		ball.setVisibility(View.VISIBLE);
		net.setVisibility(View.VISIBLE);

		tapTo.setVisibility(View.VISIBLE);

		if (PlayActivity.isPractice) {
			offerValue.setVisibility(View.VISIBLE);
			youGot.setVisibility(View.VISIBLE);
			String languageSting;
			String languageSting2;
			String languageSting3;

				languageSting = getResources().getString(
						R.string.nothing_en);
				languageSting2 = getResources().getString(
						R.string.yougot_en);
				languageSting3 = getResources().getString(
						R.string.taptoc_en);

			offerValue.setText(languageSting);
			youGot.setText(languageSting2);
			tapTo.setText(languageSting3);

			net.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					caughtAnimation2();
				}
			});
		} else {
			progressBar.setVisibility(View.VISIBLE);
		}

	}

	public void caughtAnimation2() {
		Animation animationFade = AnimationUtils.loadAnimation(
				PlayActivity.context, R.anim.fadeout);

		popup.startAnimation(animationFade);
		ball.startAnimation(animationFade);

		animationFade.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub

			}

			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				popup.setVisibility(View.GONE);
				ball.setVisibility(View.GONE);
				youGot.setVisibility(View.GONE);
				offerValue.setVisibility(View.GONE);
				progressBar.setVisibility(View.GONE);
				Animation animationFallDown = AnimationUtils.loadAnimation(
						PlayActivity.context, R.anim.falldownpause);
				net.startAnimation(animationFallDown);
				animationFallDown.setAnimationListener(new AnimationListener() {

					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
					}

					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						net.setVisibility(View.GONE);
						tapTo.setVisibility(View.GONE);
						netReady.setAlpha(1.0f);
					}
				});
			}
		});
	}

}
