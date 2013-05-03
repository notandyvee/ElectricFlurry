package com.electricflurry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


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
		
	}//end of constructor
	
	public void closeDbase() {
		database.close();
	}
	
	
	/*
	 * Methods that interact with the database
	 * */
	
	public void submitUser(Profile user) {
		ContentValues values = new ContentValues();
		values.put("name", user.getName());
		values.put("phone", user.getPhoneNumber());
		values.put("facebook", user.getFacebookURL());
		values.put("twitter", user.getTwitterURL());
		values.put("google", user.getGoogleURL());
		
		database.update("user",values, null, null);
		database.insert("temp_profiles", null, values);
		
	}
	

	public void submitFirstUser() {
		/*phone is optional
		 * can send null to just not include it*/
		ContentValues values = new ContentValues();
		Profile profile = new Profile();
		values.put("name", profile.getName());
		values.put("phone", profile.getPhoneNumber());
		values.put("facebook", profile.getFacebookURL());
		values.put("twitter", profile.getTwitterURL());
		values.put("google", profile.getGoogleURL());
		
		database.insert("user", null, values);
		
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
	 * The next few methods are specific to votes simulation stuff
	 * since having a serve will make this pointless except maybe
	 * when caching the stuff*/
	public void insertTempProfiles() {
		ContentValues tim = new ContentValues();
		tim.put("name", "Tim Lawrence");
		tim.put("phone", "123-465-4352");
		tim.put("facebook", "facebook.com/tim.lawrence9");
		tim.put("twitter", "twitter.com/timmy_law");
		tim.put("google", "Unavailable");
		database.insert("temp_profiles", null, tim);
		
		ContentValues hank = new ContentValues();
		hank.put("name", "Hank Peterson");
		hank.put("phone", "785-235-8867");
		hank.put("facebook", "Unavailable");
		hank.put("twitter", "Unavailable");
		hank.put("google", "Unavailable");
		database.insert("temp_profiles", null, hank);
		
		ContentValues juliet = new ContentValues();
		tim.put("name", "Juliet Thompson");
		tim.put("phone", "234-675-1375");
		tim.put("facebook", "facebook.com/j.thompson");
		tim.put("twitter", "twitter.com/juliet1234");
		tim.put("google", "plus.google.com/u/julieT1234/posts");
		database.insert("temp_profiles", null, tim);
		
		ContentValues sue = new ContentValues();
		tim.put("name", "Sue Martin");
		tim.put("phone", "674-342-7788");
		tim.put("facebook", "facebook.com/sue.martin");
		tim.put("twitter", "Unavailable");
		tim.put("google", "Unavailable");
		database.insert("temp_profiles", null, tim);
	}
	
	public void insertVotesSimulation() {
		/*
		 * This method just officially creates simulated vote for test
		 * And will be deleted once a test server is set up
		 * */
		ContentValues values = new ContentValues();
		values.put("name", "More Foam!");
		values.put("max", 10);
		values.put("current", 0);
		database.insert("votes", null, values);
		
		values = new ContentValues();
		values.put("name", "More Fire Breathing");
		values.put("max", 15);
		values.put("current", 0);
		database.insert("votes", null, values);
		
		values = new ContentValues();
		values.put("name", "Next Song To Play");
		database.insert("complicated_votes", null, values);
		
		values = new ContentValues();
		values.put("name", "Next Act on Stage");
		database.insert("complicated_votes", null, values);
		
	}//end of insertVotesSimulation
	
	public void eradicateVotes() {
		database.delete("votes", null, null);
		database.delete("user_votes", null, null);
		database.delete("complicated_votes", null, null);
		database.delete("user_complicated_votes", null, null);
	}//end of eradicate votes
	
	public void incrementCurrentVote(int id, int byAmount){
		/*THis one is really only used for the simulation!*/
		String query = "UPDATE votes SET current = current + "+byAmount+" WHERE _id = "+id;
		database.execSQL(query);
	}//end of incrementCurrentVote
	
	public void userIncrementCurrentVote(int id, int byAmount, int userId) {
		ContentValues values = new ContentValues();
		values.put("v_id", id);
		values.put("u_id", userId);
		database.insert("user_votes", null, values);
		
		
		String query = "UPDATE votes SET current = current + "+byAmount+" WHERE _id = "+id;
		database.execSQL(query);
	}
	
	public boolean getIfUserVoted(String table, int voteId, int userId) {
		
		Cursor result = database.query(table, null, "u_id==? AND v_id==?", new String[]{""+userId, ""+voteId}, null, null, null);
		Log.d("ELECTRICFLURRYDATABASE", "The user voted query returns this num: "+result.getCount());
		if(result.getCount() == 0)
			return false;
		
		return true;
	}//end of getIfUserVoted()
	
	
	
	
	
	
	
	
	
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
			String userCreate = " CREATE TABLE user (_id INTEGER PRIMARY KEY, name text, phone text, facebook text, twitter text, google text) ";
			dBase.execSQL(userCreate);
			
			
			String socialUrlCreate = " CREATE TABLE social_urls (_id INTEGER PRIMARY KEY, type text, url text) ";
			dBase.execSQL(socialUrlCreate);
			
			/*
			 * Voted on is a boolean simply to figure out whether that specific user voted on it or not*/
			String votesCreate = " CREATE TABLE votes (_id INTEGER PRIMARY KEY, name TEXT, max INTEGER, current INTEGER ) ";
			dBase.execSQL(votesCreate);
			
			String userVotesCreate = "CREATE TABLE user_votes (_id INTEGER PRIMARY KEY, u_id INTEGER, v_id INTEGER ) ";
			dBase.execSQL(userVotesCreate);
			
			
			String compVotesCreate = "CREATE TABLE complicated_votes (_id INTEGER PRIMARY KEY, name TEXT ) ";
			dBase.execSQL(compVotesCreate);
			
			String userCompVotesCreate = "CREATE TABLE user_complicated_votes (_id INTEGER PRIMARY KEY, u_id INTEGER, v_id INTEGER) ";
			dBase.execSQL(userCompVotesCreate);
			
			
			String tempProfilesCreate = "CREATE TABLE temp_profiles (_id INTEGER PRIMARY KEY, name text, phone text, facebook text, twitter text, google text)";
			dBase.execSQL(tempProfilesCreate);
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
