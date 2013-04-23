package com.electricflurry;

/*
 * This is a straightforward class that holds vote information
 * */

public class Vote {
	private int id;//this id should be the ID of what it will be in the server, but for now it'll be what it is in SQLite
	private String name;
	private int limit;
	private int currentVotes = 0;
	private boolean votedOn = false;
	
	public Vote (int id, String name, int limit, int current, boolean votedOn) {
		this.id = id;
		this.name = name;
		this.limit = limit;
		this.votedOn = votedOn;
		this.currentVotes = current;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}//end of getName()
	
	public int getCurrentNumVotes() {
		return currentVotes;
	}//end of getCurentNumVotes()
	
	public boolean wasVotedOn() {
		return votedOn;
	}
	/*public void setCurrentVotes(int num){
		currentVotes = currentVotes + num;
	}*/
	
	public void voted() {
		/*This method just increased the currentVote as someone just voted*/
		currentVotes++;
		votedOn = true;
	}//end of voted()
	
	public boolean limitReached() {
		return currentVotes==limit ? true : false;
	}//end of limitReached()
	
	public boolean didUserVote() {
		return votedOn;
	}//end of didUserVote
	

}//end of class
