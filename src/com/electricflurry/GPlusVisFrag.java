//package com.electricflurry;
//
//import android.app.ProgressDialog;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class GPlusVisFrag extends Fragment {
//
//	Button buttons;
//	Button loginMenu, shareMenu, signIn, signOut, revokeAccess, sharePost, plusOne;
//	
//	
//	public static GPlusVisFrag newInstance() {
//		GPlusVisFrag f = new GPlusVisFrag();
//		return f;
//	}//end of static constructor
//	
//	
//	
//	public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//	}
//	
//	
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
//		
//		
//		View gPlusMenu = inflater.inflate(R.layout.google_plus_menu, container, false);
//		View gPlusLogin = inflater.inflate(R.layout.google_plus_sign_in, container, false);
//		View gPlusShare = inflater.inflate(R.layout.google_plus_share, container, false);
//		
//
//		
//		//Main G+ Menu
//		Button loginMenu = (Button) gPlusMenu.findViewById(R.id.login_menu_button);
//		Button shareMenu = (Button) gPlusMenu.findViewById(R.id.share_menu_button);
//		//Login Menu
//		Button signIn = (Button) gPlusLogin.findViewById(R.id.sign_in_button);
//		Button signOut = (Button) gPlusLogin.findViewById(R.id.sign_out_button);
//		Button revokeAccess = (Button) gPlusLogin.findViewById(R.id.revoke_access_button);
//		//Share or Post Menu
//		Button plusOne = (Button) gPlusShare.findViewById(R.id.plus_one_button);
//		Button sharePost = (Button) gPlusShare.findViewById(R.id.share_button);
//	
//		shareMenu.setOnClickListener(new View.OnClickListener() {	
//			
//			@Override
//		public void onClick(View v) {
//			((MainActivity)getActivity()).addFragment(R.id.fragment_holder, GPlusVisFrag.newInstance());
//			Toast.makeText(getActivity(), "clicked on shareMenu", Toast.LENGTH_SHORT).show();
//		}
//
//	});
//		loginMenu.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//		public void onClick(View v) {
//			((MainActivity)getActivity()).addFragment(R.id.fragment_holder, PlusClientFragment.newInstance());
//			Toast.makeText(getActivity(), "clicked on loginMenu", Toast.LENGTH_SHORT).show();
//		}
//
//	});
//		
//	/*
//	signIn.setOnClickListener(new View.OnClickListener() {
//	
//		@Override
//	public void onClick(View v) {
//		((MainActivity)getActivity()).addFragment(R.id.fragment_holder, GPlusVisFrag.newInstance());
//		Toast.makeText(getActivity(), "clicked on signIn", Toast.LENGTH_SHORT).show();
//	}
//
//});
//*/
//
//	return loginMenu;
//
//	
//}//end of onCreateView
//	
//	
//
//}//end of class
//
