package com.electricflurry;

import java.util.ArrayList;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MingleFragment extends Fragment implements ConsumeCursor{
	ElectricFlurryDatabase database;
	ListView mingle_list;
	ArrayAdapter<String> adapter;
	ArrayList<String> mingleList = new ArrayList<String>();
	
	public static MingleFragment newInstance() {
		
		MingleFragment f = new MingleFragment();
		
		return f;
	}//end of static constructor
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mingle_fragment, container, false);
		database = new ElectricFlurryDatabase(getActivity());
		// 	database.insertTempProfiles();
		database.leQuery("temp_profiles", new String[] {"name", "phone", "facebook", "twitter", "google"}, null, null, null, null, null, this);
		adapter = new ArrayAdapter<String>(getActivity(), R.layout.mingle_view, R.id.mingle_name, mingleList);
		mingle_list = (ListView) view.findViewById(R.id.mingle_list);
		mingle_list.setAdapter(adapter);
		
		
		
		return view;
	}//end of onCreateView


	@Override
	public void consumeCursor(Cursor cursor) {
		
		if (cursor.getCount() < 1) {
			database.insertTempProfiles();
		}
		ArrayList<String> mingleList = new ArrayList<String>();
		for(int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			Profile profile = new Profile(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
			mingleList.add(profile.getName());
		}
		
		this.mingleList = mingleList;
		
		
	}
	
	
	
	

}//end of class
