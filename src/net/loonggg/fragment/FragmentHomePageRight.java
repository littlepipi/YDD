package net.loonggg.fragment;

import tx.ydd.app.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentHomePageRight extends Fragment {
	View view = null;
	private FragmentManager fragmentManager;
	private String foundkind_from_fragmenthomepage = "Ыљга";
	String didian = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_home_page_right, null);
		Bundle bundle = getArguments();
		foundkind_from_fragmenthomepage = bundle
				.getString("foundkind_from_fragmenthomepage");

		
		fragmentManager = getChildFragmentManager();
		FragmentRightMessage fragmentRightAll = new FragmentRightMessage(getActivity());
		fragmentManager
				.beginTransaction()
				.replace(R.id.fragment_home_page_tab_right_page,
						fragmentRightAll).commit();
		Bundle bundle_right = new Bundle();
		bundle_right.putString("lostkind_from_fragmenthomepageright",
				foundkind_from_fragmenthomepage);

		fragmentRightAll.setArguments(bundle_right);
		return view;
	}

}
