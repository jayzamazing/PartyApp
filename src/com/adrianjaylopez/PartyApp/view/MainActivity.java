package com.adrianjaylopez.PartyApp.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import com.adrianjaylopez.PartyApp.R;
import com.adrianjaylopez.PartyApp.view.FragmentA.Communicator;

public class MainActivity extends FragmentActivity implements FragmentA.Communicator{
	FragmentManager manager;
	FragmentA fragmentA;
	FragmentB fragmentB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		manager = getSupportFragmentManager();
		fragmentA = (FragmentA) manager.findFragmentById(R.id.fragment1);
		fragmentA.setCommunicator((Communicator) this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void NextFragment() {
		fragmentB = (FragmentB) manager.findFragmentById(R.id.fragment2);
		fragmentB.makeFieldsVisible();
	}


}
