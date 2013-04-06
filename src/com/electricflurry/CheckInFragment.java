package com.electricflurry;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

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

public class CheckInFragment extends Fragment{
	
	String TAG = "CheckInFragment";
	TextView text;
	GPSUtilities gps;
	SharedPreferences settings;
	String PREF_NAME = "MySocialSettings";
	
	public static CheckInFragment newInstance() {
		
		CheckInFragment f = new CheckInFragment();
		
		return f;	
		
	}//end of static constructor
	
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		settings = getActivity().getSharedPreferences(PREF_NAME, 0);
		
		if(settings.getString("checked_in", null) == null) {
			
			gps = new GPSUtilities((LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE),
					new LocationListener() {
	
						@Override
						public void onLocationChanged(Location location) {
							
							if(location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
								Log.d("CHECKIN", "Its running for NETWORK");
								if(gps.isWithinRange(location)) {
									text.setText("You are checked in! You fall in range of who can check in");
									gps.removeUpdates();//if checked in through network then great! Just leave since GPS would be pointless
									Calendar now = Calendar.getInstance();
									SharedPreferences.Editor editor = settings.edit();
									editor.putString("checked_in", now.get(Calendar.MONTH)+ "/"+
											now.get(Calendar.DAY_OF_MONTH)+"/"+now.get(Calendar.YEAR));
									editor.commit();
								}
								else
									text.setText("You are not in range! \n Lon="+location.getLongitude()+" Lat="+location.getLatitude() +
											"\n Accuracy is "+ location.getAccuracy());
								
								
								
							} else {
								//else I know its GPS
								Log.d("CHECKIN", "Its running for GPS");
								if(gps.isWithinRange(location)) {
									text.setText("You are checked in! You fall in range of who can check in");
									gps.removeUpdates();//if checked in through network then great! Just leave since GPS would be pointless
									Calendar now = Calendar.getInstance();
									SharedPreferences.Editor editor = settings.edit();
									editor.putString("checked_in", now.get(Calendar.MONTH)+ "/"+
											now.get(Calendar.DAY_OF_MONTH)+"/"+now.get(Calendar.YEAR));
									editor.commit();
								}
								else
									text.setText("You are not in range! \n Lon="+location.getLongitude()+" Lat="+location.getLatitude()+
											"\n Accuracy is "+ location.getAccuracy());
								
								
							}
							
						}//end of onLocationChanged
	
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
							text.setText(provider+" is just not working. \n Status code is: "+status);
							
						}
				
			});
			
		}//end of if trhat checks if user ever checked in
		
		
		
		
	}//end of onCreate
	
	
	
	
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.checkin_fragment, container, false);
		/*
		 * Your Fragment's view is created here so do work to make it look pretty*/
		text = (TextView)view.findViewById(R.id.checkin_text);
		
		if(settings.getString("checked_in", null) == null) {
		
			try {
				text.setText("Last known coordinates: Lat="+gps.lastKnownLocation.getLatitude()+" Lon="+gps.lastKnownLocation.getLongitude());
			}
			catch(NullPointerException e) {
				text.setText("Oops! No previous location was found");
			}
			
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					text.post(new Runnable() {
						@Override
						public void run() {
							text.setText("A minute passed so quit it at this point if location has not bee found");
							gps.removeUpdates();
						}
					});
				}
			}, 60000L);
			
			gps.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 20f, LocationManager.GPS_PROVIDER, 0, 20f);
			
		} else {
			
			text.setText("You are already checked in!\n Enjoy the show!");
			
		}
		
		return view;
	}//end of onCreateView
	
	
	
	public void onPause() {
		super.onPause();
		if(gps != null)
			gps.removeUpdates();
	}
	
	
	
	

}//end of class
