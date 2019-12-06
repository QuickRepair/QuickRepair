package com.quickrepair.customer;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
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

import com.quickrepair.customer.Database.AccountDatabase;
import com.quickrepair.customer.Database.OrderDatabase;
import com.quickrepair.customer.Database.OrderItem;
import com.quickrepair.customer.Models.AccountRepository;
import com.quickrepair.customer.Models.AccountViewModel;
import com.quickrepair.customer.Models.LoginAccount;
import com.quickrepair.customer.Models.OrderRepository;
import com.quickrepair.customer.Models.OrderViewModel;
import com.quickrepair.customer.Widgets.WaitLoadingFragment;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailFragment extends Fragment {

	private static final String ARG_ORDER = "order";
	private static final String DIALOG_WAIT_UPDATE_ORDER = "wait_update_order";

	private Long id;

	private FragmentManager fm;
	private WaitLoadingFragment mWaitUpdateOrderFragment;

	private OrderViewModel orderViewModel;
	private AccountViewModel accountViewModel;

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

	private Button mStartRepairButtons;

	private LinearLayout mRepairingStateButtons;
	private EditText mTransactionEditText;
	private Button mEndRepairButton;

	private Button mPayButton;

	private ViewGroupStates states;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		orderViewModel = ViewModelProviders.of(this, new OrderViewModel.OrderViewModelFactory(
				new OrderRepository(OrderDatabase.getInstance(getContext()).orderDao())
		)).get(OrderViewModel.class);
		accountViewModel = ViewModelProviders.of(this, new AccountViewModel.AccountViewModelFactory(
				new AccountRepository(AccountDatabase.getInstance(getContext()).AccountDao())
		)).get(AccountViewModel.class);

		id = (Long)getArguments().getSerializable(ARG_ORDER);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_order_detail, container, false);

		TextView title = v.findViewById(R.id.page_name);
		title.setText(R.string.title_order_detail);

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

		states = new ViewGroupStates();
		mUnreceivedStateButtons = v.findViewById(R.id.unreceivedLinearLayout);
		mAcceptButton = v.findViewById(R.id.acceptOrderButton);
		mRejectButton = v.findViewById(R.id.rejectOrderButton);
		states.getUnreceived().add(mUnreceivedStateButtons);
		states.getUnreceived().add(mAcceptButton);
		states.getUnreceived().add(mRejectButton);

		mStartRepairButtons = v.findViewById(R.id.startRepairButton);
		states.getReceived().add(mStartRepairButtons);

		mRepairingStateButtons = v.findViewById(R.id.repairingLineraLayout);
		mTransactionEditText = v.findViewById(R.id.transactionEditText);
		mEndRepairButton = v.findViewById(R.id.endRepairButton);
		states.getRepairing().add(mRepairingStateButtons);
		states.getRepairing().add(mTransactionEditText);
		states.getRepairing().add(mEndRepairButton);

		mPayButton = v.findViewById(R.id.payButton);
		states.getPaying().add(mPayButton);

		orderViewModel.getOrder(LoginAccount.getInstance(getContext()).getAccount(), id).observe(this, new Observer<OrderItem>() {
			@Override
			public void onChanged(@Nullable OrderItem orderItem) {
				 states.at(OrderItem.State.values()[orderItem.getOrderState()]);
			}
		});
		/*fm = getFragmentManager();
		mWaitUpdateOrderFragment = WaitLoadingFragment.newInstance();*/

		mAcceptButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mWaitUpdateOrderFragment.show(fm, DIALOG_WAIT_UPDATE_ORDER);
				orderViewModel.orderStateUpdate(LoginAccount.getInstance(getContext()).getToken(), OrderItem.State.received);
