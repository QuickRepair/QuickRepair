package com.har.quickrepairforandroid.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.har.quickrepairforandroid.AsyncTransmissions.AsyncHttpTask;
import com.har.quickrepairforandroid.AsyncTransmissions.HttpConnection;
import com.har.quickrepairforandroid.Models.Order;
import com.har.quickrepairforandroid.R;
import com.har.quickrepairforandroid.UI.OrderDetailActivity;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PollOrderService extends Service {

	public static final String PollOrderAccountLogin = "PollOrderAccountLogin";
	public static final String PollOrderChangeAccount = "PollOrderChangeAccount";
	public static final String PollOrderIsCustomer = "PollOrderIsCustomer";

	private boolean mIsLogin;
	private boolean mIsCustomer;
	private String mAccountString;

	private Map<Long, Order> mOrderMap = new HashMap<>();
	private static int notificationId = 0;

	public PollOrderService() {
		Log.d("servicePollOrderService", "construct");
	}
	@Override
	public void onCreate() {
		Log.d("servicePollOrderService", "create");
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent != null) {
			Bundle bundle = intent.getExtras();
			if(bundle != null) {
				mAccountString = bundle.getString(PollOrderChangeAccount);
				mIsLogin = bundle.getBoolean(PollOrderAccountLogin);
				mIsCustomer = bundle.getBoolean(PollOrderIsCustomer);
			}
		}

		if(isNetworkAvailableAndConnected() && mIsLogin) {
			NotifyUnreceivedOrders nofity = new NotifyUnreceivedOrders();
			nofity.pullOrders();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private boolean isNetworkAvailableAndConnected() {
		ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
		boolean isNetworkAvaialable = cm.getActiveNetworkInfo() != null;
		return isNetworkAvaialable && cm.getActiveNetworkInfo().isConnected();
	}

	private class NotifyUnreceivedOrders {

		private Notification.Builder mBuilder;
		private NotificationManager mNotificationManager;

		public NotifyUnreceivedOrders() {
			mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			mBuilder = new Notification.Builder(PollOrderService.this);
		}

		public void pullOrders() {
			new GetOrderListTask().execute();
		}

		public void showNotification(Order order) {
			Log.d("servicePollOrderService", "Notify");
			mBuilder.setContentTitle(getString(R.string.new_order_coming))
					.setContentText(order.detail())
					.setContentIntent(
							PendingIntent.getActivity(
									PollOrderService.this,0,
									OrderDetailActivity.newIntent(PollOrderService.this, order.id()),
									0)
					)
					.setTicker(getString(R.string.new_order_coming))
					.setWhen(System.currentTimeMillis())
					.setPriority(Notification.PRIORITY_MAX)
					.setOngoing(false)
					.setDefaults(Notification.DEFAULT_ALL)
					.setSmallIcon(R.drawable.ic_action_add);
			Notification notification = mBuilder.build();
			mNotificationManager.notify(notificationId, notification);
			notificationId++;
		}

		private class GetOrderListTask implements AsyncHttpTask {
			@Override
			public void execute() {
				HttpConnection.getInstance().getMethod(makeRequest(), this);
			}

			@Override
			public Request makeRequest() {
				Log.d("servicePollOrderService", "running with " + mAccountString);

				HttpUrl url = HttpUrl
						.parse(getString(R.string.server_ip))
						.newBuilder()
						.addQueryParameter("get_list", "order_list")
						.addQueryParameter("account", mAccountString)
						.addQueryParameter("account_type", mIsCustomer ? "customer" : "merchant")
						.build();
				return new Request.Builder().url(url).build();
			}

			@Override
			public void handler(Response response) {
				try {
					final String findJson = response.body().string();
					List<Order> orders = getOrderList(findJson);
					for(Order o : orders)
					{
						if(o.orderState() == Order.State.unreceived && mOrderMap.get(o.id()) == null) {
							showNotification(o);
							mOrderMap.put(o.id(), o);
						}
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}

			private List<Order> getOrderList(String json) {
				List<Order> orderList = new ArrayList<>();

				try {
					JSONObject jsonObject = new JSONObject(json);
					JSONArray jsonArray = jsonObject.getJSONArray("order_list");
					Log.d("servicePollOrderService", "order num: " + jsonArray.length());
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject item = jsonArray.getJSONObject(i);

						String date = item.getString("create_date");
						String type = item.getString("appliance_type");
						long id = item.getLong("id");

						Order order = new Order(id, date, type);
						Log.d("servicePollOrderService", item.toString());

						String orderState = item.getString("current_state");
						if(orderState.equalsIgnoreCase("unreceived"))
							order.setOrderState(Order.State.unreceived);
						else if(orderState.equalsIgnoreCase("received"))
							order.setOrderState(Order.State.received);
						else if(orderState.equalsIgnoreCase("repairing"))
							order.setOrderState(Order.State.repairing);
						else if(orderState.equalsIgnoreCase("paying"))
							order.setOrderState(Order.State.paying);
						else if(orderState.equalsIgnoreCase("finished"))
							order.setOrderState(Order.State.finished);
						else if(orderState.equalsIgnoreCase("reject"))
							order.setOrderState(Order.State.reject);

						orderList.add(order);
					}
				} catch (JSONException je) {
					Log.d("servicePollOrderService", je.getMessage());
					//je.printStackTrace();
				}

				return orderList;
			}
		}
	}
}
