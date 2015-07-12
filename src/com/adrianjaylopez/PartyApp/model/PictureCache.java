package com.adrianjaylopez.PartyApp.model;

import android.content.Context;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.net.URLEncoder;

public class PictureCache {
	private File pictureDir;

	public PictureCache(Context context) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			pictureDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					"PictureList");
		} else {
			pictureDir = context.getCacheDir();
		}
		if (!pictureDir.exists()) {
			pictureDir.mkdirs();
		}
	}

	public File downloadPicture(String url, String fileName) {
		File CardFile = null;
		try {
			try {

				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(new HttpPost(url));
				CardFile = getFile(fileName);
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
		return CardFile;
	}

	private File getFile(String url) {
		try {
			String filename = URLEncoder.encode(url + ".jpg", "utf8");
			File f = new File(pictureDir, filename);
			return f;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void clear() {
		File[] files = pictureDir.listFiles();
		if (files == null) {
			return;
		}
		for (File f : files) {
			f.delete();
		}
	}

}
