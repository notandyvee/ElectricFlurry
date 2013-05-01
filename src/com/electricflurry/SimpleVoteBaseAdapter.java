package com.electricflurry;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
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

public class SimpleVoteBaseAdapter extends BaseAdapter implements ConsumeCursor{
	ArrayList<Vote> votesList = new ArrayList<Vote>();
	ElectricFlurryDatabase database;
	Context context;
	int id;
	
	public SimpleVoteBaseAdapter(Context context, ElectricFlurryDatabase database) {
		this.database = database;
		this.context = context;
	}
	
	
	

	public void setId(int id) {
		this.id = id;
	}//end of setId()
	
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
		upVote.setOnClickListener(null);//just to make sure its null to begin with
		
		Vote v = votesList.get(pos);
		name.setText(v.getName());
		
		numOfVotes.setText( v.limitReached() ? ""+v.getLimit() : v.getCurrentNumVotes()+"");
		
		//just set the tag of the id of the vote you click
		view.setTag(v.getId()+"");
		
		if(v.wasVotedOn() == false && v.limitReached() == false) {
			
			/*THE ONCLICK LISTENER SHOULD BE WRAPPED IN A SEPARATE THREAD BUT SINCE NO NETWORK CALLS ARE MADE NOT DOING THAT*/
			upVote.setOnClickListener(new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
					
					
					Toast.makeText(context, "You just attempted to upvote TAG: "+((View)v.getParent()).getTag(), Toast.LENGTH_LONG).show();
					View parent = ((View)v.getParent());
					
					int id = Integer.parseInt((String)parent.getTag());//this id is the ID of the vote not the user ID
					
					Vote voteClicked = null;
					/*
					 * First get the vote that was clicked*/
					for(Vote leVote : votesList) {
						if(leVote.getId() == id) {
							voteClicked = leVote;
						}
					}//end of foreach
					
					voteClicked.voted();
					SimpleVoteBaseAdapter.this.notifyDataSetChanged();
					
					/*User can see immediate results with the above but I will also query the server again and refresh*/
					database.userIncrementCurrentVote(id, 1, SimpleVoteBaseAdapter.this.id);
					database.leQuery("votes", null, null, null, null, null, null, SimpleVoteBaseAdapter.this);	
					SimpleVoteBaseAdapter.this.notifyDataSetChanged();
					
					
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





	/*
	 * My interface put into here since this class will ultimately be doing all the
	 * work for votes so it makes sense to put it here*/
	@Override
	public void consumeCursor(Cursor cursor) {
		ArrayList<Vote> votesList = new ArrayList<Vote>();
		for(int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			int simpleVId = cursor.getInt(0);//this is the ID of the simple vote
			
			Vote vote = new Vote(simpleVId, cursor.getString(1), cursor.getInt(2), cursor.getInt(3), database.getIfUserVoted("user_votes", simpleVId, this.id) );
			votesList.add(vote);
		}
		
		this.votesList = votesList;
	}
	
	
	
	

	
	
	
	
	

}//end of class
