package net.loonggg.fragment;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewFragmentDiscover extends Fragment{
	
	    View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_new_discover, null);
		

		return view;
	}

}
