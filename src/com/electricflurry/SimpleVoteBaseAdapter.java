package com.electricflurry;

import java.util.ArrayList;

import android.content.Context;
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
	Context context;
	
	public SimpleVoteBaseAdapter(Context context, ElectricFlurryDatabase database) {
		this.database = database;
		this.context = context;
	}
	
	
	


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
		
		if(v.wasVotedOn() == false) {
			
			upVote.setOnClickListener(new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
					Toast.makeText(context, "You just attempted to upvote TAG: "+((View)v.getParent()).getTag(), Toast.LENGTH_LONG).show();
					View parent = ((View)v.getParent());
					int id = Integer.parseInt((String)parent.getTag());
					database.userIncrementCurrentVote(id, 1);
					
					for(Vote leVote : votesList) {
						if(leVote.getId() == id) {
							leVote.voted();
							SimpleVoteBaseAdapter.this.notifyDataSetChanged();
							
						}
							
					}//end of foreach
					
					//setting onClickListener to null to not allow user to vote once they have voted
					v.setOnClickListener(null);
					
				}//end of onClick method
			});
			
		}//end of check if the vote was voted on
		
		
		
		
		
		
		
		
		
		
		return view;
	}//end of getView
	
	
	
	
	public void replaceList(ArrayList<Vote> list) {
		votesList = list;
	}
	
	
	
	

	
	
	
	
	

}//end of class
