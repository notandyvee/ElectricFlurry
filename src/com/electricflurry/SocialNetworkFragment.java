package com.electricflurry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SocialNetworkFragment extends Fragment {

	Button buttons;

	TextView photo, facebook, plus, twitter;

	public static SocialNetworkFragment newInstance() {
		SocialNetworkFragment f = new SocialNetworkFragment();

		return f;
	}// end of static constructor

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstance) {

		View view = inflater.inflate(R.layout.social_network_fragment,
				container, false);

		// buttons
		facebook = (TextView) view.findViewById(R.id.facebook_button);
		plus = (TextView) view.findViewById(R.id.gPlus_button);
		twitter = (TextView) view.findViewById(R.id.twitter_button);
		photo = (TextView) view.findViewById(R.id.photo);

		// what buttons do
		// twitter.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Intent intent = new Intent(getActivity(),
		// AndroidTwitterSample.class);
		// startActivity(intent);
		// }
		// });

		twitter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						AndroidTwitterSample.class);
				startActivity(intent);
			}
		});

		// facebook.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Intent intent = new Intent(getActivity(), Facebook.class);
		// startActivity(intent);
		// }
		// });

		facebook.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).addFragment(
						R.id.fragment_holder, FacebookFragment.newInstance());
			}
		});

		plus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						GooglePlusActivity.class);
				startActivity(intent);
			}
		});

		photo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), PhotoTaker.class);
				startActivity(intent);
			}
		});

		return view;

	}// end of onCreateView

}// end of class
