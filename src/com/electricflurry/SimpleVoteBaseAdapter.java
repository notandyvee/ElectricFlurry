package com.electricflurry;

import java.util.ArrayList;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

/*
 * BaseAdapter that just has a list of simple votes
 * Users can just click on upvote and that will be there vote.
 * */

public class SimpleVoteBaseAdapter extends BaseAdapter{
	ArrayList<Vote> votesList = new ArrayList<Vote>();
	ElectricFlurryDatabase database;
	
	public SimpleVoteBaseAdapter(ElectricFlurryDatabase database) {
		this.database = database;
	}
	
	public void addVote(Vote vote) {
		votesList.add(vote);
	}//end of addVote
	


	@Override
	public int getCount() {
		return votesList.size();
	}//end of getCount()

	@Override
	public Object getItem(int position) {
		return votesList.get(position);
	}//end of getItem

	@Override
	public long getItemId(int position) {
		return position;
	}//end of getItemId

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		if(view == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			view = inflater.inflate(R.layout.simple_vote, parent, false);
		}
		TextView name = (TextView)view.findViewById(R.id.vote_name);
		TextView numOfVotes = (TextView)view.findViewById(R.id.number_of_votes);
		TextView upVote = (TextView)view.findViewById(R.id.upvote);
		
		Vote v = votesList.get(pos);
		name.setText(v.getName());
		numOfVotes.setText(v.getCurrentNumVotes()+"");
		
		//just set the tag of the id of the vote you click
		view.setTag(v.getId()+"");
		
		view.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Log.d("SimpleBaseAdapter", "TAG of current simple vote you clicked is: "+v.getTag());
			}
		});
		
		
		
		
		
		
		
		
		
		
		
		return view;
	}//end of getView
	
	
	
	
	
	
	
	
	

}//end of class
