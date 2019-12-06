package com.quickrepair.customer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.quickrepair.customer.Database.Account;
import com.quickrepair.customer.Database.AccountDatabase;
import com.quickrepair.customer.Database.OrderDatabase;
import com.quickrepair.customer.Database.OrderItem;
import com.quickrepair.customer.Models.AccountRepository;
import com.quickrepair.customer.Models.AccountViewModel;
import com.quickrepair.customer.Models.LoginAccount;
import com.quickrepair.customer.Models.OrderRepository;
import com.quickrepair.customer.Models.OrderViewModel;
import com.quickrepair.customer.Widgets.WaitLoadingFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SubmitOrderFragment extends Fragment {

    private OrderViewModel orderViewModel;
    private AccountViewModel accountViewModel;

    private Spinner mApplianceTypeSpinner;
    private Button mSubmitButton;
    private EditText mDetailEditText;
    private EditText mAddressEditText;

    private WaitLoadingFragment mWaitSubmit;
    private static final String ARG_MERCHANT = "merchant";
    private static final String ARG_MERCHANT_SERVICE = "merchant_service";
    private static final String WAITING_SUBMIT = "wait_for_submit";

    private Account.Merchant mMerchant;
    private Account.Merchant.MerchantService merchantService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMerchant = (Account.Merchant) getArguments().getSerializable(ARG_MERCHANT);
        merchantService = (Account.Merchant.MerchantService) getArguments().getSerializable(ARG_MERCHANT_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_order, container, false);

        mApplianceTypeSpinner = v.findViewById(R.id.applianceTypeSpinner);
        mSubmitButton = v.findViewById(R.id.submitButton);
        mDetailEditText = v.findViewById(R.id.detailEditText);
        mAddressEditText = v.findViewById(R.id.addressEditText);
        TextView title = v.findViewById(R.id.page_name);
        title.setText(R.string.submit_order);

        if (merchantService != null) {
            List<Map<String, Object>> appliances = new ArrayList<>();
            for (String a : merchantService.getAppliances()) {
                Map<String, Object> map = new HashMap<>();
                map.put("appliance", a);
                appliances.add(map);
            }
            SimpleAdapter applianceAdapter = new SimpleAdapter(getContext(),
                    appliances,
                    R.layout.widget_spinner_list_item,
                    new String[]{"appliance"},
                    new int[]{R.id.spinnerTextView});
            applianceAdapter.setDropDownViewResource(R.layout.widget_spinner_list_dropdown_item);
            mApplianceTypeSpinner.setAdapter(applianceAdapter);
//		mApplianceTypeSpinner.setEnabled(true);
        }

        orderViewModel = ViewModelProviders.of(this, new OrderViewModel.OrderViewModelFactory(
                new OrderRepository(OrderDatabase.getInstance(getContext()).orderDao())
        )).get(OrderViewModel.class);
        accountViewModel = ViewModelProviders.of(this, new AccountViewModel.AccountViewModelFactory(
                new AccountRepository(AccountDatabase.getInstance(getContext()).AccountDao())
        )).get(AccountViewModel.class);

        accountViewModel.getMerchantService(mMerchant).observe(this, new Observer<Account.Merchant.MerchantService>() {
            @Override
            public void onChanged(@Nullable Account.Merchant.MerchantService service) {
                List<Map<String, Object>> appliances = new ArrayList<>();
                for (String a : merchantService.getAppliances()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("appliance", a);
                    appliances.add(map);
                }
                SimpleAdapter applianceAdapter = new SimpleAdapter(getContext(),
                        appliances,
                        R.layout.widget_spinner_list_item,
                        new String[]{"appliance"},
                        new int[]{R.id.spinnerTextView});
                applianceAdapter.setDropDownViewResource(R.layout.widget_spinner_list_dropdown_item);
                mApplianceTypeSpinner.setAdapter(applianceAdapter);
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: check input
                if (!mApplianceTypeSpinner.isEnabled()) {
                    Toast.makeText(getContext(), R.string.spinner_data_not_received, Toast.LENGTH_SHORT).show();
                } else if (mApplianceTypeSpinner.getAdapter().getCount() == 0) {
                    Toast.makeText(getContext(), R.string.spinner_data_empty, Toast.LENGTH_SHORT).show();
                } else {
                    OrderItem order = new OrderItem(
                            Long.valueOf(mMerchant.getId()),
                            Long.valueOf(LoginAccount.getInstance(getContext()).getAccount().getId()),
                            (String)((Map<String, Object>)mApplianceTypeSpinner.getAdapter().getItem(mApplianceTypeSpinner.getSelectedItemPosition())).get("appliance"),
                            mDetailEditText.getText().toString()
                    );
//					new AsyncConnection(getContext()).publishOrder(order, AccountViewModel.getInstance().getToken(), new SubmitOrderListener());
                    orderViewModel.newOrder(order, LoginAccount.getInstance(getContext()).getToken());
                    FragmentManager fm = getFragmentManager();
                    mWaitSubmit = WaitLoadingFragment.newInstance();
                    mWaitSubmit.show(fm, WAITING_SUBMIT);
                }
            }
        });

        //new AsyncConnection(getContext()).getMerchantList(0, 0, new GetMerchantListListener());
        //new AsyncConnection(getContext()).getMerchantService(merchant, new GetMerchantServiceListener());

        return v;
    }

    public static SubmitOrderFragment newInstance(Account.Merchant merchant, Account.Merchant.MerchantService service) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_MERCHANT, merchant);
        args.putSerializable(ARG_MERCHANT_SERVICE, service);
        SubmitOrderFragment fragment = new SubmitOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

	/*private class SubmitOrderListener implements AsyncConnection.onResponseListener {
		@Override
		public void onResponse(Response response) {
			try {
				final String findJson = response.body().string();
				mMainHandler.post(new Runnable() {
					@Override
					public void run() {
						try {
							mWaitSubmit.dismiss();
							Toast.makeText(getContext(), R.string.submit_done, Toast.LENGTH_SHORT).show();
							JSONObject object = new JSONObject(findJson);
							long id = object.getLong("id");
							String type = object.getString("applican_type");
							String date = object.getString("create_date");
							OrderItem order = new OrderItem(id, date, type);
							OrderBaseHelper helper = new OrderBaseHelper(getContext());
							helper.insertOrder(order);
							getActivity().onBackPressed();
						} catch (JSONException je) {
							je.printStackTrace();
						}
					}
				});
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}*/
}
