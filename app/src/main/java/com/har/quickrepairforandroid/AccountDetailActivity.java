package com.har.quickrepairforandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class AccountDetailActivity extends FragmentAbstractActivity {

	@Override
	public Fragment createFragment() {
		return AccountDetailFragment.newInstance();
	}

	public static Intent newIntent(Context context) {
		return new Intent(context, AccountDetailActivity.class);
	}
}
