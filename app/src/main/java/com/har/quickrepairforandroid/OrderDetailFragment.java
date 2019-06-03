package com.har.quickrepairforandroid;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.har.quickrepairforandroid.AsyncTransmissions.AsyncHttpTask;
import com.har.quickrepairforandroid.AsyncTransmissions.HttpConnection;
import com.har.quickrepairforandroid.Models.AccountHolder;
import com.har.quickrepairforandroid.Models.Order;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailFragment extends Fragment {

	private static final String ARG_ORDER_ID = "order_id";
	private static final String DIALOG_WAIT_UPDATE_ORDER = "wait_update_order";

	private Handler mMainHandler;
	private FragmentManager fm;
	private WaitLoadingFragment mWaitUpdateOrderFragment;

	private long mOrderId;
	private Order mOrder;

	private LinearLayout mLayout;
	private TextView mOrderStateTextView;
	private TextView mApplianceTypeTextView;
	private TextView mDetailDescripTextView;
	private TextView mOrderSubmitDateTextView;
	private TextView mOrderStartRepairDateTextView;
	private TextView mOrderEndRepairDateTextView;
	private TextView mOrderFinishDateTextView;
	private TextView mOrderRejectDateTextView;
	private TextView mOrderPayDateTextView;

	private LinearLayout mUnreceivedStateButtons;
	private Button mAcceptButton;
	private Button mRejectButton;
	private OrderSpecificViewGroups mUnreceived;

	private Button mStartRepairButtons;
	private OrderSpecificViewGroups mReceived;

	private LinearLayout mRepairingStateButtons;
	private EditText mTransactionEditText;
	private Button mEndRepairButton;
	private OrderSpecificViewGroups mRepairing;

	private Button mPayButton;
	private OrderSpecificViewGroups mPaying;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mOrderId = (long)getArguments().getSerializable(ARG_ORDER_ID);
		mMainHandler = new Handler();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_order_detail, container, false);

		mLayout = v.findViewById(R.id.order_detail_layout);
		mOrderStateTextView = v.findViewById(R.id.orderStateTextView);
		mApplianceTypeTextView = v.findViewById(R.id.applianceTypeTextView);
		mDetailDescripTextView = v.findViewById(R.id.detailDescripTextView);
		mOrderSubmitDateTextView = v.findViewById(R.id.orderSubmitDateTextView);
		mOrderStartRepairDateTextView = v.findViewById(R.id.orderStartRepairDateTextView);
		mOrderEndRepairDateTextView = v.findViewById(R.id.orderEndRepairDateTextView);
		mOrderFinishDateTextView = v.findViewById(R.id.orderFinishDateTextView);
		mOrderRejectDateTextView = v.findViewById(R.id.orderRejectDateTextView);
		mOrderPayDateTextView = v.findViewById(R.id.orderPayDateTextView);

		mUnreceivedStateButtons = v.findViewById(R.id.unreceivedLinearLayout);
		mAcceptButton = v.findViewById(R.id.acceptOrderButton);
		mRejectButton = v.findViewById(R.id.rejectOrderButton);
		mUnreceived = new OrderSpecificViewGroups();
		mUnreceived.add(mUnreceivedStateButtons);
		mUnreceived.add(mAcceptButton);
		mUnreceived.add(mRejectButton);

		mStartRepairButtons = v.findViewById(R.id.startRepairButton);
		mReceived = new OrderSpecificViewGroups();
		mReceived.add(mStartRepairButtons);

		mRepairingStateButtons = v.findViewById(R.id.repairingLineraLayout);
		mTransactionEditText = v.findViewById(R.id.transactionEditText);
		mEndRepairButton = v.findViewById(R.id.endRepairButton);
		mRepairing = new OrderSpecificViewGroups();
		mRepairing.add(mRepairingStateButtons);
		mRepairing.add(mTransactionEditText);
		mRepairing.add(mEndRepairButton);

		mPayButton = v.findViewById(R.id.payButton);
		mPaying = new OrderSpecificViewGroups();
		mPaying.add(mPayButton);

		fm = getFragmentManager();
		mWaitUpdateOrderFragment = WaitLoadingFragment.newInstance();

		mAcceptButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mWaitUpdateOrderFragment.show(fm, DIALOG_WAIT_UPDATE_ORDER);
				new AcceptOrderTask().execute();
			}
		});

		mRejectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mWaitUpdateOrderFragment.show(fm, DIALOG_WAIT_UPDATE_ORDER);
				new RejectOrderTask().execute();
			}
		});

		mStartRepairButtons.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mWaitUpdateOrderFragment.show(fm, DIALOG_WAIT_UPDATE_ORDER);
				new StartRepairOrderTask().execute();
			}
		});

		mEndRepairButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mWaitUpdateOrderFragment.show(fm, DIALOG_WAIT_UPDATE_ORDER);
				new EndRepairOrderTask().execute();
			}
		});

		mPayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "pay", Toast.LENGTH_SHORT).show();
			}
		});

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

	private class OrderSpecificViewGroups {
		private List<View> mViewList = new ArrayList<>();

		public void add(View v) {
			mViewList.add(v);
		}

		public void setVisible() {
			for (View v : mViewList) {
				v.setVisibility(View.VISIBLE);
			}
		}

		public void setInvisible() {
			for(View v : mViewList) {
				v.setVisibility(View.GONE);
			}
		}
	}

	private class GetOrderDetailTask implements AsyncHttpTask {
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
								mUnreceived.setVisible();
								mReceived.setInvisible();
								mRepairing.setInvisible();
								mPaying.setInvisible();
								if(AccountHolder.getInstance().getIsCustomer())
									mUnreceived.setInvisible();
								break;
							case received:
								mOrderStateTextView.setText(R.string.order_state_received);
								mUnreceived.setInvisible();
								mReceived.setVisible();
								mRepairing.setInvisible();
								mPaying.setInvisible();
								if(AccountHolder.getInstance().getIsCustomer())
									mReceived.setInvisible();
								break;
							case repairing:
								mOrderStateTextView.setText(R.string.order_state_repairing);
								mUnreceived.setInvisible();
								mReceived.setInvisible();
								mRepairing.setVisible();
								mPaying.setInvisible();
								if(AccountHolder.getInstance().getIsCustomer())
									mRepairing.setInvisible();
								break;
							case paying:
								mOrderStateTextView.setText(R.string.order_state_paying);
								mUnreceived.setInvisible();
								mReceived.setInvisible();
								mRepairing.setInvisible();
								mPaying.setVisible();
								if(!AccountHolder.getInstance().getIsCustomer())
									mPaying.setInvisible();
								break;
							case finished:
								mOrderStateTextView.setText(R.string.order_state_finished);
								mUnreceived.setInvisible();
								mReceived.setInvisible();
								mRepairing.setInvisible();
								mPaying.setInvisible();
								break;
							case reject:
								mOrderStateTextView.setText(R.string.order_state_rejected);
								mUnreceived.setInvisible();
								mReceived.setInvisible();
								mRepairing.setInvisible();
								mPaying.setInvisible();
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
				mLayout.setVisibility(View.VISIBLE);
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

	private class AcceptOrderTask implements AsyncHttpTask {
		@Override
		public void execute() {
			HttpConnection.getInstance().getMethod(makeRequest(), this);
		}

		@Override
		public Request makeRequest() {
			HttpUrl url = HttpUrl
					.parse(getContext().getResources().getString(R.string.server_ip))
					.newBuilder()
					.addQueryParameter("type", "update_order")
					.addQueryParameter("account", AccountHolder.getInstance().getAccount())
					.addQueryParameter("order_id", String.valueOf(mOrderId))
					.addQueryParameter("order_operate", "accept")
					.build();
			return new Request.Builder().url(url).build();
		}

		@Override
		public void handler(Response response) {
			mMainHandler.post(new Runnable() {
				@Override
				public void run() {
					mWaitUpdateOrderFragment.dismiss();
					new GetOrderDetailTask().execute();
				}
			});
		}
	}

	private class RejectOrderTask implements AsyncHttpTask {
		@Override
		public void execute() {
			HttpConnection.getInstance().getMethod(makeRequest(), this);
		}

		@Override
		public Request makeRequest() {
			HttpUrl url = HttpUrl
					.parse(getContext().getResources().getString(R.string.server_ip))
					.newBuilder()
					.addQueryParameter("type", "update_order")
					.addQueryParameter("account", AccountHolder.getInstance().getAccount())
					.addQueryParameter("order_id", String.valueOf(mOrderId))
					.addQueryParameter("order_operate", "reject")
					.build();
			return new Request.Builder().url(url).build();
		}

		@Override
		public void handler(Response response) {
			mMainHandler.post(new Runnable() {
				@Override
				public void run() {
					mWaitUpdateOrderFragment.dismiss();
					new GetOrderDetailTask().execute();
				}
			});
		}
	}

	private class StartRepairOrderTask implements AsyncHttpTask {
		@Override
		public void execute() {
			HttpConnection.getInstance().getMethod(makeRequest(), this);
		}

		@Override
		public Request makeRequest() {
			HttpUrl url = HttpUrl
					.parse(getContext().getResources().getString(R.string.server_ip))
					.newBuilder()
					.addQueryParameter("type", "update_order")
					.addQueryParameter("account", AccountHolder.getInstance().getAccount())
					.addQueryParameter("order_id", String.valueOf(mOrderId))
					.addQueryParameter("order_operate", "start_repair")
					.build();
			return new Request.Builder().url(url).build();
		}

		@Override
		public void handler(Response response) {
			mMainHandler.post(new Runnable() {
				@Override
				public void run() {
					mWaitUpdateOrderFragment.dismiss();
					new GetOrderDetailTask().execute();
				}
			});
		}
	}

	private class EndRepairOrderTask implements AsyncHttpTask {
		@Override
		public void execute() {
			HttpConnection.getInstance().getMethod(makeRequest(), this);
		}

		@Override
		public Request makeRequest() {
			HttpUrl url = HttpUrl
					.parse(getContext().getResources().getString(R.string.server_ip))
					.newBuilder()
					.addQueryParameter("type", "update_order")
					.addQueryParameter("account", AccountHolder.getInstance().getAccount())
					.addQueryParameter("order_id", String.valueOf(mOrderId))
					.addQueryParameter("order_operate", "end_repair")
					.addQueryParameter("transaction", mTransactionEditText.getText().toString())
					.build();
			return new Request.Builder().url(url).build();
		}

		@Override
		public void handler(Response response) {
			mMainHandler.post(new Runnable() {
				@Override
				public void run() {
					mWaitUpdateOrderFragment.dismiss();
					new GetOrderDetailTask().execute();
				}
			});
		}
	}
}
