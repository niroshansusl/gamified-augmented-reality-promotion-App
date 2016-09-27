package com.hackers.promocatch.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import android.support.v4.app.FragmentActivity;
import com.hackers.promocatch.R;
import com.hackers.promocatch.lanuage.SettUI;
import com.hackers.promocatch.utill.DirectionsJSONParser;

public class MapActivity extends FragmentActivity {

	GoogleMap googleMap;

	public AlertDialog progress;

	private PolylineOptions lineOptionsRoute;

	Polyline polyline;

	Context context;

	public static int language;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);

		context = this;

		language = getIntent().getIntExtra("language",
				MainActivity.LANGUAGE_ENGLISH);

		Log.e("MapLang", "" + language);

		googleMap = fm.getMap();

		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
				(new LatLng(7.805545772731805, 80.71131706237793)), 7);

		googleMap.animateCamera(cameraUpdate);

		googleMap.setMyLocationEnabled(true);

		progress = new ProgressDialog(this);

		progress.setMessage("Loading...");

		downloadfromserver download = new downloadfromserver();

		download.execute("https://katchapp.dialog.lk/ws/location.php");

		// Start downloading json data from Google Directions API

		googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {

			public View getInfoWindow(Marker arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			public View getInfoContents(Marker arg0) {
				// TODO Auto-generated method stub
				googleMap
						.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

							public void onInfoWindowClick(final Marker marker) {
								// TODO Auto-generated method stub

								LatLng Start = new LatLng(googleMap
										.getMyLocation().getLatitude(),
										googleMap.getMyLocation()
												.getLongitude());

								LatLng End = new LatLng(
										marker.getPosition().latitude, marker
												.getPosition().longitude);

								String url = getDirectionsUrl(Start, End);

								DownloadTask downloadTask = new DownloadTask();

								downloadTask.execute(url);

							}
						});

				return null;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	private class downloadfromserver extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... url) {
			String data = "";

			try {
				// Fetching the data from web service

				data = downloadUrl(url[0]);

			} catch (Exception e) {
				Log.e("Background Task", "", e);
			}
			return data;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// progress.show();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try {

				JSONObject json = new JSONObject(result);

				final JSONArray jsonArray = json.getJSONArray("location");

				JSONObject json_data = null;

				for (int i = 0; i < jsonArray.length(); i++) {

					json_data = jsonArray.getJSONObject(i);

					Double lat = json_data.getDouble("latitude");
					Double lon = json_data.getDouble("longitude");

					String comment = json_data.getString("name");

					LatLng ll = new LatLng(lat, lon);

					AddMarker(ll, comment);

					// progress.dismiss();
				}

			} catch (Exception e1) {

				Log.e("Thread", "", e1);
			}

		}
	}

	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while url", "", e);
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	public void AddMarker(LatLng points, String Comment) {

		googleMap.addMarker(new MarkerOptions()
				.position(new LatLng(points.latitude, points.longitude))
				.title(Comment).snippet("Tap to get directions"));

	}

	private String getDirectionsUrl(LatLng start, LatLng end) {

		// Origin of route
		String str_origin = "origin=" + start.latitude + "," + start.longitude;

		// Destination of route
		String str_dest = "destination=" + end.latitude + "," + end.longitude;

		// Sensor enabled
		String sensor = "sensor=true";

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;

		str_origin = "";

		str_dest = "";

		return url;
	}

	private class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", "", e);
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// progress.dismiss();

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// progress.show();
		}

	}

	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		// Parsing the data in non-ui thread
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			String languageSting;

			languageSting = getResources().getString(R.string.loading_en);

			progress = SettUI.getLoadingDialog(MapActivity.this, context,
					languageSting);
			progress.show();
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;
			// MarkerOptions markerOptions = new MarkerOptions();

			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

					points.add(position);
				}

				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);

				lineOptions.width(5);
				lineOptions.color(Color.RED);
			}

			if (polyline != null) {
				polyline.remove();
			}

			lineOptionsRoute = lineOptions;

			// googleMap.clear();
			polyline = googleMap.addPolyline(lineOptionsRoute); // adds route
																// polyline
			progress.dismiss();
		}
	}

}
