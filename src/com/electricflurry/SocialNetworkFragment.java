package com.electricflurry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class SocialNetworkFragment extends Fragment {


	Button buttons;
	Button facebook, plus, twitter;


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
		plus = (Button) view.findViewById(R.id.gPlus_button);
		twitter = (Button) view.findViewById(R.id.twitter_button);

		//what buttons do
//		twitter.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(getActivity(), AndroidTwitterSample.class);
//				startActivity(intent);
//			}
//		});
		
		
		twitter.setOnClickListener(new View.OnClickListener() {
		     @Override
		     public void onClick(View v) {
		     Intent intent = new Intent(getActivity(), TestActivity.class);
		     startActivity(intent);
		     }
		 });

//		facebook.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(getActivity(), Facebook.class);
//				startActivity(intent);
//			}
//		});
		
		facebook.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).addFragment(R.id.fragment_holder, FacebookFragment.newInstance());
				Toast.makeText(getActivity(), "clicked on Facebook", Toast.LENGTH_SHORT).show();
			}
		});

		plus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), GooglePlusActivity.class);
				startActivity(intent);
			}
		});
		return view;

	}//end of onCreateView


}//end of class
