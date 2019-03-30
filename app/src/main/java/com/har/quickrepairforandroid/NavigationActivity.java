package com.har.quickrepairforandroid;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.har.quickrepairforandroid.Database.AccountBaseHelper;
import com.har.quickrepairforandroid.Database.AccountCursorWrapper;
import com.har.quickrepairforandroid.Models.AccountHolder;

public class NavigationActivity extends AppCompatActivity {

	private FragmentManager fm;
	private Fragment aboutMeFragment;
	private Fragment messageListFragment;
	private Fragment orderListFragment;

	private BottomNavigationView navigation;
	private ViewPager fragmentPagerContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);

		navigation = (BottomNavigationView) findViewById(R.id.navigation);
		fragmentPagerContainer = (ViewPager)findViewById(R.id.fragment_navigation_container);
		orderListFragment = OrderListFragment.newInstance();
		messageListFragment = MessageListFragment.newInstance();
		aboutMeFragment = AboutMeFragment.newInstance();

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
	}

	private void readAccountFromDatabase() {
		SQLiteDatabase database = new AccountBaseHelper(getApplicationContext()).getReadableDatabase();
		AccountCursorWrapper cursor = AccountHolder.getInstance().queryAccount(database);
		if(!cursor.isAfterLast()) {
			AccountHolder.getInstance().setAccount(cursor.getAccount());
			AccountHolder.getInstance().setPassword(cursor.getPassword());
			AccountHolder.getInstance().setIsCustomer(cursor.getIsCustomer());
		}
	}
}
