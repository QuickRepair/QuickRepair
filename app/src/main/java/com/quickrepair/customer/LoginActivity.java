package com.quickrepair.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements FragmentHostActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment fragment = defaultFragment();
		ft.add(R.id.fragment_login, fragment).commit();
	}

	@Override
	public Fragment defaultFragment() {
		return LoginFragment.newInstance();
	}

	public static Intent newIntent(Context context) {
		Intent intent = new Intent(context, LoginActivity.class);
		return intent;
	}
}
