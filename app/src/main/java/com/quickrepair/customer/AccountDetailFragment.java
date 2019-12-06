package com.quickrepair.customer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.quickrepair.customer.Database.Account;
import com.quickrepair.customer.Models.LoginAccount;

public class AccountDetailFragment extends Fragment {

//	private AccountViewModel accountViewModel;

	@org.jetbrains.annotations.NotNull
	@org.jetbrains.annotations.Contract(" -> new")
	public static AccountDetailFragment newInstance() {
		return new AccountDetailFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_account_detail, container, false);

		Button logoutButton = (Button)v.findViewById(R.id.logoutButton);
		final TextView accountTextView = (TextView)v.findViewById(R.id.account_detail_name);
		final TextView passwordTextView = (TextView)v.findViewById(R.id.account_detail_password);
		final TextView accountTypeTextView = (TextView)v.findViewById(R.id.account_detail_type);
		final TextView accountIdTextView = (TextView)v.findViewById(R.id.account_detail_id);

		TextView title = v.findViewById(R.id.page_name);
		title.setText(R.string.title_account_detail);

		/*accountViewModel = ViewModelProviders.of(this, new AccountViewModel.AccountViewModelFactory(
				new AccountRepository(AccountDatabase.getInstance(getContext()).AccountDao())
		)).get(AccountViewModel.class);*/

		accountTextView.setText(LoginAccount.getInstance(getContext()).getAccount().getAccountNumber());
		passwordTextView.setText(LoginAccount.getInstance(getContext()).getAccount().getPassword());
		accountTypeTextView.setText(
				Account.Type.values()[LoginAccount.getInstance(getContext()).getAccount().getAccountType()] == Account.Type.customer
						? R.string.login_type_switch_customer
						: R.string.login_type_switch_merchant
		);
		accountIdTextView.setText(LoginAccount.getInstance(getContext()).getAccount().getId());
		logoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginAccount.getInstance(getContext()).getAccount().setOnline(false);
//				new LogoutTask().execute();
			}
		});

		return v;
	}

	/*private class DetailsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private TextView mItemName;
		private TextView mItemValue;

		public DetailsHolder(LayoutInflater inflater, ViewGroup parent) {
			super(inflater.inflate(R.layout.widget_account_detail_item, parent, false));

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
					value = AccountViewModel.getInstance().getAccount().getAccountNumber();
					break;
				case 1:
					name = R.string.password_string;
					value = AccountViewModel.getInstance().getAccount().getPassword();
					break;
				case 2:
					name = R.string.account_type_string;
					value = getContext().getString(
							AccountViewModel.getInstance().getAccount().getAccountType() == Account.Type.customer ? R.string.login_type_switch_customer : R.string.login_type_switch_merchant
					);
					break;
			}
			holder.bind(name, value);
		}

		@Override
		public int getItemCount() {
			return 3;
		}
	}*/

	// TODO
	/*private class LogoutTask implements AsyncConnection {
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
	}*/
}
