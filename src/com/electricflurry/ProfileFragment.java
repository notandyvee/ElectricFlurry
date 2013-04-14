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
	Profile profile = new Profile();
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

		db.leQuery("user", new String[] {"user", "phone", "facebook", "twitter", "google"}, null, null, null, null, null, this);

		profile_name.setText(profile.getName());
		profile_phone.setText(profile.getPhoneNumber());
		facebook_url.setText(profile.getFacebookURL());
		twitter_url.setText(profile.getTwitterURL());
		google_url.setText(profile.getGoogleURL());
		
		return view;
	}//end of onCreateView


	@Override
	public void consumeCursor(Cursor cursor) {
		
		cursor.moveToFirst();
		profile.setName(cursor.toString());
		cursor.moveToPosition(1);
		profile.setPhoneNumber(cursor.toString());
		cursor.moveToPosition(2);
		profile.setFacebookURL(cursor.toString());
		cursor.moveToPosition(3);
		profile.setTwitterURL(cursor.toString());
		cursor.moveToPosition(4);
		profile.setGoogleURL(cursor.toString());

	}
	
}
