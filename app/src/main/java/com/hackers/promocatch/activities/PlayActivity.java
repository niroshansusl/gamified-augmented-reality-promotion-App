package com.hackers.promocatch.activities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;
import com.hackers.promocatch.R;
import com.hackers.promocatch.artools.ARObject;
import com.hackers.promocatch.artools.ARView;
import com.hackers.promocatch.artools.CameraView;
import com.hackers.promocatch.lanuage.SettUI;
import com.hackers.promocatch.model.Butterfly;
import com.hackers.promocatch.model.DataPackage;
import com.hackers.promocatch.model.JsonFormat1;
import com.hackers.promocatch.model.JsonFormat2;
import com.hackers.promocatch.model.JsonFormat3;
import com.hackers.promocatch.model.ObjectDetails;
import com.hackers.promocatch.utill.DatabeaseHandler;
import com.hackers.promocatch.utill.ImageViewPlus;
import com.hackers.promocatch.utill.TextViewPlus;

public class PlayActivity extends Activity implements LocationListener {

	public static Context context;
	private CameraView cameraView = null;
	public ARView virtualView = null;
	public FrameLayout frameLayout = null;
	private LocationManager locationManager;

	private AlertDialog gcmLoadingDialog;
	private AlertDialog gpsLoadingDialog;
	private AlertDialog jsonLoadingDialog;
	public AlertDialog smsProgress;
	public static ProgressDialog catchLoadingDialog;

	public static PlayActivity activity = null;

	private BroadcastReceiver sendBroadcastReceiver;
	private BroadcastReceiver deliveryBroadcastReceiver;
	String SENT = "SMS_SENT";
	String DELIVERED = "SMS_DELIVERED";

	public static boolean isPractice = false;
	private static final int NO_OF_PRACTICE_OBJECTS = 20;

	public static boolean gcmLoaded = false;
	private boolean locationLoaded = false;

	public static double latitude = 0;
	public static double longitude = 0;
	public static Location deviceLocation;

	public static LinearLayout myView;

	public static final String SENDER_ID = "781880229451";
	public static String gcmRegID = null;

	public static TelephonyManager tm;
	public static String imsi;

	private List<ObjectDetails> objectList;
	private List<DataPackage> dataPackages;
	private int selectedPackage;

	public static int screenWidth = 480;
	public static int screenHeight = 320; // will be set

	public static ARObject caughtObject = null;

	public static final float smoothingFactor = 0.5f;
	public static final String importObjectsUrl = "https://katchapp.dialog.lk/ws/json.php";
	public static final String activateDataPackageUrl = "https://katchapp.dialog.lk/ws/activate.php";
	public static final String gcmRegisterUrl = "https://katchapp.dialog.lk/ws/gcm/?deviceid=";
	public static final String gcmPushUrl = "https://katchapp.dialog.lk/ws/gcm/push.php?action=push&pushmsg=";
	public static final String catchObjectUrl = "https://katchapp.dialog.lk/ws/offer.php";

	public static String jsonObjectsAndDataPackages = null;
	public static String jsonDataPackageResponse = null;
	public static String jsonCatchResponse = null;

	public static int language;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		language = getIntent().getIntExtra("language",
				MainActivity.LANGUAGE_ENGLISH);
		context = getApplicationContext();
		activity = this;
		methodOfSMS();
		locationLoaded = false;


		WindowManager w = getWindowManager();
		Display d = w.getDefaultDisplay();
		screenWidth = d.getWidth();
		screenHeight = d.getHeight();

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		myView = (LinearLayout) inflater.inflate(R.layout.net_layout, null);

		cameraView = new CameraView(context);

		virtualView = new ARView(context, screenWidth, screenHeight);

