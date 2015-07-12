package com.adrianjaylopez.PartyApp.controller;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import com.adrianjaylopez.PartyApp.R;
import com.adrianjaylopez.PartyApp.model.HttpConnectionManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GeoLocations extends AsyncTask<String, Void, double[]> {
	Context context;
	JSONObject jObj = null;
	JSONArray request = null;
	double[] coordinates = new double[2];

	public GeoLocations(Context context) {
		this.context = context;
	}

	private String getLocationSearch(String address) {
		try {
			address = "https://maps.googleapis.com/maps/api/geocode/json"
					+ "?address=" 
					+ URLEncoder.encode(address, "UTF-8") 
					+ "&sensor="
					+ URLEncoder.encode("true", "UTF-8") 
					+ "&key="
					+ URLEncoder.encode(context.getResources().getString(R.string.googlekey), "UTF-8");
		} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
		} catch (NotFoundException e) {
						e.printStackTrace();
		}
		return address;

	}

	@Override
	protected double[] doInBackground(String... params) {

		HttpConnectionManager conn = new HttpConnectionManager();
		String temp = new String();

		temp = conn.downloadPlacesInfo(getLocationSearch(params[0]));

		try {
			jObj = new JSONObject(temp);

			request = jObj.getJSONArray("results");

				JSONObject c = request.getJSONObject(0);
				JSONObject gem = c.getJSONObject("geometry");
				JSONObject loc = gem.getJSONObject("location");
				coordinates[0] = Float.parseFloat(loc.getString("lat"));
				coordinates[1] = Float.parseFloat(loc.getString("lng"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return coordinates;
	}
}
