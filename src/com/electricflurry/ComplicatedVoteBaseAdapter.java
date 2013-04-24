package com.electricflurry;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

/*
 * The list it hold currently is just a test for now and will likely be replaced
 * as I am not sure how I want to hold the info*/

public class ComplicatedVoteBaseAdapter extends BaseAdapter implements ConsumeCursor{
	Context context;
	ElectricFlurryDatabase database;
	ArrayList<VotesWrapper> compVotesList = new ArrayList<VotesWrapper>();
	int id;
	
	public ComplicatedVoteBaseAdapter(Context context, ElectricFlurryDatabase database){
		this.context = context;
		this.database = database;
		
	}//end of constructor
	
	public void setId(int id) {
		this.id = id;
	}//end of setId
	
	@Override
	public int getCount() {
		return compVotesList.size();
	}//end of getCount()

	@Override
	public Object getItem(int position) {
		return compVotesList.get(position);
	}// end of getItem()

	@Override
	public long getItemId(int position) {
		return position;
	}//end of getItem()

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if(view == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			view = inflater.inflate(R.layout.complicated_vote, parent, false);
		}
		
		VotesWrapper v = compVotesList.get(position);
		
		//TextView numOfVotes = (TextView) view.findViewById(R.id.complicated_number_of_votes);
		TextView name = (TextView)view.findViewById(R.id.complicated_vote_name);
		name.setOnClickListener(null);
		name.setText(v.name);
		name.setTag(v.id+"");
		
		name.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "You just clicked complicated vote with id: "+v.getTag(), Toast.LENGTH_SHORT).show();
			}
		});
		
		
		
		
		
		
		return view;
	}//end of getView()

	@Override
	public void consumeCursor(Cursor cursor) {
		ArrayList<VotesWrapper> votesList = new ArrayList<VotesWrapper>();
		for(int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			int simpleVId = cursor.getInt(0);//this is the ID of the simple vote
			
			VotesWrapper vote = new VotesWrapper( simpleVId, cursor.getString(1) );
			votesList.add(vote);
		}
		
		this.compVotesList = votesList;
		
	}//end of consumeCursor()
	
	
	public class VotesWrapper {
		int id;
		String name;
		public VotesWrapper(int id, String name) {
			this.id = id;
			this.name = name;
		}
	}//end VotesWrapper
	
	

}//end of class
