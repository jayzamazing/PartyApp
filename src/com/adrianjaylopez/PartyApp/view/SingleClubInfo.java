package com.adrianjaylopez.PartyApp.view;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.adrianjaylopez.PartyApp.R;
import com.adrianjaylopez.PartyApp.controller.ClubInfo;

import java.io.File;
import java.util.ArrayList;

public class SingleClubInfo extends Activity {
	ArrayList<ClubInfo> clubs;
	ArrayList<File> pictureList;
	TextView clubName, clubAddress;
	ImageView clubPicture;
	int position, picturePosition = 0;
	Bitmap myBitmap;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clubinfo);
		clubName = (TextView) findViewById(R.id.clubNameTextView);
		clubAddress = (TextView) findViewById(R.id.clubAddressTextView);
		clubPicture  = (ImageView) findViewById(R.id.clubImageView);
		
		
		pictureList = (ArrayList<File>) getIntent().getExtras().getSerializable( "pictures" );
		clubs = getIntent().getExtras().getParcelableArrayList("clubs");
		position = getIntent().getExtras().getInt("position");
		myBitmap = BitmapFactory.decodeFile(pictureList.get(picturePosition).getAbsolutePath());
		
		clubName.setText(clubs.get(position).getName());
		clubAddress.setText(clubs.get(position).getAddress());
		clubPicture.setImageBitmap(myBitmap);
	}
	
	public void PreviousImage(View view){
		if (picturePosition == 0){
			picturePosition = pictureList.size() - 1;
		} else 
			picturePosition -= 1;
		myBitmap = BitmapFactory.decodeFile(pictureList.get(picturePosition).getAbsolutePath());
		clubPicture.setImageBitmap(myBitmap);
	}
	
	public void NextImage(View view){
		if (picturePosition == pictureList.size() - 1){
			picturePosition = 0;
		} else 
			picturePosition += 1;
		myBitmap = BitmapFactory.decodeFile(pictureList.get(picturePosition).getAbsolutePath());
		clubPicture.setImageBitmap(myBitmap);
	}
	
	public void NavigateTo(View view){
		/*DBController db = new DBController(getApplicationContext());
		String[] temp = new String[3];
		temp[0] = clubs.get(position).getName();
		temp[1] = String.valueOf(clubs.get(position).getLat());
		temp[2] = String.valueOf(clubs.get(position).getLng());
		String[] temp2 = new String[1];
		temp2[0] = "insert";
		db.execute(temp2, temp);*/
		
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + clubs.get(position).getLat()+","+ clubs.get(position).getLng()));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
