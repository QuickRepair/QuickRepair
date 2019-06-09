package com.har.quickrepairforandroid.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class LoginActivity extends FragmentAbstractActivity {

	@Override
	public Fragment createFragment() {
		return LoginFragment.newInstance();
	}

	public static Intent newIntent(Context context) {
		Intent intent = new Intent(context, LoginActivity.class);
		return intent;
	}
}
