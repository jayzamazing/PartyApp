package com.adrianjaylopez.PartyApp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.adrianjaylopez.PartyApp.R;
import com.adrianjaylopez.PartyApp.controller.GeoLocations;

import java.util.concurrent.ExecutionException;

public class FragmentB extends Fragment {
	EditText street, city, state;
	Button getGeo;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_main_b, container, false);
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		street = (EditText) getActivity().findViewById(R.id.streetEditText);
		city = (EditText) getActivity().findViewById(R.id.cityEditText);
		state = (EditText) getActivity().findViewById(R.id.stateEditText);
		getGeo = (Button) getActivity().findViewById(R.id.submitGeoButton);
		getGeo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GeoLocations geo = new GeoLocations(getActivity());
				try {
					double[] coordinates = geo.execute(formatAddress()).get();
					Intent clubList = new Intent(getActivity(), ClubList.class);
					clubList.putExtra("lat", coordinates[0]);
					clubList.putExtra("lng", coordinates[1]);
					startActivity(clubList);
				} catch (InterruptedException e) {
										e.printStackTrace();
				} catch (ExecutionException e) {
										e.printStackTrace();
				}				
			}
		});
	}
	
	public void makeFieldsVisible(){
		street.setVisibility(View.VISIBLE);
		city.setVisibility(View.VISIBLE);
		state.setVisibility(View.VISIBLE);
		getGeo.setVisibility(View.VISIBLE);
	}
	private String formatAddress(){
		return replaceSpaces(street.getText().toString()) +
				"," + replaceSpaces(city.getText().toString()) +
				"," + replaceSpaces(state.getText().toString());
	}
	private String replaceSpaces(String temp){
		return temp.replace(" ", "+");
	}
	
}
