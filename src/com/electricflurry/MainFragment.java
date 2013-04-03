package com.electricflurry;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainFragment extends Fragment{
	
	String PREF_NAME = "MySocialSettings"; //preferences for oAuth stuff to store
	String TAG = "MainFragment";
	public static final String LE_FRAGMENT = "fragment_holder";
	
	TextView text;
	LocationManager locationManager;
	LocationListener locationListener;
	
	String oAuth;
	
	public static MainFragment newInstance() {
		MainFragment f = new MainFragment();
		
		/*
		 * Don't really need to put any arguments right now so won't bother*/
		//Bundle args = new Bundle();
		//args.putString("test", "Text I put when creating myself");
		//f.setArguments(args);
		
		return f;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		/*Seems Fragment has automatic access to some values here like getArguments()*/
		super.onCreate(savedInstanceState);
		
		locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
		SharedPreferences settings = getActivity().getSharedPreferences(PREF_NAME, 0);
		oAuth = settings.getString("foursquare_oauth_token", null);
		
		
	}//end of onCreate
	
	
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_fragment, container, false);
		/*
		 * Where yo shit will go that will populate 
		 * the Fragments Views dynamically
		 * */
		text = (TextView)view.findViewById(R.id.name);
		
		doGPS();
		
		
		if(oAuth == null) {
			text.setText("Login to Foursquare!");
			
			text.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Here I will see if the fragment stuff is launching correctly
					((MainActivity)getActivity()).addFragment(
							R.id.fragment_holder, FoursquareAuthFragment.newInstance(getActivity().getSharedPreferences(PREF_NAME, 0)));
					
				}
			});
		
		}
		
		
		return view;
		
	}//end of onCreateView
	
	
	
	
	
	
	
	public void onResume() {
		super.onResume();
		//Log.d(TAG, "onResume is running!");
	}
	
	public void onPause() {
		super.onPause();
		/*For now will keep this here since I want 
		 * the stop collecting anything here*/
		locationManager.removeUpdates(locationListener);
		//Log.d(TAG, "onPause runs!!");
	}//end of onPause
	
	public void onDestroy() {
		super.onDestroy();
		//Log.d(TAG, "onDestory is running!");
	}
	
	
	
	/*This following method will just be in charge of GPS thing as a test
	 * */
	public void doGPS() {
		//this manages the location stuff
		
		
		//this creates a listener so it can do stuff I tell it with the location
		locationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				
				if(oAuth != null) {
					final Foursquare foursquare = new Foursquare( oAuth,location.getLatitude()+","+location.getLongitude());
					
					new Thread(new Runnable() {
						@Override
						public void run() {
							foursquare.queryFoursquareData();//this should be done on separate thread!
							
							text.post(new Runnable(){
								@Override
								public void run() {
									text.setText(foursquare.returnLeString());
								}
							});
							
						}
					}).start();
					
					
					
					
				}
				//text.setText("Lon="+location.getLongitude()+" Lat="+location.getLatitude());
				
				locationManager.removeUpdates(locationListener);
				
			}//end of onLocationChanged

			@Override
			public void onProviderDisabled(String provider) {}

			@Override
			public void onProviderEnabled(String provider) {}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {}
			
		};
		
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		
		
	}//end of doGPS
	
	
	
	
	
	
	
	
	
	
	

}//end of class
