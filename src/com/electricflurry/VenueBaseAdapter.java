package com.electricflurry;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VenueBaseAdapter extends BaseAdapter{
	
	ArrayList<Venue> list = new ArrayList<Venue>();

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int pos) {
		return list.get(pos);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		if(view == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			view = inflater.inflate(R.layout.venue_list_layout, parent, false);
		}
		TextView name = (TextView)view.findViewById(R.id.venue_name);
		TextView address = (TextView)view.findViewById(R.id.venue_address);
		TextView city = (TextView)view.findViewById(R.id.venue_city);
		TextView state = (TextView)view.findViewById(R.id.venue_state);
		
		Venue v = list.get(pos);
		
		name.setText(v.getLeName()+": ");
		address.setText(v.getLeAddress());
		//the following assumes that foursquare always sends city and state
		city.setText(v.getLeCity());
		state.setText(", "+v.getLeState());
		
		
		return view;
	}//end of getView
	
	
	public void replaceLeList(ArrayList<Venue> list) {
		if(list != null)
			this.list = list;
	}//end of replaceLeList
	
	
	

}//end of class
