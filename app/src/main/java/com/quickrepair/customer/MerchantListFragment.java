package com.quickrepair.customer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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

import com.quickrepair.customer.Database.Account;
import com.quickrepair.customer.Database.AccountDatabase;
import com.quickrepair.customer.Models.AccountRepository;
import com.quickrepair.customer.Models.AccountViewModel;

import java.util.ArrayList;
import java.util.List;

public class MerchantListFragment extends Fragment {

	private AccountViewModel accountViewModel;

	private RecyclerView mMerchantListView;
	private MerchantAdapter mMerchantAdapter;

	public static MerchantListFragment newInstance() {
		return new MerchantListFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_merchant_list, container, false);
		mMerchantListView = v.findViewById(R.id.merchantListRecyclerView);
		TextView title = v.findViewById(R.id.page_name);
		title.setText(R.string.merchant_list_name);
		mMerchantListView.setLayoutManager(new LinearLayoutManager(getContext()));

		accountViewModel = ViewModelProviders.of(
				this, new AccountViewModel.AccountViewModelFactory(
						new AccountRepository(AccountDatabase.getInstance(getContext()).AccountDao())
				)
		).get(AccountViewModel.class);

		mMerchantAdapter = new MerchantAdapter();
		mMerchantListView.setAdapter(mMerchantAdapter);

//		new AsyncConnection().getMerchantList(0, 0, new GetMerchantListListener());
		return v;
	}

	private class MerchantHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private Account.Merchant mMerchant;

		private ImageView mMerchantProfile;
		private TextView mMerchantName;

		public MerchantHolder(LayoutInflater inflater, ViewGroup parent) {
			super(inflater.inflate(R.layout.widget_merchant_list_item, parent, false));

			itemView.setOnClickListener(this);
			mMerchantProfile = itemView.findViewById(R.id.merchantListProfileImageView);
			mMerchantName = itemView.findViewById(R.id.merchantListNameTextView);
		}

		@Override
		public void onClick(View v) {
			((MerchantActivity)getActivity()).createMerchantDetailFragment(mMerchant);
		}

		void bind(Account.Merchant merchant) {
			mMerchant = merchant;
			mMerchantName.setText(merchant.getId());
		}
	}

	private class MerchantAdapter extends RecyclerView.Adapter<MerchantHolder> {

		private List<Account.Merchant> mMerchants;

		public MerchantAdapter() {
			mMerchants = new ArrayList<>();
			accountViewModel.getMerchantList().observe(MerchantListFragment.this, new Observer<List<Account.Merchant>>() {
				@Override
				public void onChanged(@Nullable List<Account.Merchant> merchants) {
					if (merchants != null) {
						mMerchants = merchants;
						mMerchantAdapter.notifyDataSetChanged();
					}
				}
			});
		}

		@NonNull
		@Override
		public MerchantHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
			LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
			return new MerchantHolder(layoutInflater, viewGroup);
		}

		@Override
		public void onBindViewHolder(@NonNull MerchantHolder merchantHolder, int i) {
			Account.Merchant m = mMerchants.get(i);
			merchantHolder.bind(m);
		}

		@Override
		public int getItemCount() {
			return mMerchants.size();
		}
	}

	/*private class GetMerchantListListener implements AsyncConnection.onResponseListener {
		@Override
		public void onResponse(final Response response) {
			try {
				JSONObject jsonObject = new JSONObject(response.body().string());
				final JSONArray merchants = jsonObject.getJSONArray("merchants");
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						try {
							for (int i = 0; i != merchants.length(); ++i) {
								JSONObject item = merchants.getJSONObject(i);
								Account.Merchant m = new Account.Merchant();
								m.setId(item.getString("id"));
								m.setName(item.getString("name"));
								mMerchants.add(m);
							}
							mMerchantAdapter = new MerchantAdapter(mMerchants);
							mMerchantListView.setAdapter(mMerchantAdapter);
						} catch (JSONException je) {

						}
					}
				});
			} catch (IOException ioe) {

			} catch (JSONException je) {

			}
		}*/

		/*private List<Map<String, Object>> getMerchantList(String json) {
			List<Map<String, Object>> merchantList = new ArrayList<Map<String, Object>>();
			try {
				JSONObject jsonObject = new JSONObject(json);
				JSONArray jsonArray = jsonObject.getJSONArray("merchants");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject item = jsonArray.getJSONObject(i);
					Merchant m = new Merchant();
					m.setId(item.getString("id"));
					m.setName(item.getString("name"));
					mMerchantList.add(m);
				}

				for (Merchant merchant : mMerchantList) {
					Map<String, Object> m = new HashMap<>();
					m.put("merchant", merchant.getName());
					merchantList.add(m);
				}
			} catch (JSONException je) {

			}
			return merchantList;
		}
	}*/
}
