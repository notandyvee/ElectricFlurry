package com.electricflurry;

public class Profile {
	private String name, phoneNumber, facebookURL, twitterURL, googleURL;
	//PROFILE PICTURE
	
	public Profile() {
		this.name = "Unavailable";
		this.phoneNumber = "Unavailable";
		this.facebookURL = "Unavailable";
		this.twitterURL ="Unavailable";
		this.googleURL = "Unavailable";
	}
	
	public Profile(String name, String phone, String facebook, String twitter, String google) {
		this.name = name;
		this.phoneNumber = phone;
		this.facebookURL = facebook;
		this.twitterURL = twitter;
		this.googleURL = google;
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
	
 }
