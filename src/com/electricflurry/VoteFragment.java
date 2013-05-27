package com.electricflurry;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VoteFragment extends Fragment implements ConsumeCursor{
	
	ElectricFlurryDatabase database;
	SimpleVoteBaseAdapter adapter;
	ComplicatedVoteBaseAdapter compAdapter;
	ListView simpleVotes;
	ListView complicatedVotes;
	//used soley to count how many times the simulation has ran
	int simulationCounter = 0;
	
	
	
	
	public static VoteFragment newInstance() {
		/*
		 * If you need anything to be sent in for the Fragment to initialize itself
		 * just sent it in as a paramter*/
		VoteFragment f = new VoteFragment();
		
		return f;
	}//end of static constructor
	
	
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * For simulation purposes I will add some dummy vote data
		 * here in order to show voting in action using SQLite
		 * as an example for querying the server*/
		database = new ElectricFlurryDatabase(getActivity());
		database.insertVotesSimulation();
		
		adapter = new SimpleVoteBaseAdapter(getActivity(), database, true);
		compAdapter = new ComplicatedVoteBaseAdapter(getActivity(), database);
		
		/**
		 * Getting user ID from database but ideally wanna have the user already be signed up to our server
		 * so I can just get the local copy of there ID to make things much smoother.
		 * Maybe have that happen at user creation or checkin
		 */
		database.leQuery("user", new String[] {"server_id"}, null, null, null, null, null, this);//this gets called just to get the user ID thats set on the server but ideally wanna have it set locally as well for speed
		
		
	}//end of onCreate
	
	
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		/*
		 * This is where your fragment's view is being created so do the work to make it do stuff*/
		View view = inflater.inflate(R.layout.vote_fragment, container, false);
		
		TextView refresh = (TextView)view.findViewById(R.id.refresh);
		refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				VoteFragment.this.refreshVotes();	
			}
		});
		
		simpleVotes = (ListView)view.findViewById(R.id.simple_votes_list);
		simpleVotes.setAdapter(adapter);
		
		complicatedVotes = (ListView)view.findViewById(R.id.complicated_votes_list);
		complicatedVotes.setAdapter(compAdapter);
		
		//Download simple votes
		new DownloadSimpleVotes().execute("http://ec2-54-214-95-164.us-west-2.compute.amazonaws.com:8080/electricflurry/resources/votes/"+adapter.id, adapter);
		
		//Download complicated votes parent list first
		new DownloadSimpleVotes().execute("http://ec2-54-214-95-164.us-west-2.compute.amazonaws.com:8080/electricflurry/resources/parentvotes", compAdapter);
		
		return view;
	}//end of onCreateView

	
	
	
	public void onDestroy() {
		/*just do a bit of cleaning up as I don't want the votes to persist for this simulation*/
		super.onDestroy();
		
	}//end of onDestroy
	
	





	@Override
	public void consumeCursor(Cursor cursor) {
		/*
		 * THis exists simply to get the User ID from local storage for now
		 * but will change when the other parts of the app are better integrated*/
		cursor.moveToFirst();//there should only ever be one
		if(cursor.getCount() < 1) {
			Toast.makeText(getActivity(), "You must create a user first", Toast.LENGTH_SHORT).show();
			getActivity().getSupportFragmentManager().popBackStack();
		} else {
			int id = cursor.getInt(0);
			adapter.setId(id, "http://ec2-54-214-95-164.us-west-2.compute.amazonaws.com:8080/electricflurry/resources/votes/");
			compAdapter.setId(id);
		}
		
	}
	
	public void refreshVotes() {
		//Download simple votes
		new DownloadSimpleVotes().execute("http://ec2-54-214-95-164.us-west-2.compute.amazonaws.com:8080/electricflurry/resources/votes/"+adapter.id, adapter);
		
		//Download complicated votes parent list first
		new DownloadSimpleVotes().execute("http://ec2-54-214-95-164.us-west-2.compute.amazonaws.com:8080/electricflurry/resources/parentvotes", compAdapter);
	}//end of refreshVotes

	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}//end of class
