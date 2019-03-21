package com.har.quickrepairforandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class AddOrderActivity extends FragmentAbstractActivity {

	@Override
	public Fragment createFragment() {
		return AddOrderFragment.newInstance();
	}

	public static Intent newIntent(Context context) {
		Intent intent = new Intent(context, AddOrderActivity.class);
		return intent;
	}
}
