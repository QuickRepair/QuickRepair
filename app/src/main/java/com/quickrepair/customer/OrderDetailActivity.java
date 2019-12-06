package com.quickrepair.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class OrderDetailActivity extends AppCompatActivity implements FragmentHostActivity {

	private static final String EXTRA_ORDER = "com.quickrepair.customer.order";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment fragment = defaultFragment();
		ft.add(R.id.fragment_order_detail, fragment).commit();
	}

	@Override
	public Fragment defaultFragment() {
		Long orderId = (Long)getIntent().getSerializableExtra(EXTRA_ORDER);
		return OrderDetailFragment.newInstance(orderId);
	}

	public static Intent newIntent(Context context, Long orderId) {
		Intent intent = new Intent(context, OrderDetailActivity.class);
		intent.putExtra(EXTRA_ORDER, orderId);
		return intent;
	}
}
