package com.electricflurry;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment implements ConsumeCursor{
	// created the profile fragment just copying the mingle fragment -sean
	TextView profile_phone, profile_name, facebook_url, twitter_url, google_url;
	String[] userContents = new String[2];
	String[] urlContents = new String[3];
	int urlCount = 0;
	
	public static ProfileFragment newInstance() {
		
		ProfileFragment f = new ProfileFragment();
		
		return f;
	}//end of static constructor
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.profile_fragment, container, false);
		userContents[0] = "Not available";
		userContents[1] = "Not available";
		urlContents[0] = "Not available";
		urlContents[1] = "Not available";
		urlContents[2] = "Not available";
		final ElectricFlurryDatabase db = new ElectricFlurryDatabase(getActivity());
		
		profile_name = (TextView)view.findViewById(R.id.profile_name);
		profile_phone = (TextView)view.findViewById(R.id.profile_phone);
		facebook_url = (TextView)view.findViewById(R.id.facebook_url);
		twitter_url = (TextView) view.findViewById(R.id.twitter_url);
		google_url = (TextView)view.findViewById(R.id.google_url);
		
		db.leQuery("users", new String[] {"user", "phone"}, null, null, null, null, null, this);
		db.leQuery("social_urls", new String[] {"type", "url"}, null, null, null, null, null, this);
		db.closeDbase();
		
		profile_name.setText(userContents[0]);
		profile_phone.setText(userContents[1]);
		facebook_url.setText(urlContents[0]);
		twitter_url.setText(urlContents[1]);
		google_url.setText(urlContents[2]);

		
		return view;
	}//end of onCreateView


	@Override
	public void consumeCursor(Cursor cursor) {
		if (!cursor.moveToFirst()) {
			Toast.makeText(getActivity(), "The database is empty.", Toast.LENGTH_SHORT).show();
			
		} else if(cursor.getColumnName(0).equalsIgnoreCase("user")) {
			cursor.moveToFirst();
			for (int j = 0; j <= cursor.getCount(); j++) {
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					userContents[i] = cursor.getString(i);
				}
				cursor.moveToPosition(j);
			}	
		} else if (cursor.getColumnName(0).equalsIgnoreCase("type")) {
			cursor.moveToFirst();
			for(int j = 0; j <= cursor.getCount(); j++) {
				if(cursor.getString(0).equalsIgnoreCase("facebook")) {
					urlContents[0] = cursor.getString(1);
				} else if(cursor.getString(0).equalsIgnoreCase("twitter")) {
					urlContents[1] = cursor.getString(1);
				} else if (cursor.getString(0).equalsIgnoreCase("google")) {
					urlContents[2] = cursor.getString(1);
				}
				cursor.moveToPosition(j);
			}
		} else {
			Toast.makeText(getActivity(), "There was an issue.", Toast.LENGTH_SHORT).show();
		}

	}
	
}
