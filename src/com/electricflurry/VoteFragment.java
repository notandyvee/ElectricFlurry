package com.electricflurry;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VoteFragment extends Fragment {
	
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
		
		SharedPreferences settings = getActivity().getSharedPreferences("profile", 0);
		if(settings.getInt("server_id", 0) == 0){
			Toast.makeText(getActivity(), "You must create a user first", Toast.LENGTH_SHORT).show();
			getActivity().getSupportFragmentManager().popBackStack();
		} else {
			int id = settings.getInt("server_id", 0);
			adapter.setId(id, ServerConstants.PUBLIC_DNS+"resources/votes/"+id);
			compAdapter.setId(id);
		}
		

		
		
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
		new DownloadSimpleVotes().execute(ServerConstants.PUBLIC_DNS + "resources/votes/"+adapter.id, adapter);
		
		//Download complicated votes parent list first
		new DownloadSimpleVotes().execute(ServerConstants.PUBLIC_DNS + "resources/parentvotes", compAdapter);
		
		return view;
	}//end of onCreateView

	
	
	
	public void onDestroy() {
		/*just do a bit of cleaning up as I don't want the votes to persist for this simulation*/
		super.onDestroy();
		
	}//end of onDestroy
	
	


	
	public void refreshVotes() {
		//Download simple votes
		new DownloadSimpleVotes().execute(ServerConstants.PUBLIC_DNS + "resources/votes/"+adapter.id, adapter);
		
		//Download complicated votes parent list first
		new DownloadSimpleVotes().execute(ServerConstants.PUBLIC_DNS + "resources/parentvotes", compAdapter);
	}//end of refreshVotes

	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}//end of class
