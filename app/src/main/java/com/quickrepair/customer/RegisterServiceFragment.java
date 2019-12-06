package com.quickrepair.customer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.quickrepair.customer.Database.Account;
import com.quickrepair.customer.Database.AccountDatabase;
import com.quickrepair.customer.Models.AccountRepository;
import com.quickrepair.customer.Models.AccountViewModel;
import com.quickrepair.customer.Models.LoginAccount;
import com.quickrepair.customer.Widgets.WaitLoadingFragment;

import java.util.ArrayList;

public class RegisterServiceFragment extends Fragment {

	private AccountViewModel accountViewModel;

	private EditText mDistanceEditText;
	private EditText mSupportEditText;
	private Button mSubmitButton;

	private WaitLoadingFragment mWaitSubmit;
	private static final String WAITING_SUBMIT = "wait_for_submit";

	public static RegisterServiceFragment newInstance() {
		return new RegisterServiceFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_register_service, container, false);

		mDistanceEditText = v.findViewById(R.id.distanceEditText);
		mSupportEditText = v.findViewById(R.id.supportAppliancesEditText);
		mSubmitButton = v.findViewById(R.id.submit_register_service);

		accountViewModel = ViewModelProviders.of(this, new AccountViewModel.AccountViewModelFactory(
				new AccountRepository(AccountDatabase.getInstance(getContext()).AccountDao())
		)).get(AccountViewModel.class);
		accountViewModel.getServiceIfIsMerchant((Account.Merchant)LoginAccount.getInstance(getContext()).getAccount()).observe(this, new Observer<Account.Merchant.MerchantService>() {
			@Override
			public void onChanged(@Nullable Account.Merchant.MerchantService merchantService) {
				mDistanceEditText.setText(String.valueOf(merchantService.getDistance()));
				mSupportEditText.setText(merchantService.getAppliances().toString());
			}
		});
		mSubmitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Account.Type.values()[LoginAccount.getInstance(getContext()).getAccount().getAccountType()] == Account.Type.merchant) {
					Account.Merchant account = (Account.Merchant)LoginAccount.getInstance(getContext()).getAccount();
					account.getService().setAppliances(new ArrayList<String>());	// TODO
					account.getService().setDistance(12);
				}
				/*new AsyncConnection(getContext()).updateServices(
						accountViewModel.getAccount().getValue(),
						accountViewModel.getToken().getValue(),
						new SubmitServicesListener()
				);*/
				FragmentManager fm = getFragmentManager();
				mWaitSubmit = WaitLoadingFragment.newInstance();
				mWaitSubmit.show(fm, WAITING_SUBMIT);
			}
		});

		/*new AsyncConnection(getContext()).getMerchantService(
				accountViewModel.getAccount().getValue(), new GetOriginServicesListener()
		);*/

		return v;
	}

	/*private class GetOriginServicesListener implements AsyncConnection.onResponseListener {
		@Override
		public void onResponse(Response response) {
			try {
				final String findJson = response.body().string();
				JSONObject jsonObject = new JSONObject(findJson);
				final int maxDistance = jsonObject.getInt("max_distance");
				JSONArray supportArray = jsonObject.getJSONArray("support_appliance_type");
				String supportAppliances = "";
				for(int i = 0; i < supportArray.length(); i++)
					supportAppliances += supportArray.getString(i) + ";";
				final String typeText = supportAppliances;
				mMainHandler.post(new Runnable() {
					@Override
					public void run() {

					}
				});
			} catch (IOException ioe) {

			} catch (JSONException je) {

			}
		}
	}*/

	/*private class SubmitServicesListener implements AsyncConnection.onResponseListener {
		@Override
		public void onResponse(Response response) {
			mMainHandler.post(new Runnable() {
				@Override
				public void run() {
					mWaitSubmit.dismiss();
					getActivity().onBackPressed();
				}
			});
		}
	}*/
}
