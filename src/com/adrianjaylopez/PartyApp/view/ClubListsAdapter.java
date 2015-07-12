package com.adrianjaylopez.PartyApp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import com.adrianjaylopez.PartyApp.R;
import com.adrianjaylopez.PartyApp.controller.ClubInfo;

import java.util.ArrayList;

public class ClubListsAdapter extends ArrayAdapter<ClubInfo> {
	Context context;
	ArrayList<ClubInfo> values;
	int resource;

	public ClubListsAdapter(Context context, int resource,
			ArrayList<ClubInfo> values) {
		super(context, resource, values);
		this.resource = resource;
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(this.resource, parent, false);
			viewHolder.club = (TextView) convertView
					.findViewById(R.id.clubName);
			viewHolder.rate = (RatingBar) convertView
					.findViewById(R.id.clubRatingBar);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
			viewHolder.club.setText(values.get(position).getName());
			viewHolder.rate.setRating((float) values.get(position).getRating());
		
		return convertView;
	}

	static class ViewHolder {
		TextView club;
		RatingBar rate;
	}

}