//				new AsyncConnection(getContext()).receiveOrder(
//						mOrder,
//						AccountViewModel.getInstance().getAccount(),
//						AccountViewModel.getInstance().getToken(),
//						new AcceptOrderListener()
//				);
			}
		});

		mRejectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mWaitUpdateOrderFragment.show(fm, DIALOG_WAIT_UPDATE_ORDER);
				orderViewModel.orderStateUpdate(LoginAccount.getInstance(getContext()).getToken(), OrderItem.State.reject);
//				new AsyncConnection(getContext()).rejectOrder(
//						mOrder,
//						AccountViewModel.getInstance().getAccount(),
//						AccountViewModel.getInstance().getToken(),
//						new RejectOrderListener()
//				);
			}
		});

		mStartRepairButtons.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mWaitUpdateOrderFragment.show(fm, DIALOG_WAIT_UPDATE_ORDER);
				orderViewModel.orderStateUpdate(LoginAccount.getInstance(getContext()).getToken(), OrderItem.State.repairing);
//				new AsyncConnection(getContext()).startRepairOrder(
//						mOrder,
//						AccountViewModel.getInstance().getAccount(),
//						AccountViewModel.getInstance().getToken(),
//						new StartRepairOrderListener()
//				);
			}
		});

		mEndRepairButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mWaitUpdateOrderFragment.show(fm, DIALOG_WAIT_UPDATE_ORDER);
				orderViewModel.orderStateUpdate(LoginAccount.getInstance(getContext()).getToken(), OrderItem.State.paying);
//				new AsyncConnection(getContext()).endRepairOrder(
//						mOrder,
//						AccountViewModel.getInstance().getAccount(),
//						AccountViewModel.getInstance().getToken(),
//						new EndRepairOrderListener()
//				);
			}
		});

		mPayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "pay", Toast.LENGTH_SHORT).show();
				orderViewModel.orderStateUpdate(LoginAccount.getInstance(getContext()).getToken(), OrderItem.State.finished);
			}
		});

