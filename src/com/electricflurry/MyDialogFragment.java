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
	static String parentName;
	
	static MyDialogFragment newInstance(int vId, int uId, String leParentName) {
		voteId = vId;
		userId = uId;
		parentName = leParentName;
        MyDialogFragment f = new MyDialogFragment();

        return f;
    }
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SimpleVoteBaseAdapter(getActivity(), null, false);
        adapter.setId(userId, ServerConstants.PUBLIC_DNS + "resources/childvotes/"+voteId+"/"+userId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	getDialog().setTitle(parentName);
        View v = inflater.inflate(R.layout.fragment_dialog, container, false);
        leList = (ListView)v.findViewById(R.id.child_list_votes);
        leList.setAdapter(adapter);
        
        new DownloadSimpleVotes().execute(ServerConstants.PUBLIC_DNS + "resources/childvotes/"+voteId+"/"+userId, adapter);
        

        return v;
    }//end of onCreateView()
	
	
	
	
	
	
	

}//end of class
