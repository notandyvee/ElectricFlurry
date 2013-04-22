package com.electricflurry;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
	Timer timer;
	ListView simpleVotes;
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
		
		adapter = new SimpleVoteBaseAdapter(getActivity(), database);
		
		
	}//end of onCreate
	
	
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		/*
		 * This is where your fragment's view is being created so do the work to make it do stuff*/
		View view = inflater.inflate(R.layout.vote_fragment, container, false);
		
		simpleVotes = (ListView)view.findViewById(R.id.simple_votes_list);
		simpleVotes.setAdapter(adapter);
		
		
		//set a timer here to fire things off 10 seconds after View is created
		
		fireSimulation();
		
		
		
		
		return view;
	}//end of onCreateView

	@Override
	public void consumeCursor(Cursor cursor) {
		ArrayList<Vote> votesList = new ArrayList<Vote>();
		for(int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			Vote vote = new Vote(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), (cursor.getInt(4)==0)?false:true );
			votesList.add(vote);
		}
		adapter.replaceList(votesList);
		
	}//end of consumeCursor
	
	
	public void onDestroy() {
		/*just do a bit of cleaning up as I don't want the votes to persist for this simulation*/
		super.onDestroy();
		database.eradicateVotes();
		timer.cancel();
		database.closeDbase();
	}//end of onDestroy
	
	
	
	
	
	
	
	
	/*
	 * I will put my listener stuff here to clean up all my code
	 * above but to also have the advantages of defining them in the same class
	 * */
	public void fireSimulation() {
		
		
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				
			database.leQuery("votes", null, null, null, null, null, null, VoteFragment.this);	
			
			simpleVotes.post(new Runnable() {
				@Override
				public void run() {
					adapter.notifyDataSetChanged();
				}
			});
			
			timer.scheduleAtFixedRate(repeatingtask, 10000, 10000);	
				
				
				
			}//end of run method
			
		}, 10000);
		
		
		
		
		
	}//end of fireSimulation()
	
	
	TimerTask repeatingtask = new TimerTask() {

		@Override
		public void run() {
			if(simulationCounter < 2) {
				database.incrementCurrentVote(1, 5);
				database.incrementCurrentVote(2, 3);
				database.leQuery("votes", null, null, null, null, null, null, VoteFragment.this);
				
				simpleVotes.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(VoteFragment.this.getActivity(), "Repeating at cycle: "+simulationCounter, Toast.LENGTH_SHORT).show();
						simulationCounter++;
						adapter.notifyDataSetChanged();
					}
				});
				
				
				
			} 
			else 
				if(simulationCounter == 2){
					timer.cancel();
				}
		}//end of outer run method
		
	};
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}//end of class
