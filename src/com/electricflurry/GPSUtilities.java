/*
 * This is a utility class with the purpose
 * of helping me separate this location stuff
 * while allowing easy access to Location services.*/
package com.electricflurry;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class GPSUtilities {
	LocationManager locationManager;
	LocationListener locationListener;
	Location efLocation;// this location is simply a fake location to test checkin with
	Location lastKnownLocation;
	final float MAX_DISTANCE = 100f;
	
	
	public GPSUtilities(LocationManager manager, LocationListener listener) {
		locationManager = manager;
		locationListener = listener;
		
		/*
		 * This is just a dummy location of where */
		efLocation = new Location(LocationManager.GPS_PROVIDER);
		efLocation.setLatitude(43.452697);
		efLocation.setLongitude(-76.54243300);
		
		//get last known location
		lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
	}//private constructor, also known as a singleton
	
	
	
	
	public void removeUpdates() {
		
		locationManager.removeUpdates(locationListener);
		
	}//end of removeUpdates
	
	
	public void requestLocationUpdates(String provider, long minTime, float minDistance) {
		
		locationManager.requestLocationUpdates(provider, minTime, minDistance, locationListener);
		
	}//end of requestLocationUpdates
	
	public boolean isWithinRange(Location newLocation) {
		/*If newLocation is null I can just use current location
		 * */
		if(newLocation != null) {
			
			if(efLocation.distanceTo(newLocation) < MAX_DISTANCE)
				return true;
				
		} else {
			//if null just use last known location
			if(lastKnownLocation != null) {
				if(efLocation.distanceTo(lastKnownLocation) < MAX_DISTANCE)
					return true;
			}
			
		}
		
		return false;
	}//end of isWithinRange
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * The listener for when I get the location
	 * */
	
	
	
	
	
	
	

}//end of class
