package com.electricflurry;

public class Profile {
	private String name, phoneNumber, facebookURL, twitterURL, googleURL, foursquareURL;
	int serverId;
	//PROFILE PICTURE
	
	public Profile() {
		this.name = "Unavailable";
		this.phoneNumber = "Unavailable";
		this.facebookURL = "Unavailable";
		this.twitterURL ="Unavailable";
		this.googleURL = "Unavailable";
		this.foursquareURL = "Unavailable";
	}
	
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	
	public int getServerId() {
		return this.serverId;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getFacebookURL() {
		return this.facebookURL;
	}

	public void setFacebookURL(String facebookURL) {
		this.facebookURL = facebookURL;
	}
	
	public String getTwitterURL() {
		return this.twitterURL;
	}
	public void setTwitterURL(String twitterURL) {
		this.twitterURL = twitterURL;
	}
	
	public String getGoogleURL() {
		return this.googleURL;
	}
	
	public void setGoogleURL(String googleURL) {
		this.googleURL = googleURL;
	}
	
	public String getFoursquareURL() {
		return this.foursquareURL;
	}
	public void setFoursquareURL(String foursquareURL) {
		this.foursquareURL = foursquareURL;
	}
	
 }
