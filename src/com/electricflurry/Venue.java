package com.electricflurry;

public class Venue {
	String name, address, city, state;
	String type;//used to know whether its food or drinks
	
	public void putName(String name) {
		this.name = name;
	}//end of putName
	
	public void putLocation(String address, String city, String state) {
		this.address = address;
		this.city = city;
		this.state = state;
	}//end of putLocation
	
	
	/*Setters*/
	public void putAddress(String address) {
		this.address = address;
	}
	
	public void putCity(String city) {
		this.city = city;
	}
	
	public void putState(String state) {
		this.state = state;
	}
	
	
	/*getters*/
	public String getLeName(){
		return name;
	}
	
	public String getLeAddress(){
		return address;
	}
	
	public String getLeCity(){
		return city;
	}
	
	public String getLeState(){
		return state;
	}

}
