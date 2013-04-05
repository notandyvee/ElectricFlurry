/*
 * This is a utility class with the purpose
 * of helping me separate this location stuff
 * while allowing easy access to Location services.*/
package com.electricflurry;

import android.location.LocationListener;
import android.location.LocationManager;

public class GPSUtilities {
	LocationManager locationManager;
	LocationListener locationListener;
	
	
	public GPSUtilities(LocationManager manager, LocationListener listener) {
		locationManager = manager;
		locationListener = listener;
		
	}//private constructor, also known as a singleton
	
	
	
	
	public void removeUpdates() {
		
		locationManager.removeUpdates(locationListener);
		
	}//end of removeUpdates
	
	
	public void requestLocationUpdates(String provider, int minTime, int minDistance) {
		
		locationManager.requestLocationUpdates(provider, minTime, minDistance, locationListener);
		
	}//end of requestLocationUpdates
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * The listener for when I get the location
	 * */
	
	
	
	
	
	
	

}//end of class
