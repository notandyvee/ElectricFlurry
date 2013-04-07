package com.electricflurry;

import android.app.ProgressDialog;
import android.location.LocationListener;
import android.location.LocationManager;
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
	Button facebook, googlePlus, twitter;
	
	
	public static SocialNetworkFragment newInstance() {
		SocialNetworkFragment f = new SocialNetworkFragment();
		
		return f;
	}//end of static constructor
	
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	}
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
		
		View view = inflater.inflate(R.layout.social_network_fragment, container, false);
		
		
/*		
		TextView header = (TextView) view.findViewById(R.id.socialtext); //in XML file socialnetworkfragment, get id
		header.setText ("Social Networks");
		
		TextView facebook = (TextView)view.findViewById(R.id.facebook);
		TextView googleplus = (TextView)view.findViewById(R.id.googlePlus);
		TextView twitter = (TextView)view.findViewById(R.id.twitter);
		//TextView foursquare = (TextView)view.findViewById(R.id.foursquare);
		
		text = (TextView)view.findViewById(R.id.name);
*/		

		
		Button facebookBtn = (Button) view.findViewById(R.id.facebook_button);
		Button gPlusBtn = (Button) view.findViewById(R.id.gPlus_button);
		Button twitterBtn = (Button) view.findViewById(R.id.twitter_button);
		
		buttons = (Button)view.findViewById(R.id.name);
		
		
/*	
facebookBtn.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		((MainActivity)getActivity()).addFragment(R.id.fragment_holder, FacebookFragment.newInstance());
		Toast.makeText(getActivity(), "clicked on facebook", Toast.LENGTH_SHORT).show();
	}
});

twitterBtn.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		((MainActivity)getActivity()).addFragment(R.id.fragment_holder, TwitterFragment.newInstance());
		Toast.makeText(getActivity(), "clicked on twitter", Toast.LENGTH_SHORT).show();
	}
});
*/

	gPlusBtn.setOnClickListener(new View.OnClickListener() {

	@Override
	public void onClick(View v) {
		((MainActivity)getActivity()).addFragment(R.id.fragment_holder, GPlusVisFrag.newInstance());
		Toast.makeText(getActivity(), "clicked on googlePlus", Toast.LENGTH_SHORT).show();
	}
});
return view;
	
}//end of onCreateView
	
	

}//end of class
