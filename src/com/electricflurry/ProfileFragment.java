package com.electricflurry;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.facebook.widget.ProfilePictureView;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment implements ConsumeCursor {
	// created the profile fragment just copying the mingle fragment -sean
	Profile profile = new Profile();
	ProfilePictureView profilePictureView;
	String id;

	public static ProfileFragment newInstance() {

		ProfileFragment f = new ProfileFragment();

		return f;
	}// end of static constructor

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.profile_fragment, container,
				false);
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());

		final ElectricFlurryDatabase db = new ElectricFlurryDatabase(
				getActivity());
		try {
			db.leQuery("user", new String[] { "name", "phone", "facebook",
					"twitter", "google" }, null, null, null, null, null, this);
		} catch (CursorIndexOutOfBoundsException e) {
			db.submitFirstUser();
		}
		//gets the name from the local database
		if (!profile.getName().equalsIgnoreCase("Unavailable")) {
			TextView profile_name = (TextView) view
					.findViewById(R.id.profile_name);
			profile_name.setText(profile.getName());
		}
		//gets the phone number from the local database
		if (!profile.getName().equalsIgnoreCase("Unavailable")) {
			TextView profile_phone = (TextView) view
					.findViewById(R.id.profile_phone);
			profile_phone.setText(profile.getName() + "'s Phone number is "
					+ profile.getPhoneNumber());
		}
		//gets the facebook URL from the local database
		if (!profile.getName().equalsIgnoreCase("Unavailable")) {
			TextView facebook_url = (TextView) view
					.findViewById(R.id.facebook_url);
			facebook_url.setText(profile.getName() + "'s Facebook: "
					+ profile.getFacebookURL());
		}
		//gets the Twitter URL from the local database
		if (!profile.getName().equalsIgnoreCase("Unavailable")) {
			TextView twitter_url = (TextView) view
					.findViewById(R.id.twitter_url);
			twitter_url.setText(profile.getName() + "'s Twitter: "
					+ profile.getTwitterURL());
		}
		//gets the google+ from the local database
		if (!profile.getName().equalsIgnoreCase("Unavailable")) {
			TextView google_url = (TextView) view.findViewById(R.id.google_url);
			google_url.setText(profile.getName() + "'s Google+: "
					+ profile.getGoogleURL());
		}
		//gets the profile picture from facebook
		profilePictureView = (ProfilePictureView) view.findViewById(R.id.profile_fbpic);
		id = preferences.getString("id", "");
		Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
		profilePictureView.setProfileId(id);

		return view;
	}// end of onCreateView

	@Override
	public void consumeCursor(Cursor cursor) {

		cursor.moveToPosition(0);
		profile.setName(cursor.getString(0));
		profile.setPhoneNumber(cursor.getString(1));
		profile.setFacebookURL(cursor.getString(2));
		profile.setTwitterURL(cursor.getString(3));
		profile.setGoogleURL(cursor.getString(4));

	}

}
