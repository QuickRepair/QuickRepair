package com.quickrepair.customer.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;

import com.quickrepair.customer.Database.OrderItem;
import com.quickrepair.customer.Models.AccountViewModel;
import com.quickrepair.customer.Models.LoginAccount;
import com.quickrepair.customer.NetworkConnection.AsyncConnection;
import com.quickrepair.customer.OrderDetailActivity;
import com.quickrepair.customer.R;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PollOrderService extends Service {

	private AccountViewModel accountViewModel;

	private Map<Long, OrderItem> mOrderMap = new HashMap<>();
	private static int notificationId = 0;
	private NotificationManager mNotificationManager;
	private Timer mTimer;

	@Override
	public void onCreate() {
		mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//		accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(mTimer == null)
			mTimer = new Timer();

		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(isNetworkAvailableAndConnected()) {
					PublishUnreceivedOrders publish = new PublishUnreceivedOrders();
					publish.makeNotification();
				}
			}
		}, 5);
		return super.onStartCommand(intent, flags, startId);
	}

	private boolean isNetworkAvailableAndConnected() {
		ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
		boolean isNetworkAvaialable = cm.getActiveNetworkInfo() != null;
		return isNetworkAvaialable && cm.getActiveNetworkInfo().isConnected();
	}

	private class PublishUnreceivedOrders {

		private Notification mNewOrderNotification;
		private Intent mOrderDetailIntent;
		private PendingIntent mOrderDetailPendingIntent;

		public PublishUnreceivedOrders() {
			mNewOrderNotification = new Notification();
			mNewOrderNotification.tickerText = getString(R.string.new_order_coming);
			mNewOrderNotification.defaults = Notification.DEFAULT_SOUND;
		}

		public void makeNotification() {
			new AsyncConnection().getOrderList(
					LoginAccount.getInstance(getApplicationContext()).getAccount(),
					LoginAccount.getInstance(getApplicationContext()).getToken(),
					new GetOrderListListener()
			);
		}

		private class GetOrderListListener implements AsyncConnection.onResponseListener {
			@Override
			public void onResponse(Response response) {
				try {
					final String findJson = response.body().string();
					List<OrderItem> orders = getOrderList(findJson);
					for(OrderItem o : orders)
					{
						if(mOrderMap.get(o.getId()) == null) {
							mOrderDetailIntent = OrderDetailActivity.newIntent(PollOrderService.this, o.getId());
							mNotificationManager.notify(notificationId, mNewOrderNotification);
							notificationId++;
							mOrderMap.put(o.getId(), o);
						}
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}

			private List<OrderItem> getOrderList(String json) {
				List<OrderItem> orderList = new ArrayList<>();

				try {
					JSONObject jsonObject = new JSONObject(json);
					JSONArray jsonArray = jsonObject.getJSONArray("order_list");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject item = jsonArray.getJSONObject(i);
						String date = item.getString("create_date");
						String type = item.getString("appliance_type");
						long id = item.getLong("id");
						orderList.add(new OrderItem(id, date, type));
					}
				} catch (JSONException je) {
					je.printStackTrace();
				}

				return orderList;
			}
		}
	}
}
