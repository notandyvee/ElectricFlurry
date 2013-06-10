package com.electricflurry;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

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
	ArrayList<ParentVote> votesList = new ArrayList<ParentVote>();
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
		return votesList.size();
	}//end of getCount()

	@Override
	public Object getItem(int position) {
		return votesList.get(position);
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
		
		ParentVote v = votesList.get(position);
		
		view.setTag(v.id+"");
		//TextView numOfVotes = (TextView) view.findViewById(R.id.complicated_number_of_votes);
		TextView name = (TextView)view.findViewById(R.id.complicated_vote_name);
		name.setOnClickListener(null);
		name.setText(v.name);
		name.setTag(v.id+"");
		
		name.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				
				Toast.makeText(context, "You just clicked complicated vote with server id: "+v.getTag(), Toast.LENGTH_SHORT).show();
				int voteId = Integer.parseInt((String)v.getTag());
				String parentName = (String)((TextView)v).getText();
				((MainActivity)context).popUpDialog(voteId, id, parentName);
				
			}
		});
		
		
		
		
		
		
		return view;
	}//end of getView()

	@Override
	public void consumeCursor(Cursor cursor) {
		/*ArrayList<VotesWrapper> votesList = new ArrayList<VotesWrapper>();
		for(int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			int simpleVId = cursor.getInt(0);
			
			VotesWrapper vote = new VotesWrapper( simpleVId, cursor.getString(1) );
			votesList.add(vote);
		}
		
		this.compVotesList = votesList;*/
		
	}//end of consumeCursor()
	
	
	public class ParentVote {
		int id;
		String name;
		public ParentVote(int id, String name) {
			this.id = id;
			this.name = name;
		}
	}//end VotesWrapper
	
	
	
	/*
	 * This is my method that will take the document AsyncTask 
	 * returns and actually uses it to create the list of votes*/
	public void consumeXml(Document doc) {
		ArrayList<ParentVote> votesList = new ArrayList<ParentVote>();
		
		NodeList idList = doc.getElementsByTagName("id");
		NodeList nameList = doc.getElementsByTagName("name");

		
		for(int i = 0; i < idList.getLength(); i++) {
			int id = Integer.parseInt(idList.item(i).getTextContent());
			ParentVote vote = new ParentVote(id, nameList.item(i).getTextContent());
			votesList.add(vote);
		}
		this.votesList = votesList;
		
	}//end of consumeXml
	
	
	
	
	
	
	
	
	
	
	

}//end of class
