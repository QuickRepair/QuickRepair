package com.har.quickrepairforandroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class WaitLoadingFragment extends DialogFragment {

	private ProgressBar mProgressBar;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_wait_loading, container, false);

		mProgressBar = (ProgressBar)v.findViewById(R.id.wait_loading);

		return v;
	}

	public static WaitLoadingFragment newInstance() {
		return new WaitLoadingFragment();
	}
}
