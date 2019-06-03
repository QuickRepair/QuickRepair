package com.har.quickrepairforandroid;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.har.quickrepairforandroid.AsyncTransmissions.AsyncHttpTask;
import com.har.quickrepairforandroid.AsyncTransmissions.HttpConnection;
import com.har.quickrepairforandroid.Models.AccountHolder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RegisterServiceFragment extends Fragment {

	private EditText mDistanceEditText;
	private EditText mSupportEdtiText;
	private Button mSubmitButton;

	private Handler mMainhandler;

	private WaitLoadingFragment mWaitSubmit;
	private static final String WAITING_SUBMIT = "wait_for_submit";

	public static RegisterServiceFragment newInstance() {
		return new RegisterServiceFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_register_service, container, false);

		mMainhandler = new Handler();

		mDistanceEditText = (EditText)v.findViewById(R.id.distanceEditText);
		mSupportEdtiText = (EditText)v.findViewById(R.id.supportAppliancesEditText);
		mSubmitButton = (Button)v.findViewById(R.id.submit_register_service);

		mSubmitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new submitServiceTypeTask().execute();
				FragmentManager fm = getFragmentManager();
				mWaitSubmit = WaitLoadingFragment.newInstance();
				mWaitSubmit.show(fm, WAITING_SUBMIT);
			}
		});

		new getOriginServiceTypeTask().execute();

		return v;
	}

	private class getOriginServiceTypeTask implements AsyncHttpTask {
		@Override
		public void execute() {
			HttpConnection.getInstance().getMethod(makeRequest(), this);
		}

		@Override
		public Request makeRequest() {
			HttpUrl url = HttpUrl.parse(getContext().getResources().getString(R.string.server_ip))
					.newBuilder()
					.addQueryParameter("get_list", "origin_service_type")
					.addQueryParameter("account", AccountHolder.getInstance().getAccount())
					.build();
			return new Request.Builder().url(url).build();
		}

		@Override
		public void handler(Response response) {
			try {
				final String findJson = response.body().string();
				JSONObject jsonObject = new JSONObject(findJson);
				final int maxDistance = jsonObject.getInt("max_distance");
				JSONArray supportArray = jsonObject.getJSONArray("support_appliance_type");
				String supportAppliances = "";
				for(int i = 0; i < supportArray.length(); i++)
					supportAppliances += supportArray.getString(i) + ";";
				final String typeText = supportAppliances;
				mMainhandler.post(new Runnable() {
					@Override
					public void run() {
						mDistanceEditText.setText(String.valueOf(maxDistance));
						mSupportEdtiText.setText(typeText);
					}
				});
			} catch (IOException ioe) {

			} catch (JSONException je) {

			}
		}
	}

	private class submitServiceTypeTask implements AsyncHttpTask {
		@Override
		public void execute() {
			HttpConnection.getInstance().postMethod(makeRequest(), this);
		}

		@Override
		public Request makeRequest() {
			try {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("type", "submit_service_type");
				JSONArray array = new JSONArray();
				String[] appliances = mSupportEdtiText.getText().toString().split(";");
				for(int i = 0; i < appliances.length; i++)
					array.put(i, appliances[i]);
				jsonObject.put("support_appliance", array);
				jsonObject.put("max_distance", Integer.parseInt(mDistanceEditText.getText().toString()));
				jsonObject.put("account", AccountHolder.getInstance().getAccount());
				RequestBody body = RequestBody.create(AsyncHttpTask.TypeJson, jsonObject.toString());
				return new Request.Builder().url(getContext().getResources().getString(R.string.server_ip)).post(body).build();
			} catch (JSONException je) {
				je.printStackTrace();
				return null;
			}
		}

		@Override
		public void handler(Response response) {
			mMainhandler.post(new Runnable() {
				@Override
				public void run() {
					mWaitSubmit.dismiss();
					getActivity().onBackPressed();
				}
			});
		}
	}
}
