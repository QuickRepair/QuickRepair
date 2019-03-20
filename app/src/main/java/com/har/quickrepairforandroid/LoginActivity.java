package com.har.quickrepairforandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class LoginActivity extends FragmentAbstractActivity {

	private static final int REQUEST_LOGIN_STATE = 0;

	@Override
	public Fragment createFragment() {
		return LoginFragment.newInstance();
	}

	public static Intent newIntent(Context context) {
		Intent intent = new Intent(context, LoginActivity.class);
		return intent;
	}
}
