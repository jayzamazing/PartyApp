package com.adrianjaylopez.PartyApp.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WriteToDB extends SQLiteOpenHelper {
	public static String DBName = "clubhistorydb";
	Context context;

	public WriteToDB(Context context) {
		super(context, DBName, null, 1);
		this.context = context;
		context.openOrCreateDatabase(
				context.getFilesDir().getPath() + context.getPackageName()
						+ "/clubhistorydb", 0, null);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE clubs (clubID INTEGER PRIMARY KEY autoincrement,"
				+ "name VARCHAR, lat REAL, lng REAL, date DATETIME DEFAULT CURRENT_TIMESTAMP, UNIQUE (name) ON CONFLICT REPLACE)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS clubs");
		onCreate(db);

	}

	public void insert(String[] clubInfo) {
		SQLiteDatabase db = this.getWritableDatabase();

		try {
			Date date = new Date(0);
			context.getDatabasePath(context.getFilesDir().getPath()
					+ context.getPackageName() + "/clubhistorydb");

			SQLiteStatement insert = db
					.compileStatement("Insert or Replace into clubs"
							+ " ( name, lat, lng)" + " values(?,?,?)");

			insert.bindString(1, clubInfo[0]);
			insert.bindDouble(2, Double.parseDouble(clubInfo[1]));
			insert.bindDouble(3, Double.parseDouble(clubInfo[2]));

			insert.execute();

		} finally {
			db.endTransaction();
			db.close();
		}
		Cursor cursor = db.rawQuery("SELECT name, lat, lng, date FROM clubs",
				null);
		if (cursor.getCount() < 25) {
			cursor = db.rawQuery("SELECT clubID FROM clubs WHERE date in"
					+ "(SELECT min(date) FROM clubID", null);
			db.delete("clubs", "clubID=" + cursor.getString(0), null);
		}
	}

	public List<HashMap<String, String>> getClubs() {
		List<HashMap<String, String>> dbList = new ArrayList<HashMap<String, String>>();
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			HashMap<String, String> list;

			Cursor cursor = db.rawQuery("SELECT name, lat, lng FROM clubs",
					null);

			while (cursor.moveToNext()) {
				list = new HashMap<String, String>();
				list.put("name", cursor.getString(0));
				list.put("lat", cursor.getString(1));
				list.put("lng", cursor.getString(1));
				dbList.add(list);
			}
		} finally {
			db.close();
		}
		return dbList;
	}

}
