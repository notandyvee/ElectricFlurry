package com.electricflurry;

public class Profile {
	private String name, phoneNumber, facebookURL, twitterURL, googleURL, foursquareURL;
	//PROFILE PICTURE
	
	public Profile() {
		this.name = "Not Available";
		this.phoneNumber = "Not Available";
		this.facebookURL = "Not Available";
		this.twitterURL ="Not Available";
		this.googleURL = "Not Available";
		this.foursquareURL = "Not Available";
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
