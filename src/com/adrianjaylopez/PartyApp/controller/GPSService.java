package com.adrianjaylopez.PartyApp.controller;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;

import com.adrianjaylopez.PartyApp.model.GPSUpdates;

public class GPSService extends Service implements GPSUpdates.Communicator {
	private Location loc;
	private GPSUpdates gps;
	Context context;
	Communicator comm;
	private String mess;
	

	@Override
	public void onCreate() {
		super.onCreate();
		gps = new GPSUpdates(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		gps.startTracking();
		return START_STICKY;
	}

	@Override
	public void locationUpdate() {
		this.loc = gps.getLocation();
		mess = new String("good");
	}

	@Override
	public void gpsTurnedOff() {
		mess = new String("gpsoff");
	}

	@Override
	public void tempUnavailable() {
		mess = new String("celloff");
	}

	@Override
	public void available() {
		mess = new String("cellon");
	}

	@Override
	public void outOfService() {
		mess = new String("outofservice");
	}

	public Location getLoc() {
		this.loc = gps.getLocation();
		return loc;
	}

	public String getMessages() {
		return mess;
	}

	public boolean gpsStatus() {
		return gps.isGpsOn();
	}
	
	@Override
	public IBinder onBind(Intent intent) {

		return mBinder;
	}
	private final IBinder mBinder = new MyBinder();
	public class MyBinder extends Binder {

		public GPSService getService() {
			return GPSService.this;
		}
	}

	public void setCommunicator(Communicator comm) {
		this.comm = comm;
	}

	public interface Communicator {
		public void updatesAvailable();
	}
}
