package com.quickrepair.customer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quickrepair.customer.Database.AccountDatabase;
import com.quickrepair.customer.Database.OrderDatabase;
import com.quickrepair.customer.Database.OrderItem;
import com.quickrepair.customer.Models.AccountRepository;
import com.quickrepair.customer.Models.AccountViewModel;
import com.quickrepair.customer.Models.LoginAccount;
import com.quickrepair.customer.Models.OrderRepository;
import com.quickrepair.customer.Models.OrderViewModel;

import java.util.ArrayList;
import java.util.List;

public class OrderListFragment extends Fragment {

	private AccountViewModel accountViewModel;
	private OrderViewModel orderViewModel;

	private RecyclerView mOrderRecyclerView;
	private OrderAdapter mAdapter;

	public static OrderListFragment newInstance() {
		return new OrderListFragment();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_order_list, container, false);
		mOrderRecyclerView = v.findViewById(R.id.order_recycler_view);
		TextView title = v.findViewById(R.id.page_name);
		title.setText(R.string.title_order_list);

		accountViewModel = ViewModelProviders.of(this, new AccountViewModel.AccountViewModelFactory(
						new AccountRepository(AccountDatabase.getInstance(getContext()).AccountDao())
				)).get(AccountViewModel.class);
		orderViewModel = ViewModelProviders.of(this, new OrderViewModel.OrderViewModelFactory(
						new OrderRepository(OrderDatabase.getInstance(getContext()).orderDao())
				)).get(OrderViewModel.class);

		mOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		mAdapter = new OrderAdapter();
		mOrderRecyclerView.setAdapter(mAdapter);

		/*if(mAccountViewModel.getAccount() != null)
			new AsyncConnection(getContext()).getOrderList(
					mAccountViewModel.getAccount().getValue(),
					mAccountViewModel.getToken().getValue(),
					new GetOrderListTaskListener()
			);*/

		return v;
	}

	/*@Override
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
	}*/

	/*@Override
	public void onResume() {
		super.onResume();
		if(mAccountViewModel.getAccount() != null)
			new AsyncConnection(getContext()).getOrderList(
					mAccountViewModel.getAccount().getValue(),
					mAccountViewModel.getToken().getValue(),
					new GetOrderListTaskListener()
			);
	}*/

	private class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private OrderItem orderItem;

		private TextView mOrderTitle;
		private TextView mOrderDate;

		public OrderHolder(LayoutInflater inflater, ViewGroup parent) {
			super(inflater.inflate(R.layout.widget_order_list_item, parent, false));

			itemView.setOnClickListener(this);

			mOrderTitle = itemView.findViewById(R.id.order_title);
			mOrderDate = itemView.findViewById(R.id.order_date);
		}

		@Override
		public void onClick(View v) {
			Intent intent = OrderDetailActivity.newIntent(getActivity(), orderItem.getId());
			startActivity(intent);
		}

		public void bind(OrderItem order) {
			orderItem = order;
			mOrderTitle.setText(order.getApplianceType());
			mOrderDate.setText(order.getCreateDate());
		}
	}

	private class OrderAdapter extends RecyclerView.Adapter<OrderHolder> {

		List<OrderItem> ordersItems;

		public OrderAdapter() {
			ordersItems = new ArrayList<>();
			orderViewModel.getOrderList(
					LoginAccount.getInstance(getContext()).getAccount(),
					LoginAccount.getInstance(getContext()).getToken()
			).observe(OrderListFragment.this, new Observer<List<OrderItem>>() {
				@Override
				public void onChanged(@Nullable List<OrderItem> orders) {
					ordersItems = orders;
					mAdapter.notifyDataSetChanged();
				}
			});
		}

		@NonNull
		@Override
		public OrderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
			LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
			return new OrderHolder(layoutInflater, viewGroup);
		}

		@Override
		public void onBindViewHolder(@NonNull OrderHolder orderHolder, int i) {
			orderHolder.bind(ordersItems.get(i));
		}

		@Override
		public int getItemCount() {
			return ordersItems.size();
		}
	}

	/*private class GetOrderListTaskListener implements AsyncConnection.onResponseListener {
		@Override
		public void onResponse(Response response) {
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
	}*/
}
