package com.har.quickreporforcustomer;

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
import android.widget.Toast;

import java.util.regex.Pattern;

public class LoginFragment extends Fragment {

	private Switch loginSwitch;
	private Button loginButton;
	private Button getVerification;
	private EditText accountText;
	private EditText passwordText;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_login, container, false);

		loginSwitch = (Switch)v.findViewById(R.id.switchLoginType);
		loginButton = (Button)v.findViewById(R.id.buttonLogin);
		getVerification = (Button)v.findViewById(R.id.buttonGetVerification);
		accountText = (EditText)v.findViewById(R.id.editTextAccount);
		passwordText = (EditText)v.findViewById(R.id.editTextPassword);

		loginSwitch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(loginSwitch.isChecked())
					loginSwitch.setText(R.string.login_type_switch_merchant);
				else
					loginSwitch.setText(R.string.login_type_switch_customer);
			}
		});

		getVerification.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isValidPhoneNumber(accountText.getText().toString())){
					Toast.makeText(getActivity(), accountText.getText().toString(), Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), R.string.notValidPhoneNumber, Toast.LENGTH_SHORT).show();
				}
			}
		});

		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isValidPhoneNumber(accountText.getText().toString()) && isValidVerificationCode(passwordText.getText().toString())) {
					AccountHolder.getInstance().setAccount(accountText.getText().toString());
					AccountHolder.getInstance().setPassword(passwordText.getText().toString());
					AccountHolder.getInstance().setIsCustomer(!loginSwitch.isChecked());
					Toast.makeText(getActivity(),
							AccountHolder.getInstance().getAccount() + " " + AccountHolder.getInstance().getPassword() + " " + AccountHolder.getInstance().getIsCustomer(),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), R.string.notValidInput, Toast.LENGTH_SHORT).show();
				}
			}
		});

		return v;
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
}
