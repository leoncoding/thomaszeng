package com.thomas.path.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class BaseFragment extends Fragment {
	public FragmentActivity fa;
	public FragmentManager fm;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		fa = (FragmentActivity) activity;
		fm = getFragmentManager();
		super.onAttach(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	protected void findViews() {
		
	}

	protected void init() {
		
	}

	protected void setListener() {
		
	}
}
