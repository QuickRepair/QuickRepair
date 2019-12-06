package com.quickrepair.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity implements FragmentHostActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment fragment = defaultFragment();
		ft.add(R.id.fragment_account, fragment).commit();
	}

	@Override
	public Fragment defaultFragment() {
		return AccountDetailFragment.newInstance();
	}

	public static Intent newIntent(Context context) {
		Intent intent = new Intent(context, AccountActivity.class);
		return intent;
	}

	public void createAccountDetailFragment() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment fragment = AccountDetailFragment.newInstance();
		ft.add(R.id.fragment_account, fragment).commit();
	}

	public void createRegisterServiceFragment() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment fragment = RegisterServiceFragment.newInstance();
		ft.add(R.id.fragment_account, fragment).commit();
	}
}
