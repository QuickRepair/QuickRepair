package com.har.quickrepairforandroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.har.quickrepairforandroid.AsyncTransmissions.AsyncTransmissionTask;
import com.har.quickrepairforandroid.AsyncTransmissions.HttpConnection;
import com.har.quickrepairforandroid.Models.AccountHolder;
import com.har.quickrepairforandroid.Models.ApplianceType;
import com.har.quickrepairforandroid.Models.Order;
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

public class OrderListFragment extends Fragment {

	List<Order> mOrdersList;

	private RecyclerView mOrderRecyclerView;
	private OrderAdapter mAdapter;

	private Handler mMainHandler;

	public static OrderListFragment newInstance() {
		return new OrderListFragment();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_order_list, container, false);
		mOrderRecyclerView = (RecyclerView)v.findViewById(R.id.order_recycler_view);
		mOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		mMainHandler = new Handler();
		mOrdersList = new ArrayList<>();
		mAdapter = new OrderAdapter(mOrdersList);
		mOrderRecyclerView.setAdapter(mAdapter);

		setHasOptionsMenu(true);
		new GetOrderListTask().execute();

		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_order_add, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.new_order:
				Intent intent = AddOrderActivity.newIntent(getContext());
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		new GetOrderListTask().execute();
	}

	private class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private Order mOrder;

		private TextView mOrderTitle;
		private TextView mOrderDate;

		public OrderHolder(LayoutInflater inflater, ViewGroup parent) {
			super(inflater.inflate(R.layout.order_list_item, parent, false));

			itemView.setOnClickListener(this);

			mOrderTitle = (TextView)itemView.findViewById(R.id.order_title);
			mOrderDate = (TextView)itemView.findViewById(R.id.order_date);
		}

		@Override
		public void onClick(View v) {
			Intent intent = OrderDetailActivity.newIntent(getActivity(), mOrder.id());
			startActivity(intent);
		}

		public void bind(Order order) {
			mOrder = order;
			mOrderTitle.setText(mOrder.applianceType());
			mOrderDate.setText(mOrder.createDate());
		}
	}

	private class OrderAdapter extends RecyclerView.Adapter<OrderHolder> {

		private List<Order> mOrders;

		public OrderAdapter(List<Order> orders) {
			mOrders = orders;
		}

		@NonNull
		@Override
		public OrderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
			LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
			return new OrderHolder(layoutInflater, viewGroup);
		}

		@Override
		public void onBindViewHolder(@NonNull OrderHolder orderHolder, int i) {
			Order order = mOrders.get(i);
			orderHolder.bind(order);
		}

		@Override
		public int getItemCount() {
			return mOrders.size();
		}

		public void setOrders(List<Order> orders) {
			mOrders = orders;
		}
	}

	private class GetOrderListTask implements AsyncTransmissionTask {
		@Override
		public void execute() {
			HttpConnection.getInstance().getMethod(makeRequest(), this);
		}

		@Override
		public Request makeRequest() {
			HttpUrl url = HttpUrl
					.parse(getContext().getResources().getString(R.string.server_ip))
					.newBuilder()
					.addQueryParameter("get_list", "order_list")
					.addQueryParameter("account", AccountHolder.getInstance().getAccount())
					.addQueryParameter("account_type", AccountHolder.getInstance().getIsCustomer() ? "customer" : "merchant")
					.build();
			return new Request.Builder().url(url).build();
		}

		@Override
		public void handler(Response response) {
			try {
				final String findJson = response.body().string();
				mMainHandler.post(new Runnable() {
					@Override
					public void run() {
						mOrdersList = getOrderList(findJson);
						mAdapter.setOrders(mOrdersList);
						mAdapter.notifyDataSetChanged();
					}
				});
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		private List<Order> getOrderList(String json) {
			List<Order> orderList = new ArrayList<>();

			try {
				JSONObject jsonObject = new JSONObject(json);
				JSONArray jsonArray = jsonObject.getJSONArray("order_list");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject item = jsonArray.getJSONObject(i);
					String date = item.getString("create_date");
					String type = item.getString("appliance_type");
					long id = item.getLong("id");
					orderList.add(new Order(id, date, type));
				}
			} catch (JSONException je) {
				je.printStackTrace();
			}

			return orderList;
		}
	}
}
