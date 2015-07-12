package com.adrianjaylopez.PartyApp.controller;

import android.content.Context;
import android.os.AsyncTask;
import com.adrianjaylopez.PartyApp.R;
import com.adrianjaylopez.PartyApp.model.PictureCache;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class DownloadPictures extends AsyncTask<ArrayList<String>, Void, ArrayList<File>> {
	PictureCache pictures;
	Context context;
	
	public DownloadPictures(Context context){
		this.context = context;
	}
	@Override
	protected ArrayList<File> doInBackground(ArrayList<String>... params) {
		ArrayList<String> temp = new ArrayList<String>();
		temp = params[0];
		ArrayList<File> pictureList = new ArrayList<File>();
		pictures = new PictureCache(context);
		pictures.clear();
		for(int i = 0; i < temp.size(); i++){
			pictureList.add(pictures.downloadPicture(getUrl(temp.get(i)), "picture" + i));
			
		}
		return pictureList;
	}
	
	
	public String getUrl(String reference){
		String temp = new String();
		try {
			temp = "https://maps.googleapis.com/maps/api/place/photo"
					+ "?maxwidth="
					+ URLEncoder.encode("200", "UTF-8")
					+ "&photoreference="
					+ URLEncoder.encode(reference, "UTF-8")
					+ "&sensor="
					+ URLEncoder.encode("true", "UTF-8")
					+ "&key="
					+ URLEncoder.encode(
							context.getResources()
							.getString(R.string.googlekey), "UTF-8");
		} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
		}
				
		return temp;
	}
	
	

}
