package com.adrianjaylopez.PartyApp.controller;

import android.content.Context;
import android.os.AsyncTask;
import com.adrianjaylopez.PartyApp.model.WriteToDB;

import java.util.HashMap;
import java.util.List;

public class DBController extends
		AsyncTask<String[], Void, List<HashMap<String, String>>> {
	WriteToDB db;
	Context context;

	public DBController(Context context) {
		this.context = context;
	}

	@Override
	protected List<HashMap<String, String>> doInBackground(String[]... params) {
		db = new WriteToDB(context);
		if (params[0][0] == "insert") {

			db.insert(params[1]);
			return null;
		} else
			return db.getClubs();
	}

}