		frameLayout = new FrameLayout(this);
		frameLayout.addView(cameraView, screenWidth, screenHeight);
		frameLayout.addView(myView, screenWidth, screenHeight);
		frameLayout.addView(virtualView, screenWidth, screenHeight);
		setContentView(frameLayout);

	}

	@Override
	public void onResume() {
		super.onResume();

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		jsonObjectsAndDataPackages = null;
		virtualView.arObjects.clear();
		objectList = null;
		dataPackages = null;
		selectedPackage = 0;

		isPractice = getIntent().getBooleanExtra("practice", false);

		tm = (TelephonyManager) getBaseContext().getSystemService(
				Context.TELEPHONY_SERVICE);

		imsi = tm.getSubscriberId();

		if (!isPractice) {
			if (!imsi.substring(0, 5).equals("41302")) {

				String languageSting;

					languageSting = getResources()
							.getString(R.string.suscribeerror_en);

				SettUI.createDialog(this, languageSting, PlayActivity.this)
						.show();

				return;

			}
		}

		if (!isPractice && !isConnected()) {
			internetHandler.sendEmptyMessage(-1);
			return;
		}

		if (!isPractice && !gcmLoaded) {
			activateGCM();
		} else {
			if (!locationLoaded)
				activateGPS();
			else
				locationLoaded = false;
		}

	}

	private void activateGCM() {
		try {
			// Make sure the device has GCM support.
			GCMRegistrar.checkDevice(this);
		} catch (Exception e) {

			String languagedontsupport;

				languagedontsupport = getResources()
						.getString(R.string.devicenotsupport_en);


			SettUI.createDialog(context, languagedontsupport,
					PlayActivity.activity).show();

			return;

		}

		// Make sure the manifest settings are correct (only at debug stage)
		try {
			GCMRegistrar.checkManifest(this);
		} catch (Exception e) {
			Toast.makeText(PlayActivity.this, "Manifest error!",
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		String regonserver;

		regonserver = getResources().getString(
					R.string.Registeringonserver_en);


		gcmLoadingDialog = SettUI.getLoadingDialog(PlayActivity.this, this,
				regonserver);
		gcmLoadingDialog.show();

		gcmRegID = GCMRegistrar.getRegistrationId(this);
		if (gcmRegID.equals("")) {
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			gcmRegisterOnServer();
		}
	}

	public void gcmRegisterOnServer() {
		new Thread() {
			@Override
			public void run() {
				try {
					HttpPost httppost = new HttpPost(
							PlayActivity.gcmRegisterUrl + gcmRegID);
					HttpClient httpclient = new DefaultHttpClient();
					HttpResponse response = httpclient.execute(httppost);
					BufferedReader in = new BufferedReader(
							new InputStreamReader(response.getEntity()
									.getContent()));
					String result = in.readLine();

					if (result.equals("1000")) {
						// successfully registered
						gcmRegisterHandler.sendEmptyMessage(0);
					} else {
						// invalid server response
						gcmRegisterHandler.sendEmptyMessage(0); // should be 1
					}

				} catch (Exception e) {
					// offline
					internetHandler.sendEmptyMessage(-1);
				}
			}
		}.start();
	}

	// Take action depending on the server connection result
	public Handler internetHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				decodeObjectsFromJson();
			} else if (msg.what == 1) {
				processDataPackageResponse();
			} else if (msg.what == 2) {
				processCatchResponse();
			} else {
				if (gcmLoadingDialog != null && gcmLoadingDialog.isShowing())
					gcmLoadingDialog.dismiss();

				if (jsonLoadingDialog != null && jsonLoadingDialog.isShowing())
					jsonLoadingDialog.dismiss();

				if (catchLoadingDialog != null
						&& catchLoadingDialog.isShowing())
					catchLoadingDialog.dismiss();

				String languageSting;

					languageSting = getResources()
							.getString(R.string.unabletoconnectinternet_en);

				SettUI.createDialog(context, languageSting,
						PlayActivity.activity);
			}
		}
	};

	// Handle when a GCM push message received
	public Handler gcmPushHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (!locationLoaded && msg.what < -2)
				return;

			Iterator<ARObject> i = virtualView.arObjects.iterator();
			while (i.hasNext()) {
				ARObject view = i.next();
				if (msg.what < 0 || view.id == msg.what) {
					view.visible = false;
					view.setVisibility(View.GONE);
				}
			}

			if (msg.what == -2)
				locationLoaded = false;

			// Toast.makeText(
			// PlayActivity.this,
			// "Butterfly number " + String.valueOf(msg.what)
			// + " has been caught!", Toast.LENGTH_SHORT).show();
		}
	};

	// Take action depending on the GCM registration result
	public Handler gcmRegisterHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (gcmLoadingDialog.isShowing())
				gcmLoadingDialog.dismiss();

			if (msg.what == 0) {
				gcmLoaded = true;
				if (!locationLoaded)
					activateGPS();
				else
					locationLoaded = false;
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						PlayActivity.this);
				builder.setMessage(
						"Invalid server response. Unable to launch the application.")
						.setCancelable(false)
						.setNegativeButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.dismiss();
										//finish();
									}
								});
				builder.create().show();
			}
		}
	};

	// Add relative objects for practice mode (without GPS)
	@SuppressWarnings("unused")
	private void addRelativeObjectsWithoutGPS() {

		new ARObject(this, 300, 0, 1f);
		new ARObject(this, 150, 30, 2f);
		new ARObject(this, 30, 0, 3f);
		new ARObject(this, 225, -20, 10f);
		new ARObject(this, 0, 0, 5f);

		virtualView.postInvalidate();
	}

	// Add objects into specific GPS locations
	private void addRelativeObjectsWithGPS(Location location) {

		Location objectLocation;
		String color, offer;

		for (int i = 0; i < NO_OF_PRACTICE_OBJECTS; i++) {

			objectLocation = new Location(location);
			objectLocation.setLatitude(location.getLatitude()
					+ (Math.random() - 0.5) * 0.0004);
			objectLocation.setLongitude(location.getLongitude()
					+ (Math.random() - 0.5) * 0.0004);

			int colorRand = (int) (Math.random() * 5);
			switch (colorRand) {
			case 4:
				color = "gold";
				offer = "50";
				break;
			case 3:
				color = "red";
				offer = "10";
				break;
			case 2:
				color = "green";
				offer = "5";
				break;
			case 1:
				color = "purple";
				offer = "2";
				break;
			default:
				color = "blue";
				offer = "1";
			}

			new ARObject(this, i, objectLocation,
					(float) (Math.random() - 0.5) * 4, color, offer);
		}
	}

	private void decodeObjectsFromJson() {
		try {
			Gson gson = new Gson();
			JsonFormat1 jsonData = gson.fromJson(jsonObjectsAndDataPackages,
					JsonFormat1.class);
			String status = jsonData.status;
			objectList = jsonData.objectList;
			dataPackages = jsonData.dataPackages;

			if (!status.equals("1")) {

				String languageSting;
					languageSting = getResources()
							.getString(R.string.needpackage_en);


				createPackageDialog(PlayActivity.activity, languageSting)
						.show();

			} else {
				if (!objectList.isEmpty()) {
					for (ObjectDetails o : objectList) {
						int id = Integer.parseInt(o.id);
						float altitude = Float.parseFloat(o.altitude);
						Location objectLocation = new Location(deviceLocation);
						objectLocation.setLatitude(Double
								.parseDouble(o.latitude));
						objectLocation.setLongitude(Double
								.parseDouble(o.longitude));

						// object is added to view by the constructor
						@SuppressWarnings("unused")
						ARObject ob = new ARObject(this, id, objectLocation,
								altitude, o.colour, o.offer);
					}
				} else {
					String languageSting;

						languageSting = getResources()
								.getString(R.string.noboject_en);


					SettUI.createDialog(this, languageSting,
							PlayActivity.activity).show();
				}
			}
			if (jsonLoadingDialog.isShowing())
				jsonLoadingDialog.dismiss();
		} catch (Exception e) {
			Log.e("Error", "loadobject", e);
			if (jsonLoadingDialog.isShowing())
				jsonLoadingDialog.dismiss();
		}
	}

	public void listDatapackages() {
		int availablepackages = 0;
		try {
			availablepackages = dataPackages.size();
		} catch (Exception ex) {
			finish();
		}
		if (availablepackages == 0) {

			String languageSting;

				languageSting = getResources().getString(
						R.string.needpackage_en);

			SettUI.createDialog(PlayActivity.activity, languageSting,
					PlayActivity.activity).show();

		} else {
			final CharSequence[] items = new CharSequence[availablepackages];
			final CharSequence[] smsCode = new CharSequence[availablepackages];
			int i = 0;
			for (DataPackage p : dataPackages) {
				items[i] = p.name;
				smsCode[i++] = p.id;
			}

			// Creating and Building the Dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Select Package");

			builder.setSingleChoiceItems(items, -0,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							selectedPackage = item;
						}
					});
			builder.setPositiveButton("Activate",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// requestDataPackage();

							requestDataPackageViaSMS(smsCode[selectedPackage]
									.toString());
						}
					});
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							finish();
						}
					});
			builder.create().show();
		}
	}

	private void requestDataPackageViaSMS(String code) {

		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);
		SmsManager sms = SmsManager.getDefault();

		sms.sendTextMessage("678", null, code, sentPI, deliveredPI);

		String languageSting;

			languageSting = getResources().getString(
					R.string.PackageActivating_en);


		smsProgress = SettUI.getLoadingDialog(PlayActivity.this,
				PlayActivity.activity, languageSting);
		smsProgress.show();
	}

	private void methodOfSMS() {

		// SMS sent
		sendBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					// Toast.makeText(getBaseContext(), "SMS sent",
					// Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					// Toast.makeText(getBaseContext(), "Message sending fail",
					// Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					// Toast.makeText(getBaseContext(), "No service",
					// Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					// Toast.makeText(getBaseContext(), "Null PDU",
					// Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					// Toast.makeText(getBaseContext(), "Radio off",
					// Toast.LENGTH_SHORT).show();
					break;
				}
			}
		};

		// SMS delivered
		deliveryBroadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					// Toast.makeText(getBaseContext(), "SMS Deliverd",
					// Toast.LENGTH_SHORT).show();
					smsProgress.dismiss();
					String languageSting;
						languageSting = getResources()
								.getString(R.string.loading_en);


					jsonLoadingDialog = SettUI.getLoadingDialog(
							PlayActivity.this, PlayActivity.activity,
							languageSting);
					jsonLoadingDialog.show();
					downloadJsonStringForObjects();
					break;
				case Activity.RESULT_CANCELED:
					// Toast.makeText(getBaseContext(), "SMS not delivered",
					// Toast.LENGTH_SHORT).show();
					break;
				}
			}
		};
		registerReceiver(deliveryBroadcastReceiver, new IntentFilter(DELIVERED));
		registerReceiver(sendBroadcastReceiver, new IntentFilter(SENT));
	}

	private void processDataPackageResponse() {
		try {
			Gson gson = new Gson();
			JsonFormat2 jsonData = gson.fromJson(jsonDataPackageResponse,
					JsonFormat2.class);
			String status = jsonData.status;
			String errorMsg = jsonData.error;

			if (!status.equals("1")) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						PlayActivity.this);

				if (errorMsg == null || errorMsg.length() == 0)
					builder.setMessage("Activation was not successful.");
				else
					builder.setMessage(errorMsg);

				builder.setCancelable(false);
				builder.setPositiveButton("Practice Mode",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								isPractice = true;
								addRelativeObjectsWithGPS(deviceLocation);
							}
						});
				builder.setNegativeButton("Exit Game",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								finish();
							}
						});
				builder.create().show();
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						PlayActivity.this);
				builder.setMessage("Activation successful! Now you can play the live game");
				builder.setCancelable(false);
				builder.setNegativeButton("Continue",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								downloadJsonStringForObjects();
							}
						});
				builder.create().show();
			}
		} catch (Exception e) {

		}
	}

	private void processCatchResponse() {
		try {
			Log.e("Push Json", jsonCatchResponse);
			Gson gson = new Gson();
			JsonFormat3 jsonData = gson.fromJson(jsonCatchResponse,
					JsonFormat3.class);
			String status = jsonData.status;
			String errorMsg = jsonData.error;

			// if (catchLoadingDialog.isShowing())
			// catchLoadingDialog.dismiss();

			if (!status.equals("1")) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						PlayActivity.this);
				builder.setMessage(errorMsg)
						.setCancelable(false)
						.setNegativeButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										ARView.hitProcessing = false;
										virtualView.caughtAnimation2();
										finish();
									}
								});
				builder.create().show();
			} else {
				caughtObject.visible = false;
				caughtObject.setVisibility(View.GONE);
				// frameLayout.removeView(obj.xmlView);
				DatabeaseHandler db = new DatabeaseHandler(context);
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				Log.e("DB", "" + caughtObject.id);
				String ts = dateFormat.format(cal.getTime());
				db.addButterfly(new Butterfly(caughtObject.id, ts, latitude,
						longitude, caughtObject.offer + " Values",
						caughtObject.colour, "Play"));

				final TextView offerValue;
				final ProgressBar progressBar;
				final ImageViewPlus net;
				final TextViewPlus youGot;
				final TextViewPlus tapTo;

				net = (ImageViewPlus) PlayActivity.myView
						.findViewById(R.id.imageNetCaughtPaused);
				offerValue = (TextView) PlayActivity.myView
						.findViewById(R.id.offerValue);
				progressBar = (ProgressBar) PlayActivity.myView
						.findViewById(R.id.progressBar);

				youGot = (TextViewPlus) PlayActivity.myView
						.findViewById(R.id.youGot);

				tapTo = (TextViewPlus) PlayActivity.myView
						.findViewById(R.id.tapto);
				youGot.setVisibility(View.VISIBLE);
				offerValue.setVisibility(View.VISIBLE);
				offerValue.setText(caughtObject.offer + " Values");

				String languageSting2;
				String languageSting3;

					languageSting2 = getResources()
							.getString(R.string.yougot_en);
					languageSting3 = getResources()
							.getString(R.string.taptoc_en);

				youGot.setText(languageSting2);
				tapTo.setText(languageSting3);

				progressBar.setVisibility(View.GONE);

				ARView.hitProcessing = false;

				// Toast.makeText(context, "Tap To Continue", Toast.LENGTH_LONG)
				// .show();
				net.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						virtualView.caughtAnimation2();
					}
				});

			}
		} catch (Exception e) {
			// if (catchLoadingDialog.isShowing())
			// catchLoadingDialog.dismiss();
			ARView.hitProcessing = false;
			Log.e("Error", "In catch", e);
			virtualView.caughtAnimation2();
		}
	}

	// check whether the device is connected to internet
	public boolean isConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			return true;
		}

		NetworkInfo mobileNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			return true;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		}

		return false;
	}

	// Get device location by GPS
	private void activateGPS() {
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		boolean gpsEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!gpsEnabled) {

			String languageSting;

				languageSting = getResources().getString(
						R.string.needgps_en);

			SettUI.createDialogforSettings(this, languageSting,
					PlayActivity.activity).show();

		} else {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 500, 0, this);

			String languageSting;

				languageSting = getResources().getString(
						R.string.gpsRetreiving_en);


			gpsLoadingDialog = SettUI.getLoadingDialog(PlayActivity.this, this,
					languageSting);
			gpsLoadingDialog.show();
			new Thread() {
				public void run() {
					try {
						Thread.sleep(20000);
						gpsErrorHandler.sendEmptyMessage(0);
					} catch (InterruptedException e) {
					}
				}
			}.start();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) { // Result of the Location Service intent
			locationManager = (LocationManager) this
					.getSystemService(Context.LOCATION_SERVICE);
			boolean gpsEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			if (!gpsEnabled) {

				String languageSting;

					languageSting = getResources()
							.getString(R.string.needgps_en);


				SettUI.createDialogforSettings(this, languageSting,
						PlayActivity.activity).show();

			} else {
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 500, 0, this);

				String languageSting;

					languageSting = getResources()
							.getString(R.string.gpsRetreiving_en);


				gpsLoadingDialog = SettUI.getLoadingDialog(PlayActivity.this,
						this, languageSting);
				gpsLoadingDialog.show();

				new Thread() {
					public void run() {
						try {
							Thread.sleep(20000);
							gpsErrorHandler.sendEmptyMessage(0);
						} catch (InterruptedException e) {
						}
					}
				}.start();
			}
		}
	}

	// Handle if GPS signals are not available
	Handler gpsErrorHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (gpsLoadingDialog.isShowing()) {
				gpsLoadingDialog.dismiss();
				locationManager.removeUpdates(PlayActivity.this);

				String languageSting;

					languageSting = getResources()
							.getString(R.string.cannotresivegps_en);


				SettUI.createDialog(PlayActivity.activity, languageSting,
						PlayActivity.activity).show();

			}
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			locationManager.removeUpdates(this);
			virtualView.sensorManager.unregisterListener(virtualView);
		} catch (Exception e) {

		}

	}

	public void onLocationChanged(Location location) {
		if (!locationLoaded) {
			locationLoaded = true;
			deviceLocation = location;
			latitude = location.getLatitude();
			longitude = location.getLongitude();

			if (gpsLoadingDialog.isShowing())
				gpsLoadingDialog.dismiss();

			if (isPractice) {
				addRelativeObjectsWithGPS(location);
			} else {

				String languageSting;

					languageSting = getResources()
							.getString(R.string.loading_en);


				jsonLoadingDialog = SettUI.getLoadingDialog(PlayActivity.this,
						this, languageSting);
				jsonLoadingDialog.show();
				downloadJsonStringForObjects();
			}

		} else {

			/*
			 * deviceLocation.setLatitude(location.getLatitude() *
			 * smoothingFactor + deviceLocation.getLatitude() * (1 -
			 * smoothingFactor));
			 * deviceLocation.setLongitude(location.getLongitude() *
			 * smoothingFactor + deviceLocation.getLongitude() *(1 -
			 * smoothingFactor));
			 */

			deviceLocation = location;
			latitude = location.getLatitude();
			longitude = location.getLongitude();

			Iterator<ARObject> i = virtualView.arObjects.iterator();
			while (i.hasNext())
				i.next().updateLocation();
		}
	}

	private void downloadJsonStringForObjects() {
		new Thread() {
			public void run() {

				HttpClient httpclient = new DefaultHttpClient();
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("imsi", imsi));
				nameValuePairs.add(new BasicNameValuePair("latitude", String
						.valueOf(latitude)));
				nameValuePairs.add(new BasicNameValuePair("longitude", String
						.valueOf(longitude)));
				nameValuePairs.add(new BasicNameValuePair("skey",
						"Bhasha@@82025"));

				try {
					HttpPost httpget = new HttpPost(importObjectsUrl);
					httpget.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					jsonObjectsAndDataPackages = httpclient.execute(httpget,
							responseHandler);
					Log.d("FOXR AR", "Json = " + jsonObjectsAndDataPackages);
					internetHandler.sendEmptyMessage(0);
				} catch (Exception e) {
					jsonObjectsAndDataPackages = null;
					internetHandler.sendEmptyMessage(-1);
				} finally {
					httpclient.getConnectionManager().shutdown();
				}
			}
		}.start();
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public Dialog createPackageDialog(Context context, String text) {
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
								dialog.dismiss();
								listDatapackages();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						PlayActivity.activity.finish();
					}
				});
		builder.setView(textViewMessage);
		AlertDialog alert = builder.create();
		dialog = alert;
		return dialog;
	}

}
