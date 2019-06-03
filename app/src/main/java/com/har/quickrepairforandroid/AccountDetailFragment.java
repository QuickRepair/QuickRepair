package com.har.quickrepairforandroid;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.har.quickrepairforandroid.AsyncTransmissions.AsyncHttpTask;
import com.har.quickrepairforandroid.AsyncTransmissions.HttpConnection;
import com.har.quickrepairforandroid.Models.AccountHolder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class AccountDetailFragment extends Fragment {

	private RecyclerView mRecyclerView;
	private Button mLogoutButton;
	private Handler mMainHandler;

	@org.jetbrains.annotations.NotNull
	@org.jetbrains.annotations.Contract(" -> new")
	public static AccountDetailFragment newInstance() {
		return new AccountDetailFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_account_detail, container, false);

		mMainHandler = new Handler();
		mLogoutButton = (Button)v.findViewById(R.id.logoutButton);
		mRecyclerView = (RecyclerView)v.findViewById(R.id.accountDetailRecyclerView);

		mLogoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AccountHolder.getInstance().setIsLogin(false);
				new LogoutTask().execute();
			}
		});

		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mRecyclerView.setAdapter(new AccountDetailAdapter());

		return v;
	}

	private class DetailsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private TextView mItemName;
		private TextView mItemValue;

		public DetailsHolder(LayoutInflater inflater, ViewGroup parent) {
			super(inflater.inflate(R.layout.account_detail_item, parent, false));

			mItemName = (TextView)itemView.findViewById(R.id.account_detail_item_name);
			mItemValue = (TextView)itemView.findViewById(R.id.account_detail_item_value);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {

		}

		public void bind(int name, String value) {
			mItemName.setText(name);
			mItemValue.setText(value);
		}
	}

	private class AccountDetailAdapter extends RecyclerView.Adapter<DetailsHolder> {
		@NonNull
		@Override
		public DetailsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
			LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
			return new DetailsHolder(layoutInflater, viewGroup);
		}

		@Override
		public void onBindViewHolder(@NonNull DetailsHolder holder, int position) {
			int name = R.string.account_string;
			String value = "";
			switch(position)
			{
				case 0:
					name = R.string.account_string;
					value = AccountHolder.getInstance().getAccount();
					break;
				case 1:
					name = R.string.password_string;
					value = AccountHolder.getInstance().getPassword();
					break;
				case 2:
					name = R.string.account_type_string;
					value = getContext().getString(
							AccountHolder.getInstance().getIsCustomer() ? R.string.login_type_switch_customer : R.string.login_type_switch_merchant
					);
					break;
			}
			holder.bind(name, value);
		}

		@Override
		public int getItemCount() {
			return 3;
		}
	}

	private class LogoutTask implements AsyncHttpTask {
		@Override
		public void execute() {
			HttpConnection.getInstance().postMethod(makeRequest(), this);
		}

		@Override
		public Request makeRequest() {
			return null;
		}

		@Override
		public void handler(Response response) {
			mMainHandler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getContext(), R.string.logout_successed_text, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
