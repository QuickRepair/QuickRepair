package com.har.quickrepairforandroid;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.har.quickrepairforandroid.AsyncTransmissions.AsyncTransmissionTask;
import com.har.quickrepairforandroid.AsyncTransmissions.HttpConnection;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddOrderFragment extends Fragment {

	private Spinner mMerchantSpinner;
	private Spinner mApplianceTypeSpinner;
	private Button mSubmitButton;

	private WaitLoadingFragment waitSubmit;
	private static final String WAITING_SUBMIT = "wait_for_submit";

	private Handler mMainHandler;

	private List<Merchant> mMerchantList = new ArrayList<>();
	private List<ApplianceType> mApplianceTypeList = new ArrayList<>();

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_add_order, container, false);

		mMerchantSpinner = (Spinner)v.findViewById(R.id.merchantListSpinner);
		mApplianceTypeSpinner = (Spinner)v.findViewById(R.id.applianceTypeSpinner);
		mSubmitButton = (Button)v.findViewById(R.id.submitButton);

		mMerchantSpinner.setEnabled(false);
		mApplianceTypeSpinner.setEnabled(false);

		mSubmitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO: check input
				if(mMerchantSpinner.isEnabled() && mApplianceTypeSpinner.isEnabled()) {
					new SubmitOrderTask().execute();
					FragmentManager fm = getFragmentManager();
					waitSubmit = WaitLoadingFragment.newInstance();
					waitSubmit.show(fm, WAITING_SUBMIT);
				}
			}
		});

		new GetMerchantListTask().execute();
		new GetApplianceTypeTask().execute();

		return v;
	}

	public static AddOrderFragment newInstance() {
		return new AddOrderFragment();
	}

	private class GetMerchantListTask implements AsyncTransmissionTask {
		@Override
		public void execute() {
			HttpConnection.getInstance().getMethod(makeRequest(), this);
		}

		@Override
		public Request makeRequest() {
			HttpUrl url = HttpUrl.parse(getContext().getResources().getString(R.string.server_ip))
					.newBuilder()
					.addQueryParameter("get_list", "merchant_list")
					.build();
			return new Request.Builder().url(url).build();
		}

		@Override
		public void handler(final Response response) {
			mMainHandler.post(new Runnable() {
				@Override
				public void run() {
					SimpleAdapter merchantAdapter = new SimpleAdapter(getContext(),
							getMerchantList(response),
							R.layout.spinner_list_item, new String[]{"merchant"},
							new int[]{R.id.spinnerTextView});
					merchantAdapter.setDropDownViewResource(R.layout.spinner_list_dropdown_item);
					mMerchantSpinner.setAdapter(merchantAdapter);
					mMerchantSpinner.setEnabled(true);
					mMerchantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
						}
					});
				}
			});
		}

		private List<Map<String, Object>> getMerchantList(Response response) {
			//TODO, save response in mMerchantList

			List<Map<String, Object>> merchantList = new ArrayList<Map<String, Object>>();

			for(Merchant merchant : mMerchantList) {
				Map<String, Object> m = new HashMap<>();
				m.put("merchant", merchant.name());
				merchantList.add(m);
			}

			return merchantList;
		}
	}

	private class GetApplianceTypeTask implements AsyncTransmissionTask {
		@Override
		public void execute() {
			HttpConnection.getInstance().getMethod(makeRequest(), this);
		}

		@Override
		public Request makeRequest() {
			HttpUrl url = HttpUrl.parse(getContext().getResources().getString(R.string.server_ip))
					.newBuilder()
					.addQueryParameter("get_list", "merchant_list")
					.build();
			return new Request.Builder().url(url).build();
		}

		@Override
		public void handler(final Response response) {
			mMainHandler.post(new Runnable() {
				@Override
				public void run() {
					SimpleAdapter applianceAdapter = new SimpleAdapter(getContext(),
							getApplianceTypeList(response),
							R.layout.spinner_list_item, new String[]{"appliance"},
							new int[]{R.id.spinnerTextView});
					applianceAdapter.setDropDownViewResource(R.layout.spinner_list_dropdown_item);
					mApplianceTypeSpinner.setAdapter(applianceAdapter);
					mApplianceTypeSpinner.setEnabled(true);
					mApplianceTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
						}
					});
				}
			});
		}

		private List<Map<String, Object>> getApplianceTypeList(Response response) {
			//TODO, save response in mApplianceTypeList

			List<Map<String, Object>> applianceList = new ArrayList<>();

			for(ApplianceType appliance : mApplianceTypeList) {
				Map<String, Object> a = new HashMap<>();
				a.put("appliance", appliance.name());
				applianceList.add(a);
			}

			return applianceList;
		}
	}

	private class SubmitOrderTask implements AsyncTransmissionTask {
		@Override
		public void execute() {
			HttpConnection.getInstance().postMethod(makeRequest(), this);
		}

		@Override
		public Request makeRequest() {
			try {
				JSONObject json = new JSONObject();
				json.put("merchant", mMerchantList.get(mMerchantSpinner.getSelectedItemPosition()));
				json.put("appliance", mApplianceTypeList.get(mApplianceTypeSpinner.getSelectedItemPosition()));
				RequestBody requestBody = RequestBody.create(AsyncTransmissionTask.TypeJson, json.toString());
				return new Request.Builder().url(getContext().getResources().getString(R.string.server_ip)).post(requestBody).build();
			} catch (JSONException je) {
				//TODO
				return null;
			}
		}

		@Override
		public void handler(Response response) {
		}
	}
}
