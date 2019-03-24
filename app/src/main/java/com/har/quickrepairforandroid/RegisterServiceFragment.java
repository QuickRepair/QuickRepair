package com.har.quickrepairforandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class RegisterServiceFragment extends Fragment {

	private Spinner mDistanceSpinner;
	private EditText mSupportEdtiText;
	private EditText mNotSupportEditText;
	private Button mSubmitButton;

	public static RegisterServiceFragment newInstance() {
		return new RegisterServiceFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_register_service, container, false);

		mDistanceSpinner = (Spinner)v.findViewById(R.id.distanceSpinner);
		mSupportEdtiText = (EditText)v.findViewById(R.id.supportAppliancesEditText);
		mNotSupportEditText = (EditText)v.findViewById(R.id.notSupportAppliancesEditText);
		mSubmitButton = (Button)v.findViewById(R.id.submit_register_service);

		return v;
	}
}
