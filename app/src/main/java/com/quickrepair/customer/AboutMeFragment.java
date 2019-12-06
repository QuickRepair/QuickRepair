package com.quickrepair.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.quickrepair.customer.Models.LoginAccount;

public class AboutMeFragment extends Fragment {

	private ImageView accountProfile;
	private TextView accountTextView;
	private GridLayout mAccountLayout;

//	private AccountViewModel accountViewModel;

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
		View v = inflater.inflate(R.layout.fragment_about_me, container, false);

		accountProfile = v.findViewById(R.id.accountImageView);
		accountTextView = v.findViewById(R.id.accountTextView);
		mAccountLayout = v.findViewById(R.id.aboutMeAccountGridLayout);

		accountTextView.setText(R.string.have_not_login);
		/*accountViewModel = ViewModelProviders.of(this, new AccountViewModel.AccountViewModelFactory(
				new AccountRepository(AccountDatabase.getInstance(getContext()).AccountDao())
		)).get(AccountViewModel.class);*/

		if (LoginAccount.getInstance(getContext()).getAccount().isOnline())
			accountTextView.setText(LoginAccount.getInstance(getContext()).getAccount().getAccountNumber());

		mAccountLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (LoginAccount.getInstance(getContext()).getAccount().isOnline()) {
					Intent intent = LoginActivity.newIntent(getContext());
					startActivity(intent);
				} else {
					Intent intent = AccountActivity.newIntent(getContext());
					startActivity(intent);
				}
			}
		});
		return v;
	}
}
