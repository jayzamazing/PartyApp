package com.adrianjaylopez.PartyApp.controller;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ClubInfo implements Parcelable{
	private String name, reference, address;
	private double lat, lng;
	private double rating;
	private ArrayList<String> url;
	
	public ClubInfo(String name, double lat, double lng, String reference, String address,
			double rating, ArrayList<String> url){
		this.name = name;
		this.lat = lat;
		this.lng = lng;
		this.reference = reference;
		this.address = address;
		this.rating = rating;
		this.url = url;
	}
	
	public String getAddress() {
		return address;
	}
	
	public ArrayList<String> getUrl() {
		return url;
	}

	public void setUrl(ArrayList<String> url) {
		this.url = url;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}
	
	
	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@SuppressWarnings("unchecked")
	public void readFromParcel ( Parcel in){
		name = in.readString();
		lat = in.readDouble();
		lng = in.readDouble();
		reference = in.readString();
		address = in.readString();
		rating = in.readDouble();
		url = (ArrayList<String>) in.readSerializable();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void writeToParcel( Parcel dest, int flags ) {
		dest.writeString( name );
		dest.writeDouble( lat );
		dest.writeDouble( lng );
		dest.writeString( reference );
		dest.writeString( address );
		dest.writeDouble( rating );
		dest.writeSerializable(url);
	}
	
	private ClubInfo( Parcel in ) {
		super();
		readFromParcel( in );
	}
	
	public ClubInfo(String none){}

	public static final Creator<ClubInfo>	CREATOR	= new Creator<ClubInfo>() {
		/*
		 * (non-Javadoc)
		 * @see
		 * android.os.Parcelable.Creator
		 * #createFromParcel
		 * (android.os.Parcel)
		 */
		public ClubInfo createFromParcel( Parcel in ) {
			return new ClubInfo( in );
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * android.os.Parcelable.Creator
		 * #newArray(int)
		 */
		public ClubInfo[] newArray( int size ) {
			return new ClubInfo[size];
		}
	};
	
}
