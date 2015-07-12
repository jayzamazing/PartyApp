package com.adrianjaylopez.PartyApp.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.adrianjaylopez.PartyApp.R;
import com.adrianjaylopez.PartyApp.controller.GPSService;

public class SplashScreen extends Activity {

	private static int TIME_OUT = 4000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				startService(new Intent(getApplication(), GPSService.class));
				Intent intent = new Intent(SplashScreen.this,
						MainActivity.class);
				startActivity(intent);
				finish();
				
			}
		}, TIME_OUT);
	}
}
