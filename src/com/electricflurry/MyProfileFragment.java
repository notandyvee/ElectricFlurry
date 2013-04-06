package com.electricflurry;

import com.electricflurry.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MyProfileFragment extends Fragment{
	EditText edit_name, edit_phone;
	TextView disp_name, disp_phone;
	
	//created the myprofile fragment by just copying the mingle fragment -sean

	public static MyProfileFragment newInstance() {

		MyProfileFragment f = new MyProfileFragment();
		
		return f;
	}//end of static constructor
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.myprofile_fragment, container, false);
		edit_name = (EditText) view.findViewById(R.id.edit_name);
		edit_phone = (EditText) view.findViewById(R.id.edit_phone);
		disp_name = (TextView) view.findViewById(R.id.disp_name);
		disp_phone = (TextView) view.findViewById(R.id.disp_phone);
		Button save = (Button) view.findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {
			//save button -sean
			@Override
			public void onClick(View v) {
				String name = edit_name.getText().toString();
				String phone = edit_phone.getText().toString();
				disp_name.setText(name);
				disp_phone.setText(phone);
			}
		});
		return view;
	}//end of onCreateView
	
	
	
	
	
}
