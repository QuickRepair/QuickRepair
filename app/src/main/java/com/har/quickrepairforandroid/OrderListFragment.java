package com.har.quickrepairforandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class OrderListFragment extends Fragment {

	private RecyclerView mOrderRecyclerView;

	private OrderAdapter mAdapter;

	public static OrderListFragment newInstance() {
		return new OrderListFragment();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_order_list, container, false);
		mOrderRecyclerView = (RecyclerView)v.findViewById(R.id.order_recycler_view);
		mOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		//TODO
		//for testing
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order());
		orders.add(new Order());
		mAdapter = new OrderAdapter(orders);
		mOrderRecyclerView.setAdapter(mAdapter);

		setHasOptionsMenu(true);

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
			// TODO
			Toast.makeText(getContext(), "enter", Toast.LENGTH_SHORT).show();
		}

		public void bind(Order order) {
			mOrder = order;
			mOrderTitle.setText(mOrder.title());
			mOrderDate.setText(mOrder.date());
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
	}
}
