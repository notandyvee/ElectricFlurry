package com.electricflurry;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
		
		final ElectricFlurryDatabase db = new ElectricFlurryDatabase(getActivity());
		
		profile_name = (TextView)view.findViewById(R.id.profile_name);
		profile_phone = (TextView)view.findViewById(R.id.profile_phone);
		facebook_url = (TextView)view.findViewById(R.id.facebook_url);
		twitter_url = (TextView) view.findViewById(R.id.twitter_url);
		google_url = (TextView)view.findViewById(R.id.google_url);
		
		db.leQuery("users", new String[] {"user", "phone"}, null, null, null, null, null, null);
		
		profile_name.setText("Profile name");
		profile_phone.setText("Phone");
		facebook_url.setText("Facebook");
		twitter_url.setText("Twitter");
		google_url.setText("Google");

		
		return view;
	}//end of onCreateView


	@Override
	public void consumeCursor(Cursor cursor) {
		for (int i = 0; i < cursor.getColumnCount(); i++) {
			cursor.getColumnName(i);
		}
		
	}
	
}
