package com.adrianjaylopez.PartyApp.model;

import android.content.Context;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.net.URLEncoder;

public class HttpPictureDownloads {
	File pictureDir;

	public HttpPictureDownloads(Context context) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			pictureDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					"LazyList");
		} else {
			pictureDir = context.getCacheDir();
		}
		if (!pictureDir.exists()) {
			pictureDir.mkdirs();
		}
	}

	protected void DownloadPicture(String url) {

		try {
			try {

				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(new HttpPost(url));

				File CardFile =	getFile( url );
				FileOutputStream fileOutput = new FileOutputStream(CardFile);
				byte[] buffer = new byte[1024];
				int bufflen = 0;
				InputStream blah = httpResponse.getEntity().getContent();
				while ((bufflen = blah.read(buffer)) != -1) {
					fileOutput.write(buffer, 0, bufflen);
				}
				fileOutput.close();

			} catch (ClientProtocolException cpe) {
				cpe.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		} catch (Exception uee) {
			uee.printStackTrace();
		}
	}
	
	public File getFile( String url ) {
		try {
			String filename = URLEncoder.encode( url, "utf8" );
			File file = new File( pictureDir, filename + ".jpg" );
			return file;
		} catch ( UnsupportedEncodingException e ) {
			e.printStackTrace();
			return null;
		}
	}

}
