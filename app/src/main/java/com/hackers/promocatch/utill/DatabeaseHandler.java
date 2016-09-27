package com.hackers.promocatch.utill;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hackers.promocatch.model.Butterfly;

public class DatabeaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "iButterflyHistory";

	// Contacts table name
	public static final String TABLE_BUTTERFLY = "ButterflyHistroy";

	public static final String KEY_ID = "_id";
	public static final String KEY_BUUTERFLYID = "butterflyID";
	public static final String KEY_CATHTIME = "catchtime";
	public static final String KEY_CATCHEDLAN = "catchedlan";
	public static final String KEY_CATCHEDLON = "catchedlon";
	public static final String KEY_REWARD = "reward";
	public static final String KEY_COLOUR = "colour";
	public static final String KEY_GAMEMODE = "gamemode";

	SQLiteDatabase db;

	public DatabeaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_BUTTERFLY = "CREATE TABLE " + TABLE_BUTTERFLY + "("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_BUUTERFLYID + " INTEGER," + KEY_CATHTIME + " TIMESTAMP,"
				+ KEY_CATCHEDLAN + " FLOAT," + KEY_CATCHEDLON + " FLOAT,"
				+ KEY_REWARD + " TEXT," + KEY_COLOUR + " TEXT," + KEY_GAMEMODE
				+ " TEXT" + ")";
		db.execSQL(CREATE_BUTTERFLY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUTTERFLY);

		onCreate(db);
	}

	public int addButterfly(Butterfly butterfly) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_BUUTERFLYID, butterfly.getButterflyID());
		values.put(KEY_CATHTIME, butterfly.getCtachTimeStamp());
		values.put(KEY_CATCHEDLAN, butterfly.getCatchLat());
		values.put(KEY_CATCHEDLON, butterfly.getCatchLon());
		values.put(KEY_REWARD, butterfly.getReward());
		values.put(KEY_COLOUR, butterfly.getColour());
		values.put(KEY_GAMEMODE, butterfly.getGamemode());
		db.insert(TABLE_BUTTERFLY, null, values);
		db.close();

		return 1;
	}

	public List<Butterfly> fethall() {
		List<Butterfly> cList = new ArrayList<Butterfly>();
		// Select All Query
		String selectQuery = "SELECT * " + "FROM " + TABLE_BUTTERFLY
				+ " WHERE " + KEY_GAMEMODE + "= 'Play'";

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Butterfly butterfly = new Butterfly();
				butterfly.setButterflyID(cursor.getInt(1));
				butterfly.setCtachTimeStamp(cursor.getString(2));
				butterfly.setCatchLat(cursor.getDouble(3));
				butterfly.setCatchLon(cursor.getDouble(4));
				butterfly.setReward(cursor.getString(5));
				butterfly.setColour(cursor.getString(6));
				butterfly.setGamemode(cursor.getString(7));
				cList.add(butterfly);
			} while (cursor.moveToNext());
		}

		db.close();
		// return contact list
		return cList;
	}

	public void deleteAllrecoreds() {
		db = this.getWritableDatabase();
		db.execSQL("delete from " + TABLE_BUTTERFLY);
		db.close();
	}
}
