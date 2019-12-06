package com.quickrepair.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.quickrepair.customer.Database.Account;

public class MerchantActivity extends AppCompatActivity implements FragmentHostActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchant);

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment fragment = defaultFragment();
		ft.add(R.id.fragment_merchant, fragment).commit();
	}

	@Override
	public Fragment defaultFragment() {
		return MerchantListFragment.newInstance();
	}

	public static Intent newIntent(Context context) {
		Intent intent = new Intent(context, MerchantActivity.class);
		return intent;
	}

	public void createMerchantListFragment() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment fragment = MerchantListFragment.newInstance();
		ft.replace(R.id.fragment_merchant, fragment).addToBackStack(null).commit();
	}

	public void createMerchantDetailFragment(Account.Merchant merchant) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment fragment = MerchantDetailFragment.newInstance(merchant);
		ft.replace(R.id.fragment_merchant, fragment).addToBackStack(null).commit();
	}

	public void createSubmitOrderFragment(Account.Merchant merchant, Account.Merchant.MerchantService service) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment fragment = SubmitOrderFragment.newInstance(merchant, service);
		ft.replace(R.id.fragment_merchant, fragment).addToBackStack(null).commit();
	}
}
