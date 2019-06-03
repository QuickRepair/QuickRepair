package com.har.quickrepairforandroid;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.har.quickrepairforandroid.AsyncTransmissions.AsyncHttpTask;
import com.har.quickrepairforandroid.AsyncTransmissions.HttpConnection;
import com.har.quickrepairforandroid.Database.AccountBaseHelper;
import com.har.quickrepairforandroid.Database.AccountCursorWrapper;
import com.har.quickrepairforandroid.Models.AccountHolder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class NavigationActivity extends AppCompatActivity {

	private FragmentManager fm;
	private Fragment aboutMeFragment;
	private Fragment messageListFragment;
	private Fragment orderListFragment;

	private BottomNavigationView navigation;
	private ViewPager fragmentPagerContainer;

	private Handler mMainHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);

		navigation = (BottomNavigationView) findViewById(R.id.navigation);
		fragmentPagerContainer = (ViewPager)findViewById(R.id.fragment_navigation_container);
		orderListFragment = OrderListFragment.newInstance();
		messageListFragment = MessageListFragment.newInstance();
		aboutMeFragment = AboutMeFragment.newInstance();

		mMainHandler = new Handler();

		fm = getSupportFragmentManager();
		fragmentPagerContainer.setAdapter(new FragmentPagerAdapter(fm) {
			@Override
			public Fragment getItem(int i) {
				switch (i) {
					case 0:
						return orderListFragment;
					case 1:
						return messageListFragment;
					case 2:
						return aboutMeFragment;
					default:
						return null;
				}
			}

			@Override
			public int getCount() {
				return 3;
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
						navigation.setSelectedItemId(R.id.navigation_talk);
						break;
					case 2:
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
					case R.id.navigation_talk:
						fragmentPagerContainer.setCurrentItem(1, false);
						return true;
					case R.id.navigation_about_me:
						fragmentPagerContainer.setCurrentItem(2, false);
						return true;
				}
				return false;
			}
		});

		readAccountFromDatabase();
		new AutoLoginTask().execute();
	}

	private void readAccountFromDatabase() {
		AccountBaseHelper helper = new AccountBaseHelper(getApplicationContext());
		AccountCursorWrapper cursor = helper.queryAccount();
		cursor.moveToFirst();
		if(!cursor.isAfterLast()) {
			AccountHolder.getInstance().setAccount(cursor.getAccount());
			AccountHolder.getInstance().setPassword(cursor.getPassword());
			AccountHolder.getInstance().setIsCustomer(cursor.getIsCustomer());
		}
	}

	private class AutoLoginTask implements AsyncHttpTask {
		@Override
		public void execute() {
			HttpConnection.getInstance().postMethod(makeRequest(), this);
		}

		@Override
		public Request makeRequest() {
			try {
				JSONObject json = new JSONObject();
				json.put("type", "login");
				json.put("account", AccountHolder.getInstance().getAccount());
				json.put("password", AccountHolder.getInstance().getPassword());
				json.put("account_type", AccountHolder.getInstance().getIsCustomer() ? "customer" : "merchant");
				RequestBody requestBody = RequestBody.create(AsyncHttpTask.TypeJson, json.toString());
				return new Request.Builder().url(getApplicationContext().getResources().getString(R.string.server_ip)).post(requestBody).build();
			} catch (JSONException je) {
				je.printStackTrace();
				return null;
			}
		}

		@Override
		public void handler(Response response) {
			try{
				String findJson = response.body().string();
				JSONObject jsonObject = new JSONObject(findJson);
				final String loginResult = jsonObject.getString("login_result");

				mMainHandler.post(new Runnable() {
					@Override
					public void run() {
						if(loginResult.equalsIgnoreCase("success")) {// login successed
							AccountHolder.getInstance().setIsLogin(true);
							// make toast
							Toast.makeText(NavigationActivity.this, R.string.login_successsed, Toast.LENGTH_SHORT).show();
						} else if(loginResult.equalsIgnoreCase("no such an account")){  // login failed: no account
							Toast.makeText(NavigationActivity.this, R.string.login_failed_no_such_account, Toast.LENGTH_SHORT).show();
						} else if(loginResult.equalsIgnoreCase("wrong password")) {     //login failed: password wrong
							Toast.makeText(NavigationActivity.this, R.string.login_failed_wrong_password, Toast.LENGTH_SHORT).show();
						}
					}
				});
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
