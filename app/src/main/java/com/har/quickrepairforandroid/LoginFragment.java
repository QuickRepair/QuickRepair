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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.har.quickrepairforandroid.AsyncTransmissions.AsyncTransmissionTask;
import com.har.quickrepairforandroid.AsyncTransmissions.HttpConnection;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class LoginFragment extends Fragment {

	private Switch mLoginTypeSwitch;
	private Button mLoginButton;
	private Button mGetVerification;
	private EditText mAccountText;
	private EditText mPasswordText;

	private static final String DIALOG_WAIT_LOGIN = "wait_login";
	private static final String DIALOG_WAIT_VERIFICATION = "wait_verification";

	private WaitLoadingFragment mLoginWaitingFragment;
	private WaitLoadingFragment mGetVerificationWaitingFragment;

	private Handler mMainHandler;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainHandler = new Handler();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_login, container, false);

		mLoginTypeSwitch = (Switch)v.findViewById(R.id.switchLoginType);
		mLoginButton = (Button)v.findViewById(R.id.buttonLogin);
		mGetVerification = (Button)v.findViewById(R.id.buttonGetVerification);
		mAccountText = (EditText)v.findViewById(R.id.editTextAccount);
		mPasswordText = (EditText)v.findViewById(R.id.editTextPassword);

		mLoginTypeSwitch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mLoginTypeSwitch.isChecked())
					mLoginTypeSwitch.setText(R.string.login_type_switch_merchant);
				else
					mLoginTypeSwitch.setText(R.string.login_type_switch_customer);
			}
		});

		mGetVerification.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isValidPhoneNumber(mAccountText.getText().toString())){
					// do get task
					new GetVerificationTask().execute();
					// waiting animation
					FragmentManager fm = getFragmentManager();
					mGetVerificationWaitingFragment = WaitLoadingFragment.newInstance();
					mGetVerificationWaitingFragment.show(fm, DIALOG_WAIT_VERIFICATION);
				} else {
					Toast.makeText(getActivity(), R.string.not_valid_phone_number, Toast.LENGTH_SHORT).show();
				}
			}
		});

		mLoginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isValidPhoneNumber(mAccountText.getText().toString()) && isValidVerificationCode(mPasswordText.getText().toString())) {
					// do login task
					new LoginTask().execute();
					// waiting animation
					FragmentManager fm = getFragmentManager();
					mLoginWaitingFragment = WaitLoadingFragment.newInstance();
					mLoginWaitingFragment.show(fm, DIALOG_WAIT_LOGIN);
				} else {
					Toast.makeText(getActivity(), R.string.not_valid_input, Toast.LENGTH_SHORT).show();
				}
			}
		});

		return v;
	}

	public static LoginFragment newInstance() {
		return new LoginFragment();
	}

	private boolean isValidPhoneNumber(String phoneNumber) {
		int length = getContext().getResources().getInteger(R.integer.phone_number);
		String regex = "(\\+\\d+)?1[34578]\\d{" + (length - 2) + "}$";
		return Pattern.matches(regex, phoneNumber);
	}

	private boolean isValidVerificationCode(String verification) {
		int length = getContext().getResources().getInteger(R.integer.verification);
		String regex = "\\d{" + length + "}$";
		return Pattern.matches(regex, verification);
	}

	private class GetVerificationTask implements AsyncTransmissionTask {
		@Override
		public void execute() {
			HttpConnection.getInstance().getMethod(makeRequest(), this);
		}

		@Override
		public Request makeRequest() {
			String accountType = mLoginTypeSwitch.isChecked() ? "merchant" : "customer";
			HttpUrl url = HttpUrl
					.parse("http://10.42.0.1:12345")
					.newBuilder()
					.addQueryParameter("verification", mAccountText.getText().toString())
					.addQueryParameter("account_type", accountType)
					.build();
			return new Request.Builder().url(url).build();
		}

		@Override
		public void handler(Response response) {
			//TODO
			mMainHandler.post(new Runnable() {
				@Override
				public void run() {
					// cancel animation
					mGetVerificationWaitingFragment.dismiss();
					// make toast
					Toast.makeText(getActivity(), R.string.verification_send_successed, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	private class LoginTask implements AsyncTransmissionTask {
		@Override
		public void execute() {
			HttpConnection.getInstance().postMethod(makeRequest(), this);
		}

		@Override
		public Request makeRequest() {
			try {
				JSONObject json = new JSONObject();
				json.put("account", mAccountText.getText().toString());
				json.put("password", mPasswordText.getText().toString());
				json.put("account_type", mLoginTypeSwitch.isChecked() ? "merchant" : "customer");
				RequestBody requestBody = RequestBody.create(AsyncTransmissionTask.TypeJson, json.toString());
				return new Request.Builder().url("http://10.42.0.1:12345").post(requestBody).build();
			} catch (JSONException je) {
				//TODO
				return null;
			}
		}

		@Override
		public void handler(Response response) {
			// login successed
			mMainHandler.post(new Runnable() {
				@Override
				public void run() {
					mLoginWaitingFragment.dismiss();
					AccountHolder.getInstance().setAccount(mAccountText.getText().toString());
					AccountHolder.getInstance().setPassword(mPasswordText.getText().toString());
					AccountHolder.getInstance().setIsCustomer(!mLoginTypeSwitch.isChecked());
					AccountHolder.getInstance().setIsLogin(true);
					// make toast
					Toast.makeText(getActivity(), R.string.login_successsed, Toast.LENGTH_SHORT).show();
					getActivity().onBackPressed();
				}
			});
		}
	}
}
