package com.har.quickrepairforandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.har.quickrepairforandroid.Models.AccountHolder;

public class AboutMeFragment extends Fragment {

	private ImageView accountProfile;
	private TextView accountTextView;
	private RecyclerView mAboutMeRecyclerView;

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
		View v = inflater.inflate(R.layout.about_me_fragment, container, false);

		accountProfile = (ImageView)v.findViewById(R.id.accountImageView);
		accountTextView = (TextView)v.findViewById(R.id.accountTextView);
		mAboutMeRecyclerView = (RecyclerView)v.findViewById(R.id.about_me_recycler_view);
		mAboutMeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		if(AccountHolder.getInstance().getIsLogin()) {                      //already login
			accountTextView.setText(AccountHolder.getInstance().getAccount());
			if(AccountHolder.getInstance().getIsCustomer())
				mAboutMeRecyclerView.setAdapter(new CustomerAboutMeAdapter());
			else
				mAboutMeRecyclerView.setAdapter(new MerchantAboutMeAdapter());
		} else {                                                              //did not login
			accountTextView.setText(R.string.have_not_login);
			accountTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!AccountHolder.getInstance().getIsLogin()) {
						Intent intent = LoginActivity.newIntent(getContext());
						startActivity(intent);
					}
				}
			});
		}
		return v;
	}

	@Override
	public void onResume() {
		if(AccountHolder.getInstance().getIsLogin())
			accountTextView.setText(AccountHolder.getInstance().getAccount());
		else
			accountTextView.setText(R.string.have_not_login);

		if(AccountHolder.getInstance().getIsLogin() && mAboutMeRecyclerView.getAdapter() == null) {
			if (AccountHolder.getInstance().getIsCustomer())
				mAboutMeRecyclerView.setAdapter(new CustomerAboutMeAdapter());
			else
				mAboutMeRecyclerView.setAdapter(new MerchantAboutMeAdapter());
		}

		super.onResume();
	}

	private class AboutMeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private TextView mTextView;

		public AboutMeHolder(LayoutInflater inflater, ViewGroup parent) {
			super(inflater.inflate(R.layout.about_me_list_item, parent, false));
			mTextView = (TextView)itemView.findViewById(R.id.aboutMeListTextView);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			String textString = mTextView.getText().toString();
			Intent intent = null;
			if(textString.equalsIgnoreCase(getContext().getString(R.string.about_me_register_service)))
				intent = RegisterServiceActivity.newIntent(getContext());

			if(intent != null)
				startActivity(intent);
		}

		public void bind(int textId) {
			mTextView.setText(textId);
		}
	}

	private class CustomerAboutMeAdapter extends RecyclerView.Adapter<AboutMeHolder> {
		@NonNull
		@Override
		public AboutMeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
			LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
			return new AboutMeHolder(layoutInflater, viewGroup);
		}

		@Override
		public void onBindViewHolder(@NonNull AboutMeHolder aboutMeHolder, int i) {

		}

		@Override
		public int getItemCount() {
			return 0;
		}
	}

	private class MerchantAboutMeAdapter extends RecyclerView.Adapter<AboutMeHolder> {
		@NonNull
		@Override
		public AboutMeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
			LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
			return new AboutMeHolder(layoutInflater, viewGroup);
		}

		@Override
		public void onBindViewHolder(@NonNull AboutMeHolder aboutMeHolder, int i) {
			switch (i) {
				case 0:
					aboutMeHolder.bind(R.string.about_me_register_service);
			}
		}

		@Override
		public int getItemCount() {
			return 1;
		}
	}
}
