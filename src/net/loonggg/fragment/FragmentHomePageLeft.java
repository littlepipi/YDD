package net.loonggg.fragment;

import tx.ydd.app.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentHomePageLeft extends Fragment {
	View view = null;
	private FragmentManager fragmentManager;
	String didian = null;
	private String lostkind_from_fragmenthomepage = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_home_page_left, null);
		Bundle bundle = getArguments();
		lostkind_from_fragmenthomepage = bundle
				.getString("lostkind_from_fragmenthomepage");

		

		fragmentManager = getChildFragmentManager();

		FragmentLeftMessage fragmentLeftAll = new FragmentLeftMessage(getActivity());
		fragmentManager
				.beginTransaction()
				.replace(R.id.fragment_home_page_tab_left_page, fragmentLeftAll)
				.commit();
		Bundle bundle_left = new Bundle();
		bundle_left.putString("lostkind_from_fragmenthomepageleft",
				lostkind_from_fragmenthomepage);
		fragmentLeftAll.setArguments(bundle_left);
		return view;
	}

}
