package com.electricflurry;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CheckInFragment extends Fragment{
	
	String TAG = "CheckInFragment";
	TextView text;
	GPSUtilities gps;
	
	public static CheckInFragment newInstance() {
		
		CheckInFragment f = new CheckInFragment();
		
		return f;	
		
	}//end of static constructor
	
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		gps = new GPSUtilities((LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE),
				new LocationListener() {

					@Override
					public void onLocationChanged(Location location) {
						// TODO Auto-generated method stub
						if(gps.isWithinRange(location))
							text.setText("You are checked in! You fall in range of who can check in");
						else
							text.setText("You are not in range! \n Lon="+location.getLongitude()+" Lat="+location.getLatitude());
						
					}

					@Override
					public void onProviderDisabled(String provider) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onProviderEnabled(String provider) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onStatusChanged(String provider, int status,
							Bundle extras) {
						// TODO Auto-generated method stub
						
					}
			
		});
		
	}//end of onCreate
	
	
	
	
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.checkin_fragment, container, false);
		/*
		 * Your Fragment's view is created here so do work to make it look pretty*/
		text = (TextView)view.findViewById(R.id.checkin_text);
		
		try {
			text.setText("Last known coordinates: Lat="+gps.lastKnownLocation.getLatitude()+" Lon="+gps.lastKnownLocation.getLongitude());
		}
		catch(NullPointerException e) {
			text.setText("Oops! No previous location was found");
		}
		
		gps.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0);
		
		
		return view;
	}//end of onCreateView
	
	
	
	public void onPause() {
		super.onPause();
		gps.removeUpdates();
	}
	
	
	
	

}//end of class
