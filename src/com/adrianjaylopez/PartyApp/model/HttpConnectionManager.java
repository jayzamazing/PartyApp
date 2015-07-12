package com.adrianjaylopez.PartyApp.model;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class HttpConnectionManager {
	
	
	public String downloadPlacesInfo(String url) {
		try {
			try {
																						
				HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpPost(url));
			
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

				StringBuilder stringBuilder = new StringBuilder();

				String bufferedStrChunk = null;

				while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
					stringBuilder.append(bufferedStrChunk);
				}
				return stringBuilder.toString();

			} catch (ClientProtocolException cpe) {
				cpe.printStackTrace();
				return null;
			} catch (IOException ioe) {
				ioe.printStackTrace();
				return null;
			}
		} catch (Exception uee) {
			uee.printStackTrace();
			return null;
		}
	}

	
}
