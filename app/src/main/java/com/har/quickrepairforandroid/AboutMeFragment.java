package com.har.quickrepairforandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutMeFragment extends Fragment {

	private ImageView accountProfile;
	private TextView accountTextView;

	public static AboutMeFragment newInstance() {
		return new AboutMeFragment();
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		//TODO
		View v = inflater.inflate(R.layout.about_me_fragment, container, false);

		accountProfile = (ImageView)v.findViewById(R.id.accountImageView);
		accountTextView = (TextView)v.findViewById(R.id.accountTextView);

		if(AccountHolder.getInstance().getIsLogin())
			accountTextView.setText(AccountHolder.getInstance().getAccount());
		else
			accountTextView.setText(R.string.have_not_login);

		accountTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!AccountHolder.getInstance().getIsLogin()) {
					Intent intent = LoginActivity.newIntent(getContext());
					startActivity(intent);
				}
			}
		});
		return v;
	}
}
