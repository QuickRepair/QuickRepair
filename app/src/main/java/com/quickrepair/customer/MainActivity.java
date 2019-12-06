package com.quickrepair.customer;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.quickrepair.customer.Database.AccountDatabase;
import com.quickrepair.customer.Models.AccountRepository;
import com.quickrepair.customer.Models.AccountViewModel;

public class MainActivity extends AppCompatActivity {

	private FragmentManager fm;
	private Fragment homeFragment;
	private Fragment aboutMeFragment;
	private Fragment messageListFragment;
	private Fragment orderListFragment;

	private BottomNavigationView navigation;
	private ViewPager fragmentPagerContainer;

	private AccountViewModel accountViewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);

		navigation = findViewById(R.id.navigation);
		fragmentPagerContainer = findViewById(R.id.fragment_navigation_container);
		homeFragment = HomeFragment.newInstance();
		orderListFragment = OrderListFragment.newInstance();
		messageListFragment = MessageListFragment.newInstance();
		aboutMeFragment = AboutMeFragment.newInstance();

		fm = getSupportFragmentManager();
		fragmentPagerContainer.setAdapter(new FragmentPagerAdapter(fm) {
			@Override
			public Fragment getItem(int i) {
				switch (i) {
					case 0:
						return homeFragment;
					case 1:
						return orderListFragment;
					case 2:
						return messageListFragment;
					case 3:
						return aboutMeFragment;
					default:
						return null;
				}
			}

			@Override
			public int getCount() {
				return 4;
			}
		});

		fragmentPagerContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int i, float v, int i1) {
			}

			@Override
			public void onPageSelected(int i) {
				switch(i) {
					case 0:
						navigation.setSelectedItemId(R.id.navigation_home);
						break;
					case 1:
						navigation.setSelectedItemId(R.id.navigation_order);
						break;
					case 2:
						navigation.setSelectedItemId(R.id.navigation_talk);
						break;
					case 3:
						navigation.setSelectedItemId(R.id.navigation_about_me);
						break;
				}
			}

			@Override
			public void onPageScrollStateChanged(int i) {
			}
		});

		navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
				switch (menuItem.getItemId()) {
					case R.id.navigation_home:
						fragmentPagerContainer.setCurrentItem(0, false);
						return true;
					case R.id.navigation_order:
						fragmentPagerContainer.setCurrentItem(1, false);
						return true;
					case R.id.navigation_talk:
						fragmentPagerContainer.setCurrentItem(2, false);
						return true;
					case R.id.navigation_about_me:
						fragmentPagerContainer.setCurrentItem(3, false);
						return true;
				}
				return false;
			}
		});

		accountViewModel = ViewModelProviders.of(
				this, new AccountViewModel.AccountViewModelFactory(
						new AccountRepository(AccountDatabase.getInstance(getApplicationContext()).AccountDao())
				)
		).get(AccountViewModel.class);
		/*if(AccountViewModel.getInstance().attachedWithAccount())
			new AsyncConnection(getApplicationContext()).getToken(
					AccountViewModel.getInstance().getAccount(), new AutoLoginTaskListener()
			);*/
	}

	/*private void getAccountIfAvailable() {
		AccountBaseHelper helper = new AccountBaseHelper(getApplicationContext());
		AccountCursorWrapper cursor = helper.queryAccount();
		cursor.moveToFirst();
		if(!cursor.isAfterLast()) {
			Account account = cursor.getIsCustomer() ? new Customer() : new Merchant();
			accountViewModel.setAccount(account);
			accountViewModel.getAccount().getValue().setAccountNumber(cursor.getAccount());
			accountViewModel.getAccount().getValue().setPassword(cursor.getPassword());
			accountViewModel.getAccount().getValue().setAccountType(cursor.getIsCustomer() ? Account.Type.customer : Account.Type.merchant);
		}
	}*/

	/*private class AutoLoginTaskListener implements AsyncConnection.onResponseListener {
		@Override
		public void onResponse(final Response response) {
			try{
				String findJson = response.body().string();
				JSONObject jsonObject = new JSONObject(findJson);
				final String token = jsonObject.getString("token");
				final String accountId = jsonObject.getString("id");

				mMainHandler.post(new Runnable() {
					@Override
					public void run() {
						if(response.code() == StatuesCode.OK.getCode()) {// login successed
							AccountViewModel.getInstance().getAccount().setOnline(true);
							AccountViewModel.getInstance().getAccount().setId(accountId);
							AccountViewModel.getInstance().setToken(new Token(token));
							// make toast
							Toast.makeText(MainActivity.this, R.string.login_successsed, Toast.LENGTH_SHORT).show();
						}
						*//* else if(loginResult.equalsIgnoreCase("no such an account")){  // login failed: no account
							Toast.makeText(MainActivity.this, R.string.login_failed_no_such_account, Toast.LENGTH_SHORT).show();
						} else if(loginResult.equalsIgnoreCase("wrong password")) {     //login failed: password wrong
							Toast.makeText(MainActivity.this, R.string.login_failed_wrong_password, Toast.LENGTH_SHORT).show();
						}*//*
					}
				});
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}*/
}
