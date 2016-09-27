package com.hackers.promocatch.utill;

import android.location.Location;

public class AppData {
	public static double latitude = 0;
	public static double longitude = 0;
	public static Location deviceLocation = null;
	public static boolean isPractice = false;
	public static final float smoothingFactor = 0.3f;
	public static final String importObjectsUrl = "http://bhasha.lk/test/ar/objects.php";
	public static final String registerDeviceUrl = "http://bhasha.lk/ws/katchapp/gcm/?deviceid=";
	public static final String catchObjectUrl = null;
	public static String jsonString = null;
	public static final String SENDER_ID = "781880229451";
	public static String gcmRegID = null;
	public static String androidID = null;
}
