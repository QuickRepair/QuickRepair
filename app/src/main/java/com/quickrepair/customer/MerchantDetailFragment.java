package com.quickrepair.customer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.quickrepair.customer.Database.Account;
import com.quickrepair.customer.Database.AccountDatabase;
import com.quickrepair.customer.Models.AccountRepository;
import com.quickrepair.customer.Models.AccountViewModel;


public class MerchantDetailFragment extends Fragment {

    private AccountViewModel accountViewModel;
    private static String ARG_MERCHANT = "com.quickrepair.merchant";
    private Account.Merchant merchant;
    private Account.Merchant.MerchantService service;

    public static MerchantDetailFragment newInstance(Account.Merchant merchant) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_MERCHANT, merchant);
        MerchantDetailFragment fragment = new MerchantDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        merchant = (Account.Merchant) getArguments().getSerializable(ARG_MERCHANT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_merchant_detail, container, false);
        TextView title = v.findViewById(R.id.page_name);
        Button button = v.findViewById(R.id.submit_new_order_button);

        TextView name = v.findViewById(R.id.merchantDetailNameTextView);
        name.setText(merchant.getId());

        title.setText(R.string.title_merchant_detail);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MerchantActivity) getActivity()).createSubmitOrderFragment(merchant, service);
            }
        });

        accountViewModel = ViewModelProviders.of(
                this, new AccountViewModel.AccountViewModelFactory(
                        new AccountRepository(AccountDatabase.getInstance(getContext()).AccountDao())
                )
        ).get(AccountViewModel.class);
        accountViewModel.getMerchantService(merchant).observe(this, new Observer<Account.Merchant.MerchantService>() {
            @Override
            public void onChanged(@Nullable Account.Merchant.MerchantService merchantService) {
                service = merchantService;
            }
        });

        return v;
    }

	/*private class GetMerchantServiceListener implements AsyncConnection.onResponseListener {
		@Override
		public void onResponse(final Response response) {
			try {
				final String findJson = response.body().string();
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						*//*SimpleAdapter applianceAdapter = new SimpleAdapter(getContext(),
								getApplianceTypeList(findJson),
								R.layout.widget_spinner_list_item, new String[]{"appliance"},
								new int[]{R.id.spinnerTextView});
						applianceAdapter.setDropDownViewResource(R.layout.widget_spinner_list_dropdown_item);
						mApplianceTypeSpinner.setAdapter(applianceAdapter);
						mApplianceTypeSpinner.setEnabled(true);
						mApplianceTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {
							}
						});*//*
					}
				});
			} catch (IOException ioe) {

			}
		}*/

		/*private List<Map<String, Object>> getApplianceTypeList(String json) {
			List<Map<String, Object>> applianceList = new ArrayList<>();

			try {
				JSONObject jsonObject = new JSONObject(json);
				JSONArray jsonArray = jsonObject.getJSONArray("appliance_list");
				for (int i = 0; i < jsonArray.length(); i++) {
					String name = jsonArray.getString(i);
					mApplianceTypeList.add(new ApplianceType(name));
				}
				for (ApplianceType appliance : mApplianceTypeList) {
					Map<String, Object> a = new HashMap<>();
					a.put("appliance", appliance.name());
					applianceList.add(a);
				}
			} catch (JSONException je) {

			}

			return applianceList;
		}
	}*/
}
