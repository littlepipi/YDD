package net.loonggg.fragment;

import tx.ydd.app.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentDiscoverParent extends Fragment {
	View view = null;
	private FragmentManager fragmentManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_discover_parent, null);

		fragmentManager = getChildFragmentManager();
		FragmentDiscover fragmentDiscover = new FragmentDiscover(getActivity());
		fragmentManager.beginTransaction()
				.replace(R.id.fragment_discover_parent, fragmentDiscover)
				.commit();
		return view;
	}

}
