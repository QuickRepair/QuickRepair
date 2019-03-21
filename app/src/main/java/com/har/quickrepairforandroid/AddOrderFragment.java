package com.har.quickrepairforandroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

public class AddOrderFragment extends Fragment {

	private Spinner mMerchantSpinner;
	private Spinner mApplianceTypeSpinner;
	private Button mSubmitButton;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_add_order, container, false);

		mMerchantSpinner = (Spinner)v.findViewById(R.id.merchantListSpinner);
		mApplianceTypeSpinner = (Spinner)v.findViewById(R.id.applianceTypeSpinner);
		mSubmitButton = (Button)v.findViewById(R.id.submitButton);

		mSubmitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		return v;
	}

	public static AddOrderFragment newInstance() {
		return new AddOrderFragment();
	}
}
