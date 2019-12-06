package com.quickrepair.customer;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.quickrepair.customer.Database.AccountDatabase;
import com.quickrepair.customer.Models.AccountRepository;
import com.quickrepair.customer.Models.AccountViewModel;
import com.quickrepair.customer.Models.LoginAccount;
import com.quickrepair.customer.Widgets.WaitLoadingFragment;

import java.util.regex.Pattern;


public class LoginFragment extends Fragment {

	private AccountViewModel accountViewModel;

	private Switch mLoginTypeSwitch;
	private Button mLoginButton;
	private Button mGetVerification;
	private EditText mAccountText;
	private EditText mPasswordText;

	private static final String DIALOG_WAIT_LOGIN = "wait_login";
	private static final String DIALOG_WAIT_VERIFICATION = "wait_verification";

	private WaitLoadingFragment mLoginWaitingFragment;
	private WaitLoadingFragment mGetVerificationWaitingFragment;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_login, container, false);

		accountViewModel = ViewModelProviders.of(this, new AccountViewModel.AccountViewModelFactory(
				new AccountRepository(AccountDatabase.getInstance(getContext()).AccountDao())
		)).get(AccountViewModel.class);

		mLoginTypeSwitch = (Switch)v.findViewById(R.id.switchLoginType);
		mLoginButton = (Button)v.findViewById(R.id.buttonLogin);
		mGetVerification = (Button)v.findViewById(R.id.buttonGetVerification);
		mAccountText = (EditText)v.findViewById(R.id.editTextAccount);
		mPasswordText = (EditText)v.findViewById(R.id.editTextPassword);
		TextView title = v.findViewById(R.id.page_name);
		title.setText(R.string.title_login);

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
					LoginAccount.getInstance(getContext()).getAccount().setAccountNumber(mAccountText.getText().toString());
					accountViewModel.getVerification(LoginAccount.getInstance(getContext()).getAccount());
//					accountViewModel.getAccount().getValue().setAccountNumber(mAccountText.getText().toString());
					/*new AsyncConnection(getContext()).getVerification(
							AccountViewModel.getInstance().getAccount(),
							new GetVerificationListener());*/
					// waiting animation
					/*FragmentManager fm = getFragmentManager();
					mGetVerificationWaitingFragment = WaitLoadingFragment.newInstance();
					mGetVerificationWaitingFragment.show(fm, DIALOG_WAIT_VERIFICATION);*/
					// make toast
					Toast.makeText(getActivity(), R.string.verification_send_successed, Toast.LENGTH_SHORT).show();
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
					LoginAccount.getInstance(getContext()).getAccount().setAccountNumber(mAccountText.getText().toString());
					LoginAccount.getInstance(getContext()).getAccount().setPassword(mPasswordText.getText().toString());
					accountViewModel.checkPassword(
							LoginAccount.getInstance(getContext()).getAccount(), LoginAccount.getInstance(getContext()).getToken());
//					accountViewModel.getAccount().getValue().setPassword(mPasswordText.getText().toString());
					/*new AsyncConnection(getContext()).getToken(
							AccountViewModel.getInstance().getAccount(),
							new GetTokenListener());*/
					// waiting animation
					/*FragmentManager fm = getFragmentManager();
					mLoginWaitingFragment = WaitLoadingFragment.newInstance();
					mLoginWaitingFragment.show(fm, DIALOG_WAIT_LOGIN);*/
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
		int length = getContext().getResources().getInteger(R.integer.phone_number_length);
		String regex = "(\\+\\d+)?1[34578]\\d{" + (length - 2) + "}$";
		return Pattern.matches(regex, phoneNumber);
	}

	private boolean isValidVerificationCode(String verification) {
		int length = getContext().getResources().getInteger(R.integer.verification_length);
		String regex = "\\d{" + length + "}$";
		return Pattern.matches(regex, verification);
	}

	/*private class GetVerificationListener implements AsyncConnection.onResponseListener {
		@Override
		public void onResponse(Response response) {
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
	}*/

	/*private class GetTokenListener implements AsyncConnection.onResponseListener {
		@Override
		public void onResponse(final Response response) {
			try{
				String findJson = response.body().string();
				JSONObject jsonObject = new JSONObject(findJson);
				final String token = jsonObject.getString("token");
				final String accountId = jsonObject.getString("id");

				mMainHandler.post(new Runnable() {
					@Override
					public void run() {
						if(response.code() == StatuesCode.OK.getCode()) { // login successed
							mLoginWaitingFragment.dismiss();
							AccountViewModel.getInstance().getAccount().setAccountNumber(mAccountText.getText().toString());
							AccountViewModel.getInstance().getAccount().setPassword(mPasswordText.getText().toString());
							AccountViewModel.getInstance().getAccount().setAccountType(!mLoginTypeSwitch.isChecked() ? Account.Type.merchant : Account.Type.customer);
							AccountViewModel.getInstance().getAccount().setOnline(true);
							AccountViewModel.getInstance().getAccount().setId(accountId);
							AccountViewModel.getInstance().setToken(new Token(token));
							// update database
							AccountBaseHelper helper = new AccountBaseHelper(getContext());
							helper.updateAccount();
							// make toast
							Toast.makeText(getActivity(), R.string.login_successsed, Toast.LENGTH_SHORT).show();
							getActivity().onBackPressed();
						}
						*//* else if(loginResult.equalsIgnoreCase("no such an account")){  // login failed: no account
							mLoginWaitingFragment.dismiss();
							AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
							dialog.setTitle(R.string.login_failed_title);
							dialog.setMessage(R.string.login_failed_no_such_account);
							dialog.setPositiveButton(R.string.login_failed_ok_button, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
								}
							});
							dialog.show();
						} else if(loginResult.equalsIgnoreCase("wrong password")) {     //login failed: password wrong
							mLoginWaitingFragment.dismiss();
							AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
							dialog.setTitle(R.string.login_failed_title);
							dialog.setMessage(R.string.login_failed_wrong_password);
							dialog.setPositiveButton(R.string.login_failed_ok_button, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
								}
							});
							dialog.show();
						}*//*
					}
				});
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}*/
}
