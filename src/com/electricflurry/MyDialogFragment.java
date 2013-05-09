package com.electricflurry;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class MyDialogFragment extends DialogFragment{
	SimpleVoteBaseAdapter adapter;
	ListView leList;
	static int voteId;
	static int userId;
	
	static MyDialogFragment newInstance(int vId, int uId) {
		voteId = vId;
		userId = uId;
        MyDialogFragment f = new MyDialogFragment();

        return f;
    }
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SimpleVoteBaseAdapter(getActivity(), null);
        adapter.setId(userId, "http://ec2-54-244-101-0.us-west-2.compute.amazonaws.com:8080/electricflurry/resources/childvotes/");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog, container, false);
        leList = (ListView)v.findViewById(R.id.child_list_votes);
        leList.setAdapter(adapter);
        
        new DownloadSimpleVotes().execute("http://ec2-54-244-101-0.us-west-2.compute.amazonaws.com:8080/electricflurry/resources/childvotes/"+voteId+"/"+userId, adapter);
        

        return v;
    }//end of onCreateView()
	
	
	
	
	
	
	

}//end of class
