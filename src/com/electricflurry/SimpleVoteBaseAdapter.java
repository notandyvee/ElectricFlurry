package com.electricflurry;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
//import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;

/*
 * BaseAdapter that just has a list of simple votes
 * Users can just click on upvote and that will be there vote.
 * */

public class SimpleVoteBaseAdapter extends BaseAdapter implements ConsumeCursor{
	ArrayList<Vote> votesList = new ArrayList<Vote>();
	ElectricFlurryDatabase database;
	Context context;
	int id; //this is the ID of a user!
	String refreshUrl;
	/*
	 * This boolean is gonna be used so I can tell the difference between when I call this
	 * for a single simple vote or when I am showing a list that you can only vote on one
	 * */
	boolean votingOnOne = true;
	
	public SimpleVoteBaseAdapter(Context context, ElectricFlurryDatabase database, boolean votingOnOne) {
		this.database = database;
		this.context = context;
		this.votingOnOne = votingOnOne;
	}
	
	
	

	public void setId(int id, String semiUrl) {
		this.id = id;
		refreshUrl = semiUrl+id;
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
			
			upVote.setOnClickListener(singleVoteListener);
			
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
	
	/*
	 * This is my method that will take the document AsyncTask 
	 * returns and actually uses it to create the list of votes*/
	public void consumeXml(Document doc) {
		ArrayList<Vote> votesList = new ArrayList<Vote>();
		
		NodeList idList = doc.getElementsByTagName("id");
		NodeList nameList = doc.getElementsByTagName("name");
		NodeList maxList = doc.getElementsByTagName("max");
		NodeList currentList = doc.getElementsByTagName("current");
		NodeList votedOnList = doc.getElementsByTagName("votedOn");
		
		for(int i = 0; i < idList.getLength(); i++) {
			int id = Integer.parseInt(idList.item(i).getTextContent());
			int limit = Integer.parseInt(maxList.item(i).getTextContent());
			int current = Integer.parseInt(currentList.item(i).getTextContent());
			int votedOn = Integer.parseInt(votedOnList.item(i).getTextContent());
			Vote vote = new Vote(id, nameList.item(i).getTextContent(), limit, current, votedOn==0?false:true);
			votesList.add(vote);
		}
		this.votesList = votesList;
		
	}//end of consumeXml
	
	
	private class UpVote extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			String url = params[0];
			
			HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost(url);
		    
		    try{
		    	HttpResponse response = httpclient.execute(httppost);
		    }
		    catch(ClientProtocolException e){
		    	return false;
		    }
		    catch (IOException e) {
		    	return false;
		    }
			
			
			return true;
		}
		
		protected void onPostExecute(Boolean result) {
			Log.d("UpVote", "onPostExecute is running!!!!");
			if(result) {
				Toast.makeText(context, "Successfully uploaded your upvote", Toast.LENGTH_SHORT).show();
				for(Vote leVote : votesList){
					leVote.changeVotedOn(true);
				}
				SimpleVoteBaseAdapter.this.notifyDataSetChanged();
			}
			else
				Toast.makeText(context, "Failed to upload your upvote. Try Again Please.", Toast.LENGTH_SHORT).show();
	     }
		
	}//end of DownloadSimpleVotes
	
	
	
	/*
	 * The following are my two listeners that get used depending on what
	 * the value of votingOnOne is
	 * */
	
	View.OnClickListener singleVoteListener = new View.OnClickListener() {			
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
				
				String url = 
					"http://ec2-54-214-95-164.us-west-2.compute.amazonaws.com:8080/electricflurry/resources/upvote/"+id+"/"+SimpleVoteBaseAdapter.this.id;
				
				new UpVote().execute(url);
				/*User can see immediate results with the above but I will also query the server again and refresh*/
				
				new DownloadSimpleVotes().execute(
						refreshUrl, 
						SimpleVoteBaseAdapter.this);
				
				//setting onClickListener to null to not allow user to vote once they have voted
				v.setOnClickListener(null);
				
			
		}//end of onClick method
	};
	

	
	
	

	
	
	
	
	

}//end of class
