package com.har.quickrepairforandroid;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.har.quickrepairforandroid.AsyncTransmissions.AsyncTransmissionTask;
import com.har.quickrepairforandroid.AsyncTransmissions.HttpConnection;
import com.har.quickrepairforandroid.Models.Order;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class OrderDetailFragment extends Fragment {

	private static final String ARG_ORDER_ID = "order_id";

	private Handler mMainHandler;

	private long mOrderId;
	private Order mOrder;

	private TextView mOrderStateTextView;
	private TextView mApplianceTypeTextView;
	private TextView mDetailDescripTextView;
	private TextView mOrderSubmitDateTextView;
	private TextView mOrderStartRepairDateTextView;
	private TextView mOrderEndRepairDateTextView;
	private TextView mOrderFinishDateTextView;
	private TextView mOrderRejectDateTextView;
	private TextView mOrderPayDateTextView;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mOrderId = (long)getArguments().getSerializable(ARG_ORDER_ID);
		mMainHandler = new Handler();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_order_detail, container, false);

		mOrderStateTextView = v.findViewById(R.id.orderStateTextView);
		mApplianceTypeTextView = v.findViewById(R.id.applianceTypeTextView);
		mDetailDescripTextView = v.findViewById(R.id.detailDescripTextView);
		mOrderSubmitDateTextView = v.findViewById(R.id.orderSubmitDateTextView);
		mOrderStartRepairDateTextView = v.findViewById(R.id.orderStartRepairDateTextView);
		mOrderEndRepairDateTextView = v.findViewById(R.id.orderEndRepairDateTextView);
		mOrderFinishDateTextView = v.findViewById(R.id.orderFinishDateTextView);
		mOrderRejectDateTextView = v.findViewById(R.id.orderRejectDateTextView);
		mOrderPayDateTextView = v.findViewById(R.id.orderPayDateTextView);

		new GetOrderDetailTask().execute();

		return v;
	}

	public static OrderDetailFragment newInstance(long orderId) {
		Bundle args = new Bundle();
		args.putSerializable(ARG_ORDER_ID, orderId);
		OrderDetailFragment fragment = new OrderDetailFragment();
		fragment.setArguments(args);

		return fragment;
	}

	private class GetOrderDetailTask implements AsyncTransmissionTask {
		@Override
		public void execute() {
			HttpConnection.getInstance().getMethod(makeRequest(), this);
		}

		@Override
		public Request makeRequest() {
			HttpUrl url = HttpUrl
					.parse(getContext().getResources().getString(R.string.server_ip))
					.newBuilder()
					.addQueryParameter("type", "get_order_detail")
					.addQueryParameter("order_id", String.valueOf(mOrderId))
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
						mOrder = getOrder(findJson);
						switch (mOrder.orderState()) {
							case unreceived:
								mOrderStateTextView.setText(R.string.order_state_unreceived);
								break;
							case received:
								mOrderStateTextView.setText(R.string.order_state_received);
								break;
							case repairing:
								mOrderStateTextView.setText(R.string.order_state_repairing);
								break;
							case paying:
								mOrderStateTextView.setText(R.string.order_state_paying);
								break;
							case finished:
								mOrderStateTextView.setText(R.string.order_state_finished);
								break;
							case reject:
								mOrderStateTextView.setText(R.string.order_state_rejected);
								break;
						}
						mApplianceTypeTextView.setText(mOrder.applianceType());
						mDetailDescripTextView.setText(mOrder.detail());
						mOrderSubmitDateTextView.setText(mOrder.createDate());
						mOrderStartRepairDateTextView.setText(mOrder.startRepairDate());
						mOrderEndRepairDateTextView.setText(mOrder.endRepairDate());
						mOrderFinishDateTextView.setText(mOrder.finishDate());
						mOrderRejectDateTextView.setText(mOrder.rejectDate());
						mOrderPayDateTextView.setText(mOrder.finishDate());
					}
				});
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		private Order getOrder(String json) {
			Order order = new Order();
			try {
				JSONObject jsonObject = new JSONObject(json);
				order.setId(jsonObject.getLong("id"));
				order.setApplianceType(jsonObject.getString("appliance_type"));

				order.setDetail(jsonObject.getString("detail"));
				String orderState = jsonObject.getString("state");
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

				order.setCreateDate(jsonObject.getString("create_date"));
				order.setReceivedDate(jsonObject.getString("received_date"));
				order.setStartRepairDate(jsonObject.getString("start_repair_date"));
				order.setEndRepairDate(jsonObject.getString("end_repair_date"));
				order.setFinishDate(jsonObject.getString("finish_date"));
				order.setRejectDate(jsonObject.getString("reject_date"));
			} catch (JSONException je) {
				je.printStackTrace();
			}

			return order;
		}
	}
}
