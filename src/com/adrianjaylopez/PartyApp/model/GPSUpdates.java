package com.adrianjaylopez.PartyApp.model;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

public class GPSUpdates {
	private String provider;
	private Context context;
	private Location loc;
	Communicator communicator;
	LocationManager locationManager;
	LocationListener locationListener;

	public GPSUpdates(Context context) {
		this.context = context;
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
	}

	public boolean isGpsOn(){
		/**if (provider == null)
			return false;
		else
			return true;**/

		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public void startTracking() {
		/**locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);**/

		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, true);
		// Define a listener that responds to location updates
		locationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				loc = location;
				communicator.locationUpdate();
			}

			@Override
			public void onProviderDisabled(String provider) {
			}

			@Override
			public void onProviderEnabled(String provider) {
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				switch (status) {
				case LocationProvider.TEMPORARILY_UNAVAILABLE:
					communicator.tempUnavailable();
					break;
				case LocationProvider.OUT_OF_SERVICE:
					communicator.outOfService();
					break;
				case LocationProvider.AVAILABLE:
					communicator.available();
					break;
				}

			}

		};
		if (provider != null && locationManager != null)
			loc = locationManager.getLastKnownLocation(provider);

		/**if (loc == null) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 1, 0, locationListener);
		}**/
		if (loc == null) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 1, 0, locationListener);
			if (locationManager != null) {
				loc = locationManager.getLastKnownLocation(provider);
				if (loc != null) {
					loc.getLatitude();
					loc.getLongitude();
				}
			}
		}

	}


	public Location getLocation() {
		return loc;
	}

	public void setCommunicator(Communicator comm) {
		communicator = comm;
	}

	public interface Communicator {
		public void locationUpdate();

		public void gpsTurnedOff();

		public void tempUnavailable();

		public void available();

		public void outOfService();
	}

}
