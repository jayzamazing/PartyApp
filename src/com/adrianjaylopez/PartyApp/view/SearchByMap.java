package com.adrianjaylopez.PartyApp.view;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.adrianjaylopez.PartyApp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SearchByMap extends FragmentActivity {
	// Google Map
	private GoogleMap googleMap;
	private Location loc;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_map_search);

		loc = new Location(LocationManager.NETWORK_PROVIDER);
		loc.setLatitude(getIntent().getExtras().getDouble("lat"));
		loc.setLongitude(getIntent().getExtras().getDouble("lng"));

		try {
			// Loading map
			initilizeMap();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			googleMap.addMarker(new MarkerOptions().position(
					new LatLng(loc.getLatitude(), loc.getLongitude())).title(
					"You are here"));

			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(new CameraPosition.Builder()
							.target(new LatLng(loc.getLatitude(), loc
									.getLongitude())).zoom(16.5f).bearing(0)
							.tilt(25).build()));
		}

		googleMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng point) {
				loc.setLatitude(point.latitude);
				loc.setLongitude(point.longitude);
				googleMap.clear();
				googleMap.addMarker(new MarkerOptions()
				.position(new LatLng(point.latitude, point.longitude))
						.title("Location to Search"));
			}
		});
	}
	
	public void ShowClubList(View view){
		Intent clubList = new Intent(this, ClubList.class);
		clubList.putExtra("lat", loc.getLatitude());
		clubList.putExtra("lng", loc.getLongitude());
		startActivity(clubList);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

}
