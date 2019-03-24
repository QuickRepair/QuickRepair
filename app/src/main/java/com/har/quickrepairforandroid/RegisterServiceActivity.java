package com.har.quickrepairforandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class RegisterServiceActivity extends FragmentAbstractActivity {

	@Override
	public Fragment createFragment() {
		return RegisterServiceFragment.newInstance();
	}

	public static Intent newIntent(Context context) {
		Intent intent = new Intent(context, RegisterServiceActivity.class);
		return intent;
	}
}
