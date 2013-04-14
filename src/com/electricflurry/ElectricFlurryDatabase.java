package com.electricflurry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ElectricFlurryDatabase {

	/*
	 * This main class uses our database rather than access it directly
	 * */
	
	private String DB_NAME = "electricflurry.db";
	private int DB_VERSION = 1;
	Profile initial = new Profile();
	
	private ElectricFlurryOpenHelper openHelper; //Opens the databse for us
	private SQLiteDatabase database;//access to the database ports for Android
	
	public ElectricFlurryDatabase(Context context) {
		openHelper = new ElectricFlurryOpenHelper(context);
		database = openHelper.getWritableDatabase();
		
//	ContentValues initialValues = new ContentValues();
//	initialValues.put("name", initial.getName());
//	initialValues.put("phone", initial.getPhoneNumber());
//	initialValues.put("facebook", initial.getFacebookURL());
//	initialValues.put("twitter", initial.getTwitterURL());
//	initialValues.put("google", initial.getGoogleURL());
//	
//	database.insert("user", null, initialValues);
	}//end of constructor
	
	public void closeDbase() {
		database.close();
	}
	
	
	/*
	 * Methods that interact with the database
	 * */
	public int checkUser() {
		Cursor c = database.query("user", new String[] {"name", "phone", "facebook", "twitter", "google"}, null, null, null, null, null);
		return c.getCount();
//		if (c.getCount() == 1) {
//			return 1;
//		}else if (c.getCount() > 1) {
//			return -2;
//		} else {
//		 	return -1;
//		}
		}
	
	public void submitUser(Profile profile) {
		ContentValues values = new ContentValues();
		Profile user = new Profile();
		if (checkUser() == 1) {
			Cursor c = database.query("user", new String[] {"name", "phone","facebook","twitter","google"}, null, null, null, null, null);
			c.moveToPosition(0);
			user.setName(c.toString());
			c.moveToPosition(1);
			user.setPhoneNumber(c.toString());
			c.moveToPosition(2);
			user.setFacebookURL(c.toString());
			c.moveToPosition(3);
			user.setTwitterURL(c.toString());
			c.moveToPosition(4);
			user.setGoogleURL(c.toString());	
		}
		if (!profile.getName().equalsIgnoreCase("Not available")) {
			user.setName(profile.getName());
		}
		if (!profile.getPhoneNumber().equalsIgnoreCase("Not available")) {
			user.setName(profile.getPhoneNumber());
		}
		if (!profile.getFacebookURL().equalsIgnoreCase("Not available")) {
			user.setName(profile.getFacebookURL());
		}
		if (!profile.getTwitterURL().equalsIgnoreCase("Not available")) {
			user.setName(profile.getTwitterURL());
		}
		if (!profile.getGoogleURL().equalsIgnoreCase("Not available")) {
			user.setName(profile.getGoogleURL());
		}
		values.put("name", user.getName());
		values.put("phone", user.getPhoneNumber());
		values.put("facebook", user.getFacebookURL());
		values.put("twitter", user.getTwitterURL());
		values.put("google", user.getGoogleURL());
		
		database.replace("user", null, values);
	}
	
	
	public void submitNewUser(String name, String phoneNum) {
		/*phone is optional
		 * can send null to just not include it*/
		ContentValues values = new ContentValues();
		values.put("user", name);
		
		if(phoneNum!=null)
			values.put("phone", phoneNum);
	
		database.replace("users", "phone", values);
		
	}//end of submitNewUser() method
	
	public void submitSocialUrl(String type, String url) {
		/*
		 * Type should be something like
		 * facebook/twitter/google/foursquare*/
		
		ContentValues values = new ContentValues();
		values.put("type", type);
		values.put("url", url);
		
		database.replace("social_urls", null, values);
		
		
	}//end of submitSocialUrl() method
	
	
	/*
	 * Method that allows for simple queries from the database
	 * */
	public ConsumeCursor leQuery(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having, String orderBy, ConsumeCursor teamsObj){
		
		teamsObj.consumeCursor( database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy) );
		
		return teamsObj;/*I will return this just incase, but I can just as easily return nothing*/
		
	}//end of leQuery() method
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * Inner class that actually creates the database
	 * */
	private class ElectricFlurryOpenHelper extends SQLiteOpenHelper {
		
		public ElectricFlurryOpenHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase dBase) {
			/*
			 * This first creates the database
			 * and is only run once until a new
			 * version number is sent to it
			 * */
			String userCreate = " CREATE TABLE user (_id INTEGER PRIMARY KEY, user text, phone text, facebook text, twitter text, google text) ";
			dBase.execSQL(userCreate);
			
			String socialUrlCreate = " CREATE TABLE social_urls (_id INTEGER PRIMARY KEY, type text, url text) ";
			dBase.execSQL(socialUrlCreate);
			
		}//end of constructor

		@Override
		public void onUpgrade(SQLiteDatabase dBase, int oldVersion, int newVersion) {
			/*This is used when we want to upgrade
			 * users database if any changes are made
			 * but for development purposes we should just
			 * delete app data to tell android to invoke create again*/
			
		}//end of onUpgrade
		
	}//end of inner class
	
	
	
	
}//end of class
