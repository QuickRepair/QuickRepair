package com.har.quickrepairforandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class OrderDetailActivity extends FragmentAbstractActivity {

	private static final String EXTRA_ORDER_ID = "com.har.quickrepairforandroid.order_id";

	@Override
	public Fragment createFragment() {
		long orderId = (long)getIntent().getSerializableExtra(EXTRA_ORDER_ID);
		return OrderDetailFragment.newInstance(orderId);
	}

	public static Intent newIntent(Context context, long orderId) {
		Intent intent = new Intent(context, OrderDetailActivity.class);
		intent.putExtra(EXTRA_ORDER_ID, orderId);
		return intent;
	}
}