//		new AsyncConnection(getContext()).getOrderDetail(
//				mOrder,
//				AccountViewModel.getInstance().getToken(),
//				new GetOrderDetailListener()
//		);

		return v;
	}

	public static OrderDetailFragment newInstance(Long orderId) {
		Bundle args = new Bundle();
		args.putSerializable(ARG_ORDER, orderId);
		OrderDetailFragment fragment = new OrderDetailFragment();
		fragment.setArguments(args);

		return fragment;
	}

	private class OrderStatesViewGroups {
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

	private class ViewGroupStates {
		private OrderStatesViewGroups mUnreceived;
		private OrderStatesViewGroups mReceived;
		private OrderStatesViewGroups mRepairing;
		private OrderStatesViewGroups mPaying;

		ViewGroupStates() {
			mUnreceived = new OrderStatesViewGroups();
			mReceived = new OrderStatesViewGroups();
			mRepairing = new OrderStatesViewGroups();
			mPaying = new OrderStatesViewGroups();
		}

		public OrderStatesViewGroups getUnreceived() {
			return mUnreceived;
		}

		public OrderStatesViewGroups getReceived() {
			return mReceived;
		}

		public OrderStatesViewGroups getRepairing() {
			return mRepairing;
		}

		public OrderStatesViewGroups getPaying() {
			return mPaying;
		}

		public void at(OrderItem.State state)
		{
			mUnreceived.setInvisible();
			mReceived.setInvisible();
			mRepairing.setInvisible();
			mPaying.setInvisible();
			switch (state) {
				case unreceived:
					mUnreceived.setVisible();
					break;
				case received:
					mReceived.setVisible();
					break;
				case repairing:
					mRepairing.setVisible();
					break;
				case paying:
					mPaying.setVisible();
					break;
				case finished:
					break;
				case reject:
					break;
			}
		}
	}

	/*private class GetOrderDetailListener implements AsyncConnection.onResponseListener {
		@Override
		public void onResponse(Response response) {
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
								if(AccountViewModel.getInstance().getAccount().getAccountType() == Account.Type.customer)
									mUnreceived.setInvisible();
								break;
							case received:
								mOrderStateTextView.setText(R.string.order_state_received);
								mUnreceived.setInvisible();
								mReceived.setVisible();
								mRepairing.setInvisible();
								mPaying.setInvisible();
								if(AccountViewModel.getInstance().getAccount().getAccountType() == Account.Type.customer)
									mReceived.setInvisible();
								break;
							case repairing:
								mOrderStateTextView.setText(R.string.order_state_repairing);
								mUnreceived.setInvisible();
								mReceived.setInvisible();
								mRepairing.setVisible();
								mPaying.setInvisible();
								if(AccountViewModel.getInstance().getAccount().getAccountType() == Account.Type.customer)
									mRepairing.setInvisible();
								break;
							case paying:
								mOrderStateTextView.setText(R.string.order_state_paying);
								mUnreceived.setInvisible();
								mReceived.setInvisible();
								mRepairing.setInvisible();
								mPaying.setVisible();
								if(AccountViewModel.getInstance().getAccount().getAccountType() == Account.Type.merchant)
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

		private OrderItem getOrder(String json) {
			OrderItem order = new OrderItem();
			try {
				JSONObject jsonObject = new JSONObject(json);
				order.setId(jsonObject.getLong("id"));
				order.setApplianceType(jsonObject.getString("appliance_type"));

				order.setDetail(jsonObject.getString("detail"));
				String orderState = jsonObject.getString("state");
				if(orderState.equalsIgnoreCase("unreceived"))
					order.setOrderState(OrderItem.State.unreceived);
				else if(orderState.equalsIgnoreCase("received"))
					order.setOrderState(OrderItem.State.received);
				else if(orderState.equalsIgnoreCase("repairing"))
					order.setOrderState(OrderItem.State.repairing);
				else if(orderState.equalsIgnoreCase("paying"))
					order.setOrderState(OrderItem.State.paying);
				else if(orderState.equalsIgnoreCase("finished"))
					order.setOrderState(OrderItem.State.finished);
				else if(orderState.equalsIgnoreCase("reject"))
					order.setOrderState(OrderItem.State.reject);

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

	private class AcceptOrderListener implements AsyncConnection.onResponseListener {
		@Override
		public void onResponse(Response response) {
			mMainHandler.post(new Runnable() {
				@Override
				public void run() {
					mWaitUpdateOrderFragment.dismiss();
					new AsyncConnection(getContext()).getOrderDetail(
							mOrder,
							AccountViewModel.getInstance().getToken(),
							new GetOrderDetailListener()
					);
				}
			});
		}
	}

	private class RejectOrderListener implements AsyncConnection.onResponseListener {
		@Override
		public void onResponse(Response response) {
			mMainHandler.post(new Runnable() {
				@Override
				public void run() {
					mWaitUpdateOrderFragment.dismiss();
					new AsyncConnection(getContext()).getOrderDetail(
							mOrder,
							AccountViewModel.getInstance().getToken(),
							new GetOrderDetailListener()
					);
				}
			});
		}
	}

	private class StartRepairOrderListener implements AsyncConnection.onResponseListener {
		@Override
		public void onResponse(Response response) {
			mMainHandler.post(new Runnable() {
				@Override
				public void run() {
					mWaitUpdateOrderFragment.dismiss();
					new AsyncConnection(getContext()).getOrderDetail(
							mOrder,
							AccountViewModel.getInstance().getToken(),
							new GetOrderDetailListener()
					);
				}
			});
		}
	}

	private class EndRepairOrderListener implements AsyncConnection.onResponseListener {
		@Override
		public void onResponse(Response response) {
			mMainHandler.post(new Runnable() {
				@Override
				public void run() {
					mWaitUpdateOrderFragment.dismiss();
					new AsyncConnection(getContext()).getOrderDetail(
							mOrder,
							AccountViewModel.getInstance().getToken(),
							new GetOrderDetailListener()
					);
				}
			});
		}
	}*/
}
