package com.adrianjaylopez.PartyApp.view;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.adrianjaylopez.PartyApp.R;
import com.adrianjaylopez.PartyApp.controller.GPSService;

public class FragmentA extends Fragment {

    Communicator comm;
    Button specificAddress, currentLocation, useMap, history;
    private boolean isBound;
    private GPSService gpsService;
    AlertDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        isBound = false;
        return inflater.inflate(R.layout.activity_main_a, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent objIntent = new Intent(getActivity(), GPSService.class);
        if (!isBound) {
            getActivity().startService(objIntent);
            getActivity().bindService(objIntent, myConnection,
                    Context.BIND_AUTO_CREATE);

        } else {
            getActivity().getApplicationContext().unbindService(myConnection);
        }

    }

    public void actions() {
        specificAddress = (Button) getActivity().findViewById(
                R.id.specificAddressButton);
        currentLocation = (Button) getActivity().findViewById(
                R.id.currentLocationButton);
        useMap = (Button) getActivity().findViewById(R.id.useMapButton);
        history = (Button) getActivity().findViewById(R.id.historyButton);

        currentLocation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent clubList = new Intent(getActivity(), ClubList.class);
                clubList.putExtra("lat", gpsService.getLoc().getLatitude());
                clubList.putExtra("lng", gpsService.getLoc().getLongitude());
                clubList.putExtra("type", "current");
                startActivity(clubList);

            }
        });
        history.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent clubList = new Intent(getActivity(), ClubList.class);
                clubList.putExtra("type", "history");
                startActivity(clubList);

            }
        });
        useMap.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent map = new Intent(getActivity(), SearchByMap.class);
                map.putExtra("lat", gpsService.getLoc().getLatitude());
                map.putExtra("lng", gpsService.getLoc().getLongitude());
                startActivity(map);
            }
        });

        specificAddress.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                comm.NextFragment();
            }
        });
    }

    public void setCommunicator(Communicator comm) {
        this.comm = comm;
    }

    public interface Communicator {
        public void NextFragment();
    }

    private ServiceConnection myConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {

            gpsService = ((GPSService.MyBinder) service).getService();

            isBound = true;
            checkGPS();
        }

        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }

    };

    public void checkGPS() {
        if (gpsService.gpsStatus() == true) {
            actions();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Enable GPS.");
            builder.setPositiveButton(R.string.yes,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent gpsOptionsIntent = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                            startActivity(gpsOptionsIntent);
                        }
                    });
            builder.setNegativeButton(R.string.no,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });

            dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        checkGPS();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBound) {
            // Disconnect from an application service. You will no longer
            // receive calls as the service is restarted, and the service is
            // now allowed to stop at any time.
            getActivity().unbindService(myConnection);
            isBound = false;
        }
    }
}
