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

public class MingleAdapter extends BaseAdapter implements ConsumeCursor{
	ArrayList<Profile> mingleList = new ArrayList<Profile>();
	ElectricFlurryDatabase database;
	Context context;
	int id;
	
	
	
	
	public MingleAdapter(Context context, ElectricFlurryDatabase database) {
		this.database = database;
		this.context = context;
	}

	@Override
	public int getCount() {
		return mingleList.size();
	}

	@Override
	public Object getItem(int index) {
		return mingleList.get(index);
	}

	@Override
	public long getItemId(int index) {
		// TODO Auto-generated method stub
		return index;
		
		
	}

	@Override
	public View getView(int index, View view, ViewGroup parent) {
		if(view == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			view = inflater.inflate(R.layout.mingle_view, parent, false);
		}
		TextView name = (TextView) view.findViewById(R.id.mingle_name);		
		database.leQuery("temp_profiles",new String[] {"name", "phone", "facebook", "twitter", "google"}, null, null, null, null, null, MingleAdapter.this);
		Profile p = mingleList.get(index);
		name.setText(p.getName());
		
		return null;
	}
	
	
	
	@Override
	public void consumeCursor(Cursor cursor) {
		ArrayList<Profile> mingleList = new ArrayList<Profile>();
		for(int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			Profile profile = new Profile(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
			mingleList.add(profile);
		}
		
		this.mingleList = mingleList;
		
	}
}
