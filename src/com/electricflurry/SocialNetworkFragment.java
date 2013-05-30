package com.electricflurry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SocialNetworkFragment extends Fragment {


	Button buttons;
	Button facebook, twitter;
	String APP_ID = "547265858648856";
	


	public static SocialNetworkFragment newInstance() {
		SocialNetworkFragment f = new SocialNetworkFragment();

		return f;
	}//end of static constructor



	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

		View view = inflater.inflate(R.layout.social_network_fragment, container, false);

		//buttons
		facebook = (Button) view.findViewById(R.id.facebook_button);
		twitter = (Button) view.findViewById(R.id.twitter_button);

		//what buttons do
		twitter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), AndroidTwitterSample.class);
				startActivity(intent);
			}
		});

		facebook.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), FacebookFrag.class);
				startActivity(intent);
			}
		});


		return view;

	}//end of onCreateView


}//end of class
