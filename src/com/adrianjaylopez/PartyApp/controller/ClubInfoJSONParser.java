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
import java.util.ArrayList;

public class ClubInfoJSONParser extends
        AsyncTask<String, Void, ArrayList<ClubInfo>> {
    JSONObject jObj, jObj2 = null;
    JSONArray request, request2;
    ArrayList<ClubInfo> info;
    Context context;

    public ClubInfoJSONParser(Context context) {
        this.context = context;
    }

    private String basicSearchUrl(String lat, String lng) {
        String tmp = new String();
        try {
            tmp = "https://maps.googleapis.com/maps/api/place/search/json"
                    + "?keyword="
                    + URLEncoder.encode("nightclubs|bar", "UTF-8")
                    + "&location="
                    + URLEncoder.encode(lat, "UTF-8")
                    + ","
                    + URLEncoder.encode(lng, "UTF-8")
                    + "&rankby="
                    + URLEncoder.encode("distance", "UTF-8")
                    + "&sensor="
                    + URLEncoder.encode("true", "UTF-8")
                    + "&key="
                    + URLEncoder.encode(
                    context.getResources()
                            .getString(R.string.googlekey), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return tmp;
    }

    private String detailsSearchUrl(String reference) {
        String tmp = new String();
        try {
            tmp = "https://maps.googleapis.com/maps/api/place/details/json"
                    + "?reference="
                    + URLEncoder.encode(reference, "UTF-8")
                    + "&sensor="
                    + URLEncoder.encode("true", "UTF-8")
                    + "&key="
                    + URLEncoder.encode(
                    context.getResources()
                            .getString(R.string.googlekey), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return tmp;
    }

    public ArrayList<ClubInfo> getClubList() {
        return info;
    }

    @Override
    protected ArrayList<ClubInfo> doInBackground(String... params) {
        if (params.length < 2) {
            String[] lc = params;
            for (int i = 0; i < lc.length; i += 2) {
                info = new ArrayList<ClubInfo>();
                HttpConnectionManager conn = new HttpConnectionManager();
                String temp = new String();

                try {
                    temp = conn.downloadPlacesInfo(basicSearchUrl(lc[i],
                            lc[i + 1]));
                    JSONObject c = request.getJSONObject(i);
                    JSONObject gem = c.getJSONObject("geometry");
                    String reference = c.getString("reference");
                    JSONObject loc = gem.getJSONObject("location");
                    float lat = Float.parseFloat(loc.getString("lat"));
                    float lng = Float.parseFloat(loc.getString("lng"));
                    String name = c.getString("name");
                    String address = c.getString("vicinity");
                    double rating;
                    if (c.has("rating")) {
                        rating = c.getDouble("rating");
                    } else
                        rating = 0;

                    conn = new HttpConnectionManager();
                    temp = new String();

                    temp = conn.downloadPlacesInfo(detailsSearchUrl(reference));

                    jObj2 = new JSONObject(temp).getJSONObject("result");
                    request2 = jObj2.getJSONArray("photos");
                    ArrayList<String> photoTemp = new ArrayList<String>();
                    for (int j = 0; j < request2.length(); j++) {
                        JSONObject d = request2.getJSONObject(j);
                        String photo = d.getString("photo_reference");
                        photoTemp.add(photo);
                    }

                    ClubInfo temp2 = new ClubInfo(name, lat, lng, reference,
                            address, rating, photoTemp);
                    info.add(temp2);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return info;
        } else {
            info = new ArrayList<ClubInfo>();
            HttpConnectionManager conn = new HttpConnectionManager();
            String temp = new String();

            temp = conn
                    .downloadPlacesInfo(basicSearchUrl(params[0], params[1]));

            try {
                jObj = new JSONObject(temp);

                request = jObj.getJSONArray("results");

                for (int i = 0; i < request.length(); i++) {
                    JSONObject c = request.getJSONObject(i);
                    JSONObject gem = c.getJSONObject("geometry");
                    String reference = c.getString("reference");
                    JSONObject loc = gem.getJSONObject("location");
                    float lat = Float.parseFloat(loc.getString("lat"));
                    float lng = Float.parseFloat(loc.getString("lng"));
                    String name = c.getString("name");
                    String address = c.getString("vicinity");
                    double rating;
                    if (c.has("rating")) {
                        rating = c.getDouble("rating");
                    } else
                        rating = 0;

                    conn = new HttpConnectionManager();
                    temp = new String();

                    temp = conn.downloadPlacesInfo(detailsSearchUrl(reference));

                    jObj2 = new JSONObject(temp).getJSONObject("result");
                    request2 = jObj2.getJSONArray("photos");
                    ArrayList<String> photoTemp = new ArrayList<String>();
                    for (int j = 0; j < request2.length(); j++) {
                        JSONObject d = request2.getJSONObject(j);
                        String photo = d.getString("photo_reference");
                        photoTemp.add(photo);
                    }

                    ClubInfo temp2 = new ClubInfo(name, lat, lng, reference,
                            address, rating, photoTemp);
                    info.add(temp2);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return info;
    }

}
