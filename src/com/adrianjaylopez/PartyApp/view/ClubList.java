package com.adrianjaylopez.PartyApp.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.adrianjaylopez.PartyApp.R;
import com.adrianjaylopez.PartyApp.controller.ClubInfo;
import com.adrianjaylopez.PartyApp.controller.ClubInfoJSONParser;
import com.adrianjaylopez.PartyApp.controller.DBController;
import com.adrianjaylopez.PartyApp.controller.DownloadPictures;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClubList extends Activity {

    ClubInfoJSONParser clubInfo;
    ArrayList<ClubInfo> clubs;
    ListView listView;
    ProgressDialog dialog;
    boolean progressRun = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clublist);


        listView = (ListView) findViewById(R.id.clubListView);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Bundle bundle = new Bundle();
                ArrayList<File> pictureList = new ArrayList<File>();

                DownloadPictures dlPictures = new DownloadPictures(
                        getApplicationContext());
                try {
                    pictureList = dlPictures.execute(clubs.get(arg2).getUrl())
                            .get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                bundle.putSerializable("pictures", pictureList);
                bundle.putParcelableArrayList("clubs",
                        (ArrayList<? extends Parcelable>) clubs);
                bundle.putInt("position", arg2);
                Intent intent = new Intent(ClubList.this, SingleClubInfo.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        clubInfo = new ClubInfoJSONParser(this);
        //TODO future implementation for history button, WIP
        if (String.valueOf(getIntent().getExtras().getString("type")).equals(
                "history")) {

            DBController db = new DBController(getApplicationContext());
            List<HashMap<String, String>> history = null;
            try {
                history = db.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (history == null) {
                Toast.makeText(getApplicationContext(),
                        "No history stored, please click back button",
                        Toast.LENGTH_LONG).show();
            }
            String[] temp = (String[]) history.toArray();

            try {
                clubs = clubInfo.execute(temp).get();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else {

            //Location loc = (Location) getIntent().getExtras().get("location");
            clubInfo.execute(String.valueOf(getIntent().getExtras().getDouble("lat")),
                    String.valueOf(getIntent().getExtras().getDouble("lng")));
            new WaitingProgress().execute();
        }

    }

    class WaitingProgress extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {
                clubs = clubInfo.get();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ClubListsAdapter adapter = new ClubListsAdapter(ClubList.this,
                    R.layout.clubname, clubs);
            listView.setAdapter(adapter);
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(ClubList.this);
            dialog.setTitle("Loading");
            dialog.setMessage("Please wait");
            dialog.setCancelable(true);
            dialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            while (clubInfo == null
                    && !clubInfo.getStatus().equals(AsyncTask.Status.FINISHED)) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return null;
        }

    }

}
